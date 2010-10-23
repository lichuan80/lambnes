/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lambelly.lambnes.platform.cpu;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.*;

import com.lambelly.lambnes.platform.Platform;

/**
 * 
 * @author thomasmccarthy
 */
public class NesCpuMemory
{
	/*
	 * cpu memory map 
	 * $0000–$00FF 256 bytes Zero Page — Special Zero Page addressing modes give faster memory read/write access $0100–$01FF 256
	 * bytes Stack memory $0200–$07FF 1536 bytes RAM $0800–$0FFF 2048 bytes
	 * Mirror of $0000–$07FF $0800–$08FF Zero Page $0900–$09FF Stack
	 * $0A00–$0FFF RAM $1000–$17FF 2048 bytes Mirror of $0000–$07FF
	 * $1000–$10FF Zero Page $1100–$11FF Stack $1200–$17FF RAM
	 * $1800–$1FFF 2048 bytes Mirror of $0000–$07FF $1800–$18FF Zero Page
	 * $1900–$19FF Stack $1A00–$1FFF RAM $2000–$2007 8 bytes Input/Output
	 * registers 
	 * $2008–$3FFF 8184 bytes Mirror of $2000–$2007 (multiple
	 * times) $4000–$401F 32 bytes Input/Output registers $4020–$5FFF 8160
	 * bytes Expansion ROM — Used with Nintendo's MMC5 to expand the
	 * capabilities of VRAM. $6000–$7FFF 8192 bytes SRAM — Save Ram used to
	 * save data between game plays. $8000 –$BFFF 16384 bytes PRG-ROM lower
	 * bank — executable code $C000–$FFFF 16384 bytes PRG-ROM upper bank —
	 * executable code $FFFA–$FFFB 2 bytes Address of Non Maskable Interrupt
	 * (NMI) handler routine $FFFC–$FFFD 2 bytes Address of Power on reset
	 * handler routine $FFFE–$FFFF 2 bytes Address of Break (BRK instruction)
	 * handler routine
	 */

	private int stackPointer = -1;
	private int programCounter = 0x8000; // starts at start of lower bank

	private int[] zeroPage = new int[256]; // $0000-$00FF
	private int[] stackMemory = new int[256]; // $0100-$01FF
	private int[] ram = new int[1536]; // $0200-$07FF
	private int[] inputOutput1 = new int[8]; // $2000-$2007
	private int[] inputOutput2 = new int[32]; // $4000-$401F
	private int[] expansionRam = new int[8160]; // $4020-$5FFF
	private int[] sram = new int[8192]; // $6000-$7FFF
	private int[] prgRomLowerBank = new int[16384]; // $8000-$BFFF
	private int[] prgRomUpperBank = new int[16384]; // $C000-$FFFF

    public static final int STACK_BASE = 0x0100;
    public static final int STACK_MAX = 0x01FF;
    public static final int PRG_ROM_BASE = 0x8000;
    public static final int NMI_VECTOR = 0xFFFA;
    public static final int RESET_VECTOR = 0xFFFC;
    public static final int IRQBRK_Vector = 0xFFFE;
	
	private Logger logger = Logger.getLogger(NesCpuMemory.class);

	public NesCpuMemory()
	{

	}

	public NesCpuMemory(int[] programInstructions)
	{
		this.setProgramInstructions(programInstructions);
	}

	public int getPrgRomByte(int address)
	{
		if (address > 16384)
		{
			// upper
			return this.getPrgRomUpperBank()[address];
		}
		else
		{
			// lower
			return this.getPrgRomLowerBank()[address];
		}
	}

	public int getNextPrgRomByte()
	{
		int b = this.getNextPrgRom();

		return b;
	}

	public int getNextPrgRomShort()
	{
		logger.debug("program counter: 0x" + Integer.toHexString(this.getProgramCounter()));
		String b1 = Integer.toString(this.getNextPrgRom(), 16);
		b1 = String.format("%02x", Integer.parseInt(b1,16));
		logger.debug("program counter: 0x" + Integer.toHexString(this.getProgramCounter()));
		String b2 = Integer.toString(this.getNextPrgRom(), 16);

		
		logger.debug("b1:" + b1);
		logger.debug("b2:" + b2);

		return Integer.parseInt(b2 + b1,16);
	}

	private int getNextPrgRom()
	{
		int b = 0;

		if (this.getProgramCounter() > 16384)
		{
			// upper bank
			b = this.getMemoryFromHexAddress(this.getProgramCounter());
		}
		else
		{
			// lower bank
			b = this.getMemoryFromHexAddress(this.getProgramCounter());
		}

		// increment counter
		this.incrementProgramCounter();

		// return b
		return b;
	}

