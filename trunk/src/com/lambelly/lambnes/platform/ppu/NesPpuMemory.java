package com.lambelly.lambnes.platform.ppu;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.*;
public class NesPpuMemory
{
	private Logger logger = Logger.getLogger(NesPpuMemory.class);
	private int[] patternTable0 = new int[4096]; // 0x0000
	private int[] patternTable1 = new int[4096]; // 0x1000
	
	public NesPpuMemory()
	{

	}

	public NesPpuMemory(int[] patternTiles)
	{
		this.setPatternTiles(patternTiles);
	}	
	
	public void setPatternTiles(int[] chrRom)
	{
		logger.debug("setting pattern tiles");
		if (chrRom.length > 4096)
		{
			// split in half
			int[] table0 = ArrayUtils.subarray(chrRom, 0, 4096);
			int[] table1 = ArrayUtils.subarray(chrRom, 4096, 8193);

			this.setPatternTable0(table0);
			this.setPatternTable1(table1);
		}		
	}

	public int[] getPatternTable0()
	{
		return patternTable0;
	}

	public void setPatternTable0(int[] patternTable0)
	{
		this.patternTable0 = patternTable0;
	}

	public int[] getPatternTable1()
	{
		return patternTable1;
	}

	public void setPatternTable1(int[] patternTable1)
	{
		this.patternTable1 = patternTable1;
	}
}
