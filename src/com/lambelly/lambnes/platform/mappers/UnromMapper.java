package com.lambelly.lambnes.platform.mappers;

import org.apache.log4j.Logger;

import com.lambelly.lambnes.platform.cpu.NesCpuMemory;

public class UnromMapper extends Mapper0 implements Mapper
{
	private Integer rawControlByte = null;
	private int[] prgrom = null;
	private Logger logger = Logger.getLogger(UnromMapper.class);
	NesCpuMemory cpuMemory = null;
	
	public UnromMapper(NesCpuMemory cpuMemory)
	{
		this.setCpuMemory(cpuMemory);
	}

	public void setMemoryFromHexAddress(int address, int value) throws IllegalStateException
	{	
	    super.setMemoryFromHexAddress(address,value);
	    
		if (address >= 0x8000 && address <= 0xFFFF)
		{
			logger.info("write to 0x8000 - 0xFFFF: address: " + address + " value: " + value);

			// swap out prg-rom1 with requested page
			int pageStartAddress = value * 16384;
			
			System.arraycopy(this.getPrgrom(), pageStartAddress, this.getCpuMemory().getMemory(), NesCpuMemory.PRG_ROM_BASE, 16384);
		}
	}	
	
	public void setProgramInstructions(int[] programInstructions)
	{
		this.setPrgrom(programInstructions);
		
		// load bank0 -- use first page from prgRom
		System.arraycopy(programInstructions, 0, this.getCpuMemory().getMemory(), NesCpuMemory.PRG_ROM_BASE, 16384);
		
		// load bank1 -- use last page from prgRom
		System.arraycopy(programInstructions, (programInstructions.length - 16384), this.getCpuMemory().getMemory(), (NesCpuMemory.PRG_ROM_BASE + 16384), 16384);
	}

	public Integer getRawControlByte()
    {
    	return rawControlByte;
    }

	public void setRawControlByte(Integer rawControlByte)
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

	public int[] getPrgrom()
    {
    	return prgrom;
    }

	public void setPrgrom(int[] prgrom)
    {
    	this.prgrom = prgrom;
    }
}
