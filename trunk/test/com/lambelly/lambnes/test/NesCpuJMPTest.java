package com.lambelly.lambnes.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

import org.apache.log4j.*;

import com.lambelly.lambnes.platform.cpu.NesCpuMemory;
import com.lambelly.lambnes.platform.*;
import com.lambelly.lambnes.test.utils.TestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:beans.xml"})
public class NesCpuJMPTest
{
	@Autowired
	private NesCpuMemory cpuMemory;
	@Autowired
	private TestUtils testUtils;
	private Logger logger = Logger.getLogger(NesCpuJMPTest.class);
	
	@Before
	public void setUp() throws Exception
	{
		this.getTestUtils().createTestPlatform();
	}
	
	@Test
	public void testAbsolute()
	{
		int instruction = 0x4C;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		this.getTestUtils().performInstruction(instruction);		
		assertEquals(0x0201,this.getCpuMemory().getProgramCounter());
	}
	
	@Test
	public void testIndexedIndirect()
	{
		int instruction = 0x6C;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		this.getTestUtils().performInstruction(instruction);		
		assertEquals(0x0102,this.getCpuMemory().getProgramCounter());
	}

	public TestUtils getTestUtils()
    {
    	return testUtils;
    }

	public void setTestUtils(TestUtils testUtils)
    {
    	this.testUtils = testUtils;
    }

	public NesCpuMemory getCpuMemory()
    {
    	return cpuMemory;
    }

	public void setCpuMemory(NesCpuMemory cpuMemory)
    {
    	this.cpuMemory = cpuMemory;
    }	
}
