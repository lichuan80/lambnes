/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lambelly.lambnes.cartridge;

import java.util.zip.ZipEntry;
import java.util.Enumeration;
import java.util.zip.ZipFile;
import java.net.URLDecoder;
import java.io.File;
import java.io.InputStream;
import java.io.FileNotFoundException;
import org.apache.log4j.*;

/**
 *
 * @author thomasmccarthy
 */
public class RomLoader
{
    private String filePath = null;
    private int[] romData = null;
    private Logger logger = Logger.getLogger(RomLoader.class);

    public RomLoader(String filePath) throws FileNotFoundException
    {
    	this.setFilePath(filePath);
    	if (new File(filePath) != null)
    	{
    		logger.debug("loading rom");
    		this.loadRom();
    	}
    	else
    	{
    		logger.error("unable to find rom");
    		throw new FileNotFoundException("unable to locate specified rom: " + filePath);
    	}
    }

    private void loadRom()
    {
        ZipFile file = null;

        // use ZipFile to step through the file to determine if there are .nes files.
        int nesCount = 0;
        int[] romData = null;

        try
        {
        	// Convert a URL to a URI
            file = new ZipFile(URLDecoder.decode(this.getFilePath(), "UTF-8"));
            
            Enumeration e = file.entries();
            while (e.hasMoreElements() && nesCount == 0)
            {
                ZipEntry ze = (ZipEntry)e.nextElement();

                if (ze.getName().toLowerCase().endsWith(".nes"))
                {
                    // increment nesCount -- just to stop if there happens to be more than one ines file.
                    nesCount++;

                    // read file
                    InputStream in = file.getInputStream(ze);

                    long size=ze.getSize();
                    romData = new int[(int)size];
                    int b;
                    int i = 0;
                    while ((b = in.read()) != -1)
                    {
                        romData[i] = b;
                        i++;
                    }

                    logger.debug("rom array size:" + romData.length);
                    logger.debug("size: " + size);
                }
            }
        }
        catch(Exception e)
        {
            logger.fatal("problem loading file: " + this.getFilePath(), e);
        }
        finally
        {
            if (file != null)
            {
                try
                {
                    file.close();
                }
                catch(Exception e)
                {
                    logger.error(e);
                }
            }

            this.setRomData(romData);
        }
    }

    /**
     * @return the filePath
     */
    public String getFilePath()
    {
        return filePath;
    }

    /**
     * @param filePath the filePath to set
     */
    public void setFilePath(String filePath)
    {
        this.filePath = filePath;
    }

    /**
     * @return the romData
     */
    public int[] getRomData()
    {
        return romData;
    }

    /**
     * @param romData the romData to set
     */
    public void setRomData(int[] romData)
    {
        this.romData = romData;
    }
}
