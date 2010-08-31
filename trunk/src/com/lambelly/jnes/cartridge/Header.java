/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lambelly.jnes.cartridge;

/**
 *
 * @author thomasmccarthy
 */
public class Header
{
    private boolean Nes = false;
    private byte programInstructionByte = 0;
    private byte patternTileByte = 0;

    public Header(byte[] rawData)
    {
        this.setNes(true);
        this.setProgramInstructionByte(rawData[4]);
        this.setPatternTileByte(rawData[5]);
    }

    /**
     * @return the Nes
     */
    public boolean isNes()
    {
        return Nes;
    }

    /**
     * @param Nes the Nes to set
     */
    public void setNes(boolean Nes)
    {
        this.Nes = Nes;
    }

    /**
     * @return the programInstructionByte
     */
    public byte getProgramInstructionByte()
    {
        return programInstructionByte;
    }

    /**
     * @param programInstructionByte the programInstructionByte to set
     */
    public void setProgramInstructionByte(byte programInstructionByte)
    {
        this.programInstructionByte = programInstructionByte;
    }

    /**
     * @return the patternTileByte
     */
    public byte getPatternTileByte()
    {
        return patternTileByte;
    }

    /**
     * @param patternTileByte the patternTileByte to set
     */
    public void setPatternTileByte(byte patternTileByte)
    {
        this.patternTileByte = patternTileByte;
    }
}
