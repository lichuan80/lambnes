package com.lambelly.lambnes.platform.ppu;

import com.lambelly.lambnes.gui.*;
import com.lambelly.lambnes.platform.*;
import com.lambelly.lambnes.platform.interrupts.InterruptRequest;
import com.lambelly.lambnes.platform.ppu.registers.PPUControlRegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUMaskRegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUSprRamAddressRegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUSprRamIORegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUSpriteDMARegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUStatusRegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUScrollRegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUVramAddressRegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUVramIORegister;

import java.awt.*;  
import java.awt.image.*; 
import org.apache.log4j.*;

public class NesPpu implements PictureProcessingUnit
{
	private static final int VBLANK_SCANLINE_START = 240;
	private static final int VBLANK_SCANLINE_END = 262;
	private static final int REFRESH_RATE = 60;
	private static final int NUM_SCANLINES_PER_FRAME = 262;
	private static final int NUM_HORIZONTAL_TILES = 32;
	private static final int NUM_VERTICAL_TILES = 30;
	public static final int SPRITE_COUNT = 64;
	private static final double NUM_CYCLES_PER_SCANLINE = (Platform.CPU_FREQUENCY / NesPpu.REFRESH_RATE) / NesPpu.NUM_SCANLINES_PER_FRAME;
	private PPUControlRegister ppuControlRegister = PPUControlRegister.getRegister();
	private PPUMaskRegister ppuMaskRegister = PPUMaskRegister.getRegister();
	private PPUStatusRegister ppuStatusRegister = PPUStatusRegister.getRegister();
	private PPUSpriteDMARegister ppuSpriteDMARegister = PPUSpriteDMARegister.getRegister();
	private PPUSprRamAddressRegister ppuSprRamAddressRegister = PPUSprRamAddressRegister.getRegister();
	private PPUSprRamIORegister ppuSprRamIORegister = PPUSprRamIORegister.getRegister();
	private PPUScrollRegister ppuScrollRegister = PPUScrollRegister.getRegister();
	private PPUVramAddressRegister ppuVramAddressRegister = PPUVramAddressRegister.getRegister();
	private PPUVramIORegister ppuVramIORegister = PPUVramIORegister.getRegister();
	private int scanlineCount = 0;
	private int horizontalPerPixelCount = 0;
	private int horizontalPerTileCount = 0;
	private int horizontalNameCount = 0;
	private int verticalPerTileCount = 0;
	private int verticalPerPixelCount = 0;
	private int verticalNameCount = 0;     
	private int registerAddressFlipFlopLatch = 0;
	private int loopyT = 0;
	private int loopyV = 0;
	private int loopyX = 0;
    private ScreenBuffer screenBuffer = new ScreenBuffer();
    private int screenBuffersWrittenToScreen = 0; 
	private Logger logger = Logger.getLogger(NesPpu.class);
	
	public void cycle(int cycleCount)
	{	
		Platform.getPpu().doRegisterReadsWrites();
		
    	if (cycleCount % NesPpu.NUM_CYCLES_PER_SCANLINE == 0)
    	{	 
    		//logger.info("cycleCount: " + cycleCount + " scanline: " + this.getScanlineCount() + " status: " + this.getPpuStatusRegister() + " nametable control: " + this.getPpuControlRegister().getNameTableAddress() + " scroll: " + this.getPpuScrollRegister() + " mask: " + this.getPpuMaskRegister() + " loopyv: " + this.getLoopyV() + " loopyT: " + this.getLoopyT());
    		// during vblank
    		if (getScanlineCount() >= NesPpu.VBLANK_SCANLINE_START && getScanlineCount() <= NesPpu.VBLANK_SCANLINE_END)
    		{
    			
    			// vblank start
    			if (getScanlineCount() == NesPpu.VBLANK_SCANLINE_START)
    			{
        			// vblank
        			Platform.getPpu().getPpuStatusRegister().setVblank(true);

        			this.getScreenBuffer().pushBufferToScreen();
        			this.incrementScreenBuffersWrittenToScreen();
        			
        			if (this.getPpuControlRegister().isExecuteNMIOnVBlank())
        			{
        				Platform.getNesInterrupts().addInterruptRequestToQueue(new InterruptRequest(InterruptRequest.interruptTypeNMI));
        			}
        			
        			this.setLoopyV(this.getLoopyT());
    			}
    		}
    		// vblank end
    		else if (getScanlineCount() >= NesPpu.VBLANK_SCANLINE_END)
    		{
    			// reset scanlineCount (new frame)
    			this.getPpuStatusRegister().setSprite0Occurance(false);
    			this.getPpuStatusRegister().setVblank(false);
    			this.getPpuStatusRegister().setScanlineSpriteCount(false);
    			setScanlineCount(0);
    			this.setLoopyV(this.getLoopyT());
    			
                //update loopyv hscroll
    			//int v = this.getLoopyT() & 0x41f;
    			//v |= this.getLoopyV();
                //this.setLoopyV(v);
    		}
    		else
    		{
    			// draw scanline
    			this.drawScanline(this.getScanlineCount());
    		}
    		
    		incrementScanlineCount();
    	}
	}
	
