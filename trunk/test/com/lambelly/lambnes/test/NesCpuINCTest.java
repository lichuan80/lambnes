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
public class NesCpuINCTest
{
	@Autowired
	private NesCpu cpu;
	@Autowired
	private NesCpuMemory cpuMemory;
	@Autowired
	private TestUtils testUtils;
	private Logger logger = Logger.getLogger(NesCpuINCTest.class);
	
	@Before
	public void setUp() throws Exception
	{
		logger.debug("shut yo mouf");
		this.getTestUtils().createTestPlatform();
	}

	@Test
	public void testINX()
	{
		// test 1
		int instruction = 0xE8;
		this.getCpu().setX(0xCC);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0xCD, this.getCpu().getX());
		assertTrue((this.getCpu()).getFlags().isNegative());
		assertFalse((this.getCpu()).getFlags().isZero());
		
		// test 2
		this.getCpu().setX(0xFF);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x00, this.getCpu().getX());
		assertFalse((this.getCpu()).getFlags().isNegative());
		assertTrue((this.getCpu()).getFlags().isZero());		
	}
	
	@Test
	public void testINY()
	{
		// test 1
		int instruction = 0xC8;
		this.getCpu().setY(0xCC);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0xCD, this.getCpu().getY());
		assertTrue((this.getCpu()).getFlags().isNegative());
		assertFalse((this.getCpu()).getFlags().isZero());
		
		// test 2
		this.getCpu().setY(0xFF);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x00, this.getCpu().getY());
		assertFalse((this.getCpu()).getFlags().isNegative());
		assertTrue((this.getCpu()).getFlags().isZero());		
	}
	
	@Test
	public void testINCZeroPage()
	{
		// test 1
		int instruction = 0xE6;
		this.getTestUtils().setMemory(0x01,0xCC);
		this.getTestUtils().performInstruction(instruction);	
		
		assertEquals(0xCD, this.getCpuMemory().getMemoryFromHexAddress(0x0001));
		assertTrue((this.getCpu()).getFlags().isNegative());
		assertFalse((this.getCpu()).getFlags().isZero());
		
		// test 2
		this.getTestUtils().setMemory(0x03, 0xFF);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x00, this.getCpuMemory().getMemoryFromHexAddress(0x0003));
		assertFalse((this.getCpu()).getFlags().isNegative());
		assertTrue((this.getCpu()).getFlags().isZero());		
	}
	
	@Test
	public void testINCZeroPageX()
	{
		// test 1
		int instruction = 0xF6;
		this.getCpu().setX(0xC0);
		this.getTestUtils().setMemory(0xC1,0xCD);
		this.getTestUtils().performInstruction(instruction);	
		
		assertEquals(0xCE, this.getCpuMemory().getMemoryFromHexAddress(0x00C1));
		assertTrue((this.getCpu()).getFlags().isNegative());
		assertFalse((this.getCpu()).getFlags().isZero());
		
		logger.debug("test2");
		// test 2
		this.getCpu().setX(0xC0);
		this.getTestUtils().setMemory(0x8003, 0x01);
		this.getTestUtils().setMemory(0xC1, 0xFF);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x00, this.getCpuMemory().getMemoryFromHexAddress(0x00C1));
		assertFalse((this.getCpu()).getFlags().isNegative());
		assertTrue((this.getCpu()).getFlags().isZero());		
	}	
	
	@Test
	public void testAbsolute()
	{
		// test 1
		int instruction = 0xEE;
		this.getCpu().setX(0xC0);
		this.getTestUtils().setMemory(0x8001,0xCC);
		this.getTestUtils().performInstruction(instruction);	
		
		assertEquals(0xCD, this.getCpuMemory().getMemoryFromHexAddress(0x02CC));
		assertTrue((this.getCpu()).getFlags().isNegative());
		assertFalse((this.getCpu()).getFlags().isZero());
		
		logger.debug("test2");
		// test 2
		this.getCpu().setX(0xC0);
		this.getTestUtils().setMemory(0x0504, 0xFF);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x00, this.getCpuMemory().getMemoryFromHexAddress(0x0504));
		assertFalse((this.getCpu()).getFlags().isNegative());
		assertTrue((this.getCpu()).getFlags().isZero());		
	}
	
	@Test
	public void testAbsoluteX()
	{
		// test 1
		int instruction = 0xFE;
		this.getCpu().setX(0xC0);
		this.getTestUtils().setMemory(0x02C1,0xCD);
		this.getTestUtils().performInstruction(instruction);	
		
		assertEquals(0xCE, this.getCpuMemory().getMemoryFromHexAddress(0x02C1));
		assertTrue((this.getCpu()).getFlags().isNegative());
		assertFalse((this.getCpu()).getFlags().isZero());
		
		logger.debug("test2");
		// test 2
		this.getCpu().setX(0xC0);
		this.getTestUtils().setMemory(0x05C4, 0xFF);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x00, this.getCpuMemory().getMemoryFromHexAddress(0x05C4));
		assertFalse((this.getCpu()).getFlags().isNegative());
		assertTrue((this.getCpu()).getFlags().isZero());		
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
