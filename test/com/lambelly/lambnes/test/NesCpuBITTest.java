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
public class NesCpuBITTest
{
	@Autowired
	private NesCpu cpu;
	@Autowired
	private NesCpuMemory cpuMemory;
	@Autowired
	private TestUtils testUtils;
	private Logger logger = Logger.getLogger(NesCpuBITTest.class);
	
	@Before
	public void setUp() throws Exception
	{
		this.getTestUtils().createTestPlatform();
	}
	
	@Test
	public void testZeroPage()
	{
		int instruction = 0x24;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		this.getTestUtils().performInstruction(instruction, 0xCF);
		logger.debug("accumulator: " + Integer.toHexString(this.getCpu().getAccumulator()));		
		assertEquals(0xCF,this.getCpu().getAccumulator());
		assertEquals(0x8002,this.getCpuMemory().getProgramCounter());
		assertFalse(this.getCpu().getFlags().isOverflow());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
		
		// test case 2 
		this.getTestUtils().performInstruction(instruction, 0x00);
		logger.debug("accumulator: " + this.getCpu().getAccumulator());		
		logger.debug("bits: " + Integer.toBinaryString(this.getCpu().getAccumulator()));
		assertEquals(0x00,this.getCpu().getAccumulator());
		assertEquals(0x8004,this.getCpuMemory().getProgramCounter());
		assertFalse(this.getCpu().getFlags().isCarry());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertTrue(this.getCpu().getFlags().isZero());	
		
		// test case 3
		this.getTestUtils().performInstruction(instruction, 0xCC);
		logger.debug("accumulator: " + this.getCpu().getAccumulator());		
		logger.debug("bits: " + Integer.toBinaryString(this.getCpu().getAccumulator()));
		assertTrue(this.getCpu().getAccumulator() != 194);
		assertEquals(0xCC,this.getCpu().getAccumulator());
		assertEquals(0x8006,this.getCpuMemory().getProgramCounter());
		assertFalse(this.getCpu().getFlags().isCarry());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
	}
	
	@Test
	public void testAbsolute()
	{
		int instruction = 0x2C;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		this.getTestUtils().performInstruction(instruction, 0xCF);
		logger.debug("accumulator: " + Integer.toHexString(this.getCpu().getAccumulator()));		
		assertEquals(0xCF,this.getCpu().getAccumulator());
		assertEquals(0x8003,this.getCpuMemory().getProgramCounter());
		assertFalse(this.getCpu().getFlags().isOverflow());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
		
		// test case 2 
		this.getTestUtils().performInstruction(instruction, 0x00);
		logger.debug("accumulator: " + this.getCpu().getAccumulator());		
		logger.debug("bits: " + Integer.toBinaryString(this.getCpu().getAccumulator()));
		assertEquals(0x00,this.getCpu().getAccumulator());
		assertEquals(0x8006,this.getCpuMemory().getProgramCounter());
		assertFalse(this.getCpu().getFlags().isCarry());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertTrue(this.getCpu().getFlags().isZero());	
		
		// test case 3
		this.getTestUtils().performInstruction(instruction, 0xCC);
		logger.debug("accumulator: " + this.getCpu().getAccumulator());		
		logger.debug("bits: " + Integer.toBinaryString(this.getCpu().getAccumulator()));
		assertTrue(this.getCpu().getAccumulator() != 194);
		assertEquals(0xCC,this.getCpu().getAccumulator());
		assertEquals(0x8009,this.getCpuMemory().getProgramCounter());
		assertFalse(this.getCpu().getFlags().isCarry());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
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
