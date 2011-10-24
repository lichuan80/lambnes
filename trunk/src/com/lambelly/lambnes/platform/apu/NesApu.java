package com.lambelly.lambnes.platform.apu;

import com.lambelly.lambnes.platform.apu.registers.*;
import javax.sound.sampled.*;

public class NesApu implements AudioProcessingUnit
{
	APUControlRegister apuControlRegister = APUControlRegister.getRegister();
	
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
