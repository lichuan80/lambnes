package com.lambelly.lambnes.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.log4j.*;
import java.util.BitSet;
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
	public void bitStringTest()
	{
		logger.debug(BitUtils.generateBinaryStringWithleadingZeros(3, 8));
	}
}
