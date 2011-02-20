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
	
	private void generateSpriteAttributes(int spriteNumber)
	{
		// it appears that the spriteTile and spriteNumber ought be equal. Basically, SPR-RAM isn't necessarily in sprite order.
		boolean spriteFound = false;
		int sprAddress = 0;
		int rawBit1 = 0;
		int rawBit2 = 0;
		int rawBit3 = 0;
		int rawBit4 = 0;
		while (!spriteFound && sprAddress < 64)
		{
			// loop through SPR-RAM
			logger.debug("looking for spriteAttribute for sprite " + spriteNumber);
			rawBit1 = Platform.getPpuMemory().getSprRamFromHexAddress(sprAddress * 4);
			rawBit2 = Platform.getPpuMemory().getSprRamFromHexAddress(sprAddress * 4 + 1);
			rawBit3 = Platform.getPpuMemory().getSprRamFromHexAddress(sprAddress * 4 + 2);
			rawBit4 = Platform.getPpuMemory().getSprRamFromHexAddress(sprAddress * 4 + 3);
			
			// if not found, continue loop.
			if (rawBit2 == spriteNumber)
			{
				spriteFound = true;
			}
			else
			{
				sprAddress++;
			}
		}
		
		/*
		int sprAddress = spriteNumber * 4;
		int rawBit1 = Platform.getPpuMemory().getSprRamFromHexAddress(sprAddress);
		int rawBit2 = Platform.getPpuMemory().getSprRamFromHexAddress(sprAddress + 1);
		int rawBit3 = Platform.getPpuMemory().getSprRamFromHexAddress(sprAddress + 2);
		int rawBit4 = Platform.getPpuMemory().getSprRamFromHexAddress(sprAddress + 3);
		*/
	
		if(logger.isDebugEnabled())
		{
			logger.debug("rawBit1: " + rawBit1);
			logger.debug("rawBit2: " + rawBit2);
			logger.debug("rawBit3: " + rawBit3);
			logger.debug("rawBit4: " + rawBit4);
		}
		
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
		int colorHighBit = 0;
		if (BitUtils.isBitSet(rawBit3, 1))
		{
			BitUtils.setBit(colorHighBit, 1);
		}
		if (BitUtils.isBitSet(rawBit3, 0))
		{
			BitUtils.setBit(colorHighBit, 0);
		}
		
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
