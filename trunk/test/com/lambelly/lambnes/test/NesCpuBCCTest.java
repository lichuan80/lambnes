package com.lambelly.lambnes.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.log4j.*;

import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.platform.cpu.NesCpu;
import com.lambelly.lambnes.test.utils.TestUtils;

public class NesCpuBCCTest
{
	private Logger logger = Logger.getLogger(NesCpuBCCTest.class);
	
	@Before
	public void setUp()throws Exception
	{
		TestUtils.createTestPlatform();
	}
	
	@Test
	public void testBCC()
	{
		int instruction = 0x90;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		((NesCpu)Platform.getCpu()).getFlags().setCarry(false);
		TestUtils.performInstruction(instruction);
		assertEquals(0x8003,Platform.getCpuMemory().getProgramCounter());

		// test case 2
		((NesCpu)Platform.getCpu()).getFlags().setCarry(true);
		TestUtils.performInstruction(instruction);
		assertEquals(0x8005,Platform.getCpuMemory().getProgramCounter());		
	}	
	
	@Test
	public void testBCS()
	{
		int instruction = 0xB0;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		((NesCpu)Platform.getCpu()).getFlags().setCarry(true);
		TestUtils.performInstruction(instruction);
		assertEquals(0x8003,Platform.getCpuMemory().getProgramCounter());

		// test case 2
		((NesCpu)Platform.getCpu()).getFlags().setCarry(false);
		TestUtils.performInstruction(instruction);
		assertEquals(0x8005,Platform.getCpuMemory().getProgramCounter());		
	}	
	
	@Test
	public void testBEQ()
	{
		int instruction = 0xF0;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		((NesCpu)Platform.getCpu()).getFlags().setZero(true);
		TestUtils.performInstruction(instruction);
		assertEquals(0x8003,Platform.getCpuMemory().getProgramCounter());

		// test case 2
		((NesCpu)Platform.getCpu()).getFlags().setZero(false);
		TestUtils.performInstruction(instruction);
		assertEquals(0x8005,Platform.getCpuMemory().getProgramCounter());		
	}	
	
	@Test
	public void testBMI()
	{
		int instruction = 0x30;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		((NesCpu)Platform.getCpu()).getFlags().setNegative(true);
		TestUtils.performInstruction(instruction);
		assertEquals(0x8003,Platform.getCpuMemory().getProgramCounter());

		// test case 2
		((NesCpu)Platform.getCpu()).getFlags().setNegative(false);
		TestUtils.performInstruction(instruction);
		assertEquals(0x8005,Platform.getCpuMemory().getProgramCounter());		
	}		
	
	@Test
	public void testBNE()
	{
		int instruction = 0xD0;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		((NesCpu)Platform.getCpu()).getFlags().setZero(false);
		TestUtils.performInstruction(instruction);
		assertEquals(0x8003,Platform.getCpuMemory().getProgramCounter());

		// test case 2
		((NesCpu)Platform.getCpu()).getFlags().setZero(true);
		TestUtils.performInstruction(instruction);
		assertEquals(0x8005,Platform.getCpuMemory().getProgramCounter());		
	}		
	
	@Test
	public void testBPL()
	{
		int instruction = 0x10;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		((NesCpu)Platform.getCpu()).getFlags().setNegative(false);
		TestUtils.performInstruction(instruction);
		assertEquals(0x8003,Platform.getCpuMemory().getProgramCounter());

		// test case 2
		((NesCpu)Platform.getCpu()).getFlags().setNegative(true);
		TestUtils.performInstruction(instruction);
		assertEquals(0x8005,Platform.getCpuMemory().getProgramCounter());		
	}	
	
	@Test
	public void testBVC()
	{
		int instruction = 0x50;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		((NesCpu)Platform.getCpu()).getFlags().setOverflow(false);
		TestUtils.performInstruction(instruction);
		assertEquals(0x8003,Platform.getCpuMemory().getProgramCounter());

		// test case 2
		((NesCpu)Platform.getCpu()).getFlags().setOverflow(true);
		TestUtils.performInstruction(instruction);
		assertEquals(0x8005,Platform.getCpuMemory().getProgramCounter());		
	}	
	
	@Test
	public void testBVS()
	{
		int instruction = 0x70;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		((NesCpu)Platform.getCpu()).getFlags().setOverflow(true);
		TestUtils.performInstruction(instruction);
		assertEquals(0x8003,Platform.getCpuMemory().getProgramCounter());

		// test case 2
		((NesCpu)Platform.getCpu()).getFlags().setOverflow(false);
		TestUtils.performInstruction(instruction);
		assertEquals(0x8005,Platform.getCpuMemory().getProgramCounter());		
	}		
}
