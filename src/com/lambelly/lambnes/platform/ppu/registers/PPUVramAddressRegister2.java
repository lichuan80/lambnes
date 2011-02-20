package com.lambelly.lambnes.platform.ppu.registers;

import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.util.BitUtils;

import org.apache.log4j.*;

public class PPUVramAddressRegister2
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
	public static final int REGISTER_ADDRESS = 0x2006;
	private static PPUVramAddressRegister2 register = new PPUVramAddressRegister2();
	private Integer addressLowByte = null;
	private Integer addressHighByte = null;
	private Integer rawControlByte = null;
	private Logger logger = Logger.getLogger(PPUVramAddressRegister2.class);
	
	private PPUVramAddressRegister2()
	{
		
	}
	
	public void cycle()
	{
		if (this.getRawControlByte() != null)
		{
			if(logger.isDebugEnabled())
			{
				logger.debug("raw control byte sent to PPU VRAM Register: " + this.getRawControlByte());
			}
			
			if (this.getAddressHighByte() != null)
			{
				if(logger.isDebugEnabled())
				{
					logger.debug("setting low bit: " + this.getRawControlByte());
				}
				
				this.setAddressLowByte(this.getRawControlByte());

				// both are set, so set 0x2007
				Platform.getPpu().getPpuVramIORegister().setIoAddress(BitUtils.unsplitAddress(this.getAddressHighByte(), this.getAddressLowByte()));
				if(logger.isDebugEnabled())
				{
					logger.debug("io address: " + BitUtils.unsplitAddress(this.getAddressHighByte(), this.getAddressLowByte()));
				}
				
				// clear? I suppose it makes sense.
				this.clear();
			}
			else
			{
				// high bit unset, so set it
				if(logger.isDebugEnabled())
				{
					logger.debug("setting high bit: " + this.getRawControlByte());
				}
				this.setAddressHighByte(this.getRawControlByte());
				this.setRawControlByte(null);
			}
			
		}
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
	
	public static PPUVramAddressRegister2 getRegister()
	{
		return register;
	}
}
