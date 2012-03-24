package com.lambelly.lambnes.platform.mappers;

import org.apache.log4j.*;

import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.platform.cpu.NesCpuMemory;
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
import com.lambelly.lambnes.util.NumberConversionUtils;

/**
 * 
 * @author thomasmccarthy
 */
public class Mapper0  implements Mapper
{	
	NesCpuMemory cpuMemory = null;
	private Logger logger = Logger.getLogger(Mapper0.class);
	
	public int getMemoryFromHexAddress(int address) throws IllegalStateException
	{
		//logger.debug("getting memory from address: 0x" + Integer.toHexString(address));

		int value = 0;
		
		value = this.getCpuMemory().getMemory()[address];
		
		// determine if register holds memory
		if (address >= 0x2000 && address <= 0x3FFF)
		{
			// determine if mirrored
			if (address > 0x2007)
			{
				// I only really care about the LSB
				address = address & 0xFF; // throw out MSB
				
				// of the LSB I only really care which register it's referring to
				address = address % 8;
				address += 0x2000; 
			}
			
			// Input/Output registers
			if (address == PPUControlRegister.REGISTER_ADDRESS)
			{
				throw new IllegalStateException("reading from write only register: " + Integer.toHexString(address));
			}
			else if (address == PPUMaskRegister.REGISTER_ADDRESS)
			{
				throw new IllegalStateException("reading from write only register: " + Integer.toHexString(address));
			}
			else if (address == PPUSprRamAddressRegister.REGISTER_ADDRESS)
			{
				throw new IllegalStateException("reading from write only register: " + Integer.toHexString(address));
			}
			else if (address == PPUSprRamIORegister.REGISTER_ADDRESS)
			{
				value = this.getCpuMemory().getPpuSprRamIORegister().getRegisterValue();
			}
			else if (address == PPUStatusRegister.REGISTER_ADDRESS)
			{
				value = this.getCpuMemory().getPpuStatusRegister().getRegisterValue();
			}
			else if (address == PPUScrollRegister.REGISTER_ADDRESS)
			{
				throw new IllegalStateException("reading from write only register: " + Integer.toHexString(address));
			}
			else if (address == PPUVramAddressRegister.REGISTER_ADDRESS)
			{
				throw new IllegalStateException("reading from write only register: " + Integer.toHexString(address));
			}
			else if (address == PPUVramIORegister.REGISTER_ADDRESS)
			{
				value = this.getCpuMemory().getPpuVramIORegister().getRegisterValue();
			}
			
			logger.debug("getting memory from control register 0x" + Integer.toHexString(address) + ": " + Integer.toHexString(value));
		}
		else if (address >= 0x4000 && address <= 0x401F)
		{
			// Input/Output registers
			if (address == PPUSpriteDMARegister.REGISTER_ADDRESS)
			{
				throw new IllegalStateException("reading from write only register");
			}
			else if (address == ControlRegister1.REGISTER_ADDRESS)
			{
				value = this.getCpuMemory().getControlRegister1().getRegisterValue();
			}
			else if (address == ControlRegister2.REGISTER_ADDRESS)
			{
				value = this.getCpuMemory().getControlRegister2().getRegisterValue();
			}

		}
		
		return value;
	}

