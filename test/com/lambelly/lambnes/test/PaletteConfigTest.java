package com.lambelly.lambnes.test;

import java.util.List;
import java.util.Iterator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

import com.lambelly.lambnes.platform.*;
import com.lambelly.lambnes.test.utils.TestUtils;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.log4j.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:beans.xml"})
public class PaletteConfigTest
{
	@Autowired
	private TestUtils testUtils;
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
	public void palette()throws Exception
	{
		this.getTestUtils().createTestPlatform();
		logger.debug(NesMasterPalette.getInstance());
	}

	public TestUtils getTestUtils()
    {
    	return testUtils;
    }

	public void setTestUtils(TestUtils testUtils)
    {
    	this.testUtils = testUtils;
    }
}
