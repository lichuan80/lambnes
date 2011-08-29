package com.lambelly.lambnes.platform.ppu.registers;

import com.lambelly.lambnes.platform.Platform;
import org.apache.log4j.*;
import org.apache.commons.lang.ArrayUtils;

public class PPUSpriteDMARegister
{
	public static final int REGISTER_ADDRESS = 0x4014;
	private static PPUSpriteDMARegister register = new PPUSpriteDMARegister();
	private static final int CYCLES_PER_EXECUTION = 513;
	private Integer rawControlByte = null;
	private Logger logger = Logger.getLogger(PPUSpriteDMARegister.class);
	
	private PPUSpriteDMARegister()
	{
		
	}
	
	public int cycle()
	{
		int cyclesPassed = 0;
		
		if (this.getRawControlByte() != null)
		{
			cyclesPassed = PPUSpriteDMARegister.CYCLES_PER_EXECUTION;
			int dmaFromStart = this.getRawControlByte() << 8;
			int dmaToStart = Platform.getPpu().getPpuSprRamAddressRegister().getRawControlByte();
			
			//if(logger.isDebugEnabled())
			{
				logger.info("raw control bytes: 0x4014: " + this.getRawControlByte() + " 0x2003: " + Platform.getPpu().getPpuSprRamAddressRegister().getRawControlByte());
				logger.info("reading from memory: " + Integer.toHexString(this.getRawControlByte()));
				logger.info("start: " + dmaFromStart);
				logger.info("pulling dma from: " + Integer.toHexString(dmaFromStart));
				logger.info("pulling dma to: " + Integer.toHexString(dmaFromStart + 0xFF));
				logger.info("memory at start: " + Platform.getCpuMemory().getMemoryFromHexAddress(dmaFromStart));
			}

			//System.arraycopy(Platform.getCpuMemory().getMemory(), start, Platform.getPpuMemory().getSprRam(), 0, 256);
			for (int x = 0; x < 256; x++)
			{
				int value = Platform.getCpuMemory().getMemoryFromHexAddress(dmaFromStart + x);
				int address = (dmaToStart + x) & 0xFF;
				logger.info("setting: " + x + " to " + value);
				Platform.getPpuMemory().setSprRamFromHexAddress(address, value);
			} 
			
			
			if(logger.isDebugEnabled())
			{
				for (int i = 0; i< Platform.getPpuMemory().getSprRam().length; i++)
				{
					logger.debug("spr[" + i + "] : " + Platform.getPpuMemory().getSprRam()[i]);
				}
			}
			
			this.clear();
		}
		
		return cyclesPassed;
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
