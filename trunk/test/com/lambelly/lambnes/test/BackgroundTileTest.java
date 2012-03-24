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
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.apache.log4j.*;


/**
 *
 * @author thomasmccarthy
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:beans.xml"})
public class BackgroundTileTest
{
	@Autowired
	private Platform platform;
	@Autowired
	private Ines cartridge;
	@Autowired
	private NesPpuMemory ppuMemory;
	private Logger logger = Logger.getLogger(BackgroundTileTest.class);
	
	@Test
	public void testColorIndex()
	{
		BackgroundTile bg = new BackgroundTile();
		bg.setPatternA(new int[]{0,0,0,0,128,128,128,0});
		bg.setPatternB(new int[]{0,0,128,128,64,96,32,0});
		bg.setAttributes(4);

		assertEquals(18, bg.getPixelColorPaletteIndex(7,2));
	}
	
	@Test
	public void testColorIndex2() throws Exception
	{	
		RomLoader rl = new RomLoader("./roms/BalloonFight.zip");
        this.getCartridge().init(rl.getRomData());
        
		ArrayUtils.head(this.getCartridge().getPatternTiles(), 16);
		this.getPpuMemory().setPatternTiles(this.getCartridge().getPatternTiles());
        
        BackgroundTile bg = new BackgroundTile(36,4);
        assertEquals(18, bg.getPixelColorPaletteIndex(7,2));
	}
	
	@Test
	public void testColorIndex3() throws Exception
	{
		RomLoader rl = new RomLoader("./roms/BalloonFight.zip");
        this.getCartridge().init(rl.getRomData());
        
        int tile = 0;
        int pattern = 0;
        for (int i=0x1000; i < 0x2000; i++)
        {
        	if (pattern >= 0 && pattern < 8)
        	{
        		logger.debug("byte " + pattern + " for tile " + tile + ":" + this.getCartridge().getPatternTiles()[i]);
        	}
        	else if (pattern >= 8 && pattern < 16)
        	{
        		logger.debug("byte " + pattern + " for tile " + tile + ":" + this.getCartridge().getPatternTiles()[i]);
        	}
        	
        	pattern ++;
        	if (pattern > 15)
        	{
        		tile ++;
        		pattern = 0;
        	}
        }
        this.getCartridge().getPatternTiles();
	}
	
	@Test
	public void testImageGenerator() throws Exception
	{
		RomLoader rl = new RomLoader("./roms/BalloonFight.zip");
		this.getCartridge().init(rl.getRomData());
        TileTestImageViewer t = new TileTestImageViewer(0x45);
        t.run();
	}

	public Platform getPlatform()
    {
    	return platform;
    }

	public void setPlatform(Platform platform)
    {
    	this.platform = platform;
    }

	public Ines getCartridge()
    {
    	return cartridge;
    }

	public void setCartridge(Ines cartridge)
    {
    	this.cartridge = cartridge;
    }

	public NesPpuMemory getPpuMemory()
    {
    	return ppuMemory;
    }

	public void setPpuMemory(NesPpuMemory ppuMemory)
    {
    	this.ppuMemory = ppuMemory;
    }
}
