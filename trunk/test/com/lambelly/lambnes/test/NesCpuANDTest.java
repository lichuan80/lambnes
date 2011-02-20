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
import static org.junit.Assert.*;
import org.apache.log4j.*;
import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.platform.cpu.NesCpu;
import com.lambelly.lambnes.test.utils.TestUtils;

/**
 * 
 * @author thomasmccarthy
 */
public class NesCpuANDTest
{
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
		TestUtils.createTestPlatform();
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
		TestUtils.performInstruction(instruction, 0xFF);
		assertEquals(1,Platform.getCpu().getAccumulator());
		
		// test zero flag
		TestUtils.performInstruction(instruction, 0);
		assertEquals(0,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		
		// test negative flag
		TestUtils.setMemory(0x8005, 0xFF);
		TestUtils.performInstruction(instruction, 0xFF);
		logger.debug(Integer.toHexString(Platform.getCpu().getAccumulator()));
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
	}
	
	@Test
	public void testANDZeroPage()
	{
		int instruction = 0x25;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));
		// test general AND
		TestUtils.performInstruction(instruction, 0xFF);
		assertEquals(1,Platform.getCpu().getAccumulator());
		
		// test zero flag
		TestUtils.performInstruction(instruction, 0);
		assertEquals(0,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		
		// test negative flag
		int address = Platform.getCpuMemory().getProgramCounter() + 1;
		TestUtils.setMemory(address, 0xFF);
		TestUtils.performInstruction(instruction, 0xFFFF);
		logger.debug(Integer.toHexString(Platform.getCpu().getAccumulator()));
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());
	}	
	
	@Test
	public void testANDZeroPageXIndexed()
	{
		int instruction = 0x35;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test general AND
		Platform.getCpu().setX(1);
		TestUtils.performInstruction(instruction, 0xFF);
		assertEquals(2,Platform.getCpu().getAccumulator());
		
		// test zero flag
		Platform.getCpu().setX(0xFC);
		TestUtils.performInstruction(instruction,0);
		assertEquals(0,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		
		// test negative flag
		Platform.getCpu().setX(0xFA);
		TestUtils.performInstruction(instruction,0x8F);
		logger.debug(Integer.toHexString(Platform.getCpu().getAccumulator()));
		assertEquals(0x8F,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());		
	}

	@Test
	public void testANDZeroPageXIndexedIndirect()
	{
		int instruction = 0x21;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test general AND
		Platform.getCpu().setX(1);
		TestUtils.performInstruction(instruction, 0xFF);
		assertEquals(2,Platform.getCpu().getAccumulator());
		
		// test zero flag
		Platform.getCpu().setX(0xFC);
		TestUtils.performInstruction(instruction,0);
		assertEquals(0,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		
		// test negative flag
		Platform.getCpu().setX(0xFA);
		TestUtils.performInstruction(instruction,0x8F);
		logger.debug(Integer.toHexString(Platform.getCpu().getAccumulator()));
		assertEquals(0x8F,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());		
	}	

	@Test
	public void testANDZeroPageYIndirectIndexed()
	{
		int instruction = 0x31;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test general AND
		Platform.getCpu().setY(1);
		TestUtils.performInstruction(instruction, 0xFF);
		assertEquals(2,Platform.getCpu().getAccumulator());
		
		// test zero flag
		Platform.getCpu().setY(0xFC);
		TestUtils.performInstruction(instruction,0);
		assertEquals(0,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		
		// test negative flag
		Platform.getCpu().setY(0xFA);
		TestUtils.performInstruction(instruction,0x8F);
		logger.debug(Integer.toHexString(Platform.getCpu().getAccumulator()));
		assertEquals(0x8F,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());		
	}	
	
	@Test
	public void testANDAbsolute()
	{
		int instruction = 0x2D;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test general AND
		Platform.getCpuMemory().getPrgRomLowerBank()[1] = 0xFE;
		Platform.getCpuMemory().getPrgRomLowerBank()[2] = 0x02;
		TestUtils.performInstruction(instruction, 0xFF);
		assertEquals(254,Platform.getCpu().getAccumulator());
		
		// test zero flag
		TestUtils.performInstruction(instruction,0);
		assertEquals(0,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		
		// test negative flag
		Platform.getCpuMemory().getPrgRomLowerBank()[7] = 0xCD;		
		Platform.getCpuMemory().getPrgRomLowerBank()[8] = 0xCD;
		TestUtils.performInstruction(instruction,0x8F); // 07 & 8E
		logger.debug(Integer.toHexString(Platform.getCpu().getAccumulator()));
		assertEquals(141,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());		
	}	

	@Test
	public void testANDAbsoluteX()
	{
		int instruction = 0x3D;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test general AND
		Platform.getCpu().setX(0x0A);
		Platform.getCpuMemory().getPrgRomLowerBank()[1] = 0xFE;
		Platform.getCpuMemory().getPrgRomLowerBank()[2] = 0x02;
		TestUtils.performInstruction(instruction, 0xFF);
		assertEquals(8,Platform.getCpu().getAccumulator());
		
		// test zero flag
		TestUtils.performInstruction(instruction,0);
		assertEquals(0,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		
		// test negative flag
		Platform.getCpuMemory().getPrgRomLowerBank()[7] = 0xCD;		
		Platform.getCpuMemory().getPrgRomLowerBank()[8] = 0xCD;
		Platform.getCpu().setX(0xC);
		TestUtils.performInstruction(instruction,0x8F);
		logger.debug(Integer.toHexString(Platform.getCpu().getAccumulator()));
		assertEquals(137,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());		
	}		

	@Test
	public void testANDAbsoluteY()
	{
		int instruction = 0x39;

		logger.debug("testing instruction: " + Integer.toHexString(instruction));

		// test general AND
		Platform.getCpu().setY(0x0A);
		Platform.getCpuMemory().getPrgRomLowerBank()[1] = 0xFE;
		Platform.getCpuMemory().getPrgRomLowerBank()[2] = 0x02;
		TestUtils.performInstruction(instruction, 0xFF);
		assertEquals(8,Platform.getCpu().getAccumulator());
		
		// test zero flag
		TestUtils.performInstruction(instruction,0);
		assertEquals(0,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isZero());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		
		// test negative flag
		Platform.getCpuMemory().getPrgRomLowerBank()[7] = 0xCD;		
		Platform.getCpuMemory().getPrgRomLowerBank()[8] = 0xCD;
		Platform.getCpu().setY(0xC);
		TestUtils.performInstruction(instruction,0x8F);
		logger.debug(Integer.toHexString(Platform.getCpu().getAccumulator()));
		assertEquals(137,Platform.getCpu().getAccumulator());
		assertTrue(((NesCpu)Platform.getCpu()).getFlags().isNegative());
		assertFalse(((NesCpu)Platform.getCpu()).getFlags().isZero());		
	}	
}