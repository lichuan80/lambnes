package com.lambelly.lambnes.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.log4j.*;

import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.platform.cpu.NesCpu;
import com.lambelly.lambnes.test.utils.TestUtils;

public class NesCpuSBCTest
{
	private Logger logger = Logger.getLogger(NesCpuSBCTest.class);
	
	@Before
	public void setUp()throws Exception
	{
		TestUtils.createTestPlatform();
	}
	
	@Test
	public void test1()
	{
		int instruction = 0xE9;
		
		// test case 1
		Platform.getCpu().getFlags().resetFlags();
		Platform.getCpuMemory().setMemoryFromHexAddress(0x8001, 0);
		TestUtils.performInstruction(instruction, 0x80);
		logger.debug("accumulator: " + Integer.toHexString(Platform.getCpu().getAccumulator()));		
		assertEquals(0x7F,Platform.getCpu().getAccumulator());

		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isOverflow());
	}
}
