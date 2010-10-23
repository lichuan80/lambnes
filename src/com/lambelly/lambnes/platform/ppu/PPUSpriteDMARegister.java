package com.lambelly.lambnes.platform.ppu;

import com.lambelly.lambnes.platform.Platform;
import org.apache.log4j.*;

public class PPUSpriteDMARegister
{
	public static final int REGISTER_ADDRESS = 0x4014;
	private int rawControlByte = 0;
	private Logger logger = Logger.getLogger(PPUSpriteDMARegister.class);
	public void read()
	{
		this.setRawControlByte(Platform.getCpuMemory().getMemoryFromHexAddress(PPUSpriteDMARegister.REGISTER_ADDRESS));
		if (rawControlByte > 0 )
		{
			logger.debug("\n\n\n\n\n***********************DMA*************************");
		}
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
