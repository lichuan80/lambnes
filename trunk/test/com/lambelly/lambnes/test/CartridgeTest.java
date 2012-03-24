/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lambelly.lambnes.test;

import static org.junit.Assert.*;
import org.junit.*;
import com.lambelly.lambnes.cartridge.*;
import com.lambelly.lambnes.util.ArrayUtils;

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
public class CartridgeTest
{
    private Logger logger = Logger.getLogger(CartridgeTest.class);
    private RomLoader rl = null;
    @Autowired
    private Ines ines;

    @Before
    public void initialize() throws FileNotFoundException
    {
    	logger.debug("loading file");
        this.setRl(new RomLoader("./roms/Wrecking Crew.zip"));
        logger.debug("creating ines");
        this.getInes().init(getRl().getRomData());
    }
    
    @Test
    public void headerControl() throws FileNotFoundException
    {
    	int controlBit1 = 0x10;
    	int controlBit2 = 0;
    	
    	logger.debug("controlBit1 & 0xF0: " + (controlBit1 & 0xF0));
    	
    	assertEquals(1,((controlBit1 & 0xF0) >> 4) | (controlBit2 & 0xF0));
    }

    @Test
    public void header() throws FileNotFoundException
    {
        assertTrue(this.getInes().getHeader().isNes());
    }

    @Test
    public void program() throws FileNotFoundException
    {
        assertEquals(32768,this.getInes().getProgramInstructions().length);
    }

    @Test
    public void pattern() throws FileNotFoundException
    {
        assertEquals(8192,this.getInes().getPatternTiles().length);
    }
    
    @Test
    public void patternDebug() throws FileNotFoundException
    {
    	logger.debug("pattern tile head: ");
        ArrayUtils.head(this.getInes().getPatternTiles(), 16);
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

	public Ines getInes()
    {
    	return ines;
    }

	public void setInes(Ines ines)
    {
    	this.ines = ines;
    }
}
