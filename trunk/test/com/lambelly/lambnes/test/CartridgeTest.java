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
import org.apache.log4j.*;
import java.io.FileNotFoundException;

/**
 *
 * @author thomasmccarthy
 */
public class CartridgeTest
{
    private Logger logger = Logger.getLogger(CartridgeTest.class);
    private RomLoader rl = null;
    private Ines cart = null;

    @Before
    public void initialize() throws FileNotFoundException
    {
    	logger.debug("loading file");
        this.setRl(new RomLoader("./roms/Wrecking Crew.zip"));
        logger.debug("creating ines");
        this.setCart(new Ines(getRl().getRomData()));
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
        assertTrue(getCart().getHeader().isNes());
    }

    @Test
    public void program() throws FileNotFoundException
    {
        assertEquals(32768,getCart().getProgramInstructions().length);
    }

    @Test
    public void pattern() throws FileNotFoundException
    {
        assertEquals(8192,getCart().getPatternTiles().length);
    }
    
    @Test
    public void patternDebug() throws FileNotFoundException
    {
    	logger.debug("pattern tile head: ");
        ArrayUtils.head(getCart().getPatternTiles(), 16);
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
