package com.lambelly.lambnes.platform.ppu.registers;

import com.lambelly.lambnes.platform.Platform;
import org.apache.log4j.*;

public class PPUSprRamIORegister
{
	public static final int REGISTER_ADDRESS = 0x2004;
	private static final int CYCLES_PER_EXECUTION = 0;
	private static PPUSprRamIORegister register = new PPUSprRamIORegister();
	//private Integer ioAddress = null;
	private Integer rawControlByte = null;
	private int sramBuffer = 0;
	private Logger logger = Logger.getLogger(PPUSprRamIORegister.class);
	
	private PPUSprRamIORegister()
	{
		
	}
	
	public int cycle()
	{
		// get raw control byte
		logger.debug("0x2004 raw control byte: " + this.getRawControlByte());
		logger.debug("0x2004 IO address: " + Platform.getPpu().getPpuSprRamAddressRegister());
		
		if (Platform.getPpu().getPpuSprRamAddressRegister().getRawControlByte() != null)
		{
			this.setSramBuffer(Platform.getPpuMemory().getSprRamFromHexAddress(Platform.getPpu().getPpuSprRamAddressRegister().getRawControlByte()));
		}
		
		if (this.getRawControlByte() != null)
		{
			if (Platform.getPpu().getPpuSprRamAddressRegister().getRawControlByte() != null)
			{			
				// write control byte to ioAddress
				if(logger.isDebugEnabled())
				{
					logger.debug("0x2004 writing " + this.getRawControlByte() + " to ioAddress: " + Integer.toHexString(Platform.getPpu().getPpuSprRamAddressRegister().getRawControlByte()));
				}
				Platform.getPpuMemory().setSprRamFromHexAddress(Platform.getPpu().getPpuSprRamAddressRegister().getRawControlByte(), this.getRawControlByte());
				this.incrementIoAddress();
			}
			
			this.clear();
		}
		return PPUSprRamIORegister.CYCLES_PER_EXECUTION;
	}
	
	public void incrementIoAddress()
	{
		Platform.getPpu().getPpuSprRamAddressRegister().setRegisterValue(Platform.getPpu().getPpuSprRamAddressRegister().getRawControlByte() + 1);
	}
	
	public void setRegisterValue(int value)
	{
		this.setRawControlByte(value);
	}
	
	public int getRegisterValue()
	{
		
		return this.getSramBuffer();
	}
	
	private void clear()
	{
		this.setRawControlByte(null);
	}

	/*
	private Integer getIoAddress()
	{
		return ioAddress;
	}

	protected void setIoAddress(Integer ioAddress)
	{
		this.ioAddress = ioAddress;
	}
	
	private void incrementIoAddress()
	{
		this.setIoAddress((this.getIoAddress() + 1) & 0xFF);
	}
	*/

	private Integer getRawControlByte()
	{
		return rawControlByte;
	}

	private void setRawControlByte(Integer rawControlByte)
	{
		this.rawControlByte = rawControlByte;
	}
	
	public static PPUSprRamIORegister getRegister()
	{
		return register;
	}

	public int getSramBuffer()
	{
		return sramBuffer;
	}

	public void setSramBuffer(int sramBuffer)
	{
		this.sramBuffer = sramBuffer;
	}
}
