package com.lambelly.lambnes.test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.log4j.*;
import com.lambelly.lambnes.platform.*;

import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.platform.cpu.NesCpu;
import com.lambelly.lambnes.test.utils.TestUtils;

public class NesCpuASLTest
{
	private Logger logger = Logger.getLogger(NesCpuASLTest.class);
	
	@Before
	public void setUp()
	{
		TestUtils.createTestPlatform();
	}
	
	@Test
	public void testAccumulator()
	{
		int instruction = 0x0A;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		TestUtils.performInstruction(instruction, 0xFF);
		logger.debug("accumulator: " + Integer.toHexString(Platform.getCpu().getAccumulator()));		
		assertEquals(254,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test case 2 
		TestUtils.performInstruction(instruction, 0x15);
		logger.debug("accumulator: " + Platform.getCpu().getAccumulator());		
		logger.debug("bits: " + Integer.toBinaryString(Platform.getCpu().getAccumulator()));
		assertEquals(0x2A,Platform.getCpu().getAccumulator());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());	
		
		// test case 3
		TestUtils.performInstruction(instruction, 0xCA);
		logger.debug("accumulator: " + Platform.getCpu().getAccumulator());		
		logger.debug("bits: " + Integer.toBinaryString(Platform.getCpu().getAccumulator()));
		assertTrue(Platform.getCpu().getAccumulator() != 194);
		assertEquals(0x94,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
	}
	
	@Test
	public void testZeroPage()
	{
		int instruction = 0x06;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		TestUtils.setMemory(0x01,0xFF);
		TestUtils.performInstruction(instruction);
		logger.debug("accumulator: " + Integer.toHexString(Platform.getCpu().getAccumulator()));		
		assertEquals(254,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test case 2 
		TestUtils.setMemory(0x03,0x15);
		TestUtils.performInstruction(instruction);
		logger.debug("accumulator: " + Platform.getCpu().getAccumulator());		
		logger.debug("bits: " + Integer.toBinaryString(Platform.getCpu().getAccumulator()));
		assertEquals(0x2A,Platform.getCpu().getAccumulator());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());	
		
		// test case 3
		TestUtils.setMemory(0x05, 0xCA);
		TestUtils.performInstruction(instruction);
		logger.debug("accumulator: " + Platform.getCpu().getAccumulator());		
		logger.debug("bits: " + Integer.toBinaryString(Platform.getCpu().getAccumulator()));
		assertTrue(Platform.getCpu().getAccumulator() != 194);
		assertEquals(0x94,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
	}	
	
	@Test
	public void testZeroPageXIndexed()
	{
		int instruction = 0x16;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		Platform.getCpu().setX(0x02);
		TestUtils.setMemory(0x03,0xFF);
		TestUtils.performInstruction(instruction);
		logger.debug("accumulator: " + Integer.toHexString(Platform.getCpu().getAccumulator()));		
		assertEquals(254,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test case 2 
		Platform.getCpu().setX(0x03);
		TestUtils.setMemory(0x06,0x15);
		TestUtils.performInstruction(instruction);
		logger.debug("accumulator: " + Platform.getCpu().getAccumulator());		
		logger.debug("bits: " + Integer.toBinaryString(Platform.getCpu().getAccumulator()));
		assertEquals(0x2A,Platform.getCpu().getAccumulator());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());	
		
		// test case 3
		Platform.getCpu().setX(0x04);
		TestUtils.setMemory(0x09, 0xCA);
		TestUtils.performInstruction(instruction);
		logger.debug("accumulator: " + Platform.getCpu().getAccumulator());		
		logger.debug("bits: " + Integer.toBinaryString(Platform.getCpu().getAccumulator()));
		assertTrue(Platform.getCpu().getAccumulator() != 194);
		assertEquals(0x94,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
	}	
	
	@Test
	public void testAbsolute()
	{
		int instruction = 0x0E;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		TestUtils.setMemory(0x8001, 0x03);
		TestUtils.setMemory(0x8002, 0x0);
		TestUtils.setMemory(0x03,0xFF);
		TestUtils.performInstruction(instruction);
		logger.debug("accumulator: " + Integer.toHexString(Platform.getCpu().getAccumulator()));		
		assertEquals(254,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test case 2 
		TestUtils.setMemory(0x8004, 0x06);
		TestUtils.setMemory(0x8005, 0);
		TestUtils.setMemory(0x06,0x15);
		TestUtils.performInstruction(instruction);
		logger.debug("accumulator: " + Platform.getCpu().getAccumulator());		
		logger.debug("bits: " + Integer.toBinaryString(Platform.getCpu().getAccumulator()));
		assertEquals(0x2A,Platform.getCpu().getAccumulator());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());	
		
		// test case 3
		TestUtils.setMemory(0x8007, 0x09);
		TestUtils.setMemory(0x8008, 0x00);
		TestUtils.setMemory(0x09, 0xCA);
		TestUtils.performInstruction(instruction);
		logger.debug("accumulator: " + Platform.getCpu().getAccumulator());		
		logger.debug("bits: " + Integer.toBinaryString(Platform.getCpu().getAccumulator()));
		assertTrue(Platform.getCpu().getAccumulator() != 194);
		assertEquals(0x94,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
	}		
	
	@Test
	public void testAbsoluteX()
	{
		int instruction = 0x1E;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		TestUtils.setMemory(0x8001, 0x03);
		TestUtils.setMemory(0x8002, 0x0);
		Platform.getCpu().setX(0x01);
		TestUtils.setMemory(0x04,0xFF);
		TestUtils.performInstruction(instruction);
		logger.debug("accumulator: " + Integer.toHexString(Platform.getCpu().getAccumulator()));		
		assertEquals(254,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test case 2 
		Platform.getCpu().setX(0x03);
		TestUtils.setMemory(0x8004, 0x06);
		TestUtils.setMemory(0x8005, 0);
		TestUtils.setMemory(0x09,0x15);
		TestUtils.performInstruction(instruction);
		logger.debug("accumulator: " + Platform.getCpu().getAccumulator());		
		logger.debug("bits: " + Integer.toBinaryString(Platform.getCpu().getAccumulator()));
		assertEquals(0x2A,Platform.getCpu().getAccumulator());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());	
		
		// test case 3
		Platform.getCpu().setX(0x04);
		TestUtils.setMemory(0x8007, 0x09);
		TestUtils.setMemory(0x8008, 0x00);
		TestUtils.setMemory(0x0D, 0xCA);
		TestUtils.performInstruction(instruction);
		logger.debug("accumulator: " + Platform.getCpu().getAccumulator());		
		logger.debug("bits: " + Integer.toBinaryString(Platform.getCpu().getAccumulator()));
		assertTrue(Platform.getCpu().getAccumulator() != 194);
		assertEquals(0x94,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
	}			
}
