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

public class NesCpuRORTest
{
	private Logger logger = Logger.getLogger(NesCpuRORTest.class);
	
	@Before
	public void setUp() throws Exception
	{
		TestUtils.createTestPlatform();
	}
	
	@Test
	public void testAccumulator()
	{
		int instruction = 0x6A;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		TestUtils.performInstruction(instruction, 0xFF);
		logger.debug("accumulator: " + Integer.toHexString(Platform.getCpu().getAccumulator()));		
		assertEquals(0x7f,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test case 2 
		TestUtils.performInstruction(instruction, 0x15);
		logger.debug("accumulator: " + Platform.getCpu().getAccumulator());		
		logger.debug("bits: " + Integer.toBinaryString(Platform.getCpu().getAccumulator()));
		assertEquals(0xA,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());	
		
		// test case 3
		TestUtils.performInstruction(instruction, 0xCA);
		logger.debug("accumulator: " + Platform.getCpu().getAccumulator());		
		logger.debug("bits: " + Integer.toBinaryString(Platform.getCpu().getAccumulator()));
		assertTrue(Platform.getCpu().getAccumulator() != 194);
		assertEquals(0x65,Platform.getCpu().getAccumulator());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
	}
	
	@Test
	public void testZeroPage()
	{
		int instruction = 0x66;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		TestUtils.setMemory(0x01,0xFF);
		TestUtils.performInstruction(instruction);
		logger.debug("accumulator: " + Integer.toHexString(Platform.getCpu().getAccumulator()));		
		assertEquals(0x7f,Platform.getCpuMemory().getMemoryFromHexAddress(0x01));
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test case 2 
		TestUtils.setMemory(0x03,0x15);
		TestUtils.performInstruction(instruction);
		logger.debug("accumulator: " + Platform.getCpu().getAccumulator());		
		logger.debug("bits: " + Integer.toBinaryString(Platform.getCpu().getAccumulator()));
		assertEquals(0xA,Platform.getCpuMemory().getMemoryFromHexAddress(0x03));
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());	
		
		// test case 3
		TestUtils.setMemory(0x05, 0xCA);
		TestUtils.performInstruction(instruction);
		logger.debug("accumulator: " + Platform.getCpu().getAccumulator());		
		logger.debug("bits: " + Integer.toBinaryString(Platform.getCpu().getAccumulator()));
		assertTrue(Platform.getCpuMemory().getMemoryFromHexAddress(0x05) != 194);
		assertEquals(0x65,Platform.getCpuMemory().getMemoryFromHexAddress(0x05));
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
	}	
	
	@Test
	public void testZeroPageXIndexed()
	{
		int instruction = 0x76;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		Platform.getCpu().setX(0x02);
		TestUtils.setMemory(0x03,0xFF);
		TestUtils.performInstruction(instruction);
		logger.debug("accumulator: " + Integer.toHexString(Platform.getCpu().getAccumulator()));		
		assertEquals(0x7f,Platform.getCpuMemory().getMemoryFromHexAddress(0x03));
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test case 2 
		Platform.getCpu().setX(0x03);
		TestUtils.setMemory(0x06,0x15);
		TestUtils.performInstruction(instruction);
		logger.debug("accumulator: " + Platform.getCpu().getAccumulator());		
		logger.debug("bits: " + Integer.toBinaryString(Platform.getCpu().getAccumulator()));
		assertEquals(0xA,Platform.getCpuMemory().getMemoryFromHexAddress(0x06));
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());	
		
		// test case 3
		Platform.getCpu().setX(0x04);
		TestUtils.setMemory(0x09, 0xCA);
		TestUtils.performInstruction(instruction);
		logger.debug("accumulator: " + Platform.getCpu().getAccumulator());		
		logger.debug("bits: " + Integer.toBinaryString(Platform.getCpu().getAccumulator()));
		assertTrue(Platform.getCpu().getAccumulator() != 194);
		assertEquals(0x65,Platform.getCpuMemory().getMemoryFromHexAddress(0x09));
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
	}	
	
	@Test
	public void testAbsolute()
	{
		int instruction = 0x6E;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		TestUtils.setMemory(0x8001, 0x03);
		TestUtils.setMemory(0x8002, 0x0);
		TestUtils.setMemory(0x03,0xFF);
		TestUtils.performInstruction(instruction);
		logger.debug("accumulator: " + Integer.toHexString(Platform.getCpu().getAccumulator()));		
		assertEquals(0x7f,Platform.getCpuMemory().getMemoryFromHexAddress(0x03));
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test case 2 
		TestUtils.setMemory(0x8004, 0x06);
		TestUtils.setMemory(0x8005, 0);
		TestUtils.setMemory(0x06,0x15);
		TestUtils.performInstruction(instruction);
		logger.debug("accumulator: " + Platform.getCpu().getAccumulator());		
		logger.debug("bits: " + Integer.toBinaryString(Platform.getCpu().getAccumulator()));
		assertEquals(0xA,Platform.getCpuMemory().getMemoryFromHexAddress(0x06));
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isCarry());
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
		assertEquals(0x65,Platform.getCpuMemory().getMemoryFromHexAddress(0x09));
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
	}		
	
	@Test
	public void testAbsoluteX()
	{
		int instruction = 0x7E;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		TestUtils.setMemory(0x8001, 0x03);
		TestUtils.setMemory(0x8002, 0x0);
		Platform.getCpu().setX(0x01);
		TestUtils.setMemory(0x04,0xFF);
		TestUtils.performInstruction(instruction);
		logger.debug("accumulator: " + Integer.toHexString(Platform.getCpu().getAccumulator()));		
		assertEquals(0x7f,Platform.getCpuMemory().getMemoryFromHexAddress(0x04));
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test case 2 
		Platform.getCpu().setX(0x03);
		TestUtils.setMemory(0x8004, 0x06);
		TestUtils.setMemory(0x8005, 0);
		TestUtils.setMemory(0x09,0x15);
		TestUtils.performInstruction(instruction);
		logger.debug("accumulator: " + Platform.getCpu().getAccumulator());		
		logger.debug("bits: " + Integer.toBinaryString(Platform.getCpu().getAccumulator()));
		assertEquals(0xA,Platform.getCpuMemory().getMemoryFromHexAddress(0x09));
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isCarry());
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
		assertEquals(0x65,Platform.getCpuMemory().getMemoryFromHexAddress(0x0D));
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
	}		

}
