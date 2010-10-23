package com.lambelly.lambnes.test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.log4j.*;

import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.platform.cpu.NesCpu;
import com.lambelly.lambnes.test.utils.TestUtils;

public class NesCpuBITTest
{
	private Logger logger = Logger.getLogger(NesCpuBITTest.class);
	
	@Before
	public void setUp() throws Exception
	{
		TestUtils.createTestPlatform();
	}
	
	@Test
	public void testZeroPage()
	{
		int instruction = 0x24;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		TestUtils.performInstruction(instruction, 0xCF);
		logger.debug("accumulator: " + Integer.toHexString(Platform.getCpu().getAccumulator()));		
		assertEquals(0xCF,Platform.getCpu().getAccumulator());
		assertEquals(0x8002,Platform.getCpuMemory().getProgramCounter());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isOverflow());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test case 2 
		TestUtils.performInstruction(instruction, 0x00);
		logger.debug("accumulator: " + Platform.getCpu().getAccumulator());		
		logger.debug("bits: " + Integer.toBinaryString(Platform.getCpu().getAccumulator()));
		assertEquals(0x00,Platform.getCpu().getAccumulator());
		assertEquals(0x8004,Platform.getCpuMemory().getProgramCounter());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isZero());	
		
		// test case 3
		TestUtils.performInstruction(instruction, 0xCC);
		logger.debug("accumulator: " + Platform.getCpu().getAccumulator());		
		logger.debug("bits: " + Integer.toBinaryString(Platform.getCpu().getAccumulator()));
		assertTrue(Platform.getCpu().getAccumulator() != 194);
		assertEquals(0xCC,Platform.getCpu().getAccumulator());
		assertEquals(0x8006,Platform.getCpuMemory().getProgramCounter());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
	}
	
	@Test
	public void testAbsolute()
	{
		int instruction = 0x2C;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		TestUtils.performInstruction(instruction, 0xCF);
		logger.debug("accumulator: " + Integer.toHexString(Platform.getCpu().getAccumulator()));		
		assertEquals(0xCF,Platform.getCpu().getAccumulator());
		assertEquals(0x8003,Platform.getCpuMemory().getProgramCounter());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isOverflow());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test case 2 
		TestUtils.performInstruction(instruction, 0x00);
		logger.debug("accumulator: " + Platform.getCpu().getAccumulator());		
		logger.debug("bits: " + Integer.toBinaryString(Platform.getCpu().getAccumulator()));
		assertEquals(0x00,Platform.getCpu().getAccumulator());
		assertEquals(0x8006,Platform.getCpuMemory().getProgramCounter());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isZero());	
		
		// test case 3
		TestUtils.performInstruction(instruction, 0xCC);
		logger.debug("accumulator: " + Platform.getCpu().getAccumulator());		
		logger.debug("bits: " + Integer.toBinaryString(Platform.getCpu().getAccumulator()));
		assertTrue(Platform.getCpu().getAccumulator() != 194);
		assertEquals(0xCC,Platform.getCpu().getAccumulator());
		assertEquals(0x8009,Platform.getCpuMemory().getProgramCounter());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
	}	
}
