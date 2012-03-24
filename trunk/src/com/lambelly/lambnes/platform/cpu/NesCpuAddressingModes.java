package com.lambelly.lambnes.platform.cpu;

import org.apache.log4j.Logger;

import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.platform.ppu.NesPpuMemory;
import com.lambelly.lambnes.util.BitUtils;

public class NesCpuAddressingModes
{
	private Logger logger = Logger.getLogger(NesCpuAddressingModes.class);
	private NesCpu cpu;
	private NesCpuMemory cpuMemory;
	
	public NesCpuAddressingModes()
	{
		
	}
	
	/**
	 * getImmediate - #aa returns the next value in program memory to be used by
	 * the instruction
	 * 
	 * @return
	 */
	public int getImmediateValue()
	{
		return this.getCpuMemory().getNextPrgRomByte();
	}

	/**
	 * getAbsolute - aaaa 
	 * contains the 16 bit address of the 8 bit value to be used
	 * 
	 * @return
	 */
	public int getAbsoluteValue()
	{
		int address = this.getAbsoluteAddress();
		if(logger.isDebugEnabled())
		{
			logger.debug("using address: " + Integer.toHexString(address));
		}
		return this.getCpuMemory().getMemoryFromHexAddress(address);
	}

	/**
	 * getAbsoluteIndexXValue - aaaa,X
	 * adds X to 16 bit address
	 * 
	 * @return
	 */
	public int getAbsoluteIndexedXValue()
	{
		int address = this.getAbsoluteIndexedXAddress();
		return this.getCpuMemory().getMemoryFromHexAddress(address);
	}

	/**
	 * getAbsoluteIndexYValue - aaaa,Y
	 * adds Y to 16 bit address
	 * 
	 * @return
	 */
	public int getAbsoluteIndexedYValue()
	{
		int address = this.getAbsoluteIndexedYAddress();
		return this.getCpuMemory().getMemoryFromHexAddress(address);
	}	
	
	/**
	 * getZeroPageValue - aa - 8 bit address returns value from zero page
	 * 
	 * @return
	 */
	public int getZeroPageValue()
	{
		int address = this.getZeroPageAddress();
		return this.getCpuMemory().getMemoryFromHexAddress(address);
	}
	
	/**
	 * getIndirectAbsoluteValue() | (aaaa) | aa = 2 hex digits private int
	 * 
	 */
	public int getIndirectAbsoluteValue() 
	{
		int address = this.getIndirectAbsoluteAddress();
		return BitUtils.unsplitAddress(this.getCpuMemory().getMemoryFromHexAddress(address), this.getCpuMemory().getMemoryFromHexAddress(++address));
	}

	/**
	 * getZeroPageIndexedXValue - aa,x - 8 bit address returns value from address + x register
	 * 
	 * @return
	 */
	public int getZeroPageIndexedXValue() 
	{
		int address = this.getZeroPageIndexedXAddress();
		return this.getCpuMemory().getMemoryFromHexAddress(address);
	}
	
	/**
	 * getZeroPageIndexedYValue() | aa,Y | digits as private int
	 */
	public int getZeroPageIndexedYValue()
	{
		int address = this.getZeroPageIndexedYAddress();
		return this.getCpuMemory().getMemoryFromHexAddress(address);
	}
	
	/**
	 *  getIndexedIndirectXValue (aa,X)
	 *  zero page address + x points to a 16bit address
	 *  
	 *  @return
	 */
	public int getIndexedIndirectXValue()
	{
		int address = this.getIndexedIndirectXAddress();
		return this.getCpuMemory().getMemoryFromHexAddress(address);
	}
	
	 /**
	 * getIndirectIndexedYValue() - (aa),Y 
	 * gets the value of a 16 bit address from zero page based on 8 bit address, adds Y, then gets the value the 16 bit address points at 
	 * @return 
	 */
	public int getIndirectIndexedYValue()
	{
		int address = this.getIndirectIndexedYAddress();
		return this.getCpuMemory().getMemoryFromHexAddress(address);
	}
	
	public int getAbsoluteAddress()
	{
		int address = this.getCpuMemory().getNextPrgRomShort();
		return address;
	}
	
