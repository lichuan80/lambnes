package com.lambelly.lambnes.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.log4j.*;

import com.lambelly.lambnes.platform.*;
import com.lambelly.lambnes.platform.cpu.NesCpu;
import com.lambelly.lambnes.test.utils.TestUtils;

public class NesCpuTAXTest
{
	private Logger logger = Logger.getLogger(NesCpuTAXTest.class);
	
	@Before
	public void setUp() throws Exception
	{
		TestUtils.createTestPlatform();
	}
	
	@Test
	public void testTAX()
	{
		int instruction = 0xAA;
		Platform.getCpu().setAccumulator(0xDF);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0xDF, Platform.getCpu().getX());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
	}
	
	@Test
	public void testTAY()
	{
		int instruction = 0xA8;
		Platform.getCpu().setAccumulator(0x1E);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0x1E, Platform.getCpu().getY());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
	}
	
	@Test
	public void testTSX()
	{
		int instruction = 0xBA;
		Platform.getCpuMemory().setStackPointer(0x1E);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0x1E, Platform.getCpu().getX());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
	}	
	
	@Test
	public void testTXA()
	{
		int instruction = 0x8A;
		Platform.getCpu().setX(0xDF);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0xDF, Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
	}
	
	@Test
	public void testTXS()
	{
		int instruction = 0x9A;
		Platform.getCpu().setX(0xAE);
		Platform.getCpu().getFlags().setOverflow(true);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0xAE, Platform.getCpuMemory().getStackPointer());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isOverflow());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
	}		
	
	@Test
	public void testTYA()
	{
		int instruction = 0x98;
		Platform.getCpu().setY(0x1E);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0x1E, Platform.getCpu().getAccumulator());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
	}
}