	/**
	 * getImmediate - #aa returns the next value in program memory to be used by
	 * the instruction
	 * 
	 * @return
	 */
	public int getImmediateValue()
	{
		return this.getNextPrgRomByte();
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
		logger.debug("using address: " + Integer.toHexString(address));
		return this.getMemoryFromHexAddress(address);
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
		return this.getMemoryFromHexAddress(address);
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
		return this.getMemoryFromHexAddress(address);
	}	
	
	/**
	 * getZeroPageValue - aa - 8 bit address returns value from zero page
	 * 
	 * @return
	 */
	public int getZeroPageValue()
	{
		int address = this.getZeroPageAddress();
		logger.debug(address);
		return this.getMemoryFromHexAddress(address);
	}
	
	/**
	 * getIndirectAbsoluteValue() | (aaaa) | aa = 2 hex digits private int
	 * 
	 */
	public int getIndirectAbsoluteValue() 
	{
		int address = this.getIndirectAbsoluteAddress();
		String b1 = Integer.toString(this.getMemoryFromHexAddress(address), 16);
		b1 = String.format("%02x", Integer.parseInt(b1,16));
		String b2 = Integer.toString(this.getMemoryFromHexAddress(++address), 16);
		return Integer.parseInt(b2 + b1,16);
	}

	/**
	 * getZeroPageIndexedXValue - aa,x - 8 bit address returns value from address + x register
	 * 
	 * @return
	 */
	public int getZeroPageIndexedXValue() 
	{
		int address = this.getZeroPageIndexedXAddress();
		return this.getMemoryFromHexAddress(address);
	}
	
	/**
	 * getZeroPageIndexedYValue() | aa,Y | digits as private int
	 */
	public int getZeroPageIndexedYValue()
	{
		int address = this.getZeroPageIndexedYAddress();
		return this.getMemoryFromHexAddress(address);
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
		return this.getMemoryFromHexAddress(address);
	}
	
	 /**
	 * getIndirectIndexedYValue() - (aa),Y 
	 * gets the value of a 16 bit address from zero page based on 8 bit address, adds Y, then gets the value the 16 bit address points at 
	 * @return 
	 */
	public int getIndirectIndexedYValue()
	{
		int address = this.getIndirectIndexedYAddress();
		return this.getMemoryFromHexAddress(address);
	}
	
	public int getAbsoluteAddress()
	{
		int address = this.getNextPrgRomShort();
		return address;
	}
	
	public int getIndirectAbsoluteAddress()
	{
		int address = this.getNextPrgRomShort();
		return address;
	}
	
	public int getAbsoluteIndexedXAddress()
	{
		int address = this.getNextPrgRomShort();
		address += Platform.getCpu().getX();
		return address;
	}
	
	public int getAbsoluteIndexedYAddress()
	{
		int address = this.getNextPrgRomShort();
		logger.debug("Y: " + Integer.toHexString(Platform.getCpu().getY()));
		logger.debug("Address: " + Integer.toHexString(address));
		address += Platform.getCpu().getY();

		return address;
	}
	
	public int getZeroPageAddress()
	{
		return this.getNextPrgRomByte();
	}
	
	public int getZeroPageIndexedXAddress()
	{
		// get initial address
		int address = this.getNextPrgRomByte();
		logger.debug("initial address: " + Integer.toHexString(address));
		
		// add address to x register
		address += Platform.getCpu().getX();
		logger.debug("X: " + Integer.toHexString(Platform.getCpu().getX()));
		logger.debug("address: " + Integer.toHexString(address));
		// if address is > FF it wraps around.
		address = address & Platform.EIGHT_BIT_MASK;
		logger.debug("address: " + Integer.toHexString(address));
		return address;
	}
	
	public int getZeroPageIndexedYAddress()
	{
		// get initial address
		int address = this.getNextPrgRomByte();
		logger.debug("initial address: " + Integer.toHexString(address));
		
		// add address to Y register
		address += Platform.getCpu().getY();
		logger.debug("Y: " + Integer.toHexString(Platform.getCpu().getY()));
		logger.debug("address: " + Integer.toHexString(address));
		// if address is > FF it wraps around.
		address = address & Platform.EIGHT_BIT_MASK;
		logger.debug("address: " + Integer.toHexString(address));
		return address;
	}	
	
