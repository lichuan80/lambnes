package com.lambelly.lambnes.platform.ppu.registers;

import com.lambelly.lambnes.platform.Platform;

public class PPUSprRamAddressRegister
{
	public static final int REGISTER_ADDRESS = 0x2003;
	public static PPUSprRamAddressRegister register = new PPUSprRamAddressRegister();
	private static final int CYCLES_PER_EXECUTION = 0;
	private Integer rawControlByte = null;
	
	private PPUSprRamAddressRegister()
	{
		
	}
	
	public int cycle()
	{	
		// if not null.
		/*
		if (this.getRawControlByte() != null)
		{
			// set 0x2004
			Platform.getPpu().getPpuSprRamIORegister().setIoAddress(this.getRawControlByte());
				
			// clear? I suppose it makes sense.
			this.clear();
		}
		*/
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
	
	private void clear()
	{
		this.setRawControlByte(null);
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
