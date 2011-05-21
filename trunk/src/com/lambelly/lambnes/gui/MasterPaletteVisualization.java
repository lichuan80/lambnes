package com.lambelly.lambnes.gui;

import java.awt.*;  
import javax.swing.*;

import org.apache.log4j.*;

import com.lambelly.lambnes.platform.NesMasterColor;
import com.lambelly.lambnes.platform.Platform;

public class MasterPaletteVisualization extends JPanel
{
	private Logger logger = Logger.getLogger(MasterPaletteVisualization.class);
	public static final int SCREEN_HORIZONTAL_RESOLUTION = 256;
	public static final int SCREEN_VERTICAL_RESOLUTION = 240;
	
	public MasterPaletteVisualization()
	{
		super(new GridLayout(8,8));
		if (logger.isDebugEnabled())
		{
			logger.debug("initializing");
		}
    	this.setBounds(0, 0, MasterPaletteVisualization.SCREEN_HORIZONTAL_RESOLUTION, MasterPaletteVisualization.SCREEN_VERTICAL_RESOLUTION);
    	this.setPreferredSize(new Dimension(MasterPaletteVisualization.SCREEN_HORIZONTAL_RESOLUTION, MasterPaletteVisualization.SCREEN_VERTICAL_RESOLUTION));
    	this.setToolTipText("master palette table");
    	
		for (int x=0;x<64;x++)
		{
			this.add(new PaletteLabel(Platform.getMasterPalette().getColor(x)));
		}
	}
}
