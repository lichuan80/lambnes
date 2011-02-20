/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lambelly.lambnes.test;

import static org.junit.Assert.*;
import com.lambelly.lambnes.cartridge.*;
import com.lambelly.lambnes.platform.*;
import com.lambelly.lambnes.platform.ppu.*;
import com.lambelly.lambnes.util.*;
import com.lambelly.lambnes.test.utils.TestUtils;
import org.junit.Test;
import org.apache.log4j.*;


/**
 *
 * @author thomasmccarthy
 */
public class BackgroundTileTest
{
	private Logger logger = Logger.getLogger(BackgroundTileTest.class);
	
	@Test
	public void testColorIndex()
	{
		BackgroundTile bg = new BackgroundTile();
		bg.setPatternA(new int[]{0,0,0,0,128,128,128,0});
		bg.setPatternB(new int[]{0,0,128,128,64,96,32,0});
		bg.setBackgroundAttributes(new BackgroundAttribute(4));

		assertEquals(18, bg.getPixelBackgroundColorPaletteIndex(7,2));
	}
	
	@Test
	public void testColorIndex2() throws Exception
	{	
		Platform p = Platform.getInstance();
		
		RomLoader rl = new RomLoader("./roms/BalloonFight.zip");
        Cartridge cart = new Ines(rl.getRomData());
        Platform.setCartridge(cart);
        
		ArrayUtils.head(Platform.getCartridge().getPatternTiles(), 16);
		Platform.getPpuMemory().setPatternTiles(Platform.getCartridge().getPatternTiles());
        
        BackgroundTile bg = new BackgroundTile(36,4);
        assertEquals(18, bg.getPixelBackgroundColorPaletteIndex(7,2));
	}
	
	@Test
	public void testColorIndex3() throws Exception
	{
		Platform p = Platform.getInstance();
		
		RomLoader rl = new RomLoader("./roms/BalloonFight.zip");
        Cartridge cart = new Ines(rl.getRomData());
        Platform.setCartridge(cart);
        
        int tile = 0;
        int pattern = 0;
        for (int i=0x1000; i < 0x2000; i++)
        {
        	if (pattern >= 0 && pattern < 8)
        	{
        		logger.debug("byte " + pattern + " for tile " + tile + ":" + Platform.getCartridge().getPatternTiles()[i]);
        	}
        	else if (pattern >= 8 && pattern < 16)
        	{
        		logger.debug("byte " + pattern + " for tile " + tile + ":" + Platform.getCartridge().getPatternTiles()[i]);
        	}
        	
        	pattern ++;
        	if (pattern > 15)
        	{
        		tile ++;
        		pattern = 0;
        	}
        }
        Platform.getCartridge().getPatternTiles();
	}
	
	@Test
	public void testImageGenerator() throws Exception
	{
		RomLoader rl = new RomLoader("./roms/NEStress.zip");
        Cartridge cart = new Ines(rl.getRomData());
        TileTestImageViewer t = new TileTestImageViewer(cart, 0x45);
        t.run();
	}
}
