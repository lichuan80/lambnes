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
public class NesCpuLDATest
{
	@Autowired
	private NesCpu cpu;
	@Autowired
	private NesCpuMemory cpuMemory;
	@Autowired
	private TestUtils testUtils;
	
	@Before
	public void setUp() throws Exception
	{
		this.getTestUtils().createTestPlatform();
	}
	
	// LDA
	@Test
	public void testLdaImmediate()
	{
		int instruction = 0xA9;
		
		// test 1
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x01,this.getCpu().getAccumulator());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
		
		// test 2
		this.getTestUtils().setMemory(0x8003, 0xCC);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0xCC,this.getCpu().getAccumulator());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());		
	}
	
	@Test
	public void testLdaZeroPage()
	{
		int instruction = 0xA5;
		
		// test 1
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x01,this.getCpu().getAccumulator());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
		
		// test 2
		this.getTestUtils().setMemory(0x8003, 0xCC);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0xCC,this.getCpu().getAccumulator());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());		
	}
	
	@Test
	public void testLdaZeroPageX()
	{
		int instruction = 0xB5;
		
		// test 1
		this.getCpu().setX(0xA1);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0xA2,this.getCpu().getAccumulator());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
		
		// test 2
		this.getCpu().setX(0x31);
		this.getTestUtils().setMemory(0x8003, 0xCC);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0xFD,this.getCpu().getAccumulator());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());		
	}	
	
	@Test
	public void testLdaAbsolute()
	{
		int instruction = 0xAD;
		
		// test 1
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x0001,this.getCpu().getAccumulator());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
		
		// test 2
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x0004,this.getCpu().getAccumulator());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());		
	}	
	
	public void testLdaAbsoluteX()
	{
		int instruction = 0xB9;
		
		// test 1
		this.getCpu().setX(0xA1);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x00A2,this.getCpu().getAccumulator());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
		
		// test 2
		this.getTestUtils().performInstruction(instruction);
		this.getCpu().setX(0x1F);
		
		assertEquals(0x0023,this.getCpu().getAccumulator());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());		
	}	
	
	@Test
	public void testLdaIndexedIndirect()
	{
		int instruction = 0xA1;
		
		// test 1
		this.getCpu().setX(0xA1);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x00A2,this.getCpu().getAccumulator());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
		
		// test 2
		this.getCpu().setX(0x1F);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x002,this.getCpu().getAccumulator());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());		
	}	
	
	@Test
	public void testLdaIndirectIndexed()
	{
		int instruction = 0xB1;
		
		// test 1
		this.getCpu().setY(0x01);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x0002,this.getCpu().getAccumulator());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
		
		// TODO: Not sure why this has this value.
		// test 2
		this.getCpu().setY(0x1F);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x022,this.getCpu().getAccumulator());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());		
	}	
	
	// LDX
	@Test
	public void testLdxImmediate()
	{
		int instruction = 0xA2;
		
		// test 1
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x1,this.getCpu().getX());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
		
		// test 2
		this.getTestUtils().setMemory(0x8003, 0xCE);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0xCE,this.getCpu().getX());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());		
	}	
	
	@Test
	public void testLdxZeroPage()
	{
		int instruction = 0xA6;
		
		// test 1
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x1,this.getCpu().getX());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
		
		// test 2
		this.getTestUtils().setMemory(0x8003, 0xCE);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0xCE,this.getCpu().getX());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());		
	}
	
	@Test
	public void testLdxZeroPageY()
	{
		int instruction = 0xB6;
		
		// test 1
		this.getCpu().setY(0xA);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0xB,this.getCpu().getX());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
		
		// test 2
		this.getTestUtils().setMemory(0x8003, 0xCE);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0xD8,this.getCpu().getX());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());		
	}
	
	@Test
	public void testLdxAbsolute()
	{
		int instruction = 0xAE;
		
		// test 1
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x1,this.getCpu().getX());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
		
		// test 2
		this.getTestUtils().setMemory(0x8003, 0xCE);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x04,this.getCpu().getX());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());		
	}	
	
	@Test
	public void testLdxAbsoluteY()
	{
		int instruction = 0xBE;
		
		// test 1
		this.getCpu().setY(0xA1);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0xA2,this.getCpu().getX());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
		
		// test 2
		this.getTestUtils().setMemory(0x8003, 0xCE);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0xA5,this.getCpu().getX());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());		
	}		
	
	// LDY
	@Test
	public void testLdyImmediate()
	{
		int instruction = 0xA0;
		
		// test 1
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x1,this.getCpu().getY());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
		
		// test 2
		this.getTestUtils().setMemory(0x8003, 0xCE);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0xCE,this.getCpu().getY());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());		
	}	
	
	@Test
	public void testLdyZeroPage()
	{
		int instruction = 0xA4;
		
		// test 1
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x1,this.getCpu().getY());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
		
		// test 2
		this.getTestUtils().setMemory(0x8003, 0xCE);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0xCE,this.getCpu().getY());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());		
	}
	
	@Test
	public void testLdyZeroPageY()
	{
		int instruction = 0xB4;
		
		// test 1
		this.getCpu().setX(0xA);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0xB,this.getCpu().getY());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
		
		// test 2
		this.getTestUtils().setMemory(0x8003, 0xCE);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0xD8,this.getCpu().getY());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());		
	}
	
	@Test
	public void testLdyAbsolute()
	{
		int instruction = 0xAC;
		
		// test 1
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x1,this.getCpu().getY());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
		
		// test 2
		this.getTestUtils().setMemory(0x8003, 0xCE);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0x04,this.getCpu().getY());
		assertFalse(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());		
	}	
	
	@Test
	public void testLdyAbsoluteX()
	{
		int instruction = 0xBC;
		
		// test 1
		this.getCpu().setX(0xA1);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0xA2,this.getCpu().getY());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
		
		// test 2
		this.getTestUtils().setMemory(0x8003, 0xCE);
		this.getTestUtils().performInstruction(instruction);
		
		assertEquals(0xA5,this.getCpu().getY());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());		
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

	public NesCpu getCpu()
    {
    	return cpu;
    }

	public void setCpu(NesCpu cpu)
    {
    	this.cpu = cpu;
    }		
}
