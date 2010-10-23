package com.lambelly.lambnes.test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.log4j.*;

import com.lambelly.lambnes.platform.*;
import com.lambelly.lambnes.platform.cpu.NesCpu;
import com.lambelly.lambnes.test.utils.TestUtils;

public class NesCpuSETTest
{
	private Logger logger = Logger.getLogger(NesCpuSETTest.class);
	
	@Before
	public void setUp() throws Exception
	{
		TestUtils.createTestPlatform();
	}
	
	@Test
	public void testSEC()
	{
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isCarry());
		TestUtils.performInstruction(0x38);
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isCarry());
	}
	
	@Test
	public void testSED()
	{
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isDecimalMode());
		TestUtils.performInstruction(0xF8);
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isDecimalMode());
	}
	
	@Test
	public void testSEI()
	{
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isIrqDisable());
		TestUtils.performInstruction(0x78);
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isIrqDisable());
	}
}
