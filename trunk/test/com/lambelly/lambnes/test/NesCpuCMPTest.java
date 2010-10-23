package com.lambelly.lambnes.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.log4j.*;

import com.lambelly.lambnes.platform.*;
import com.lambelly.lambnes.platform.cpu.NesCpu;
import com.lambelly.lambnes.test.utils.TestUtils;

public class NesCpuCMPTest
{
	private Logger logger = Logger.getLogger(NesCpuCMPTest.class);
	
	@Before
	public void setUp() throws Exception
	{
		TestUtils.createTestPlatform();
	}

	@Test
	public void testImmediate()
	{
		int instruction = 0xC9;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		Platform.getCpu().setAccumulator(0x01);
		Platform.getCpuMemory().setMemoryFromHexAddress(0x8001, 0xFF);
		TestUtils.performInstruction(instruction);
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		
		// test case 2
		Platform.getCpu().setAccumulator(0x7F);
		Platform.getCpuMemory().setMemoryFromHexAddress(0x8003, 0x80);
		TestUtils.performInstruction(instruction);
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
	}
	
	@Test
	public void testZeroPage()
	{
		int instruction = 0xC5;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		Platform.getCpu().setAccumulator(0x01);
		Platform.getCpuMemory().setMemoryFromHexAddress(0x0001, 0xFF);
		TestUtils.performInstruction(instruction);
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		
		// test case 2
		Platform.getCpu().setAccumulator(0x7F);
		Platform.getCpuMemory().setMemoryFromHexAddress(0x0003, 0x80);
		TestUtils.performInstruction(instruction);
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
	}	
	
	@Test
	public void testZeroPageX()
	{
		int instruction = 0xD5;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		Platform.getCpu().setAccumulator(0x01);
		Platform.getCpu().setX(0x01);
		Platform.getCpuMemory().setMemoryFromHexAddress(0x0002, 0xFF);
		TestUtils.performInstruction(instruction);
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		
		// test case 2
		Platform.getCpu().setAccumulator(0x7F);
		Platform.getCpu().setX(0x03);
		Platform.getCpuMemory().setMemoryFromHexAddress(0x0006, 0x80);
		TestUtils.performInstruction(instruction);
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
	}
	
	@Test
	public void testAbsolute()
	{
		int instruction = 0xCD;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		Platform.getCpu().setAccumulator(0x01);
		Platform.getCpuMemory().setMemoryFromHexAddress(0x0201, 0xFF);
		TestUtils.performInstruction(instruction);
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		
		// test case 2
		Platform.getCpu().setAccumulator(0x7F);
		Platform.getCpuMemory().setMemoryFromHexAddress(0x0504, 0x80);
		TestUtils.performInstruction(instruction);
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
	}	
	
	@Test
	public void testAbsoluteX()
	{
		int instruction = 0xDD;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		Platform.getCpu().setAccumulator(0x01);
		Platform.getCpu().setX(0x01);
		Platform.getCpuMemory().setMemoryFromHexAddress(0x0202, 0xFF);
		TestUtils.performInstruction(instruction);
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		
		// test case 2
		Platform.getCpu().setAccumulator(0x7F);
		Platform.getCpu().setX(0x03);
		Platform.getCpuMemory().setMemoryFromHexAddress(0x0507, 0x80);
		TestUtils.performInstruction(instruction);
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
	}	
	
	@Test
	public void testAbsoluteY()
	{
		int instruction = 0xD9;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		Platform.getCpu().setAccumulator(0x01);
		Platform.getCpu().setY(0x01);
		Platform.getCpuMemory().setMemoryFromHexAddress(0x0202, 0xFF);
		TestUtils.performInstruction(instruction);
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		
		// test case 2
		Platform.getCpu().setAccumulator(0x7F);
		Platform.getCpu().setY(0x03);
		Platform.getCpuMemory().setMemoryFromHexAddress(0x0507, 0x80);
		TestUtils.performInstruction(instruction);
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
	}		
	
	@Test
	public void testIndexedIndirect()
	{
		int instruction = 0xC1;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		Platform.getCpu().setAccumulator(0x01);
		Platform.getCpu().setX(0x01);
		Platform.getCpuMemory().setMemoryFromHexAddress(0x0002, 0xFF);
		TestUtils.performInstruction(instruction);
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		
		// test case 2
		Platform.getCpu().setAccumulator(0x7F);
		Platform.getCpu().setX(0x03);
		Platform.getCpuMemory().setMemoryFromHexAddress(0x0006, 0x80);
		TestUtils.performInstruction(instruction);
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
	}		
	
	@Test
	public void testIndirectIndexed()
	{
		int instruction = 0xD1;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		Platform.getCpu().setAccumulator(0x01);
		Platform.getCpu().setY(0x01);
		Platform.getCpuMemory().setMemoryFromHexAddress(0x0202, 0xFF);
		TestUtils.performInstruction(instruction);
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		
		// test case 2
		Platform.getCpu().setAccumulator(0x7F);
		Platform.getCpu().setY(0x03);
		Platform.getCpuMemory().setMemoryFromHexAddress(0x0406, 0x80);
		TestUtils.performInstruction(instruction);
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
	}		
	
	@Test
	public void compareXImmediate()
	{
		int instruction = 0xE0;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		Platform.getCpu().setX(0x01);
		Platform.getCpuMemory().setMemoryFromHexAddress(0x8001, 0xFF);
		TestUtils.performInstruction(instruction);
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		
		// test case 2
		Platform.getCpu().setX(0x7F);
		Platform.getCpuMemory().setMemoryFromHexAddress(0x8003, 0x80);
		TestUtils.performInstruction(instruction);
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());		
	}
	
	@Test
	public void compareXZeroPage()
	{
		int instruction = 0xE4;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		Platform.getCpu().setX(0x01);
		Platform.getCpuMemory().setMemoryFromHexAddress(0x0001, 0xFF);
		TestUtils.performInstruction(instruction);
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		
		// test case 2
		Platform.getCpu().setX(0x7F);
		Platform.getCpuMemory().setMemoryFromHexAddress(0x0003, 0x80);
		TestUtils.performInstruction(instruction);
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());		
	}
	
	@Test
	public void compareXAbsolute()
	{
		int instruction = 0xEC;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		Platform.getCpu().setX(0x01);
		Platform.getCpuMemory().setMemoryFromHexAddress(0x0201, 0xFF);
		TestUtils.performInstruction(instruction);
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		
		// test case 2
		Platform.getCpu().setX(0x7F);
		Platform.getCpuMemory().setMemoryFromHexAddress(0x0504, 0x80);
		TestUtils.performInstruction(instruction);
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());		
	}
}
