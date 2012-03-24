package com.lambelly.lambnes.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
import org.apache.log4j.*;

import com.lambelly.lambnes.platform.cpu.NesCpu;
import com.lambelly.lambnes.platform.cpu.NesCpuMemory;
import com.lambelly.lambnes.platform.ppu.NesPpu;
import com.lambelly.lambnes.platform.ppu.NesPpuMemory;
import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.test.utils.TestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:beans.xml"})
public class PPUVramAddressRegisterTest
{
	@Autowired
	private TestUtils testUtils;
	@Autowired
	private NesCpu cpu;
	@Autowired
	private NesCpuMemory cpuMemory;	
	@Autowired
	private NesPpu ppu;
	@Autowired
	private NesPpuMemory ppuMemory;
	
	private Logger logger = Logger.getLogger(PPUVramAddressRegisterTest.class);
	
	@Before
	public void setUp()throws Exception
	{
		this.getTestUtils().createTestPlatform();
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
		this.getCpuMemory().setMemoryFromHexAddress(0x2006, 0x3F);
		this.getPpu().cycle(1);
		this.getCpuMemory().setMemoryFromHexAddress(0x2006, 0x00);
		this.getPpu().cycle(2);
		this.getCpu().setY(0x20);
		this.getPpu().cycle(3);
		this.getCpu().setX(0x20);
		this.getPpu().cycle(4);
		this.getCpuMemory().setMemoryFromHexAddress(0x2007, 0x2F);
		this.getPpu().cycle(5);
		this.getCpuMemory().setMemoryFromHexAddress(0x2007, 0x3F);
		this.getPpu().cycle(6);
		this.getPpu().cycle(7);
		
		// test
		assertEquals(0x2F, this.getPpuMemory().getMemoryFromHexAddress(0x3F00));
		assertEquals(0x3F, this.getPpuMemory().getMemoryFromHexAddress(0x3F01));
	}

	public TestUtils getTestUtils()
    {
    	return testUtils;
    }

	public void setTestUtils(TestUtils testUtils)
    {
    	this.testUtils = testUtils;
    }

	public NesCpu getCpu()
    {
    	return cpu;
    }

	public void setCpu(NesCpu cpu)
    {
    	this.cpu = cpu;
    }

	public NesCpuMemory getCpuMemory()
    {
    	return cpuMemory;
    }

	public void setCpuMemory(NesCpuMemory cpuMemory)
    {
    	this.cpuMemory = cpuMemory;
    }

	public NesPpu getPpu()
    {
    	return ppu;
    }

	public void setPpu(NesPpu ppu)
    {
    	this.ppu = ppu;
    }

	public NesPpuMemory getPpuMemory()
    {
    	return ppuMemory;
    }

	public void setPpuMemory(NesPpuMemory ppuMemory)
    {
    	this.ppuMemory = ppuMemory;
    }	
}