	public int getIndirectAbsoluteAddress()
	{
		int lsbAddress = this.getCpuMemory().getNextPrgRomShort();
		int lsb = this.getCpuMemory().getMemoryFromHexAddress(lsbAddress);
		
		// simulate msb bug
		int lsbAddressMsb = lsbAddress & 0xFF00;
		int lsbAddressLsb = lsbAddress & 0x00FF;
		lsbAddressLsb = lsbAddressLsb + 1;
		lsbAddressLsb = lsbAddressLsb & 0x00FF;
		int msb = this.getCpuMemory().getMemoryFromHexAddress(lsbAddressMsb | lsbAddressLsb);
		
		//logger.debug("lsbAddress: " + Integer.toHexString(lsbAddress));
		//logger.debug("lsb: " + Integer.toHexString(lsb));
		//logger.debug("msb: " + Integer.toHexString(msb));
	
		return BitUtils.unsplitAddress(msb, lsb);
	}
	
	public int getAbsoluteIndexedXAddress()
	{
		int address = this.getCpuMemory().getNextPrgRomShort();
		address += this.getCpu().getX();
		return address & Platform.SIXTEEN_BIT_MASK;
	}
	
	public int getAbsoluteIndexedYAddress()
	{
		int address = this.getCpuMemory().getNextPrgRomShort();
		if(logger.isDebugEnabled())
		{
			logger.debug("Y: " + Integer.toHexString(this.getCpu().getY()));
			logger.debug("Address: " + Integer.toHexString(address));
		}
		address += this.getCpu().getY();

		return address & Platform.SIXTEEN_BIT_MASK;
	}
	
	public int getZeroPageAddress()
	{
		return this.getCpuMemory().getNextPrgRomByte();
	}
	
	public int getZeroPageIndexedXAddress()
	{
		// get initial address
		int address = this.getCpuMemory().getNextPrgRomByte();
		
		// add address to x register
		address += this.getCpu().getX();

		// if address is > FF it wraps around.
		address = address & Platform.EIGHT_BIT_MASK;

		return address;
	}
	
	public int getZeroPageIndexedYAddress()
	{
		// get initial address
		int address = this.getCpuMemory().getNextPrgRomByte();
		
		// add address to Y register
		address += this.getCpu().getY();
		
		// if address is > FF it wraps around.
		address = address & Platform.EIGHT_BIT_MASK;

		return address;
	}	
	
	public int getIndexedIndirectXAddress()
	{ 
		int lowByteAddress = this.getCpuMemory().getNextPrgRomByte() + this.getCpu().getX() & Platform.EIGHT_BIT_MASK;
		int highByteAddress =  lowByteAddress + 1 & Platform.EIGHT_BIT_MASK;
		
		return BitUtils.unsplitAddress(this.getCpuMemory().getMemoryFromHexAddress(highByteAddress), this.getCpuMemory().getMemoryFromHexAddress(lowByteAddress));
	}
	
	public int getIndirectIndexedYAddress()
	{
		int lowByteAddress = this.getCpuMemory().getNextPrgRomByte();
		int highByteAddress =  lowByteAddress + 1 & Platform.EIGHT_BIT_MASK;
		int finalAddress = BitUtils.unsplitAddress(this.getCpuMemory().getMemoryFromHexAddress(highByteAddress), this.getCpuMemory().getMemoryFromHexAddress(lowByteAddress));
		finalAddress += this.getCpu().getY();
		return finalAddress & Platform.SIXTEEN_BIT_MASK;
	}
	
	public int getRelativeAddress()
	{	
		int offset = this.getCpuMemory().getNextPrgRomByte();
		if (offset > 0x7F)
		{
			offset = offset - 0x100;
		}
		return (offset + this.getCpuMemory().getProgramCounter());
	}
	
	public NesCpu getCpu()
    {
    	return cpu;
    }

	public void setCpu(NesCpu cpu)
    {
    	this.cpu = cpu;
    }

	public NesCpuMemory getCpuMemory()
    {
    	return cpuMemory;
    }

	public void setCpuMemory(NesCpuMemory memory)
    {
    	this.cpuMemory = memory;
    }
}
