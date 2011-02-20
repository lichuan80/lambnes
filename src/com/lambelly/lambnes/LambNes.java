/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lambelly.lambnes;

import java.io.FileNotFoundException;

import org.apache.log4j.*;

import com.lambelly.lambnes.gui.*;
import com.lambelly.lambnes.cartridge.Cartridge;
import com.lambelly.lambnes.cartridge.CartridgeLocator;
import com.lambelly.lambnes.cartridge.Ines;
import com.lambelly.lambnes.cartridge.RomLoader;
import com.lambelly.lambnes.platform.Platform;

/**
 *
 * @author thomasmccarthy
 */
public class LambNes
{
	private static Logger logger = Logger.getLogger(LambNes.class);
	private static final String VERSION = "0.0.1";

	/**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
    	String cartridge = null;
        if (args.length > 0)
        {
        	logger.debug(args[0]);
        	cartridge = args[0];
        }
        
        // instantiate platform
    	// start gui
    	LambNesGui gui = new LambNesGui();
        Thread mainwindow = new Thread(gui);
        
        
        System.out.println("\nLambNes\nby Tom McCarthy\nversion " + LambNes.VERSION + "\n");
        logger.info("instantiating platform");
        Platform p = Platform.getInstance();
        
        // load default cartridge
    	try
    	{
    		// get cartridge
    		CartridgeLocator c = new CartridgeLocator();
    		cartridge = c.locateCartridge();
	        RomLoader rl = new RomLoader(cartridge);
	        Cartridge cart = new Ines(rl.getRomData());
	        
	        Platform.setCartridge(cart);
	        
	        // start
	        mainwindow.start();
	        Platform.power();    
    	}
    	catch(IllegalStateException ex)
    	{
    		mainwindow.interrupt();
    		logger.error("illegal state",ex);
    	}
    	catch(FileNotFoundException ex)
    	{
    		mainwindow.interrupt();
    		logger.error("unable to load default cartridge: " + cartridge,ex);
    	}
    	catch(NullPointerException ex)
    	{
    		mainwindow.interrupt();
    		logger.error("null pointer", ex);
    	}
    	catch(Exception e)
    	{
    		mainwindow.interrupt();
    		logger.error("unhandled exception",e);
    	}        
    }
}
