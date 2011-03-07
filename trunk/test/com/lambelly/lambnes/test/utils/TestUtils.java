/*
  * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lambelly.lambnes.test.utils;

import com.lambelly.lambnes.cartridge.Ines;
import com.lambelly.lambnes.cartridge.RomLoader;
import com.lambelly.lambnes.platform.*;
import com.lambelly.lambnes.util.ArrayUtils;

/**
 *
 * @author thomasmccarthy
 */
public class TestUtils
{
    /**
     * creates test rom used for testing memory
     *
     * @return
     */
    public static int[] createTestIntArray(int size)
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

    public static Integer[] createTestIntegerArray(int size)
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
    
    
    public static void createTestPlatform() throws Exception
    {
    	// make sure platform has been instantiated
    	Platform p = Platform.getInstance();
    	
    	// establish memories
    	Platform.getCpuMemory().setMemory(TestUtils.createTestIntArray(65536));
    	Platform.getCpuMemory().setProgramInstructions(TestUtils.createTestIntArray(32768));
    	
        RomLoader rl = new RomLoader("./roms/NEStress.zip");
        Ines i = new Ines(rl.getRomData());
        ArrayUtils.head(i.getPatternTiles(), 16);
    	Platform.getPpuMemory().setPatternTable0(i.getPatternTiles());
    	
    	//reset some stuff
    	Platform.getCpuMemory().resetCounters();  
    	Platform.getCpu().getFlags().resetFlags();
    	Platform.getCpu().resetRegisters();
    }
    
	public static void performInstruction(int instruction)
	{
		setNextMemory(instruction);
		Platform.getCpu().processNextInstruction();		
	}
	
	public static void performInstruction(int instruction, int accumulator)
	{
		setNextMemory(instruction);
		Platform.getCpu().setAccumulator(accumulator);
		Platform.getCpu().processNextInstruction();		
	}
	
	public static void setNextMemory(int value)
	{
		int curPrgAddress = Platform.getCpuMemory().getProgramCounter();
		Platform.getCpuMemory().setMemoryFromHexAddress(curPrgAddress, value);
	}
	
	public static void setMemory(int address, int value)
	{
		Platform.getCpuMemory().setMemoryFromHexAddress(address, value);
	}    
}
