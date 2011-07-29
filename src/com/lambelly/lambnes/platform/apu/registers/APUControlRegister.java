package com.lambelly.lambnes.platform.apu.registers;

import org.apache.log4j.Logger;

public class APUControlRegister
{
	public static final int REGISTER_ADDRESS = 0x4015;
	private static APUControlRegister register = new APUControlRegister();
	private Integer rawControlByte = null;
	private Logger logger = Logger.getLogger(APUControlRegister.class);
	
	public int cycle()
	{
		if (this.getRawControlByte() != null)
		{
			logger.info("beep");
		}
		
		return 0;
	}
	
	public void setRegisterValue(int value)
	{
		this.setRawControlByte(value);
	}
	
	private Integer getRawControlByte()
	{
		return rawControlByte;
	}

	private void setRawControlByte(Integer rawControlByte)
	{
		this.rawControlByte = rawControlByte;
	}
	
	public static APUControlRegister getRegister()
	{
		return register;
	}

	private static void setRegister(APUControlRegister register)
	{
		APUControlRegister.register = register;
	}
}
