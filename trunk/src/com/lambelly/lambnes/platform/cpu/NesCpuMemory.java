package com.lambelly.lambnes.platform.cpu;

import org.apache.log4j.*;

import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.platform.apu.registers.APUControlRegister;
import com.lambelly.lambnes.platform.apu.registers.APUPulse1ChannelRegister;
import com.lambelly.lambnes.platform.apu.registers.APUPulse1LengthCounterRegister;
import com.lambelly.lambnes.platform.apu.registers.APUPulse1SweepRegister;
import com.lambelly.lambnes.platform.apu.registers.APUPulse1TimerLowRegister;
import com.lambelly.lambnes.platform.apu.registers.APUFrameCounterRegister;
import com.lambelly.lambnes.platform.controllers.ControlRegister1;
import com.lambelly.lambnes.platform.controllers.ControlRegister2;
import com.lambelly.lambnes.platform.ppu.registers.PPUControlRegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUMaskRegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUSprRamAddressRegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUSprRamIORegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUSpriteDMARegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUStatusRegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUScrollRegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUVramAddressRegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUVramIORegister;
import com.lambelly.lambnes.util.BitUtils;
import com.lambelly.lambnes.platform.mappers.Mapper;
import com.lambelly.lambnes.platform.mappers.Mapper0;
import com.lambelly.lambnes.util.NumberConversionUtils;

/**
 * 
 * @author thomasmccarthy
 */
public class NesCpuMemory
{
	/* 
	 * cpu memory map 
	 * $0000 - $00FF 256 bytes Zero Page - Special Zero Page addressing modes give faster memory read/write access 
	 * $0100 - $01FF 256 bytes Stack memory 
	 * $0200 - $07FF 1536 bytes RAM 
	 * $0800 - $0FFF 2048 bytes Mirror of $0000 - $07FF 
	 * 	$0800 - $08FF Zero Page 
	 * 	$0900 - $09FF Stack
	 * 	$0A00 - $0FFF RAM $1000–$17FF 2048 bytes Mirror of $0000–$07FF
	 * $1000 - $10FF Zero Page 
	 * $1100 - $11FF Stack 
	 * $1200 - $17FF RAM
	 * $1800 - $1FFF 2048 bytes Mirror of $0000 - $07FF 
	 * 	$1800 - $18FF Zero Page
	 * 	$1900 - $19FF Stack 
	 * 	$1A00 - $1FFF RAM 
	 * $2000 - $2007 8 bytes Input/Output registers 
	 * $2008 - $3FFF 8184 bytes Mirror of $2000 - $2007 (multiple times) 
	 * $4000 - $401F 32 bytes Input/Output registers 
	 * $4020 - $5FFF 8160 bytes Expansion ROM - Used with Nintendo's MMC5 to expand the capabilities of VRAM. 
	 * $6000 - $7FFF 8192 bytes SRAM - Save Ram used to save data between game plays. 
	 * $8000 - $BFFF 16384 bytes PRG-ROM lower bank - executable code 
	 * $C000 - $FFFF 16384 bytes PRG-ROM upper bank - executable code 
	 * $FFFA - $FFFB 2 bytes Address of Non Maskable Interrupt (NMI) handler routine 
	 * $FFFC - $FFFD 2 bytes Address of Power on reset handler routine 
	 * $FFFE - $FFFF 2 bytes Address of Break (BRK instruction)handler routine
	 */

	private int stackPointer = 0;
	private int programCounter = 0x8000; // starts at start of lower bank
	private int[] memory = new int[65536];

    public static final int STACK_BASE = 0x0100;
    public static final int STACK_MAX = 0x01FF;
    public static final int PRG_ROM_BASE = 0x8000; // lower bank
    public static final int NMI_VECTOR = 0xFFFA;
    public static final int RESET_VECTOR = 0xFFFC;
    public static final int IRQBRK_VECTOR = 0xFFFE;
	
    private PPUSprRamIORegister ppuSprRamIORegister; // 0x2004
    private PPUSprRamAddressRegister ppuSprRamAddressRegister; // 0x2003 
    private PPUControlRegister ppuControlRegister; // 0x2000
    private PPUStatusRegister ppuStatusRegister; // 0x2002
    private PPUVramAddressRegister ppuVramAddressRegister; // 0x2006
    private PPUVramIORegister ppuVramIORegister; // 0x2007
    private PPUScrollRegister ppuScrollRegister; // 0x2005
    private PPUSpriteDMARegister ppuSpriteDmaRegister; // 0x4014
    private PPUMaskRegister ppuMaskRegister; // 0x2001
    
