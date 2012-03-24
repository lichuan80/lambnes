package com.lambelly.lambnes.platform.ppu;

import org.apache.log4j.*;

import com.lambelly.lambnes.LambNes;

public class NesTileCache
{
	private BackgroundTile[] backgroundTileCache = new BackgroundTile[256];
	private SpriteTile[] spriteTileCache = new SpriteTile[256];
	private Logger logger = Logger.getLogger(NesTileCache.class);
	
	public NesTileCache()
	{
		this.loadBackgroundCache();
		this.loadSpriteCache();
	}
	
	private void loadBackgroundCache()
	{
		for (int i = 0; i < backgroundTileCache.length; i++)
		{
			this.setBackgroundTile(new BackgroundTile(i), i);
		}
	}
	
	private void loadSpriteCache()
	{
		for (int i = 0; i < spriteTileCache.length; i++)
		{
			this.setSpriteTile(new SpriteTile(i), i);
		}
	}
	
	public boolean checkForTileStaleness(NesTile tile)
	{
		// get first bit of tile from pattern table to determine if stale
		int patternTableAddress = (tile.getNameTableSelectBit() * 0x1000) | (tile.tileNumber * 16);
		int patternTableBit = LambNes.getPlatform().getPpuMemory().getMemoryFromHexAddress(patternTableAddress);
		if (tile.getPatternA()[0] != patternTableBit)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public BackgroundTile getBackgroundTile(int backgroundTileNumber)
	{
		return backgroundTileCache[backgroundTileNumber];
	}
	
	public BackgroundTile getBackgroundTile(int backgroundTileNumber, int colorMSB)
	{
		
		BackgroundTile bg = this.getBackgroundTile(backgroundTileNumber);
		
		// make sure cache isn't stale. Not sure this is any more efficient than anything else.
		if (this.checkForTileStaleness(bg))
		{
			//logger.info("reloading background cache");
			this.loadBackgroundCache();
			bg = this.getBackgroundTile(backgroundTileNumber);
		}
		
		bg.setAttributes(new BackgroundAttribute(colorMSB));
		
		return bg;
	}
	
	public SpriteTile getSpriteTile(int spriteTileNumber)
	{
		
		return spriteTileCache[spriteTileNumber];
	}
	
	public SpriteTile getSpriteTile(int spriteTileNumber, SpriteAttribute spriteAttribute)
	{	
		SpriteTile s = spriteTileCache[spriteTileNumber];
		s.setAttributes(spriteAttribute);
		
		if (this.checkForTileStaleness(s))
		{
			// logger.debug("reloading sprite cache");
			this.loadSpriteCache();
			s = this.getSpriteTile(spriteTileNumber, spriteAttribute);
		}
		
		return spriteTileCache[spriteTileNumber];
	}
	
	private void setBackgroundTile(BackgroundTile bg, int backgroundTileNumber)
	{
		backgroundTileCache[backgroundTileNumber] = bg;
	}

	public BackgroundTile[] getBackgroundTileCache()
	{
		return backgroundTileCache;
	}

	public void setBackgroundTileCache(BackgroundTile[] backgroundTileCache)
	{
		this.backgroundTileCache = backgroundTileCache;
	}

	private void setSpriteTile(SpriteTile sp, int spriteTileNumber)
	{
		spriteTileCache[spriteTileNumber] = sp;
	}
	
	public SpriteTile[] getSpriteTileCache()
	{
		return spriteTileCache;
	}

	public void setSpriteTileCache(SpriteTile[] spriteTileCache)
	{
		this.spriteTileCache = spriteTileCache;
	}
	
}
