package com.lambelly.lambnes.platform.controllers;

public class ControlRegister2
{
	private Integer rawControlByte = null;
	private int strobeValue = 0;
	public static final int REGISTER_ADDRESS = 0x4017;
	
	private ControlRegister2()
	{
	
	}
	
	public void cycle()
	{
		if (this.getRawControlByte() != null)
		{
			this.clear();
		}
	}
	
	private void clear()
	{
		this.setRawControlByte(null);
	}
	
	public void setRegisterValue(int value)
	{
		this.setRawControlByte(value);
	}
	
	public int getRegisterValue()
	{
		return this.getStrobeValue();
	}

	private Integer getRawControlByte()
	{
		return rawControlByte;
	}

	private void setRawControlByte(Integer rawControlByte)
	{
		this.rawControlByte = rawControlByte;
	}

	public int getStrobeValue()
	{
		return strobeValue;
	}

	public void setStrobeValue(int strobeValue)
	{
		this.strobeValue = strobeValue;
	}
	
}
