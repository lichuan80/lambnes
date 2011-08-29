package com.lambelly.lambnes.platform.ppu.registers;

import com.lambelly.lambnes.platform.Platform;
import org.apache.log4j.*;

public class PPUSprRamAddressRegister
{
	public static final int REGISTER_ADDRESS = 0x2003;
	public static PPUSprRamAddressRegister register = new PPUSprRamAddressRegister();
	private static final int CYCLES_PER_EXECUTION = 0;
	private Integer rawControlByte = null;
	private Logger logger = Logger.getLogger(PPUSprRamAddressRegister.class);
	
	private PPUSprRamAddressRegister()
	{

	}
	
	public int cycle()
	{	
		return PPUSprRamAddressRegister.CYCLES_PER_EXECUTION;
	}
	
	public static PPUSprRamAddressRegister getRegister()
	{
		return register;
	}
	
	public void setRegisterValue(int value)
	{
		this.setRawControlByte(value);
	}
	
	public Integer getRawControlByte()
	{
		return rawControlByte;
	}

	private void setRawControlByte(Integer rawControlByte)
	{
		this.rawControlByte = rawControlByte;
	}
}
