package com.lambelly.lambnes.test;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.log4j.*;

import com.lambelly.lambnes.platform.*;
import com.lambelly.lambnes.platform.cpu.NesCpu;
import com.lambelly.lambnes.test.utils.TestUtils;

public class NesCpuORATest
{
	@Before
	public void setUp() throws Exception
	{
		TestUtils.createTestPlatform();
	}
	
	@Test
	public void testImmediate()
	{
		// test 1
		int instruction = 0x09;
		Platform.getCpu().setAccumulator(0xFF);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0xFF,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test 2
		TestUtils.setMemory(0x8003, 0xFE);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0xFF,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());	
	}
	
	@Test
	public void testZeroPage()
	{
		// test 1
		int instruction = 0x05;
		Platform.getCpu().setAccumulator(0xCE);
		TestUtils.setMemory(0x8001, 0xA9);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0xEF,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test 2
		TestUtils.setMemory(0x03, 0xFE);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0xFF,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());			
	}
	
	@Test
	public void testZeroPageX()
	{
		// test 1
		int instruction = 0x15;
		Platform.getCpu().setAccumulator(0xCE);
		TestUtils.setMemory(0x8001, 0xA9);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0xEF,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test 2
		TestUtils.setMemory(0x03, 0xFE);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0xFF,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());		
	}
	
	@Test
	public void testAbsolute()
	{
		int instruction = 0x0D;
		
		// test 1
		Platform.getCpu().setAccumulator(0x4e);
		TestUtils.setMemory(0x0201, 0xbd);
		TestUtils.performInstruction(instruction);

		assertEquals(0xFF,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test 2
		Platform.getCpu().setAccumulator(0x1e);
		TestUtils.setMemory(0x0504, 0x36);
		TestUtils.performInstruction(instruction);

		assertEquals(0x3E,Platform.getCpu().getAccumulator());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());		
	}

	@Test
	public void testAbsoluteX()
	{
		int instruction = 0x1D;
		
		// test 1
		Platform.getCpu().setAccumulator(0x4e);
		Platform.getCpu().setX(0xA);
		TestUtils.setMemory(0x020B, 0xbd);
		TestUtils.performInstruction(instruction);

		assertEquals(0xFF,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test 2
		Platform.getCpu().setAccumulator(0x1e);
		Platform.getCpu().setX(0x9);
		TestUtils.setMemory(0x050D, 0x36);
		TestUtils.performInstruction(instruction);

		assertEquals(0x3E,Platform.getCpu().getAccumulator());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());		
	}
	
	@Test
	public void testAbsoluteY()
	{
		int instruction = 0x19;
		
		// test 1
		Platform.getCpu().setAccumulator(0x4e);
		Platform.getCpu().setY(0xA);
		TestUtils.setMemory(0x020B, 0xbd);
		TestUtils.performInstruction(instruction);

		assertEquals(0xFF,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test 2
		Platform.getCpu().setAccumulator(0x1e);
		Platform.getCpu().setY(0x9);
		TestUtils.setMemory(0x050D, 0x36);
		TestUtils.performInstruction(instruction);

		assertEquals(0x3E,Platform.getCpu().getAccumulator());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());		
	}	

	@Test
	public void testIndexedIndirect()
	{
		int instruction = 0x01;
		
		// test 1
		Platform.getCpu().setAccumulator(0x4e);
		Platform.getCpu().setX(0xA);
		TestUtils.setMemory(0x000B, 0xbd);
		TestUtils.performInstruction(instruction);

		assertEquals(0xFF,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test 2
		Platform.getCpu().setAccumulator(0x1e);
		Platform.getCpu().setX(0x9);
		TestUtils.setMemory(0x000C, 0x36);
		TestUtils.performInstruction(instruction);

		assertEquals(0x3E,Platform.getCpu().getAccumulator());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());		
	}
	
	@Test
	public void testIndirectIndexed()
	{
		int instruction = 0x11;
		
		// test 1
		Platform.getCpu().setAccumulator(0x4e);
		Platform.getCpu().setY(0xA);
		TestUtils.setMemory(0x020B, 0xbd);
		TestUtils.performInstruction(instruction);

		assertEquals(0xFF,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test 2
		Platform.getCpu().setAccumulator(0x1e);
		Platform.getCpu().setY(0x9);
		TestUtils.setMemory(0x040C, 0x36);
		TestUtils.performInstruction(instruction);

		assertEquals(0x3E,Platform.getCpu().getAccumulator());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());		
	}	
}
