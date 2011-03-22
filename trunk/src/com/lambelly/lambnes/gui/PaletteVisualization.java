package com.lambelly.lambnes.gui;

import java.awt.*;  
import javax.swing.*;

import org.apache.log4j.*;

public class PaletteVisualization extends JPanel
{
	private Logger logger = Logger.getLogger(PaletteVisualization.class);
	PanelThread t = null;
	private int paletteMemoryBaseAddress = 0;
	public static final int SCREEN_HORIZONTAL_RESOLUTION = 256;
	public static final int SCREEN_VERTICAL_RESOLUTION = 240;
	
	public PaletteVisualization(int paletteMemoryBaseAddress)
	{
		super(new GridLayout(8,2));
		if(logger.isDebugEnabled())
		{
			logger.debug("initializing");
		}
    	this.setBounds(0, 0, PaletteVisualization.SCREEN_HORIZONTAL_RESOLUTION, PaletteVisualization.SCREEN_VERTICAL_RESOLUTION);
    	this.setPreferredSize(new Dimension(PaletteVisualization.SCREEN_HORIZONTAL_RESOLUTION, PaletteVisualization.SCREEN_VERTICAL_RESOLUTION));
    	this.setToolTipText("sprite palette table");
    
    	this.setPaletteMemoryBaseAddress(paletteMemoryBaseAddress);
		for (int x=0;x<16;x++)
		{
			this.add(new PaletteLabel(this.getPaletteMemoryBaseAddress() + x));
		}
		
		t = new PanelThread(this);
    	new Thread(t).start();
	}

	public int getPaletteMemoryBaseAddress() 
	{
		return paletteMemoryBaseAddress;
	}

	public void setPaletteMemoryBaseAddress(int paletteMemoryBaseAddress) 
	{
		this.paletteMemoryBaseAddress = paletteMemoryBaseAddress;
	}
}
