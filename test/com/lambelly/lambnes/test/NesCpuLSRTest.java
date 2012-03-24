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
public class NesCpuLSRTest
{
	@Autowired
	private NesCpu cpu;
	@Autowired
	private NesCpuMemory cpuMemory;
	@Autowired
	private TestUtils testUtils;
	private Logger logger = Logger.getLogger(NesCpuLSRTest.class);
	
	@Before
	public void setUp() throws Exception
	{
		this.getTestUtils().createTestPlatform();
	}
	
	@Test
	public void LSRTest()
	{
		int value = 0xFF;
		logger.debug("0xFF: " + Integer.toBinaryString(value));
		value = value >> 1;
		logger.debug("after shift: " + Integer.toBinaryString(value));
	}

	@Test
	public void testAccumulator()
	{
		int instruction = 0x4A;
		
		// test 1
		this.getCpu().setAccumulator(0xE4);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x72, this.getCpu().getAccumulator());
		assertFalse(((NesCpu)this.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)this.getCpu()).getFlags().isZero());
		assertFalse(((NesCpu)this.getCpu()).getFlags().isNegative());

	    // test 2
		this.getCpu().setAccumulator(0x9B);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x4D, this.getCpu().getAccumulator());
		assertTrue(((NesCpu)this.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)this.getCpu()).getFlags().isZero());
		assertFalse(((NesCpu)this.getCpu()).getFlags().isNegative());		
	}
	
	@Test
	public void testZeroPage()
	{
		int instruction = 0x46;
		
		// test 1
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0, this.getCpuMemory().getMemoryFromHexAddress(0x01));
		assertTrue(((NesCpu)this.getCpu()).getFlags().isCarry());
		assertTrue(((NesCpu)this.getCpu()).getFlags().isZero());
		assertFalse(((NesCpu)this.getCpu()).getFlags().isNegative());

	    // test 2
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(1, this.getCpuMemory().getMemoryFromHexAddress(0x03));
		assertTrue(((NesCpu)this.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)this.getCpu()).getFlags().isZero());
		assertFalse(((NesCpu)this.getCpu()).getFlags().isNegative());		
	}	
	
	@Test
	public void testZeroPageX()
	{
		int instruction = 0x56;
		
		// test 1
		this.getCpu().setX(0xF);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(8, this.getCpuMemory().getMemoryFromHexAddress(0x10));
		assertFalse(((NesCpu)this.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)this.getCpu()).getFlags().isZero());
		assertFalse(((NesCpu)this.getCpu()).getFlags().isNegative());

	    // test 2
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(9, this.getCpuMemory().getMemoryFromHexAddress(0x12));
		assertFalse(((NesCpu)this.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)this.getCpu()).getFlags().isZero());
		assertFalse(((NesCpu)this.getCpu()).getFlags().isNegative());		
	}	
	
	@Test
	public void testAbsolute()
	{
		int instruction = 0x4E;
		
		// test 1
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0, this.getCpuMemory().getMemoryFromHexAddress(0x0201));
		assertTrue(((NesCpu)this.getCpu()).getFlags().isCarry());
		assertTrue(((NesCpu)this.getCpu()).getFlags().isZero());
		assertFalse(((NesCpu)this.getCpu()).getFlags().isNegative());

	    // test 2
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(2, this.getCpuMemory().getMemoryFromHexAddress(0x0504));
		assertFalse(((NesCpu)this.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)this.getCpu()).getFlags().isZero());
		assertFalse(((NesCpu)this.getCpu()).getFlags().isNegative());		
	}		
	
	@Test
	public void testAbsoluteX()
	{
		int instruction = 0x5E;
		
		// test 1
		this.getCpu().setX(0xA1);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(81, this.getCpuMemory().getMemoryFromHexAddress(0x02A2));
		assertFalse(((NesCpu)this.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)this.getCpu()).getFlags().isZero());
		assertFalse(((NesCpu)this.getCpu()).getFlags().isNegative());

	    // test 2
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(82, this.getCpuMemory().getMemoryFromHexAddress(0x05A5));
		assertTrue(((NesCpu)this.getCpu()).getFlags().isCarry());
		assertFalse(((NesCpu)this.getCpu()).getFlags().isZero());
		assertFalse(((NesCpu)this.getCpu()).getFlags().isNegative());		
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
