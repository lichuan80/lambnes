package com.lambelly.lambnes.util;

public class NumberConversionUtils
{
	public static String generateBinaryStringWithleadingZeros(int number, int minimumLength)
	{
		String nString = Integer.toBinaryString(number);
		return pad(nString,minimumLength);
	}
	
	public static String generateHexStringWithleadingZeros(int number, int minimumLength)
	{
		String nString = Integer.toHexString(number);
		return pad(nString,minimumLength);
	}
	
	private static String pad(String nString, int minimumLength)
	{
		// generate mask
		String mask = "";
		for (int i=0;i<minimumLength;i++)
		{
			mask += "0";
		}	
		
		return mask.substring(0 , mask.length() - nString.length()) + nString;
	}
}
