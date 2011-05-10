package com.lambelly.lambnes.platform.cpu;

import java.util.Map;
import java.util.HashMap;
import java.util.EnumSet;

public enum Instruction
{
	ADC_IMMEDIATE(0x69, 2, 2),
	ADC_ZERO_PAGE(0x65, 2, 3),
	ADC_ZERO_PAGE_X(0x75, 2, 4),
	ADC_ABSOLUTE(0x6D, 3, 4),
	ADC_ABSOLUTE_X(0x7D, 3, 4),
	ADC_ABSOLUTE_Y(0x79, 3, 4),
	ADC_INDEXED_INDIRECT(0x61, 2, 6),
	ADC_INDIRECT_INDEXED(0x71, 2, 5),
	AND_IMMEDIATE(0x29, 2, 2),
	AND_ZERO_PAGE(0x25, 2, 3),
	AND_ZERO_PAGE_X(0x35, 2, 4),
	AND_ABSOLUTE(0x2D, 3, 4),
	AND_ABSOLUTE_X(0x3D, 3, 4),
	AND_ABSOLUTE_Y(0x39, 3, 4),
	AND_INDEXED_INDIRECT(0x21, 2, 6),
	AND_INDIRECT_INDEXED(0x31, 2, 5),
	ASL_ACCUMULATOR(0x0A, 1, 2),
	ASL_ZERO_PAGE(0x06, 2, 5),
	ASL_ZERO_PAGE_X(0x16, 2, 6),
	ASL_ABSOLUTE(0x0E, 3, 6),
	ASL_ABSOLUTE_X(0x1E, 3, 7),
	BCC(0x90, 2, 2),
	BCS(0xB0, 2, 2),
	BEQ(0xF0, 2, 2),
	BIT_ZERO_PAGE(0x24, 2, 3),
	BIT_ABSOLUTE(0x2C, 3, 4),
	BMI(0x30, 2, 2),
	BNE(0xD0, 2, 2),           
	BPL(0x10, 2, 2),
	BRK(0x00, 1, 7),
	BVC(0x50, 2, 2),
	BVS(0x70, 2, 2),
	CLC(0x18, 1, 2),
	CLD(0xD8, 1, 2),        
	CLI(0x58, 1, 2),
	CLV(0xB8, 1, 2),
	CMP_IMMEDIATE(0xC9, 2, 2),
	CMP_ZERO_PAGE(0xC5, 2, 3),
	CMP_ZERO_PAGE_X(0xD5, 2, 4),
	CMP_ABSOLUTE(0xCD, 3, 4),
	CMP_ABSOLUTE_X(0xDD, 3, 4),
	CMP_ABSOLUTE_Y(0xD9, 3, 4),
	CMP_INDEXED_INDIRECT(0xC1, 2, 6),
	CMP_INDIRECT_INDEXED(0xD1, 2, 5),
	CPX_IMMEDIATE(0xE0, 2, 2),
	CPX_ZERO_PAGE(0xE4, 2, 3),
	CPX_ABSOLUTE(0xEC, 3, 4),
	CPY_IMMEDIATE(0xC0, 2, 2),
	CPY_ZERO_PAGE(0xC4, 2, 3),
	CPY_ABSOLURE(0xCC, 3, 4),                
	DEC_ZERO_PAGE(0xC6, 2, 5),
	DEC_ZERO_PAGE_X(0xD6, 2, 6),
	DEC_ABSOLUTE(0xCE, 3, 6),
	DEC_ABSOLUTE_X(0xDE, 3, 7),
	DEX(0xCA, 1, 2),
	DEY(0x88, 1, 2),
	EOR_IMMEDIATE(0x49, 2, 2),
	EOR_ZERO_PAGE(0x45, 2, 3),
	EOR_ZERO_PAGE_X(0x55, 2, 4),
	EOR_ABSOLUTE(0x4D, 3, 4),
	EOR_ABSOLUTE_X(0x5D, 3, 4),
	EOR_ABSOLUTE_Y(0x59, 3, 4),
	EOR_INDEXED_INDIRECT(0x41, 2, 6),
	EOR_INDIRECT_INDEXED(0x51, 2, 5),
	INC_ZERO_PAGE(0xE6, 2, 5),
	INC_ZERO_PAGE_X(0xF6, 2, 6),
	INC_ABSOLUTE(0xEE, 3, 6),
	INC_ABSOLUTE_X(0xFE, 3, 7),
	INX(0xE8, 1, 2),
	INY(0xC8, 1, 2),
	JMP_ABSOLUTE(0x4C, 3, 3),
	JMP_RELATIVE(0x6C, 3, 5),
	JSR_ABSOLUTE(0x20, 3, 6),                
	LDA_IMMEDIATE(0xA9, 2, 2),
	LDA_ZERO_PAGE(0xA5, 2, 3),
	LDA_ZERO_PAGE_X(0xB5, 2, 4),
	LDA_ABSOLUTE(0xAD, 3, 4),
	LDA_ABSOLUTE_X(0xBD, 3, 4),
	LDA_ABSOLUTE_Y(0xB9, 3, 4),
	LDA_INDEXED_INDIRECT(0xA1, 2, 6),
	LDA_INDIRECT_INDEXED(0xB1, 2, 5),
	LDX_IMMEDIATE(0xA2, 2, 2),
	LDX_ZERO_PAGE(0xA6, 2, 3),
	LDX_ZERO_PAGE_Y(0xB6, 2, 4),
	LDX_ABSOLUTE(0xAE, 3, 4),
	LDX_ABSOLUTE_Y(0xBE, 3, 4),
	LDY_IMMEDIATE(0xA0, 2, 2),
	LDY_ZERO_PAGE(0xA4, 2, 3),
	LDY_ZERO_PAGE_X(0xB4, 2, 4),
	LDY_ABSOLUTE(0xAC, 3, 4),
	LDY_ABSOLUTE_X(0xBC, 3, 4),
	LSR_ACCUMULATOR(0x4A, 1, 2),
	LSR_ZERO_PAGE(0x46, 2, 5),
	LSR_ZERO_PAGE_X(0x56, 2, 6),
	LSR_ABSOLUTE(0x4E, 3, 6),
	LSR_ABSOLUTE_X(0x5E, 3, 7),
	NOP(0xEA, 1, 2),
	ORA_IMMEDIATE(0x09, 2, 2),
	ORA_ZERO_PAGE(0x05, 2, 3),
	ORA_ZERO_PAGE_X(0x15, 2, 4),
	ORA_ABSOLUTE(0x0D, 3, 4),
	ORA_ABSOLUTE_X(0x1D, 3, 4),
	ORA_ABSOLUTE_Y(0x19, 3, 4),
	ORA_INDEXED_INDIRECT(0x01, 2, 6),
	ORA_INDIRECT_INDEXED(0x11, 2, 5),
	PHA(0x48, 1, 3),    
	PHP(0x08, 1, 3),
	PLA(0x68, 1, 4),
	PLP(0x28, 1, 4),
	ROL_ACCUMULATOR(0x2A, 1, 2),
	ROL_ZERO_PAGE(0x26, 2, 5),
	ROL_ZERO_PAGE_X(0x36, 2, 6),
	ROL_ABSOLUTE(0x2E, 3, 6),
	ROL_ABSOLUTE_X(0x3E, 3, 7),
	ROR_ACCUMULATOR(0x6A, 1, 2),
	ROR_ZERO_PAGE(0x66, 2, 5),
	ROR_ZERO_PAGE_X(0x76, 2, 6),
	ROR_ABSOLUTE(0x6E, 3, 6),
	ROR_ABSOLUTE_X(0x7E, 3, 7),
	RTI(0x40, 1, 6),
	RTS(0x60, 1, 6),
	SBC_IMMEDIATE(0xE9, 2, 2),
	SBC_ZERO_PAGE(0xE5, 2, 3),
	SBC_ZERO_PAGE_X(0xF5, 2, 4),
	SBC_ABSOLUTE(0xED, 3, 4),
	SBC_ABSOLUTE_X(0xFD, 3, 4),
	SBC_ABSOLUTE_Y(0xF9, 3, 4),
	SBC_INDEXED_INDIRECT(0xE1, 2, 6),
	SBC_INDIRECT_INDEXED(0xF1, 2, 5),
	SEC(0x38, 1, 2),
	SED(0xF8, 1, 2),
	SEI(0x78, 1, 2),
	STA_ZERO_PAGE(0x85, 2, 3),
	STA_ZERO_PAGE_X(0x95, 2, 4),
	STA_ABSOLUTE(0x8D, 3, 4),
	STA_ABSOLUTE_X(0x9D, 3, 5),
	STA_ABSOLUTE_Y(0x99, 3, 5),
	STA_INDEXED_INDIRECT(0x81, 2, 6),
	STA_INDIRECT_INDEXED(0x91, 2, 6),
	STX_ZERO_PAGE(0x86, 2, 3),
	STX_ZERO_PAGE_Y(0x96, 2, 4),
	STX_ABSOLUTE(0x8E, 3, 4),
	STY_ZERO_PAGE(0x84, 2, 3),
	STY_ZERO_PAGE_X(0x94, 2, 4),
	STY_ABSOLUTE(0x8C, 3, 4),
	TAX(0xAA, 1, 2),
	TAY(0xA8, 1, 2),
	TSX(0xBA, 1, 2),
	TXA(0x8A, 1, 2),
	TXS(0x9A, 1, 2),
	TYA(0x98, 1, 2);	

	private static final Map<Integer,Instruction> lookup = new HashMap<Integer,Instruction>();
	private int opCode = 0;
	private int cycles = 0;
	private int bytes = 0;
	
    static 
    {
        for(Instruction i : EnumSet.allOf(Instruction.class))
        {
             lookup.put(i.getOpCode(), i);
        }
    }

	private Instruction(int opCode)
	{
		this.setOpCode(opCode);
	}    
    
	private Instruction(int opCode, int bytes, int cycle)
	{
		this.setOpCode(opCode);
		this.setCycles(cycle);
		this.setBytes(bytes);
	}    
    
    public static Instruction get(int opCode) { 
        return lookup.get(opCode); 
    }	
	
	public int getOpCode()
	{
		return opCode;
	}
	
	public void setOpCode(int opCode)
	{
		this.opCode = opCode;
	}

	public int getCycles()
	{
		return cycles;
	}

	public void setCycles(int cycles)
	{
		this.cycles = cycles;
	}

	public int getBytes()
	{
		return bytes;
	}

	public void setBytes(int bytes)
	{
		this.bytes = bytes;
	}
}
