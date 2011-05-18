package com.lambelly.lambnes.platform.ppu;

import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.platform.PaletteColor;

public class PixelColor
{
	private int paletteIndex = 0;
	private int masterPaletteIndex = 0;
	private int paletteType = 0;

	public static final int PALETTE_TYPE_SPRITE = 0;
	public static final int PALETTE_TYPE_BACKGROUND = 1;
	
	public PixelColor(int paletteIndex, int paletteType)
	{
		this.setPaletteType(paletteType);
		this.setPaletteIndex(paletteIndex);
		
		// determine master palette index
		if (paletteType == PixelColor.PALETTE_TYPE_BACKGROUND)
		{
			this.setMasterPaletteIndex(Platform.getPpuMemory().getMemoryFromHexAddress(NesPpuMemory.BACKGROUND_PALETTE_ADDRESS + paletteIndex));
		}
		else
		{
			this.setMasterPaletteIndex(Platform.getPpuMemory().getMemoryFromHexAddress(NesPpuMemory.SPRITE_PALETTE_ADDRESS + paletteIndex));
		}
	}
	
	public PaletteColor getMasterPaletteColor()
	{
		return Platform.getMasterPalette().getColor(masterPaletteIndex);
	}

	public int getPaletteIndex()
	{
		return paletteIndex;
	}

	public void setPaletteIndex(int paletteIndex)
	{
		this.paletteIndex = paletteIndex;
	}

	public int getMasterPaletteIndex()
	{
		return masterPaletteIndex;
	}

	public void setMasterPaletteIndex(int masterPaletteIndex)
	{
		this.masterPaletteIndex = masterPaletteIndex;
	}

	public int getPaletteType()
	{
		return paletteType;
	}

	public void setPaletteType(int paletteType)
	{
		this.paletteType = paletteType;
	}
}
