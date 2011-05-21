package com.lambelly.lambnes.platform;

import org.apache.commons.configuration.*;
import org.apache.log4j.*;

import java.util.List;

public class Palette
{
	private Logger logger = Logger.getLogger(Palette.class);
	private NesMasterColor[] paletteColors = new NesMasterColor[64];
	
	
	public Palette() throws ConfigurationException
	{
		try
		{
		    XMLConfiguration paletteConfig = new XMLConfiguration("palette.xml");
		    List<String> reds = paletteConfig.getList("color[@red]");
		    List<String> blues = paletteConfig.getList("color[@blue]");
		    List<String> greens = paletteConfig.getList("color[@green]");
		    
		    for (int i=0;i<64;i++)
		    {
		    	if(logger.isDebugEnabled())
		    	{
		    		logger.debug("setting color[" + i + "] with " + reds.get(i));
		    	}
		    	this.setColor(i, new NesMasterColor(reds.get(i), blues.get(i), greens.get(i)));		    	
		    }
		    
		}
		catch(Exception e)
		{
			throw new ConfigurationException("palette could not be configured.");
		}
	    
	}
	
	public String toString()
	{
		String returnString = "";
		for (int i=0;i<64;i++)
		{
			returnString += "color[" + i + "]:\n" + this.getColor(i);
		}
		return returnString;
	}
	
	private NesMasterColor[] getPaletteColors()
	{
		return paletteColors;
	}
	
	private void setPaletteColors(NesMasterColor[] colors)
	{
		this.paletteColors = colors;
	}
	
	public NesMasterColor getColor(int index)
	{
		if (!Config.getConfig().getBoolean("colorScreen"))
		{
			index = (index & 0x30);
		}
		
		return this.getPaletteColors()[index];	
	}
	
	public void setColor(int index, NesMasterColor color)
	{
		this.getPaletteColors()[index] = color;
	}
}
