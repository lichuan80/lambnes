package com.lambelly.lambnes.platform.ppu;

import org.apache.log4j.Logger;

public class PPUNameTable
{
	private int[] nameTable = new int[960]; // 0x2000 - 0x23BF
	private int[] attributeTable = new int [64]; // 0x23C0 - 23FF
	private static final int BASE_ADDRESS_MASK = 0x3FF; // max array index. masks to find array index from address provided.
	private NesTileCache tileCache = new NesTileCache();
	private Logger logger = Logger.getLogger(PPUNameTable.class);
	
	public PPUNameTable()
	{
		
	}
	
	public void setMemoryFromHexAddress(int address, int value)
	{
		// logger.debug("setting value " + value + " to address: 0x" + Integer.toHexString(address));
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
			int nameTableRow = index >> 5; // column is essentially the first 5 bits of the number -- throw out for row
			int nameTableCol = index & 31; // column is essentially the first 5 bits of the number -- exclude for column
			
			// instantiate tile with correct MSB
			colorMSB = this.getColorMSB(nameTableRow, nameTableCol);
			BackgroundTile bTile = this.getTileCache().getBackgroundTile(this.getMemoryFromHexAddress(address),colorMSB);
			
			//logger.info("using tile: " + bTile.getTileNumber() + " with MSB: " + bTile.getAttributes().getColorHighBit() + " for NameTableRow: " + nameTableRow + " and NameTableColumn: " + nameTableCol + " and address: " + address);
			
			return (bTile);
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
		int attributeTableCol = (nameTableCol / 4) & 7;
		int attributeTableRow = (nameTableRow / 4) & 7;
		int attributeIndex = (attributeTableRow << 3) | (attributeTableCol);
		int attributeValue = this.getAttributeTable()[attributeIndex];
		
        int bit1 = nameTableCol & 0x2;
        int bit2 =  nameTableRow & 0x2;
        int shift = bit1 | (bit2 << 1);
        
        if (logger.isDebugEnabled())
        {
        	logger.debug("looking up background attribute row: " + nameTableRow + " col: " + nameTableCol + " attributeIndex: " + attributeIndex + " attributeValue: " + attributeValue + " shift " + shift);
        }
        
        return (attributeValue >> shift) & 0x3;
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

	public NesTileCache getTileCache()
    {
    	return tileCache;
    }

	public void setTileCache(NesTileCache tileCache)
    {
    	this.tileCache = tileCache;
    }
}
