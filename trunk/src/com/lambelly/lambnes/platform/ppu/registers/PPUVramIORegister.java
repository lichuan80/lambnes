package com.lambelly.lambnes.platform.ppu.registers;

import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.platform.ppu.NesPpuMemory;

import org.apache.log4j.*;

public class PPUVramIORegister
{
	public static final int REGISTER_ADDRESS = 0x2007;
	private static final int CYCLES_PER_EXECUTION = 0;
	private Integer ioAddress = null;
	private Integer rawControlByte = null;
	private int vramBuffer = 0; 
	private NesPpuMemory ppuMemory;
	private PPUControlRegister ppuControlRegister;
	private Logger logger = Logger.getLogger(PPUVramIORegister.class);
	
	private PPUVramIORegister()
	{
		
	}
	public int cycle()
	{
		//logger.debug("raw control byte: " + this.getRawControlByte());
		//logger.debug("ioAddress: " + this.getIoAddress());
		//logger.debug("buffer value: " + this.getVramBuffer());
		
		if (this.getIoAddress() != null)
		{
			// write functions
			if (this.getRawControlByte() != null)
			{
				//logger.info("VRAM IO register writing value " + Integer.toHexString(this.getRawControlByte()) + " to " + Integer.toHexString(this.getIoAddress()) + " with X: " + Integer.toHexString(Platform.getCpu().getX()) + " and Y: " + Integer.toHexString(Platform.getCpu().getY()));
				this.getPpuMemory().setMemoryFromHexAddress(this.getIoAddress(), this.getRawControlByte());
				this.incrementIoAddress();
			}
		}	
		
		this.clear();
		
		return PPUVramIORegister.CYCLES_PER_EXECUTION;
	}
	
	private void clear()
	{
		this.setRawControlByte(null);
	}
	
	public int getRegisterValue()
	{
		int value = 0;
		
		int bufferedValue = this.getVramBuffer();
		
		// getMemoryFromHexAddress of fails for buffer read. Buffer read ought to be able to access nametable memory hidden by palette.
		if (this.getIoAddress() >= 0x3F00 && this.getIoAddress() <= 0x3FFF)
		{
			this.setVramBuffer(this.getPpuMemory().getNameTable3().getMemoryFromHexAddress(this.getIoAddress()));
		}
		else
		{
			this.setVramBuffer(this.getPpuMemory().getMemoryFromHexAddress(this.getIoAddress()));
		}
		
		//logger.debug("pulling information from register: buffer was: " + bufferedValue + " buffer is now: " + this.getVramBuffer());
		
		if (this.getIoAddress() >= 0x3F00 && this.getIoAddress() <= 0x3FFF)
		{
			value = this.getPpuMemory().getMemoryFromHexAddress(this.getIoAddress());
		}
		else
		{
			value = bufferedValue;
		}
		
		this.incrementIoAddress();
		
		return value;
	}
	
	public void setRegisterValue(int value)
	{
		this.setRawControlByte(value);
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
		if (this.getPpuControlRegister().getPpuAddressIncrement() == PPUControlRegister.PPU_ADDRESS_INCREMENT_1)
		{
			this.ioAddress++;
		}
		else
		{
			this.ioAddress += 32;
		}
	}

	private Integer getRawControlByte()
	{
		return rawControlByte;
	}

	private void setRawControlByte(Integer rawControlByte)
	{
		this.rawControlByte = rawControlByte;
	}

	public int getVramBuffer()
	{
		return vramBuffer;
	}

	public void setVramBuffer(int buffer)
	{
		this.vramBuffer = buffer;
	}

	public NesPpuMemory getPpuMemory()
    {
    	return ppuMemory;
    }
	public void setPpuMemory(NesPpuMemory ppuMemory)
    {
    	this.ppuMemory = ppuMemory;
    }
	public PPUControlRegister getPpuControlRegister()
    {
    	return ppuControlRegister;
    }
	public void setPpuControlRegister(PPUControlRegister ppuControlRegister)
    {
    	this.ppuControlRegister = ppuControlRegister;
    }
}
