package com.lambelly.lambnes.platform;

import org.apache.commons.configuration.*;
import org.apache.log4j.*;

import java.util.List;

public class Palette
{
	private Logger logger = Logger.getLogger(Palette.class);
	private PaletteColor[] paletteColors = new PaletteColor[64];
	
	
	public Palette() throws ConfigurationException
	{
		try
		{
		    XMLConfiguration config = new XMLConfiguration("palette.xml");
		    List<String> reds = config.getList("color[@red]");
		    List<String> blues = config.getList("color[@blue]");
		    List<String> greens = config.getList("color[@green]");
		    
		    for (int i=0;i<64;i++)
		    {
		    	if(logger.isDebugEnabled())
		    	{
		    		logger.debug("setting color[" + i + "] with " + reds.get(i));
		    	}
		    	this.setColor(i, new PaletteColor(reds.get(i), blues.get(i), greens.get(i)));		    	
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
	
	private PaletteColor[] getPaletteColors()
	{
		return paletteColors;
	}
	
	private void setPaletteColors(PaletteColor[] colors)
	{
		this.paletteColors = colors;
	}
	
	public PaletteColor getColor(int index)
	{
		return this.getPaletteColors()[index];
	}
	
	public void setColor(int index, PaletteColor color)
	{
		this.getPaletteColors()[index] = color;
	}
}