	public int getIndexedIndirectXAddress()
	{
		int zeroPageAddress = Platform.getCpuMemory().getNextPrgRomByte();
		zeroPageAddress += Platform.getCpu().getX();
		
		// zero page wrap around
		zeroPageAddress = zeroPageAddress & Platform.EIGHT_BIT_MASK;
		
		String b1 = Integer.toString(this.getMemoryFromHexAddress(zeroPageAddress), 16);
		b1 = String.format("%02x", Integer.parseInt(b1,16));
		String b2 = Integer.toString(this.getMemoryFromHexAddress(++zeroPageAddress), 16);
		
		return Integer.parseInt(b2 + b1,16);
	}
	
	public int getIndirectIndexedYAddress()
	{
		int zeroPageAddress = this.getNextPrgRomByte();
		String b1 = Integer.toString(this.getMemoryFromHexAddress(zeroPageAddress), 16);
		b1 = String.format("%02x", Integer.parseInt(b1,16));
		String b2 = Integer.toString(this.getMemoryFromHexAddress(++zeroPageAddress), 16);
		
		int finalAddress = Integer.parseInt((b2 + b1),16);
		finalAddress += Platform.getCpu().getY();
		return finalAddress;
	}
	
	public int getRelativeAddress()
	{
		// treat offset as 8-bit signed
		byte offset = (byte)this.getNextPrgRomByte();
		
		logger.debug("prog counter: 0x" + Integer.toHexString(this.getProgramCounter()));
		logger.debug("offset: " + offset);
		return this.getProgramCounter() + offset;
	}
	
	public int getMemoryFromHexAddress(int address) throws IllegalStateException
	{
		logger.debug("getting memory from address: 0x" + Integer.toHexString(address));
		if (address >= 0x0000 && address <= 0x00FF)
		{
			// Zero Page
			return this.getZeroPage()[address];
		}
		else if (address >= 0x0100 && address <= 0x01FF)
		{
			// Stack memory
			int arrayIndex = address - 0x0100;
			return this.getStackMemory()[arrayIndex];
		}
		else if (address >= 0x0200 && address <= 0x07FF)
		{
			// RAM
			int arrayIndex = address - 0x0200;
			return this.getRam()[arrayIndex];
		}
		else if (address >= 0x0800 && address <= 0x08FF)
		{
			// Zero Page
			int arrayIndex = address - 0x0800;
			return this.getZeroPage()[arrayIndex];
		}
		else if (address >= 0x0900 && address <= 0x09FF)
		{
			// Stack
			int arrayIndex = address - 0x0900;
			return this.getStackMemory()[arrayIndex];
		}
		else if (address >= 0x0A00 && address <= 0x0FFF)
		{
			// RAM
			int arrayIndex = address - 0x0A00;
			return this.getRam()[arrayIndex];
		}
		else if (address >= 0x1000 && address <= 0x10FF)
		{
			// Zero Page
			int arrayIndex = address - 0x1000;
			return this.getZeroPage()[arrayIndex];
		}
		else if (address >= 0x1100 && address <= 0x11FF)
		{
			// Stack
			int arrayIndex = address - 0x1100;
			return this.getStackMemory()[arrayIndex];
		}
		else if (address >= 0x1200 && address <= 0x17FF)
		{
			// RAM
			int arrayIndex = address - 0x1200;
			return this.getRam()[arrayIndex];
		}
		else if (address >= 0x1800 && address <= 0x18FF)
		{
			// Zero Page
			int arrayIndex = address - 0x1800;
			return this.getZeroPage()[arrayIndex];
		}
		else if (address >= 0x1900 && address <= 0x19FF)
		{
			// Stack
			int arrayIndex = address - 0x1900;
			return this.getStackMemory()[arrayIndex];
		}
		else if (address >= 0x1A00 && address <= 0x1FFF)
		{
			// RAM
			int arrayIndex = address - 0x1A00;
			return this.getRam()[arrayIndex];
		}
		else if (address >= 0x2000 && address <= 0x2007)
		{
			// Input/Output registers
			int arrayIndex = address - 0x2000;
			return this.getInputOutput1()[arrayIndex];
		}
		else if (address >= 0x2008 && address <= 0x3FFF)
		{
			// (mirror of $2000 - $2007 multiple times)
			int arrayIndex = address % 8;
			return this.getInputOutput1()[arrayIndex];
		}
		else if (address >= 0x4000 && address <= 0x401F)
		{
			// Input/Output registers
			int arrayIndex = address - 0x4000;
			return this.getInputOutput2()[arrayIndex];
		}
		else if (address >= 0x4020 && address <= 0x5FFF)
		{
			// Expansion ROM
			int arrayIndex = address - 0x4020;
			return this.getExpansionRam()[arrayIndex];
		}
		else if (address >= 0x6000 && address <= 0x7FFF)
		{
			// SRAM
			int arrayIndex = address - 0x6000;
			return this.getSram()[arrayIndex];
		}
		else if (address >= 0x8000 && address <= 0xBFFF)
		{
			// PRG-ROM lower bank
			int arrayIndex = address - 0x8000;
			return this.getPrgRomLowerBank()[arrayIndex];
		}
		else if (address >= 0xC000 && address <= 0xFFFF)
		{
			// PRG-ROM upper bank
			int arrayIndex = address - 0xC000;
			return this.getPrgRomUpperBank()[arrayIndex];
		}
		else
		{
			throw new IllegalStateException("tried to access memory location " + Integer.toHexString(address));
		}
	}

