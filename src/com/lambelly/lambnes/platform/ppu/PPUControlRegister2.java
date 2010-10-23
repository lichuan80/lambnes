package com.lambelly.lambnes.platform.ppu;

import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.util.*;

public class PPUControlRegister2
{
	public static final int REGISTER_ADDRESS = 0x2001;
	public static final int FULL_BACKGROUND_COLOR_NONE = 0;
	public static final int FULL_BACKGROUND_COLOR_GREEN = 1;
	public static final int FULL_BACKGROUND_COLOR_BLUE = 2;
	public static final int FULL_BACKGROUND_COLOR_RED = 3;
	public static final int COLOR_INTENSITY_NONE = 0;
	public static final int COLOR_INTENSITY_GREEN = 1;
	public static final int COLOR_INTENSITY_BLUE = 2;
	public static final int COLOR_INTENSITY_RED = 3;
	public static final int DISPLAY_TYPE_COLOR = 0;
	public static final int DISPLAY_TYPE_MONOCHROME = 1;

	private int rawControlByte = 0;
	private int fullBackgroundColor = 0;
	private int colorIntensity = 0;
	private boolean spriteVisibility = false;
	private boolean backgroundVisibility = false;
	private boolean spriteClipping = false;
	private boolean backGroundClipping = false;
	private int displayType = 0;

	public PPUControlRegister2()
	{

	}

	public void read()
	{
		int rawControlByte = Platform.getCpuMemory().getMemoryFromHexAddress(PPUControlRegister2.REGISTER_ADDRESS);
		this.setRawControlByte(rawControlByte);
		this.setSpriteVisibility(BitUtils.isBitSet(rawControlByte, 4));
		this.setBackgroundVisibility(BitUtils.isBitSet(rawControlByte, 3));
		this.setSpriteClipping(BitUtils.isBitSet(rawControlByte, 2));
		this.setBackGroundClipping(BitUtils.isBitSet(rawControlByte, 1));
		this.setDisplayType(BitUtils.isBitSet(rawControlByte, 0)?DISPLAY_TYPE_MONOCHROME:DISPLAY_TYPE_COLOR);
		
		if (this.getDisplayType() == DISPLAY_TYPE_MONOCHROME)
		{
			// set full background color
			if (BitUtils.isBitSet(rawControlByte, 7))
			{
				this.setFullBackgroundColor(FULL_BACKGROUND_COLOR_RED);
			}
			else if (BitUtils.isBitSet(rawControlByte, 6))
			{
				this.setFullBackgroundColor(FULL_BACKGROUND_COLOR_BLUE);
			}
			else if (BitUtils.isBitSet(rawControlByte, 5))
			{
				this.setFullBackgroundColor(FULL_BACKGROUND_COLOR_GREEN);
			}
			else
			{
				this.setFullBackgroundColor(FULL_BACKGROUND_COLOR_NONE);
			}
		}
		else
		{
			// color intensity
			if (BitUtils.isBitSet(rawControlByte, 7))
			{
				this.setColorIntensity(COLOR_INTENSITY_RED);
			}
			else if (BitUtils.isBitSet(rawControlByte, 6))
			{
				this.setColorIntensity(COLOR_INTENSITY_BLUE);
			}
			else if (BitUtils.isBitSet(rawControlByte, 5))
			{
				this.setColorIntensity(COLOR_INTENSITY_GREEN);
			}
			else
			{
				this.setColorIntensity(COLOR_INTENSITY_NONE);
			}
		}
	}
	
	public int getFullBackgroundColor()
	{
		return fullBackgroundColor;
	}

	public void setFullBackgroundColor(int fullBackgroundColor)
	{
		this.fullBackgroundColor = fullBackgroundColor;
	}

	public int getColorIntensity()
	{
		return colorIntensity;
	}

	public void setColorIntensity(int colorIntensity)
	{
		this.colorIntensity = colorIntensity;
	}

	public boolean isSpriteVisibility()
	{
		return spriteVisibility;
	}

	public void setSpriteVisibility(boolean spriteVisibility)
	{
		this.spriteVisibility = spriteVisibility;
	}

	public boolean isBackgroundVisibility()
	{
		return backgroundVisibility;
	}

	public void setBackgroundVisibility(boolean backgroundVisibility)
	{
		this.backgroundVisibility = backgroundVisibility;
	}

	public boolean isSpriteClipping()
	{
		return spriteClipping;
	}

	public void setSpriteClipping(boolean spriteClipping)
	{
		this.spriteClipping = spriteClipping;
	}

	public boolean isBackGroundClipping()
	{
		return backGroundClipping;
	}

	public void setBackGroundClipping(boolean backGroundClipping)
	{
		this.backGroundClipping = backGroundClipping;
	}

	public int getDisplayType()
	{
		return displayType;
	}

	public void setDisplayType(int displayType)
	{
		this.displayType = displayType;
	}

	public int getRawControlByte()
	{
		return rawControlByte;
	}

	public void setRawControlByte(int rawControlByte)
	{
		this.rawControlByte = rawControlByte;
	}
}
