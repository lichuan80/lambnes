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

public class NesCpuADCTest
{
	private Logger logger = Logger.getLogger(NesCpuADCTest.class);
	
	@Before
	public void setUp()
	{
		TestUtils.createTestPlatform();
	}
	
	@Test
	public void Immediate()
	{
		int instruction = 0x69;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		TestUtils.performInstruction(instruction, 0xFF);
		logger.debug("accumulator: " + Integer.toHexString(Platform.getCpu().getAccumulator()));		
		assertEquals(0,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test case 2
		TestUtils.performInstruction(instruction, 0xAE);
		logger.debug("accumulator: " + Integer.toHexString(Platform.getCpu().getAccumulator()));		
		assertEquals(0xB2,Platform.getCpu().getAccumulator());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());	
		
		// test case 3
		TestUtils.performInstruction(instruction, 0xA);
		logger.debug("accumulator: " + Integer.toHexString(Platform.getCpu().getAccumulator()));		
		assertEquals(0xF,Platform.getCpu().getAccumulator());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
	}	
	
	@Test
	public void ZeroPage()
	{
		int instruction = 0x65;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		TestUtils.performInstruction(instruction, 0xFF);
		logger.debug("accumulator: " + Integer.toHexString(Platform.getCpu().getAccumulator()));		
		assertEquals(0,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test case 2
		TestUtils.performInstruction(instruction, 0xAE);
		logger.debug("accumulator: " + Integer.toHexString(Platform.getCpu().getAccumulator()));		
		assertEquals(0xB2,Platform.getCpu().getAccumulator());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());	
		
		// test case 3
		TestUtils.performInstruction(instruction, 0xA);
		logger.debug("accumulator: " + Integer.toHexString(Platform.getCpu().getAccumulator()));		
		assertEquals(0xF,Platform.getCpu().getAccumulator());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
	}	
	
	@Test
	public void ZeroPageX()
	{
		int instruction = 0x75;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		TestUtils.performInstruction(instruction, 0xFF);
		logger.debug("accumulator: " + Integer.toHexString(Platform.getCpu().getAccumulator()));		
		assertEquals(0,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test case 2
		TestUtils.performInstruction(instruction, 0xAE);
		logger.debug("accumulator: " + Integer.toHexString(Platform.getCpu().getAccumulator()));		
		assertEquals(0xB2,Platform.getCpu().getAccumulator());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());	
		
		// test case 3
		TestUtils.performInstruction(instruction, 0xA);
		logger.debug("accumulator: " + Integer.toHexString(Platform.getCpu().getAccumulator()));		
		assertEquals(0xF,Platform.getCpu().getAccumulator());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
	}		
	
	@Test
	public void Absolute()
	{
		int instruction = 0x6D;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		TestUtils.performInstruction(instruction, 0xFF);
		logger.debug("accumulator: " + Integer.toHexString(Platform.getCpu().getAccumulator()));		
		assertEquals(0,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test case 2
		TestUtils.performInstruction(instruction, 0xAE);
		logger.debug("accumulator: " + Integer.toHexString(Platform.getCpu().getAccumulator()));		
		assertEquals(0xB3,Platform.getCpu().getAccumulator());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());	
		
		// test case 3
		TestUtils.performInstruction(instruction, 0xA);
		logger.debug("accumulator: " + Integer.toHexString(Platform.getCpu().getAccumulator()));		
		assertEquals(0x11,Platform.getCpu().getAccumulator());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
	}
	
	@Test
	public void AbsoluteX()
	{
		int instruction = 0x7D;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		Platform.getCpu().setX(0xA);
		
		// test case 1
		TestUtils.performInstruction(instruction, 0xFF);
		logger.debug("accumulator: " + Integer.toHexString(Platform.getCpu().getAccumulator()));		
		assertEquals(0xA,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test case 2
		TestUtils.performInstruction(instruction, 0xAE);
		logger.debug("accumulator: " + Integer.toHexString(Platform.getCpu().getAccumulator()));		
		assertEquals(0xBD,Platform.getCpu().getAccumulator());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());	
		
		// test case 3
		TestUtils.performInstruction(instruction, 0xA);
		logger.debug("accumulator: " + Integer.toHexString(Platform.getCpu().getAccumulator()));		
		assertEquals(0x1B,Platform.getCpu().getAccumulator());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
	}			
	
	@Test
	public void ZeroPageY()
	{
		int instruction = 0x79;

		Platform.getCpu().setY(0xA);
		
		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		TestUtils.performInstruction(instruction, 0xFF);
		logger.debug("accumulator: " + Integer.toHexString(Platform.getCpu().getAccumulator()));		
		assertEquals(0xA,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test case 2
		TestUtils.performInstruction(instruction, 0xAE);
		logger.debug("accumulator: " + Integer.toHexString(Platform.getCpu().getAccumulator()));		
		assertEquals(0xBD,Platform.getCpu().getAccumulator());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());	
		
		// test case 3
		TestUtils.performInstruction(instruction, 0xA);
		logger.debug("accumulator: " + Integer.toHexString(Platform.getCpu().getAccumulator()));		
		assertEquals(0x1B,Platform.getCpu().getAccumulator());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
	}			
	
	@Test
	public void IndexedIndirect()
	{
		int instruction = 0x61;

		Platform.getCpu().setX(0xA);
		
		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		TestUtils.performInstruction(instruction, 0xFF);
		logger.debug("accumulator: " + Integer.toHexString(Platform.getCpu().getAccumulator()));		
		assertEquals(0xA,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test case 2
		TestUtils.performInstruction(instruction, 0xAE);
		logger.debug("accumulator: " + Integer.toHexString(Platform.getCpu().getAccumulator()));		
		assertEquals(0xBC,Platform.getCpu().getAccumulator());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());	
		
		// test case 3
		TestUtils.performInstruction(instruction, 0xA);
		logger.debug("accumulator: " + Integer.toHexString(Platform.getCpu().getAccumulator()));		
		assertEquals(0x19,Platform.getCpu().getAccumulator());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
	}			
	
	@Test
	public void IndirectIndexed()
	{
		int instruction = 0x71;

		Platform.getCpu().setY(0xA);
		
		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		TestUtils.performInstruction(instruction, 0xFF);
		logger.debug("accumulator: " + Integer.toHexString(Platform.getCpu().getAccumulator()));		
		assertEquals(0xA,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test case 2
		TestUtils.performInstruction(instruction, 0xAE);
		logger.debug("accumulator: " + Integer.toHexString(Platform.getCpu().getAccumulator()));		
		assertEquals(0xBC,Platform.getCpu().getAccumulator());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());	
		
		// test case 3
		TestUtils.performInstruction(instruction, 0xA);
		logger.debug("accumulator: " + Integer.toHexString(Platform.getCpu().getAccumulator()));		
		assertEquals(0x19,Platform.getCpu().getAccumulator());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
	}			
}
