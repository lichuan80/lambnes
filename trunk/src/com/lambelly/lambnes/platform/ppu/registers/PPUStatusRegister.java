package com.lambelly.lambnes.platform.ppu.registers;

import com.lambelly.lambnes.platform.Platform;
import org.apache.log4j.*;

public class PPUStatusRegister
{
	public static final int REGISTER_ADDRESS = 0x2002;
	private static final int CYCLES_PER_EXECUTION = 0;
	private static PPUStatusRegister register = new PPUStatusRegister();
	private boolean vblank = false;
	private boolean sprite0Occurance = false;
	private boolean scanlineSpriteCount = false;
	private boolean vramWriteFlag = false;
	private int rawControlByte = 0;
	private Logger logger = Logger.getLogger(PPUStatusRegister.class);
	
	private PPUStatusRegister()
	{
		
	}
	
	public int cycle()
	{
		this.setRawControlByte(((this.isVblank()?1:0) << 7) |
			((this.isSprite0Occurance()?1:0) << 6) |
			((this.isScanlineSpriteCount()?1:0) << 5) |
			((this.isVramWriteFlag()?1:0) << 4) |
			0xF); // so far as I know, d3-d0 are not used.
		
		if (logger.isDebugEnabled())
		{
			logger.debug("0x2002: " + this.getRawControlByte() + "\n" +
					"raw bits: " + Integer.toBinaryString(this.getRawControlByte()) + "\n" +
					"isVblank(): " + this.isVblank() + "\n" +
					"isSprite0Occurance(): " + this.isSprite0Occurance() + "\n" +
					"isScanlineSpriteCount(): " + this.isScanlineSpriteCount() + "\n" +
					"isVramWriteFlag(): " + this.isVramWriteFlag() + "\n");
		}
		
		return PPUStatusRegister.CYCLES_PER_EXECUTION;
	}
	
	public int getRegisterValue()
	{
		int rValue = this.getRawControlByte();
		this.setVblank(false);
		Platform.getPpu().resetRegisterAddressFlipFlopLatch();
		return rValue;
	}
	
	public String toString()
	{
		return "0x" + REGISTER_ADDRESS + ": " + Integer.toBinaryString(this.getRawControlByte());
	}
	
	public void setRegisterValue(int value)
	{
		this.setRawControlByte(value);
	}
	
	public boolean isVblank()
	{
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

	private int getRawControlByte()
	{
		return rawControlByte;
	}

	private void setRawControlByte(int rawControlByte)
	{
		this.rawControlByte = rawControlByte;
	}
	
	public static PPUStatusRegister getRegister()
	{
		return register;
	}
	
}
