package com.lambelly.lambnes.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
import org.apache.log4j.*;

import com.lambelly.lambnes.platform.*;
import com.lambelly.lambnes.platform.cpu.NesCpu;
import com.lambelly.lambnes.platform.cpu.NesCpuMemory;
import com.lambelly.lambnes.test.utils.TestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:beans.xml"})
public class NesCpuTAXTest
{
	@Autowired
	private NesCpu cpu;
	@Autowired
	private NesCpuMemory cpuMemory;
	@Autowired
	private TestUtils testUtils;
	private Logger logger = Logger.getLogger(NesCpuTAXTest.class);
	
	@Before
	public void setUp() throws Exception
	{
		this.getTestUtils().createTestPlatform();
	}
	
	@Test
	public void testTAX()
	{
		int instruction = 0xAA;
		this.getCpu().setAccumulator(0xDF);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0xDF, this.getCpu().getX());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
	}
	
	@Test
	public void testTAY()
	{
		int instruction = 0xA8;
		this.getCpu().setAccumulator(0x1E);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x1E, this.getCpu().getY());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
	}
	
	@Test
	public void testTSX()
	{
		int instruction = 0xBA;
		this.getCpuMemory().setStackPointer(0x1E);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x1E, this.getCpu().getX());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
	}	
	
	@Test
	public void testTXA()
	{
		int instruction = 0x8A;
		this.getCpu().setX(0xDF);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0xDF, this.getCpu().getAccumulator());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
	}
	
	@Test
	public void testTXS()
	{
		int instruction = 0x9A;
		this.getCpu().setX(0xAE);
		this.getCpu().getFlags().setOverflow(true);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0xAE, this.getCpuMemory().getStackPointer());
		assertTrue(this.getCpu().getFlags().isOverflow());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
	}		
	
	@Test
	public void testTYA()
	{
		int instruction = 0x98;
		this.getCpu().setY(0x1E);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x1E, this.getCpu().getAccumulator());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
	}

	public NesCpu getCpu()
    {
    	return cpu;
    }

	public void setCpu(NesCpu cpu)
    {
    	this.cpu = cpu;
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
}
