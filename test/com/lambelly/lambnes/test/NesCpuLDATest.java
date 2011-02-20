package com.lambelly.lambnes.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.lambelly.lambnes.platform.*;
import com.lambelly.lambnes.platform.cpu.NesCpu;
import com.lambelly.lambnes.test.utils.TestUtils;


public class NesCpuLDATest
{
	@Before
	public void setUp() throws Exception
	{
		TestUtils.createTestPlatform();
	}
	
	// LDA
	@Test
	public void testLdaImmediate()
	{
		int instruction = 0xA9;
		
		// test 1
		TestUtils.performInstruction(instruction);
		
		assertEquals(0x01,Platform.getCpu().getAccumulator());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test 2
		TestUtils.setMemory(0x8003, 0xCC);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0xCC,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());		
	}
	
	@Test
	public void testLdaZeroPage()
	{
		int instruction = 0xA5;
		
		// test 1
		TestUtils.performInstruction(instruction);
		
		assertEquals(0x01,Platform.getCpu().getAccumulator());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test 2
		TestUtils.setMemory(0x8003, 0xCC);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0xCC,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());		
	}
	
	@Test
	public void testLdaZeroPageX()
	{
		int instruction = 0xB5;
		
		// test 1
		Platform.getCpu().setX(0xA1);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0xA2,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test 2
		Platform.getCpu().setX(0x31);
		TestUtils.setMemory(0x8003, 0xCC);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0xFD,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());		
	}	
	
	@Test
	public void testLdaAbsolute()
	{
		int instruction = 0xAD;
		
		// test 1
		TestUtils.performInstruction(instruction);
		
		assertEquals(0x0001,Platform.getCpu().getAccumulator());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test 2
		TestUtils.performInstruction(instruction);
		
		assertEquals(0x0004,Platform.getCpu().getAccumulator());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());		
	}	
	
	public void testLdaAbsoluteX()
	{
		int instruction = 0xB9;
		
		// test 1
		Platform.getCpu().setX(0xA1);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0x00A2,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test 2
		TestUtils.performInstruction(instruction);
		Platform.getCpu().setX(0x1F);
		
		assertEquals(0x0023,Platform.getCpu().getAccumulator());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());		
	}	
	
	@Test
	public void testLdaIndexedIndirect()
	{
		int instruction = 0xA1;
		
		// test 1
		Platform.getCpu().setX(0xA1);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0x00A2,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test 2
		Platform.getCpu().setX(0x1F);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0x002,Platform.getCpu().getAccumulator());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());		
	}	
	
	@Test
	public void testLdaIndirectIndexed()
	{
		int instruction = 0xB1;
		
		// test 1
		Platform.getCpu().setY(0x01);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0x0002,Platform.getCpu().getAccumulator());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// TODO: Not sure why this has this value.
		// test 2
		Platform.getCpu().setY(0x1F);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0x022,Platform.getCpu().getAccumulator());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());		
	}	
	
	// LDX
	@Test
	public void testLdxImmediate()
	{
		int instruction = 0xA2;
		
		// test 1
		TestUtils.performInstruction(instruction);
		
		assertEquals(0x1,Platform.getCpu().getX());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test 2
		TestUtils.setMemory(0x8003, 0xCE);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0xCE,Platform.getCpu().getX());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());		
	}	
	
	@Test
	public void testLdxZeroPage()
	{
		int instruction = 0xA6;
		
		// test 1
		TestUtils.performInstruction(instruction);
		
		assertEquals(0x1,Platform.getCpu().getX());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test 2
		TestUtils.setMemory(0x8003, 0xCE);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0xCE,Platform.getCpu().getX());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());		
	}
	
	@Test
	public void testLdxZeroPageY()
	{
		int instruction = 0xB6;
		
		// test 1
		Platform.getCpu().setY(0xA);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0xB,Platform.getCpu().getX());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test 2
		TestUtils.setMemory(0x8003, 0xCE);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0xD8,Platform.getCpu().getX());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());		
	}
	
	@Test
	public void testLdxAbsolute()
	{
		int instruction = 0xAE;
		
		// test 1
		TestUtils.performInstruction(instruction);
		
		assertEquals(0x1,Platform.getCpu().getX());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test 2
		TestUtils.setMemory(0x8003, 0xCE);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0x04,Platform.getCpu().getX());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());		
	}	
	
	@Test
	public void testLdxAbsoluteY()
	{
		int instruction = 0xBE;
		
		// test 1
		Platform.getCpu().setY(0xA1);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0xA2,Platform.getCpu().getX());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test 2
		TestUtils.setMemory(0x8003, 0xCE);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0xA5,Platform.getCpu().getX());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());		
	}		
	
	// LDY
	@Test
	public void testLdyImmediate()
	{
		int instruction = 0xA0;
		
		// test 1
		TestUtils.performInstruction(instruction);
		
		assertEquals(0x1,Platform.getCpu().getY());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test 2
		TestUtils.setMemory(0x8003, 0xCE);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0xCE,Platform.getCpu().getY());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());		
	}	
	
	@Test
	public void testLdyZeroPage()
	{
		int instruction = 0xA4;
		
		// test 1
		TestUtils.performInstruction(instruction);
		
		assertEquals(0x1,Platform.getCpu().getY());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test 2
		TestUtils.setMemory(0x8003, 0xCE);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0xCE,Platform.getCpu().getY());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());		
	}
	
	@Test
	public void testLdyZeroPageY()
	{
		int instruction = 0xB4;
		
		// test 1
		Platform.getCpu().setX(0xA);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0xB,Platform.getCpu().getY());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test 2
		TestUtils.setMemory(0x8003, 0xCE);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0xD8,Platform.getCpu().getY());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());		
	}
	
	@Test
	public void testLdyAbsolute()
	{
		int instruction = 0xAC;
		
		// test 1
		TestUtils.performInstruction(instruction);
		
		assertEquals(0x1,Platform.getCpu().getY());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test 2
		TestUtils.setMemory(0x8003, 0xCE);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0x04,Platform.getCpu().getY());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());		
	}	
	
	@Test
	public void testLdyAbsoluteX()
	{
		int instruction = 0xBC;
		
		// test 1
		Platform.getCpu().setX(0xA1);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0xA2,Platform.getCpu().getY());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test 2
		TestUtils.setMemory(0x8003, 0xCE);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0xA5,Platform.getCpu().getY());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());		
	}		
}
