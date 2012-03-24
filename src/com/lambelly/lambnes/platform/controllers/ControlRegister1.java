package com.lambelly.lambnes.platform.controllers;

import org.apache.log4j.Logger;

import com.lambelly.lambnes.platform.Platform;

public class ControlRegister1
{
	private Integer rawControlByte = null;
	private Integer firstWrite = null;
	private boolean strobe = false;
	private int strobeCount = 0;
	private NesControllerPorts controllerPorts;
	public static final int REGISTER_ADDRESS = 0x4016;
	private static Logger logger = Logger.getLogger(ControlRegister1.class);
	
	private ControlRegister1()
	{
	
	}

	public void cycle()
	{	
		if (this.getRawControlByte() != null)
		{
			//logger.debug("received rawControlByte: " + this.getRawControlByte());
			
			if (this.getRawControlByte() == 1)
			{
				this.setFirstWrite(this.getRawControlByte());
			}
			else if (this.getRawControlByte() == 0)
			{
				if (this.getFirstWrite() == 1)
				{
					// full strobe
					this.setStrobe(true);
					this.setStrobeCount(0);
				}
			}
			
			this.clear();
		}
	}
	
	private void clear()
	{
		this.setRawControlByte(null);
	}
	
	public void setRegisterValue(int value)
	{
		this.setRawControlByte(value);
	}
	
	public int getRegisterValue()
	{
		int strobeValue = 0;
		
		// if in strobe mode
		if (this.getStrobe() && this.getStrobeCount() < 24)
		{
			strobeValue = (this.getControllerPorts().getPortA().read(this.getStrobeCount()));
			//logger.debug("reading " + this.getStrobeCount() + ":" + strobeValue);
			this.incrementStrobeCount();
		}
		
		return strobeValue;
	}

	private Integer getRawControlByte()
	{
		return rawControlByte;
	}

	private void setRawControlByte(Integer rawControlByte)
	{
		this.rawControlByte = rawControlByte;
	}

	public boolean getStrobe()
	{
		return this.strobe;
	}

	public void setStrobe(boolean strobe)
	{
		this.strobe = strobe;
	}

	public Integer getFirstWrite()
	{
		return firstWrite;
	}

	public void setFirstWrite(Integer firstWrite)
	{
		this.firstWrite = firstWrite;
	}

	public int getStrobeCount()
	{
		return strobeCount;
	}

	public void setStrobeCount(int strobeCount)
	{
		this.strobeCount = strobeCount;
	}
	
	public void incrementStrobeCount()
	{
		this.strobeCount++;
	}

	public NesControllerPorts getControllerPorts()
    {
    	return controllerPorts;
    }

	public void setControllerPorts(NesControllerPorts controllerPorts)
    {
    	this.controllerPorts = controllerPorts;
    }
}
