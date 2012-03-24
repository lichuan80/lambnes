package com.lambelly.lambnes.cartridge;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.*;
import org.apache.commons.lang.ArrayUtils;

import com.lambelly.lambnes.LambNes;
import com.lambelly.lambnes.platform.cpu.NesCpuMemory;

/**
 *
 * @author thomasmccarthy
 */
public class Ines implements Cartridge
{
    private RomLoader romLoader = null;
    private static int[] programInstructions = null;
    private int[] patternTiles = null;
    private Header header = null;
    private String cartridgePath = null;
    private Logger logger = Logger.getLogger(Ines.class);
    
    private Ines()
    {    	

    }
    
    public Ines(RomLoader romLoader)
    {
    	this.init(romLoader.getRomData());
    }
    
    public Ines(String cartridgePath)
    {
    	this.setCartridgePath(cartridgePath);
    }
    
    public void locateCartidge() throws IOException
    {
        // load default cartridge
		CartridgeLocator c = null;
		if (this.getCartridgePath() != null)
		{
			// absolute path provided by command line
			c = new CartridgeLocator(this.getCartridgePath());
		}
		else
		{
			// path provided from rom chosen from default location 
			c = new CartridgeLocator();
		}
		
		File romFile = c.locateCartridge();
		
		if (romFile != null)
		{
			this.setCartridgePath(romFile.getAbsolutePath());
		}
		
		if (this.getCartridgePath() != null)
		{
			try
			{
				// initiate cartridge and propagate in system
				RomLoader rl = new RomLoader(this.getCartridgePath());
				this.init(rl.getRomData());
			}
			catch(Exception e)
			{
				throw new IOException("unable to open cartridge: " + e.getMessage());
			}
		}   
    }

    /**
     * init
     * 
     * loads data from file, 
     *
     * parses the data into the header, program instructions, and pattern tiles
     */
    public void init() throws IOException
    {
    	this.locateCartidge();
    }
    
    
    /**
     * init
     *
     * parses the data into the header, program instructions, and pattern tiles
     *
     * @param rawRomData
     */
    public void init(int[] rawRomData)
    {
        // first 16 bytes is the header
        header = (new Header (ArrayUtils.subarray(rawRomData, 0, 16)));
    	
    	// determine other lengths
        int programLength = this.determineProgramInstructionLength(this.getHeader().getProgramInstructionByte());        
        int patternTileLength = this.determinePatternTileInstructionLength(this.getHeader().getPatternTileByte());

        // the program length is determined by byte 4 of the header
        this.setProgramInstructions(ArrayUtils.subarray(rawRomData, 16, programLength + 16));
        int[] patternTiles = ArrayUtils.subarray(rawRomData,(16 + programLength), rawRomData.length);
        
        this.setPatternTiles(patternTiles);

        if (logger.isDebugEnabled())
        {
	        logger.debug("byte 4 (programLength): " + programLength);
	        logger.debug("getting pattern from: " + (16 + programLength));
	        logger.debug("getting pattern to: " + rawRomData.length);
	        logger.debug("pattern tile length: " + patternTiles.length);
	        logger.debug("program pages: " + this.getHeader().getProgramInstructionByte());
	        logger.debug("chr-rom pages: " + this.getHeader().getPatternTileByte());
	        logger.debug("program array length: " + this.getProgramInstructions().length);
	        logger.debug("pattern array length: " + this.getPatternTiles().length);
        }
    }

    /**
     * parses the program instruction length from the raw rom data
     *
     * per http://nesdev.parodius.com/neshdr20.txt:
     * To figure out the exact size in bytes each of these pages are worth
     * just start at 1 x 16kb pages (aka: 16384 bytes) and just keep adding
     * 16384 more for each "page" higher.
     *
     * @param b -- the byte at position 4 in the header
     * @return -- the length
     */
    private int determineProgramInstructionLength(int b)
    {
        return (Integer.parseInt(Integer.toString(b),16) * 16384);
    }

    /**
     * parses the pattern tile length from the raw rom data
     *
     * per http://nesdev.parodius.com/neshdr20.txt:
     * To figure out the exact size in bytes each of these pages are worth
     * just start at 1 x 8kb pages (aka: 8192 bytes) and just keep adding
     * 8192 bytes more for each "page" higher.
     *
     * @param b -- the byte at position 5 in the header
     * @return -- the length
     */
    private int determinePatternTileInstructionLength(int b)
    {
        return (Integer.parseInt(Integer.toString(b),16) * 8192);
    }
    
    public int[] getPage(int pageIndex)
    {
    	int[] page = null;
    	System.arraycopy(this.getProgramInstructions(), 0, page, NesCpuMemory.PRG_ROM_BASE, 16384);
    	
    	return page;
    }
    
    /**
     * @return the romLoader
     */
    public RomLoader getRomLoader()
    {
        return romLoader;
    }

    /**
     * @param romLoader the romLoader to set
     */
    public void setRomLoader(RomLoader romLoader)
    {
        this.romLoader = romLoader;
    }

    /**
     * @return the programInstructions
     */
    public int[] getProgramInstructions()
    {
        return programInstructions;
    }

    /**
     * @param programInstructions the programInstructions to set
     */
    public void setProgramInstructions(int[] programInstructions)
    {
        this.programInstructions = programInstructions;
    }

    /**
     * @return the patternTiles
     */
    public int[] getPatternTiles()
    {
        return patternTiles;
    }

    /**
     * @param patternTiles the patternTiles to set
     */
    public void setPatternTiles(int[] patternTiles)
    {
        this.patternTiles = patternTiles;
    }

    /**
     * @return the header
     */
    public Header getHeader()
    {
        return header;
    }
    
    /**
     * set the header
     */
    public void setHeader(Header header)
    {
        this.header = header;
    }

	public String getCartridgePath()
    {
    	return cartridgePath;
    }

	public void setCartridgePath(String cartridgePath)
    {
    	this.cartridgePath = cartridgePath;
    }
}
