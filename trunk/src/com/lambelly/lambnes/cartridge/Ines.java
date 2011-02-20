/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lambelly.lambnes.cartridge;

import org.apache.log4j.*;
import org.apache.commons.lang.ArrayUtils;

/**
 *
 * @author thomasmccarthy
 */
public class Ines implements Cartridge
{
    private RomLoader romLoader = null;
    private int[] programInstructions = null;
    private int[] patternTiles = null;
    private Header header = null;
    private Logger logger = Logger.getLogger(Ines.class);

    public Ines(int[] rawRomData)
    {
        this.parseRom(rawRomData);
    }

    /**
     * parses ines data
     *
     * parses the data into the header, program instructions, and pattern tiles
     *
     * @param rawRomData
     */
    private void parseRom(int[] rawRomData)
    {
        // first 16 bytes is the header
        this.setHeader(new Header (ArrayUtils.subarray(rawRomData, 0, 16)));

        // determine other lengths
        int programLength = this.determineProgramInstructionLength(this.getHeader().getProgramInstructionByte());
        logger.debug("byte 4 (programLength): " + programLength);
        int patternTileLength = this.determinePatternTileInstructionLength(this.getHeader().getPatternTileByte());

        // the program length is determined by byte 4 of the header
        this.setProgramInstructions(ArrayUtils.subarray(rawRomData, 16, programLength + 16));
        logger.debug("getting pattern from: " + (16 + programLength));
        logger.debug("getting pattern to: " + rawRomData.length);
        int[] patternTiles = ArrayUtils.subarray(rawRomData,(16 + programLength), rawRomData.length);
        logger.debug("pattern tile length: " + patternTiles.length);
        this.setPatternTiles(patternTiles);

        logger.debug("chr-rom pages: " + this.getHeader().getPatternTileByte());
        logger.debug("program array length: " + this.getProgramInstructions().length);
        logger.debug("pattern array length: " + this.getPatternTiles().length);
		for (int i = 0; i< 32; i++)
		{
			logger.debug("pattern array data: " + this.getPatternTiles()[i]);
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
     * @param header the header to set
     */
    public void setHeader(Header header)
    {
        this.header = header;
    }
}
