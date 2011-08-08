package com.lambelly.lambnes.platform.ppu.registers;

import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.util.*;
import org.apache.log4j.*;

public class PPUControlRegister
{
	public static final int REGISTER_ADDRESS = 0x2000;
	public static final int MASTER_SLAVE_SELECTION_SLAVE = 1;
	public static final int MASTER_SLAVE_SELECTION_MASTER = 0;
	public static final int SPRITE_SIZE_8X8 = 0;
	public static final int SPRITE_SIZE_8X16 = 1;
	public static final int BACKGROUND_PATTERN_TABLE_ADDRESS_0000 = 0;
	public static final int BACKGROUND_PATTERN_TABLE_ADDRESS_1000 = 1;
	public static final int SPRITE_PATTERN_TABLE_ADDRESS_0000 = 0;
	public static final int SPRITE_PATTERN_TABLE_ADDRESS_1000 = 1;
	public static final int PPU_ADDRESS_INCREMENT_1 = 0;
	public static final int PPU_ADDRESS_INCREMENT_32 = 1;
	private static final int CYCLES_PER_EXECUTION = 0;
	public static final int NAME_TABLE_ADDRESS_2000 = 0;
	public static final int NAME_TABLE_ADDRESS_2400 = 1;
	public static final int NAME_TABLE_ADDRESS_2800 = 2;
	public static final int NAME_TABLE_ADDRESS_2C00 = 3;
	
	private static PPUControlRegister register = new PPUControlRegister();
	private Integer rawControlByte = null;
	private boolean executeNMIOnVBlank = false;
	private int masterSlaveSelection = 0;
	private int spriteSize = 0;
	private int backgroundPatternTableAddress = 0;
	private int spritePatternTableAddress = 0;
	private int ppuAddressIncrement = 0;
	private int nameTableAddress = 0;
	private Logger logger = Logger.getLogger(PPUControlRegister.class);
	
	private PPUControlRegister()
	{
	
	}
	
	public int cycle()
	{
		if (this.getRawControlByte() != null)
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("rawControlByte: " + rawControlByte);
			}
			this.setExecuteNMIOnVBlank(BitUtils.isBitSet(this.getRawControlByte(), 7));
			this.setMasterSlaveSelection(BitUtils.isBitSet(this.getRawControlByte(), 6)?MASTER_SLAVE_SELECTION_SLAVE:MASTER_SLAVE_SELECTION_MASTER);
			this.setSpriteSize(BitUtils.isBitSet(this.getRawControlByte(), 5)?SPRITE_SIZE_8X16:SPRITE_SIZE_8X8);
			logger.info("setting background pattern table address to: " + (BitUtils.isBitSet(this.getRawControlByte(), 4)?BACKGROUND_PATTERN_TABLE_ADDRESS_1000:BACKGROUND_PATTERN_TABLE_ADDRESS_0000));
			logger.debug("setting sprite pattern table address to: " + (BitUtils.isBitSet(this.getRawControlByte(), 3)?BACKGROUND_PATTERN_TABLE_ADDRESS_1000:BACKGROUND_PATTERN_TABLE_ADDRESS_0000));
			this.setBackgroundPatternTableAddress(BitUtils.isBitSet(this.getRawControlByte(), 4)?BACKGROUND_PATTERN_TABLE_ADDRESS_1000:BACKGROUND_PATTERN_TABLE_ADDRESS_0000);
			this.setSpritePatternTableAddress(BitUtils.isBitSet(this.getRawControlByte(), 3)?SPRITE_PATTERN_TABLE_ADDRESS_1000:SPRITE_PATTERN_TABLE_ADDRESS_0000);
			this.setPpuAddressIncrement(BitUtils.isBitSet(this.getRawControlByte(), 2)?PPU_ADDRESS_INCREMENT_32:PPU_ADDRESS_INCREMENT_1);

			// base name table logic
			int nameTableControlBit = (this.getRawControlByte() & 3); 
			this.setNameTableAddress(nameTableControlBit);
			int loopyT = (Platform.getPpu().getLoopyT() & 0x1FF) | (nameTableControlBit << 9);
			logger.info("rawControlByte was: " + this.getRawControlByte() + " loopyT was: " + Platform.getPpu().getLoopyT() + " setting loopyT to: " + loopyT);
			Platform.getPpu().setLoopyT(loopyT);
			
			this.clear();
		}
		
		return PPUControlRegister.CYCLES_PER_EXECUTION;
	}
	
	public String toString()
	{
		if (this.getRawControlByte() != null)
		{
			return "0x" + REGISTER_ADDRESS + ": " + Integer.toBinaryString(this.getRawControlByte());
		}
		else
		{
			return "0x" + REGISTER_ADDRESS + ": 0";
		}
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

	public boolean isExecuteNMIOnVBlank()
	{
		return executeNMIOnVBlank;
	}

	private void setExecuteNMIOnVBlank(boolean executeNMIOnVBlank)
	{
		this.executeNMIOnVBlank = executeNMIOnVBlank;
	}

	public int getMasterSlaveSelection()
	{
		return masterSlaveSelection;
	}

	private void setMasterSlaveSelection(int masterSlaveSelection)
	{
		this.masterSlaveSelection = masterSlaveSelection;
	}

	public int getSpriteSize()
	{
		return spriteSize;
	}

	private void setSpriteSize(int spriteSize)
	{
		this.spriteSize = spriteSize;
	}

	public int getBackgroundPatternTableAddress()
	{
		return backgroundPatternTableAddress;
	}

	private void setBackgroundPatternTableAddress(int backgroundPatternTableAddress)
	{
		this.backgroundPatternTableAddress = backgroundPatternTableAddress;
	}

	public int getSpritePatternTableAddress()
	{
		return spritePatternTableAddress;
	}

	private void setSpritePatternTableAddress(int spritePatternTableAddress)
	{
		this.spritePatternTableAddress = spritePatternTableAddress;
	}

	public int getPpuAddressIncrement()
	{
		return ppuAddressIncrement;
	}

	private void setPpuAddressIncrement(int ppuAddressIncrement)
	{
		this.ppuAddressIncrement = ppuAddressIncrement;
	}

	public int getNameTableAddress()
	{
		return nameTableAddress;
	}

	private void setNameTableAddress(int nameTableAddress)
	{
		this.nameTableAddress = nameTableAddress;
	}

	public static PPUControlRegister getRegister()
	{
		return register;
	}

	private static void setRegister(PPUControlRegister register)
	{
		PPUControlRegister.register = register;
	}
}
