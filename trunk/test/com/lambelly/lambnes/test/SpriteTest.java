package com.lambelly.lambnes.test;

import org.junit.Before;
import org.junit.Test;

import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.platform.ppu.*;
import com.lambelly.lambnes.platform.ppu.registers.PPUControlRegister1;
import com.lambelly.lambnes.test.utils.TestUtils;

import org.apache.log4j.*;

public class SpriteTest
{
	private Logger logger = Logger.getLogger(SpriteTest.class);
	
	@Before
	public void setUp() throws Exception
	{
		TestUtils.createTestPlatform();
	}
	
	@Test
	public void test()
	{
		// try instantiating ppu control register
		//Platform.getPpu().getPpuControlRegister1().setSpriteSize(PPUControlRegister1.SPRITE_SIZE_8X8);
		/*
		 * 06 Dec 2010 11:11:26,111 [DEBUG] com.lambelly.lambnes.platform.ppu.SpriteAttribute {SpriteAttribute.java:33} - rawBit1: 78
		 * 06 Dec 2010 11:11:26,111 [DEBUG] com.lambelly.lambnes.platform.ppu.SpriteAttribute {SpriteAttribute.java:34} - rawBit2: 1
		 * 06 Dec 2010 11:11:26,111 [DEBUG] com.lambelly.lambnes.platform.ppu.SpriteAttribute {SpriteAttribute.java:35} - rawBit3: 0
		 * 06 Dec 2010 11:11:26,111 [DEBUG] com.lambelly.lambnes.platform.ppu.SpriteAttribute {SpriteAttribute.java:36} - rawBit4: 102
		 */
		Platform.getPpuMemory().setSprRamFromHexAddress(0, 78);
		Platform.getPpuMemory().setSprRamFromHexAddress(1, 1);
		Platform.getPpuMemory().setSprRamFromHexAddress(2, 0);
		Platform.getPpuMemory().setSprRamFromHexAddress(3, 102);
		
		SpriteTile s0 = new SpriteTile(0);
		logger.debug(s0);
	}
}
