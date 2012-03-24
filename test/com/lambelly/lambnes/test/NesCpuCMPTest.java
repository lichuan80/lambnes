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
public class NesCpuCMPTest
{
	@Autowired
	private NesCpu cpu;
	@Autowired
	private NesCpuMemory cpuMemory;
	@Autowired
	private TestUtils testUtils;
	private Logger logger = Logger.getLogger(NesCpuCMPTest.class);
	
	@Before
	public void setUp() throws Exception
	{
		this.getTestUtils().createTestPlatform();
	}

	@Test
	public void testImmediate()
	{
		int instruction = 0xC9;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		this.getCpu().setAccumulator(0x01);
		this.getCpuMemory().setMemoryFromHexAddress(0x8001, 0xFF);
		this.getTestUtils().performInstruction(instruction);
		assertFalse(this.getCpu().getFlags().isZero());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isCarry());
		
		// test case 2
		this.getCpu().setAccumulator(0x7F);
		this.getCpuMemory().setMemoryFromHexAddress(0x8003, 0x80);
		this.getTestUtils().performInstruction(instruction);
		assertFalse(this.getCpu().getFlags().isZero());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isCarry());
	}
	
	@Test
	public void testZeroPage()
	{
		int instruction = 0xC5;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		this.getCpu().setAccumulator(0x01);
		this.getCpuMemory().setMemoryFromHexAddress(0x0001, 0xFF);
		this.getTestUtils().performInstruction(instruction);
		assertFalse(this.getCpu().getFlags().isZero());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isCarry());
		
		// test case 2
		this.getCpu().setAccumulator(0x7F);
		this.getCpuMemory().setMemoryFromHexAddress(0x0003, 0x80);
		this.getTestUtils().performInstruction(instruction);
		assertFalse(this.getCpu().getFlags().isZero());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isCarry());
	}	
	
	@Test
	public void testZeroPageX()
	{
		int instruction = 0xD5;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		this.getCpu().setAccumulator(0x01);
		this.getCpu().setX(0x01);
		this.getCpuMemory().setMemoryFromHexAddress(0x0002, 0xFF);
		this.getTestUtils().performInstruction(instruction);
		assertFalse(this.getCpu().getFlags().isZero());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isCarry());
		
		// test case 2
		this.getCpu().setAccumulator(0x7F);
		this.getCpu().setX(0x03);
		this.getCpuMemory().setMemoryFromHexAddress(0x0006, 0x80);
		this.getTestUtils().performInstruction(instruction);
		assertFalse(this.getCpu().getFlags().isZero());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isCarry());
	}
	
	@Test
	public void testAbsolute()
	{
		int instruction = 0xCD;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		this.getCpu().setAccumulator(0x01);
		this.getCpuMemory().setMemoryFromHexAddress(0x0201, 0xFF);
		this.getTestUtils().performInstruction(instruction);
		assertFalse(this.getCpu().getFlags().isZero());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isCarry());
		
		// test case 2
		this.getCpu().setAccumulator(0x7F);
		this.getCpuMemory().setMemoryFromHexAddress(0x0504, 0x80);
		this.getTestUtils().performInstruction(instruction);
		assertFalse(this.getCpu().getFlags().isZero());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isCarry());
	}	
	
	@Test
	public void testAbsoluteX()
	{
		int instruction = 0xDD;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		this.getCpu().setAccumulator(0x01);
		this.getCpu().setX(0x01);
		this.getCpuMemory().setMemoryFromHexAddress(0x0202, 0xFF);
		this.getTestUtils().performInstruction(instruction);
		assertFalse(this.getCpu().getFlags().isZero());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isCarry());
		
		// test case 2
		this.getCpu().setAccumulator(0x7F);
		this.getCpu().setX(0x03);
		this.getCpuMemory().setMemoryFromHexAddress(0x0507, 0x80);
		this.getTestUtils().performInstruction(instruction);
		assertFalse(this.getCpu().getFlags().isZero());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isCarry());
	}	
	
	@Test
	public void testAbsoluteY()
	{
		int instruction = 0xD9;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		this.getCpu().setAccumulator(0x01);
		this.getCpu().setY(0x01);
		this.getCpuMemory().setMemoryFromHexAddress(0x0202, 0xFF);
		this.getTestUtils().performInstruction(instruction);
		assertFalse(this.getCpu().getFlags().isZero());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isCarry());
		
		// test case 2
		this.getCpu().setAccumulator(0x7F);
		this.getCpu().setY(0x03);
		this.getCpuMemory().setMemoryFromHexAddress(0x0507, 0x80);
		this.getTestUtils().performInstruction(instruction);
		assertFalse(this.getCpu().getFlags().isZero());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isCarry());
	}		
	
	@Test
	public void testIndexedIndirect()
	{
		int instruction = 0xC1;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		this.getCpu().setAccumulator(0x01);
		this.getCpu().setX(0x01);
		this.getCpuMemory().setMemoryFromHexAddress(0x0002, 0xFF);
		this.getTestUtils().performInstruction(instruction);
		assertFalse(this.getCpu().getFlags().isZero());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isCarry());
		
		// test case 2
		this.getCpu().setAccumulator(0x7F);
		this.getCpu().setX(0x03);
		this.getCpuMemory().setMemoryFromHexAddress(0x0006, 0x80);
		this.getTestUtils().performInstruction(instruction);
		assertFalse(this.getCpu().getFlags().isZero());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isCarry());
	}		
	
	@Test
	public void testIndirectIndexed()
	{
		int instruction = 0xD1;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		this.getCpu().setAccumulator(0x01);
		this.getCpu().setY(0x01);
		this.getCpuMemory().setMemoryFromHexAddress(0x0202, 0xFF);
		this.getTestUtils().performInstruction(instruction);
		assertFalse(this.getCpu().getFlags().isZero());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isCarry());
		
		// test case 2
		this.getCpu().setAccumulator(0x7F);
		this.getCpu().setY(0x03);
		this.getCpuMemory().setMemoryFromHexAddress(0x0406, 0x80);
		this.getTestUtils().performInstruction(instruction);
		assertFalse(this.getCpu().getFlags().isZero());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isCarry());
	}		
	
	@Test
	public void compareXImmediate()
	{
		int instruction = 0xE0;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		this.getCpu().setX(0x01);
		this.getCpuMemory().setMemoryFromHexAddress(0x8001, 0xFF);
		this.getTestUtils().performInstruction(instruction);
		assertFalse(this.getCpu().getFlags().isZero());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isCarry());
		
		// test case 2
		this.getCpu().setX(0x7F);
		this.getCpuMemory().setMemoryFromHexAddress(0x8003, 0x80);
		this.getTestUtils().performInstruction(instruction);
		assertFalse(this.getCpu().getFlags().isZero());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isCarry());		
	}
	
	@Test
	public void compareXZeroPage()
	{
		int instruction = 0xE4;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		this.getCpu().setX(0x01);
		this.getCpuMemory().setMemoryFromHexAddress(0x0001, 0xFF);
		this.getTestUtils().performInstruction(instruction);
		assertFalse(this.getCpu().getFlags().isZero());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isCarry());
		
		// test case 2
		this.getCpu().setX(0x7F);
		this.getCpuMemory().setMemoryFromHexAddress(0x0003, 0x80);
		this.getTestUtils().performInstruction(instruction);
		assertFalse(this.getCpu().getFlags().isZero());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isCarry());		
	}
	
	@Test
	public void compareXAbsolute()
	{
		int instruction = 0xEC;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		this.getCpu().setX(0x01);
		this.getCpuMemory().setMemoryFromHexAddress(0x0201, 0xFF);
		this.getTestUtils().performInstruction(instruction);
		assertFalse(this.getCpu().getFlags().isZero());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isCarry());
		
		// test case 2
		this.getCpu().setX(0x7F);
		this.getCpuMemory().setMemoryFromHexAddress(0x0504, 0x80);
		this.getTestUtils().performInstruction(instruction);
		assertFalse(this.getCpu().getFlags().isZero());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isCarry());		
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
