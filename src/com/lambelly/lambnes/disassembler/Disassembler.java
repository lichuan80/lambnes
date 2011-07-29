package com.lambelly.lambnes.disassembler;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.log4j.Logger;

import com.lambelly.lambnes.LambNes;
import com.lambelly.lambnes.cartridge.Cartridge;
import com.lambelly.lambnes.cartridge.CartridgeLocator;
import com.lambelly.lambnes.cartridge.Ines;
import com.lambelly.lambnes.cartridge.RomLoader;
import com.lambelly.lambnes.gui.LambNesGui;
import com.lambelly.lambnes.platform.Config;
import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.platform.cpu.Instruction;

public class Disassembler
{
	private static Logger logger = Logger.getLogger(Disassembler.class);
	
	public Disassembler()
	{
		
	}
	
	private static void disassembleRom()
	{
		logger.debug("disassembling rom");
	
		while (true)
		{
			// get instruction from memory
			int instAddress = Platform.getCpuMemory().getProgramCounter();
			Instruction curInst = Instruction.get(Platform.getCpuMemory().getNextPrgRomByte());
			int address = 0;
			
			// get the requisite number of bytes
			if (curInst.getBytes() == 1)
			{
				// get 0 bytes for address -- byte 1 is instruction byte
			}
			else if (curInst.getBytes() == 2)
			{
				// get 1 bytes for address
				address = Platform.getCpuMemory().getNextPrgRomByte();
			}
			else if (curInst.getBytes() == 3)
			{
				// get 2 bytes for address
				address = Platform.getCpuMemory().getNextPrgRomShort();
			}
			
			DisassembledLine line = new DisassembledLine(curInst, address);
			System.out.println("0x" + Integer.toHexString(instAddress) + " -- " + line);
		}
	}
	
    public static void main(String[] args) 
    {
    	String cartridgeLoadPath = null;
        if (args.length > 0)
        {
        	logger.debug(args[0]);
        	cartridgeLoadPath = args[0];
        }
        
        // instantiate platform
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
		        
		        Platform.init();
		        
		        disassembleRom();
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