    private APUControlRegister apuControlRegister; // 0x4015
    private APUFrameCounterRegister apuFrameCounterRegister; // 0x4017
    private APUPulse1ChannelRegister apuPulse1ChannelRegister; // 0x4000
    private APUPulse1LengthCounterRegister apuPulse1LengthCounterRegister; // 0x4003
    private APUPulse1SweepRegister apuPulse1SweepRegister; // 0x4001
    private APUPulse1TimerLowRegister apuPulse1TimerLowRegister; // 0x4002
    
    private ControlRegister1 controlRegister1;
    private ControlRegister2 controlRegister2; 
    
    private Mapper mapper = null;
    
	private static Logger logger = Logger.getLogger(NesCpuMemory.class);

	public void init(int[] programInstructions)
	{
		this.setProgramInstructions(programInstructions);
	}

	public int getPrgRomByte(int address)
	{
		return this.getMemory()[address];
	}

	public int getNextPrgRomByte()
	{
		return this.getNextPrgRom();
	}

	public int getNextPrgRomShort()
	{
		int lowerBit = this.getNextPrgRom();
		return BitUtils.unsplitAddress(this.getNextPrgRom(), lowerBit);
	}

	private int getNextPrgRom()
	{
		int b = 0;

		// upper bank
		b = this.getMemoryFromHexAddress(this.getProgramCounter());
		
		// increment counter
		this.incrementProgramCounter();

		// return b
		return b;
	}

	public int getMemoryFromHexAddress(int address) throws IllegalStateException
	{		
		return this.getMapper().getMemoryFromHexAddress(address);
	}

	public void setMemoryFromHexAddress(int address, int value) throws IllegalStateException
	{
		this.getMapper().setMemoryFromHexAddress(address, value);
	}	
	
	public void setProgramInstructions(int[] programInstructions)
	{
		this.getMapper().setProgramInstructions(programInstructions);
	}

	public void resetCounters()
	{
		this.setProgramCounter(NesCpuMemory.PRG_ROM_BASE);
		this.setStackPointer(0);
	}
	
	public void pushStack(int value)
	{
		// logger.debug("stack pointer: " + this.getStackPointer());
		if (Integer.bitCount(value) > 8)
		{
			throw new IllegalStateException("tried to push non-8 bit digit on stack: 0x" + Integer.toHexString(value));
		}
		
		// stackPointer contains value between 0x00 and 0xFF
		// get stack pointer
		int stackPointer = this.getStackPointer();
		
		// set memory to stack pointer
		int address = Integer.parseInt("01" + NumberConversionUtils.generateHexStringWithleadingZeros(stackPointer, 2),16);
		this.setMemoryFromHexAddress(address, value);
		
		// decrement stack pointer
		--stackPointer;

		// logger.debug("stack pointer: " + stackPointer);


		// roll over
		stackPointer = (stackPointer & Platform.EIGHT_BIT_MASK);

		//logger.debug("stack pointer: " + stackPointer);
		
		// set stack pointer
		this.setStackPointer(stackPointer);
	}
	
	public int popStack()
	{
		int stackPointer = this.getStackPointer();
		
		// increment
		++stackPointer;
		
		// roll over
		stackPointer = (stackPointer & Platform.EIGHT_BIT_MASK);
	
		// obtain return value
		int value = this.getMemoryFromHexAddress(Integer.parseInt("01" + NumberConversionUtils.generateHexStringWithleadingZeros(stackPointer, 2),16));

		// set stack pointer
		this.setStackPointer(stackPointer);
		
		return value;
	}

	/**
	 * @return the stackPointer
	 */
	public int getStackPointer()
	{
		return stackPointer;
	}

	/**
	 * @param stackPointer
	 *            the stackPointer to set
	 */
	public void setStackPointer(int stackPointer)
	{
		this.stackPointer = stackPointer;
	}

	/**
	 * @return the programCounter
	 */
	public int getProgramCounter()
	{
		return programCounter;
	}

	/**
	 * @param programCounter
	 *            the programCounter to set
	 */
	public void setProgramCounter(int programCounter)
	{
		this.programCounter = programCounter;
	}

	/**
	 * increments program counter by 1
	 */
	public void incrementProgramCounter()
	{
		this.setProgramCounter(++programCounter);
	}

	public int[] getMemory()
	{
		return memory;
	}

	public void setMemory(int[] memory)
	{
		this.memory = memory;
	}

	public PPUSprRamIORegister getPpuSprRamIORegister()
    {
    	return ppuSprRamIORegister;
    }

	public void setPpuSprRamIORegister(PPUSprRamIORegister ppuSprRamIORegister)
    {
    	this.ppuSprRamIORegister = ppuSprRamIORegister;
    }

