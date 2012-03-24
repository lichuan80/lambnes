package com.lambelly.lambnes.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
import org.apache.log4j.*;

import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.platform.cpu.NesCpu;
import com.lambelly.lambnes.platform.cpu.NesCpuMemory;
import com.lambelly.lambnes.test.utils.TestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:beans.xml"})
public class NesCpuBCCTest
{
	@Autowired
	private NesCpu cpu;
	@Autowired
	private NesCpuMemory cpuMemory;
	@Autowired
	private TestUtils testUtils;
	private Logger logger = Logger.getLogger(NesCpuBCCTest.class);
	
	@Before
	public void setUp()throws Exception
	{
		this.getTestUtils().createTestPlatform();
	}
	
	@Test
	public void testBCC()
	{
		int instruction = 0x90;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		this.getCpu().getFlags().setCarry(false);
		this.getTestUtils().performInstruction(instruction);
		assertEquals(0x8003,this.getCpuMemory().getProgramCounter());

		// test case 2
		this.getCpu().getFlags().setCarry(true);
		this.getTestUtils().performInstruction(instruction);
		assertEquals(0x8005,this.getCpuMemory().getProgramCounter());		
	}	
	
	@Test
	public void testBCS()
	{
		int instruction = 0xB0;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		this.getCpu().getFlags().setCarry(true);
		this.getTestUtils().performInstruction(instruction);
		assertEquals(0x8003,this.getCpuMemory().getProgramCounter());

		// test case 2
		this.getCpu().getFlags().setCarry(false);
		this.getTestUtils().performInstruction(instruction);
		assertEquals(0x8005,this.getCpuMemory().getProgramCounter());		
	}	
	
	@Test
	public void testBEQ()
	{
		int instruction = 0xF0;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		this.getCpu().getFlags().setZero(true);
		this.getTestUtils().performInstruction(instruction);
		assertEquals(0x8003,this.getCpuMemory().getProgramCounter());

		// test case 2
		this.getCpu().getFlags().setZero(false);
		this.getTestUtils().performInstruction(instruction);
		assertEquals(0x8005,this.getCpuMemory().getProgramCounter());		
	}	
	
	@Test
	public void testBMI()
	{
		int instruction = 0x30;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		this.getCpu().getFlags().setNegative(true);
		this.getTestUtils().performInstruction(instruction);
		assertEquals(0x8003,this.getCpuMemory().getProgramCounter());

		// test case 2
		this.getCpu().getFlags().setNegative(false);
		this.getTestUtils().performInstruction(instruction);
		assertEquals(0x8005,this.getCpuMemory().getProgramCounter());		
	}		
	
	@Test
	public void testBNE()
	{
		int instruction = 0xD0;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		this.getCpu().getFlags().setZero(false);
		this.getTestUtils().performInstruction(instruction);
		assertEquals(0x8003,this.getCpuMemory().getProgramCounter());

		// test case 2
		this.getCpu().getFlags().setZero(true);
		this.getTestUtils().performInstruction(instruction);
		assertEquals(0x8005,this.getCpuMemory().getProgramCounter());		
	}		
	
	@Test
	public void testBPL()
	{
		int instruction = 0x10;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		this.getCpu().getFlags().setNegative(false);
		this.getTestUtils().performInstruction(instruction);
		assertEquals(0x8003,this.getCpuMemory().getProgramCounter());

		// test case 2
		this.getCpu().getFlags().setNegative(true);
		this.getTestUtils().performInstruction(instruction);
		assertEquals(0x8005,this.getCpuMemory().getProgramCounter());		
	}	
	
	@Test
	public void testBVC()
	{
		int instruction = 0x50;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		this.getCpu().getFlags().setOverflow(false);
		this.getTestUtils().performInstruction(instruction);
		assertEquals(0x8003,this.getCpuMemory().getProgramCounter());

		// test case 2
		this.getCpu().getFlags().setOverflow(true);
		this.getTestUtils().performInstruction(instruction);
		assertEquals(0x8005,this.getCpuMemory().getProgramCounter());		
	}	
	
	@Test
	public void testBVS()
	{
		int instruction = 0x70;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		this.getCpu().getFlags().setOverflow(true);
		this.getTestUtils().performInstruction(instruction);
		assertEquals(0x8003,this.getCpuMemory().getProgramCounter());

		// test case 2
		this.getCpu().getFlags().setOverflow(false);
		this.getTestUtils().performInstruction(instruction);
		assertEquals(0x8005,this.getCpuMemory().getProgramCounter());		
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

	public TestUtils getTestUtils()
    {
    	return testUtils;
    }

	public void setTestUtils(TestUtils testUtils)
    {
    	this.testUtils = testUtils;
    }		
}
