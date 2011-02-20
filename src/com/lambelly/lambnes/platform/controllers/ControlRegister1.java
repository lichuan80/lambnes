package com.lambelly.lambnes.platform.controllers;

import org.apache.log4j.Logger;

public class ControlRegister1
{
	private Integer rawControlByte = null;
	public static final int REGISTER_ADDRESS = 0x4016;
	private static ControlRegister1 register = new ControlRegister1();
	private Logger logger = Logger.getLogger(ControlRegister1.class);
	
	private ControlRegister1()
	{
	
	}

	public void cycle()
	{
		if (this.getRawControlByte() != null)
		{
			logger.debug("received rawControlByte: " + this.getRawControlByte());
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
	
	public static ControlRegister1 getRegister()
	{
		return register;
	}

	public static void setRegister(ControlRegister1 register)
	{
		ControlRegister1.register = register;
	}

	private Integer getRawControlByte()
	{
		return rawControlByte;
	}

	private void setRawControlByte(Integer rawControlByte)
	{
		this.rawControlByte = rawControlByte;
	}
	
	
}
