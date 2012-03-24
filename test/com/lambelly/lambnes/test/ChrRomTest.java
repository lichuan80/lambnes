/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lambelly.lambnes.test;

import org.junit.*;
import com.lambelly.lambnes.util.*;
import com.lambelly.lambnes.cartridge.*;
import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.platform.ppu.NesPpuMemory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.apache.log4j.*;
import java.io.FileNotFoundException;

/**
 *
 * @author thomasmccarthy
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:beans.xml"})
public class ChrRomTest
{
    private Logger logger = Logger.getLogger(ChrRomTest.class);
    private RomLoader rl = null;
	@Autowired
	private Platform platform;
	@Autowired
	private Ines cartridge;
	@Autowired
	private NesPpuMemory ppuMemory;

    @Before
    public void initialize() throws FileNotFoundException
    {
    	// make sure platform has been instantiated
    	logger.debug("loading file");
        this.setRl(new RomLoader("./roms/rom.zip"));
        logger.debug("creating ines");
        this.getCartridge().init(getRl().getRomData());
        logger.debug("attempting to set pattern tiles");
        System.out.println(this.getCartridge().getPatternTiles());
        System.out.println(this.getPpuMemory());
        this.getPpuMemory().setPatternTiles(this.getCartridge().getPatternTiles());
    }
    
    @Test
    public void testData()
    {
    	ArrayUtils.head(this.getPpuMemory().getPatternTable0(),20);
    	ArrayUtils.foot(this.getPpuMemory().getPatternTable0(),20);
    	ArrayUtils.head(this.getPpuMemory().getPatternTable1(),20);
    	ArrayUtils.foot(this.getPpuMemory().getPatternTable1(),20);
    	
    	// try to print sprite1.
    	int line1a = this.getPpuMemory().getPatternTable0()[0];
    	int line2a = this.getPpuMemory().getPatternTable0()[1];
    	int line3a = this.getPpuMemory().getPatternTable0()[2];
    	int line4a = this.getPpuMemory().getPatternTable0()[3];
    	int line5a = this.getPpuMemory().getPatternTable0()[4];
    	int line6a = this.getPpuMemory().getPatternTable0()[5];
    	int line7a = this.getPpuMemory().getPatternTable0()[6];
    	int line8a = this.getPpuMemory().getPatternTable0()[7];
    	int line1b = this.getPpuMemory().getPatternTable0()[0];
    	int line2b = this.getPpuMemory().getPatternTable0()[1];
    	int line3b = this.getPpuMemory().getPatternTable0()[2];
    	int line4b = this.getPpuMemory().getPatternTable0()[3];
    	int line5b = this.getPpuMemory().getPatternTable0()[4];
    	int line6b = this.getPpuMemory().getPatternTable0()[5];
    	int line7b = this.getPpuMemory().getPatternTable0()[6];
    	int line8b = this.getPpuMemory().getPatternTable0()[7];
    	
    	// print a
    	logger.debug("a");
    	logger.debug(NumberConversionUtils.generateBinaryStringWithleadingZeros(line1a,8));
    	logger.debug(NumberConversionUtils.generateBinaryStringWithleadingZeros(line2a,8));
    	logger.debug(NumberConversionUtils.generateBinaryStringWithleadingZeros(line3a,8));
    	logger.debug(NumberConversionUtils.generateBinaryStringWithleadingZeros(line4a,8));
    	logger.debug(NumberConversionUtils.generateBinaryStringWithleadingZeros(line5a,8));
    	logger.debug(NumberConversionUtils.generateBinaryStringWithleadingZeros(line6a,8));
    	logger.debug(NumberConversionUtils.generateBinaryStringWithleadingZeros(line7a,8));
    	logger.debug(NumberConversionUtils.generateBinaryStringWithleadingZeros(line8a,8));
    	
    	// print b
    	logger.debug("b");
    	logger.debug(NumberConversionUtils.generateBinaryStringWithleadingZeros(line1b,8));
    	logger.debug(NumberConversionUtils.generateBinaryStringWithleadingZeros(line2b,8));
    	logger.debug(NumberConversionUtils.generateBinaryStringWithleadingZeros(line3b,8));
    	logger.debug(NumberConversionUtils.generateBinaryStringWithleadingZeros(line4b,8));
    	logger.debug(NumberConversionUtils.generateBinaryStringWithleadingZeros(line5b,8));
    	logger.debug(NumberConversionUtils.generateBinaryStringWithleadingZeros(line6b,8));
    	logger.debug(NumberConversionUtils.generateBinaryStringWithleadingZeros(line7b,8));
    	logger.debug(NumberConversionUtils.generateBinaryStringWithleadingZeros(line8b,8));    	
    }

    /**
     * @return the rl
     */
    public RomLoader getRl()
    {
        return rl;
    }

    /**
     * @param rl the rl to set
     */
    public void setRl(RomLoader rl)
    {
        this.rl = rl;
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
