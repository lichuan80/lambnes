/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lambelly.lambnes.platform.ppu;

import com.lambelly.lambnes.platform.ppu.registers.PPUControlRegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUMaskRegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUStatusRegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUVramIORegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUSprRamIORegister;

/**
 *
 * @author thomasmccarthy
 */
public interface PictureProcessingUnit
{
	public PPUControlRegister getPpuControlRegister();
	public PPUMaskRegister getPpuMaskRegister();
	public PPUStatusRegister getPpuStatusRegister();
	public PPUVramIORegister getPpuVramIORegister();
	public PPUSprRamIORegister getPpuSprRamIORegister();
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
