package com.lambelly.lambnes.platform.apu.registers;

import org.apache.log4j.Logger;

import com.lambelly.lambnes.util.BitUtils;

public class APUControlRegister
{
	public static final int REGISTER_ADDRESS = 0x4015;
	
	// write variables
	private boolean dmcBytesRemainingFlag = false;
	private boolean noiseChannelLengthCounterEnabledFlag = false;
	private boolean triangleChannelLengthCounterEnabledFlag = false;
	private boolean pulse2LengthCounterEnabledFlag = false;
	private boolean pulse1LengthCounterEnabledFlag = false;
	
	// read variables
	private boolean dmcInterruptFlag = false;
	private boolean frameInterruptFlag = false;
	private boolean dmcBytesRemainingNonZero = false;
	private boolean noiseChannelLengthCounterNonZero = false;
	private boolean triangleChannelsLengthCounterNonZero = false;
	private boolean pulse2LengthCounterNonZero = false;
	private boolean pulse1LengthCounterNonZero = false;
	
	private int rawControlByte = 0;
	private Logger logger = Logger.getLogger(APUControlRegister.class);
	
	private APUControlRegister()
	{
		
	}
	
	public int cycle()
	{
		if (this.getRawControlByte() != null)
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("rawControlByte: " + rawControlByte);
			}
			this.parseWrite(this.getRawControlByte());
			this.setDmcInterruptFlag(false);
		}
		
		return 0;
	}
	
	private int parseRead()
	{
		// IF-D NT21
		int parsedRead = ((this.isDmcInterruptFlag() ? 1:0) << 7) |
			((this.isFrameInterruptFlag() ? 1:0) << 6) |
			((this.isDmcBytesRemainingFlag() ? 1:0) << 4) |
			((this.isNoiseChannelLengthCounterNonZero() ? 1:0) << 3) |
			((this.isTriangleChannelsLengthCounterNonZero() ? 1:0) << 2) |
			((this.isPulse2LengthCounterNonZero() ? 1:0) <<1) |
			((this.isPulse1LengthCounterNonZero() ? 1:0));
		return parsedRead;
	}
	
	private void parseWrite(int rawControlByte)
	{
		// ---D NT21
		this.setDmcBytesRemainingFlag(BitUtils.isBitSet(rawControlByte,4));
		this.setNoiseChannelLengthCounterEnabledFlag(BitUtils.isBitSet(rawControlByte,3));
		this.setTriangleChannelLengthCounterEnabledFlag(BitUtils.isBitSet(rawControlByte,2));
		this.setPulse2LengthCounterEnabledFlag(BitUtils.isBitSet(rawControlByte,1));
		this.setPulse1LengthCounterEnabledFlag(BitUtils.isBitSet(rawControlByte,0));
	}
	
	public int getRegisterValue()
	{
		int readValue = this.parseRead();
		this.setFrameInterruptFlag(false);
		// TODO: If an interrupt flag was set at the same moment of the read, it will read back as 1 but it will not be cleared.
		return readValue;
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

	public boolean isDmcBytesRemainingFlag()
    {
    	return dmcBytesRemainingFlag;
    }

	public void setDmcBytesRemainingFlag(boolean dmcBytesRemainingFlag)
    {
    	this.dmcBytesRemainingFlag = dmcBytesRemainingFlag;
    }

	public boolean isNoiseChannelLengthCounterEnabledFlag()
    {
    	return noiseChannelLengthCounterEnabledFlag;
    }

	public void setNoiseChannelLengthCounterEnabledFlag(
            boolean noiseChannelLengthCounterEnabledFlag)
    {
    	this.noiseChannelLengthCounterEnabledFlag = noiseChannelLengthCounterEnabledFlag;
    }

	public boolean isTriangleChannelLengthCounterEnabledFlag()
    {
    	return triangleChannelLengthCounterEnabledFlag;
    }

	public void setTriangleChannelLengthCounterEnabledFlag(
            boolean triangleChannelLengthCounterEnabledFlag)
    {
    	this.triangleChannelLengthCounterEnabledFlag = triangleChannelLengthCounterEnabledFlag;
    }

	public boolean isPulse2LengthCounterEnabledFlag()
    {
    	return pulse2LengthCounterEnabledFlag;
    }

	public void setPulse2LengthCounterEnabledFlag(
            boolean pulse2LengthCounterEnabledFlag)
    {
    	this.pulse2LengthCounterEnabledFlag = pulse2LengthCounterEnabledFlag;
    }

	public boolean isPulse1LengthCounterEnabledFlag()
    {
    	return pulse1LengthCounterEnabledFlag;
    }

	public void setPulse1LengthCounterEnabledFlag(
            boolean pulse1LengthCounterEnabledFlag)
    {
    	this.pulse1LengthCounterEnabledFlag = pulse1LengthCounterEnabledFlag;
    }

	public boolean isDmcInterruptFlag()
    {
    	return dmcInterruptFlag;
    }

	public void setDmcInterruptFlag(boolean dmcInterruptFlag)
    {
    	this.dmcInterruptFlag = dmcInterruptFlag;
    }

	public boolean isFrameInterruptFlag()
    {
    	return frameInterruptFlag;
    }

	public void setFrameInterruptFlag(boolean frameInterruptFlag)
    {
    	this.frameInterruptFlag = frameInterruptFlag;
    }

	public boolean isDmcBytesRemainingNonZero()
    {
    	return dmcBytesRemainingNonZero;
    }

	public void setDmcBytesRemainingNonZero(boolean dmcBytesRemainingNonZero)
    {
    	this.dmcBytesRemainingNonZero = dmcBytesRemainingNonZero;
    }

	public boolean isNoiseChannelLengthCounterNonZero()
    {
    	return noiseChannelLengthCounterNonZero;
    }

	public void setNoiseChannelLengthCounterNonZero(
            boolean noiseChannelLengthCounterNonZero)
    {
    	this.noiseChannelLengthCounterNonZero = noiseChannelLengthCounterNonZero;
    }

	public boolean isTriangleChannelsLengthCounterNonZero()
    {
    	return triangleChannelsLengthCounterNonZero;
    }

	public void setTriangleChannelsLengthCounterNonZero(
            boolean triangleChannelsLengthCounterNonZero)
    {
    	this.triangleChannelsLengthCounterNonZero = triangleChannelsLengthCounterNonZero;
    }

	public boolean isPulse2LengthCounterNonZero()
    {
    	return pulse2LengthCounterNonZero;
    }

	public void setPulse2LengthCounterNonZero(boolean pulse2LengthCounterNonZero)
    {
    	this.pulse2LengthCounterNonZero = pulse2LengthCounterNonZero;
    }

	public boolean isPulse1LengthCounterNonZero()
    {
    	return pulse1LengthCounterNonZero;
    }

	public void setPulse1LengthCounterNonZero(boolean pulse1LengthCounterNonZero)
    {
    	this.pulse1LengthCounterNonZero = pulse1LengthCounterNonZero;
    }
}
