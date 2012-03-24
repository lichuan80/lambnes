package com.lambelly.lambnes.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
import org.apache.log4j.*;

import com.lambelly.lambnes.platform.cpu.NesCpu;
import com.lambelly.lambnes.platform.cpu.NesCpuMemory;
import com.lambelly.lambnes.platform.ppu.NesPpu;
import com.lambelly.lambnes.platform.ppu.NesPpuMemory;
import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.test.utils.TestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:beans.xml"})
public class PPUSpriteDMARegisterTest
{
	@Autowired
	private NesPpu ppu;
	@Autowired
	private NesCpuMemory cpuMemory;
	@Autowired
	private NesPpuMemory ppuMemory;
	@Autowired
	private TestUtils testUtils;
	private Logger logger = Logger.getLogger(PPUSpriteDMARegisterTest.class);
	
	@Before
	public void setUp()throws Exception
	{
		this.getTestUtils().createTestPlatform();
	}
	
	@Test
	public void write()
	{
		// write to 0X3F00
		this.getCpuMemory().setMemoryFromHexAddress(0x0350, 0x8C);
		this.getCpuMemory().setMemoryFromHexAddress(0x4014, 0x03);
		this.getPpu().cycle(1);
		
		// test
		assertEquals(0x3F, this.getPpuMemory().getSprRam()[0x3F]);
		assertEquals(0x8C, this.getPpuMemory().getSprRam()[0x50]);
	}

	public TestUtils getTestUtils()
    {
    	return testUtils;
    }

	public void setTestUtils(TestUtils testUtils)
    {
    	this.testUtils = testUtils;
    }

	public NesCpuMemory getCpuMemory()
    {
    	return cpuMemory;
    }

	public void setCpuMemory(NesCpuMemory cpuMemory)
    {
    	this.cpuMemory = cpuMemory;
    }

	public NesPpuMemory getPpuMemory()
    {
    	return ppuMemory;
    }

	public void setPpuMemory(NesPpuMemory ppuMemory)
    {
    	this.ppuMemory = ppuMemory;
    }

	public NesPpu getPpu()
    {
    	return ppu;
    }

	public void setPpu(NesPpu ppu)
    {
    	this.ppu = ppu;
    }	
}
