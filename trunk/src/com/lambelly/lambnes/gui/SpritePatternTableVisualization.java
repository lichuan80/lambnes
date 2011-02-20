package com.lambelly.lambnes.gui;

import java.awt.*;  
import javax.swing.*;

import org.apache.log4j.*;

public class SpritePatternTableVisualization extends JPanel
{
	private Logger logger = Logger.getLogger(SpritePatternTableVisualization.class);
	public static final int SCREEN_HORIZONTAL_RESOLUTION = 256;
	public static final int SCREEN_VERTICAL_RESOLUTION = 240;
	public static final String SCREEN_TITLE = "Sprite Pattern Table"; 
	
	public SpritePatternTableVisualization()
	{
		super(new GridLayout(8,8));
		if(logger.isDebugEnabled())
		{
			logger.debug("initializing");
		}
    	this.setBounds(0, 0, SpritePatternTableVisualization.SCREEN_HORIZONTAL_RESOLUTION, SpritePatternTableVisualization.SCREEN_VERTICAL_RESOLUTION);
    	this.setPreferredSize(new Dimension(SpritePatternTableVisualization.SCREEN_HORIZONTAL_RESOLUTION, SpritePatternTableVisualization.SCREEN_VERTICAL_RESOLUTION));
    	this.setToolTipText("sprite pattern table");
    	
		for (int x=0;x<64;x++)
		{
			this.add(new SpriteLabel(x));
		}
	}
}
