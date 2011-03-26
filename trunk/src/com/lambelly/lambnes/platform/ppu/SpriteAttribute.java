package com.lambelly.lambnes.platform.ppu;

import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.util.BitUtils;
import org.apache.log4j.*;

public class SpriteAttribute
{
	private int tileIndex = 0;
	private boolean verticalFlip = false;
	private boolean horizontalFlip = false;
	private int backgroundPriority = 0;
	private int colorHighBit = 0;
	private int xCoordinate = 0;
	private int yCoordinate = 0;
	public static final int BACKGROUND_PRIORITY_FRONT = 0;
	public static final int BACKGROUND_PRIORITY_BEHIND = 1;
	private Logger logger = Logger.getLogger(SpriteAttribute.class);
	
	public SpriteAttribute(int spriteNumber)
	{
		this.generateSpriteAttributes(spriteNumber);
	}
	
	public SpriteAttribute(int rawBit1, int rawBit2, int rawBit3, int rawBit4)
	{
		this.parseSprRam(rawBit1, rawBit2, rawBit3, rawBit4);
	}
	
	private void generateSpriteAttributes(int spriteNumber)
	{
		int rawBit1 = 0;
		int rawBit2 = 0;
		int rawBit3 = 0;
		int rawBit4 = 0;
		
		// tileIndex and spriteNumber ought to match, so iterate the spr-ram until the right tileIndex is found
		int tileIndex = -1;
		int x = 0;
		while (tileIndex != spriteNumber && x < 64)
		{	
			tileIndex = Platform.getPpuMemory().getSprRamFromHexAddress(x * 4 + 1); 
		
			if (logger.isDebugEnabled())
			{
				logger.debug("looking at sprite attribute: " + x + " : tileIndex: " + tileIndex + " : spriteNumber: " + spriteNumber);
			}
			
			if (tileIndex == spriteNumber)
			{
				rawBit1 = Platform.getPpuMemory().getSprRamFromHexAddress(x * 4);
				rawBit2 = Platform.getPpuMemory().getSprRamFromHexAddress(x * 4 + 1);
				rawBit3 = Platform.getPpuMemory().getSprRamFromHexAddress(x * 4 + 2);
				rawBit4 = Platform.getPpuMemory().getSprRamFromHexAddress(x * 4 + 3);
			}
			x++;
		}
		
		
		if(logger.isDebugEnabled())
		{
			logger.debug("sprite " + spriteNumber + " rawBit1: " + rawBit1);
			logger.debug("sprite " + spriteNumber + " rawBit2: " + rawBit2);
			logger.debug("sprite " + spriteNumber + " rawBit3: " + rawBit3);
			logger.debug("sprite " + spriteNumber + " rawBit4: " + rawBit4);
		}

		this.parseSprRam(rawBit1, rawBit2, rawBit3, rawBit4);
	}
	
	private void parseSprRam(int rawBit1, int rawBit2, int rawBit3, int rawBit4)
	{		
		this.setyCoordinate(rawBit1);
		this.setTileIndex(rawBit2);
		
		// parse flips
		this.setVerticalFlip(BitUtils.isBitSet(rawBit3, 7));
		this.setHorizontalFlip(BitUtils.isBitSet(rawBit3, 6));
		
		// parse background priority
		if (BitUtils.isBitSet(rawBit3, 5))
		{
			this.setBackgroundPriority(SpriteAttribute.BACKGROUND_PRIORITY_BEHIND);
		}
		
		// parse color high bit
		int colorMSB = 0;
		colorMSB = ((BitUtils.isBitSet(rawBit3,1)?1:0) << 1) | (BitUtils.isBitSet(rawBit3, 0)?1:0);

		if (logger.isDebugEnabled())
		{
			logger.debug("generated MSB " + colorMSB + " from raw bit: " + Integer.toBinaryString(rawBit3));
		}
		
		this.setColorHighBit(colorMSB);
		
		this.setxCoordinate(rawBit4);
	}

	public String toString()
	{
		return "yCoordinate: " + this.getyCoordinate() + "\n" +
			"tileIndex: " + this.getTileIndex() +  "\n" +
			"verticalFlip: " + this.isVerticalFlip() + "\n" +
			"horizontalFlip: " + this.isHorizontalFlip() + "\n" +
			"backgroundPriority: " + this.isBackgroundPriority() + "\n" +
			"colorHighBit: " + this.getColorHighBit() +  "\n" +
			"xCoordinate: " + this.getxCoordinate() + "\n";
	}
	
	public int getyCoordinate()
	{
		return yCoordinate;
	}

	public void setyCoordinate(int yCoordinate)
	{
		this.yCoordinate = yCoordinate;
	}

	public int getTileIndex()
	{
		return tileIndex;
	}

	public void setTileIndex(int tileIndex)
	{
		this.tileIndex = tileIndex;
	}

	public boolean isVerticalFlip()
	{
		return verticalFlip;
	}

	public void setVerticalFlip(boolean verticalFlip)
	{
		this.verticalFlip = verticalFlip;
	}

	public boolean isHorizontalFlip()
	{
		return horizontalFlip;
	}

	public void setHorizontalFlip(boolean horizontalFlip)
	{
		this.horizontalFlip = horizontalFlip;
	}

	public int isBackgroundPriority()
	{
		return backgroundPriority;
	}

	public void setBackgroundPriority(int backgroundPriority)
	{
		this.backgroundPriority = backgroundPriority;
	}

	public int getColorHighBit()
	{
		return colorHighBit;
	}

	public void setColorHighBit(int colorHighBit)
	{
		this.colorHighBit = colorHighBit;
	}

	public int getxCoordinate()
	{
		return xCoordinate;
	}

	public void setxCoordinate(int xCoordinate)
	{
		this.xCoordinate = xCoordinate;
	}
}
