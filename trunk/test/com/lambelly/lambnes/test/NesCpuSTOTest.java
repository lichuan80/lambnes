package com.lambelly.lambnes.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.log4j.*;

import com.lambelly.lambnes.platform.*;
import com.lambelly.lambnes.test.utils.TestUtils;

public class NesCpuSTOTest
{
	private Logger logger = Logger.getLogger(NesCpuSTOTest.class);
	
	@Before
	public void setUp() throws Exception
	{
		TestUtils.createTestPlatform();
	}
	
	@Test
	public void testSTAZeroPage()
	{
		int instruction = 0x85;
		Platform.getCpu().setAccumulator(0xDF);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0xDF, Platform.getCpuMemory().getMemoryFromHexAddress(0x01));
	}
	
	@Test
	public void testSTAZeroPageX()
	{
		int instruction = 0x95;
		Platform.getCpu().setAccumulator(0xDF);
		Platform.getCpu().setX(0x1A);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0xDF, Platform.getCpuMemory().getMemoryFromHexAddress(0x1B));
	}
	
	@Test
	public void testSTAAbsolute()
	{
		int instruction = 0x8D;
		Platform.getCpu().setAccumulator(0xDF);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0xDF, Platform.getCpuMemory().getMemoryFromHexAddress(0x0201));
	}
	
	@Test
	public void testSTAAbsoluteX()
	{
		int instruction = 0x9D;
		Platform.getCpu().setX(0x1A);
		Platform.getCpu().setAccumulator(0xDF);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0xDF, Platform.getCpuMemory().getMemoryFromHexAddress(0x021B));
	}	
	
	@Test
	public void testSTAAbsoluteY()
	{
		int instruction = 0x99;
		Platform.getCpu().setY(0x1A);
		Platform.getCpu().setAccumulator(0xDF);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0xDF, Platform.getCpuMemory().getMemoryFromHexAddress(0x021B));
	}		
	
	@Test
	public void testSTXZeroPage()
	{
		int instruction = 0x86;
		Platform.getCpu().setX(0x1A);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0x1A, Platform.getCpuMemory().getMemoryFromHexAddress(0x01));
	}	
	
	@Test
	public void testSTXZeroPageY()
	{
		int instruction = 0x96;
		Platform.getCpu().setX(0x1A);
		Platform.getCpu().setY(0x1A);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0x1A, Platform.getCpuMemory().getMemoryFromHexAddress(0x1B));
	}	
	
	@Test
	public void testSTXAbsolute()
	{
		int instruction = 0x8E;
		Platform.getCpu().setX(0x1A);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0x1A, Platform.getCpuMemory().getMemoryFromHexAddress(0x201));
	}	
	
	@Test
	public void testSTYZeroPage()
	{
		int instruction = 0x84;
		Platform.getCpu().setY(0x1A);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0x1A, Platform.getCpuMemory().getMemoryFromHexAddress(0x01));
	}	
	
	@Test
	public void testSTYZeroPageX()
	{
		int instruction = 0x94;
		Platform.getCpu().setX(0x1A);
		Platform.getCpu().setY(0x1A);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0x1A, Platform.getCpuMemory().getMemoryFromHexAddress(0x1B));
	}	
	
	@Test
	public void testSTYAbsolute()
	{
		int instruction = 0x8C;
		Platform.getCpu().setY(0x1A);
		TestUtils.performInstruction(instruction);
		
		assertEquals(0x1A, Platform.getCpuMemory().getMemoryFromHexAddress(0x201));
	}	
}
