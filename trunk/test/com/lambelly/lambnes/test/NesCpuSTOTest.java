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
public class NesCpuSTOTest
{
	@Autowired
	private NesCpu cpu;
	@Autowired
	private NesCpuMemory cpuMemory;
	@Autowired
	private TestUtils testUtils;
	private Logger logger = Logger.getLogger(NesCpuSTOTest.class);
	
	@Before
	public void setUp() throws Exception
	{
		this.getTestUtils().createTestPlatform();
	}
	
	@Test
	public void testSTAZeroPage()
	{
		int instruction = 0x85;
		this.getCpu().setAccumulator(0xDF);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0xDF, this.getCpuMemory().getMemoryFromHexAddress(0x01));
	}
	
	@Test
	public void testSTAZeroPageX()
	{
		int instruction = 0x95;
		this.getCpu().setAccumulator(0xDF);
		this.getCpu().setX(0x1A);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0xDF, this.getCpuMemory().getMemoryFromHexAddress(0x1B));
	}
	
	@Test
	public void testSTAAbsolute()
	{
		int instruction = 0x8D;
		this.getCpu().setAccumulator(0xDF);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0xDF, this.getCpuMemory().getMemoryFromHexAddress(0x0201));
	}
	
	@Test
	public void testSTAAbsoluteX()
	{
		int instruction = 0x9D;
		this.getCpu().setX(0x1A);
		this.getCpu().setAccumulator(0xDF);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0xDF, this.getCpuMemory().getMemoryFromHexAddress(0x021B));
	}	
	
	@Test
	public void testSTAAbsoluteY()
	{
		int instruction = 0x99;
		this.getCpu().setY(0x1A);
		this.getCpu().setAccumulator(0xDF);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0xDF, this.getCpuMemory().getMemoryFromHexAddress(0x021B));
	}		
	
	@Test
	public void testSTXZeroPage()
	{
		int instruction = 0x86;
		this.getCpu().setX(0x1A);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x1A, this.getCpuMemory().getMemoryFromHexAddress(0x01));
	}	
	
	@Test
	public void testSTXZeroPageY()
	{
		int instruction = 0x96;
		this.getCpu().setX(0x1A);
		this.getCpu().setY(0x1A);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x1A, this.getCpuMemory().getMemoryFromHexAddress(0x1B));
	}	
	
	@Test
	public void testSTXAbsolute()
	{
		int instruction = 0x8E;
		this.getCpu().setX(0x1A);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x1A, this.getCpuMemory().getMemoryFromHexAddress(0x201));
	}	
	
	@Test
	public void testSTYZeroPage()
	{
		int instruction = 0x84;
		this.getCpu().setY(0x1A);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x1A, this.getCpuMemory().getMemoryFromHexAddress(0x01));
	}	
	
	@Test
	public void testSTYZeroPageX()
	{
		int instruction = 0x94;
		this.getCpu().setX(0x1A);
		this.getCpu().setY(0x1A);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x1A, this.getCpuMemory().getMemoryFromHexAddress(0x1B));
	}	
	
	@Test
	public void testSTYAbsolute()
	{
		int instruction = 0x8C;
		this.getCpu().setY(0x1A);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x1A, this.getCpuMemory().getMemoryFromHexAddress(0x201));
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
