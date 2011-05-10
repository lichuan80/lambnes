package com.lambelly.lambnes.platform.ppu.registers;

import com.lambelly.lambnes.platform.Platform;
import org.apache.log4j.*;

public class PPUSprRamIORegister
{
	public static final int REGISTER_ADDRESS = 0x2004;
	private static final int CYCLES_PER_EXECUTION = 0;
	private static PPUSprRamIORegister register = new PPUSprRamIORegister();
	private Integer ioAddress = null;
	private Integer rawControlByte = null;
	private Logger logger = Logger.getLogger(PPUSprRamIORegister.class);
	
	private PPUSprRamIORegister()
	{
		
	}
	
	public int cycle()
	{
		// get raw control byte
		logger.debug("0x2004 raw control byte: " + this.getRawControlByte());
		logger.debug("0x2004 IO address: " + this.getIoAddress());
		if (this.getRawControlByte() != null)
		{
			if (this.getIoAddress() != null)
			{			
				// write control byte to ioAddress
				if(logger.isDebugEnabled())
				{
					logger.debug("0x2004 writing " + this.getRawControlByte() + " to ioAddress: " + Integer.toHexString(this.getIoAddress()));
				}
				Platform.getPpuMemory().setSprRamFromHexAddress(this.getIoAddress(), this.getRawControlByte());
				this.incrementIoAddress();
			}
			else
			{
				this.setIoAddress(this.getRawControlByte());
			}
			
			this.clear();
		}
		return PPUSprRamIORegister.CYCLES_PER_EXECUTION;
	}
	
	public void setRegisterValue(int value)
	{
		this.setRawControlByte(value);
	}
	
	private void clear()
	{
		this.setRawControlByte(null);
	}

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
}
