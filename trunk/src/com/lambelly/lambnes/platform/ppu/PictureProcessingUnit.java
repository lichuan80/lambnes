package com.lambelly.lambnes.platform.ppu;

/**
 *
 * @author thomasmccarthy
 */
public interface PictureProcessingUnit
{
	public int getRegisterAddressFlipFlopLatch();
	public void resetRegisterAddressFlipFlopLatch();
	public int getScanlineCount();
	public long getScreenCount();
	
	public void setLoopyX(int loopyX);
	public void setLoopyT(int loopyT);
	public void setLoopyV(int loopyV);
	public int getLoopyT();
	public void cycle(int cycleCount);
}
