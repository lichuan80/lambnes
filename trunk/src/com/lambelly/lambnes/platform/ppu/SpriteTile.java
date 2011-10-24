package com.lambelly.lambnes.platform.ppu;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.*;
import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.platform.ppu.registers.PPUControlRegister;
import com.lambelly.lambnes.util.BitUtils;
import com.lambelly.lambnes.util.NumberConversionUtils;
import java.awt.image.*;  

public class SpriteTile
{
	private int spriteNumber = 0;
	private int[] patternA = null;
	private int[] patternB = null;
	private SpriteAttribute spriteAttribute = new SpriteAttribute();
	private Logger logger = Logger.getLogger(SpriteTile.class);
	
	public SpriteTile(int spriteNumber)
	{
		this.setSpriteNumber(spriteNumber);
		this.instantiateSprite();
	}
	
	public SpriteTile(int spriteNumber, SpriteAttribute attribute)
	{
		this.setSpriteNumber(spriteNumber);
		this.instantiateSprite();
		this.setSpriteAttributes(attribute);
	}
	
	public SpriteTile (SpriteTile tile)
	{
		this.setSpriteNumber(tile.getSpriteNumber());
		this.setPatternA(tile.getPatternA());
		this.setPatternB(tile.getPatternB());
	}
	
	private void instantiateSprite()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("instantiating sprite");
		}
		if (Platform.getPpu().getPpuControlRegister().getSpriteSize() == PPUControlRegister.SPRITE_SIZE_8X8)
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
				logger.debug(Platform.getPpu().getPpuControlRegister());
			}
			
		}
	}
	
	public PaletteColor[] getTileColorRow(int row)
	{
		PaletteColor colorBit[] = new PaletteColor[8];
		for (int i = 0; i < 8; i++)
		{
			PaletteColor pixel = new PaletteColor(this.getPixelSpriteColorPaletteIndex(i, row),PaletteColor.PALETTE_TYPE_SPRITE);
			colorBit[i ^ 0x07] = pixel;
		}
		
		return colorBit;
	}
	
	public int getPixelSpriteColorPaletteIndex(int column, int row)
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("generating pixel color for sprite: " + this.getSpriteAttributes().getTileIndex() + ", spriteNumber: " + this.getSpriteNumber() + " column:" + column + ", row: " + row + ", msb: " + this.getSpriteAttributes().getColorHighBit());
			logger.debug("pattern a row " + row + ": " + this.getPatternA()[row]);
			logger.debug("pattern b row " + row + ": " + this.getPatternB()[row]);
		}
		
		// horizontal flip
		if (this.getSpriteAttributes().isHorizontalFlip())
		{
			// flip column's bits
			column ^= 0x07;
		}
		
		// vertical flip
		if (this.getSpriteAttributes().isVerticalFlip())
		{
			// flip row's bits
			row ^= 0x07;
		}
		
		int color = 0;
		color = (this.getSpriteAttributes().getColorHighBit() << 2) | 
			(((BitUtils.isBitSet(this.getPatternB()[row],column))?1:0) << 1) | 
			((BitUtils.isBitSet(this.getPatternA()[row],column))?1:0);
 
		if(logger.isDebugEnabled())
		{
			logger.debug("pattern a pixel: " + ((BitUtils.isBitSet(this.getPatternA()[row],column))?1:0));
			logger.debug("pattern b pixel: " + (((BitUtils.isBitSet(this.getPatternB()[row],column))?1:0) << 1));
			logger.debug("color bitstring generated for sprite " + this.getSpriteNumber()+ ": c: " + column + ", r: " + row + ": " + Integer.toBinaryString(color));
		}
		return color;
	}
	
	public int[] getSpriteTileByteArray(int spriteNumber)
	{
		int[] sprite = new int[16];
		
		// determine address high bit (either in 0x0000 or 0x1000)
		int highBit = Platform.getPpu().getPpuControlRegister().getSpritePatternTableAddress();
		int address = (spriteNumber * 16) | (highBit * 0x1000);
		for (int x = 0; x < 16; x++)
		{
			sprite[x] = Platform.getPpuMemory().getMemoryFromHexAddress(address + x);
			
			if(logger.isDebugEnabled())
			{
				logger.debug("getting memory from " + (address + x) + " for sprite " + spriteNumber + ": " + sprite[x]);
			}
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
				PaletteColor[] p = this.getTileColorRow(row);
				
				// set pixel
				bImage.setRGB(col, row, p[col].getMasterPaletteColor().getColorInt());
			}
		}
		
		return bImage;
	}
	
	public void refreshAttributes()
	{
		this.setSpriteAttributes(new SpriteAttribute(this.getSpriteNumber()));
	}
	
	public void refreshSprite()
	{
		this.refreshAttributes();
		this.instantiateSprite();
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
