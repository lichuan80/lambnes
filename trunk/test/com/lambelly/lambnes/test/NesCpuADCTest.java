package com.lambelly.lambnes.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.log4j.*;

import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.platform.cpu.NesCpu;
import com.lambelly.lambnes.test.utils.TestUtils;

public class NesCpuADCTest
{
	private Logger logger = Logger.getLogger(NesCpuADCTest.class);
	
	@Before
	public void setUp()throws Exception
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
	
	/* Test Cases: 
	 * 00 + 00 and C=0 gives 00 and N=0 V=0 Z=1 C=0 (simulate) 
	 * 79 + 00 and C=1 gives 80 and N=1 V=1 Z=0 C=0 (simulate) 
	 * 24 + 56 and C=0 gives 80 and N=1 V=1 Z=0 C=0 (simulate) 
	 * 93 + 82 and C=0 gives 75 and N=0 V=1 Z=0 C=1 (simulate) 
	 * 89 + 76 and C=0 gives 55 and N=0 V=0 Z=0 C=1 (simulate) 
	 * 89 + 76 and C=1 gives 56 and N=0 V=0 Z=1 C=1 (simulate) 
	 * 80 + f0 and C=0 gives d0 and N=0 V=1 Z=0 C=1 (simulate) 
	 * 80 + fa and C=0 gives e0 and N=1 V=0 Z=0 C=1 (simulate) 
	 * 2f + 4f and C=0 gives 74 and N=0 V=0 Z=0 C=0 (simulate) 
	 * 6f + 00 and C=1 gives 76 and N=0 V=0 Z=0 C=0 (simulate) 
	 * 
	 * */
	
	
	@Test
	public void test1()
	{
		int instruction = 0x69;
		
		// test case 1
		Platform.getCpu().getFlags().resetFlags();
		Platform.getCpuMemory().setMemoryFromHexAddress(0x8001, 0);
		TestUtils.performInstruction(instruction, 0x0);
		logger.debug("accumulator: " + Integer.toHexString(Platform.getCpu().getAccumulator()));		
		assertEquals(0,Platform.getCpu().getAccumulator());

		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isOverflow());
	}
	
	@Test
	public void test2()
	{
		int instruction = 0x69;
		
		// test case 1
		Platform.getCpu().getFlags().resetFlags();
		Platform.getCpu().getFlags().setCarry(true);
		Platform.getCpuMemory().setMemoryFromHexAddress(0x8001, 0x7F);
		TestUtils.performInstruction(instruction, 0x0);
		logger.debug("accumulator: " + Integer.toHexString(Platform.getCpu().getAccumulator()));		
		assertEquals(0x80,Platform.getCpu().getAccumulator());

		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isOverflow());
	}	
	
	@Test
	public void test3()
	{
		int instruction = 0x69;
		
		// test case 1
		Platform.getCpu().getFlags().resetFlags();
		Platform.getCpu().getFlags().setCarry(true);
		Platform.getCpuMemory().setMemoryFromHexAddress(0x8001, 0xFF);
		TestUtils.performInstruction(instruction, 0xFF);
		logger.debug("accumulator: " + Integer.toHexString(Platform.getCpu().getAccumulator()));		
		assertEquals(0xFF,Platform.getCpu().getAccumulator());

		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isOverflow());
	}	
}
