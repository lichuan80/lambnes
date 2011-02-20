package com.lambelly.lambnes.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.log4j.*;

import com.lambelly.lambnes.platform.*;
import com.lambelly.lambnes.platform.cpu.NesCpu;
import com.lambelly.lambnes.test.utils.TestUtils;

public class NesCpuLSRTest
{
	private Logger logger = Logger.getLogger(NesCpuLSRTest.class);
	
	@Before
	public void setUp() throws Exception
	{
		TestUtils.createTestPlatform();
	}
	
	@Test
	public void LSRTest()
	{
		int value = 0xFF;
		logger.debug("0xFF: " + Integer.toBinaryString(value));
		value = value >> 1;
		logger.debug("after shift: " + Integer.toBinaryString(value));
	}

	@Test
	public void testAccumulator()
	{
		int instruction = 0x4A;
		
		// test 1
		Platform.getCpu().setAccumulator(0xE4);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0x72, Platform.getCpu().getAccumulator());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());

	    // test 2
		Platform.getCpu().setAccumulator(0x9B);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0x4D, Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());		
	}
	
	@Test
	public void testZeroPage()
	{
		int instruction = 0x46;
		
		// test 1
		TestUtils.performInstruction(instruction);
		
		assertEquals(0, Platform.getCpuMemory().getMemoryFromHexAddress(0x01));
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());

	    // test 2
		TestUtils.performInstruction(instruction);
		
		assertEquals(1, Platform.getCpuMemory().getMemoryFromHexAddress(0x03));
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());		
	}	
	
	@Test
	public void testZeroPageX()
	{
		int instruction = 0x56;
		
		// test 1
		Platform.getCpu().setX(0xF);
		TestUtils.performInstruction(instruction);
		
		assertEquals(8, Platform.getCpuMemory().getMemoryFromHexAddress(0x10));
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());

	    // test 2
		TestUtils.performInstruction(instruction);
		
		assertEquals(9, Platform.getCpuMemory().getMemoryFromHexAddress(0x12));
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());		
	}	
	
	@Test
	public void testAbsolute()
	{
		int instruction = 0x4E;
		
		// test 1
		TestUtils.performInstruction(instruction);
		
		assertEquals(0, Platform.getCpuMemory().getMemoryFromHexAddress(0x0201));
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());

	    // test 2
		TestUtils.performInstruction(instruction);
		
		assertEquals(2, Platform.getCpuMemory().getMemoryFromHexAddress(0x0504));
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());		
	}		
	
	@Test
	public void testAbsoluteX()
	{
		int instruction = 0x5E;
		
		// test 1
		Platform.getCpu().setX(0xA1);
		TestUtils.performInstruction(instruction);
		
		assertEquals(81, Platform.getCpuMemory().getMemoryFromHexAddress(0x02A2));
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());

	    // test 2
		TestUtils.performInstruction(instruction);
		
		assertEquals(82, Platform.getCpuMemory().getMemoryFromHexAddress(0x05A5));
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());		
	}		
}
