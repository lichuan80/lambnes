package com.lambelly.lambnes.gui;

import java.awt.*;  
import javax.swing.*;

import org.apache.log4j.*;

import com.lambelly.lambnes.platform.MasterColor;
import com.lambelly.lambnes.platform.MasterPalette;
import com.lambelly.lambnes.platform.Platform;

public class MasterPaletteVisualization extends JPanel
{
	private Logger logger = Logger.getLogger(MasterPaletteVisualization.class);
	private MasterPalette masterPalette = null;
	public static final int SCREEN_HORIZONTAL_RESOLUTION = 256;
	public static final int SCREEN_VERTICAL_RESOLUTION = 240;
	
	public MasterPaletteVisualization(MasterPalette masterPalette)
	{
		super(new GridLayout(8,8));
		this.setMasterPalette(masterPalette);
		
		if (logger.isDebugEnabled())
		{
			logger.debug("initializing");
		}
    	this.setBounds(0, 0, MasterPaletteVisualization.SCREEN_HORIZONTAL_RESOLUTION, MasterPaletteVisualization.SCREEN_VERTICAL_RESOLUTION);
    	this.setPreferredSize(new Dimension(MasterPaletteVisualization.SCREEN_HORIZONTAL_RESOLUTION, MasterPaletteVisualization.SCREEN_VERTICAL_RESOLUTION));
    	this.setToolTipText("master palette table");
    	
		for (int x=0;x<64;x++)
		{
			this.add(new PaletteLabel(masterPalette.getColor(x)));
		}
	}

	public MasterPalette getMasterPalette()
    {
    	return masterPalette;
    }

	public void setMasterPalette(MasterPalette masterPalette)
    {
    	this.masterPalette = masterPalette;
    }
}
