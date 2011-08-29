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
	private static final int SCANLINE_IDLE= 240;
	private static final int SCANLINE_261=261;
	private static final int VBLANK_SCANLINE_START = 241;
	private static final int VBLANK_SCANLINE_END = 260;
	private static final int REFRESH_RATE = 60;
	private static final int NUM_SCANLINES_PER_FRAME = 262;
	private static final int NUM_HORIZONTAL_TILES = 32;
	private static final int NUM_VERTICAL_TILES = 30;
	public static final int SPRITE_COUNT = 64;
    public static final int PPU_CYCLES_PER_LINE = 341; // PPU is 3 times faster than CPU
    public static final int CPU_CYCLES_PER_LINE = 113; // 341/3=113.66. Every 3 cpu cycles, we need to add 2 more to get to the correct PPU count
    public static final int PPU_CYCLES_PER_FRAME = NUM_SCANLINES_PER_FRAME * PPU_CYCLES_PER_LINE;
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
	private int horizontalNameCount = 0;
	private int verticalPerTileCount = 0;
	private int verticalNameCount = 0;     
	private int registerAddressFlipFlopLatch = 0;
	private int loopyT = 0;
	private int loopyV = 0;
	private int loopyX = 0;
    private int ppuCyclesUntilEndOfFrame = NesPpu.PPU_CYCLES_PER_FRAME;
    private int ppuCyclesUntilEndOfLine = NesPpu.PPU_CYCLES_PER_LINE;
    private long vblankInterval = 0;
    private ScreenBuffer screenBuffer = new ScreenBuffer();
    private long screenCount = 0;
	private Logger logger = Logger.getLogger(NesPpu.class);
	
	public void cycle(int cpuCycleCount)
	{	
		logger.info("scanline " + this.getScanlineCount() + " at cpu " + Platform.getCycleCount());
		this.doRegisterReadsWrites();
		
		// timing
		this.subtractFromPpuCyclesUntilEndOfFrame(cpuCycleCount * 3);
		this.subtractFromPpuCyclesUntilEndOfLine(cpuCycleCount * 3);
		
    	if (this.getPpuCyclesUntilEndOfLine() <= 0)
    	{	
    		if (getScanlineCount() >= NesPpu.VBLANK_SCANLINE_START && getScanlineCount() <= NesPpu.VBLANK_SCANLINE_END)
    		{
    			// vblank
    			this.setLoopyV(this.getLoopyT());
    			
    			if (getScanlineCount() == NesPpu.VBLANK_SCANLINE_START)
    			{
        			// vblank start
    				logger.info("entering vblank at cycle " + Platform.getCycleCount() + " and scanline " + this.getScanlineCount());
        			Platform.getPpu().getPpuStatusRegister().setVblank(true);
        			this.vblankInterval = Platform.getCycleCount();
        			
        			if (this.getPpuControlRegister().isExecuteNMIOnVBlank())
        			{
        				Platform.getNesInterrupts().addInterruptRequestToQueue(new InterruptRequest(InterruptRequest.interruptTypeNMI));
        			}
    			}
        		// vblank end
    			else if (getScanlineCount() == NesPpu.VBLANK_SCANLINE_END)
        		{
        			logger.info("resetting vblank at cycle " + Platform.getCycleCount() + " and scanline " + this.getScanlineCount() + " vblank interval: " + (Platform.getCycleCount() - this.vblankInterval));
        			// reset scanlineCount (new frame)
        			this.getPpuStatusRegister().setSprite0Occurance(false);
        			this.getPpuStatusRegister().setVblank(false);
        			this.getPpuStatusRegister().setScanlineSpriteCount(false);
        			this.setLoopyV(this.getLoopyT());
        		}
    			
    			incrementScanlineCount();
    		}
    		else if (getScanlineCount() == NesPpu.SCANLINE_IDLE)
    		{
    			// idle
    			incrementScanlineCount();
    		}
    		else if (getScanlineCount() == NesPpu.SCANLINE_261)
    		{
    			// not exactly idle. This is probably inaccurate right now.
    			setScanlineCount(0);
    		}
    		else
    		{
    			// draw scanline
    			this.drawScanline(this.getScanlineCount());
    			incrementScanlineCount();
    		}
    		
    		this.addToPpuCyclesUntilEndOfLine(NesPpu.PPU_CYCLES_PER_LINE);
    	}
    	
    	if (this.getPpuCyclesUntilEndOfFrame() <= 0)
    	{
    		logger.info("new screen " + this.getScreenCount() + " at cpu cycle " + Platform.getCycleCount() + " and scanline " + this.getScanlineCount() + " cycles until end of frame: " + this.getPpuCyclesUntilEndOfFrame() + " cpuCycleCount: " + cpuCycleCount);
    		this.incrementScreenCount();
			this.getScreenBuffer().pushBufferToScreen();
			this.addToPpuCyclesUntilEndOfFrame(NesPpu.PPU_CYCLES_PER_FRAME);
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
                	//if (logger.isDebugEnabled())
                	{
                		logger.info("sprite0 true at: scanline: " + this.getScanlineCount() + " screencount: " + this.getScreenCount());
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
		        	if (sprite.getSpriteNumber() == sprite0Number && !this.getPpuStatusRegister().isSprite0Occurance())                   
		        	{                     
		        		sprite0Triggered = true;
		        		//if (logger.isDebugEnabled())
		        		{
		        			logger.info("sprite0 logic: sprite0Triggered: " + sprite0Triggered + " spriteNumber: " + sprite.getSpriteNumber() + " sprite0 number: " + sprite0Number + " verticalPerPixelCount: " + verticalPerPixelCount + " vcoord: " + sprite.getSpriteAttributes().getyCoordinate() + " xcoord: " + sprite.getSpriteAttributes().getxCoordinate() + " for screen " + this.getScreenCount());
		        			logger.info("background color: " + LambNesGui.getScreen().getImage().getRGB((spriteXPosition & Platform.EIGHT_BIT_MASK), verticalPerPixelCount));
		        			logger.info("background transparent color: " +  backgroundTransparentColor.getMasterPaletteColor().getColorInt());
		        			logger.info("using sprite0: " + sprite);
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
            	SpriteTile currentSprite = new SpriteTile(NesTileCache.getSpriteTile(spriteAttribute.getTileIndex()));
            	
            	//if (logger.isDebugEnabled())
            	{
            		logger.info("adding sprite to buffer: scanline: " + verticalPerPixelCount + " tileIndex: " + spriteAttribute.getTileIndex() + " spriteYcoordinate: " + spriteAttribute.getyCoordinate() + " diff: " + diff + " spritecount: " + spriteCount + " spriteNumber: " + currentSprite.getSpriteNumber());
            	}
            	
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
    	/*
    	 * ----------------------------------------
    	 * loopyT:
    	 * OOO.NN.YYYYY.XXXXX
    	 * X is denoted as the X scroll counter.
    	 * Y is denoted as the Y scroll counter.
    	 * N is denoted the nametable select bits.
    	 * O is the Y scroll offset
    	 * -----------------------------------------
    	 * */
    	
    	//logger.info("name table control: " + Platform.getPpu().getPpuControlRegister().getNameTableAddress() + " coarseX: " + (this.getLoopyT() & 0x1F) + " finex: " + (this.getLoopyX() & 0x07) + " coarseY: " + ((this.getLoopyT() & 0x3E0) >> 5) + " fineY: " + ((this.getLoopyT() & 0x7000) >> 12) + " loopyT: " + this.getLoopyT() + " loopyV: " + this.getLoopyV() + " at scanline " + this.getScanlineCount() + " and frame " + this.getScreenCount());
    	
		// iterate one line in name table
		for (int horizontalPerTileCount = 0; horizontalPerTileCount < NesPpu.NUM_HORIZONTAL_TILES; horizontalPerTileCount++)
		{
			//hscroll
			int hCoarseScrollOffset = (this.getLoopyT() & 0x1F);
			int offsetHorizontalPerTileCount = horizontalPerTileCount + hCoarseScrollOffset; // coarse x
			int hFineScrollOffset = (this.getLoopyX() & 0x07);
			
			// vscroll
			int vCoarseScrollOffset = (this.getLoopyT() & 0x3E0) >> 5;
			int offsetVerticalPerTileCount = verticalPerTileCount + vCoarseScrollOffset;
			int vFineScrollOffset = (this.getLoopyT() & 0x7000) >> 12;
			
			this.setHorizontalNameCount(Platform.getPpu().getPpuControlRegister().getNameTableAddress());
			
			// see if hscroll is outside nametable, if so flip horiz
			if ((offsetHorizontalPerTileCount * 8) + hFineScrollOffset > Screen.SCREEN_HORIZONTAL_RESOLUTION)
			{
				// flip horiz
				this.flipHorizontalNameCount();
				
				// circle nameTable 
				offsetHorizontalPerTileCount &= 0x1F;
			}
			
			// see if vscroll is outside nametable, if so flip vert
			if ((offsetVerticalPerTileCount * 8) + vFineScrollOffset > Screen.SCREEN_VERTICAL_RESOLUTION)
			{
				// flip vert
				this.flipVerticalNameCount();
				
				// circle nameTable 
				offsetVerticalPerTileCount = offsetVerticalPerTileCount % NesPpu.NUM_VERTICAL_TILES;
			}
			
			//essentially creates an integer that represents the offset from start of name table (0x2000).
			int nameTableAddress = 0x2000 + offsetHorizontalPerTileCount | (this.getVerticalPerTileCount() << 5) | (this.getHorizontalNameCount() << 10) | (this.getVerticalNameCount() << 11); 
			
			//if (logger.isDebugEnabled())
			//{
				//logger.info("name table control: " + Platform.getPpu().getPpuControlRegister().getNameTableAddress());
				//logger.info("hscroll offset: " + hCoarseScrollOffset);
				//logger.info("offsetHorizontalPerTileCount: " + offsetHorizontalPerTileCount);
				//logger.info("horizontalPerTileCount: " + horizontalPerTileCount);
				//logger.info("verticalPerTileCount: " + this.getVerticalPerTileCount());
				//logger.info("horizontalNameCount: " + this.getHorizontalNameCount());
				//logger.info("verticalNameCount: " + this.getVerticalNameCount());
				//logger.info("scanline: " + this.getScanlineCount() + " for screen " + this.getScreenCount());
				//logger.info("looking at address: 0x" + Integer.toHexString(nameTableAddress) + " horizontalPerTileCount: " + horizontalPerTileCount + " vOffset: " + offsetVerticalPerTileCount + " scanline: " + this.getScanlineCount() + " for screen " + this.getScreenCount());
				//logger.info("pulled from name table: 0x" + Integer.toHexString(Platform.getPpuMemory().getMemoryFromHexAddress(nameTableAddress)));
			//} 
		
			drawBackgroundTile(nameTableAddress, horizontalPerTileCount, hFineScrollOffset, verticalPerPixelCount, vFineScrollOffset);
			
			// reset name count
			this.setHorizontalNameCount(0);
		}
    }
	
	private void drawBackgroundTile(int nameTableAddress, int horizontalPerTileCount, int hFineScrollOffset, int verticalPerPixelCount, int vFineScrollOffset)
	{
		if (horizontalPerTileCount >= 1 || !this.getPpuMaskRegister().isBackGroundClipping())
		{
			PaletteColor[] tile = new PaletteColor[8];
			int tileYindex = verticalPerPixelCount & 7; // find out tile y index.

			// select tile to write.
			BackgroundTile bg = Platform.getPpuMemory().getNameTableFromHexAddress(nameTableAddress).getTileFromHexAddress(nameTableAddress);
			System.arraycopy(bg.getTileColorRow(tileYindex), 0, tile, 0, (tile.length));
			
			logger.info("drawing bg tile: " + bg.getBackgroundNumber() + " at position " + horizontalPerTileCount + " on scanline " + this.getScanlineCount() + " on screen " + this.getScreenCount());
			logger.info("drawing bg tile: " + bg);
			
			this.getScreenBuffer().setScreenBufferTileRow((horizontalPerTileCount * 8), verticalPerPixelCount, hFineScrollOffset, vFineScrollOffset, tile); 
		}
		else
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("isBackgroundClipping: " + this.getPpuMaskRegister().isBackGroundClipping());
			}
		}
	}
	
	private void doRegisterReadsWrites()
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
		logger.info("loopyT being set to " + loopyT + " at scanline " + this.getScanlineCount() + " screencount: " + this.getScreenCount());
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

	public int getPpuCyclesUntilEndOfFrame()
	{
		return ppuCyclesUntilEndOfFrame;
	}

	public void setPpuCyclesUntilEndOfFrame(int ppuCyclesUntilEndOfFrame)
	{
		this.ppuCyclesUntilEndOfFrame = ppuCyclesUntilEndOfFrame;
	}

	public void addToPpuCyclesUntilEndOfFrame(int cycles)
	{
		this.ppuCyclesUntilEndOfFrame += cycles;
	}
	
	public void subtractFromPpuCyclesUntilEndOfFrame(int cycles)
	{
		this.ppuCyclesUntilEndOfFrame -= cycles;
	}
	
	public int getPpuCyclesUntilEndOfLine()
	{
		return ppuCyclesUntilEndOfLine;
	}

	public void setPpuCyclesUntilEndOfLine(int ppuCyclesUntilEndOfLine)
	{
		this.ppuCyclesUntilEndOfLine = ppuCyclesUntilEndOfLine;
	}
	
	public void addToPpuCyclesUntilEndOfLine(int cycles)
	{
		this.ppuCyclesUntilEndOfLine += cycles;
	}
	
	public void subtractFromPpuCyclesUntilEndOfLine(int cycles)
	{
		this.ppuCyclesUntilEndOfLine -= cycles;
	}

	public long getScreenCount()
	{
		return screenCount;
	}

	public void setScreenCount(long screenCount)
	{
		this.screenCount = screenCount;
	}
	
	public void incrementScreenCount()
	{
		this.screenCount++;
	}
}
