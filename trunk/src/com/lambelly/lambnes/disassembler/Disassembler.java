package com.lambelly.lambnes.disassembler;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.lambelly.lambnes.LambNes;
import com.lambelly.lambnes.cartridge.Cartridge;
import com.lambelly.lambnes.cartridge.CartridgeLocator;
import com.lambelly.lambnes.cartridge.Ines;
import com.lambelly.lambnes.cartridge.RomLoader;
import com.lambelly.lambnes.gui.LambNesGui;
import com.lambelly.lambnes.platform.Config;
import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.platform.cpu.Instruction;
import com.lambelly.lambnes.platform.cpu.NesCpuMemory;

public class Disassembler
{
	private static Logger logger = Logger.getLogger(Disassembler.class);
	private static Platform platform = null;
	private static Cartridge cartridge = null;
	
	public Disassembler()
	{
		
	}
	
	private static void disassembleRom()
	{
		logger.debug("disassembling rom");
		
		while (true)
		{
			// get instruction from memory
			int instAddress = getPlatform().getCpuMemory().getProgramCounter();
			Instruction curInst = Instruction.get(getPlatform().getCpuMemory().getNextPrgRomByte());
			int address = 0;
			
			if (curInst != null)
			{
				// get the requisite number of bytes
				if (curInst.getBytes() == 1)
				{
					// get 0 bytes for address -- byte 1 is instruction byte
				}
				else if (curInst.getBytes() == 2)
				{
					// get 1 bytes for address
					address = getPlatform().getCpuMemory().getNextPrgRomByte();
				}
				else if (curInst.getBytes() == 3)
				{
					// get 2 bytes for address
					address = getPlatform().getCpuMemory().getNextPrgRomShort();
				}
				
				DisassembledLine line = new DisassembledLine(curInst, address);
				System.out.println("0x" + Integer.toHexString(instAddress) + " -- " + line);
			}
			else
			{
				System.out.println("0x" + Integer.toHexString(instAddress) + " -- UNDEFINED");
			}
		}
	}
	
    public static void main(String[] args) 
    {   
        // instantiate platform
        logger.info("instantiating platform");
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
        Disassembler.setPlatform(applicationContext.getBean(Platform.class));
        Disassembler.setCartridge(applicationContext.getBean(Ines.class));
        
    	try
    	{	
    		if (getCartridge().getCartridgePath() != null)
    		{   
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
    	catch(NullPointerException ex)
    	{
    		logger.error("null pointer", ex);
    	}
    	catch(Exception e)
    	{
    		logger.error("unhandled exception",e);
    	}        
    }

	public static Platform getPlatform()
    {
    	return platform;
    }

	public static void setPlatform(Platform platform)
    {
    	Disassembler.platform = platform;
    }

	public static Cartridge getCartridge()
    {
    	return cartridge;
    }

	public static void setCartridge(Cartridge cartridge)
    {
    	Disassembler.cartridge = cartridge;
    }
	
}
