/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lambelly.lambnes;

import java.io.FileNotFoundException;
import java.io.File;
import org.apache.log4j.*;
import javax.swing.SwingUtilities;

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
	private static final String VERSION = "0.0.5";

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
		        SwingUtilities.invokeLater(new Runnable() 
		        {
		            public void run() 
		            {
		            	LambNesGui gui = new LambNesGui();
		            	gui.setVisible(true);
		            	Thread t = new Thread(gui);
		            	t.start();
		            }
		        });

		        
		        Platform.power();
    		}
    		else
    		{
    			System.err.println("no rom was found");
    		}
    	}
    	catch(IllegalStateException ex)
    	{
    		logger.error("illegal state",ex);
    	}
    	catch(FileNotFoundException ex)
    	{
    		logger.error("unable to load default cartridge: " + cartridgeLoadPath,ex);
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
