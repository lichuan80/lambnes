package com.lambelly.lambnes.gui;

import java.awt.*;  
import javax.swing.*;

import org.apache.log4j.*;

public class BackgroundPatternTableVisualization extends JPanel
{
	private Logger logger = Logger.getLogger(BackgroundPatternTableVisualization.class);
	public static final int SCREEN_HORIZONTAL_RESOLUTION = 256;
	public static final int SCREEN_VERTICAL_RESOLUTION = 240;
	public static final String SCREEN_TITLE = "Background Pattern Table"; 
	
	public BackgroundPatternTableVisualization()
	{
		super(new GridLayout(16,16));
		if(logger.isDebugEnabled())
		{
			logger.debug("initializing");
		}
    	this.setBounds(0, 0, BackgroundPatternTableVisualization.SCREEN_HORIZONTAL_RESOLUTION, BackgroundPatternTableVisualization.SCREEN_VERTICAL_RESOLUTION);
    	this.setPreferredSize(new Dimension(BackgroundPatternTableVisualization.SCREEN_HORIZONTAL_RESOLUTION, BackgroundPatternTableVisualization.SCREEN_VERTICAL_RESOLUTION));
    	this.setToolTipText("background pattern table");
    	
		for (int x=0;x<256;x++)
		{
			this.add(new BackgroundLabel(x));
		}
	}
}
