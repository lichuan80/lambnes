package com.lambelly.lambnes.platform.ppu;

import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.util.*;

public class PPUControlRegister1
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
	public static final int NAME_TABLE_ADDRESS_2000 = 0;
	public static final int NAME_TABLE_ADDRESS_2400 = 1;
	public static final int NAME_TABLE_ADDRESS_2800 = 2;
	public static final int NAME_TABLE_ADDRESS_2C00 = 3;
	
	private int rawControlByte = 0;
	private boolean executeNMIOnVBlank = false;
	private int masterSlaveSelection = 0;
	private int spriteSize = 0;
	private int backgroundPatternTableAddress = 0;
	private int spritePatternTableAccess = 0;
	private int ppuAddressIncrement = 0;
	private int nameTableAddress = 0;
	
	public PPUControlRegister1()
	{
	
	}
	
	public void read()
	{
		int rawControlByte = Platform.getCpuMemory().getMemoryFromHexAddress(PPUControlRegister1.REGISTER_ADDRESS);
		this.setRawControlByte(rawControlByte);
		this.setExecuteNMIOnVBlank(BitUtils.isBitSet(rawControlByte, 7));
		this.setMasterSlaveSelection(BitUtils.isBitSet(rawControlByte, 6)?MASTER_SLAVE_SELECTION_SLAVE:MASTER_SLAVE_SELECTION_MASTER);
		this.setSpriteSize(BitUtils.isBitSet(rawControlByte, 5)?SPRITE_SIZE_8X16:SPRITE_SIZE_8X8);
		this.setBackgroundPatternTableAddress(BitUtils.isBitSet(rawControlByte, 4)?BACKGROUND_PATTERN_TABLE_ADDRESS_1000:BACKGROUND_PATTERN_TABLE_ADDRESS_0000);
		this.setSpritePatternTableAccess(BitUtils.isBitSet(rawControlByte, 7)?SPRITE_PATTERN_TABLE_ADDRESS_1000:SPRITE_PATTERN_TABLE_ADDRESS_0000);
		this.setPpuAddressIncrement(BitUtils.isBitSet(rawControlByte, 7)?PPU_ADDRESS_INCREMENT_32:PPU_ADDRESS_INCREMENT_1);
		
		int nameTableControlbit = (rawControlByte & 3); 
		
		if (nameTableControlbit == 0)
		{
			this.setNameTableAddress(NAME_TABLE_ADDRESS_2000);
		}
		else if (nameTableControlbit == 0)
		{
			this.setNameTableAddress(NAME_TABLE_ADDRESS_2400);
		}
		else if (nameTableControlbit == 2)
		{
			this.setNameTableAddress(NAME_TABLE_ADDRESS_2800);
		}
		else if (nameTableControlbit == 3)
		{
			this.setNameTableAddress(NAME_TABLE_ADDRESS_2C00);
		}	
	}

	public int getRawControlByte()
	{
		return rawControlByte;
	}

	public void setRawControlByte(int rawControlByte)
	{
		this.rawControlByte = rawControlByte;
	}

	public boolean isExecuteNMIOnVBlank()
	{
		return executeNMIOnVBlank;
	}

	public void setExecuteNMIOnVBlank(boolean executeNMIOnVBlank)
	{
		this.executeNMIOnVBlank = executeNMIOnVBlank;
	}

	public int getMasterSlaveSelection()
	{
		return masterSlaveSelection;
	}

	public void setMasterSlaveSelection(int masterSlaveSelection)
	{
		this.masterSlaveSelection = masterSlaveSelection;
	}

	public int getSpriteSize()
	{
		return spriteSize;
	}

	public void setSpriteSize(int spriteSize)
	{
		this.spriteSize = spriteSize;
	}

	public int getBackgroundPatternTableAddress()
	{
		return backgroundPatternTableAddress;
	}

	public void setBackgroundPatternTableAddress(int backgroundPatternTableAddress)
	{
		this.backgroundPatternTableAddress = backgroundPatternTableAddress;
	}

	public int getSpritePatternTableAccess()
	{
		return spritePatternTableAccess;
	}

	public void setSpritePatternTableAccess(int spritePatternTableAccess)
	{
		this.spritePatternTableAccess = spritePatternTableAccess;
	}

	public int getPpuAddressIncrement()
	{
		return ppuAddressIncrement;
	}

	public void setPpuAddressIncrement(int ppuAddressIncrement)
	{
		this.ppuAddressIncrement = ppuAddressIncrement;
	}

	public int getNameTableAddress()
	{
		return nameTableAddress;
	}

	public void setNameTableAddress(int nameTableAddress)
	{
		this.nameTableAddress = nameTableAddress;
	}
}
