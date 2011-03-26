package com.lambelly.lambnes.platform.ppu.registers;

import com.lambelly.lambnes.platform.Platform;
import org.apache.log4j.*;
import org.apache.commons.lang.ArrayUtils;

public class PPUSpriteDMARegister
{
	public static final int REGISTER_ADDRESS = 0x4014;
	private static PPUSpriteDMARegister register = new PPUSpriteDMARegister();
	private Integer rawControlByte = null;
	private Logger logger = Logger.getLogger(PPUSpriteDMARegister.class);
	
	private PPUSpriteDMARegister()
	{
		
	}
	
	public void cycle()
	{
		if (this.getRawControlByte() != null)
		{
			int start = (this.getRawControlByte() * 0x100);
			
			if(logger.isDebugEnabled())
			{
				logger.debug("reading from memory: " + Integer.toHexString(this.getRawControlByte()));
				logger.debug("start: " + start);
				logger.debug("pulling dma from: " + Integer.toHexString(start));
				logger.debug("pulling dma to: " + Integer.toHexString(start + 0xFF));
				logger.debug("memory at start: " + Platform.getCpuMemory().getMemoryFromHexAddress(start));
			}

			System.arraycopy(Platform.getCpuMemory().getMemory(), start, Platform.getPpuMemory().getSprRam(), 0, 256);
			
			if(logger.isDebugEnabled())
			{
				for (int i = 0; i< Platform.getPpuMemory().getSprRam().length; i++)
				{
					logger.debug("spr[" + i + "] : " + Platform.getPpuMemory().getSprRam()[i]);
				}
			}
			
			this.clear();
		}
	}
	
	private void clear()
	{
		this.setRawControlByte(null);
	}
	
	public static PPUSpriteDMARegister getRegister()
	{
		return register;
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
}
