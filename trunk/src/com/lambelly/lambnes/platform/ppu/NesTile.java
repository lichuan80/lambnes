package com.lambelly.lambnes.platform.ppu;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.*;

import com.lambelly.lambnes.LambNes;
import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.platform.ppu.registers.PPUControlRegister;
import com.lambelly.lambnes.util.BitUtils;
import com.lambelly.lambnes.util.NumberConversionUtils; 
import java.awt.image.*;  

public abstract class NesTile
{
	protected int tileNumber = 0;
	protected int[] patternA = null;
	protected int[] patternB = null;
	protected Logger logger = Logger.getLogger(NesTile.class);
	
	public NesTile()
	{
		
	}
	
	public NesTile(int tileNumber)
	{
		this.setTileNumber(tileNumber);
		this.instantiateTile();
	}
	
	public NesTile(int tileNumber, int colorHighBit)
	{
		this.setTileNumber(tileNumber);
		this.setAttributes(colorHighBit);
		this.instantiateTile();
	}
	
	public NesTile(NesTile tile)
	{
		this.setTileNumber(tile.getTileNumber());
		this.setAttributes(tile.getAttributes().getColorHighBit());
		this.setPatternA(tile.getPatternA());
		this.setPatternB(tile.getPatternB());
	}
	
	protected abstract void instantiateTile();

	public abstract void refreshAttributes();	
	
	public void refreshTile()
	{
		this.refreshAttributes();
		this.instantiateTile();
	}
	
	public int[] getTileByteArray(int tileNumber)
	{
		int spriteLength;
		int address;
		
		address = (this.getNameTableSelectBit() * 0x1000) | (tileNumber * 16);
		spriteLength = 16;
		
		int[] tile = new int[spriteLength];
		
		for (int x = 0; x < spriteLength; x++)
		{
			int value = LambNes.getPlatform().getPpuMemory().getMemoryFromHexAddress(address + x);
		
			// logger.debug("getting memory from " + Integer.toHexString(address + x) + " for tile " + tileNumber + " with base address " + address + " and high bit " + this.getNameTableSelectBit() + ": " + value);
			
			tile[x] = value;
		}		
		
		return tile;
	}	
	
	public PaletteColor getTileColorRowPixel(int row, int column)
	{
		return this.getTileColorRow(row)[column];
	}
	
	public PaletteColor[] getTileColorRow(int row)
	{
		PaletteColor colorBit[] = new PaletteColor[8];
		for (int i = 0; i < 8; i++)
		{
			PaletteColor pixel = new PaletteColor(this.getPixelColorPaletteIndex(i, row), PaletteColor.PALETTE_TYPE_BACKGROUND);
			//logger.info("generated MPI: " + pixel + " for tile " + this.getTileNumber());
			colorBit[i ^ 0x07] = pixel;
		}
		
		return colorBit;
	}
	
	public abstract int getPixelColorPaletteIndex(int column, int row);
	
	public BufferedImage getBufferedImage()
	{
		int width = 8;
		int height = 8;
		// Create buffered image that does not support transparency
		BufferedImage bImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		for (int row = 0; row < 8; row++)
		{
			PaletteColor[] p = this.getTileColorRow(row);
			
			// logger.debug("row " + row + " of background + " + Integer.toHexString(this.getTileNumber()) + ": " + ArrayUtils.toString(p));
			
			for (int col = 0; col < 8; col++)
			{
				// set pixel
				bImage.setRGB(col, row, p[col].getMasterPaletteColor().getColorInt());
			}
		}
		
		return bImage;
	}
	
	public abstract int getNameTableSelectBit();
	
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
		
		return "background tile number: " + this.getTileNumber() + "\n" + 
			"patternA: \n" +
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
			"\t\tbackground color palette index map: " + colorMapString;
	}
	
	public int getTileNumber()
	{
		return tileNumber;
	}

	public void setTileNumber(int tileNumber)
	{
		this.tileNumber = tileNumber;
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
	
	public abstract NesTileAttribute getAttributes();

	public abstract void setAttributes(int colorHighBit);
	
	public abstract void setAttributes(NesTileAttribute tileAttribute);

}
