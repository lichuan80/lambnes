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
			if(logger.isDebugEnabled())
			{
				logger.debug("reading from memory: " + Integer.toHexString(this.getRawControlByte()));
			}
			
			int start = (this.getRawControlByte() * 0x100);
			if(logger.isDebugEnabled())
			{
				logger.debug("start: " + start);
			}
			
			int[] a = ArrayUtils.subarray(Platform.getCpuMemory().getRam(),start - 0x200,(start  - 0x200) + 256);
			if(logger.isDebugEnabled())
			{
				logger.debug("pulling dma from: " + Integer.toHexString((start - 0x200)));
				logger.debug("pulling dma to: " + Integer.toHexString(((start- 0x200) + 0xFF)));
				logger.debug("a size: " + a.length);
				logger.debug("memory at start: " + Platform.getCpuMemory().getMemoryFromHexAddress(start));
				logger.debug("a[0]: " + a[0]);
			}
			Platform.getPpuMemory().setSprRam(a);

			if(logger.isDebugEnabled())
			{
				logger.debug("SPR: " + Platform.getPpuMemory().getSprRam()[0]);
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
