package com.lambelly.lambnes.test;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author thomasmccarthy
 */
import com.lambelly.lambnes.cartridge.RomLoader;
import org.junit.Test;
import org.apache.log4j.*;
import java.io.FileNotFoundException;

public class RomLoaderTest
{
    private Logger logger = Logger.getLogger(RomLoaderTest.class);

    @Test (expected = java.io.FileNotFoundException.class)
    public void badRom() throws FileNotFoundException
    {
        RomLoader rl = new RomLoader("a");
    }

    @Test
    public void loadRom() throws FileNotFoundException
    {
            RomLoader rl = new RomLoader("./roms/super_mario_bros.zip");
    }

}
