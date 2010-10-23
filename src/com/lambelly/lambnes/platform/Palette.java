package com.lambelly.lambnes.platform;

import static org.junit.Assert.assertEquals;

import org.apache.commons.configuration.*;
import org.apache.log4j.*;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class Palette
{
	private Logger logger = Logger.getLogger(Palette.class);
	private Color[] paletteColors = new Color[64];
	
	
	public Palette() throws ConfigurationException
	{
		try
		{
		    XMLConfiguration config = new XMLConfiguration("palette.xml");
		    List<String> reds = config.getList("color[@red]");
		    List<String> blues = config.getList("color[@blue]");
		    List<String> greens = config.getList("color[@green]");
		    assertEquals(64,reds.size());
		    
		    for (int i=0;i<64;i++)
		    {
		    	logger.debug("setting color[" + i + "] with " + reds.get(i));
		    	this.setColor(i, new Color(reds.get(i), blues.get(i), greens.get(i)));		    	
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
	
	private Color[] getPaletteColors()
	{
		return paletteColors;
	}
	
	private void setPaletteColors(Color[] colors)
	{
		this.paletteColors = colors;
	}
	
	public Color getColor(int index)
	{
		return this.getPaletteColors()[index];
	}
	
	public void setColor(int index, Color color)
	{
		this.getPaletteColors()[index] = color;
	}
}
