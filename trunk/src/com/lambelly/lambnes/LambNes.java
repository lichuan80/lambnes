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
        
        // start gui
    	LambNesGui gui = new LambNesGui();
        //Thread mainwindow = new Thread(gui);
        //mainwindow.setDaemon(true);
        
        // load default cartridge
    	try
    	{
    		// get cartridge
    		CartridgeLocator c = null;
    		if (cartridgeLoadPath != null)
    		{
    			c = new CartridgeLocator(cartridgeLoadPath);
    		}
    		else
    		{
    			c = new CartridgeLocator();
    		}
    		
    		cartridgeLoadPath = c.locateCartridge();
    		
    		if (cartridgeLoadPath != null)
    		{
		        RomLoader rl = new RomLoader(cartridgeLoadPath);
		        Cartridge cart = new Ines(rl.getRomData());
		        
		        Platform.setCartridge(cart);
		        
		        // start
		        gui.setVisible(true);
		        //mainwindow.start();
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
