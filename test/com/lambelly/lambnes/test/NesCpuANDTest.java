/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lambelly.lambnes.test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
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

/**
 * 
 * @author thomasmccarthy
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:beans.xml"})
public class NesCpuANDTest
{
	@Autowired
	private NesCpu cpu;
	@Autowired
	private NesCpuMemory cpuMemory;
	@Autowired
	private TestUtils testUtils;
	private Logger logger = Logger.getLogger(NesCpuANDTest.class);
	public NesCpuANDTest()
	{
	}

	@BeforeClass
	public static void setUpClass() throws Exception
	{
	}

	@AfterClass
	public static void tearDownClass() throws Exception
	{
	}

	@Before
	public void setUp()throws Exception
	{
		this.getTestUtils().createTestPlatform();
	}

	@After
	public void tearDown()
	{
	}

	@Test
	public void testANDImmediate()
	{
		int instruction = 0x29;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));
		// test general AND
		this.getTestUtils().performInstruction(instruction, 0xFF);
		assertEquals(1,this.getCpu().getAccumulator());
		
		// test zero flag
		this.getTestUtils().performInstruction(instruction, 0);
		assertEquals(0,this.getCpu().getAccumulator());
		assertTrue(this.getCpu().getFlags().isZero());
		assertFalse(this.getCpu().getFlags().isNegative());
		
		// test negative flag
		this.getTestUtils().setMemory(0x8005, 0xFF);
		this.getTestUtils().performInstruction(instruction, 0xFF);
		logger.debug(Integer.toHexString(this.getCpu().getAccumulator()));
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
	}
	
	@Test
	public void testANDZeroPage()
	{
		int instruction = 0x25;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));
		// test general AND
		this.getTestUtils().performInstruction(instruction, 0xFF);
		assertEquals(1,this.getCpu().getAccumulator());
		
		// test zero flag
		this.getTestUtils().performInstruction(instruction, 0);
		assertEquals(0,this.getCpu().getAccumulator());
		assertTrue(this.getCpu().getFlags().isZero());
		assertFalse(this.getCpu().getFlags().isNegative());
		
		// test negative flag
		int address = this.getCpuMemory().getProgramCounter() + 1;
		this.getTestUtils().setMemory(address, 0xFF);
		this.getTestUtils().performInstruction(instruction, 0xFFFF);
		logger.debug(Integer.toHexString(this.getCpu().getAccumulator()));
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());
	}	
	
	@Test
	public void testANDZeroPageXIndexed()
	{
		int instruction = 0x35;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test general AND
		this.getCpu().setX(1);
		this.getTestUtils().performInstruction(instruction, 0xFF);
		assertEquals(2,this.getCpu().getAccumulator());
		
		// test zero flag
		this.getCpu().setX(0xFC);
		this.getTestUtils().performInstruction(instruction,0);
		assertEquals(0,this.getCpu().getAccumulator());
		assertTrue(this.getCpu().getFlags().isZero());
		assertFalse(this.getCpu().getFlags().isNegative());
		
		// test negative flag
		this.getCpu().setX(0xFA);
		this.getTestUtils().performInstruction(instruction,0x8F);
		logger.debug(Integer.toHexString(this.getCpu().getAccumulator()));
		assertEquals(0x8F,this.getCpu().getAccumulator());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());		
	}

	@Test
	public void testANDZeroPageXIndexedIndirect()
	{
		int instruction = 0x21;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test general AND
		this.getCpu().setX(1);
		this.getTestUtils().performInstruction(instruction, 0xFF);
		assertEquals(2,this.getCpu().getAccumulator());
		
		// test zero flag
		this.getCpu().setX(0xFC);
		this.getTestUtils().performInstruction(instruction,0);
		assertEquals(0,this.getCpu().getAccumulator());
		assertTrue(this.getCpu().getFlags().isZero());
		assertFalse(this.getCpu().getFlags().isNegative());
		
		// test negative flag
		this.getCpu().setX(0xFA);
		this.getTestUtils().performInstruction(instruction,0x8F);
		logger.debug(Integer.toHexString(this.getCpu().getAccumulator()));
		assertEquals(0x8F,this.getCpu().getAccumulator());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());		
	}	

	@Test
	public void testANDZeroPageYIndirectIndexed()
	{
		int instruction = 0x31;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test general AND
		this.getCpu().setY(1);
		this.getTestUtils().performInstruction(instruction, 0xFF);
		assertEquals(2,this.getCpu().getAccumulator());
		
		// test zero flag
		this.getCpu().setY(0xFC);
		this.getTestUtils().performInstruction(instruction,0);
		assertEquals(0,this.getCpu().getAccumulator());
		assertTrue(this.getCpu().getFlags().isZero());
		assertFalse(this.getCpu().getFlags().isNegative());
		
		// test negative flag
		this.getCpu().setY(0xFA);
		this.getTestUtils().performInstruction(instruction,0x8F);
		logger.debug(Integer.toHexString(this.getCpu().getAccumulator()));
		assertEquals(0x8F,this.getCpu().getAccumulator());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());		
	}	
	
	@Test
	public void testANDAbsolute()
	{
		int instruction = 0x2D;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test general AND
		this.getCpuMemory().setMemoryFromHexAddress(0x8001,0xFE);
		this.getCpuMemory().setMemoryFromHexAddress(0x8002,0x02);
		this.getTestUtils().performInstruction(instruction, 0xFF);
		assertEquals(254,this.getCpu().getAccumulator());
		
		// test zero flag
		this.getTestUtils().performInstruction(instruction,0);
		assertEquals(0,this.getCpu().getAccumulator());
		assertTrue(this.getCpu().getFlags().isZero());
		assertFalse(this.getCpu().getFlags().isNegative());
		
		// test negative flag
		this.getCpuMemory().setMemoryFromHexAddress(0x8007,0xCD);		
		this.getCpuMemory().setMemoryFromHexAddress(0x8008,0xCD);
		this.getTestUtils().performInstruction(instruction,0x8F); // 07 & 8E
		logger.debug(Integer.toHexString(this.getCpu().getAccumulator()));
		assertEquals(141,this.getCpu().getAccumulator());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());		
	}	

	@Test
	public void testANDAbsoluteX()
	{
		int instruction = 0x3D;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test general AND
		this.getCpu().setX(0x0A);
		this.getCpuMemory().setMemoryFromHexAddress(0x8001,0xFE);
		this.getCpuMemory().setMemoryFromHexAddress(0x8002,0x02);
		this.getTestUtils().performInstruction(instruction, 0xFF);
		assertEquals(8,this.getCpu().getAccumulator());
		
		// test zero flag
		this.getTestUtils().performInstruction(instruction,0);
		assertEquals(0,this.getCpu().getAccumulator());
		assertTrue(this.getCpu().getFlags().isZero());
		assertFalse(this.getCpu().getFlags().isNegative());
		
		// test negative flag
		this.getCpuMemory().setMemoryFromHexAddress(0x8007,0xCD);		
		this.getCpuMemory().setMemoryFromHexAddress(0x8008,0xCD);
		this.getCpu().setX(0xC);
		this.getTestUtils().performInstruction(instruction,0x8F);
		logger.debug(Integer.toHexString(this.getCpu().getAccumulator()));
		assertEquals(137,this.getCpu().getAccumulator());
		assertTrue(this.getCpu().getFlags().isNegative());
		assertFalse(this.getCpu().getFlags().isZero());		
	}		

	@Test
	public void testANDAbsoluteY()
	{
		int instruction = 0x39;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test general AND
		this.getCpu().setY(0x0A);
		this.getCpuMemory().setMemoryFromHexAddress(0x8001,0xFE);
		this.getCpuMemory().setMemoryFromHexAddress(0x8002,0x02);
		this.getTestUtils().performInstruction(instruction, 0xFF);
		assertEquals(8,this.getCpu().getAccumulator());
		
		// test zero flag
		this.getTestUtils().performInstruction(instruction,0);
		assertEquals(0,this.getCpu().getAccumulator());
		assertTrue(this.getCpu().getFlags().isZero());
		assertFalse(this.getCpu().getFlags().isNegative());
		
		// test negative flag
		this.getCpuMemory().setMemoryFromHexAddress(0x8007,0xCD);		
		this.getCpuMemory().setMemoryFromHexAddress(0x8008,0xCD);
		this.getCpu().setY(0xC);
		this.getTestUtils().performInstruction(instruction,0x8F);
		logger.debug(Integer.toHexString(this.getCpu().getAccumulator()));
		assertEquals(137,this.getCpu().getAccumulator());
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