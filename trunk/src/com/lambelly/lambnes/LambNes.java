/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lambelly.lambnes;

import java.util.Scanner;
import java.io.FileNotFoundException;

import org.apache.log4j.*;

import com.lambelly.lambnes.cartridge.Cartridge;
import com.lambelly.lambnes.cartridge.Ines;
import com.lambelly.lambnes.cartridge.RomLoader;
import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.platform.cpu.NesCpu;
import com.lambelly.lambnes.platform.cpu.NesCpuMemory;
import com.lambelly.lambnes.test.utils.TestUtils;

/**
 *
 * @author thomasmccarthy
 */
public class LambNes
{
	private static Logger logger = Logger.getLogger(LambNes.class);
	private static final String version = "0.0.1";
	

	/**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
    	String cartridge = null;
        if (args.length > 0)
        {
        	cartridge = args[0];
        }
        
        // instantiate platform
        logger.info("LambNes version " + LambNes.version + " starting");
        logger.info("instantiating platform");
        Platform p = Platform.getInstance();
        
        // load default cartridge
    	try
    	{
    		// get cartridge
	        RomLoader rl = new RomLoader("rom.zip");
	        Cartridge cart = new Ines(rl.getRomData());
	        
	        // insert cartridge
	        Platform.getCpuMemory().setProgramInstructions(cart.getProgramInstructions());
	        
	        // start
	        Platform.power();
    	}
    	catch(IllegalStateException ex)
    	{
    		logger.error("illegal state",ex);
    	}
    	catch(FileNotFoundException ex)
    	{
    		logger.error("unable to load default cartridge.",ex);
    	}
    	catch(NullPointerException ex)
    	{
    		logger.error("null pointer", ex);
    	}
    	catch(Exception e)
    	{
    		logger.error("unhandled exception",e);
    	}        
    }
}
