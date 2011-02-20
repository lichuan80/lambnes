package com.lambelly.lambnes.gui;

import java.awt.*;  
import javax.swing.*;

import org.apache.log4j.*;

public class SpritePaletteVisualization extends JPanel
{
	private Logger logger = Logger.getLogger(SpritePaletteVisualization.class);
	public static final int SCREEN_HORIZONTAL_RESOLUTION = 256;
	public static final int SCREEN_VERTICAL_RESOLUTION = 240;
	
	public SpritePaletteVisualization()
	{
		super(new GridLayout(8,2));
		if(logger.isDebugEnabled())
		{
			logger.debug("initializing");
		}
    	this.setBounds(0, 0, SpritePaletteVisualization.SCREEN_HORIZONTAL_RESOLUTION, SpritePaletteVisualization.SCREEN_VERTICAL_RESOLUTION);
    	this.setPreferredSize(new Dimension(SpritePaletteVisualization.SCREEN_HORIZONTAL_RESOLUTION, SpritePaletteVisualization.SCREEN_VERTICAL_RESOLUTION));
    	this.setToolTipText("sprite palette table");
    	
		for (int x=0;x<16;x++)
		{
			this.add(new SpritePaletteLabel(x));
		}
	}
}
