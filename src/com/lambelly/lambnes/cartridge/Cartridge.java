package com.lambelly.lambnes.cartridge;

import java.io.IOException;

/**
 *
 * @author thomasmccarthy
 */
public interface Cartridge
{
	public void init() throws IOException;
	public void init(int[] rawRomData);
    public Header getHeader();
    public int[] getProgramInstructions();
    public void setProgramInstructions(int[] programInstructions);
    public int[] getPatternTiles();
    public void setPatternTiles(int[] patternTiles);
    public int[] getPage(int pageIndex);
    public void setRomLoader(RomLoader romLoader);
    public void setCartridgePath(String cartridgePath);
    public String getCartridgePath();
}
