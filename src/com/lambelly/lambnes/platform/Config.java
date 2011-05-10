package com.lambelly.lambnes.platform;

import org.apache.commons.configuration.*;
import org.apache.log4j.*;

import java.util.List;

public class Config
{
	private static Logger logger = Logger.getLogger(Config.class);
	private static Configuration config = null;
	
	public Config()
	{
		loadConfig();
	}
	
	private static void loadConfig()
	{
		try
		{
		    Configuration config = new PropertiesConfiguration("config.properties");
		    setConfig(config);
		}
		catch(Exception e)
		{
			logger.fatal("unable to load config");
		}
	}

	public static Configuration getConfig()
	{		
		return config;
	}

	public static void setConfig(Configuration config)
	{
		Config.config = config;
	}
}
