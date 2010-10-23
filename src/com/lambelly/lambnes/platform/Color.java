package com.lambelly.lambnes.platform;

public class Color
{
	int red = 0;
	int green = 0;
	int blue = 0;
	
	public Color(int red,int blue,int green)
	{
		this.setRed(red);
		this.setBlue(blue);
		this.setGreen(green);
	}
	
	public Color(String red,String blue,String green)
	{
		this.setRed(red);
		this.setBlue(blue);
		this.setGreen(green);
	}
	
	public String toString()
	{
		return "red: 0x" + Integer.toHexString(this.getRed()) + "\n" +
			"blue: 0x" + Integer.toHexString(this.getBlue()) + "\n" +
			"green: 0x" + Integer.toHexString(this.getGreen()) + "\n";
	}
	
	public int getRed()
	{
		return red;
	}
	public void setRed(int red)
	{
		this.red = red;
	}
	public void setRed(String red)
	{
		this.red = Integer.parseInt(red,16);
	}
	public int getGreen()
	{
		return green;
	}
	public void setGreen(int green)
	{
		this.green = green;
	}
	public void setGreen(String green)
	{
		this.green = Integer.parseInt(green,16);
	}
	public int getBlue()
	{
		return blue;
	}
	public void setBlue(int blue)
	{
		this.blue = blue;
	}
	public void setBlue(String blue)
	{
		this.blue = Integer.parseInt(blue,16);
	}	
}