	private void drawScanline(int scanline)
	{			
		// draw background
		if (this.getPpuMaskRegister().isBackgroundVisibility())
		{
			this.drawBackground(scanline);
		}
		
        // update and draw sprite buffer 
        if (this.getPpuMaskRegister().isSpriteVisibility())
        {
        	// use sprites currently in buffer
            drawSprites(scanline);
            
            // clear buffer
            Platform.getPpuMemory().clearSpriteBuffer();
            
            // fill buffer for next scanline.
            updateSpriteBuffer(scanline);
        }
        
        // tile row written logic -- executes after all tiles for a given row have been written.
        if (((scanline + 1) & 7) == 0)
        {
            this.incrementVerticalPerTileCount();

            if (this.getVerticalPerTileCount() == NesPpu.NUM_VERTICAL_TILES)
            {
                //this.flipVerticalNameCount();
                this.setVerticalPerTileCount(0);
            }
        }         
	}
	
    private void drawSprites(int verticalPerPixelCount)
    {
    	if (logger.isDebugEnabled())
    	{
	    	logger.debug("using pattern memory for sprite tiles: " + (Platform.getPpu().getPpuControlRegister().getSpritePatternTableAddress() == PPUControlRegister.SPRITE_PATTERN_TABLE_ADDRESS_1000 ? 1000 : 0));
	    	logger.debug("previousSpriteCount: " + Platform.getPpuMemory().getSpritesInBufferCount());
	    	logger.debug("vline: " + verticalPerPixelCount + " sprite buffer size: " + Platform.getPpuMemory().getSpritesInBufferCount());
    	}
        if (Platform.getPpuMemory().getSpritesInBufferCount() > 0)
        {
            for (int i = 0 ; i < Platform.getPpuMemory().getSpritesInBufferCount(); i++)
            {
                SpriteTile sprite = Platform.getPpuMemory().getSpriteFromBuffer(i);
                
                if (drawSpriteTileLine(sprite,verticalPerPixelCount) && (!this.getPpuStatusRegister().isSprite0Occurance()))
                {
                	if (logger.isDebugEnabled())
                	{
                		logger.info("sprite0 true: " + this.getScanlineCount());
                	}
                    this.getPpuStatusRegister().setSprite0Occurance(true);                        
                }
            }
        }
    }
	
