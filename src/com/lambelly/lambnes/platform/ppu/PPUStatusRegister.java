package com.lambelly.lambnes.platform.ppu;

import com.lambelly.lambnes.platform.Platform;
import org.apache.log4j.*;

public class PPUStatusRegister
{
	public static final int REGISTER_ADDRESS = 0x2002;
	private boolean vblank = false;
	private boolean sprite0Occurance = false;
	private boolean scanlineSpriteCount = false;
	private boolean vramWriteFlag = false;
	private int rawControlByte = 0;
	private Logger logger = Logger.getLogger(PPUStatusRegister.class);
	
	public void write()
	{
		String bitString = (this.isVblank()?"1":"0") +
					(this.isSprite0Occurance()?"1":"0") +
					(this.isScanlineSpriteCount()?"1":"0") +
					(this.isVramWriteFlag()?"1":"0") +
					"1111"; // so far as I know, d3-d0 are not used.
		this.setRawControlByte(Integer.parseInt(bitString,2));
		logger.debug("ppu status byte: " + Integer.toBinaryString(this.getRawControlByte()));
		Platform.getCpuMemory().setMemoryFromHexAddress(PPUStatusRegister.REGISTER_ADDRESS, this.getRawControlByte());
	}
	
	public boolean isVblank()
	{
		boolean vblank = this.vblank;
		this.setVblank(false);
		return vblank;
	}
	public void setVblank(boolean vblank)
	{
		this.vblank = vblank;
	}
	public boolean isSprite0Occurance()
	{
		return sprite0Occurance;
	}
	public void setSprite0Occurance(boolean sprite0Occurance)
	{
		this.sprite0Occurance = sprite0Occurance;
	}
	public boolean isScanlineSpriteCount()
	{
		return scanlineSpriteCount;
	}
	public void setScanlineSpriteCount(boolean scanlineSpriteCount)
	{
		this.scanlineSpriteCount = scanlineSpriteCount;
	}
	public boolean isVramWriteFlag()
	{
		return vramWriteFlag;
	}
	public void setVramWriteFlag(boolean vramWriteFlag)
	{
		this.vramWriteFlag = vramWriteFlag;
	}

	public int getRawControlByte()
	{
		return rawControlByte;
	}

	public void setRawControlByte(int rawControlByte)
	{
		this.rawControlByte = rawControlByte;
	}
	
	
	
}
