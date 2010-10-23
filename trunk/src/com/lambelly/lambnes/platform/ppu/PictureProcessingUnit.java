/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lambelly.lambnes.platform.ppu;

/**
 *
 * @author thomasmccarthy
 */
public interface PictureProcessingUnit
{
	public void doRegisterReadsWrites();
	public PPUStatusRegister getPpuStatusRegister();
}
