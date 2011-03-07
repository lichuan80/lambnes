package com.lambelly.lambnes.platform.ppu;

import org.apache.log4j.Logger;

public class PPUNameTable
{
	private int[] nameTable = new int[960]; // 0x2000 - 0x23C0
	private int[] attributeTable = new int [64]; // 0x23C1 - 2400
	private static final int BASE_ADDRESS_MASK = 0x3FF; // max array index. masks to find array index from address provided.
	private Logger logger = Logger.getLogger(PPUNameTable.class);
	
	public PPUNameTable()
	{
	}
	
	public void setMemoryFromHexAddress(int address, int value)
	{
		logger.debug("setting value " + value + " to address: 0x" + Integer.toHexString(address));
		int index = address & PPUNameTable.BASE_ADDRESS_MASK;
		
		if (index >= 0 && index < 960)
		{
			this.getNameTable()[index] = value;
		}
		else if (index >= 960 && index < 1024)
		{
			this.getAttributeTable()[index - 960] = value;
		}
		else
		{
			throw new IllegalStateException("tried to set memory address 0x" + Integer.toHexString(address) + " which is not mapped to any data structure");
		}
	}

	public BackgroundTile getTileFromHexAddress(int address) throws IllegalStateException
	{
		int index = address & PPUNameTable.BASE_ADDRESS_MASK;

		// expect value in pattern table memory range
		if (index >= 0 && index <=959)
		{
			// determine attribute table bit (color MSB)
			int colorMSB = 0;
			
			// determine row and column in nametable for address
			int nameTableRow = address % 30; // each nameTable has 30 rows
			int nameTableCol = address % 32; // each nameTable has 32 rows
			
			colorMSB = this.getColorMSB(nameTableRow, nameTableCol);
			logger.debug("upperBits: " + colorMSB);
			return (NesTileCache.getBackgroundTile(this.getMemoryFromHexAddress(address),colorMSB));
		}
		else
		{
			throw new IllegalStateException("tried to access memory address 0x" + Integer.toHexString(address) + " which is not mapped to any data structure");
		}
	}
	
	public int getMemoryFromHexAddress(int address) throws IllegalStateException
	{
		int index = address & PPUNameTable.BASE_ADDRESS_MASK;
		
		if (index >= 0 && index <=959)
		{
			return (this.getNameTable()[index]);
		}
		else if (index >= 960 && index <= 1023)
		{
			return (this.getAttributeTable()[index - 960]);
		}
		else
		{
			throw new IllegalStateException("tried to access memory address 0x" + Integer.toHexString(address) + " which is not mapped to any data structure");
		}
	}
	
	private int getColorMSB(int nameTableRow, int nameTableCol)
	{
		// determine which attribute applies to this particular row and column
		// nameTableRow ought to be a value between 0-31
		// nameTableCol ought to be a value between 0-29
		
		int attributeIndex = (nameTableCol >> 2) | (nameTableRow >> 2);
		logger.debug("looking up row: " + nameTableRow + " col: " + nameTableCol + " attributeIndex: " + attributeIndex);
		
		// determine which bit applies to this particular col / row
        int bit1 = nameTableCol & 0x2;
        int bit2 =  nameTableRow & 0x2;
        int shift = bit1 | (bit2 << 1);
        return (this.getAttributeTable()[attributeIndex] >> shift) & 0x3;
	}    
	
	public int[] getNameTable()
	{
		return nameTable;
	}

	public void setNameTable(int[] nameTable)
	{
		this.nameTable = nameTable;
	}

	public int[] getAttributeTable()
	{
		return attributeTable;
	}

	public void setAttributeTable(int[] attributeTable)
	{
		this.attributeTable = attributeTable;
	}
}
