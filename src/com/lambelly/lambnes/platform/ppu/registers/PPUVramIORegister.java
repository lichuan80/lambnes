package com.lambelly.lambnes.platform.ppu.registers;

import com.lambelly.lambnes.platform.Platform;
import org.apache.log4j.*;

public class PPUVramIORegister
{
	public static final int REGISTER_ADDRESS = 0x2007;
	private static PPUVramIORegister register = new PPUVramIORegister();
	private static final int CYCLES_PER_EXECUTION = 0;
	private Integer ioAddress = null;
	private Integer rawControlByte = null;
	private int vramBuffer = 0; 
	private Logger logger = Logger.getLogger(PPUVramIORegister.class);
	
	private PPUVramIORegister()
	{
		
	}
	public int cycle()
	{
		if (logger.isDebugEnabled())
		{	
			logger.debug("raw control byte: " + this.getRawControlByte());
			logger.debug("ioAddress: " + this.getIoAddress());
			logger.debug("buffer value: " + this.getVramBuffer());
		}
		
		if (this.getIoAddress() != null)
		{
			// write functions
			if (this.getRawControlByte() != null)
			{
				//logger.info("VRAM IO register writing value " + Integer.toHexString(this.getRawControlByte()) + " to " + Integer.toHexString(this.getIoAddress()) + " with X: " + Integer.toHexString(Platform.getCpu().getX()) + " and Y: " + Integer.toHexString(Platform.getCpu().getY()));
				Platform.getPpuMemory().setMemoryFromHexAddress(this.getIoAddress(), this.getRawControlByte());
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
			this.setVramBuffer(Platform.getPpuMemory().getNameTable3().getMemoryFromHexAddress(this.getIoAddress()));
		}
		else
		{
			this.setVramBuffer(Platform.getPpuMemory().getMemoryFromHexAddress(this.getIoAddress()));
		}
		
		if (logger.isDebugEnabled())
		{
			logger.debug("pulling information from register: buffer was: " + bufferedValue + " buffer is now: " + this.getVramBuffer());
		}
		
		if (this.getIoAddress() >= 0x3F00 && this.getIoAddress() <= 0x3FFF)
		{
			value = Platform.getPpuMemory().getMemoryFromHexAddress(this.getIoAddress());
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
		logger.debug("vram address: " + ioAddress);
		this.ioAddress = ioAddress;
	}
	
	private void incrementIoAddress()
	{
		this.ioAddress++;
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
		logger.debug("setting buffer to: " + buffer);
		this.vramBuffer = buffer;
	}

	public static PPUVramIORegister getRegister()
	{
		return register;
	}

	private static void setRegister(PPUVramIORegister register)
	{
		PPUVramIORegister.register = register;
	}
}
