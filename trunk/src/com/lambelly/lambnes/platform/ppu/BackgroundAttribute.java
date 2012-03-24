package com.lambelly.lambnes.platform.ppu;

public class BackgroundAttribute extends NesTileAttribute
{
	public BackgroundAttribute()
	{
		super();
	}
	
	public BackgroundAttribute(int colorHighBit)
	{
		super(colorHighBit);
		super.setColorHighBit(colorHighBit);
	}
}
