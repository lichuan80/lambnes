package com.lambelly.lambnes.platform;

import org.apache.commons.configuration.*;
import org.apache.log4j.*;

import com.lambelly.lambnes.platform.ppu.NesPpuMemory;

import java.util.List;

public class NesMasterPalette implements MasterPalette
{
	private static Logger logger = Logger.getLogger(NesMasterPalette.class);
	private MasterColor[] paletteColors = new MasterColor[64];
	private static NesMasterPalette instance= null;
	
	public NesMasterPalette() throws ConfigurationException
	{
		try
		{
		    XMLConfiguration paletteConfig = new XMLConfiguration("palette.xml");
		    List<String> reds = paletteConfig.getList("color[@red]");
		    List<String> blues = paletteConfig.getList("color[@blue]");
		    List<String> greens = paletteConfig.getList("color[@green]");
		    
		    for (int i=0;i<64;i++)
		    {
		    	//logger.debug("setting color[" + i + "] with " + reds.get(i));
		    	this.setColor(i, new MasterColor(reds.get(i), blues.get(i), greens.get(i)));		    	
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
	
    public static NesMasterPalette getInstance()
    {
        if(instance == null)
        {
        	try
        	{
        		instance = new NesMasterPalette();
        	}
        	catch(Exception e)
        	{
        		logger.fatal("boot issue: " + e.getMessage(),e);
        	}
        }
        return instance;
    }
	private MasterColor[] getPaletteColors()
	{
		return paletteColors;
	}
	
	private void setPaletteColors(MasterColor[] colors)
	{
		this.paletteColors = colors;
	}
	
	public MasterColor getColor(int index)
	{
		if (!Config.getConfig().getBoolean("colorScreen"))
		{
			index = (index & 0x30);
		}
		
		return this.getPaletteColors()[index];	
	}
	
	public void setColor(int index, MasterColor color)
	{
		this.getPaletteColors()[index] = color;
	}
}