	public void setMemoryFromHexAddress(int address, int value) throws IllegalStateException
	{
		logger.debug("setting memory to address: 0x" + Integer.toHexString(address) + ": 0x" + Integer.toHexString(value));
		if (address >= 0x0000 && address <= 0x00FF)
		{
			// Zero Page
		    this.getZeroPage()[address] = value;
		}
		else if (address >= 0x0100 && address <= 0x01FF)
		{
			// Stack memory
			int arrayIndex = address - 0x0100;
			this.getStackMemory()[arrayIndex] = value;
		}
		else if (address >= 0x0200 && address <= 0x07FF)
		{
			// RAM
			int arrayIndex = address - 0x0200;
			this.getRam()[arrayIndex] = value;
		}
		else if (address >= 0x0800 && address <= 0x08FF)
		{
			// Zero Page
			int arrayIndex = address - 0x0800;
			this.getZeroPage()[arrayIndex] = value;
		}
		else if (address >= 0x0900 && address <= 0x09FF)
		{
			// Stack
			int arrayIndex = address - 0x0900;
			this.getStackMemory()[arrayIndex] = value;
		}
		else if (address >= 0x0A00 && address <= 0x0FFF)
		{
			// RAM
			int arrayIndex = address - 0x0A00;
			this.getRam()[arrayIndex] = value;
		}
		else if (address >= 0x1000 && address <= 0x10FF)
		{
			// Zero Page
			int arrayIndex = address - 0x1000;
			this.getZeroPage()[arrayIndex] = value;
		}
		else if (address >= 0x1100 && address <= 0x11FF)
		{
			// Stack
			int arrayIndex = address - 0x1100;
			this.getStackMemory()[arrayIndex] = value;
		}
		else if (address >= 0x1200 && address <= 0x17FF)
		{
			// RAM
			int arrayIndex = address - 0x1200;
			this.getRam()[arrayIndex] = value;
		}
		else if (address >= 0x1800 && address <= 0x18FF)
		{
			// Zero Page
			int arrayIndex = address - 0x1800;
			this.getZeroPage()[arrayIndex] = value;
		}
		else if (address >= 0x1900 && address <= 0x19FF)
		{
			// Stack
			int arrayIndex = address - 0x1900;
			this.getStackMemory()[arrayIndex] = value;
		}
		else if (address >= 0x1A00 && address <= 0x1FFF)
		{
			// RAM
			int arrayIndex = address - 0x1A00;
			this.getRam()[arrayIndex] = value;
		}
		else if (address >= 0x2000 && address <= 0x2007)
		{
			// Input/Output registers
			int arrayIndex = address - 0x2000;
			this.getInputOutput1()[arrayIndex] = value;
		}
		else if (address >= 0x2008 && address <= 0x3FFF)
		{
			// (mirror of $2000 - $2007 multiple times)
			int arrayIndex = address % 8;
			this.getInputOutput1()[arrayIndex] = value;
		}
		else if (address >= 0x4000 && address <= 0x401F)
		{
			// Input/Output registers
			int arrayIndex = address - 0x4000;
			this.getInputOutput2()[arrayIndex] = value;
		}
		else if (address >= 0x4020 && address <= 0x5FFF)
		{
			// Expansion ROM
			int arrayIndex = address - 0x4020;
			this.getExpansionRam()[arrayIndex] = value;
		}
		else if (address >= 0x6000 && address <= 0x7FFF)
		{
			// SRAM
			int arrayIndex = address - 0x6000;
			this.getSram()[arrayIndex] = value;
		}
		else if (address >= this.PRG_ROM_BASE && address <= 0xBFFF)
		{
			// PRG-ROM lower bank
			int arrayIndex = address - this.PRG_ROM_BASE;
			this.getPrgRomLowerBank()[arrayIndex] = value;
		}
		else if (address >= 0xC000 && address <= 0xFFFF)
		{
			// PRG-ROM upper bank
			int arrayIndex = address - 0xC000;
			this.getPrgRomUpperBank()[arrayIndex] = value;
		}
		else
		{
			throw new IllegalStateException("tried to access memory location " + Integer.toHexString(address));
		}
	}	
	
