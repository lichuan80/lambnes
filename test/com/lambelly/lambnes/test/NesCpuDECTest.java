package com.lambelly.lambnes.test;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.log4j.*;

import com.lambelly.lambnes.platform.*;
import com.lambelly.lambnes.platform.cpu.NesCpu;
import com.lambelly.lambnes.test.utils.TestUtils;

public class NesCpuDECTest
{
	private Logger logger = Logger.getLogger(NesCpuDECTest.class);
	
	@Before
	public void setUp() throws Exception
	{
		TestUtils.createTestPlatform();
	}

	@Test
	public void testDEX()
	{
		// test 1
		int instruction = 0xCA;
		Platform.getCpu().setX(0xCC);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0xCB, Platform.getCpu().getX());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test 2
		Platform.getCpu().setX(0x01);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0x00, Platform.getCpu().getX());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test3 
		Platform.getCpu().setX(0x00);
		TestUtils.performInstruction(instruction);
		assertEquals(0xFF, Platform.getCpu().getX());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
	}
	
	@Test
	public void testDEY()
	{
		// test 1
		int instruction = 0x88;
		Platform.getCpu().setY(0xCC);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0xCB, Platform.getCpu().getY());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test 2
		Platform.getCpu().setY(0x01);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0x00, Platform.getCpu().getY());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isZero());		

		// test 3
		Platform.getCpu().setY(0x00);
		TestUtils.performInstruction(instruction);
		assertEquals(0xFF, Platform.getCpu().getY());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
	}
	
	@Test
	public void testDECZeroPage()
	{
		// test 1
		int instruction = 0xC6;
		TestUtils.setMemory(0x01,0xCC);
		TestUtils.performInstruction(instruction);	
		
		assertEquals(0xCB, Platform.getCpuMemory().getMemoryFromHexAddress(0x0001));
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test 2
		TestUtils.setMemory(0x03, 0x01);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0x00, Platform.getCpuMemory().getMemoryFromHexAddress(0x0003));
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isZero());		

		// test 3
		TestUtils.setMemory(0x05, 0x00);
		TestUtils.performInstruction(instruction);
		assertEquals(0xFF, Platform.getCpuMemory().getMemoryFromHexAddress(0x0005));
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());	
	}
	
	@Test
	public void testDECZeroPageX()
	{
		// test 1
		int instruction = 0xD6;
		Platform.getCpu().setX(0xC0);
		TestUtils.setMemory(0xC1,0xCC);
		TestUtils.performInstruction(instruction);	
		
		assertEquals(0xCB, Platform.getCpuMemory().getMemoryFromHexAddress(0x00C1));
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		logger.debug("test2");
		// test 2
		Platform.getCpu().setX(0xC0);
		TestUtils.setMemory(0x8003, 0x01);
		TestUtils.setMemory(0xC1, 0x01);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0x00, Platform.getCpuMemory().getMemoryFromHexAddress(0x00C1));
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isZero());		

		// test 3
		Platform.getCpu().setX(0x43);
		TestUtils.setMemory(0x48, 0x00);
		TestUtils.performInstruction(instruction);
		assertEquals(0xFF, Platform.getCpuMemory().getMemoryFromHexAddress(0x0048));
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());	
	}	
	
	@Test
	public void testAbsolute()
	{
		// test 1
		int instruction = 0xCE;
		Platform.getCpu().setX(0xC0);
		TestUtils.setMemory(0x0201,0xCC);
		TestUtils.performInstruction(instruction);	
		
		assertEquals(0xCB, Platform.getCpuMemory().getMemoryFromHexAddress(0x0201));
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		logger.debug("test2");
		// test 2
		Platform.getCpu().setX(0xC0);
		TestUtils.setMemory(0x0504, 0x01);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0x00, Platform.getCpuMemory().getMemoryFromHexAddress(0x0504));
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isZero());		
	
		// test 3
		TestUtils.setMemory(0x0807, 0x00);
		TestUtils.performInstruction(instruction);
		assertEquals(0xFF, Platform.getCpuMemory().getMemoryFromHexAddress(0x0807));
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
	}
	
	@Test
	public void testAbsoluteX()
	{
		// test 1
		int instruction = 0xDE;
		Platform.getCpu().setX(0xC0);
		TestUtils.setMemory(0x02C1,0xCC);
		TestUtils.performInstruction(instruction);	
		
		assertEquals(0xCB, Platform.getCpuMemory().getMemoryFromHexAddress(0x02C1));
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		logger.debug("test2");
		// test 2
		Platform.getCpu().setX(0xC0);
		TestUtils.setMemory(0x05C4, 0x01);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0x00, Platform.getCpuMemory().getMemoryFromHexAddress(0x05C4));
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test 3
		Platform.getCpu().setX(0xA0);
		TestUtils.setMemory(0x8A7, 0x00);
		TestUtils.performInstruction(instruction);
		assertEquals(0xFF, Platform.getCpuMemory().getMemoryFromHexAddress(0x08A7));
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());	
	}	
}
