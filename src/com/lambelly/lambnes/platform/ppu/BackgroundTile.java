package com.lambelly.lambnes.platform.ppu;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.*;

import com.lambelly.lambnes.platform.NesMasterColor;
import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.util.BitUtils;
import com.lambelly.lambnes.util.NumberConversionUtils; 
import java.awt.image.*;  

public class BackgroundTile
{
	private int backgroundNumber = 0;
	private int[] patternA = null;
	private int[] patternB = null;
	private BackgroundAttribute backgroundAttribute = new BackgroundAttribute();
	private Logger logger = Logger.getLogger(BackgroundTile.class);
	
	public BackgroundTile()
	{
		
	}
	
	public BackgroundTile(int backgroundNumber)
	{
		this.setBackgroundNumber(backgroundNumber);
		this.instantiateBackground();
	}
	
	public BackgroundTile(int backgroundNumber, int colorHighBit)
	{
		this.setBackgroundNumber(backgroundNumber);
		this.setBackgroundAttributes(new BackgroundAttribute(colorHighBit));
		this.instantiateBackground();
	}
	
	public BackgroundTile(BackgroundTile tile)
	{
		this.setBackgroundNumber(tile.getBackgroundNumber());
		this.setBackgroundAttributes(new BackgroundAttribute(tile.getBackgroundAttributes().getColorHighBit()));
		this.setPatternA(tile.getPatternA());
		this.setPatternB(tile.getPatternB());
	}
	
	private void instantiateBackground()
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("instantiating background: " + this.getBackgroundNumber());
		}
		int[] background = this.getBackgroundTileByteArray(this.getBackgroundNumber());
		this.setPatternA(ArrayUtils.subarray(background, 0, 8));
		if(logger.isDebugEnabled())
		{
			logger.debug(this.getPatternA().length);
		}
		this.setPatternB(ArrayUtils.subarray(background, 8, 16));
		if(logger.isDebugEnabled())
		{
			logger.debug(this.getPatternB().length);
		}
	}
	
	public int[] getBackgroundTileByteArray(int backgroundNumber)
	{
		int[] background = new int[16];
		
		// determine address high bit (either in 0x0000 or 0x1000)
		int highBit = Platform.getPpu().getPpuControlRegister().getBackgroundPatternTableAddress();
		int address = (highBit * 0x1000) | (backgroundNumber * 16);
		for (int x = 0; x < 16; x++)
		{
			if(logger.isDebugEnabled())
			{
				logger.debug("getting memory from " + Integer.toHexString(address + x) + " for background " + backgroundNumber + " with base address " + address + " and high bit " + highBit);
			}
			background[x] = Platform.getPpuMemory().getMemoryFromHexAddress(address + x);
		}
		
		return background;
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
			PaletteColor pixel = new PaletteColor(this.getPixelBackgroundColorPaletteIndex(i, row), PaletteColor.PALETTE_TYPE_BACKGROUND);
			colorBit[i ^ 0x07] = pixel;
		}
		
		return colorBit;
	}
	
	public int getPixelBackgroundColorPaletteIndex(int column, int row)
	{
		int patternABit = (BitUtils.isBitSet(this.getPatternA()[row],column))?1:0;
		int patternBBit = (BitUtils.isBitSet(this.getPatternB()[row],column))?1:0;
	
		if(logger.isDebugEnabled())
		{
			logger.debug("generating pixel color for " + column + ", " + row);
			logger.debug(this.toString());
			logger.debug("pattern a row " + row + ": " + Integer.toBinaryString(this.getPatternA()[row]));
			logger.debug("pattern b row " + row + ": " + Integer.toBinaryString(this.getPatternB()[row]));
			logger.debug("pattern a pixel bit: " + patternABit);
			logger.debug("pattern b pixel bit: " + patternBBit);
			logger.debug("highbit: " + this.getBackgroundAttributes().getColorHighBit());
		}
		
		int lowbit = (patternBBit) << 1 | (patternABit); 
		int color = (this.getBackgroundAttributes().getColorHighBit() << 2) | lowbit;
		if(logger.isDebugEnabled())
		{
			logger.debug("color bitstring generated for " + column + ", " + row + ": " + Integer.toBinaryString(color));
		}
		return color;
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
				int backgroundPaletteIndex = this.getPixelBackgroundColorPaletteIndex(col, row);
				int masterPaletteIndex = Platform.getPpuMemory().getMemoryFromHexAddress(NesPpuMemory.BACKGROUND_PALETTE_ADDRESS + backgroundPaletteIndex); 
				int rgb = Platform.getMasterPalette().getColor(masterPaletteIndex).getColorInt();
				
				// set pixel
				bImage.setRGB(col, row, rgb);
			}
		}
		
		return bImage;
	}
	
	public String toString()
	{
		/*
		// generate color map string
		String colorMapString = "";
		for (int row = 0; row < 8; row++)
		{
			colorMapString+= "\n";
			for (int col = 0; col < 8; col++)
			{
				colorMapString += this.getPixelBackgroundColorPaletteIndex(col, row);
			}
		}
		*/
		
		return "background tile number: " + this.getBackgroundNumber() + "\n" + 
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
			"\t\tcolor high bit: " + this.getBackgroundAttributes().getColorHighBit() + "\n";
			//"\t\tbackground color palette index map: " + colorMapString;
	}

	public int getBackgroundNumber()
	{
		return backgroundNumber;
	}

	public void setBackgroundNumber(int backgroundNumber)
	{
		this.backgroundNumber = backgroundNumber;
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

	public BackgroundAttribute getBackgroundAttributes()
	{
		return backgroundAttribute;
	}

	public void setBackgroundAttributes(BackgroundAttribute backgroundAttribute)
	{
		this.backgroundAttribute = backgroundAttribute;
	}
}
