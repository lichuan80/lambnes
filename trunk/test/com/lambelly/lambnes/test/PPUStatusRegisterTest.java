package com.lambelly.lambnes.test;

import org.junit.Test;

import org.apache.log4j.*;

public class PPUStatusRegisterTest
{
	private Logger logger = Logger.getLogger(PPUStatusRegisterTest.class);
	
	@Test
	public void testWrite()
	{
		boolean a = false;
		boolean b = false;
		boolean c = false;
		boolean d = false;
		
		String bitString = (a?"1":"0") +
					(b?"1":"0") +
					(c?"1":"0") +
					(d?"1":"0") +
					"1111"; // so far as I know, d3-d0 are not used.
		logger.debug(bitString);
		int rawControlByte = Integer.parseInt(bitString,2);
		logger.debug("ppu status byte: " + Integer.toBinaryString(rawControlByte));
	}
	
	
}
