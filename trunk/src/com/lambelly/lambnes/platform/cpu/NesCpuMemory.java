/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lambelly.lambnes.platform.cpu;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.*;

import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.platform.controllers.ControlRegister1;
import com.lambelly.lambnes.platform.controllers.ControlRegister2;
import com.lambelly.lambnes.platform.controllers.NesJoypad;
import com.lambelly.lambnes.platform.ppu.registers.PPUControlRegister1;
import com.lambelly.lambnes.platform.ppu.registers.PPUControlRegister2;
import com.lambelly.lambnes.platform.ppu.registers.PPUSprRamAddressRegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUSprRamIORegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUSpriteDMARegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUStatusRegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUVramAddressRegister1;
import com.lambelly.lambnes.platform.ppu.registers.PPUVramAddressRegister2;
import com.lambelly.lambnes.platform.ppu.registers.PPUVramIORegister;
import com.lambelly.lambnes.util.BitUtils;
import com.lambelly.lambnes.util.NumberConversionUtils;

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

	private int stackPointer = 0;
	private int programCounter = 0x8000; // starts at start of lower bank

	private int[] zeroPage = new int[256]; // $0000-$00FF
	private int[] stackMemory = new int[256]; // $0100-$01FF
	private int[] ram = new int[1536]; // $0200-$07FF
	private PPUControlRegister1 ppuControlRegister1 = PPUControlRegister1.getRegister(); // 2000
	private PPUControlRegister2 ppuControlRegister2 = PPUControlRegister2.getRegister(); // 2001
	private PPUStatusRegister ppuStatusRegister = PPUStatusRegister.getRegister(); //2002
	private PPUSprRamAddressRegister ppuSprRamAddressRegister = PPUSprRamAddressRegister.getRegister(); // 2003
	private PPUSprRamIORegister ppuSprRamIORegister = PPUSprRamIORegister.getRegister(); // 2004
	private PPUVramAddressRegister1 ppuVramAddressRegister1 = PPUVramAddressRegister1.getRegister(); // 2005
	private PPUVramAddressRegister2 ppuVramAddressRegister2 = PPUVramAddressRegister2.getRegister(); // 2006
	private PPUVramIORegister ppuVramIORegister = PPUVramIORegister.getRegister(); // 2007
	private PPUSpriteDMARegister ppuDMARegister = PPUSpriteDMARegister.getRegister(); // $4014
	private ControlRegister1 joypadControlRegister1 = ControlRegister1.getRegister(); // $4016
	private ControlRegister2 joypadControlRegister2 = ControlRegister2.getRegister(); // $4017
	private Integer[] inputOutput2 = new Integer[32]; // $4000-$401F
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
		int lowerBit = this.getNextPrgRom();
		int higherBit = this.getNextPrgRom();
		return BitUtils.unsplitAddress(higherBit, lowerBit);
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
		if(logger.isDebugEnabled())
		{
			logger.debug("using address: " + Integer.toHexString(address));
		}
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
		if(logger.isDebugEnabled())
		{
			logger.debug(address);
		}
		return this.getMemoryFromHexAddress(address);
	}
	
	/**
	 * getIndirectAbsoluteValue() | (aaaa) | aa = 2 hex digits private int
	 * 
	 */
	public int getIndirectAbsoluteValue() 
	{
		int address = this.getIndirectAbsoluteAddress();
		return BitUtils.unsplitAddress(this.getMemoryFromHexAddress(address), this.getMemoryFromHexAddress(++address));
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
		if(logger.isDebugEnabled())
		{
			logger.debug("initial address: " + address);
			logger.debug("X: " + Platform.getCpu().getX());
		}
		address += Platform.getCpu().getX();
		return address;
	}
	
	public int getAbsoluteIndexedYAddress()
	{
		int address = this.getNextPrgRomShort();
		if(logger.isDebugEnabled())
		{
			logger.debug("Y: " + Integer.toHexString(Platform.getCpu().getY()));
			logger.debug("Address: " + Integer.toHexString(address));
		}
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
		if(logger.isDebugEnabled())
		{
			logger.debug("initial address: " + Integer.toHexString(address));
		}
		
		// add address to x register
		address += Platform.getCpu().getX();
		if(logger.isDebugEnabled())
		{
			logger.debug("X: " + Integer.toHexString(Platform.getCpu().getX()));
			logger.debug("address: " + Integer.toHexString(address));
		}
		// if address is > FF it wraps around.
		address = address & Platform.EIGHT_BIT_MASK;
		if(logger.isDebugEnabled())
		{
			logger.debug("address: " + Integer.toHexString(address));
		}
		return address;
	}
	
	public int getZeroPageIndexedYAddress()
	{
		// get initial address
		int address = this.getNextPrgRomByte();
		if(logger.isDebugEnabled())
		{
			logger.debug("initial address: " + Integer.toHexString(address));
		}
		
		// add address to Y register
		address += Platform.getCpu().getY();
		if(logger.isDebugEnabled())
		{
			logger.debug("Y: " + Integer.toHexString(Platform.getCpu().getY()));
			logger.debug("address: " + Integer.toHexString(address));
		}
		// if address is > FF it wraps around.
		address = address & Platform.EIGHT_BIT_MASK;
		if(logger.isDebugEnabled())
		{
			logger.debug("address: " + Integer.toHexString(address));
		}
		return address;
	}	
	
	public int getIndexedIndirectXAddress()
	{ 
		int lowByteAddress = Platform.getCpuMemory().getNextPrgRomByte() + Platform.getCpu().getX();
		// zero page wrap around
		lowByteAddress = lowByteAddress & Platform.EIGHT_BIT_MASK;
		
		int highByteAddress =  lowByteAddress + 1;
		
		
		return BitUtils.unsplitAddress(this.getMemoryFromHexAddress(highByteAddress), this.getMemoryFromHexAddress(lowByteAddress));
	}
	
	public int getIndirectIndexedYAddress()
	{
		int lowByteAddress = this.getNextPrgRomByte();
		int highByteAddress =  lowByteAddress + 1;
		int finalAddress = BitUtils.unsplitAddress(this.getMemoryFromHexAddress(highByteAddress), this.getMemoryFromHexAddress(lowByteAddress));
		finalAddress += Platform.getCpu().getY();
		return finalAddress;
	}
	
	public int getRelativeAddress()
	{
		// treat offset as 8-bit signed
		byte offset = (byte)this.getNextPrgRomByte();
		
		if(logger.isDebugEnabled())
		{
			logger.debug("prog counter: 0x" + Integer.toHexString(this.getProgramCounter()));
			logger.debug("offset: " + offset);
		}
		return this.getProgramCounter() + offset;
	}
	
	public int getMemoryFromHexAddress(int address) throws IllegalStateException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("getting memory from address: 0x" + Integer.toHexString(address));
		}
		int value = 0;
		if (address >= 0x0000 && address <= 0x00FF)
		{
			// Zero Page
			value = this.getZeroPage()[address];
		}
		else if (address >= 0x0100 && address <= 0x01FF)
		{
			// Stack memory
			int arrayIndex = address - 0x0100;
			value = this.getStackMemory()[arrayIndex];
		}
		else if (address >= 0x0200 && address <= 0x07FF)
		{
			// RAM
			int arrayIndex = address - 0x0200;
			value = this.getRam()[arrayIndex];
		}
		else if (address >= 0x0800 && address <= 0x08FF)
		{
			// Zero Page
			int arrayIndex = address - 0x0800;
			value = this.getZeroPage()[arrayIndex];
		}
		else if (address >= 0x0900 && address <= 0x09FF)
		{
			// Stack
			int arrayIndex = address - 0x0900;
			value = this.getStackMemory()[arrayIndex];
		}
		else if (address >= 0x0A00 && address <= 0x0FFF)
		{
			// RAM
			int arrayIndex = address - 0x0A00;
			value = this.getRam()[arrayIndex];
		}
		else if (address >= 0x1000 && address <= 0x10FF)
		{
			// Zero Page
			int arrayIndex = address - 0x1000;
			value = this.getZeroPage()[arrayIndex];
		}
		else if (address >= 0x1100 && address <= 0x11FF)
		{
			// Stack
			int arrayIndex = address - 0x1100;
			value = this.getStackMemory()[arrayIndex];
		}
		else if (address >= 0x1200 && address <= 0x17FF)
		{
			// RAM
			int arrayIndex = address - 0x1200;
			value = this.getRam()[arrayIndex];
		}
		else if (address >= 0x1800 && address <= 0x18FF)
		{
			// Zero Page
			int arrayIndex = address - 0x1800;
			value = this.getZeroPage()[arrayIndex];
		}
		else if (address >= 0x1900 && address <= 0x19FF)
		{
			// Stack
			int arrayIndex = address - 0x1900;
			value = this.getStackMemory()[arrayIndex];
		}
		else if (address >= 0x1A00 && address <= 0x1FFF)
		{
			// RAM
			int arrayIndex = address - 0x1A00;
			value = this.getRam()[arrayIndex];
		}
		else if (address >= 0x2000 && address <= 0x2007)
		{
			// Input/Output registers
			if (address == PPUControlRegister1.REGISTER_ADDRESS)
			{
				throw new IllegalStateException("reading from write only register");
			}
			else if (address == PPUControlRegister2.REGISTER_ADDRESS)
			{
				throw new IllegalStateException("reading from write only register");
			}
			else if (address == PPUSprRamAddressRegister.REGISTER_ADDRESS)
			{
				throw new IllegalStateException("reading from write only register");
			}
			else if (address == PPUSprRamIORegister.REGISTER_ADDRESS)
			{
				throw new IllegalStateException("reading from write only register");
			}
			else if (address == PPUStatusRegister.REGISTER_ADDRESS)
			{
				value = this.getPpuStatusRegister().getRegisterValue();
			}
			else if (address == PPUVramAddressRegister2.REGISTER_ADDRESS)
			{
				throw new IllegalStateException("reading from write only register");
			}
			else if (address == PPUVramIORegister.REGISTER_ADDRESS)
			{
				value = this.getPpuVramIORegister().getRegisterValue();
			}
			
			logger.debug("getting memory from control register 0x" + Integer.toHexString(address) + ": " + Integer.toHexString(value));
		}
		else if (address >= 0x2008 && address <= 0x3FFF)
		{
			// (mirror of $2000 - $2007 multiple times)
			int index = address % 8;
			this.getMemoryFromHexAddress(0x2000 + index);
		}
		else if (address >= 0x4000 && address <= 0x401F)
		{
			// Input/Output registers
			int arrayIndex = address - 0x4000;
			Integer i = this.getInputOutput2()[arrayIndex];
			
			if (i == null)
			{
				value = 0;
			}
			else
			{
				value = i;
			}
		}
		else if (address >= 0x4020 && address <= 0x5FFF)
		{
			// Expansion ROM
			int arrayIndex = address - 0x4020;
			value = this.getExpansionRam()[arrayIndex];
		}
		else if (address >= 0x6000 && address <= 0x7FFF)
		{
			// SRAM
			int arrayIndex = address - 0x6000;
			value = this.getSram()[arrayIndex];
		}
		else if (address >= 0x8000 && address <= 0xBFFF)
		{
			// PRG-ROM lower bank
			int arrayIndex = address - 0x8000;
			value = this.getPrgRomLowerBank()[arrayIndex];
		}
		else if (address >= 0xC000 && address <= 0xFFFF)
		{
			// PRG-ROM upper bank
			int arrayIndex = address - 0xC000;
			value = this.getPrgRomUpperBank()[arrayIndex];
		}
		else
		{
			throw new IllegalStateException("tried to access memory address 0x" + Integer.toHexString(address) + " which is not mapped to any data structure");
		}
		
		return value;
	}

	public void setMemoryFromHexAddress(int address, int value) throws IllegalStateException
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("setting memory to address: 0x" + Integer.toHexString(address) + ": 0x" + Integer.toHexString(value));
		}
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
			// Input/Output registers
			if (address == PPUControlRegister1.REGISTER_ADDRESS)
			{
				this.getPpuControlRegister1().setRegisterValue(value);
			}
			else if (address == PPUControlRegister2.REGISTER_ADDRESS)
			{
				this.getPpuControlRegister2().setRegisterValue(value);
			}
			else if (address == PPUSprRamAddressRegister.REGISTER_ADDRESS)
			{
				this.getPpuSprRamAddressRegister().setRegisterValue(value);
			}
			else if (address == PPUSprRamIORegister.REGISTER_ADDRESS)
			{
				this.getPpuSprRamIORegister().setRegisterValue(value);
			}
			else if (address == PPUStatusRegister.REGISTER_ADDRESS)
			{
				this.getPpuStatusRegister().setRegisterValue(value);
			}
			else if (address == PPUVramAddressRegister1.REGISTER_ADDRESS)
			{
				this.getPpuVramAddressRegister1().setRegisterValue(value);
			}
			else if (address == PPUVramAddressRegister2.REGISTER_ADDRESS)
			{
				this.getPpuVramAddressRegister2().setRegisterValue(value);
			}
			else if (address == PPUVramIORegister.REGISTER_ADDRESS)
			{
				this.getPpuVramIORegister().setRegisterValue(value);
			}
			
			logger.debug("setting control register 0x" + Integer.toHexString(address) + " to " + Integer.toHexString(value));			
		}
		else if (address >= 0x2008 && address <= 0x3FFF)
		{
			// (mirror of $2000 - $2007 multiple times)
			int index = address % 8;
			this.setMemoryFromHexAddress(0x2000 + index,value);
		}
		else if (address >= 0x4000 && address <= 0x401F)
		{
			// Input/Output registers
			if (address == PPUSpriteDMARegister.REGISTER_ADDRESS)
			{
				this.getPpuDMARegister().setRegisterValue(value);
			}
			else if (address == ControlRegister1.REGISTER_ADDRESS)
			{
				this.getJoypadControlRegister1().setRegisterValue(value);
			}
			else if (address == ControlRegister2.REGISTER_ADDRESS)
			{
				this.getJoypadControlRegister2().setRegisterValue(value);
			}
			int arrayIndex = address - 0x4000;
			this.getInputOutput2()[arrayIndex] = value;
			logger.debug("setting control register 0x" + Integer.toHexString(address) + " to " + Integer.toHexString(value));
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
			throw new IllegalStateException("tried to access memory address 0x" + Integer.toHexString(address) + " which is not mapped to any data structure");
		}
	}	
	
	public void setProgramInstructions(int[] programInstructions)
	{
		if (programInstructions.length > 16384)
		{
			if(logger.isDebugEnabled())
			{
				logger.debug("program instructions length: " + programInstructions.length);
			}
			
			// split in half
			int[] lower = ArrayUtils.subarray(programInstructions, 0, 16384);
			int[] upper = ArrayUtils
					.subarray(programInstructions, 16384, 32769);

			 this.setPrgRomLowerBank(lower);
			 this.setPrgRomUpperBank(upper);
		}
		else if (programInstructions.length == 16384)
		{
			if(logger.isDebugEnabled())
			{
				logger.debug("mirroring prg rom");
			}
			// instructions are mirrored to fill the progrom
			this.setPrgRomLowerBank(programInstructions);
			this.setPrgRomUpperBank(programInstructions);
		}
	}

	public void resetCounters()
	{
		this.setProgramCounter(this.PRG_ROM_BASE);
		this.setStackPointer(0);
	}
	
	public void pushStack(int value)
	{
		if(logger.isDebugEnabled())
		{
			logger.debug("stack pointer: " + this.getStackPointer());
		}
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
		if(logger.isDebugEnabled())
		{
			logger.debug("stack pointer: " + stackPointer);
		}

		// roll over
		stackPointer = (stackPointer & Platform.EIGHT_BIT_MASK);
		if(logger.isDebugEnabled())
		{
			logger.debug("stack pointer: " + stackPointer);
		}
		
		// set stack pointer
		this.setStackPointer(stackPointer);
	}
	
	public int popStack()
	{
		int stackPointer = this.getStackPointer();
		
		// increment
		stackPointer = (++stackPointer);
		
		// roll over
		stackPointer = (stackPointer & Platform.EIGHT_BIT_MASK);
	
		// obtain return value
		int value = this.getMemoryFromHexAddress(Integer.parseInt("01" + NumberConversionUtils.generateHexStringWithleadingZeros(stackPointer, 2),16));

		// set stack pointer
		this.setStackPointer(stackPointer);
		
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
	 *   w         the ram to set
	 */
	public void setRam(int[] ram)
	{
		this.ram = ram;
	}

	/**
	 * @return the intputOutput2
	 */
	public Integer[] getInputOutput2()
	{
		return inputOutput2;
	}

	/**
	 * @param intputOutput2
	 *            the intputOutput2 to set
	 */
	public void setInputOutput2(Integer[] inputOutput2)
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

	public PPUVramIORegister getPpuVramIORegister()
	{
		return ppuVramIORegister;
	}

	public PPUControlRegister1 getPpuControlRegister1()
	{
		return ppuControlRegister1;
	}

	public void setPpuControlRegister1(PPUControlRegister1 ppuControlRegister1)
	{
		this.ppuControlRegister1 = ppuControlRegister1;
	}

	public PPUControlRegister2 getPpuControlRegister2()
	{
		return ppuControlRegister2;
	}

	public void setPpuControlRegister2(PPUControlRegister2 ppuControlRegister2)
	{
		this.ppuControlRegister2 = ppuControlRegister2;
	}

	public PPUStatusRegister getPpuStatusRegister()
	{
		return ppuStatusRegister;
	}

	public void setPpuStatusRegister(PPUStatusRegister ppuStatusRegister)
	{
		this.ppuStatusRegister = ppuStatusRegister;
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

	public PPUSprRamIORegister getPpuSprRamIORegister()
	{
		return ppuSprRamIORegister;
	}

	public void setPpuSprRamIORegister(PPUSprRamIORegister ppuSprRamIORegister)
	{
		this.ppuSprRamIORegister = ppuSprRamIORegister;
	}

	public PPUVramAddressRegister2 getPpuVramAddressRegister2()
	{
		return ppuVramAddressRegister2;
	}

	public void setPpuVramAddressRegister2(
			PPUVramAddressRegister2 ppuVramAddressRegister)
	{
		this.ppuVramAddressRegister2 = ppuVramAddressRegister;
	}

	public void setPpuVramIORegister(PPUVramIORegister ppuVramIORegister)
	{
		this.ppuVramIORegister = ppuVramIORegister;
	}

	public PPUSpriteDMARegister getPpuDMARegister()
	{
		return ppuDMARegister;
	}

	public void setPpuDMARegister(PPUSpriteDMARegister ppuDMARegister)
	{
		this.ppuDMARegister = ppuDMARegister;
	}

	public PPUVramAddressRegister1 getPpuVramAddressRegister1()
	{
		return ppuVramAddressRegister1;
	}

	public void setPpuVramAddressRegister1(
			PPUVramAddressRegister1 ppuVramAddressRegister1)
	{
		this.ppuVramAddressRegister1 = ppuVramAddressRegister1;
	}

	public ControlRegister1 getJoypadControlRegister1()
	{
		return joypadControlRegister1;
	}

	public void setJoypadControlRegister1(ControlRegister1 joypadControlRegister1)
	{
		this.joypadControlRegister1 = joypadControlRegister1;
	}

	public ControlRegister2 getJoypadControlRegister2()
	{
		return joypadControlRegister2;
	}

	public void setJoypadControlRegister2(ControlRegister2 joypadControlRegister2)
	{
		this.joypadControlRegister2 = joypadControlRegister2;
	}
}
