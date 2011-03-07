package com.lambelly.lambnes.util;
import org.apache.log4j.*;

public class BitUtils
{
	private static Logger logger = Logger.getLogger(BitUtils.class);
	
	public static boolean isBitSet(int value, int bitIndex)
	{
		return ((value & (1 << bitIndex)) != 0); 
	}
	
	public static int setBit(int value, int bitIndex)
	{
		return (value | (1 << bitIndex));
	}
	
	public static int unsetBit(int value, int bitIndex)
	{
		return (value & ~(1 << bitIndex));
	}
	
	public static int flipBit(int value, int bitIndex)
	{
		if (isBitSet(value,bitIndex))
		{
			return unsetBit(value,bitIndex);
		}
		else
		{
			return setBit(value,bitIndex);
		}
	}
	
	public static int[] splitAddress(int address)
	{
		int[] a = new int[2];
		String hexString = NumberConversionUtils.generateHexStringWithleadingZeros(address, 4);
		int higherBit = Integer.parseInt(hexString.substring(0,2),16); 
		int lowerBit = Integer.parseInt(hexString.substring(2, 4),16);
		a[0] = lowerBit;
		a[1] = higherBit;
		return a;
	}
	
	/**
	 * 
	 * @param highbyte
	 * @param lowbyte
	 * @return
	 */
	public static int unsplitAddress(int highbyte, int lowbyte)
	{
		return (highbyte << 8) + lowbyte;
	}
}
