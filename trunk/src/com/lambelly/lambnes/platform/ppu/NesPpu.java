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
	private PPUScrollRegister ppuVramAddressRegister1 = PPUScrollRegister.getRegister();
	private PPUVramAddressRegister ppuVramAddressRegister2 = PPUVramAddressRegister.getRegister();
	private PPUVramIORegister ppuVramIORegister = PPUVramIORegister.getRegister();
	private int scanlineCount = 0;
	private int horizontalPerPixelCount = 0;
	private int horizontalPerTileCount = 0;
	private int horizontalNameCount = 0;
	private int verticalPerTileCount = 0;
	private int verticalPerPixelCount = 0;
	private int verticalNameCount = 0;
	private int registerAddressFlipFlopLatch = 0;
    private ScreenBuffer screenBuffer = new ScreenBuffer();
	private Logger logger = Logger.getLogger(NesPpu.class);
	
	public void cycle(int cycleCount)
	{	
		Platform.getPpu().doRegisterReadsWrites();
		
    	if (cycleCount % NesPpu.NUM_CYCLES_PER_SCANLINE == 0)
    	{	 
    		if (getScanlineCount() >= NesPpu.VBLANK_SCANLINE_START && getScanlineCount() <= NesPpu.VBLANK_SCANLINE_END)
    		{
    			// vblank
    			Platform.getPpu().getPpuStatusRegister().setVblank(true);
    			
    			
    			// request interrupt (assumed it happens once per vBlank.
    			if (getScanlineCount() == NesPpu.VBLANK_SCANLINE_START)
    			{
        			this.getScreenBuffer().pushBufferToScreen();
        			
        			if (this.getPpuControlRegister().isExecuteNMIOnVBlank())
        			{
        				Platform.getNesInterrupts().addInterruptRequestToQueue(new InterruptRequest(InterruptRequest.interruptTypeNMI));
        			}
    			}
    		}
    		else if (getScanlineCount() >= NesPpu.VBLANK_SCANLINE_END)
    		{
    			// reset scanlineCount (new frame)
    			logger.info("setting sprite0 false at " + this.getScanlineCount());
    			Platform.getPpu().getPpuStatusRegister().setSprite0Occurance(false);
    			this.getPpuStatusRegister().setVblank(false);
    			setScanlineCount(0); 
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
        
        // implements an internal "vertical scroll counter"
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
                
                if (drawSpriteTileLine(sprite,verticalPerPixelCount) && 
                    (!this.getPpuStatusRegister().isSprite0Occurance()))
                {
                	logger.info("sprite0 true: " + this.getScanlineCount());
                    this.getPpuStatusRegister().setSprite0Occurance(true);                        
                }
            }
        }
    }
	
	private boolean drawSpriteTileLine(SpriteTile sprite, int verticalPerPixelCount)
	{
		boolean sprite0Triggered = false;
        int spriteLine = verticalPerPixelCount - sprite.getSpriteAttributes().getyCoordinate();
        
        //if (logger.isDebugEnabled())
        {
        	logger.info("drawing sprite " + sprite.getSpriteNumber() + " at " + verticalPerPixelCount);
        	logger.info("spriteLine: " + spriteLine + " sprite: " + sprite.getSpriteNumber() + " scanline: " + verticalPerPixelCount + " y coord: " + sprite.getSpriteAttributes());
        	logger.info("sprite attributes for sprite: " + sprite.getSpriteAttributes().getTileIndex() + "\n" + sprite.getSpriteAttributes());
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
        int spriteXOffset = 0;
        int spriteXPosition = sprite.getSpriteAttributes().getxCoordinate();
    	int sprite0Number = Platform.getPpuMemory().getSprRam()[1];
        
        // TODO -- should this be the way that transparentColor is determined? It probably should be part of an object so it can be reused.
        PixelColor spriteTransparentColor = new PixelColor(0, PixelColor.PALETTE_TYPE_SPRITE);
        PixelColor backgroundTransparentColor =  new PixelColor(0, PixelColor.PALETTE_TYPE_BACKGROUND);
        
        for (PixelColor pixel : sprite.getTileColorRow(spriteLine))
        {
        	logger.info("sprite 0 logic: sprite 0 number: " + sprite0Number + " sprite number: " + sprite.getSpriteNumber() + " sprite pixel color: " + LambNesGui.getScreen().getImage().getRGB(spriteXPosition + spriteXOffset, verticalPerPixelCount) + " sprite pixel transparent color: " + spriteTransparentColor.getMasterPaletteColor().getColorInt() + " background pixel color: " + LambNesGui.getScreen().getImage().getRGB(spriteXPosition + spriteXOffset, verticalPerPixelCount) + " background pixel transparent color: " + backgroundTransparentColor.getMasterPaletteColor().getColorInt() + " sprite0Triggered: " + sprite0Triggered);
        	
        	// check sprite visibility
        	if (pixel.getPaletteIndex() != spriteTransparentColor.getPaletteIndex())                  
	        {   
        		logger.info("sprite 0 logic: sprite visible");
        		// check background visibility
        		if (LambNesGui.getScreen().getImage().getRGB(spriteXPosition + spriteXOffset, verticalPerPixelCount) != backgroundTransparentColor.getMasterPaletteColor().getColorInt())
        		{
        			logger.info("sprite 0 logic: background visible");
	        	     // sprite 0 logic
		        	if (sprite.getSpriteNumber() == sprite0Number)                   
		        	{                          
		        		sprite0Triggered = true;
		        		logger.info("sprite 0 logic: sprite0Triggered: " + sprite0Triggered + " spriteNumber: " + sprite.getSpriteNumber() + " sprite0 number: " + sprite0Number);
		        	}
	        	}   
        		else
        		{
        			// background invisible
        		}
	        }
	        
        	// draw pixel
        	if (spriteXPosition >= 8 || this.getPpuMaskRegister().isSpriteVisibility())                      
        	{                          
        		this.getScreenBuffer().setScreenBufferPixel(spriteXPosition + spriteXOffset,verticalPerPixelCount,pixel.getMasterPaletteColor().getColorInt());                      
        	}		        	
        	
	        spriteXOffset++;
        }
        
        //if (logger.isDebugEnabled())
        {
        	logger.info("drawing line " + spriteLine + " of sprite number: " + sprite.getSpriteNumber() +  " index: " + sprite.getSpriteAttributes().getTileIndex() + " at y: " + verticalPerPixelCount + "x: " + spriteXPosition + " sprite0: " + this.getPpuStatusRegister().isSprite0Occurance());
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
	        	logger.debug("scanline: " + verticalPerPixelCount + " tileIndex: " + spriteAttribute.getTileIndex() + " spriteYcoordinate: " + spriteAttribute.getyCoordinate());
	        	logger.debug("ppu sprite clipping: " + this.getPpuMaskRegister().isSpriteClipping());
        	}
        	
            //   determine if Y coord is being drawn (for line + 1) 
            int diff = ((verticalPerPixelCount + 1) - spriteAttribute.getyCoordinate());
            
            if ((diff >= 0 && diff <= 7) || ((this.getPpuControlRegister().getSpriteSize() == PPUControlRegister.SPRITE_SIZE_8X16) && (diff >= 0 && diff <= 15)))
            {
            	//if (logger.isDebugEnabled())
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
		// iterate one line in name table
		for (int nameTableEntry = 0; nameTableEntry < NesPpu.NUM_HORIZONTAL_TILES; nameTableEntry++)
		{
			//essentially creates an integer that represents the offset from start of name table (0x2000).
			int nameTableAddress = 0x2000 + this.getHorizontalPerTileCount() | (this.getVerticalPerTileCount() << 5) | (this.getHorizontalNameCount() << 10) | (this.getVerticalNameCount() << 11);
			if (logger.isDebugEnabled())
			{
				logger.debug("horizontalPerTileCount: " + this.getHorizontalPerTileCount());
				logger.debug("verticalPerTileCount: " + this.getVerticalPerTileCount());
				logger.debug("horizontalNameCount: " + this.getHorizontalNameCount());
				logger.debug("verticalNameCount: " + this.getVerticalNameCount());
				logger.debug("scanline: " + this.getScanlineCount());
				logger.debug("looking at address: 0x" + Integer.toHexString(nameTableAddress));
				logger.debug("pulled from name table: 0x" + Integer.toHexString(Platform.getPpuMemory().getMemoryFromHexAddress(nameTableAddress)));
			} 
		
			drawBackgroundTile(nameTableAddress, this.getHorizontalPerPixelCount(), verticalPerPixelCount);
			
			this.setHorizontalPerPixelCount(this.getHorizontalPerPixelCount() + 8);

			this.incrementHorizontalPerTileCount();

			if (this.getHorizontalPerTileCount() == 32) // reached end of line, essentially
			{
				this.setHorizontalPerPixelCount(0);
				//this.flipHorizontalNameCount();
				this.setHorizontalPerTileCount(0);
			}
		}
    	
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
		this.getPpuVramAddressRegister1().cycle();
		this.getPpuVramAddressRegister2().cycle();
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

	public PPUVramAddressRegister getPpuVramAddressRegister2()
	{
		return ppuVramAddressRegister2;
	}

	public void setPpuVramAddressRegister2(
			PPUVramAddressRegister ppuVramAddressRegister)
	{
		this.ppuVramAddressRegister2 = ppuVramAddressRegister;
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
		if (this.getPpuControlRegister().getPpuAddressIncrement() == PPUControlRegister.PPU_ADDRESS_INCREMENT_1)
		{
			this.horizontalPerPixelCount++;
		}
		else
		{
			this.horizontalPerPixelCount += 32;
		}
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
		this.setHorizontalNameCount((this.getHorizontalNameCount() == 0)?1:0);
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
		this.setVerticalNameCount((this.getVerticalNameCount() == 0)?1:0);
	}	

	public PPUScrollRegister getPpuVramAddressRegister1()
	{
		return ppuVramAddressRegister1;
	}

	public void setPpuVramAddressRegister1(
			PPUScrollRegister ppuVramAddressRegister1)
	{
		this.ppuVramAddressRegister1 = ppuVramAddressRegister1;
	}
	
	public int getRegisterAddressFlipFlopLatch()
	{
		int latchValue = this.registerAddressFlipFlopLatch;
		this.flipRegisterAddressFlipFlopLatch();
		return latchValue;
	}

	public void flipRegisterAddressFlipFlopLatch()
	{
		logger.debug("flipping flip flop from " + this.registerAddressFlipFlopLatch + " to " + (1^this.registerAddressFlipFlopLatch));
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
}