	public void setProgramInstructions(int[] programInstructions)
	{
		if (programInstructions.length > 16384)
		{
			// split in half
			int[] lower = ArrayUtils.subarray(programInstructions, 0, 16384);
			int[] upper = ArrayUtils
					.subarray(programInstructions, 16384, 32769);

			 this.setPrgRomLowerBank(lower);
			 this.setPrgRomUpperBank(upper);
		}
	}

	public void resetCounters()
	{
		this.setProgramCounter(this.PRG_ROM_BASE);
		this.setStackPointer(-1);
	}
	
	public void pushStack(int value)
	{
		logger.debug("stack pointer: " + this.getStackPointer());
		logger.debug(this.STACK_BASE);
		if (this.getStackPointer() >= this.STACK_BASE)
		{
			logger.debug("incrementing stack pointer");
			// increment pointer
			this.setStackPointer(this.getStackPointer() + 1);
		}
		else if (this.getStackPointer() < 0)
		{
			logger.debug("first push on stack");
			this.setStackPointer(this.STACK_BASE);
		}
		else if (this.getStackPointer() == this.STACK_MAX)
		{
			logger.error("STACK POINTER ABOVE MAX");
		}
		this.setMemoryFromHexAddress(this.getStackPointer(), value);
	}
	
	public int popStack()
	{
		// obtain return value
		int value = this.getMemoryFromHexAddress(this.getStackPointer());
		
		// decrement
		if (this.getStackPointer() > this.STACK_BASE)
		{
			this.setStackPointer(this.getStackPointer() - 1);
		}
		
		return value;
	}
	
	/**
	 * @return the prgRomLowerBank
	 */
	public int[] getPrgRomLowerBank()
	{
		return prgRomLowerBank;
	}

	/**
	 * @return the prgRomUpperBank
	 */
	public int[] getPrgRomUpperBank()
	{
		return prgRomUpperBank;
	}

	public void setPrgRomLowerBank(int[] prgRomLowerBank)
	{
		this.prgRomLowerBank = prgRomLowerBank;
	}

	public void setPrgRomUpperBank(int[] prgRomUpperBank)
	{
		this.prgRomUpperBank = prgRomUpperBank;
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

	/**
	 * @return the zeroPage
	 */
	public int[] getZeroPage()
	{
		return zeroPage;
	}

	/**
	 * @param zeroPage
	 *            the zeroPage to set
	 */
	public void setZeroPage(int[] zeroPage)
	{
		this.zeroPage = zeroPage;
	}

	/**
	 * @return the stackMemory
	 */
	public int[] getStackMemory()
	{
		return stackMemory;
	}

	/**
	 * @param stackMemory
	 *            the stackMemory to set
	 */
	public void setStackMemory(int[] stackMemory)
	{
		this.stackMemory = stackMemory;
	}

	/**
	 * @return the ram
	 */
	public int[] getRam()
	{
		return ram;
	}

	/**
	 * @param ram
	 *            the ram to set
	 */
	public void setRam(int[] ram)
	{
		this.ram = ram;
	}

	/**
	 * @return the inputOutput1
	 */
	public int[] getInputOutput1()
	{
		return inputOutput1;
	}

	/**
	 * @param inputOutput1
	 *            the inputOutput1 to set
	 */
	public void setInputOutput1(int[] inputOutput1)
	{
		this.inputOutput1 = inputOutput1;
	}

	/**
	 * @return the intputOutput2
	 */
	public int[] getInputOutput2()
	{
		return inputOutput2;
	}

	/**
	 * @param intputOutput2
	 *            the intputOutput2 to set
	 */
	public void setInputOutput2(int[] inputOutput2)
	{
		this.inputOutput2 = inputOutput2;
	}

	/**
	 * @return the expansionRam
	 */
	public int[] getExpansionRam()
	{
		return expansionRam;
	}

	/**
	 * @param expansionRam
	 *            the expansionRam to set
	 */
	public void setExpansionRam(int[] expansionRam)
	{
		this.expansionRam = expansionRam;
	}

	/**
	 * @return the sram
	 */
	public int[] getSram()
	{
		return sram;
	}

	/**
	 * @param sram
	 *            the sram to set
	 */
	public void setSram(int[] sram)
	{
		this.sram = sram;
	}
}
