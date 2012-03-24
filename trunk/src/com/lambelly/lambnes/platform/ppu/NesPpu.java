package com.lambelly.lambnes.platform.ppu;

import com.lambelly.lambnes.LambNes;
import com.lambelly.lambnes.gui.*;
import com.lambelly.lambnes.platform.*;
import com.lambelly.lambnes.platform.interrupts.InterruptRequest;
import com.lambelly.lambnes.platform.interrupts.NesInterrupts;

import com.lambelly.lambnes.platform.ppu.registers.PPUControlRegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUMaskRegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUSprRamAddressRegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUSprRamIORegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUSpriteDMARegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUStatusRegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUScrollRegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUVramAddressRegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUVramIORegister;

import org.apache.log4j.*;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

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
	private int scanlineCount = 0;
	private int verticalPerTileCount = 0;  
	private int registerAddressFlipFlopLatch = 0;
	private int loopyT = 0;
	private int loopyV = 0;
	private int loopyX = 0;
    private int ppuCyclesUntilEndOfFrame = NesPpu.PPU_CYCLES_PER_FRAME;
    private int ppuCyclesUntilEndOfLine = NesPpu.PPU_CYCLES_PER_LINE;
    private long vblankInterval = 0;
    private ScreenBuffer screenBuffer = new ScreenBuffer();
    private long screenCount = 0;
    private NesPpuMemory ppuMemory;
    private PPUSprRamIORegister ppuSprRamIORegister;
    private PPUSprRamAddressRegister ppuSprRamAddressRegister;
    private PPUControlRegister ppuControlRegister;
    private PPUStatusRegister ppuStatusRegister;
    private PPUVramAddressRegister ppuVramAddressRegister;
    private PPUVramIORegister ppuVramIORegister;
    private PPUScrollRegister ppuScrollRegister;
    private PPUSpriteDMARegister ppuSpriteDmaRegister;
    private PPUMaskRegister ppuMaskRegister;
    private NesInterrupts interrupts;
	private Logger logger = Logger.getLogger(NesPpu.class);
	
	public void cycle(int cpuCycleCount)
	{			
		//logger.info("scanline " + this.getScanlineCount() + " at cpu " + Platform.getCycleCount());
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
    				//logger.info("entering vblank at cycle " + Platform.getCycleCount() + " and scanline " + this.getScanlineCount());
    				this.getPpuStatusRegister().setVblank(true);
        			this.vblankInterval = Platform.getCycleCount();
        			
        			if (this.getPpuControlRegister().isExecuteNMIOnVBlank())
        			{   
        				interrupts.addInterruptRequestToQueue(new InterruptRequest(InterruptRequest.interruptTypeNMI));
        			}
    			}
        		// vblank end
    			else if (getScanlineCount() == NesPpu.VBLANK_SCANLINE_END)
        		{
        			//logger.info("resetting vblank at cycle " + Platform.getCycleCount() + " and scanline " + this.getScanlineCount() + " vblank interval: " + (Platform.getCycleCount() - this.vblankInterval));
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
    			
    			// update loopyV with xScroll
    			// At pixel #257 of the scanline, if background and sprites are enabled, these bits get copied:
    			// v:.....x.....xxxxx=t:.....x.....xxxxx
    			int setLoopyV = (this.getLoopyV() & 0xFBE0) | (this.getLoopyT() & 0x41F);
    			this.setLoopyV(setLoopyV);
    		}
    		
    		this.addToPpuCyclesUntilEndOfLine(NesPpu.PPU_CYCLES_PER_LINE);
    	}
    	
    	if (this.getPpuCyclesUntilEndOfFrame() <= 0)
    	{
    		//logger.debug("new screen " + this.getScreenCount() + " at cpu cycle " + Platform.getCycleCount() + " and scanline " + this.getScanlineCount() + " cycles until end of frame: " + this.getPpuCyclesUntilEndOfFrame() + " cpuCycleCount: " + cpuCycleCount);
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
			//logger.info("drawing background for " + scanline);
			this.drawBackground(scanline);
		}
		
        // update and draw sprite buffer 
        if (this.getPpuMaskRegister().isSpriteVisibility())
        {
        	// use sprite s currently in buffer
        	//logger.info("drawing sprites for " + scanline);
            drawSprites(scanline);
            
            // clear buffer
            this.getPpuMemory().clearSpriteBuffer();
            
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
    	//logger.info("using pattern memory for sprite tiles: " + (this.getPpuControlRegister().getSpritePatternTableAddress() == PPUControlRegister.SPRITE_PATTERN_TABLE_ADDRESS_1000 ? 1000 : 0));
    	//logger.info("using pattern memory for background tiles: " + (this.getPpuControlRegister().getBackgroundPatternTableAddress() == PPUControlRegister.BACKGROUND_PATTERN_TABLE_ADDRESS_1000 ? 1000 : 0));
    	//logger.info("previousSpriteCount: " + ppuMemory.getSpritesInBufferCount());
    	//logger.info("vline: " + verticalPerPixelCount + " sprite buffer size: " + this.getPpuMemory().getSpritesInBufferCount());
        
    	if (this.getPpuMemory().getSpritesInBufferCount() > 0)
        {
            for (int i = 0 ; i < this.getPpuMemory().getSpritesInBufferCount(); i++)
            {
                SpriteTile sprite = this.getPpuMemory().getSpriteFromBuffer(i);                
                
                if (drawSpriteTileLine(sprite,verticalPerPixelCount) && (!this.getPpuStatusRegister().isSprite0Occurance()))
                {
                	// logger.debug("sprite0 true at: scanline: " + this.getScanlineCount() + " screencount: " + this.getScreenCount());
                	this.getPpuStatusRegister().setSprite0Occurance(true);                        
                }
            }
        }
    }
	
	private boolean drawSpriteTileLine(SpriteTile sprite, int verticalPerPixelCount)
	{
		boolean sprite0Triggered = false;
        int spriteLine = verticalPerPixelCount - sprite.getAttributes().getyCoordinate();
        
        //logger.info("drawing sprite " + sprite.getSpriteNumber() + " at " + verticalPerPixelCount);
        //logger.info("spriteLine: " + spriteLine + " sprite: " + sprite.getSpriteNumber() + " scanline: " + verticalPerPixelCount + " y coord: " + sprite.getAttributes());
        //logger.info("sprite attributes for sprite: " + sprite.getAttributes().getTileIndex() + "\n" + sprite.getAttributes());
        //logger.info("sprite: " + sprite);
        
        //TODO -- 8x16 logics
        /** unused for the moment. 
            tileNum = (sprite.getSpriteNumber() & 0xFE) | (spriteLine >> 3);
		*/
        
        // get one sprite line and draw it pixel by pixel
        if (this.getPpuControlRegister().getSpriteSize() == PPUControlRegister.SPRITE_SIZE_8X8)
        {
        	spriteLine = spriteLine & 0x7; // if more than the index, roll over
        }
        else
        {
        	spriteLine = spriteLine & 0xF; // if more than the index, roll over
        }
        int spriteXPosition = sprite.getAttributes().getxCoordinate();
    	int sprite0Number = this.getPpuMemory().getSprRam(0).getTileIndex();
        
        // TODO -- should this be the way that transparentColor is determined? It probably should be part of an object so it can be reused.
        PaletteColor spriteTransparentColor = new PaletteColor(0, PaletteColor.PALETTE_TYPE_SPRITE);
        PaletteColor backgroundTransparentColor =  new PaletteColor(0, PaletteColor.PALETTE_TYPE_BACKGROUND);
        
        // go through each pixel of one current line being drawn for sprite
        PaletteColor[] spriteRow = sprite.getTileColorRow(spriteLine);
        for (int pixelIndex = 0; pixelIndex < spriteRow.length; pixelIndex++)
        {	
        	// get pixel
        	PaletteColor pixelToBeDrawn = spriteRow[pixelIndex];
        	
        	// check sprite visibility
        	if (pixelToBeDrawn.getMasterPaletteIndex() != spriteTransparentColor.getMasterPaletteIndex())                  
	        {   
        		// logger.debug("sprite 0 logic: sprite visible");
        		// check background visibility
        		if (LambNesGui.getScreen().getImage().getRGB(((spriteXPosition + pixelIndex) & Platform.EIGHT_BIT_MASK), verticalPerPixelCount) != backgroundTransparentColor.getMasterPaletteColor().getColorInt())
        		{
        			//logger.debug("sprite 0 logic: background visible");
	        	    // sprite 0 logic
		        	if (sprite.getSpriteNumber() == sprite0Number && !this.getPpuStatusRegister().isSprite0Occurance())                   
		        	{                     
		        		sprite0Triggered = true;
		        		// logger.debug("sprite0 logic: sprite0Triggered: " + sprite0Triggered + " spriteNumber: " + sprite.getSpriteNumber() + " sprite0 number: " + sprite0Number + " verticalPerPixelCount: " + verticalPerPixelCount + " vcoord: " + sprite.getAttributes().getyCoordinate() + " xcoord: " + sprite.getAttributes().getxCoordinate() + " for screen " + this.getScreenCount());
		        		// logger.debug("background color: " + LambNesGui.getScreen().getImage().getRGB((spriteXPosition & Platform.EIGHT_BIT_MASK), verticalPerPixelCount));
		        		// logger.debug("background transparent color: " +  backgroundTransparentColor.getMasterPaletteColor().getColorInt());
		        		// logger.debug("using sprite0: " + sprite);
		        	}
	        	}   
        		else
        		{
        			// background invisible
        		}
        		
            	// draw pixel
            	if (spriteXPosition >= 8 || this.getPpuMaskRegister().isSpriteVisibility())                      
            	{
                    //logger.info("drawing pixel " + pixelIndex + " of line " + spriteLine + " of sprite number: " + sprite.getSpriteNumber() +  " index: " + sprite.getAttributes().getTileIndex() + " at y: " + verticalPerPixelCount + " x: " + (spriteXPosition & Platform.EIGHT_BIT_MASK) + " sprite0: " + this.getPpuStatusRegister().isSprite0Occurance() + " color: " + pixelToBeDrawn.getMasterPaletteIndex());
            		this.getScreenBuffer().setScreenBufferPixel((spriteXPosition & Platform.EIGHT_BIT_MASK),verticalPerPixelCount,pixelToBeDrawn);
            		//this.getScreenBuffer().toFile("_screen_" + this.getScreenCount() + "_scanline_" + this.getScanlineCount()); // debug ppu sprite
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
        
        // get all sprites that need to be added to buffer
        for (int spriteAttributeIndexNumber = 0; spriteAttributeIndexNumber < SPRITE_COUNT; spriteAttributeIndexNumber++)
        {
        	SpriteAttribute spriteAttribute = this.getPpuMemory().getSprRam(spriteAttributeIndexNumber);
        	
        	// logger.debug("scanline: " + verticalPerPixelCount + " tileIndex: " + spriteAttribute.getTileIndex() + " spriteYcoordinate: " + spriteAttribute.getyCoordinate());
        	
            //   determine if Y coord is being drawn (for line + 1) 
            int diff = ((verticalPerPixelCount + 1) - spriteAttribute.getyCoordinate());
            
            if ((diff >= 0 && diff <= 7) || ((this.getPpuControlRegister().getSpriteSize() == PPUControlRegister.SPRITE_SIZE_8X16) && (diff >= 0 && diff <= 15)))
            {	
            	SpriteTile sprite = new SpriteTile(spriteAttribute.getTileIndex(), spriteAttribute); 
            	//logger.info("adding sprite to buffer: scanline: " + verticalPerPixelCount + " tileIndex: " + spriteAttribute.getTileIndex() + " spriteYcoordinate: " + spriteAttribute.getyCoordinate() + " diff: " + diff + " spritecount: " + spriteCount + " spriteNumber: " + sprite.getSpriteNumber());

            	this.getPpuMemory().setSpriteToBuffer(spriteCount, sprite);
                spriteCount++;
                
                if (spriteCount == 9 )
                {
                	// logger.debug("sprite overflow");
                    spriteCount = 8;
                    this.getPpuStatusRegister().setScanlineSpriteCount(true);
                    break;
                }
            }
        }   
    }
    
    private void drawBackground(int scanline)
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
    	
		// iterate one 8x8 pixel line in name table
		for (int horizontalPerTileCount = 0; horizontalPerTileCount < NesPpu.NUM_HORIZONTAL_TILES; horizontalPerTileCount++)
		{
			//hscroll
			int hCoarseScrollOffset = (this.getLoopyV() & 0x1F);
			int offsetHorizontalPerTileCount = horizontalPerTileCount + hCoarseScrollOffset; // coarse x
			int hFineScrollOffset = (this.getLoopyX() & 0x07);
			
			// vscroll
			int vCoarseScrollOffset = ((this.getLoopyV() & 0x3E0) >> 5);
			int offsetVerticalPerTileCount = this.getVerticalPerTileCount() + vCoarseScrollOffset;
			int vFineScrollOffset = ((this.getLoopyV() & 0x7000) >> 12);
			
    		// nametable pointer
			int horizontalNameCount = this.getPpuControlRegister().getNameTableAddress();
			int verticalNameCount = this.getPpuControlRegister().getNameTableAddress();
			
			// tileYindex -- the row that actually gets drawn
			int tileYindex = (scanline & 0x7);
			tileYindex += vFineScrollOffset;
			if (tileYindex > 0x7)
			{
				// flip to next tile row.
				tileYindex = (tileYindex & 0x7);
				offsetVerticalPerTileCount++;
			}
			
			// see if hscroll is outside nametable, if so flip horiz
			if ((offsetHorizontalPerTileCount * 8) + hFineScrollOffset > Screen.SCREEN_HORIZONTAL_RESOLUTION)
			{
				// flip horiz
				horizontalNameCount = (1 ^ horizontalNameCount);
				
				// circle nameTable 
				offsetHorizontalPerTileCount &= 0x1F;
			}
			
			// see if vscroll is outside nametable, if so flip vert
			if ((offsetVerticalPerTileCount * 8) + vFineScrollOffset >= Screen.SCREEN_VERTICAL_RESOLUTION)
			{
				// flip vert
				verticalNameCount = (1 ^ verticalNameCount);
				
				// circle nameTable 
				offsetVerticalPerTileCount = offsetVerticalPerTileCount % NesPpu.NUM_VERTICAL_TILES;
			}
			
			//essentially creates an integer that represents the offset from start of name table (0x2000).
			int nameTableAddress = 0x2000 + offsetHorizontalPerTileCount | (offsetVerticalPerTileCount << 5) | (horizontalNameCount << 10) | (verticalNameCount << 11); 
			
			//if (logger.isDebugEnabled())
			//{
				//logger.info("name table control: " + Platform.getPpu().getPpuControlRegister().getNameTableAddress());
				//logger.info("hscroll offset: " + hCoarseScrollOffset);
				//logger.info("offsetHorizontalPerTileCount: " + offsetHorizontalPerTileCount);
				//logger.info("horizontalPerTileCount: " + horizontalPerTileCount);
				//logger.info("verticalPerTileCount: " + this.getVerticalPerTileCount());
				//logger.info("horizontalNameCount: " + this.getHorizontalNameCount());
				//logger.info("verticalNameCount: " + this.getVerticalNameCount());
				//logger.info("loopyV: " + this.getLoopyV() + " loopyT: " + loopyT + " loopyX: " + loopyX + " vCoarseScrollOffset: " + vCoarseScrollOffset + " vFineScrollOffset: " + vFineScrollOffset +" at scanline: " + this.getScanlineCount() + " screen: " + this.getScreenCount());
				//logger.info("scanline: " + this.getScanlineCount() + " for screen " + this.getScreenCount());
				//logger.info("looking at address: 0x" + Integer.toHexString(nameTableAddress) + " horizontalPerTileCount: " + horizontalPerTileCount + " vOffset: " + offsetVerticalPerTileCount + " scanline: " + this.getScanlineCount() + " for screen " + this.getScreenCount());
			//if (loopyV > 0)
			//{
				//logger.info("looking at address: 0x" + Integer.toHexString(nameTableAddress) + " loopyV: " + loopyV + " vFineScrollOffset: " + vFineScrollOffset +" vCoarseScrollOffset: " + vCoarseScrollOffset + " offsetHorizontalPerTileCount: " + offsetHorizontalPerTileCount + " offsetVerticalPerTileCount: " + offsetVerticalPerTileCount + " horizontalNameCount: " + horizontalNameCount + " verticalNameCount: " + verticalNameCount + " scanline: " + this.getScanlineCount() + " for screen " + this.getScreenCount());
				//logger.info("pulled from name table: 0x" + Integer.toHexString(Platform.getPpuMemory().getMemoryFromHexAddress(nameTableAddress)));
				//logger.info("vCoarseScrollOffset: " + vCoarseScrollOffset);
				//logger.info("offsetVerticalPerTileCount: " + offsetVerticalPerTileCount);
			//}
			//} 
		
			drawBackgroundTile(nameTableAddress, horizontalPerTileCount, hFineScrollOffset, vFineScrollOffset, tileYindex, scanline);
		}
    }
	
	private void drawBackgroundTile(int nameTableAddress, int horizontalPerTileCount, int hFineScrollOffset, int vFineScrollOffset, int tileYindex, int scanline)
	{	
		if (horizontalPerTileCount >= 1 || !this.getPpuMaskRegister().isBackGroundClipping())
		{
			PaletteColor[] tileRow = new PaletteColor[8];

			// select tile row to write.
			BackgroundTile bg = this.getPpuMemory().getNameTableFromHexAddress(nameTableAddress).getTileFromHexAddress(nameTableAddress);
			
			//if (vFineScrollOffset > 0)
			//{
				//logger.info("drawing bg tile: " + bg);
				//logger.info("drawing line " + tileYindex + " of bg tile: " + bg.getTileNumber() + " at position " + horizontalPerTileCount + " on line " + this.getScanlineCount() + " on screen " + this.getScreenCount());
				//logger.info("tile y index: " + tileYindex + " vFineScrollOffset: " + vFineScrollOffset + " nameTableAddress: " + nameTableAddress + " scanline: " + this.getScanlineCount() + " for screen " + this.getScreenCount());
			//}
			
			System.arraycopy(bg.getTileColorRow(tileYindex), 0, tileRow, 0, (tileRow.length));
			this.getScreenBuffer().setScreenBufferTileRow((horizontalPerTileCount * 8), scanline, hFineScrollOffset, vFineScrollOffset, tileRow); 
		}
		else
		{
			// logger.debug("isBackgroundClipping: " + this.getPpuMaskRegister().isBackGroundClipping());
		}
	}
	
	private void doRegisterReadsWrites()
	{
		this.getPpuControlRegister().cycle();
		this.getPpuMaskRegister().cycle();
		this.getPpuStatusRegister().cycle();
		this.getPpuSpriteDmaRegister().cycle();
		this.getPpuSprRamAddressRegister().cycle();
		this.getPpuSprRamIORegister().cycle();
		this.getPpuScrollRegister().cycle();
		this.getPpuVramAddressRegister().cycle();
		this.getPpuVramIORegister().cycle();
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

	public NesPpuMemory getPpuMemory()
    {
    	return ppuMemory;
    }

	public void setPpuMemory(NesPpuMemory ppuMemory)
    {
    	this.ppuMemory = ppuMemory;
    }

	public PPUSprRamIORegister getPpuSprRamIORegister()
    {
    	return ppuSprRamIORegister;
    }

	public void setPpuSprRamIORegister(PPUSprRamIORegister ppuSprRamIORegister)
    {
    	this.ppuSprRamIORegister = ppuSprRamIORegister;
    }

	public PPUControlRegister getPpuControlRegister()
    {
    	return ppuControlRegister;
    }

	public void setPpuControlRegister(PPUControlRegister ppuControlRegister)
    {
    	this.ppuControlRegister = ppuControlRegister;
    }

	public PPUStatusRegister getPpuStatusRegister()
    {
    	return ppuStatusRegister;
    }

	public void setPpuStatusRegister(PPUStatusRegister ppuStatusRegister)
    {
    	this.ppuStatusRegister = ppuStatusRegister;
    }

	public PPUVramAddressRegister getPpuVramAddressRegister()
    {
    	return ppuVramAddressRegister;
    }

	public void setPpuVramAddressRegister(
            PPUVramAddressRegister ppuVramAddressRegister)
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

	public PPUScrollRegister getPpuScrollRegister()
    {
    	return ppuScrollRegister;
    }

	public void setPpuScrollRegister(PPUScrollRegister ppuScrollRegister)
    {
    	this.ppuScrollRegister = ppuScrollRegister;
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

	public PPUSpriteDMARegister getPpuSpriteDmaRegister()
    {
    	return ppuSpriteDmaRegister;
    }

	public void setPpuSpriteDmaRegister(PPUSpriteDMARegister ppuSpriteDmaRegister)
    {
    	this.ppuSpriteDmaRegister = ppuSpriteDmaRegister;
    }

	public PPUMaskRegister getPpuMaskRegister()
    {
    	return ppuMaskRegister;
    }

	public void setPpuMaskRegister(PPUMaskRegister ppuMaskRegister)
    {
    	this.ppuMaskRegister = ppuMaskRegister;
    }

	public NesInterrupts getInterrupts()
    {
    	return interrupts;
    }

	public void setInterrupts(NesInterrupts interrupts)
    {
    	this.interrupts = interrupts;
    }
}
