package com.lambelly.lambnes.test;

import java.util.List;
import java.util.Iterator;

import org.junit.Test;
import static org.junit.Assert.*;

import com.lambelly.lambnes.platform.*;
import com.lambelly.lambnes.test.utils.TestUtils;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.*;


public class PaletteConfigTest
{
	private Logger logger = Logger.getLogger(PaletteConfigTest.class);
	
	@Test
	public void paletteConfig() throws ConfigurationException
	{
		try
		{
		    XMLConfiguration config = new XMLConfiguration("palette.xml");
		    logger.debug(config.isEmpty());
		    List reds = config.getList("color[@red]");
		    assertEquals(64,reds.size());
		    Iterator it = reds.iterator();
		    while (it.hasNext())
		    {
		    	String s = (String)it.next();
		    	logger.debug(s);
		    }
		    
		    String color = config.getString("palette.color(0).[@red]");
		    logger.debug(color);
		}
		catch(Exception e)
		{
			logger.error(e);
		}
	}
	
	@Test
	public void palette()
	{
		TestUtils.createTestPlatform();
		logger.debug(Platform.getMasterPalette());
	}
}