	public void setMemoryFromHexAddress(int address, int value) throws IllegalStateException
	{
	    //logger.info("setting memory to address: 0x" + Integer.toHexString(address) + ": 0x" + Integer.toHexString(value));
		
		// set memory
	    this.getCpuMemory().getMemory()[address] = value;
		
		// check registers for set
		if (address >= 0x2000 && address <= 0x3FFF)
		{
			// determine if mirrored
			if (address > 0x2007)
			{
				// I only really care about the LSB
				address = address & 0xFF; // throw out MSB
				
				// of the LSB I only really care which register it's referring to
				address = address % 8;
				address += 0x2000; 
			}
			
			// Input/Output registers
			if (address == PPUControlRegister.REGISTER_ADDRESS)
			{
				this.getCpuMemory().getPpuControlRegister().setRegisterValue(value);
			}
			else if (address == PPUMaskRegister.REGISTER_ADDRESS)
			{
				this.getCpuMemory().getPpuMaskRegister().setRegisterValue(value);
			}
			else if (address == PPUSprRamAddressRegister.REGISTER_ADDRESS)
			{
				this.getCpuMemory().getPpuSprRamAddressRegister().setRegisterValue(value);
			}
			else if (address == PPUSprRamIORegister.REGISTER_ADDRESS)
			{
				logger.info("write to 0x2004");
				this.getCpuMemory().getPpuSprRamIORegister().setRegisterValue(value);
			}
			else if (address == PPUStatusRegister.REGISTER_ADDRESS)
			{
				this.getCpuMemory().getPpuStatusRegister().setRegisterValue(value);
			}
			else if (address == PPUScrollRegister.REGISTER_ADDRESS)
			{
				this.getCpuMemory().getPpuScrollRegister().setRegisterValue(value);
			}
			else if (address == PPUVramAddressRegister.REGISTER_ADDRESS)
			{
				this.getCpuMemory().getPpuVramAddressRegister().setRegisterValue(value);
			}
			else if (address == PPUVramIORegister.REGISTER_ADDRESS)
			{
				this.getCpuMemory().getPpuVramIORegister().setRegisterValue(value);
			}
			
			//logger.debug("setting control register 0x" + Integer.toHexString(address) + " to " + Integer.toHexString(value) + " at cpu cycle " + Platform.getCycleCount());
		}
		else if (address >= 0x4000 && address <= 0x401F)
		{
			//logger.info("write to one register " + Integer.toHexString(address) + ": " + value);
			// Input/Output registers
			if (address == APUPulse1ChannelRegister.REGISTER_ADDRESS)
			{
				APUPulse1ChannelRegister.setRegisterValue(value);
			}
			else if (address == PPUSpriteDMARegister.REGISTER_ADDRESS)
			{
				this.getCpuMemory().getPpuSpriteDmaRegister().setRegisterValue(value);
			}
			else if (address == APUControlRegister.REGISTER_ADDRESS)
			{
				this.getCpuMemory().getApuControlRegister().setRegisterValue(value);
			}
			else if (address == APUPulse1SweepRegister.REGISTER_ADDRESS)
			{
				this.getCpuMemory().getApuPulse1SweepRegister().setRegisterValue(value);
			}
			else if (address == APUPulse1TimerLowRegister.REGISTER_ADDRESS)
			{
				this.getCpuMemory().getApuPulse1TimerLowRegister().setRegisterValue(value);
			}
			else if (address == APUPulse1LengthCounterRegister.REGISTER_ADDRESS)
			{
				this.getCpuMemory().getApuPulse1LengthCounterRegister().setRegisterValue(value);
			}
			else if (address == ControlRegister1.REGISTER_ADDRESS)
			{
				this.getCpuMemory().getControlRegister1().setRegisterValue(value);
			}
			else if (address == ControlRegister2.REGISTER_ADDRESS)
			{
				this.getCpuMemory().getControlRegister2().setRegisterValue(value);
				this.getCpuMemory().getApuFrameCounterRegister().setRegisterValue(value);
			}
		}
	}	
	
	public void setProgramInstructions(int[] programInstructions)
	{
		if (programInstructions.length > 16384)
		{
			logger.info("program instructions length: " + programInstructions.length);

			System.arraycopy(programInstructions, 0, this.getCpuMemory().getMemory(), NesCpuMemory.PRG_ROM_BASE, programInstructions.length);
		}
		else if (programInstructions.length <= 16384)
		{
			if(logger.isDebugEnabled())
			{
				logger.debug("mirroring prg rom");
			}
			// instructions are mirrored to fill the progrom
			for (int i = 0; i< (32768 / programInstructions.length); i++)
			{
				logger.debug("at " + i);
				System.arraycopy(programInstructions, 0, this.getCpuMemory().getMemory(), NesCpuMemory.PRG_ROM_BASE + (programInstructions.length * i), programInstructions.length);
			}
		}
	}

	public NesCpuMemory getCpuMemory()
    {
    	return cpuMemory;
    }

	public void setCpuMemory(NesCpuMemory cpuMemory)
    {
    	this.cpuMemory = cpuMemory;
    }
}
