package com.lambelly.lambnes.platform.ppu.registers;

import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.platform.cpu.NesCpuMemory;
import com.lambelly.lambnes.platform.ppu.NesPpuMemory;

import org.apache.log4j.*;
import org.apache.commons.lang.ArrayUtils;

public class PPUSpriteDMARegister
{
	public static final int REGISTER_ADDRESS = 0x4014;
	private static final int CYCLES_PER_EXECUTION = 513;
	private Integer rawControlByte = null;
	private Logger logger = Logger.getLogger(PPUSpriteDMARegister.class);
	private NesCpuMemory cpuMemory;
	private NesPpuMemory ppuMemory;
	private PPUSprRamAddressRegister ppuSprRamAddressRegister;
	
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
			int dmaToStart = this.getPpuSprRamAddressRegister().getRawControlByte();
			
			if(logger.isDebugEnabled())
			{
				logger.debug("raw control bytes: 0x4014: " + this.getRawControlByte() + " 0x2003: " + this.getPpuSprRamAddressRegister().getRawControlByte());
				logger.debug("reading from memory: " + Integer.toHexString(this.getRawControlByte()));
				logger.debug("start: " + dmaFromStart);
				logger.debug("pulling dma from: " + Integer.toHexString(dmaFromStart));
				logger.debug("pulling dma to: " + Integer.toHexString(dmaFromStart + 0xFF));
				logger.debug("memory at start: " + this.getCpuMemory().getMemoryFromHexAddress(dmaFromStart));
			}

			//System.arraycopy(Platform.getCpuMemory().getMemory(), start, Platform.getPpuMemory().getSprRam(), 0, 256);
			for (int x = 0; x < 256; x++)
			{
				int value = this.getCpuMemory().getMemoryFromHexAddress(dmaFromStart + x);
				int address = (dmaToStart + x) & 0xFF;
				this.getPpuMemory().setSprRamFromHexAddress(address, value);
			} 
			
			
			if(logger.isDebugEnabled())
			{
				for (int i = 0; i< this.getPpuMemory().getSprRam().length; i++)
				{
					logger.debug("spr[" + i + "] : " + this.getPpuMemory().getSprRam()[i]);
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

	public NesCpuMemory getCpuMemory()
    {
    	return cpuMemory;
    }

	public void setCpuMemory(NesCpuMemory cpuMemory)
    {
    	this.cpuMemory = cpuMemory;
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
