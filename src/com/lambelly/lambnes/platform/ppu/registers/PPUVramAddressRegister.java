package com.lambelly.lambnes.platform.ppu.registers;

import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.platform.ppu.NesPpu;
import com.lambelly.lambnes.util.BitUtils;

import org.apache.log4j.*;

public class PPUVramAddressRegister
{
	public static final int REGISTER_ADDRESS = 0x2006;
	private static final int CYCLES_PER_EXECUTION = 0;
	private Integer addressLowByte = null;
	private Integer addressHighByte = null;
	private Integer rawControlByte = null;
	private Logger logger = Logger.getLogger(PPUVramAddressRegister.class);
	private NesPpu ppu;
    private PPUVramIORegister ppuVramIORegister;
	
	private PPUVramAddressRegister()
	{
		
	}
	
	public int cycle()
	{
		if (this.getRawControlByte() != null)
		{			 
			//if (Platform.getPpu().getPpuStatusRegister().isVblank() || (!Platform.getPpu().getPpuMaskRegister().isBackgroundVisibility()) && !Platform.getPpu().getPpuMaskRegister().isSpriteVisibility())
			{
				int flipflop = this.getPpu().getRegisterAddressFlipFlopLatch();
				int loopyT = this.getPpu().getLoopyT();
				int setLoopyT = 0;
					
				// do memory access on vblank.
				if (flipflop == 1)
				{
					//logger.debug("setting low bit: " + this.getRawControlByte());
					
					this.setAddressLowByte(this.getRawControlByte());
	
					// both are set, so set 0x2007
					this.getPpuVramIORegister().setIoAddress(BitUtils.unsplitAddress(this.getAddressHighByte(), this.getAddressLowByte()));
					
					// perform loopyT changes
					setLoopyT = (loopyT & 0xFF00) | (this.getRawControlByte());
					this.getPpu().setLoopyT(setLoopyT);
					this.getPpu().setLoopyV(this.getPpu().getLoopyT());
					
					//logger.debug("io address: " + BitUtils.unsplitAddress(this.getAddressHighByte(), this.getAddressLowByte()));
				}
				else
				{
					// latch returned 0
					if(logger.isDebugEnabled())
					{
						logger.debug("setting high bit: " + this.getRawControlByte());
					}
					
					// perform loopyT changes
					setLoopyT = loopyT & 0xBFFF; // clear D14
					setLoopyT = (loopyT & 0xC0FF) | (this.getRawControlByte() & 0x3F);
					this.getPpu().setLoopyT(setLoopyT);
				}
				
				if (logger.isDebugEnabled())
				{
					logger.debug("write to register 0x2006: " + this.getRawControlByte() + "\n" +
							"flipflop is " + flipflop + "\n" + 
							"loopyT is: " + loopyT + "\n" +
							"setting loopyT to: " + setLoopyT + "\n" +
							"at scanline " + this.getPpu().getScanlineCount() + " screencount: " + this.getPpu().getScreenCount() + " cpu cycle: " + Platform.getCycleCount());
				}
				
				if (flipflop == 1)
				{
					// clear? I suppose it makes sense.
					this.clear();					
				}
				else
				{
					this.setAddressHighByte(this.getRawControlByte());
					this.setRawControlByte(null);
				}
			}
			//else
			//{
				//logger.warn("byte sent to 0x2006 during rendering: " + this.getRawControlByte());
				//this.setRawControlByte(null);
			//}
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

	public NesPpu getPpu()
    {
    	return ppu;
    }

	public void setPpu(NesPpu ppu)
    {
    	this.ppu = ppu;
    }

	public PPUVramIORegister getPpuVramIORegister()
    {
    	return ppuVramIORegister;
    }

	public void setPpuVramIORegister(PPUVramIORegister ppuVramIORegister)
    {
    	this.ppuVramIORegister = ppuVramIORegister;
    }
}
