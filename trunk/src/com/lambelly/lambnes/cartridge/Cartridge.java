/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lambelly.lambnes.cartridge;

/**
 *
 * @author thomasmccarthy
 */
public interface Cartridge
{
    public Header getHeader();
    public void setHeader(Header header);
    public int[] getProgramInstructions();
    public void setProgramInstructions(int[] programInstructions);
    public int[] getPatternTiles();
    public void setPatternTiles(int[] patternTiles);
    public void setRomLoader(RomLoader romLoader);
}
