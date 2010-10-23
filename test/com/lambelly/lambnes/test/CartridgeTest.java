/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lambelly.lambnes.test;

import static org.junit.Assert.*;
import org.junit.*;
import com.lambelly.lambnes.cartridge.*;
import org.junit.Test;
import org.apache.log4j.*;
import java.io.FileNotFoundException;

/**
 *
 * @author thomasmccarthy
 */
public class CartridgeTest
{
    private Logger logger = Logger.getLogger("CartridgeTest.class");
    private RomLoader rl = null;
    private Ines cart = null;

    @Before
    public void initialize() throws FileNotFoundException
    {
    	logger.debug("loading file");
        this.setRl(new RomLoader("rom.zip"));
        logger.debug("creating ines");
        this.setCart(new Ines(getRl().getRomData()));
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
