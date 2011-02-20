package com.lambelly.lambnes.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.log4j.*;

import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.test.utils.TestUtils;

public class PPUSpriteDMARegisterTest
{
	private Logger logger = Logger.getLogger(PPUSpriteDMARegisterTest.class);
	
	@Before
	public void setUp()throws Exception
	{
		TestUtils.createTestPlatform();
	}
	
	@Test
	public void write()
	{
		// write to 0X3F00
		Platform.getCpuMemory().setMemoryFromHexAddress(0x4014, 0x03);
		Platform.getPpu().cycle(1);
		
		// test
		assertEquals(0x3F, Platform.getPpuMemory().getSprRam()[0x3F]);
	}	
}
