package com.lambelly.lambnes.platform.ppu.registers;

import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.util.BitUtils;

import org.apache.log4j.*;

public class PPUVramAddressRegister
{
	public static final int REGISTER_ADDRESS = 0x2006;
	private static PPUVramAddressRegister register = new PPUVramAddressRegister();
	private static final int CYCLES_PER_EXECUTION = 0;
	private Integer addressLowByte = null;
	private Integer addressHighByte = null;
	private Integer rawControlByte = null;
	private Logger logger = Logger.getLogger(PPUVramAddressRegister.class);
	
	private PPUVramAddressRegister()
	{
		
	}
	
	public int cycle()
	{
		if (this.getRawControlByte() != null)
		{
			if(logger.isDebugEnabled())
			{
				logger.debug("raw control byte sent to PPU VRAM Register: " + this.getRawControlByte());
				logger.debug("background visibility: " + Platform.getPpu().getPpuMaskRegister().isBackgroundVisibility());
				logger.debug("sprite visibility: " + Platform.getPpu().getPpuMaskRegister().isSpriteVisibility());
			}
			 
			if (Platform.getPpu().getPpuStatusRegister().isVblank() || (!Platform.getPpu().getPpuMaskRegister().isBackgroundVisibility()) && !Platform.getPpu().getPpuMaskRegister().isSpriteVisibility())
			{
				// do memory access on vblank.
				if (Platform.getPpu().getRegisterAddressFlipFlopLatch() == 1)
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
					// latch returned 0
					if(logger.isDebugEnabled())
					{
						logger.debug("setting high bit: " + this.getRawControlByte());
					}
					this.setAddressHighByte(this.getRawControlByte());
					this.setRawControlByte(null);
				}
			}
			else
			{
				logger.warn("byte sent to 0x2006 during rendering: " + this.getRawControlByte());
				this.setRawControlByte(null);
			}
		}
		
		return PPUVramAddressRegister.CYCLES_PER_EXECUTION;
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
	
	public static PPUVramAddressRegister getRegister()
	{
		return register;
	}
}
