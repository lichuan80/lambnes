package com.lambelly.lambnes.platform.ppu.registers;

import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.platform.ppu.NesPpuMemory;

import org.apache.log4j.*;

public class PPUSprRamIORegister
{
	public static final int REGISTER_ADDRESS = 0x2004;
	private static final int CYCLES_PER_EXECUTION = 0;
	private Integer rawControlByte = null;
	private int sramBuffer = 0;
	private Logger logger = Logger.getLogger(PPUSprRamIORegister.class);
	private NesPpuMemory ppuMemory;
	private PPUSprRamAddressRegister ppuSprRamAddressRegister;
	
	private PPUSprRamIORegister()
	{
		
	}
	
	public int cycle()
	{	
		if (this.getPpuSprRamAddressRegister().getRawControlByte() != null)
		{
			this.setSramBuffer(this.getPpuMemory().getSprRamFromHexAddress(this.getPpuSprRamAddressRegister().getRawControlByte()));
		}
		
		if (this.getRawControlByte() != null)
		{
			
			if (this.getPpuSprRamAddressRegister().getRawControlByte() != null)
			{			
				// write control byte to ioAddress
				//if(logger.isDebugEnabled())
				{
					logger.info("0x2004 raw control byte: " + this.getRawControlByte());
					logger.info("0x2004 IO address: " + this.getPpuSprRamAddressRegister().getRawControlByte());
					logger.info("0x2004 writing " + this.getRawControlByte() + " to ioAddress: " + Integer.toHexString(this.getPpuSprRamAddressRegister().getRawControlByte()));
				}
				this.getPpuMemory().setSprRamFromHexAddress(this.getPpuSprRamAddressRegister().getRawControlByte(), this.getRawControlByte());
				this.incrementIoAddress();
			}
			
			this.clear();
		}
		return PPUSprRamIORegister.CYCLES_PER_EXECUTION;
	}
	
	public void incrementIoAddress()
	{
		this.getPpuSprRamAddressRegister().setRegisterValue((this.getPpuSprRamAddressRegister().getRawControlByte() + 1) & 0xFF);
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

	private Integer getRawControlByte()
	{
		return rawControlByte;
	}

	private void setRawControlByte(Integer rawControlByte)
	{
		this.rawControlByte = rawControlByte;
	}

	public int getSramBuffer()
	{
		return sramBuffer;
	}

	public void setSramBuffer(int sramBuffer)
	{
		this.sramBuffer = sramBuffer;
	}

	public NesPpuMemory getPpuMemory()
    {
    	return ppuMemory;
    }

	public void setPpuMemory(NesPpuMemory ppuMemory)
    {
    	this.ppuMemory = ppuMemory;
    }

	public PPUSprRamAddressRegister getPpuSprRamAddressRegister()
    {
    	return ppuSprRamAddressRegister;
    }

	public void setPpuSprRamAddressRegister(
            PPUSprRamAddressRegister ppuSprRamAddressRegister)
    {
    	this.ppuSprRamAddressRegister = ppuSprRamAddressRegister;
    }
}
