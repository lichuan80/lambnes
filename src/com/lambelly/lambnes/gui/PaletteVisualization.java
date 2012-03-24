package com.lambelly.lambnes.gui;

import java.awt.*;  
import javax.swing.*;
import com.lambelly.lambnes.platform.MasterColor;
import com.lambelly.lambnes.platform.NesMasterPalette;
import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.platform.ppu.NesPpuMemory;

import org.apache.log4j.*;

public class PaletteVisualization extends JPanel
{
	private Logger logger = Logger.getLogger(PaletteVisualization.class);
	private int paletteMemoryBaseAddress = 0;
	private PaletteLabel[] paletteLabels = null;
	private NesPpuMemory ppuMemory;
	public static final int SCREEN_HORIZONTAL_RESOLUTION = 256;
	public static final int SCREEN_VERTICAL_RESOLUTION = 240;
	
	public PaletteVisualization(int paletteMemoryBaseAddress, int paletteSize)
	{
		super(new GridLayout((int)Math.sqrt(paletteSize),(int)Math.sqrt(paletteSize)));
		
		if(logger.isDebugEnabled())
		{
			logger.debug("initializing");
		}
    	this.setBounds(0, 0, PaletteVisualization.SCREEN_HORIZONTAL_RESOLUTION, PaletteVisualization.SCREEN_VERTICAL_RESOLUTION);
    	this.setPreferredSize(new Dimension(PaletteVisualization.SCREEN_HORIZONTAL_RESOLUTION, PaletteVisualization.SCREEN_VERTICAL_RESOLUTION));
    	this.setToolTipText("sprite palette table");
    	this.setPaletteMemoryBaseAddress(paletteMemoryBaseAddress);
    	
    	// instantiate palette labels
    	this.setPaletteLabels(new PaletteLabel[paletteSize]);
		for (int x=0;x<paletteSize;x++)
		{
			PaletteLabel pl = new PaletteLabel(NesMasterPalette.getInstance().getColor(0));
			this.setPaletteLabel(x, pl);
			this.add(pl);
		}
		
		//refresh palette labels with real colors
		this.refreshPalette();
	}
	
	public void refreshPalette()
	{
		for (int x=0;x<this.getPaletteLabels().length;x++)
		{
			int masterPaletteIndex = this.getPpuMemory().getMemoryFromHexAddress(this.getPaletteMemoryBaseAddress() + x);
			MasterColor p = NesMasterPalette.getInstance().getColor(masterPaletteIndex);
			this.getPaletteLabel(x).refreshBackground(p);
		}
	}

	public int getPaletteMemoryBaseAddress() 
	{
		return paletteMemoryBaseAddress;
	}

	public void setPaletteMemoryBaseAddress(int paletteMemoryBaseAddress) 
	{
		this.paletteMemoryBaseAddress = paletteMemoryBaseAddress;
	}

	public PaletteLabel[] getPaletteLabels()
	{
		return paletteLabels;
	}

	public PaletteLabel getPaletteLabel(int index)
	{
		return paletteLabels[index];
	}
	
	public void setPaletteLabels(PaletteLabel[] paletteLabels)
	{
		this.paletteLabels = paletteLabels;
	}
	
	public void setPaletteLabel(int index, PaletteLabel paletteLabel)
	{
		this.paletteLabels[index] = paletteLabel;
	}

	public NesPpuMemory getPpuMemory()
    {
    	return ppuMemory;
    }

	public void setPpuMemory(NesPpuMemory ppuMemory)
    {
    	this.ppuMemory = ppuMemory;
    }	
}
