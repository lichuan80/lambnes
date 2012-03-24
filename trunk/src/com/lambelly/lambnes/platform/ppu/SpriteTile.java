package com.lambelly.lambnes.platform.ppu;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.*;

import com.lambelly.lambnes.LambNes;
import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.platform.ppu.registers.PPUControlRegister;
import com.lambelly.lambnes.util.BitUtils;
import com.lambelly.lambnes.util.NumberConversionUtils;
import java.awt.image.*;  

public class SpriteTile extends NesTile
{
	private int spriteNumber = 0;
	private int[] patternA = null;
	private int[] patternB = null;
	private SpriteAttribute spriteAttribute = new SpriteAttribute();
	private Logger logger = Logger.getLogger(SpriteTile.class);
	
	public SpriteTile(int spriteNumber)
	{
		this.setSpriteNumber(spriteNumber);
		this.instantiateTile();
	}
	
	public SpriteTile(int spriteNumber, SpriteAttribute attribute)
	{
		this.setSpriteNumber(spriteNumber);
		this.instantiateTile();
		this.setAttributes(attribute);
	}
	
	public SpriteTile (SpriteTile tile)
	{
		this.setSpriteNumber(tile.getSpriteNumber());
		this.setPatternA(tile.getPatternA());
		this.setPatternB(tile.getPatternB());
	}
	
	public int[] getTileByteArray(int tileNumber)
	{
		int spriteLength;
		int address;
		
		// determine length and address -- 8x16 logic
		if (LambNes.getPlatform().getPpu().getPpuControlRegister().getSpriteSize() == PPUControlRegister.SPRITE_SIZE_8X8)
		{
			address = (this.getNameTableSelectBit() * 0x1000) | (tileNumber * 16);
			spriteLength = 16;
		}
		else
		{
			address = (BitUtils.isBitSet(tileNumber, 0)?1:0 * 0x1000) | (tileNumber * 16);
			spriteLength = 32;
		}
		
		int[] tile = new int[spriteLength];
		
		for (int x = 0; x < spriteLength; x++)
		{
			int value = LambNes.getPlatform().getPpuMemory().getMemoryFromHexAddress(address + x);
		
			// logger.debug("getting memory from " + Integer.toHexString(address + x) + " for tile " + tileNumber + " with base address " + address + " and high bit " + this.getNameTableSelectBit() + ": " + value);
			
			tile[x] = value;
		}		
		
		return tile;
	}	
	
	protected void instantiateTile()
	{
		// logger.debug("instantiating sprite");
		
		int[] sprite = this.getTileByteArray(this.getSpriteNumber());			
		
		// for (int i = 0; i < sprite.length; i++)
		// {
		// 		logger.debug("sprite array for sprite " + this.getSpriteNumber() + ": row: " + i + ": " + sprite[i]);
		// }
		
		this.setPatternA(ArrayUtils.subarray(sprite, 0, sprite.length / 2));
		this.setPatternB(ArrayUtils.subarray(sprite, sprite.length / 2, sprite.length));
	}
	
	public PaletteColor[] getTileColorRow(int row)
	{
		PaletteColor colorBit[] = new PaletteColor[8];
		for (int i = 0; i < 8; i++)
		{
			PaletteColor pixel = new PaletteColor(this.getPixelColorPaletteIndex(i, row),PaletteColor.PALETTE_TYPE_SPRITE);
			colorBit[i ^ 0x07] = pixel;
		}
		
		return colorBit;
	}
	
	public int getPixelColorPaletteIndex(int column, int row)
	{
		// staleness check -- occurs sometimes
		if (this.getPatternA() == null) 
		{
			//logger.info("refreshing tile");
			this.refreshTile();
		}
		
		// logger.debug("generating pixel color for sprite: " + this.getAttributes().getTileIndex() + ", spriteNumber: " + this.getSpriteNumber() + " column:" + column + ", row: " + row + ", msb: " + this.getAttributes().getColorHighBit());
		// logger.debug("pattern a row " + row + ": " + this.getPatternA()[row]);
		// logger.debug("pattern b row " + row + ": " + this.getPatternB()[row]);
		
		// horizontal flip
		if (this.getAttributes().isHorizontalFlip())
		{
			// flip column's bits
			column ^= 0x07;
		}
		
		// vertical flip
		if (this.getAttributes().isVerticalFlip())
		{
			// flip row's bits
			row ^= 0x07;
		}
		
		int color = 0;
		color = (this.getAttributes().getColorHighBit() << 2) | 
			(((BitUtils.isBitSet(this.getPatternB()[row],column))?1:0) << 1) | 
			((BitUtils.isBitSet(this.getPatternA()[row],column))?1:0);
 
		// logger.debug("pattern a pixel: " + ((BitUtils.isBitSet(this.getPatternA()[row],column))?1:0));
		// logger.debug("pattern b pixel: " + (((BitUtils.isBitSet(this.getPatternB()[row],column))?1:0) << 1));
		// logger.debug("color bitstring generated for sprite " + this.getSpriteNumber()+ ": c: " + column + ", r: " + row + ": " + Integer.toBinaryString(color));
		return color;
	}
	
	public void refreshAttributes()
	{
		this.setAttributes(LambNes.getPlatform().getPpuMemory().getSprRamForSpriteNumber(this.getSpriteNumber()));
	}
	
	@Override
	public int getNameTableSelectBit()
	{
		return LambNes.getPlatform().getPpu().getPpuControlRegister().getSpritePatternTableAddress();
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
				colorMapString += this.getPixelColorPaletteIndex(col, row);
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
			"\t\tcolor high bit: " + this.getAttributes().getColorHighBit() + "\n" +
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

	public SpriteAttribute getAttributes()
	{
		return spriteAttribute;
	}
	
	public void setAttributes(int spriteNumber)
	{
		this.spriteAttribute = LambNes.getPlatform().getPpuMemory().getSprRamForSpriteNumber(spriteNumber);
	}
	
	public void setAttributes(NesTileAttribute spriteAttributes)
	{
		this.spriteAttribute = (SpriteAttribute)spriteAttributes;
	}
}
