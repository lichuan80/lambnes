/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lambelly.jnes.cartridge;

import java.util.zip.ZipEntry;
import java.util.Enumeration;
import java.util.zip.ZipFile;
import java.util.Formatter;
import java.io.InputStream;
import java.io.FileNotFoundException;
import org.apache.log4j.*;

/**
 *
 * @author thomasmccarthy
 */
public class RomLoader
{
    private String fileName = null;
    private String filePath = null;
    private byte[] romData = null;
    private Logger logger = Logger.getLogger(RomLoader.class);

    public RomLoader(String fileName) throws FileNotFoundException
    {
        this.setFileName(fileName);
        if (Thread.currentThread().getContextClassLoader().getResource(fileName) != null)
        {
            this.setFilePath(Thread.currentThread().getContextClassLoader().getResource(fileName).getFile());
        }
        else
        {
            throw new FileNotFoundException("rom not found.");
        }
        this.loadRom();
    }

    private void loadRom()
    {
        ZipFile file = null;

        // use ZipFile to step through the file to determine if there are .nes files.
        int nesCount = 0;
        byte[] romData = null;

        try
        {
            file = new ZipFile(this.getFilePath());
            
            Enumeration e = file.entries();
            while (e.hasMoreElements() && nesCount == 0)
            {
                ZipEntry ze = (ZipEntry)e.nextElement();

                if (ze.getName().endsWith(".nes"))
                {
                    // increment nesCount -- just to stop if there happens to be more than one ines file.
                    nesCount++;

                    // read file
                    InputStream in = file.getInputStream(ze);

                    long size=ze.getSize();
                    romData = new byte[(int)size];
                    int read = in.read(romData,0,(int)size);
                    logger.debug("rom array size:" + romData.length);
                    logger.debug("size: " + size);
                    logger.debug("read: " + read);
                }
            }
        }
        catch(Exception e)
        {
            logger.fatal(e);
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
     * @return the fileName
     */
    public String getFileName()
    {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    /**
     * @return the romData
     */
    public byte[] getRomData()
    {
        return romData;
    }

    /**
     * @param romData the romData to set
     */
    public void setRomData(byte[] romData)
    {
        this.romData = romData;
    }
}
