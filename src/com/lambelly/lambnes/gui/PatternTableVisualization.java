package com.lambelly.lambnes.gui;

import java.awt.*;  
import javax.swing.*;

import org.apache.log4j.*;

public class PatternTableVisualization extends JPanel
{
	private Logger logger = Logger.getLogger(PatternTableVisualization.class);
	private PatternTableIcon[] patternTableIcons = new PatternTableIcon[256];
	public static final int SCREEN_HORIZONTAL_RESOLUTION = 256;
	public static final int SCREEN_VERTICAL_RESOLUTION = 240;
	
	public PatternTableVisualization(String toolTipText)
	{
		super(new GridLayout(16,16));
		if(logger.isDebugEnabled())
		{
			logger.debug("initializing");
		}
    	this.setBounds(0, 0, PatternTableVisualization.SCREEN_HORIZONTAL_RESOLUTION, PatternTableVisualization.SCREEN_VERTICAL_RESOLUTION);
    	this.setPreferredSize(new Dimension(PatternTableVisualization.SCREEN_HORIZONTAL_RESOLUTION, PatternTableVisualization.SCREEN_VERTICAL_RESOLUTION));
    	this.setToolTipText(toolTipText);
    	
    	//initial initialization of icons
		for (int x=0;x<256;x++)
		{
			PatternTableIcon i = new PatternTableIcon(x);
			this.setPatternTableIcon(x, i);
			this.add(new PatternTableLabel(x,i));
		}  
	}

	public PatternTableIcon[] getPatternTableIcons()
	{
		return patternTableIcons;
	}
	
	public PatternTableIcon getPatternTableIcon(int index)
	{
		return patternTableIcons[index];
	}

	public void setPatternTableIcons(PatternTableIcon[] patternTableIcons)
	{
		this.patternTableIcons = patternTableIcons;
	}
	
	public void setPatternTableIcon(int index, PatternTableIcon icon)
	{
		this.patternTableIcons[index] = icon;
	}
}
