package com.lambelly.lambnes.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.platform.ppu.*;
import com.lambelly.lambnes.platform.ppu.registers.PPUSpriteDMARegister;
import com.lambelly.lambnes.test.utils.TestUtils;
import com.lambelly.lambnes.util.ArrayUtils;
import com.lambelly.lambnes.platform.cpu.NesCpu;
import com.lambelly.lambnes.platform.cpu.NesCpuMemory;

import org.apache.log4j.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:beans.xml"})
public class PaletteTest
{
	@Autowired
	private NesCpuMemory cpuMemory;
	@Autowired
	private PPUSpriteDMARegister spriteDMARegister;
	@Autowired
	private TestUtils testUtils;
	private Logger logger = Logger.getLogger(PaletteTest.class);
	
	@Before
	public void setUp() throws Exception
	{
		this.getTestUtils().createTestPlatform();
	}
	
	@Test
	public void testDMA()
	{
		this.getCpuMemory().setMemoryFromHexAddress(0x4014, 0x02);
		
		this.getSpriteDMARegister().cycle();
		
		// ArrayUtils.head(Platform.getPpuMemory().getSprRam(), 10);
	}
	
	@Test
	public void testDMA2()
	{
		this.getCpuMemory().setMemoryFromHexAddress(0x4014, 0x07);
		
		this.getSpriteDMARegister().cycle();
		
		// ArrayUtils.head(Platform.getPpuMemory().getSprRam(), 10);		
	}
	
	@Test
	public void testDMA3()
	{
		this.getCpuMemory().setMemoryFromHexAddress(0x4014, 0x06);
		
		this.getSpriteDMARegister().cycle();
		
		// ArrayUtils.head(Platform.getPpuMemory().getSprRam(), 10);		
	}	
	
	@Test
	public void testDMA4()
	{
		this.getCpuMemory().setMemoryFromHexAddress(0x4014, 0x05);
		
		this.getSpriteDMARegister().cycle();
		
		// ArrayUtils.head(Platform.getPpuMemory().getSprRam(), 10);		
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
		this.getCpuMemory().setMemoryFromHexAddress(0x550,24);
	}

	public NesCpuMemory getCpuMemory()
    {
    	return cpuMemory;
    }

	public void setCpuMemory(NesCpuMemory cpuMemory)
    {
    	this.cpuMemory = cpuMemory;
    }

	public TestUtils getTestUtils()
    {
    	return testUtils;
    }

	public void setTestUtils(TestUtils testUtils)
    {
    	this.testUtils = testUtils;
    }

	public PPUSpriteDMARegister getSpriteDMARegister()
    {
    	return spriteDMARegister;
    }

	public void setSpriteDMARegister(PPUSpriteDMARegister spriteDMARegister)
    {
    	this.spriteDMARegister = spriteDMARegister;
    }
}
