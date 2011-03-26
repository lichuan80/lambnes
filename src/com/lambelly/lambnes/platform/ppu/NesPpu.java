package com.lambelly.lambnes.platform.ppu;

import com.lambelly.lambnes.gui.*;
import com.lambelly.lambnes.platform.*;
import com.lambelly.lambnes.platform.interrupts.InterruptRequest;
import com.lambelly.lambnes.platform.ppu.registers.PPUControlRegister1;
import com.lambelly.lambnes.platform.ppu.registers.PPUControlRegister2;
import com.lambelly.lambnes.platform.ppu.registers.PPUSprRamAddressRegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUSprRamIORegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUSpriteDMARegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUStatusRegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUVramAddressRegister1;
import com.lambelly.lambnes.platform.ppu.registers.PPUVramAddressRegister2;
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
	private PPUControlRegister1 ppuControlRegister1 = PPUControlRegister1.getRegister();
	private PPUControlRegister2 ppuControlRegister2 = PPUControlRegister2.getRegister();
	private PPUStatusRegister ppuStatusRegister = PPUStatusRegister.getRegister();
	private PPUSpriteDMARegister ppuSpriteDMARegister = PPUSpriteDMARegister.getRegister();
	private PPUSprRamAddressRegister ppuSprRamAddressRegister = PPUSprRamAddressRegister.getRegister();
	private PPUSprRamIORegister ppuSprRamIORegister = PPUSprRamIORegister.getRegister();
	private PPUVramAddressRegister1 ppuVramAddressRegister1 = PPUVramAddressRegister1.getRegister();
	private PPUVramAddressRegister2 ppuVramAddressRegister2 = PPUVramAddressRegister2.getRegister();
	private PPUVramIORegister ppuVramIORegister = PPUVramIORegister.getRegister();
	private int scanlineCount = 0;
	private int horizontalPerPixelCount = 0;
	private int horizontalPerTileCount = 0;
	private int horizontalNameCount = 0;
	private int verticalPerTileCount = 0;
	private int verticalNameCount = 0;
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
    			if (getScanlineCount() == 241 && this.getPpuControlRegister1().isExecuteNMIOnVBlank())
    			{
    				Platform.getNesInterrupts().addInterruptRequestToQueue(new InterruptRequest(InterruptRequest.interruptTypeNMI));
    			}
    		}
    		else if (getScanlineCount() >= NesPpu.VBLANK_SCANLINE_END)
    		{
    			// reset scanlineCount (new frame)
    			Platform.getPpu().getPpuStatusRegister().setVblank(false);
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
			
			if (this.getPpuControlRegister2().isBackgroundVisibility())
			{
				drawBackgroundTile(nameTableAddress, this.getHorizontalPerPixelCount(), scanline);
			}
			
			this.setHorizontalPerPixelCount(this.getHorizontalPerPixelCount() + 8);

			this.incrementHorizontalPerTileCount();

			if (this.getHorizontalPerTileCount() == 32) // reached end of line, essentially
			{
				this.setHorizontalPerPixelCount(0);
				//this.flipHorizontalNameCount();
				this.setHorizontalPerTileCount(0);
			}
		}
		
        // for 64 sprites  
        if (this.getPpuControlRegister2().isSpriteVisibility())
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
	
    private void drawSprites(int scanline)
    {
    	logger.debug("using pattern memory for sprite tiles: " + (Platform.getPpu().getPpuControlRegister1().getSpritePatternTableAddress() == PPUControlRegister1.SPRITE_PATTERN_TABLE_ADDRESS_1000 ? 1000 : 0));
    	logger.debug("previousSpriteCount: " + Platform.getPpuMemory().getSpritesInBufferCount());
        if (Platform.getPpuMemory().getSpritesInBufferCount() > 0)
        {
            for (int i = 0 ; i < Platform.getPpuMemory().getSpritesInBufferCount(); i++)
            {
                SpriteTile sprite = Platform.getPpuMemory().getSpriteFromBuffer(i);
                
                //if (logger.isDebugEnabled())
                {
                	logger.debug("drawing sprite " + sprite.getSpriteAttributes().getTileIndex());
                }
                
                if (drawSpriteTile(sprite,scanline) && 
                    (!this.getPpuStatusRegister().isSprite0Occurance()))
                {
                    this.getPpuStatusRegister().setSprite0Occurance(true);                        
                }
            }
        }
    }
	
	private boolean drawSpriteTile(SpriteTile sprite, int scanline)
	{
		logger.debug("drawing sprite " + sprite.getSpriteNumber() + " at " + scanline); 
		
		boolean sprite0Triggered = false;
        int spriteLine = scanline - sprite.getSpriteAttributes().getyCoordinate();
        
        logger.debug("spriteLine: " + spriteLine + " sprite: " + sprite.getSpriteNumber() + " scanline: " + scanline + " y coord: " + sprite.getSpriteAttributes());
        
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
        
        spriteLine = spriteLine & 7; // if more than the index, roll over

        int spriteXPosition = sprite.getSpriteAttributes().getxCoordinate();
        
        logger.debug("sprite attributes for sprite: " + sprite.getSpriteAttributes().getTileIndex() + "\n" + sprite.getSpriteAttributes());
        
        // TODO -- should this be the way that transparentColor is determined? It probably should be part of an object so it can be reused.
        int transparentColor = Platform.getPpuMemory().getMemoryFromHexAddress(NesPpuMemory.BACKGROUND_PALETTE_ADDRESS);
        
        // draw one row from the sprite -- leftmost pixel column is 7, rightmost is 0.
        for (int spritePixelColumn = 7; spritePixelColumn >= 0; spritePixelColumn--)
        {
            if (spriteXPosition < 256)
            {
            	int spritePixelRow = 0;
            	if (sprite.getSpriteAttributes().isVerticalFlip())
            	{
            		spritePixelRow = 7 - spriteLine;
            	}
            	else
            	{
            		spritePixelRow = spriteLine;
            	}
                        	
            	// TODO -- kind of shitty way of doing this.
            	int spritePaletteIndex = sprite.getPixelSpriteColorPaletteIndex(spritePixelColumn, spritePixelRow);
            	int masterPaletteAddress = NesPpuMemory.SPRITE_PALETTE_ADDRESS + spritePaletteIndex;
                int masterPaletteIndex = Platform.getPpuMemory().getMemoryFromHexAddress(masterPaletteAddress);
                PaletteColor color = Platform.getMasterPalette().getColor(masterPaletteIndex);
                
                //if (logger.isDebugEnabled())
                {
	            	logger.info("sprite pixel column for sprite " + sprite.getSpriteNumber() + ": " + spritePixelColumn);
	            	logger.info("sprite pixel row for sprite " + sprite.getSpriteNumber() + ": " + spritePixelRow);
	                logger.info("sprite index number: " + sprite.getSpriteAttributes().getTileIndex());
	                logger.info("spritePaletteIndex: " + spritePaletteIndex);
	                logger.info("masterPaletteIndex: " + masterPaletteIndex);
	                logger.info("palette color: " + color);
	                logger.info("pulling masterPaletteIndex from " + masterPaletteAddress);
	                logger.info("painting screen for sprite: " + sprite.getSpriteAttributes().getTileIndex() + " x: " + (spriteXPosition + (spritePixelColumn ^ 0x7)) + " y: " + scanline + " r: " + color.getRed() + " g: " + color.getGreen() + " b: " + color.getBlue());
                }
                
                if (masterPaletteIndex != transparentColor)
                {
                	
                    if (sprite.getSpriteAttributes().getTileIndex() == 0 && LambNesGui.getScreen().getImage().getRGB(spriteXPosition, scanline) != transparentColor)
                    {
                        sprite0Triggered = true;
                    }
                    if (spriteXPosition >= 8 || this.getPpuControlRegister2().isSpriteVisibility())
                    {
                        LambNesGui.getScreen().getImage().setRGB(((spriteXPosition + (spritePixelColumn ^ 0x7)) & 0xFF),scanline,color.getColorInt());
                    }
                }
            }
        }
        return sprite0Triggered;		
	}
	
    private void updateSpriteBuffer(int scanline)
    {
    	// reset sprite overflow status
        this.getPpuStatusRegister().setScanlineSpriteCount(false);
        
        int spriteCount = 0;
        for (int spriteNumber = 0; spriteNumber < SPRITE_COUNT; spriteNumber++)
        {
        	SpriteAttribute spriteAttribute = Platform.getPpuMemory().getSpriteAttribute(spriteNumber);
        	
        	if (logger.isDebugEnabled())
        	{
	        	logger.info("scanline: " + scanline + " tileIndex: " + spriteAttribute.getTileIndex() + " spriteYcoordinate: " + spriteAttribute.getyCoordinate());
	        	logger.info("ppu sprite clipping: " + this.getPpuControlRegister2().isSpriteClipping());
        	}
        	
            //   determine if Y coord is being drawn (for line + 1) 
            int diff = ((scanline + 1) - spriteAttribute.getyCoordinate());
            
            if ((diff >= 0 && diff <= 7) || ((this.getPpuControlRegister1().getSpriteSize() == PPUControlRegister1.SPRITE_SIZE_8X16) && (diff >= 0 && diff <= 15)))
            {
            	logger.debug("adding sprite to buffer: scanline: " + scanline + " tileIndex: " + spriteAttribute.getTileIndex() + " spriteYcoordinate: " + spriteAttribute.getyCoordinate() + " diff: " + diff);
            	Platform.getPpuMemory().setSpriteToBuffer(spriteCount, NesTileCache.getSpriteTile(spriteAttribute.getTileIndex(), spriteAttribute));
                spriteCount++;
                
                if (spriteCount == 9 )
                {
                    spriteCount = 8;
                    this.getPpuStatusRegister().setScanlineSpriteCount(true);
                    break;
                }
            }
        }   
    }	
	
	private void drawBackgroundTile(int nameTableAddress, int horizontalPixel, int scanline)
	{
		logger.debug("drawing background tile: nameTableAddress: " + nameTableAddress + " horizontalPixel: " + horizontalPixel + " scanline: " + scanline);
		if (horizontalPixel >= 8 || !this.getPpuControlRegister2().isBackGroundClipping())
		{
			// select tile to write.
			BackgroundTile bg = Platform.getPpuMemory().getNameTableFromHexAddress(nameTableAddress).getTileFromHexAddress(nameTableAddress);

			for (int tileXindex = 7; tileXindex >= 0; tileXindex--)
			{   
				int tileYindex = scanline & 7; // find out tile y index.
				logger.debug("background tile: " + bg.getBackgroundNumber() + " scanline: " + scanline + " tileYindex: " + tileYindex);
				
				if (horizontalPixel < 256)
				{
					// get pixel from line, x
					int paletteIndex = bg.getPixelBackgroundColorPaletteIndex(tileXindex, tileYindex);
					int masterPaletteIndex = Platform.getPpuMemory().getMemoryFromHexAddress(NesPpuMemory.BACKGROUND_PALETTE_ADDRESS + paletteIndex);
					PaletteColor backgroundPixelColor = Platform.getMasterPalette().getColor(masterPaletteIndex);
	    				
					LambNesGui.getScreen().getImage().setRGB(horizontalPixel, scanline, backgroundPixelColor.getColorInt());
					horizontalPixel++;
				}
			}
		}
		else
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("isBackgroundClipping: " + this.getPpuControlRegister2().isBackGroundClipping());
			}
		}
	}
	
	public void doRegisterReadsWrites()
	{
		this.getPpuControlRegister1().cycle();
		this.getPpuControlRegister2().cycle();
		this.getPpuStatusRegister().cycle();
		this.getPpuSpriteDMARegister().cycle();
		this.getPpuSprRamAddressRegister().cycle();
		this.getPpuSprRamIORegister().cycle();
		this.getPpuVramAddressRegister1().cycle();
		this.getPpuVramAddressRegister2().cycle();
		this.getPpuVramIORegister().cycle();
	}

	public PPUControlRegister1 getPpuControlRegister1()
	{
		return ppuControlRegister1;
	}

	public void setPpuControlRegister1(PPUControlRegister1 ppuControlRegister1)
	{
		this.ppuControlRegister1 = ppuControlRegister1;
	}

	public PPUControlRegister2 getPpuControlRegister2()
	{
		return ppuControlRegister2;
	}

	public void setPpuControlRegister2(PPUControlRegister2 ppuControlRegister2)
	{
		this.ppuControlRegister2 = ppuControlRegister2;
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

	public PPUVramAddressRegister2 getPpuVramAddressRegister2()
	{
		return ppuVramAddressRegister2;
	}

	public void setPpuVramAddressRegister2(
			PPUVramAddressRegister2 ppuVramAddressRegister)
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

	public PPUVramAddressRegister1 getPpuVramAddressRegister1()
	{
		return ppuVramAddressRegister1;
	}

	public void setPpuVramAddressRegister1(
			PPUVramAddressRegister1 ppuVramAddressRegister1)
	{
		this.ppuVramAddressRegister1 = ppuVramAddressRegister1;
	}
}
