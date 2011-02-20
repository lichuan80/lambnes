package com.lambelly.lambnes.gui;

import java.awt.*;  
import javax.swing.*;

import org.apache.log4j.*;

public class BackgroundPaletteVisualization extends JPanel
{
	private Logger logger = Logger.getLogger(BackgroundPaletteVisualization.class);
	public static final int SCREEN_HORIZONTAL_RESOLUTION = 256;
	public static final int SCREEN_VERTICAL_RESOLUTION = 240;
	
	public BackgroundPaletteVisualization()
	{
		super(new GridLayout(8,2));
		if (logger.isDebugEnabled())
		{
			logger.debug("initializing");
		}
    	this.setBounds(0, 0, BackgroundPaletteVisualization.SCREEN_HORIZONTAL_RESOLUTION, BackgroundPaletteVisualization.SCREEN_VERTICAL_RESOLUTION);
    	this.setPreferredSize(new Dimension(BackgroundPaletteVisualization.SCREEN_HORIZONTAL_RESOLUTION, BackgroundPaletteVisualization.SCREEN_VERTICAL_RESOLUTION));
    	this.setToolTipText("background palette table");
    	
		for (int x=0;x<16;x++)
		{
			this.add(new BackgroundPaletteLabel(x));
		}
	}
}
