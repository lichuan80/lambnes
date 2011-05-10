package com.lambelly.lambnes.platform.ppu.registers;

import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.util.BitUtils;

import org.apache.log4j.*;

public class PPUScrollRegister
{
	/*
	 * ----------------------------------------
	 * OOO.NN.YYYYY.XXXXX
	 * X is denoted as the X scroll counter.
	 * Y is denoted as the Y scroll counter.
	 * N is denoted the nametable select bits.
	 * O is the Y scroll offset
	 * -----------------------------------------
	 * */
	public static final int REGISTER_ADDRESS = 0x2005;
	private static PPUScrollRegister register = new PPUScrollRegister();
	private static final int CYCLES_PER_EXECUTION = 0;
	private Integer addressLowByte = null;
	private Integer addressHighByte = null;
	private Integer rawControlByte = null;
	private Logger logger = Logger.getLogger(PPUScrollRegister.class);
	
	private PPUScrollRegister()
	{
		
	}
	
	public int cycle()
	{
		if (this.getRawControlByte() != null)
		{
			logger.warn("write to register 0x2005: " + this.getRawControlByte());
		}
		
		this.clear();
		
		return PPUScrollRegister.CYCLES_PER_EXECUTION;
	}
	
	private void clear()
	{
		this.setAddressLowByte(null);
		this.setAddressHighByte(null);
		this.setRawControlByte(null);
	}
	

	
	public void setRegisterValue(int value)
	{
		this.setRawControlByte(value);
	}

	private Integer getAddressLowByte()
	{
		return addressLowByte;
	}

	private void setAddressLowByte(Integer addressLowByte)
	{
		this.addressLowByte = addressLowByte;
	}

	private Integer getAddressHighByte()
	{
		return addressHighByte;
	}

	private void setAddressHighByte(Integer addressHighByte)
	{
		this.addressHighByte = addressHighByte;
	}

	private Integer getRawControlByte()
	{
		return rawControlByte;
	}

	private void setRawControlByte(Integer rawControlByte)
	{
		this.rawControlByte = rawControlByte;
	}
	
	public static PPUScrollRegister getRegister()
	{
		return register;
	}
}
