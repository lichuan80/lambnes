package com.lambelly.lambnes.util;

public class BitUtils
{
	public static String generateBinaryStringWithleadingZeros(int number, int minimumLength)
	{
		// generate mask
		String mask = "";
		for (int i=0;i<minimumLength;i++)
		{
			mask += "0";
		}

		String nString = Integer.toBinaryString(number);
		return mask.substring(0 , mask.length() - nString.length()) + nString;
	}
	
	public static boolean isBitSet(int value, int bitIndex)
	{
		if ((value & (1 << bitIndex)) != 0) 
		{ 
			return true; 
		} 
		else
		{
			return false;
		}		
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
}
