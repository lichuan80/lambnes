package com.lambelly.lambnes.test;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

import com.lambelly.lambnes.platform.*;
import com.lambelly.lambnes.platform.cpu.NesCpu;
import com.lambelly.lambnes.platform.cpu.NesCpuMemory;
import com.lambelly.lambnes.test.utils.TestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:beans.xml"})
public class NesCpuEORTest
{
	@Autowired
	private NesCpu cpu;
	@Autowired
	private TestUtils testUtils;
	
	@Before
	public void setUp() throws Exception
	{
		this.getTestUtils().createTestPlatform();
	}
	
	@Test
	public void testImmediate()
	{
		// test 1
		int instruction = 0x49;
		this.getCpu().setAccumulator(0xFF);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0xFE,this.getCpu().getAccumulator());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
		
		// test 2
		this.getTestUtils().setMemory(0x8003, 0xFE);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0,this.getCpu().getAccumulator());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertTrue(this.getCpu().getFlags().isZero());	
	}
	
	@Test
	public void testZeroPage()
	{
		// test 1
		int instruction = 0x45;
		this.getCpu().setAccumulator(0xCE);
		this.getTestUtils().setMemory(0x8001, 0xA9);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x67,this.getCpu().getAccumulator());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
		
		// test 2
		this.getTestUtils().setMemory(0x03, 0xFE);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x99,this.getCpu().getAccumulator());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());			
	}
	
	@Test
	public void testZeroPageX()
	{
		// test 1
		int instruction = 0x55;
		this.getCpu().setAccumulator(0xCE);
		this.getTestUtils().setMemory(0x8001, 0xA9);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x67,this.getCpu().getAccumulator());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
		
		// test 2
		this.getTestUtils().setMemory(0x03, 0xFE);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x99,this.getCpu().getAccumulator());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());		
	}
	
	@Test
	public void testAbsolute()
	{
		int instruction = 0x4D;
		
		// test 1
		this.getCpu().setAccumulator(0x4e);
		this.getTestUtils().setMemory(0x0201, 0xbd);
		this.getTestUtils().performInstruction(instruction);

		assertEquals(0xF3,this.getCpu().getAccumulator());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
		
		// test 2
		this.getCpu().setAccumulator(0x1e);
		this.getTestUtils().setMemory(0x0504, 0x36);
		this.getTestUtils().performInstruction(instruction);

		assertEquals(0x28,this.getCpu().getAccumulator());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());		
	}

	@Test
	public void testAbsoluteX()
	{
		int instruction = 0x5D;
		
		// test 1
		this.getCpu().setAccumulator(0x4e);
		this.getCpu().setX(0xA);
		this.getTestUtils().setMemory(0x020B, 0xbd);
		this.getTestUtils().performInstruction(instruction);

		assertEquals(0xF3,this.getCpu().getAccumulator());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
		
		// test 2
		this.getCpu().setAccumulator(0x1e);
		this.getCpu().setX(0x9);
		this.getTestUtils().setMemory(0x050D, 0x36);
		this.getTestUtils().performInstruction(instruction);

		assertEquals(0x28,this.getCpu().getAccumulator());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());		
	}
	
	@Test
	public void testAbsoluteY()
	{
		int instruction = 0x59;
		
		// test 1
		this.getCpu().setAccumulator(0x4e);
		this.getCpu().setY(0xA);
		this.getTestUtils().setMemory(0x020B, 0xbd);
		this.getTestUtils().performInstruction(instruction);

		assertEquals(0xF3,this.getCpu().getAccumulator());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
		
		// test 2
		this.getCpu().setAccumulator(0x1e);
		this.getCpu().setY(0x9);
		this.getTestUtils().setMemory(0x050D, 0x36);
		this.getTestUtils().performInstruction(instruction);

		assertEquals(0x28,this.getCpu().getAccumulator());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());		
	}	

	@Test
	public void testIndexedIndirect()
	{
		int instruction = 0x41;
		
		// test 1
		this.getCpu().setAccumulator(0x4e);
		this.getCpu().setX(0xA);
		this.getTestUtils().setMemory(0x000B, 0xbd);
		this.getTestUtils().performInstruction(instruction);

		assertEquals(0xF3,this.getCpu().getAccumulator());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
		
		// test 2
		this.getCpu().setAccumulator(0x1e);
		this.getCpu().setX(0x9);
		this.getTestUtils().setMemory(0x000C, 0x36);
		this.getTestUtils().performInstruction(instruction);

		assertEquals(0x28,this.getCpu().getAccumulator());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());		
	}
	
	@Test
	public void testIndirectIndexed()
	{
		int instruction = 0x51;
		
		// test 1
		this.getCpu().setAccumulator(0x4e);
		this.getCpu().setY(0xA);
		this.getTestUtils().setMemory(0x020B, 0xbd);
		this.getTestUtils().performInstruction(instruction);

		assertEquals(0xF3,this.getCpu().getAccumulator());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
		
		// test 2
		this.getCpu().setAccumulator(0x1e);
		this.getCpu().setY(0x9);
		this.getTestUtils().setMemory(0x040C, 0x36);
		this.getTestUtils().performInstruction(instruction);

		assertEquals(0x28,this.getCpu().getAccumulator());
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

	public TestUtils getTestUtils()
    {
    	return testUtils;
    }

	public void setTestUtils(TestUtils testUtils)
    {
    	this.testUtils = testUtils;
    }	
}
