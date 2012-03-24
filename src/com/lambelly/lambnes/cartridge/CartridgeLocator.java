package com.lambelly.lambnes.cartridge;

import java.io.*;
import org.apache.log4j.*;

import com.lambelly.lambnes.platform.Config;

import java.util.Vector;
import java.util.Scanner;
import java.util.Iterator;

public class CartridgeLocator
{
	private Vector<File> roms = new Vector<File>();
	private Logger logger = Logger.getLogger(CartridgeLocator.class);
	
	public CartridgeLocator()
	{
		this.generateListOfDefaultRoms(Config.getConfig().getString("defaultRomLocation"));
	}
	
	public CartridgeLocator(String secondaryPath)
	{
		this.generateListOfDefaultRoms(secondaryPath);
	}
	
	public File locateCartridge()
	{
		if (this.getRoms().size() > 1)
		{
			return this.getRoms().get(this.selectRom());
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
		Iterator<File> it = this.getRoms().iterator();
		int i = 0;
		while (it.hasNext())
		{
			System.out.println(i + "." + it.next().getName());
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
		
		File[] children = dir.listFiles();
		if (children == null) 
		{
		    // Either dir does not exist or is not a directory
			if (dir.isFile())
			{
				// path was direct to a file
				if (dir.getName().toLowerCase().endsWith(".zip"))
				{
					this.addRom(dir);
				}
			}
		} 
		else 
		{
		    for (int i=0; i<children.length; i++) 
		    {
		        // Get filename of file or directory
		        File file = children[i];
		        
		        if (file.getName().toLowerCase().endsWith(".zip"))
		        {
		        	this.addRom(file);
		        }
		        else
		        {
		        	logger.debug("not adding file: " + file.getName());
		        }
		    }
		}		
	}
	
	public void addRom(File rom)
	{
		this.roms.add(rom);
	}
	
	public Vector<File> getRoms()
	{
		return roms;
	}

	public void setRoms(Vector<File> roms)
	{
		this.roms = roms;
	}
}
