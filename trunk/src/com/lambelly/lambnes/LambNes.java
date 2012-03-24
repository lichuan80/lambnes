package com.lambelly.lambnes;

import org.apache.log4j.*;
import javax.swing.SwingUtilities;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

import com.lambelly.lambnes.gui.*;
import com.lambelly.lambnes.cartridge.Cartridge;
import com.lambelly.lambnes.cartridge.Ines;
import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.platform.Config;

/**
 *
 * @author thomasmccarthy
 */
public class LambNes
{
	private static Logger logger = Logger.getLogger(LambNes.class);
	private static final String VERSION = "0.0.8";
	private static String cartridgeLoadPath = null;
	private static Platform platform = null;
	private static Cartridge cartridge = null;

	/**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        if (args.length > 0)
        {
        	logger.debug(args[0]);
        	LambNes.setCartridgeLoadPath(args[0]);
        }
                
        // load default cartridge
    	try
    	{
            // instantiate platform
            System.out.println("\nLambNes\nby Tom McCarthy\nversion " + LambNes.VERSION + "\n");
            
            ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
            LambNes.setPlatform(applicationContext.getBean(Platform.class));
            LambNes.setCartridge(applicationContext.getBean(Ines.class));
            
            // set path -- the cartridge is initially null and can't run the system. 
            LambNes.getCartridge().setCartridgePath(LambNes.getCartridgeLoadPath());
            LambNes.getCartridge().init();
            
            if (getCartridge().getCartridgePath() != null)
            {   
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

		        
		        getPlatform().power();
            }
            else
            {
            	System.err.println("no roms found in default cartridge location: " + Config.getConfig().getString("defaultRomLocation"));
            }
    	}
    	catch(IllegalStateException ex)
    	{
    		logger.error("illegal state",ex);
    		LambNesGui.pushErrorMessage("illegal state");
    	}
    	catch(IOException iox)
    	{
    		logger.error("io exception",iox);
    		LambNesGui.pushErrorMessage("io exception");
    	}
    	catch(NullPointerException ex)
    	{
    		logger.error("null pointer", ex);
    		LambNesGui.pushErrorMessage("java.Lang.NullPointerException");
    	}
    	catch(Exception e)
    	{
    		logger.error("unhandled exception",e);
    		LambNesGui.pushErrorMessage("unhandled exception");
    	}
    	finally
    	{
    		getPlatform().setRun(false);
    	}
    }

	public static Platform getPlatform()
    {
    	return platform;
    }

	private static void setPlatform(Platform platform)
    {
    	LambNes.platform = platform;
    }

	public static Cartridge getCartridge()
    {
    	return cartridge;
    }

	private static void setCartridge(Cartridge cartridge)
    {
    	LambNes.cartridge = cartridge;
    }

	public static String getCartridgeLoadPath()
    {
    	return cartridgeLoadPath;
    }

	public static void setCartridgeLoadPath(String cartridgeLoadPath)
    {
    	LambNes.cartridgeLoadPath = cartridgeLoadPath;
    }
}
