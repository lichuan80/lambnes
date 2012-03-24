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
public class NesCpuDECTest
{
	@Autowired
	private NesCpu cpu;
	@Autowired
	private NesCpuMemory cpuMemory;
	@Autowired
	private TestUtils testUtils;
	private Logger logger = Logger.getLogger(NesCpuDECTest.class);
	
	@Before
	public void setUp() throws Exception
	{
		this.getTestUtils().createTestPlatform();
	}

	@Test
	public void testDEX()
	{
		// test 1
		int instruction = 0xCA;
		this.getCpu().setX(0xCC);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0xCB, this.getCpu().getX());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
		
		// test 2
		this.getCpu().setX(0x01);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x00, this.getCpu().getX());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertTrue(this.getCpu().getFlags().isZero());
		
		// test3 
		this.getCpu().setX(0x00);
		this.getTestUtils().performInstruction(instruction);
		assertEquals(0xFF, this.getCpu().getX());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
	}
	
	@Test
	public void testDEY()
	{
		// test 1
		int instruction = 0x88;
		this.getCpu().setY(0xCC);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0xCB, this.getCpu().getY());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
		
		// test 2
		this.getCpu().setY(0x01);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x00, this.getCpu().getY());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertTrue(this.getCpu().getFlags().isZero());		

		// test 3
		this.getCpu().setY(0x00);
		this.getTestUtils().performInstruction(instruction);
		assertEquals(0xFF, this.getCpu().getY());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
	}
	
	@Test
	public void testDECZeroPage()
	{
		// test 1
		int instruction = 0xC6;
		this.getTestUtils().setMemory(0x01,0xCC);
		this.getTestUtils().performInstruction(instruction);	
		
		assertEquals(0xCB, this.getCpuMemory().getMemoryFromHexAddress(0x0001));
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
		
		// test 2
		this.getTestUtils().setMemory(0x03, 0x01);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x00, this.getCpuMemory().getMemoryFromHexAddress(0x0003));
		assertFalse(this.getCpu().getFlags().isNegative());
		assertTrue(this.getCpu().getFlags().isZero());		

		// test 3
		this.getTestUtils().setMemory(0x05, 0x00);
		this.getTestUtils().performInstruction(instruction);
		assertEquals(0xFF, this.getCpuMemory().getMemoryFromHexAddress(0x0005));
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());	
	}
	
	@Test
	public void testDECZeroPageX()
	{
		// test 1
		int instruction = 0xD6;
		this.getCpu().setX(0xC0);
		this.getTestUtils().setMemory(0xC1,0xCC);
		this.getTestUtils().performInstruction(instruction);	
		
		assertEquals(0xCB, this.getCpuMemory().getMemoryFromHexAddress(0x00C1));
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
		
		logger.debug("test2");
		// test 2
		this.getCpu().setX(0xC0);
		this.getTestUtils().setMemory(0x8003, 0x01);
		this.getTestUtils().setMemory(0xC1, 0x01);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x00, this.getCpuMemory().getMemoryFromHexAddress(0x00C1));
		assertFalse(this.getCpu().getFlags().isNegative());
		assertTrue(this.getCpu().getFlags().isZero());		

		// test 3
		this.getCpu().setX(0x43);
		this.getTestUtils().setMemory(0x48, 0x00);
		this.getTestUtils().performInstruction(instruction);
		assertEquals(0xFF, this.getCpuMemory().getMemoryFromHexAddress(0x0048));
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());	
	}	
	
	@Test
	public void testAbsolute()
	{
		// test 1
		int instruction = 0xCE;
		this.getCpu().setX(0xC0);
		this.getTestUtils().setMemory(0x0201,0xCC);
		this.getTestUtils().performInstruction(instruction);	
		
		assertEquals(0xCB, this.getCpuMemory().getMemoryFromHexAddress(0x0201));
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
		
		logger.debug("test2");
		// test 2
		this.getCpu().setX(0xC0);
		this.getTestUtils().setMemory(0x0504, 0x01);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x00, this.getCpuMemory().getMemoryFromHexAddress(0x0504));
		assertFalse(this.getCpu().getFlags().isNegative());
		assertTrue(this.getCpu().getFlags().isZero());		
	
		// test 3
		this.getTestUtils().setMemory(0x0807, 0x00);
		this.getTestUtils().performInstruction(instruction);
		assertEquals(0xFF, this.getCpuMemory().getMemoryFromHexAddress(0x0807));
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
	}
	
	@Test
	public void testAbsoluteX()
	{
		// test 1
		int instruction = 0xDE;
		this.getCpu().setX(0xC0);
		this.getTestUtils().setMemory(0x02C1,0xCC);
		this.getTestUtils().performInstruction(instruction);	
		
		assertEquals(0xCB, this.getCpuMemory().getMemoryFromHexAddress(0x02C1));
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
		
		logger.debug("test2");
		// test 2
		this.getCpu().setX(0xC0);
		this.getTestUtils().setMemory(0x05C4, 0x01);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x00, this.getCpuMemory().getMemoryFromHexAddress(0x05C4));
		assertFalse(this.getCpu().getFlags().isNegative());
		assertTrue(this.getCpu().getFlags().isZero());
		
		// test 3
		this.getCpu().setX(0xA0);
		this.getTestUtils().setMemory(0x8A7, 0x00);
		this.getTestUtils().performInstruction(instruction);
		assertEquals(0xFF, this.getCpuMemory().getMemoryFromHexAddress(0x08A7));
		assertTrue(this.getCpu().getFlags().isNegative());
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
