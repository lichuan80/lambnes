package com.lambelly.lambnes.test;

import org.junit.Before;
import org.junit.Test;

import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.platform.ppu.*;
import com.lambelly.lambnes.test.utils.TestUtils;
import com.lambelly.lambnes.util.ArrayUtils;

import org.apache.log4j.*;

public class PaletteTest
{
	private Logger logger = Logger.getLogger(PaletteTest.class);
	
	@Before
	public void setUp() throws Exception
	{
		TestUtils.createTestPlatform();
	}
	
	@Test
	public void testDMA()
	{
		Platform.getCpuMemory().setMemoryFromHexAddress(0x4014, 0x02);
		
		((NesPpu)Platform.getPpu()).getPpuSpriteDMARegister().cycle();
		
		ArrayUtils.head(Platform.getPpuMemory().getSprRam(), 10);
	}
	
	@Test
	public void testDMA2()
	{
		Platform.getCpuMemory().setMemoryFromHexAddress(0x4014, 0x07);
		
		((NesPpu)Platform.getPpu()).getPpuSpriteDMARegister().cycle();
		
		ArrayUtils.head(Platform.getPpuMemory().getSprRam(), 10);		
	}
	
	@Test
	public void testDMA3()
	{
		Platform.getCpuMemory().setMemoryFromHexAddress(0x4014, 0x06);
		
		((NesPpu)Platform.getPpu()).getPpuSpriteDMARegister().cycle();
		
		ArrayUtils.head(Platform.getPpuMemory().getSprRam(), 10);		
	}	
	
	@Test
	public void testDMA4()
	{
		Platform.getCpuMemory().setMemoryFromHexAddress(0x4014, 0x05);
		
		((NesPpu)Platform.getPpu()).getPpuSpriteDMARegister().cycle();
		
		ArrayUtils.head(Platform.getPpuMemory().getSprRam(), 10);		
	}
	
	@Test
	public void testSubArray()
	{
		int[] a = new int[250];
		a[10] = 24;
		int[] b = org.apache.commons.lang.ArrayUtils.subarray(a, 0, 20);
		ArrayUtils.head(b, 20);
	}

	@Test
	public void testSubArray2()
	{
		Platform.getCpuMemory().getRam()[0x550] = 24;
		logger.debug(Integer.toHexString(Platform.getCpuMemory().getRam().length));
		int[] b = org.apache.commons.lang.ArrayUtils.subarray(Platform.getCpuMemory().getRam(), 0x540, 0x540 + 0x20);
		ArrayUtils.head(b, 0x20);
	}
}
