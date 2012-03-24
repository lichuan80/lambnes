package com.lambelly.lambnes.platform.apu.registers;

import org.apache.log4j.Logger;

import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.util.BitUtils;

public class APUPulse1LengthCounterRegister
{
	public static final int REGISTER_ADDRESS = 0x4003;
	public static final int[] LENGTH_ARRAY_0 = {0x0A,0x14,0x28,0x50,0xA0,0x3C,0x0E,0x1A,0x0C,0x18,0x30,0x60,0xC0,0x48,0x10,0x20};
    public static final int[] LENGTH_ARRAY_1 = {0xFE,0x02,0x04,0x06,0x08,0x0A,0x0C,0x0E,0x10,0x12,0x14,0x16,0x18,0x1A,0x1C,0x1E};
	private Integer rawControlByte = null;
	private int lengthCounter = 0;
	private int timerHigh = 0;
	private APUControlRegister apuControlRegister;
	private Logger logger = Logger.getLogger(APUPulse1LengthCounterRegister.class);
	
	private APUPulse1LengthCounterRegister()
	{
		
	}
	
	public int cycle()
	{	
		if (this.getRawControlByte() != null)
		{   
			this.parseWrite(this.getRawControlByte());
		}
		else
		{
			if (this.getLengthCounter() > 0)
			{
				if (this.getApuControlRegister().isPulse1LengthCounterEnabledFlag())
				{
					this.decrementLengthCounter();
				}
			}
			else
			{
				this.getApuControlRegister().setPulse1LengthCounterNonZero(true);
			}
		}
		
		return 0;
	}
	
	private void parseWrite(int rawControlByte)
	{
		//LLLL LHHH
		// length index
		this.getApuControlRegister().setPulse1LengthCounterNonZero(false);
	    int lengthIndex = ((this.getRawControlByte() & 0xF0) >> 4);
	 
	    // length array choice
	    if (BitUtils.isBitSet(this.getRawControlByte(), 3))
	    {
	    	this.setLengthCounter(LENGTH_ARRAY_1[lengthIndex]);
	    }
	    else
	    {
	    	this.setLengthCounter(LENGTH_ARRAY_0[lengthIndex]);
	    }
		
	    // counter high
	    this.setTimerHigh(rawControlByte & 0x7);
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
	
	public int getLengthCounter()
    {
    	return lengthCounter;
    }

	public void setLengthCounter(int lengthCounter)
    {
    	this.lengthCounter = lengthCounter;
    }
	
	private void decrementLengthCounter()
    {
    	this.lengthCounter--;
    }

	public int getTimerHigh()
    {
    	return timerHigh;
    }

	public void setTimerHigh(int timerHigh)
    {
    	this.timerHigh = timerHigh;
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
