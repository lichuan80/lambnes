package com.lambelly.lambnes.platform.ppu;

import org.apache.log4j.*;

public class BackgroundAttribute
{
	private int colorHighBit = 0;
	private Logger logger = Logger.getLogger(BackgroundAttribute.class);
	
	public BackgroundAttribute(int colorHighBit)
	{
		this.setColorHighBit(colorHighBit);
	}

	public String toString()
	{
		return "color high bit: " + this.getColorHighBit();
	}

	public int getColorHighBit()
	{
		return colorHighBit;
	}

	public void setColorHighBit(int colorHighBit)
	{
		this.colorHighBit = colorHighBit;
	}

}