	private boolean drawSpriteTileLine(SpriteTile sprite, int verticalPerPixelCount)
	{
		boolean sprite0Triggered = false;
        int spriteLine = verticalPerPixelCount - sprite.getSpriteAttributes().getyCoordinate();
        
        if (logger.isDebugEnabled())
        {
        	logger.debug("drawing sprite " + sprite.getSpriteNumber() + " at " + verticalPerPixelCount);
        	logger.debug("spriteLine: " + spriteLine + " sprite: " + sprite.getSpriteNumber() + " scanline: " + verticalPerPixelCount + " y coord: " + sprite.getSpriteAttributes());
        	logger.debug("sprite attributes for sprite: " + sprite.getSpriteAttributes().getTileIndex() + "\n" + sprite.getSpriteAttributes());
        }
        
        //TODO -- 8x16 logics
        /** unused for the moment. 
        if (!(this.getPpuControlRegister1().getSpriteSize() == PPUControlRegister1.SPRITE_SIZE_8X16))
        { 
        	// 8x8 tiles
        	if (this.getPpuControlRegister1().getSpritePatternTableAddress() == PPUControlRegister1.SPRITE_PATTERN_TABLE_ADDRESS_0000)
        	{
        		spritePatternAddress = 0x0;
        	}
        	else
        	{
        		spritePatternAddress = 0x1000;
        	}
        }
        else 
        { 
        	// 8x16 tiles
            /**
             * Basically the tile index tells us which pattern table it comes from
             * And then, apparently, we take the MSB of the offset ("range") and use it
             * as the LSB of the tile
             
            if ((sprite.getSpriteNumber() & 0x1) == 0)
            {
                spritePatternAddress = 0x0000;
            }
            else 
            {
                spritePatternAddress = 0x1000;
            }

            tileNum = (sprite.getSpriteNumber() & 0xFE) | (spriteLine >> 3);
        }
		*/
        
        // get one sprite line and draw it pixel by pixel
        spriteLine = spriteLine & 7; // if more than the index, roll over
        int spriteXPosition = sprite.getSpriteAttributes().getxCoordinate();
    	int sprite0Number = Platform.getPpuMemory().getSprRam()[1];
        
        // TODO -- should this be the way that transparentColor is determined? It probably should be part of an object so it can be reused.
        PaletteColor spriteTransparentColor = new PaletteColor(0, PaletteColor.PALETTE_TYPE_SPRITE);
        PaletteColor backgroundTransparentColor =  new PaletteColor(0, PaletteColor.PALETTE_TYPE_BACKGROUND);
        
        for (PaletteColor pixel : sprite.getTileColorRow(spriteLine))
        {	
        	// check sprite visibility
        	if (pixel.getPaletteIndex() != spriteTransparentColor.getPaletteIndex())                  
	        {   
        		if (logger.isDebugEnabled())
        		{
        			logger.debug("sprite 0 logic: sprite visible");
        		}
        		// check background visibility
        		if (LambNesGui.getScreen().getImage().getRGB((spriteXPosition & Platform.EIGHT_BIT_MASK), verticalPerPixelCount) != backgroundTransparentColor.getMasterPaletteColor().getColorInt())
        		{
        			if (logger.isDebugEnabled())
        			{
        				logger.debug("sprite 0 logic: background visible");
        			}
	        	     // sprite 0 logic
		        	if (sprite.getSpriteNumber() == sprite0Number)                   
		        	{                          
		        		sprite0Triggered = true;
		        		if (logger.isDebugEnabled())
		        		{
		        			logger.debug("sprite 0 logic: sprite0Triggered: " + sprite0Triggered + " spriteNumber: " + sprite.getSpriteNumber() + " sprite0 number: " + sprite0Number);
		        		}
		        	}
	        	}   
        		else
        		{
        			// background invisible
        		}
        		
            	// draw pixel
            	if (spriteXPosition >= 8 || this.getPpuMaskRegister().isSpriteVisibility())                      
            	{
                    // logger.info("drawing pixel of line " + spriteLine + " of sprite number: " + sprite.getSpriteNumber() +  " index: " + sprite.getSpriteAttributes().getTileIndex() + " at y: " + verticalPerPixelCount + " x: " + (spriteXPosition & Platform.EIGHT_BIT_MASK) + " sprite0: " + this.getPpuStatusRegister().isSprite0Occurance());
            		this.getScreenBuffer().setScreenBufferPixel((spriteXPosition & Platform.EIGHT_BIT_MASK),verticalPerPixelCount,pixel.getMasterPaletteColor().getColorInt());                      
            	}	
	        }	        	
        	
	        spriteXPosition++;
        }

        return sprite0Triggered;		
	}
	
    private void updateSpriteBuffer(int verticalPerPixelCount)
    {
    	// reset sprite overflow status
        this.getPpuStatusRegister().setScanlineSpriteCount(false);
        
        int spriteCount = 0;
        for (int spriteNumber = 0; spriteNumber < SPRITE_COUNT; spriteNumber++)
        {
        	// inspect each sprite attribute
        	SpriteAttribute spriteAttribute = Platform.getPpuMemory().getSpriteAttribute(spriteNumber);
        	
        	if (logger.isDebugEnabled())
        	{
	        	logger.info("scanline: " + verticalPerPixelCount + " tileIndex: " + spriteAttribute.getTileIndex() + " spriteYcoordinate: " + spriteAttribute.getyCoordinate());
        	}
        	
            //   determine if Y coord is being drawn (for line + 1) 
            int diff = ((verticalPerPixelCount + 1) - spriteAttribute.getyCoordinate());
            
            if ((diff >= 0 && diff <= 7) || ((this.getPpuControlRegister().getSpriteSize() == PPUControlRegister.SPRITE_SIZE_8X16) && (diff >= 0 && diff <= 15)))
            {
            	if (logger.isDebugEnabled())
            	{
            		logger.info("adding sprite to buffer: scanline: " + verticalPerPixelCount + " tileIndex: " + spriteAttribute.getTileIndex() + " spriteYcoordinate: " + spriteAttribute.getyCoordinate() + " diff: " + diff + " spritecount: " + spriteCount);
            	}
            	SpriteTile currentSprite = new SpriteTile(NesTileCache.getSpriteTile(spriteAttribute.getTileIndex()));
            	currentSprite.setSpriteAttributes(spriteAttribute);
            	Platform.getPpuMemory().setSpriteToBuffer(spriteCount, currentSprite);
                spriteCount++;
                
                if (spriteCount == 9 )
                {
                	if (logger.isDebugEnabled())
                	{
                		logger.debug("sprite overflow");
                	}
                    spriteCount = 8;
                    this.getPpuStatusRegister().setScanlineSpriteCount(true);
                    break;
                }
            }
        }   
    }
    
