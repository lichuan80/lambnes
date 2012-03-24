package com.lambelly.lambnes.platform.ppu;

import org.apache.log4j.Logger;

import com.lambelly.lambnes.LambNes;
import com.lambelly.lambnes.platform.NesMasterPalette;
import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.platform.MasterColor;

public class PaletteColor
{
	private int paletteIndex = 0;
	private int masterPaletteIndex = 0;
	private int paletteType = 0;

	public static final int PALETTE_TYPE_SPRITE = 0;
	public static final int PALETTE_TYPE_BACKGROUND = 1;
	
	private Logger logger = Logger.getLogger(PaletteColor.class);
	
	public PaletteColor(int paletteIndex, int paletteType)
	{
		this.setPaletteType(paletteType);
		this.setPaletteIndex(paletteIndex);
		
		// determine master palette index
		int address = 0;
		if (paletteType == PaletteColor.PALETTE_TYPE_BACKGROUND)
		{
			address = NesPpuMemory.BACKGROUND_PALETTE_ADDRESS + paletteIndex;
		}
		else
		{
			address = NesPpuMemory.SPRITE_PALETTE_ADDRESS + paletteIndex;
		}
		this.setMasterPaletteIndex(LambNes.getPlatform().getPpuMemory().getMemoryFromHexAddress(address));
		
		//logger.info("instantiated with MPI from " + address + " : " + this.toString());
	}
	
	public String toString()
	{
		return "MPI: " + this.getMasterPaletteIndex() + " paletteIndex: " + paletteIndex + " paletteType: " + this.getPaletteType();
	}
	
	public MasterColor getMasterPaletteColor()
	{
		return NesMasterPalette.getInstance().getColor(masterPaletteIndex);
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
