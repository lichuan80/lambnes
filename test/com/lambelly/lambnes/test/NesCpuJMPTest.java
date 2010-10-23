package com.lambelly.lambnes.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.apache.log4j.*;

import com.lambelly.lambnes.platform.*;
import com.lambelly.lambnes.test.utils.TestUtils;

public class NesCpuJMPTest
{
	private Logger logger = Logger.getLogger(NesCpuJMPTest.class);
	
	@Before
	public void setUp() throws Exception
	{
		TestUtils.createTestPlatform();
	}
	
	@Test
	public void testAbsolute()
	{
		int instruction = 0x4C;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		TestUtils.performInstruction(instruction);		
		assertEquals(0x0201,Platform.getCpuMemory().getProgramCounter());
	}
	
	@Test
	public void testIndexedIndirect()
	{
		int instruction = 0x6C;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		Platform.getCpu().setX(0x0C);
		TestUtils.performInstruction(instruction);		
		assertEquals(0x0201,Platform.getCpuMemory().getProgramCounter());
	}	
}
