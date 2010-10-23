/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lambelly.lambnes.test;

import static org.junit.Assert.*;
import org.junit.*;
import com.lambelly.lambnes.util.*;
import com.lambelly.lambnes.cartridge.*;
import com.lambelly.lambnes.platform.Platform;

import org.junit.Test;
import org.apache.log4j.*;
import java.io.FileNotFoundException;

/**
 *
 * @author thomasmccarthy
 */
public class ChrRomTest
{
    private Logger logger = Logger.getLogger(ChrRomTest.class);
    private RomLoader rl = null;
    private Ines cart = null;

    @Before
    public void initialize() throws FileNotFoundException
    {
    	// make sure platform has been instantiated
    	Platform p = Platform.getInstance();
    	
    	logger.debug("loading file");
        this.setRl(new RomLoader("rom.zip"));
        logger.debug("creating ines");
        this.setCart(new Ines(getRl().getRomData()));
        logger.debug("attempting to set pattern tiles");
        Platform.getPpuMemory().setPatternTiles(getCart().getPatternTiles());
    }
    
    @Test
    public void testData()
    {
    	ArrayUtils.head(Platform.getPpuMemory().getPatternTable0(),20);
    	ArrayUtils.foot(Platform.getPpuMemory().getPatternTable0(),20);
    	ArrayUtils.head(Platform.getPpuMemory().getPatternTable1(),20);
    	ArrayUtils.foot(Platform.getPpuMemory().getPatternTable1(),20);
    	
    	// try to print sprite1.
    	int line1a = Platform.getPpuMemory().getPatternTable0()[0];
    	int line2a = Platform.getPpuMemory().getPatternTable0()[1];
    	int line3a = Platform.getPpuMemory().getPatternTable0()[2];
    	int line4a = Platform.getPpuMemory().getPatternTable0()[3];
    	int line5a = Platform.getPpuMemory().getPatternTable0()[4];
    	int line6a = Platform.getPpuMemory().getPatternTable0()[5];
    	int line7a = Platform.getPpuMemory().getPatternTable0()[6];
    	int line8a = Platform.getPpuMemory().getPatternTable0()[7];
    	int line1b = Platform.getPpuMemory().getPatternTable0()[0];
    	int line2b = Platform.getPpuMemory().getPatternTable0()[1];
    	int line3b = Platform.getPpuMemory().getPatternTable0()[2];
    	int line4b = Platform.getPpuMemory().getPatternTable0()[3];
    	int line5b = Platform.getPpuMemory().getPatternTable0()[4];
    	int line6b = Platform.getPpuMemory().getPatternTable0()[5];
    	int line7b = Platform.getPpuMemory().getPatternTable0()[6];
    	int line8b = Platform.getPpuMemory().getPatternTable0()[7];
    	
    	// print a
    	logger.debug("a");
    	logger.debug(BitUtils.generateBinaryStringWithleadingZeros(line1a,8));
    	logger.debug(BitUtils.generateBinaryStringWithleadingZeros(line2a,8));
    	logger.debug(BitUtils.generateBinaryStringWithleadingZeros(line3a,8));
    	logger.debug(BitUtils.generateBinaryStringWithleadingZeros(line4a,8));
    	logger.debug(BitUtils.generateBinaryStringWithleadingZeros(line5a,8));
    	logger.debug(BitUtils.generateBinaryStringWithleadingZeros(line6a,8));
    	logger.debug(BitUtils.generateBinaryStringWithleadingZeros(line7a,8));
    	logger.debug(BitUtils.generateBinaryStringWithleadingZeros(line8a,8));
    	
    	// print b
    	logger.debug("b");
    	logger.debug(BitUtils.generateBinaryStringWithleadingZeros(line1b,8));
    	logger.debug(BitUtils.generateBinaryStringWithleadingZeros(line2b,8));
    	logger.debug(BitUtils.generateBinaryStringWithleadingZeros(line3b,8));
    	logger.debug(BitUtils.generateBinaryStringWithleadingZeros(line4b,8));
    	logger.debug(BitUtils.generateBinaryStringWithleadingZeros(line5b,8));
    	logger.debug(BitUtils.generateBinaryStringWithleadingZeros(line6b,8));
    	logger.debug(BitUtils.generateBinaryStringWithleadingZeros(line7b,8));
    	logger.debug(BitUtils.generateBinaryStringWithleadingZeros(line8b,8));    	
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

    /**
     * @return the cart
     */
    public Ines getCart()
    {
        return cart;
    }

    /**
     * @param cart the cart to set
     */
    public void setCart(Ines cart)
    {
        this.cart = cart;
    }
}
