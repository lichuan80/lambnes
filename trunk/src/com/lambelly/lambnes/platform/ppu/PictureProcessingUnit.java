/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lambelly.lambnes.platform.ppu;

import com.lambelly.lambnes.platform.ppu.registers.PPUControlRegister1;
import com.lambelly.lambnes.platform.ppu.registers.PPUControlRegister2;
import com.lambelly.lambnes.platform.ppu.registers.PPUStatusRegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUVramIORegister;
import com.lambelly.lambnes.platform.ppu.registers.PPUSprRamIORegister;

/**
 *
 * @author thomasmccarthy
 */
public interface PictureProcessingUnit
{
	public void doRegisterReadsWrites();
	public PPUControlRegister1 getPpuControlRegister1();
	public PPUControlRegister2 getPpuControlRegister2();
	public PPUStatusRegister getPpuStatusRegister();
	public PPUVramIORegister getPpuVramIORegister();
	public PPUSprRamIORegister getPpuSprRamIORegister();
	
	public void cycle(int cycleCount);
}