    private void drawBackground(int verticalPerPixelCount)
    {
    	// load nametable control
    	if (this.getPpuControlRegister().getNameTableAddress() == PPUControlRegister.NAME_TABLE_ADDRESS_2000)
    	{
    		this.setHorizontalNameCount(0);
    		this.setVerticalNameCount(0);
    	}
    	else if (this.getPpuControlRegister().getNameTableAddress() == PPUControlRegister.NAME_TABLE_ADDRESS_2400)
    	{
    		this.setHorizontalNameCount(1);
    		this.setVerticalNameCount(0);
    	}
    	else if (this.getPpuControlRegister().getNameTableAddress() == PPUControlRegister.NAME_TABLE_ADDRESS_2800)
    	{
    		this.setHorizontalNameCount(0);
    		this.setVerticalNameCount(1);
    	}
    	else if (this.getPpuControlRegister().getNameTableAddress() == PPUControlRegister.NAME_TABLE_ADDRESS_2C00)
    	{
    		this.setHorizontalNameCount(1);
    		this.setVerticalNameCount(1);
    	}
    	
		// iterate one line in name table
		for (int nameTableEntry = 0; nameTableEntry < NesPpu.NUM_HORIZONTAL_TILES; nameTableEntry++)
		{
			int hScrollOffset = (this.getLoopyV() & 0x1F);
			int offsetHorizontalPerTileCount = this.getHorizontalPerTileCount() + hScrollOffset;
			
			// see if hscroll is outside nametable, if so flip horiz
			if (offsetHorizontalPerTileCount > NesPpu.NUM_HORIZONTAL_TILES)
			{
				if (loopyV > 0) { logger.info("looping"); }
				// flip horiz if not already flipped
				//if (this.getHorizontalNameCount() == 0)
				//{
					this.flipHorizontalNameCount();
				//}
				
				// circle nameTable 
				offsetHorizontalPerTileCount &= 0x1F;
			}
			
			//essentially creates an integer that represents the offset from start of name table (0x2000).
			int nameTableAddress = 0x2000 + offsetHorizontalPerTileCount | (this.getVerticalPerTileCount() << 5) | (this.getHorizontalNameCount() << 10) | (this.getVerticalNameCount() << 11); 
			
			//if (logger.isDebugEnabled())
			{
				if (loopyV > 0)
				{
					logger.info("loopyV: " + loopyV);
					logger.info("hscroll offset: " + hScrollOffset);
					logger.info("offsetHorizontalPerTileCount: " + offsetHorizontalPerTileCount);
					logger.info("horizontalPerTileCount: " + this.getHorizontalPerTileCount());
					logger.info("verticalPerTileCount: " + this.getVerticalPerTileCount());
					logger.info("horizontalNameCount: " + this.getHorizontalNameCount());
					logger.info("verticalNameCount: " + this.getVerticalNameCount());
					logger.info("scanline: " + this.getScanlineCount());
					logger.info("looking at address: 0x" + Integer.toHexString(nameTableAddress));
					logger.info("pulled from name table: 0x" + Integer.toHexString(Platform.getPpuMemory().getMemoryFromHexAddress(nameTableAddress)));
				}
			} 
		
			drawBackgroundTile(nameTableAddress, this.getHorizontalPerPixelCount(), verticalPerPixelCount);
			
			this.setHorizontalPerPixelCount(this.getHorizontalPerPixelCount() + 8);

			this.incrementHorizontalPerTileCount();
			
			// reset name count
			this.setHorizontalNameCount(0);
		}
		
		// reset row counts 
		this.setHorizontalPerPixelCount(0);
		this.setHorizontalPerTileCount(0);
    }
	
