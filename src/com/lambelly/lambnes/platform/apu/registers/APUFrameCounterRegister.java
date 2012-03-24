package com.lambelly.lambnes.platform.apu.registers;

import com.lambelly.lambnes.util.BitUtils;

public class APUFrameCounterRegister
{
	public static final int REGISTER_ADDRESS = 0x4017;
	public static final int SEQUENCER_MODE_4_STEP_SEQUENCE = 0;
	public static final int SEQUENCER_MODE_5_STEP_SEQUENCE = 1;

	private int sequencerMode = 0;
	private boolean interuptInhibitFlag = false;
	private Integer rawControlByte = 0;

	public void cycle()
	{
		if (this.getRawControlByte() != null)
		{
			this.parseWrite(rawControlByte);
		}
	}
	
	public void parseWrite(int rawControlByte)
	{
		// SD-- ----
		this.setSequencerMode(BitUtils.isBitSet(rawControlByte, 7) ? 1 : 0);
		this.setInteruptInhibitFlag(BitUtils.isBitSet(rawControlByte, 6));
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

	public int getSequencerMode()
    {
    	return sequencerMode;
    }

	public void setSequencerMode(int sequencerMode)
    {
    	this.sequencerMode = sequencerMode;
    }

	public boolean isInteruptInhibitFlag()
    {
    	return interuptInhibitFlag;
    }

	public void setInteruptInhibitFlag(boolean interuptInhibitFlag)
    {
    	this.interuptInhibitFlag = interuptInhibitFlag;
    }
}
