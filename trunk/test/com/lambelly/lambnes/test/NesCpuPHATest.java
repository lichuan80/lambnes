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
public class NesCpuPHATest
{
	@Autowired
	private NesCpu cpu;
	@Autowired
	private NesCpuMemory cpuMemory;
	@Autowired
	private TestUtils testUtils;
	private Logger logger = Logger.getLogger(NesCpuPHATest.class);
	
	@Before
	public void setUp() throws Exception
	{
		this.getTestUtils().createTestPlatform();
	}
	
	@Test
	public void testPHA()
	{
		int instruction = 0x48;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		this.getTestUtils().performInstruction(instruction, 0xA1);
		assertEquals(0xFF,this.getCpuMemory().getStackPointer());
		int result = this.getCpuMemory().popStack();
		assertEquals(0xA1, result);
	}
	
	@Test
	public void testPLA()
	{
		int instruction = 0x68;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		this.getCpuMemory().pushStack(0xCF);
		this.getTestUtils().performInstruction(instruction, 0xA1);
		assertEquals(0x0,this.getCpuMemory().getStackPointer());
		assertEquals(0xCF, this.getCpu().getAccumulator());
	}	
	
	@Test
	public void testPHP()
	{
		int instruction = 0x08;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
    	this.getCpu().getFlags().setNegative(true);
    	this.getCpu().getFlags().setOverflow(false);
    	this.getCpu().getFlags().setDecimalMode(false);
    	this.getCpu().getFlags().setIrqDisable(true);
    	this.getCpu().getFlags().setZero(false);
    	this.getCpu().getFlags().setCarry(true);
    	this.getTestUtils().performInstruction(instruction);
    	int result = this.getCpuMemory().popStack();
    	assertEquals(0xB5,result);
	}	

	@Test
	public void testPHP2()
	{
		int instruction = 0x08;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 2
		/*
		 *  negative: true
		 *	overflow: true
		 *	brkCommand: false
		 *	decimalMode: false
		 *	irqDisable: true
		 *	zero: false
		 *	carry: false^M
		 *	11 Nov 2010 11:35:35,214 [DEBUG] com.lambelly.lambnes.platform.Platform {Platform.java:78} -
		 *	X: 255	
		 * 	Y: 1
		 *	A: 144
		 * */
    	this.getCpu().getFlags().setNegative(true);
    	this.getCpu().getFlags().setOverflow(true);
    	this.getCpu().getFlags().setDecimalMode(false);
    	this.getCpu().getFlags().setIrqDisable(true);
    	this.getCpu().getFlags().setZero(false);
    	this.getCpu().getFlags().setCarry(false);
    	this.getTestUtils().performInstruction(instruction);
    	int result = this.getCpuMemory().popStack();
    	logger.debug("status: 0x" + Integer.toHexString(result));
    	assertEquals(0xF4,result);
	}	
	
	@Test
	public void testPLP()
	{
		int instruction = 0x28;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		this.getCpuMemory().pushStack(0x55);
    	this.getTestUtils().performInstruction(instruction);
    	assertFalse(this.getCpu().getFlags().isNegative());
    	assertTrue(this.getCpu().getFlags().isOverflow());
    	assertFalse(this.getCpu().getFlags().isDecimalMode());
    	assertTrue(this.getCpu().getFlags().isIrqDisable());
    	assertFalse(this.getCpu().getFlags().isZero());
    	assertTrue(this.getCpu().getFlags().isCarry());

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
