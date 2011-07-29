package com.lambelly.lambnes.disassembler;

import com.lambelly.lambnes.platform.cpu.Instruction;

public class DisassembledLine
{
	private Instruction instruction = null;
	private int data = 0;
	
	public DisassembledLine (Instruction instruction, int data)
	{
		this.setInstruction(instruction);
		this.setData(data);
	}
	
	public String toString()
	{
		return this.getInstruction().name() + "\t" + "0x" + Integer.toHexString(this.getData());
	}

	public Instruction getInstruction()
	{
		return instruction;
	}

	public void setInstruction(Instruction instruction)
	{
		this.instruction = instruction;
	}

	public int getData()
	{
		return data;
	}

	public void setData(int data)
	{
		this.data = data;
	}
}
