package com.lambelly.lambnes.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.log4j.*;
import com.lambelly.lambnes.util.*;

public class BitUtilsTest
{
	private Logger logger = Logger.getLogger(BitUtilsTest.class);
	@Before
	public void setUp() throws Exception
	{
	}

	@Test
	public void setBitTest()
	{
		int value = 2;
		value = BitUtils.setBit(value, 0);
		assertEquals(3,value);
	}
	
	@Test
	public void setBitTest2()
	{
		int value = 0;
		value = BitUtils.setBit(value, 7);
		assertEquals(0x80,value);
	}
	
	
	@Test
	public void assureBitTest()
	{
		int value = 2;
		boolean lsb = true;
    	if (lsb)
    	{
    		value = BitUtils.setBit(value, 0);
    	}
    	else
    	{
    		value = BitUtils.unsetBit(value, 0);
    	}    	
    	assertEquals(3,value);
    	
    	value = 2;
    	lsb = false;
    	if (lsb)
    	{
    		value = BitUtils.setBit(value, 0);
    	}
    	else
    	{
    		value = BitUtils.unsetBit(value, 0);
    	}
    	assertEquals(2,value);
	}
	
	@Test
	public void splitAddress()
	{
		int[] a = BitUtils.splitAddress(0xabcd);
		assertEquals(0xcd,a[0]);
		assertEquals(0xab,a[1]);
	}
	
	@Test
	public void isBitSet()
	{
		assertFalse(BitUtils.isBitSet(2,7));
	}
	
	@Test
	public void isBitSet2()
	{
		assertTrue(BitUtils.isBitSet(24,4));
	}	
}
