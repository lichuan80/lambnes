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
public class NesCpuADCTest
{
	@Autowired
	private NesCpu cpu;
	@Autowired
	private NesCpuMemory cpuMemory;
	@Autowired
	private TestUtils testUtils;
	private Logger logger = Logger.getLogger(NesCpuADCTest.class);
	
	@Before
	public void setUp()throws Exception
	{
		this.getTestUtils().createTestPlatform();
	}
	
	@Test
	public void Immediate()
	{
		int instruction = 0x69;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		this.getTestUtils().performInstruction(instruction, 0xFF);
		logger.debug("accumulator: " + Integer.toHexString(this.getCpu().getAccumulator()));		
		assertEquals(0,this.getCpu().getAccumulator());
		assertTrue(this.getCpu().getFlags().isCarry());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertTrue(this.getCpu().getFlags().isZero());
		
		// test case 2
		this.getTestUtils().performInstruction(instruction, 0xAE);
		logger.debug("accumulator: " + Integer.toHexString(this.getCpu().getAccumulator()));		
		assertEquals(0xB2,this.getCpu().getAccumulator());
		assertFalse(this.getCpu().getFlags().isCarry());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());	
		
		// test case 3
		this.getTestUtils().performInstruction(instruction, 0xA);
		logger.debug("accumulator: " + Integer.toHexString(this.getCpu().getAccumulator()));		
		assertEquals(0xF,this.getCpu().getAccumulator());
		assertFalse(this.getCpu().getFlags().isCarry());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
	}	
	
	@Test
	public void ZeroPage()
	{
		int instruction = 0x65;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		this.getTestUtils().performInstruction(instruction, 0xFF);
		logger.debug("accumulator: " + Integer.toHexString(this.getCpu().getAccumulator()));		
		assertEquals(0,this.getCpu().getAccumulator());
		assertTrue(this.getCpu().getFlags().isCarry());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertTrue(this.getCpu().getFlags().isZero());
		
		// test case 2
		this.getTestUtils().performInstruction(instruction, 0xAE);
		logger.debug("accumulator: " + Integer.toHexString(this.getCpu().getAccumulator()));		
		assertEquals(0xB2,this.getCpu().getAccumulator());
		assertFalse(this.getCpu().getFlags().isCarry());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());	
		
		// test case 3
		this.getTestUtils().performInstruction(instruction, 0xA);
		logger.debug("accumulator: " + Integer.toHexString(this.getCpu().getAccumulator()));		
		assertEquals(0xF,this.getCpu().getAccumulator());
		assertFalse(this.getCpu().getFlags().isCarry());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
	}	
	
	@Test
	public void ZeroPageX()
	{
		int instruction = 0x75;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		this.getTestUtils().performInstruction(instruction, 0xFF);
		logger.debug("accumulator: " + Integer.toHexString(this.getCpu().getAccumulator()));		
		assertEquals(0,this.getCpu().getAccumulator());
		assertTrue(this.getCpu().getFlags().isCarry());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertTrue(this.getCpu().getFlags().isZero());
		
		// test case 2
		this.getTestUtils().performInstruction(instruction, 0xAE);
		logger.debug("accumulator: " + Integer.toHexString(this.getCpu().getAccumulator()));		
		assertEquals(0xB2,this.getCpu().getAccumulator());
		assertFalse(this.getCpu().getFlags().isCarry());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());	
		
		// test case 3
		this.getTestUtils().performInstruction(instruction, 0xA);
		logger.debug("accumulator: " + Integer.toHexString(this.getCpu().getAccumulator()));		
		assertEquals(0xF,this.getCpu().getAccumulator());
		assertFalse(this.getCpu().getFlags().isCarry());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
	}		
	
	@Test
	public void Absolute()
	{
		int instruction = 0x6D;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		this.getTestUtils().performInstruction(instruction, 0xFF);
		logger.debug("accumulator: " + Integer.toHexString(this.getCpu().getAccumulator()));		
		assertEquals(0,this.getCpu().getAccumulator());
		assertTrue(this.getCpu().getFlags().isCarry());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertTrue(this.getCpu().getFlags().isZero());
		
		// test case 2
		this.getTestUtils().performInstruction(instruction, 0xAE);
		logger.debug("accumulator: " + Integer.toHexString(this.getCpu().getAccumulator()));		
		assertEquals(0xB3,this.getCpu().getAccumulator());
		assertFalse(this.getCpu().getFlags().isCarry());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());	
		
		// test case 3
		this.getTestUtils().performInstruction(instruction, 0xA);
		logger.debug("accumulator: " + Integer.toHexString(this.getCpu().getAccumulator()));		
		assertEquals(0x11,this.getCpu().getAccumulator());
		assertFalse(this.getCpu().getFlags().isCarry());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
	}
	
	@Test
	public void AbsoluteX()
	{
		int instruction = 0x7D;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		this.getCpu().setX(0xA);
		
		// test case 1
		this.getTestUtils().performInstruction(instruction, 0xFF);
		logger.debug("accumulator: " + Integer.toHexString(this.getCpu().getAccumulator()));		
		assertEquals(0xA,this.getCpu().getAccumulator());
		assertTrue(this.getCpu().getFlags().isCarry());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
		
		// test case 2
		this.getTestUtils().performInstruction(instruction, 0xAE);
		logger.debug("accumulator: " + Integer.toHexString(this.getCpu().getAccumulator()));		
		assertEquals(0xBD,this.getCpu().getAccumulator());
		assertFalse(this.getCpu().getFlags().isCarry());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());	
		
		// test case 3
		this.getTestUtils().performInstruction(instruction, 0xA);
		logger.debug("accumulator: " + Integer.toHexString(this.getCpu().getAccumulator()));		
		assertEquals(0x1B,this.getCpu().getAccumulator());
		assertFalse(this.getCpu().getFlags().isCarry());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
	}			
	
	@Test
	public void ZeroPageY()
	{
		int instruction = 0x79;

		this.getCpu().setY(0xA);
		
		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		this.getTestUtils().performInstruction(instruction, 0xFF);
		logger.debug("accumulator: " + Integer.toHexString(this.getCpu().getAccumulator()));		
		assertEquals(0xA,this.getCpu().getAccumulator());
		assertTrue(this.getCpu().getFlags().isCarry());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
		
		// test case 2
		this.getTestUtils().performInstruction(instruction, 0xAE);
		logger.debug("accumulator: " + Integer.toHexString(this.getCpu().getAccumulator()));		
		assertEquals(0xBD,this.getCpu().getAccumulator());
		assertFalse(this.getCpu().getFlags().isCarry());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());	
		
		// test case 3
		this.getTestUtils().performInstruction(instruction, 0xA);
		logger.debug("accumulator: " + Integer.toHexString(this.getCpu().getAccumulator()));		
		assertEquals(0x1B,this.getCpu().getAccumulator());
		assertFalse(this.getCpu().getFlags().isCarry());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
	}			
	
	@Test
	public void IndexedIndirect()
	{
		int instruction = 0x61;

		this.getCpu().setX(0xA);
		
		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		this.getTestUtils().performInstruction(instruction, 0xFF);
		logger.debug("accumulator: " + Integer.toHexString(this.getCpu().getAccumulator()));		
		assertEquals(0xA,this.getCpu().getAccumulator());
		assertTrue(this.getCpu().getFlags().isCarry());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
		
		// test case 2
		this.getTestUtils().performInstruction(instruction, 0xAE);
		logger.debug("accumulator: " + Integer.toHexString(this.getCpu().getAccumulator()));		
		assertEquals(0xBC,this.getCpu().getAccumulator());
		assertFalse(this.getCpu().getFlags().isCarry());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());	
		
		// test case 3
		this.getTestUtils().performInstruction(instruction, 0xA);
		logger.debug("accumulator: " + Integer.toHexString(this.getCpu().getAccumulator()));		
		assertEquals(0x19,this.getCpu().getAccumulator());
		assertFalse(this.getCpu().getFlags().isCarry());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
	}			
	
	@Test
	public void IndirectIndexed()
	{
		int instruction = 0x71;

		this.getCpu().setY(0xA);
		
		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		this.getTestUtils().performInstruction(instruction, 0xFF);
		logger.debug("accumulator: " + Integer.toHexString(this.getCpu().getAccumulator()));		
		assertEquals(0xA,this.getCpu().getAccumulator());
		assertTrue(this.getCpu().getFlags().isCarry());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
		
		// test case 2
		this.getTestUtils().performInstruction(instruction, 0xAE);
		logger.debug("accumulator: " + Integer.toHexString(this.getCpu().getAccumulator()));		
		assertEquals(0xBC,this.getCpu().getAccumulator());
		assertFalse(this.getCpu().getFlags().isCarry());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());	
		
		// test case 3
		this.getTestUtils().performInstruction(instruction, 0xA);
		logger.debug("accumulator: " + Integer.toHexString(this.getCpu().getAccumulator()));		
		assertEquals(0x19,this.getCpu().getAccumulator());
		assertFalse(this.getCpu().getFlags().isCarry());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
	}	
	
	@Test
	public void test1()
	{
		int instruction = 0x69;
		
		// test case 1
		this.getCpu().getFlags().resetFlags();
		this.getCpuMemory().setMemoryFromHexAddress(0x8001, 0);
		this.getTestUtils().performInstruction(instruction, 0x0);
		logger.debug("accumulator: " + Integer.toHexString(this.getCpu().getAccumulator()));		
		assertEquals(0,this.getCpu().getAccumulator());

		assertFalse(this.getCpu().getFlags().isCarry());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertTrue(this.getCpu().getFlags().isZero());
		assertFalse(this.getCpu().getFlags().isOverflow());
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

	public NesCpuMemory getCpuMemory()
    {
    	return cpuMemory;
    }

	public void setCpuMemory(NesCpuMemory cpuMemory)
    {
    	this.cpuMemory = cpuMemory;
    }		
}
