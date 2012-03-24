package com.lambelly.lambnes.platform.apu.registers;

import org.apache.log4j.Logger;

import com.lambelly.lambnes.util.BitUtils;

public class APUPulse1SweepRegister
{
	public static final int REGISTER_ADDRESS = 0x4001;
	private boolean enabled = false;
	private int period = 0;
	private int negative = 0;
	private int shiftCount = 0;
	private static Integer rawControlByte = null;
	private Logger logger = Logger.getLogger(APUPulse1SweepRegister.class);
	
	private APUPulse1SweepRegister()
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
		// EPPP NSSS
		this.setEnabled(BitUtils.isBitSet(rawControlByte, 7));
		this.setPeriod((rawControlByte & 70) >> 4);
		this.setNegative(BitUtils.isBitSet(rawControlByte, 3) ? 1 : 0);
		this.setShiftCount(rawControlByte & 0x7);
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
		APUPulse1SweepRegister.rawControlByte = rawControlByte;
	}

	public boolean isEnabled()
    {
    	return enabled;
    }

	public void setEnabled(boolean enabled)
    {
    	this.enabled = enabled;
    }

	public int getPeriod()
    {
    	return period;
    }

	public void setPeriod(int period)
    {
    	this.period = period;
    }

	public int getNegative()
    {
    	return negative;
    }

	public void setNegative(int negative)
    {
    	this.negative = negative;
    }

	public int getShiftCount()
    {
    	return shiftCount;
    }

	public void setShiftCount(int shiftCount)
    {
    	this.shiftCount = shiftCount;
    }
}
