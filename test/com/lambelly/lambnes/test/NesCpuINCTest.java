package com.lambelly.lambnes.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.log4j.*;

import com.lambelly.lambnes.platform.*;
import com.lambelly.lambnes.platform.cpu.NesCpu;
import com.lambelly.lambnes.test.utils.TestUtils;

public class NesCpuINCTest
{
	private Logger logger = Logger.getLogger(NesCpuINCTest.class);
	
	@Before
	public void setUp() throws Exception
	{
		TestUtils.createTestPlatform();
	}

	@Test
	public void testINX()
	{
		// test 1
		int instruction = 0xE8;
		Platform.getCpu().setX(0xCC);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0xCD, Platform.getCpu().getX());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test 2
		Platform.getCpu().setX(0xFF);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0x00, Platform.getCpu().getX());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isZero());		
	}
	
	@Test
	public void testINY()
	{
		// test 1
		int instruction = 0xC8;
		Platform.getCpu().setY(0xCC);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0xCD, Platform.getCpu().getY());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test 2
		Platform.getCpu().setY(0xFF);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0x00, Platform.getCpu().getY());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isZero());		
	}
	
	@Test
	public void testINCZeroPage()
	{
		// test 1
		int instruction = 0xE6;
		TestUtils.setMemory(0x01,0xCC);
		TestUtils.performInstruction(instruction);	
		
		assertEquals(0xCD, Platform.getCpuMemory().getMemoryFromHexAddress(0x0001));
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		// test 2
		TestUtils.setMemory(0x03, 0xFF);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0x00, Platform.getCpuMemory().getMemoryFromHexAddress(0x0003));
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isZero());		
	}
	
	@Test
	public void testINCZeroPageX()
	{
		// test 1
		int instruction = 0xF6;
		Platform.getCpu().setX(0xC0);
		TestUtils.setMemory(0xC1,0xCD);
		TestUtils.performInstruction(instruction);	
		
		assertEquals(0xCE, Platform.getCpuMemory().getMemoryFromHexAddress(0x00C1));
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		logger.debug("test2");
		// test 2
		Platform.getCpu().setX(0xC0);
		TestUtils.setMemory(0x8003, 0x01);
		TestUtils.setMemory(0xC1, 0xFF);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0x00, Platform.getCpuMemory().getMemoryFromHexAddress(0x00C1));
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isZero());		
	}	
	
	@Test
	public void testAbsolute()
	{
		// test 1
		int instruction = 0xEE;
		Platform.getCpu().setX(0xC0);
		TestUtils.setMemory(0x8001,0xCC);
		TestUtils.performInstruction(instruction);	
		
		assertEquals(0xCD, Platform.getCpuMemory().getMemoryFromHexAddress(0x02CC));
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		logger.debug("test2");
		// test 2
		Platform.getCpu().setX(0xC0);
		TestUtils.setMemory(0x0504, 0xFF);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0x00, Platform.getCpuMemory().getMemoryFromHexAddress(0x0504));
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isZero());		
	}
	
	@Test
	public void testAbsoluteX()
	{
		// test 1
		int instruction = 0xFE;
		Platform.getCpu().setX(0xC0);
		TestUtils.setMemory(0x02C1,0xCD);
		TestUtils.performInstruction(instruction);	
		
		assertEquals(0xCE, Platform.getCpuMemory().getMemoryFromHexAddress(0x02C1));
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		
		logger.debug("test2");
		// test 2
		Platform.getCpu().setX(0xC0);
		TestUtils.setMemory(0x05C4, 0xFF);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0x00, Platform.getCpuMemory().getMemoryFromHexAddress(0x05C4));
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isZero());		
	}	
}