	public PPUControlRegister getPpuControlRegister()
    {
    	return ppuControlRegister;
    }

	public void setPpuControlRegister(PPUControlRegister ppuControlRegister)
    {
    	this.ppuControlRegister = ppuControlRegister;
    }

	public PPUStatusRegister getPpuStatusRegister()
    {
    	return ppuStatusRegister;
    }

	public void setPpuStatusRegister(PPUStatusRegister ppuStatusRegister)
    {
    	this.ppuStatusRegister = ppuStatusRegister;
    }

	public PPUVramAddressRegister getPpuVramAddressRegister()
    {
    	return ppuVramAddressRegister;
    }

	public void setPpuVramAddressRegister(
            PPUVramAddressRegister ppuVramAddressRegister)
    {
    	this.ppuVramAddressRegister = ppuVramAddressRegister;
    }

	public PPUVramIORegister getPpuVramIORegister()
    {
    	return ppuVramIORegister;
    }

	public void setPpuVramIORegister(PPUVramIORegister ppuVramIORegister)
    {
    	this.ppuVramIORegister = ppuVramIORegister;
    }

	public PPUScrollRegister getPpuScrollRegister()
    {
    	return ppuScrollRegister;
    }

	public void setPpuScrollRegister(PPUScrollRegister ppuScrollRegister)
    {
    	this.ppuScrollRegister = ppuScrollRegister;
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

	public PPUSpriteDMARegister getPpuSpriteDmaRegister()
    {
    	return ppuSpriteDmaRegister;
    }

	public void setPpuSpriteDmaRegister(PPUSpriteDMARegister ppuSpriteDmaRegister)
    {
    	this.ppuSpriteDmaRegister = ppuSpriteDmaRegister;
    }

	public PPUMaskRegister getPpuMaskRegister()
    {
    	return ppuMaskRegister;
    }

	public void setPpuMaskRegister(PPUMaskRegister ppuMaskRegister)
    {
    	this.ppuMaskRegister = ppuMaskRegister;
    }

	public APUControlRegister getApuControlRegister()
    {
    	return apuControlRegister;
    }

	public void setApuControlRegister(APUControlRegister apuControlRegister)
    {
    	this.apuControlRegister = apuControlRegister;
    }

	public APUFrameCounterRegister getApuFrameCounterRegister()
    {
    	return apuFrameCounterRegister;
    }

	public void setApuFrameCounterRegister(
            APUFrameCounterRegister apuFrameCounterRegister)
    {
    	this.apuFrameCounterRegister = apuFrameCounterRegister;
    }

	public APUPulse1ChannelRegister getApuPulse1ChannelRegister()
    {
    	return apuPulse1ChannelRegister;
    }

	public void setApuPulse1ChannelRegister(
            APUPulse1ChannelRegister apuPulse1ChannelRegister)
    {
    	this.apuPulse1ChannelRegister = apuPulse1ChannelRegister;
    }

	public APUPulse1LengthCounterRegister getApuPulse1LengthCounterRegister()
    {
    	return apuPulse1LengthCounterRegister;
    }

	public void setApuPulse1LengthCounterRegister(
            APUPulse1LengthCounterRegister apuPulse1LengthCounterRegister)
    {
    	this.apuPulse1LengthCounterRegister = apuPulse1LengthCounterRegister;
    }

	public APUPulse1SweepRegister getApuPulse1SweepRegister()
    {
    	return apuPulse1SweepRegister;
    }

	public void setApuPulse1SweepRegister(
            APUPulse1SweepRegister apuPulse1SweepRegister)
    {
    	this.apuPulse1SweepRegister = apuPulse1SweepRegister;
    }

	public APUPulse1TimerLowRegister getApuPulse1TimerLowRegister()
    {
    	return apuPulse1TimerLowRegister;
    }

	public void setApuPulse1TimerLowRegister(
            APUPulse1TimerLowRegister apuPulse1TimerLowRegister)
    {
    	this.apuPulse1TimerLowRegister = apuPulse1TimerLowRegister;
    }

	public ControlRegister1 getControlRegister1()
    {
    	return controlRegister1;
    }

	public void setControlRegister1(ControlRegister1 controlRegister1)
    {
    	this.controlRegister1 = controlRegister1;
    }

	public ControlRegister2 getControlRegister2()
    {
    	return controlRegister2;
    }

	public void setControlRegister2(ControlRegister2 controlRegister2)
    {
    	this.controlRegister2 = controlRegister2;
    }

	public Mapper getMapper()
    {
    	return mapper;
    }

	public void setMapper(Mapper mapper)
    {
    	this.mapper = mapper;
    }
}
