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
import com.lambelly.lambnes.util.BitUtils;

public class NesCpuPHATest
{
	private Logger logger = Logger.getLogger(NesCpuPHATest.class);
	
	@Before
	public void setUp() throws Exception
	{
		TestUtils.createTestPlatform();
	}
	
	@Test
	public void testPHA()
	{
		int instruction = 0x48;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		TestUtils.performInstruction(instruction, 0xA1);
		assertEquals(0x100,Platform.getCpuMemory().getStackPointer());
		int result = Platform.getCpuMemory().popStack();
		assertEquals(0xA1, result);
	}
	
	@Test
	public void testPLA()
	{
		int instruction = 0x68;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		Platform.getCpuMemory().pushStack(0xCF);
		TestUtils.performInstruction(instruction, 0xA1);
		assertEquals(0x100,Platform.getCpuMemory().getStackPointer());
		assertEquals(0xCF, Platform.getCpu().getAccumulator());
	}	
	
	@Test
	public void testPHP()
	{
		int instruction = 0x08;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
    	((NesCpu)Platform.getCpu()).getFlags().setNegative(true);
    	((NesCpu)Platform.getCpu()).getFlags().setOverflow(false);
    	((NesCpu)Platform.getCpu()).getFlags().setBrkCommand(true);
    	((NesCpu)Platform.getCpu()).getFlags().setDecimalMode(false);
    	((NesCpu)Platform.getCpu()).getFlags().setIrqDisable(true);
    	((NesCpu)Platform.getCpu()).getFlags().setZero(false);
    	((NesCpu)Platform.getCpu()).getFlags().setCarry(true);
    	TestUtils.performInstruction(instruction);
    	int result = Platform.getCpuMemory().popStack();
    	assertEquals(85,result);
	}	
	
	@Test
	public void testPLP()
	{
		int instruction = 0x28;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
    	Platform.getCpuMemory().pushStack(0x55);
    	TestUtils.performInstruction(instruction);
    	assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
    	assertTrue(!((NesCpu)Platform.getCpu()).getFlags().isOverflow());
    	assertTrue(((NesCpu)Platform.getCpu()).getFlags().isBrkCommand());
    	assertTrue(!((NesCpu)Platform.getCpu()).getFlags().isDecimalMode());
    	assertTrue(((NesCpu)Platform.getCpu()).getFlags().isIrqDisable());
    	assertTrue(!((NesCpu)Platform.getCpu()).getFlags().isZero());
    	assertTrue(((NesCpu)Platform.getCpu()).getFlags().isCarry());

	}		
}
