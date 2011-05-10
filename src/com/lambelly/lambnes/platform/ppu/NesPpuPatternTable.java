package com.lambelly.lambnes.platform.ppu;

import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.platform.ppu.registers.PPUControlRegister;

public class NesPpuPatternTable
{
	private int numSprites = 0;
	private SpriteTile[] sprites = null;
	
	public NesPpuPatternTable()
	{
		// set up pattern table
		if (Platform.getPpu().getPpuControlRegister().getSpriteSize() == PPUControlRegister.SPRITE_SIZE_8X8)
		{
			this.setNumSprites(64);
		}
		else
		{
			this.setNumSprites(32);
		}
		
		this.setSprites(new SpriteTile[this.getNumSprites()]);
		
		
	}

	public int getNumSprites()
	{
		return numSprites;
	}

	public void setNumSprites(int numSprites)
	{
		this.numSprites = numSprites;
	}

	public SpriteTile[] getSprites()
	{
		return sprites;
	}

	public void setSprites(SpriteTile[] sprites)
	{
		this.sprites = sprites;
	}
}
