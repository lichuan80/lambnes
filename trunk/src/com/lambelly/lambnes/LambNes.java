/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lambelly.lambnes;

import java.io.FileNotFoundException;
import java.io.File;
import org.apache.log4j.*;

import com.lambelly.lambnes.gui.*;
import com.lambelly.lambnes.cartridge.Cartridge;
import com.lambelly.lambnes.cartridge.CartridgeLocator;
import com.lambelly.lambnes.cartridge.Ines;
import com.lambelly.lambnes.cartridge.RomLoader;
import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.platform.Config;

/**
 *
 * @author thomasmccarthy
 */
public class LambNes
{
	private static Logger logger = Logger.getLogger(LambNes.class);
	private static final String VERSION = "0.0.4";

	/**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
    	String cartridgeLoadPath = null;
        if (args.length > 0)
        {
        	logger.debug(args[0]);
        	cartridgeLoadPath = args[0];
        }
        
        // instantiate platform
        System.out.println("\nLambNes\nby Tom McCarthy\nversion " + LambNes.VERSION + "\n");
        logger.info("instantiating platform");
        Platform p = Platform.getInstance();
        Config config = new Config();
        
        // start gui
    	LambNesGui gui = new LambNesGui();
        
        // load default cartridge
    	try
    	{
    		// get cartridge
    		CartridgeLocator c = null;
    		if (cartridgeLoadPath != null)
    		{
    			// absolute path provided by command line
    			c = new CartridgeLocator(cartridgeLoadPath);
    		}
    		else
    		{
    			// path provided from rom chosen from default location 
    			c = new CartridgeLocator();
    		}
    		
    		File romFile = c.locateCartridge();
			
			if (romFile != null)
    		{
				cartridgeLoadPath = romFile.getAbsolutePath();
    		}
    		
    		if (cartridgeLoadPath != null)
    		{
		        RomLoader rl = new RomLoader(cartridgeLoadPath);
		        Cartridge cart = new Ines(rl.getRomData());
		        
		        Platform.setCartridge(cart);
		        
		        // start
		        gui.setVisible(true);
		        Platform.power();
    		}
    		else
    		{
    			System.err.println("no rom was found");
    		}
    	}
    	catch(IllegalStateException ex)
    	{
    		//mainwindow.interrupt();
    		logger.error("illegal state",ex);
    	}
    	catch(FileNotFoundException ex)
    	{
    		//mainwindow.interrupt();
    		logger.error("unable to load default cartridge: " + cartridgeLoadPath,ex);
    	}
    	catch(NullPointerException ex)
    	{
    		//mainwindow.interrupt();
    		logger.error("null pointer", ex);
    	}
    	catch(Exception e)
    	{
    		//mainwindow.interrupt();
    		logger.error("unhandled exception",e);
    	}        
    }
}
