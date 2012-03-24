package com.lambelly.lambnes.platform.apu.registers;

import org.apache.log4j.Logger;

public class APUPulse1TimerLowRegister
{
	public static final int REGISTER_ADDRESS = 0x4002;
	private static Integer rawControlByte = null;
	private int timerLowByte = 0;
	private Logger logger = Logger.getLogger(APUPulse1TimerLowRegister.class);
	
	private APUPulse1TimerLowRegister()
	{
		
	}
	
	public int cycle()
	{
		if (this.getRawControlByte() != null)
		{
			this.parseWrite(rawControlByte);
		}
		
		return 0;
	}
	
	private void parseWrite(int rawControlByte)
	{
		this.setTimerLowByte(rawControlByte);
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
		APUPulse1TimerLowRegister.rawControlByte = rawControlByte;
	}

	public int getTimerLowByte()
    {
    	return timerLowByte;
    }

	public void setTimerLowByte(int timerLowByte)
    {
    	this.timerLowByte = timerLowByte;
    }
}
