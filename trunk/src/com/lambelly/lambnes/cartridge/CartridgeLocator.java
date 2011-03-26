package com.lambelly.lambnes.cartridge;

import java.io.*;
import org.apache.log4j.*;
import java.util.Vector;
import java.util.Scanner;
import java.util.Iterator;

public class CartridgeLocator
{
	private static final String DEFAULT_ROM_LOCATION = "roms/";
	private Vector<String> roms = new Vector<String>();
	private Logger logger = Logger.getLogger(CartridgeLocator.class);
	
	public CartridgeLocator()
	{
		this.generateListOfDefaultRoms(CartridgeLocator.DEFAULT_ROM_LOCATION);
	}
	
	public CartridgeLocator(String secondaryPath)
	{
		this.generateListOfDefaultRoms(secondaryPath);
	}
	
	public String locateCartridge()
	{
		if (this.getRoms().size() > 1)
		{
			return CartridgeLocator.DEFAULT_ROM_LOCATION + this.getRoms().get(this.selectRom());
		}
		else if (this.getRoms().size() == 1)
		{
			return this.getRoms().get(0);
		}	
		else
		{
			return null;
		}
	}
	
	private int selectRom()
	{
		System.out.println("multiple cartridges exist in the default location.");
		System.out.println("please select a cartridge to load: ");
		Iterator<String> it = this.getRoms().iterator();
		int i = 0;
		while (it.hasNext())
		{
			System.out.println(i + "." + it.next());
			i++;
		}
		
		Scanner sc = new Scanner(System.in);
		int select = sc.nextInt();
		if (select >= 0 && select < this.getRoms().size())
		{
			return select;
		}
		else
		{
			return this.selectRom();
		}
	}

	private void generateListOfDefaultRoms(String path)
	{
		// maybe eventually make a version capable of traversing subdirectories.
		File dir = new File(path);
		
		String[] children = dir.list();
		if (children == null) 
		{
		    // Either dir does not exist or is not a directory
			if (dir.isFile())
			{
				// path was direct to a file
				logger.debug("loading file directly");
				if (path.toLowerCase().endsWith(".zip"))
				{
					this.addRom(path);
				}
			}
		} 
		else 
		{
		    for (int i=0; i<children.length; i++) 
		    {
		        // Get filename of file or directory
		        String filename = children[i];
		        
		        if (filename.toLowerCase().endsWith(".zip"))
		        {
		        	logger.debug("adding rom: " + filename);
		        	this.addRom(filename);
		        }
		        else
		        {
		        	logger.debug("not adding file: " + filename);
		        }
		    }
		}		
	}
	
	public void addRom(String rom)
	{
		this.roms.add(rom);
	}
	
	public Vector<String> getRoms()
	{
		return roms;
	}

	public void setRoms(Vector<String> roms)
	{
		this.roms = roms;
	}
}
