package com.lambelly.lambnes.platform.ppu.registers;

import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.util.BitUtils;

import org.apache.log4j.*;

public class PPUScrollRegister
{
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
			int flipflop = Platform.getPpu().getRegisterAddressFlipFlopLatch();
			int loopyT = Platform.getPpu().getLoopyT();
			int setLoopyT = 0;
			
			if (flipflop == 0)
			{
				setLoopyT = ((loopyT & 0x7FE0) | ((this.getRawControlByte() & 0xF8) >> 3));
			}
			else
			{
				setLoopyT = (Platform.getPpu().getLoopyT() & 0x8C1F) | 
					(((this.getRawControlByte() & 7) << 12) | 
					((this.getRawControlByte() & 0xF8) << 2));
			}
			
			if (logger.isDebugEnabled())
			{
				logger.debug("write to register 0x2005: " + this.getRawControlByte() + "\n" +
						"flipflop is " + flipflop + "\n" + 
						"loopyT is: " + loopyT + "\n" +
						"setting loopyT to: " + setLoopyT + "\n" +
						"at scanline " + Platform.getPpu().getScanlineCount() + " screencount: " + Platform.getPpu().getScreenCount() + " cpu cycle: " + Platform.getCycleCount());
			}
			
			if (flipflop == 0)
			{				
				Platform.getPpu().setLoopyX(this.getRawControlByte() & 7);
				Platform.getPpu().setLoopyT(setLoopyT);
				this.setRawControlByte(null);
			}
			else
			{
				Platform.getPpu().setLoopyT(setLoopyT);
				this.clear();
			}
		}
		
		return PPUScrollRegister.CYCLES_PER_EXECUTION;
	}
	
	private void clear()
	{
		this.setAddressLowByte(null);
		this.setAddressHighByte(null);
		this.setRawControlByte(null);
	}
	
	public String toString()
	{
		if (this.getRawControlByte() != null)
		{
			return "0x" + REGISTER_ADDRESS + ": " + Integer.toBinaryString(this.getRawControlByte());
		}
		else
		{
			return "0x" + REGISTER_ADDRESS + ": 0";
		}
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
