/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lambelly.jnes.test;

import static org.junit.Assert.*;
import org.junit.*;
import com.lambelly.jnes.cartridge.*;
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
        this.setRl(new RomLoader("./super_mario_bros.zip"));
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
        assertEquals(getCart().getProgramInstructions().length,32768);
    }

    @Test
    public void pattern() throws FileNotFoundException
    {
        assertEquals(getCart().getPatternTiles().length,8192);
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
