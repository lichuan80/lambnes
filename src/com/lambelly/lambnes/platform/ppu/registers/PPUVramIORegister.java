package com.lambelly.lambnes.platform.ppu.registers;

import com.lambelly.lambnes.platform.Platform;
import org.apache.log4j.*;

public class PPUVramIORegister
{
	public static final int REGISTER_ADDRESS = 0x2007;
	private static PPUVramIORegister register = new PPUVramIORegister();
	private static final int CYCLES_PER_EXECUTION = 0;
	private boolean readAccess = false;
	private boolean writeAccess = false;
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
		}
		
		// wait until address is set as well as have something to write.
		if (this.isReadAccess())
		{
			this.setReadAccess(false);
			this.incrementIoAddress();

			this.setVramBuffer(Platform.getPpuMemory().getMemoryFromHexAddress(this.getIoAddress()));
		}
		
		if (this.isWriteAccess())
		{
			this.setWriteAccess(false);
			Platform.getPpuMemory().setMemoryFromHexAddress(this.getIoAddress(), this.getRawControlByte());
			this.incrementIoAddress();			
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
		this.setReadAccess(true);
		return this.getVramBuffer();
	}
	
	public void setRegisterValue(int value)
	{
		this.setWriteAccess(true);
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
		this.vramBuffer = buffer;
	}

	public boolean isReadAccess()
	{
		return readAccess;
	}

	public void setReadAccess(boolean access)
	{
		this.readAccess = access;
	}

	public boolean isWriteAccess()
	{
		return writeAccess;
	}

	public void setWriteAccess(boolean writeAccess)
	{
		this.writeAccess = writeAccess;
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
