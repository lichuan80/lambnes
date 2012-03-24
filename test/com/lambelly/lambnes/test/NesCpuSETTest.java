package com.lambelly.lambnes.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
import org.apache.log4j.*;

import com.lambelly.lambnes.platform.*;
import com.lambelly.lambnes.platform.cpu.NesCpu;
import com.lambelly.lambnes.platform.cpu.NesCpuMemory;
import com.lambelly.lambnes.test.utils.TestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:beans.xml"})
public class NesCpuSETTest
{
	@Autowired
	private NesCpu cpu;
	@Autowired
	private TestUtils testUtils;
	private Logger logger = Logger.getLogger(NesCpuSETTest.class);
	
	@Before
	public void setUp() throws Exception
	{
		this.getTestUtils().createTestPlatform();
	}
	
	@Test
	public void testSEC()
	{
		assertFalse(this.getCpu().getFlags().isCarry());
		this.getTestUtils().performInstruction(0x38);
		assertTrue(this.getCpu().getFlags().isCarry());
	}
	
	@Test
	public void testSED()
	{
		assertFalse(this.getCpu().getFlags().isDecimalMode());
		this.getTestUtils().performInstruction(0xF8);
		assertTrue(this.getCpu().getFlags().isDecimalMode());
	}
	
	@Test
	public void testSEI()
	{
		assertFalse(this.getCpu().getFlags().isIrqDisable());
		this.getTestUtils().performInstruction(0x78);
		assertTrue(this.getCpu().getFlags().isIrqDisable());
	}

	public NesCpu getCpu()
    {
    	return cpu;
    }

	public void setCpu(NesCpu cpu)
    {
    	this.cpu = cpu;
    }

	public TestUtils getTestUtils()
    {
    	return testUtils;
    }

	public void setTestUtils(TestUtils testUtils)
    {
    	this.testUtils = testUtils;
    }
}
