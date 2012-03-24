/*
  * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lambelly.lambnes.test.utils;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

import com.lambelly.lambnes.LambNes;
import com.lambelly.lambnes.cartridge.Cartridge;
import com.lambelly.lambnes.cartridge.Ines;
import com.lambelly.lambnes.cartridge.RomLoader;
import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.platform.cpu.NesCpu;
import com.lambelly.lambnes.platform.cpu.NesCpuMemory;
import com.lambelly.lambnes.platform.ppu.NesPpuMemory;
import com.lambelly.lambnes.util.ArrayUtils;

/**
 *
 * @author thomasmccarthy
 */
@ContextConfiguration(locations={"classpath:beans.xml"})
public class TestUtils
{
	@Autowired
    private NesCpuMemory cpuMemory;
	@Autowired
	private NesPpuMemory ppuMemory;
	@Autowired
	private NesCpu cpu;
	
    /**
     * creates test rom used for testing memory
     *
     * @return
     */
    public int[] createTestIntArray(int size)
    {
        int[] testArray = new int[size];

        int hexIndex = 0;
        for (int i=0; i<size; i++)
        {
            testArray[i] = (hexIndex);

            hexIndex++;
            if (hexIndex > 255)
            {
                hexIndex = 0;
            }
        }

        return testArray;
    }

    public Integer[] createTestIntegerArray(int size)
    {
        Integer[] testArray = new Integer[size];

        int hexIndex = 0;
        for (int i=0; i<size; i++)
        {
            testArray[i] = (hexIndex);

            hexIndex++;
            if (hexIndex > 255)
            {
                hexIndex = 0;
            }
        }

        return testArray;
    }
    
    public void createTestPlatform() throws Exception
    {    	
    	// establish memories
    	this.getCpuMemory().setMemory(this.createTestIntArray(65536));
    	this.getCpuMemory().setProgramInstructions(this.createTestIntArray(32768));
    	
        RomLoader rl = new RomLoader("./roms/NEStress.zip");
        Ines cartridge = new Ines(rl);
        ArrayUtils.head(cartridge.getPatternTiles(), 16);
        this.getPpuMemory().setPatternTable0(cartridge.getPatternTiles());
    	
    	//reset some stuff
        this.getCpuMemory().resetCounters();  
        this.getCpu().getFlags().resetFlags();
    	this.getCpu().resetRegisters();
    }
    
	public void performInstruction(int instruction)
	{
		setNextMemory(instruction);
		cpu.processNextInstruction();		
	}
	
	public void performInstruction(int instruction, int accumulator)
	{
		setNextMemory(instruction);
		cpu.setAccumulator(accumulator);
		cpu.processNextInstruction();		
	}
	
	public void setNextMemory(int value)
	{
		int curPrgAddress = cpuMemory.getProgramCounter();
		cpuMemory.setMemoryFromHexAddress(curPrgAddress, value);
	}
	
	public void setMemory(int address, int value)
	{
		cpuMemory.setMemoryFromHexAddress(address, value);
	}

	public NesCpuMemory getCpuMemory()
    {
    	return cpuMemory;
    }

	public void setCpuMemory(NesCpuMemory cpuMemory)
    {
		this.cpuMemory = cpuMemory;
    }

	public NesPpuMemory getPpuMemory()
    {
    	return ppuMemory;
    }

	public void setPpuMemory(NesPpuMemory ppuMemory)
    {
		this.ppuMemory = ppuMemory;
    }

	public NesCpu getCpu()
    {
    	return cpu;
    }

	public void setCpu(NesCpu cpu)
    {
    	this.cpu = cpu;
    }    
}
