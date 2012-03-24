package com.lambelly.lambnes.platform.mappers;

public interface Mapper
{
	public int getMemoryFromHexAddress(int address) throws IllegalStateException;

	public void setMemoryFromHexAddress(int address, int value) throws IllegalStateException;	
	
	public void setProgramInstructions(int[] programInstructions);
}
