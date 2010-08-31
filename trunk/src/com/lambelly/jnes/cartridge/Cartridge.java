/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lambelly.jnes.cartridge;

/**
 *
 * @author thomasmccarthy
 */
public interface Cartridge
{
    public Header getHeader();
    public void setHeader(Header header);
    public byte[] getProgramInstructions();
    public void setProgramInstructions(byte[] programInstructions);
    public byte[] getPatternTiles();
    public void setPatternTiles(byte[] patternTiles);
    public void setRomLoader(RomLoader romLoader);
}
