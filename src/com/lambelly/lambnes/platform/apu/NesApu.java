package com.lambelly.lambnes.platform.apu;

import com.lambelly.lambnes.platform.apu.registers.*;

public class NesApu implements AudioProcessingUnit
{
	APUControlRegister apuControlRegister = new APUControlRegister();
	
	public void cycle()
	{
		this.doRegisterReadsWrites();
	}
	
	public void doRegisterReadsWrites()
	{
		this.getApuControlRegister().cycle();
	}
	
	public APUControlRegister getApuControlRegister()
	{
		return apuControlRegister;
	}

	public void setApuControlRegister(APUControlRegister apuControlRegister)
	{
		this.apuControlRegister = apuControlRegister;
	}
}
