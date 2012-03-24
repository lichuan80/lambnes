package com.lambelly.lambnes.platform.ppu;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.*;

import com.lambelly.lambnes.LambNes;
import com.lambelly.lambnes.platform.ppu.registers.PPUControlRegister;
import com.lambelly.lambnes.util.BitUtils;
import com.lambelly.lambnes.util.NumberConversionUtils; 

public class BackgroundTile extends NesTile
{
	private BackgroundAttribute backgroundAttributes = new BackgroundAttribute();
	
	public BackgroundTile()
	{
		
	}
	
	public BackgroundTile(int backgroundNumber, int colorHighBit)
	{
		super(backgroundNumber, colorHighBit);
		this.setAttributes(colorHighBit);
	}
	
	public void refreshAttributes()
	{
		// this ought not really do anything. Background attributes are set in real time.
	}	
	
	public BackgroundTile(int backgroundNumber)
	{
		super(backgroundNumber);
	}
	
	public BackgroundTile(BackgroundTile background)
	{
		super(background);
	}
	
	@Override
	protected void instantiateTile()
	{
		int[] background = this.getTileByteArray(this.getTileNumber());
		this.setPatternA(ArrayUtils.subarray(background, 0, 8));
		this.setPatternB(ArrayUtils.subarray(background, 8, 16));
	}
	
	public int getPixelColorPaletteIndex(int column, int row)
	{
		int patternABit = (BitUtils.isBitSet(this.getPatternA()[row],column))?1:0;
		int patternBBit = (BitUtils.isBitSet(this.getPatternB()[row],column))?1:0;
	
		//logger.debug("generating pixel color for " + column + ", " + row);
		// logger.debug(this.toString());
		// logger.debug("pattern a row " + row + ": " + Integer.toBinaryString(this.getPatternA()[row]));
		// logger.debug("pattern b row " + row + ": " + Integer.toBinaryString(this.getPatternB()[row]));
		// logger.debug("pattern a pixel bit: " + patternABit);
		// logger.debug("pattern b pixel bit: " + patternBBit);
		// logger.debug("highbit: " + this.getAttributes().getColorHighBit());

		int lowbit = (patternBBit) << 1 | (patternABit); 
		int highbit = (this.getAttributes().getColorHighBit());
		int color = (highbit << 2) | lowbit;

		// logger.debug("color bitstring generated for pixel " + column + ", " + row + ", of tile " + this.getTileNumber() + " + msb: " + highbit + ", lsb: " + lowbit + " : " + Integer.toBinaryString(color));
		return color;
	}
	
	@Override
	public int getNameTableSelectBit()
	{
		LambNes.getPlatform();
		LambNes.getPlatform().getPpu();
		LambNes.getPlatform().getPpu().getPpuControlRegister();
		return LambNes.getPlatform().getPpu().getPpuControlRegister().getBackgroundPatternTableAddress();
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
			"\t\tcolor high bit: " + this.getAttributes().getColorHighBit() + "\n" +
			"\t\tbackground color palette index map: " + colorMapString;
	}

	public BackgroundAttribute getAttributes()
	{
		return backgroundAttributes;
	}
	
	public void setAttributes(NesTileAttribute tileAttribute)
	{
		this.backgroundAttributes = (BackgroundAttribute)tileAttribute;
	}

	public void setAttributes(int colorHighBit)
	{
		this.backgroundAttributes = new BackgroundAttribute(colorHighBit);
	}


}