	private void drawBackgroundTile(int nameTableAddress, int horizontalPixel, int verticalPerPixelCount)
	{
		if (horizontalPixel >= 8 || !this.getPpuMaskRegister().isBackGroundClipping())
		{
			// select tile to write.
			BackgroundTile bg = Platform.getPpuMemory().getNameTableFromHexAddress(nameTableAddress).getTileFromHexAddress(nameTableAddress);

			if (logger.isDebugEnabled())
			{
				logger.debug("drawing background tile: nameTableAddress: " + nameTableAddress + " background tile number: " + bg.getBackgroundNumber() + " horizontalPixel: " + horizontalPixel + " scanline: " + verticalPerPixelCount + " color high bit: " + bg.getBackgroundAttributes().getColorHighBit());
			}
			
			int tileYindex = verticalPerPixelCount & 7; // find out tile y index.
			
			this.getScreenBuffer().setScreenBufferTileRow(horizontalPixel, verticalPerPixelCount, bg.getTileColorRow(tileYindex));
			horizontalPixel += 8;
		}
		else
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("isBackgroundClipping: " + this.getPpuMaskRegister().isBackGroundClipping());
			}
		}
	}
	
	public void doRegisterReadsWrites()
	{
		this.getPpuControlRegister().cycle();
		this.getPpuMaskRegister().cycle();
		this.getPpuStatusRegister().cycle();
		this.getPpuSpriteDMARegister().cycle();
		this.getPpuSprRamAddressRegister().cycle();
		this.getPpuSprRamIORegister().cycle();
		this.getPpuScrollRegister().cycle();
		this.getPpuVramAddressRegister().cycle();
		this.getPpuVramIORegister().cycle();
	}

	public PPUControlRegister getPpuControlRegister()
	{
		return ppuControlRegister;
	}

	public void setPpuControlRegister(PPUControlRegister ppuControlRegister)
	{
		this.ppuControlRegister = ppuControlRegister;
	}

	public PPUMaskRegister getPpuMaskRegister()
	{
		return ppuMaskRegister;
	}

	public void setPpuControlRegister2(PPUMaskRegister ppuControlRegister2)
	{
		this.ppuMaskRegister = ppuControlRegister2;
	}

	public PPUStatusRegister getPpuStatusRegister()
	{
		return ppuStatusRegister;
	}

	public void setPpuStatusRegister(PPUStatusRegister ppuStatusRegister)
	{
		this.ppuStatusRegister = ppuStatusRegister;
	}

	public PPUSpriteDMARegister getPpuSpriteDMARegister()
	{
		return ppuSpriteDMARegister;
	}

	public void setPpuSpriteDMARegister(PPUSpriteDMARegister ppuSpriteDMARegister)
	{
		this.ppuSpriteDMARegister = ppuSpriteDMARegister;
	}

	public int getScanlineCount()
	{
		return scanlineCount;
	}
	
	public void incrementScanlineCount()
	{
		++scanlineCount;
	}

	public void setScanlineCount(int scanlineCount)
	{
		this.scanlineCount = scanlineCount;
	}

	public PPUSprRamAddressRegister getPpuSprRamAddressRegister()
	{
		return ppuSprRamAddressRegister;
	}

	public void setPpuSprRamAddressRegister(
			PPUSprRamAddressRegister ppuSprRamAddressRegister)
	{
		this.ppuSprRamAddressRegister = ppuSprRamAddressRegister;
	}

	public PPUSprRamIORegister getPpuSprRamIORegister()
	{
		return ppuSprRamIORegister;
	}

	public void setPpuSprRamIORegister(PPUSprRamIORegister ppuSprRamIORegister)
	{
		this.ppuSprRamIORegister = ppuSprRamIORegister;
	}

	public PPUVramAddressRegister getPpuVramAddressRegister()
	{
		return ppuVramAddressRegister;
	}

	public void setPpuVramAddressRegister(PPUVramAddressRegister ppuVramAddressRegister)
	{
		this.ppuVramAddressRegister = ppuVramAddressRegister;
	}

	public PPUVramIORegister getPpuVramIORegister()
	{
		return ppuVramIORegister;
	}

	public void setPpuVramIORegister(PPUVramIORegister ppuVramIORegister)
	{
		this.ppuVramIORegister = ppuVramIORegister;
	}
	
	public int getHorizontalPerPixelCount()
	{
		return horizontalPerPixelCount;
	}

	public void setHorizontalPerPixelCount(int horizontalPerPixelCount)
	{
		this.horizontalPerPixelCount = horizontalPerPixelCount;
	}
	
	public void incrementHorizontalPerPixelCount()
	{
		this.horizontalPerPixelCount++;
	}

	public int getHorizontalPerTileCount()
	{
		return horizontalPerTileCount;
	}
	
	public void setHorizontalPerTileCount(int horizontalTileCount)
	{
		this.horizontalPerTileCount = horizontalTileCount;
	}

	public void incrementHorizontalPerTileCount()
	{
		this.horizontalPerTileCount++;
	}
	
	public int getHorizontalNameCount()
	{
		return horizontalNameCount;
	}

	public void setHorizontalNameCount(int horizontalNameCount)
	{
		this.horizontalNameCount = horizontalNameCount;
	}
	
	public void flipHorizontalNameCount()
	{
		this.setHorizontalNameCount(1 ^ this.horizontalNameCount);
	}

	public int getVerticalPerTileCount()
	{
		return verticalPerTileCount;
	}

	public void setVerticalPerTileCount(int verticalTileCount)
	{
		this.verticalPerTileCount = verticalTileCount;
	}
	
	public void incrementVerticalPerTileCount()
	{
		this.verticalPerTileCount++;
	}

	public int getVerticalNameCount()
	{
		return verticalNameCount;
	}

	public void setVerticalNameCount(int verticalNameCount)
	{
		this.verticalNameCount = verticalNameCount;
	}
	
	public void flipVerticalNameCount()
	{
		this.setVerticalNameCount(1 ^ this.verticalNameCount);
	}	

	public PPUScrollRegister getPpuScrollRegister()
	{
		return ppuScrollRegister;
	}

	public void setPpuScrollRegister(PPUScrollRegister ppuVramAddressRegister)
	{
		this.ppuScrollRegister = ppuVramAddressRegister;
	}
	
	public int getRegisterAddressFlipFlopLatch()
	{
		int latchValue = this.registerAddressFlipFlopLatch;
		this.flipRegisterAddressFlipFlopLatch();
		return latchValue;
	}

	public void flipRegisterAddressFlipFlopLatch()
	{
		this.registerAddressFlipFlopLatch = (1^this.registerAddressFlipFlopLatch);
	}
	
	public void resetRegisterAddressFlipFlopLatch()
	{
		this.registerAddressFlipFlopLatch = 0;
	}

	public int getVerticalPerPixelCount()
	{
		return verticalPerPixelCount;
	}

	public void setVerticalPerPixelCount(int verticalPerPixelCount)
	{
		this.verticalPerPixelCount = verticalPerPixelCount;
	}
	
	public void incrementVerticalPerPixelCount()
	{
		this.verticalPerPixelCount++;
	}

	public void setPpuMaskRegister(PPUMaskRegister ppuMaskRegister)
	{
		this.ppuMaskRegister = ppuMaskRegister;
	}

	public void setRegisterAddressFlipFlopLatch(int registerAddressFlipFlopLatch)
	{
		this.registerAddressFlipFlopLatch = registerAddressFlipFlopLatch;
	}

	public ScreenBuffer getScreenBuffer()
	{
		return screenBuffer;
	}

	public void setScreenBuffer(ScreenBuffer screenBuffer)
	{
		this.screenBuffer = screenBuffer;
	}

	public int getLoopyT()
	{
		return loopyT;
	}

	public void setLoopyT(int loopyT)
	{
		logger.info("loopyT being set to " + loopyT + " at scanline " + this.getScanlineCount());
		this.loopyT = loopyT;
	}

	public int getLoopyV()
	{
		return loopyV;
	}

	public void setLoopyV(int loopyV)
	{
		this.loopyV = loopyV;
	}

	public int getLoopyX()
	{
		return loopyX;
	}

	public void setLoopyX(int loopyX)
	{
		this.loopyX = loopyX;
	}

	private void incrementScreenBuffersWrittenToScreen()
	{
		screenBuffersWrittenToScreen++;
	}
	
	public void resetScreenBuffersWrittenToScreen()
	{
		screenBuffersWrittenToScreen = 0;
	}
	
	public int getScreenBuffersWrittenToScreen()
	{
		return screenBuffersWrittenToScreen;
	}

	public void setScreenBuffersWrittenToScreen(int screenBuffersWrittenToScreen)
	{
		this.screenBuffersWrittenToScreen = screenBuffersWrittenToScreen;
	}
}
