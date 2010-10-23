/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lambelly.lambnes.test.utils;

import com.lambelly.lambnes.platform.*;

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
    public static int[] createTestArray(int size)
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
    
    public static void createTestPlatform()
    {
    	// make sure platform has been instantiated
    	Platform p = Platform.getInstance();
    	
    	// establish memories
    	Platform.getCpuMemory().setZeroPage(TestUtils.createTestArray(256));
    	Platform.getCpuMemory().setStackMemory(TestUtils.createTestArray(256));
    	Platform.getCpuMemory().setRam(TestUtils.createTestArray(1536));
    	Platform.getCpuMemory().setInputOutput1(TestUtils.createTestArray(8));
    	Platform.getCpuMemory().setInputOutput2(TestUtils.createTestArray(32));
    	Platform.getCpuMemory().setExpansionRam(TestUtils.createTestArray(8160));
    	Platform.getCpuMemory().setSram(TestUtils.createTestArray(8192));
    	Platform.getCpuMemory().setProgramInstructions(TestUtils.createTestArray(32768));
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
