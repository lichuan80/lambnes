package com.lambelly.lambnes.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.log4j.*;

import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.test.utils.TestUtils;

public class PPUVramAddressRegisterTest
{
	private Logger logger = Logger.getLogger(PPUVramAddressRegisterTest.class);
	
	@Before
	public void setUp()throws Exception
	{
		TestUtils.createTestPlatform();
	}
	
	@Test
	public void write()
	{
		/*
		 *     ldx   #$3F
			   stx   $2006
			   ldx   #$00
			   stx   $2006
			
			   ldy   #$00
			   ldx   #$20     ;Set BG & Sprite palettes.
			.InitPal lda   (PALETTEPTR),Y
			   sta   $2007
			   sta   TEMPPAL,Y               ;Store to palette copy
			   iny
			   dex
			   bne   .InitPal
		 */
		
		// write to 0X3F00
		Platform.getCpuMemory().setMemoryFromHexAddress(0x2006, 0x3F);
		Platform.getPpu().cycle(1);
		Platform.getCpuMemory().setMemoryFromHexAddress(0x2006, 0x00);
		Platform.getPpu().cycle(2);
		Platform.getCpu().setY(0x20);
		Platform.getPpu().cycle(3);
		Platform.getCpu().setX(0x20);
		Platform.getPpu().cycle(4);
		Platform.getCpuMemory().setMemoryFromHexAddress(0x2007, 0x2F);
		Platform.getPpu().cycle(5);
		Platform.getCpuMemory().setMemoryFromHexAddress(0x2007, 0x3F);
		Platform.getPpu().cycle(6);
		Platform.getPpu().cycle(7);
		
		// test
		assertEquals(0x2F, Platform.getPpuMemory().getMemoryFromHexAddress(0x3F00));
		assertEquals(0x3F, Platform.getPpuMemory().getMemoryFromHexAddress(0x3F01));
	}	
}
