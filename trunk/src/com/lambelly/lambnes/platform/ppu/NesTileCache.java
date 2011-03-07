package com.lambelly.lambnes.platform.ppu;

public class NesTileCache
{
	private static BackgroundTile[] backgroundTileCache = new BackgroundTile[256];
	private static SpriteTile[] spriteTileCache = new SpriteTile[64];
	
	private static void loadBackgroundCache()
	{
		for (int i = 0; i < backgroundTileCache.length; i++)
		{
			NesTileCache.setBackgroundTile(new BackgroundTile(i), i);
		}
	}
	
	private static void loadSpriteCache()
	{
		for (int i = 0; i < spriteTileCache.length; i++)
		{
			NesTileCache.setSpriteTile(new SpriteTile(i), i);
		}
	}
	
	public static BackgroundTile getBackgroundTile(int backgroundTileNumber)
	{
		// make sure the cache is loaded -- loads on first request
		if (backgroundTileCache[backgroundTileNumber] == null)
		{
			NesTileCache.loadBackgroundCache();
		}

		return backgroundTileCache[backgroundTileNumber];
	}
	
	public static BackgroundTile getBackgroundTile(int backgroundTileNumber, int colorMSB)
	{
		BackgroundTile bg = NesTileCache.getBackgroundTile(backgroundTileNumber);
		bg.getBackgroundAttributes().setColorHighBit(colorMSB);
		return bg;
	}
	
	public static SpriteTile getSpriteTile(int spriteTileNumber)
	{
		// make sure the cache is loaded -- loads on first request
		if (spriteTileCache[spriteTileNumber] == null)
		{
			NesTileCache.loadSpriteCache();
		}

		return spriteTileCache[spriteTileNumber];
	}
	
	private static void setBackgroundTile(BackgroundTile bg, int backgroundTileNumber)
	{
		backgroundTileCache[backgroundTileNumber] = bg;
	}

	public static BackgroundTile[] getBackgroundTileCache()
	{
		return backgroundTileCache;
	}

	public static void setBackgroundTileCache(BackgroundTile[] backgroundTileCache)
	{
		NesTileCache.backgroundTileCache = backgroundTileCache;
	}

	private static void setSpriteTile(SpriteTile sp, int spriteTileNumber)
	{
		spriteTileCache[spriteTileNumber] = sp;
	}
	
	public static SpriteTile[] getSpriteTileCache()
	{
		return spriteTileCache;
	}

	public static void setSpriteTileCache(SpriteTile[] spriteTileCache)
	{
		NesTileCache.spriteTileCache = spriteTileCache;
	}
	
}
