/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lambelly.jnes.cartridge;

import org.apache.log4j.*;

/**
 *
 * @author thomasmccarthy
 */
public class Ines implements Cartridge
{
    private RomLoader romLoader = null;
    private byte[] programInstructions = null;
    private byte[] patternTiles = null;
    private Header header = null;
    private Logger logger = Logger.getLogger(Ines.class);

    public Ines(byte[] rawRomData)
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
    private void parseRom(byte[] rawRomData)
    {
        // first 16 bytes is the header
        this.setHeader(new Header (this.copyArray(rawRomData, 0, 16)));

        // determine other lengths
        int programLength = this.determineProgramInstructionLength(this.getHeader().getProgramInstructionByte());
        int patternTileLength = this.determinePatternTileInstructionLength(this.getHeader().getPatternTileByte());

        // the program length is determined by byte 4 of the header
        this.setProgramInstructions(this.copyArray(rawRomData, 16, programLength));
        this.setPatternTiles(this.copyArray(rawRomData,(16 + programLength), patternTileLength));

        logger.debug("program array length: " + this.getProgramInstructions().length);
        logger.debug("pattern array length: " + this.getPatternTiles().length);
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
    private int determineProgramInstructionLength(byte b)
    {
        return (Integer.parseInt(Byte.toString(b),16) * 16384);
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
    private int determinePatternTileInstructionLength(byte b)
    {
        return (Integer.parseInt(Byte.toString(b),16) * 8192);
    }

    /**
     * returns a subset of an array.
     *
     * @param a -- the array to subset
     * @param start -- the start position
     * @param length -- the final length of the subset
     * @return
     */
    private byte[] copyArray(byte[] a, int start, int length)
    {
        logger.debug("array length: " + a.length);
        logger.debug("start: " + start);
        logger.debug("length: " + length);

        byte[] returnArray = new byte[length];

        for (int i=start;i< length;i++)
        {
            returnArray[i] = a[i];
        }

        logger.debug("final size: " + returnArray.length);

        return returnArray;
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
    public byte[] getProgramInstructions()
    {
        return programInstructions;
    }

    /**
     * @param programInstructions the programInstructions to set
     */
    public void setProgramInstructions(byte[] programInstructions)
    {
        this.programInstructions = programInstructions;
    }

    /**
     * @return the patternTiles
     */
    public byte[] getPatternTiles()
    {
        return patternTiles;
    }

    /**
     * @param patternTiles the patternTiles to set
     */
    public void setPatternTiles(byte[] patternTiles)
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
