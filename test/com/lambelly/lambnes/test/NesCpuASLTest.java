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
import com.lambelly.lambnes.platform.cpu.NesCpuAddressingModes;
import com.lambelly.lambnes.platform.cpu.NesCpuMemory;
import com.lambelly.lambnes.test.utils.TestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:beans.xml"})
public class NesCpuASLTest
{
	@Autowired
	private NesCpu cpu;
	@Autowired
	private NesCpuMemory cpuMemory;
	@Autowired
	private TestUtils testUtils;
	private Logger logger = Logger.getLogger(NesCpuASLTest.class);
	
	@Before
	public void setUp()throws Exception
	{
		this.getTestUtils().createTestPlatform();
	}
	
	@Test
	public void testAccumulator()
	{
		int instruction = 0x0A;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		this.getTestUtils().performInstruction(instruction, 0xFF);
		logger.debug("accumulator: " + Integer.toHexString(this.getCpu().getAccumulator()));		
		assertEquals(254,this.getCpu().getAccumulator());
		assertTrue(this.getCpu().getFlags().isCarry());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
		
		// test case 2 
		this.getTestUtils().performInstruction(instruction, 0x15);
		logger.debug("accumulator: " + this.getCpu().getAccumulator());		
		logger.debug("bits: " + Integer.toBinaryString(this.getCpu().getAccumulator()));
		assertEquals(0x2A,this.getCpu().getAccumulator());
		assertFalse(this.getCpu().getFlags().isCarry());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());	
		
		// test case 3
		this.getTestUtils().performInstruction(instruction, 0xCA);
		logger.debug("accumulator: " + this.getCpu().getAccumulator());		
		logger.debug("bits: " + Integer.toBinaryString(this.getCpu().getAccumulator()));
		assertTrue(this.getCpu().getAccumulator() != 194);
		assertEquals(0x94,this.getCpu().getAccumulator());
		assertTrue(this.getCpu().getFlags().isCarry());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
	}
	
	@Test
	public void testZeroPage()
	{
		int instruction = 0x06;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		this.getTestUtils().setMemory(0x8001,0xFF);
		this.getTestUtils().performInstruction(instruction);		
		assertEquals(254,this.getCpuMemory().getMemoryFromHexAddress(0xFF));
		assertTrue(this.getCpu().getFlags().isCarry());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
		
		// test case 2 
		this.getTestUtils().setMemory(0x8003,0x15);
		this.getTestUtils().performInstruction(instruction);		
		assertEquals(0x2A,this.getCpuMemory().getMemoryFromHexAddress(0x15));
		assertFalse(this.getCpu().getFlags().isCarry());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());	
		
		// test case 3
		this.getTestUtils().setMemory(0x8005, 0xCA);
		this.getTestUtils().performInstruction(instruction);		
		assertEquals(0x94,this.getCpuMemory().getMemoryFromHexAddress(0xCA));
		assertTrue(this.getCpu().getFlags().isCarry());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
	}	
	
	@Test
	public void testZeroPageXIndexed()
	{
		int instruction = 0x16;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		this.getCpu().setX(0x02);
		this.getTestUtils().setMemory(0x8001,0xFF);
		this.getTestUtils().performInstruction(instruction);		
		assertEquals(2,this.getCpuMemory().getMemoryFromHexAddress(0x01));
		assertFalse(this.getCpu().getFlags().isCarry());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
		
		// test case 2 
		this.getCpu().setX(0x03);
		this.getTestUtils().setMemory(0x8003,0x15);
		this.getTestUtils().performInstruction(instruction);
		assertEquals(0x30,this.getCpuMemory().getMemoryFromHexAddress(0x18));
		assertFalse(this.getCpu().getFlags().isCarry());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());	
		
		// test case 3
		this.getCpu().setX(0x04);
		this.getTestUtils().setMemory(0x8005, 0xCA);
		this.getTestUtils().performInstruction(instruction);		
		assertEquals(0x9C,this.getCpuMemory().getMemoryFromHexAddress(0xCE));
		assertTrue(this.getCpu().getFlags().isCarry());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
	}	
	
	@Test
	public void testAbsolute()
	{
		int instruction = 0x0E;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		this.getTestUtils().setMemory(0x8001, 0x03);
		this.getTestUtils().setMemory(0x8002, 0x0);
		this.getTestUtils().setMemory(0x03,0xFF);
		this.getTestUtils().performInstruction(instruction);		
		assertEquals(254,this.getCpuMemory().getMemoryFromHexAddress(0x03));
		assertTrue(this.getCpu().getFlags().isCarry());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
		
		// test case 2 
		this.getTestUtils().setMemory(0x8004, 0x06);
		this.getTestUtils().setMemory(0x8005, 0);
		this.getTestUtils().setMemory(0x06,0x15);
		this.getTestUtils().performInstruction(instruction);
		assertEquals(0x2A,this.getCpuMemory().getMemoryFromHexAddress(0x06));
		assertFalse(this.getCpu().getFlags().isCarry());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());	
		
		// test case 3
		this.getTestUtils().setMemory(0x8007, 0x09);
		this.getTestUtils().setMemory(0x8008, 0x00);
		this.getTestUtils().setMemory(0x09, 0xCA);
		this.getTestUtils().performInstruction(instruction);
		assertEquals(0x94,this.getCpuMemory().getMemoryFromHexAddress(0x09));
		assertTrue(this.getCpu().getFlags().isCarry());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
	}		
	
	@Test
	public void testAbsoluteX()
	{
		int instruction = 0x1E;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test case 1
		this.getTestUtils().setMemory(0x8001, 0x03);
		this.getTestUtils().setMemory(0x8002, 0x0);
		this.getCpu().setX(0x01);
		this.getTestUtils().setMemory(0x04,0xFF);
		this.getTestUtils().performInstruction(instruction);
		assertEquals(254,this.getCpuMemory().getMemoryFromHexAddress(0x04));
		assertTrue(this.getCpu().getFlags().isCarry());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
		
		// test case 2 
		this.getCpu().setX(0x03);
		this.getTestUtils().setMemory(0x8004, 0x06);
		this.getTestUtils().setMemory(0x8005, 0);
		this.getTestUtils().setMemory(0x09,0x15);
		this.getTestUtils().performInstruction(instruction);
		assertEquals(0x2A,this.getCpuMemory().getMemoryFromHexAddress(0x09));
		assertFalse(this.getCpu().getFlags().isCarry());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());	
		
		// test case 3
		this.getCpu().setX(0x04);
		this.getTestUtils().setMemory(0x8007, 0x09);
		this.getTestUtils().setMemory(0x8008, 0x00);
		this.getTestUtils().setMemory(0x0D, 0xCA);
		this.getTestUtils().performInstruction(instruction);
		assertEquals(0x94,this.getCpuMemory().getMemoryFromHexAddress(0x0D));
		assertTrue(this.getCpu().getFlags().isCarry());
		assertTrue(this.getCpu().getFlags().isNegative());
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
