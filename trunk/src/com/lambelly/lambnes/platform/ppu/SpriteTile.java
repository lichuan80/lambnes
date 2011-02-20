package com.lambelly.lambnes.platform.ppu;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.*;
import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.platform.ppu.registers.PPUControlRegister1;
import com.lambelly.lambnes.util.BitUtils;
import com.lambelly.lambnes.util.NumberConversionUtils;
import java.awt.image.*;  

public class SpriteTile
{
	private int spriteNumber = 0;
	private int[] patternA = null;
	private int[] patternB = null;
	
	private SpriteAttribute spriteAttribute = null;
	private Logger logger = Logger.getLogger(SpriteTile.class);
	
	public SpriteTile(int spriteNumber)
	{
		this.setSpriteNumber(spriteNumber);
		this.setSpriteAttributes(new SpriteAttribute(spriteNumber));
		this.instantiateSprite();
	}
	
	private void instantiateSprite()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("instantiating sprite");
		}
		if (Platform.getPpu().getPpuControlRegister1().getSpriteSize() == PPUControlRegister1.SPRITE_SIZE_8X8)
		{
			int[] sprite = this.getSpriteTileByteArray(this.getSpriteNumber());
			
			if (logger.isDebugEnabled())
			{
				for (int i = 0; i < 16; i++)
				{
					logger.debug("sprite array for sprite " + this.getSpriteNumber() + ": row: " + i + ": " + sprite[i]);
				}
			}
			
			this.setPatternA(ArrayUtils.subarray(sprite, 0, 8));
			if(logger.isDebugEnabled())
			{
				logger.debug(this.getPatternA().length);
			}
			this.setPatternB(ArrayUtils.subarray(sprite, 8, 16));
			if(logger.isDebugEnabled())
			{
				logger.debug(this.getPatternB().length);
			}
		}
		else
		{
			if(logger.isDebugEnabled())
			{
				logger.debug(Platform.getPpu().getPpuControlRegister1());
			}
			
		}
	}
	
	public int getPixelSpriteColorPaletteIndex(int column, int row)
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("generating pixel color for sprite: " + this.getSpriteAttributes().getTileIndex() + ", spriteNumber: " + this.getSpriteNumber() + " column:" + column + ", row: " + row);
			logger.debug("pattern a row " + row + ": " + this.getPatternA()[row]);
			logger.debug("pattern b row " + row + ": " + this.getPatternB()[row]);
		}
		
		int patternABit = (BitUtils.isBitSet(this.getPatternA()[row],column))?1:0;
		int patternBBit = (BitUtils.isBitSet(this.getPatternB()[row],column))?1:0;
		
		int lowbit = (patternBBit) << 1 | (patternABit); 
		int color = (this.getSpriteAttributes().getColorHighBit() << 2) | lowbit;
		if(logger.isDebugEnabled())
		{
			logger.debug("color bitstring generated for " + column + ", " + row + ": " + Integer.toBinaryString(color));
		}
		return color;
	}
	
	public int[] getSpriteTileByteArray(int spriteNumber)
	{
		int[] sprite = new int[16];
		
		// determine address high bit (either in 0x0000 or 0x1000)
		int highBit = Platform.getPpu().getPpuControlRegister1().getSpritePatternTableAddress();
		int address = (spriteNumber * 16) | (highBit * 0x1000);
		for (int x = 0; x < 16; x++)
		{
			if(logger.isDebugEnabled())
			{
				logger.debug("getting memory from " + (address + x) + " for sprite " + spriteNumber);
			}
			sprite[x] = Platform.getPpuMemory().getMemoryFromHexAddress(address + x);
		}
		
		return sprite;
	}
	
	public BufferedImage getBufferedImage()
	{
		int width = 8;
		int height = 8;
		// Create buffered image that does not support transparency
		BufferedImage bImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		for (int row = 0; row < 8; row++)
		{
			for (int col = 0; col < 8; col++)
			{
				// figure out color
				int spritePaletteIndex = this.getPixelSpriteColorPaletteIndex(col, row);
				int masterPaletteIndex = Platform.getPpuMemory().getMemoryFromHexAddress(NesPpuMemory.SPRITE_PALETTE_ADDRESS + spritePaletteIndex);
				int rgb = Platform.getMasterPalette().getColor(masterPaletteIndex).getColorInt();
				
				// set pixel
				bImage.setRGB(col, row, rgb);
			}
		}
		
		return bImage;
	}
	
	public String toString()
	{
		// generate color map string
		String colorMapString = "";
		for (int row = 0; row < 8; row++)
		{
			colorMapString+= "\n";
			for (int col = 0; col < 8; col++)
			{
				colorMapString += this.getPixelSpriteColorPaletteIndex(col, row);
			}
		}
		
		return "patternA: \n" +
			"\t" + NumberConversionUtils.generateBinaryStringWithleadingZeros(this.patternA[0], 8) + "\n" +
			"\t" + NumberConversionUtils.generateBinaryStringWithleadingZeros(this.patternA[1], 8) + "\n" +
			"\t" + NumberConversionUtils.generateBinaryStringWithleadingZeros(this.patternA[2], 8) + "\n" +
			"\t" + NumberConversionUtils.generateBinaryStringWithleadingZeros(this.patternA[3], 8) + "\n" +
			"\t" + NumberConversionUtils.generateBinaryStringWithleadingZeros(this.patternA[4], 8) + "\n" +
			"\t" + NumberConversionUtils.generateBinaryStringWithleadingZeros(this.patternA[5], 8) + "\n" +
			"\t" + NumberConversionUtils.generateBinaryStringWithleadingZeros(this.patternA[6], 8) + "\n" +
			"\t" + NumberConversionUtils.generateBinaryStringWithleadingZeros(this.patternA[7], 8) + "\n" +
			"patternB: \n" +
			"\t" + NumberConversionUtils.generateBinaryStringWithleadingZeros(this.patternB[0], 8) + "\n" +
			"\t" + NumberConversionUtils.generateBinaryStringWithleadingZeros(this.patternB[1], 8) + "\n" +
			"\t" + NumberConversionUtils.generateBinaryStringWithleadingZeros(this.patternB[2], 8) + "\n" +
			"\t" + NumberConversionUtils.generateBinaryStringWithleadingZeros(this.patternB[3], 8) + "\n" +
			"\t" + NumberConversionUtils.generateBinaryStringWithleadingZeros(this.patternB[4], 8) + "\n" +
			"\t" + NumberConversionUtils.generateBinaryStringWithleadingZeros(this.patternB[5], 8) + "\n" +
			"\t" + NumberConversionUtils.generateBinaryStringWithleadingZeros(this.patternB[6], 8) + "\n" +
			"\t" + NumberConversionUtils.generateBinaryStringWithleadingZeros(this.patternB[7], 8) + "\n" +
			"\t\tcolor high bit: " + this.getSpriteAttributes().getColorHighBit() + "\n" +
			"\t\tcolor map: " + colorMapString;
	}

	public int getSpriteNumber()
	{
		return spriteNumber;
	}

	public void setSpriteNumber(int spriteNumber)
	{
		this.spriteNumber = spriteNumber;
	}

	public int[] getPatternA()
	{
		return patternA;
	}

	public void setPatternA(int[] patternA)
	{
		this.patternA = patternA;
	}

	public int[] getPatternB()
	{
		return patternB;
	}

	public void setPatternB(int[] patternB)
	{
		this.patternB = patternB;
	}

	public SpriteAttribute getSpriteAttributes()
	{
		return spriteAttribute;
	}

	public void setSpriteAttributes(SpriteAttribute spriteAttributes)
	{
		this.spriteAttribute = spriteAttributes;
	}
	
	
}
