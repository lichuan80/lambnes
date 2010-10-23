package com.lambelly.lambnes.platform.cpu;

import org.apache.log4j.*;

public class InstructionDEPRECATED
{
	private static Logger logger = Logger.getLogger(InstructionDEPRECATED.class);
	
	public InstructionDEPRECATED(int instructionCode)
	{
		
	}
	
    public static String mapInstructionCodeToLabel(int instructionCode)
    {
    	String r = null;
        switch (instructionCode)
        {        
            /**
                ADC  -  Add to Accumulator with Carry
             */
            case 0x69:
                // #aa
            	r = ("ADC");
                break;
            case 0x65:
                // aa
            	r = ("ADC");
            	break;
            case 0x75:
                // aa,X
            	r = ("ADC");
            	break;
            case 0x6D:
                // aaaa
            	r = ("ADC");
            	break;
            case 0x7D:
                // aaaa,X
            	r = ("ADC");
                break;

            /**
             * AND  -  AND Memory with Accumulator
             */
            case 0x29:
                // #aa
            	r = ("AND");
                break;
            case 0x25:
                // aa
            	r = ("AND");
                break;
            case 0x35:
                // aa,X
            	r = ("AND");
            	break;
            case 0x2D:
                // aaaa
            	r = ("AND");
                break;
            case 0x3D:
                // aaaa,X
            	r = ("AND");
            	break;
            case 0x39:
                // aaaa,Y
            	r = ("AND");
                break;
            case 0x21:
                // (aa,X)
            	r = ("AND");
                break;
            case 0x31:
                // (aa),Y
            	r = ("AND");
                break;

            /**
                ASL  -  Arithmetic Shift Left
             */
            case 0x0A:
                // A
            	r = ("ASL");
                break;
            case 0x06:
                // aa
            	r = ("ASL");
                break;
            case 0x16:
                // aa,X
            	r = ("ASL");
                break;
            case 0x0E:
                // aaaa
            	r = ("ASL");
                break;
            case 0x1E:
                //aaaa,X
            	r = ("ASL");
                break;

            /**
                BCC  -  Branch on Carry Clear
             */
            case 0x90:
                // aa
            	r = ("BCC");
                break;

            /**
                BCS  -  Branch on Carry Set
             */
            case 0xC0:
                // aa
            	r = ("BCS");
                break;

            /**
                BEQ  -  Branch Zero Set
             */
            case 0xF0:
                // aa
            	r = ("BEQ");
                break;
   
            /**
                BIT  -  Test Bits in Memory with Accumulator
                This instructions is used to test if one or more bits are set in a target memory location. The mask pattern in A is ANDed with the value in memory to set or clear the zero flag, but the result is not kept. Bits 7 and 6 of the value from memory are copied into the N and V flags.
             */
            case 0x24:
                // aa
            	r = ("BIT");
                break;
            case 0x2C:
                // aaaa
            	r = ("BIT");
            	break;

            /**
                BMI  -  Branch on Result Minus
             */
            case 0x30:
                // aa
            	r = ("BMI");
                break;

            /**
                BNE  -  Branch on Z reset
             */
            case 0xD0:
                // aa
            	r = ("BNE");
                break;
                
            /**
                BPL  -  Branch on Result Plus (or Positive)
             */
            case 0x10:
                // aa
            	r = ("BPL");
                break;

            /**
                BRK  -  Force a Break
             */
            case 0x00:
            	r = ("BRK");
                break;
                
            /**
                BVC  -  Branch on Overflow Clear
             */
            case 0x50:
                // aa
            	r = ("BVC");
                break;

            /**
                BVS  -  Branch on Overflow Set
             */
            case 0x70:
                // aa
            	r = ("BVS");
                break;

            /**
                CLC  -  Clear Carry Flag
             */
            case 0x18:
            	r = ("CLC");
                break;

            /**
                CLD  -  Clear Decimal Mode
             */
            case 0xD8:
            	r = ("CLD");
                break;
                
            /**
                CLI  -  Clear Interrupt Disable
             */
            case 0x58:
            	r = ("CLI");
                break;

            /**
                CLV  -  Clear Overflow Flag
             */
            case 0xB8:
            	r = ("CLV");
                break;

            /**
                CMP  -  Compare Memory and Accumulator
             */
            case 0xC9:
                // #aa
            	r = ("CMP");
                break;
            case 0xC5:
                // aa
            	r = ("CMP");
                break;
            case 0xD5:
                // aa,X
            	r = ("CMP");
                break;
            case 0xCD:
                // aaaa
            	r = ("CMP");
                break;
            case 0xDD:
                // aaaa,X
            	r = ("CMP");
                break;
            case 0xD9:
                // aaaa,Y
            	r = ("CMP");
                break;
            case 0xC1:
                // (aa,X)
            	r = ("CMP");
            	break;
            case 0xD1:
                // (aa),Y
            	r = ("CMP");
            	break;
                
            /**
                CPX  -  Compare Memory and X register
             */
            case 0xE0:
                // #aa
            	r = ("CPX");
                break;
            case 0xE4:
                // aa
            	r = ("CPX");
                break;
            case 0xEC:
                // aaaa
            	r = ("CPX");
                break;

            /**
                DEC  -  Decrement Memory by One
             */
            case 0xC6:
                // aa
            	r = ("DEC");
                break;
            case 0xD6:
                // aa,X
            	r = ("DEC");
            	break;
            case 0xCE:
                // aaaa
            	r = ("DEC");
            	break;
            case 0xDE:
                // aaaa,X
            	r = ("DEC");
            	break;
                
            /**
                DEX  -  Decrement X
             */
            case 0xCA:
            	r = ("DEX");
                break;

            /**
                DEY  -  Decrement Y
             */
            case 0x88:
            	r = ("DEY");
                break;

            /**
                EOR  -  Exclusive-OR Memory with Accumulator
             */
            case 0x49:
            	r = ("EOR");
                break;
            case 0x45:
                // $aa
            	r = ("EOR");
                break;
            case 0x55:
                // $aa,X
            	r = ("EOR");
                break;
            case 0x4D:
                // $aaaa
            	r = ("EOR");
                break;
            case 0x5D:
                // $aaaa,X
            	r = ("EOR");
                break;
            case 0x59:
                // $aaaa,Y
            	r = ("EOR");
                break;
            case 0x41:
                // ($aa,X)
            	r = ("EOR");
                break;
            case 0x51:
                // ($aa),Y
            	r = ("EOR");
                break;

            /**
                INC  -  Increment Memory by one
             */
            case 0xE6:
                // $aa
            	r = ("INC");
            	break;
            case 0xF6:
                //  $aa,X
            	r = ("INC");
            	break;
            case 0xEE:
                // $aaaa
            	r = ("INC");
            	break;
            case 0xFE:
                // $aaaa,X
            	r = ("INC");
                break;

            /**
                INX  -  Increment X by one
             */
            case 0xE8:
            	r = ("INX");
                break;

            /**
                INY  -  Increment Y by one
             */
            case 0xC8:
            	r = ("INY");
            	break;

            /**
                JMP  -  Jump
             */
            case 0x4C:
                // $aaaa
            	r = ("JMP");
            	break;
            case 0x6C:
                // ($aaaa)
            	r = ("JMP");
            	break;

            /**
                JSR  -  Jump to subroutine
             */
            case 0x20:
                // $aaaa
            	r = ("JSR");
            	break;
                
            /**
                LDA  -  Load Accumulator with memory
             */
            case 0xA9:
                // #aa
            	r = ("LDA");
                break;
            case 0xA5:
                // $aa
            	r = ("LDA");
            	break;
            case 0xB5:
                // $aa,X
            	r = ("LDA");
            	break;
            case 0xAD:
                // $aaaa
            	r = ("LDA");
            	break;
            case 0xBD:
                // $aaaa,X
            	r = ("LDA");
            	break;
            case 0xB9:
                //  $aaaa,Y
            	r = ("LDA");
            	break;
            case 0xA1:
                //  ($aa,X)
            	r = ("LDA");
            	break;
            case 0xB1:
                // ($aa),Y
            	r = ("LDA");
            	break;

            /**
                LDX  -  Load X with Memory
             */
            case 0xA2:
                // #aa      
            	r = ("LDX");
            	break;
            case 0xA6:
                // $aa      
            	r = ("LDX");
            	break;
            case 0xB6:
                // $aa,Y    
            	r = ("LDX");
            	break;
            case 0xAE:
                // $aaaa    
            	r = ("LDX");
            	break;
            case 0xBE:
            	r = ("LDX");
            	break;

            /**
                LDY  -  Load Y with Memory
             */
            case 0xA0:
                // #aa    
            	r = ("LDY");
            	break;
            case 0xA4:
                // $aa
            	r = ("LDY");
            	break;
            case 0xB4:
                // $aa,X    
            	r = ("LDY");
            	break;
            case 0xAC:
                // $aaaa    
            	r = ("LDY");
            	break;
            case 0xBC:
                // $aaaa,X  
            	r = ("LDY");
            	break;

            /**
                LSR  -  Logical Shift Right
             */
            case 0x4A:
                // A     
            	r = ("LSR");
                break;
            case 0x46:
                // $aa
            	r = ("LSR");
                break;
            case 0x56:
                // $aa,X    
            	r = ("LSR");
                break;
            case 0x4E:
                // $aaaa
            	r = ("LSR");
                break;
            case 0x5E:
                // $aaaa,X  
            	r = ("LSR");
                break;

            /**
                NOP  -  No Operation
             */
            case 0xEA:
            	r = ("NOP");
                break;

            /**
                ORA  -  OR Memory with Accumulator
             */
            case 0x09:
                // #aa 
            	r = ("ORA");
                break;
            case 0x05:
                // $aa
            	r = ("ORA");
                break;
            case 0x15:
                // $aa,X   
            	r = ("ORA");
                break;
            case 0x0D:
                // $aaaa   
            	r = ("ORA");
                break;
            case 0x1D:
                // $aaaa,X 
            	r = ("ORA");
                 break;
            case 0x19:
                // $aaaa,Y 
            	r = ("ORA");
                break;
            case 0x01:
                // ($aa,X) 
            	r = ("ORA");
                break;
            case 0x11:
                // ($aa),Y 
            	r = ("ORA");
                break;

            /**
                PHA  -  Push Accumulator on Stack
             */
            case 0x48:
            	r = ("PHA");
                break;
            
            /**
                PHP  -  Push Processor Status on Stack
             */
            case 0x08:
            	r = ("PHP");
                break;

            /**
                PLA  -  Pull Accumulator from Stack
             */
            case 0x68:
            	r = ("PLA");
                break;

            /**
                PLP  -  Pull Processor Status from Stack
             */
            case 0x28:
            	r = ("PLP");
                break;

            /**
                ROL  -  Rotate Left
             */
            case 0x2A:
            	r = ("ROL");
                // A
                break;
            case 0x26:
                // $aa
            	r = ("ROL");
                break;
            case 0x36:
                // $aa,X
            	r = ("ROL");
                 break;
            case 0x2E:
                // $aaaa   
            	r = ("ROL");
            	break;
            case 0x3E:
                // $aaaa,X 
            	r = ("ROL");
            	break;

            /**
                ROR  -  Rotate Right
             */
            case 0x6A:
                // A       
            	r = ("ROR");
                break;
            case 0x66:
                // $aa
            	r = ("ROR");
                break;
            case 0x76:
                // $aa,X   
            	r = ("ROR");
                 break;
            case 0x6E:
                // $aaaa   
            	r = ("ROR");
                break;
            case 0x7E:
                // $aaaa,X 
            	r = ("ROR");
            	break;

            /**
                RTI  -  Return from Interrupt
             */
            case 0x40:
            	r = ("RTI");
                break;

            /**
                RTS  -  Return from Subroutine
             */
            case 0x60:
            	r = ("RTS");
                break;

            /**
                SBC  -  Subtract from Accumulator with Carry
             */
            case 0xE9:
                // #aa
            	r = ("SBC");
                break;
            case 0xE5:
                // $aa
            	r = ("SBC");
                break;
            case 0xF5:
                // $aa,X   
            	r = ("SBC");
                break;
            case 0xED:
                // $aaaa   
            	r = ("SBC");
                break;
            case 0xFD:
                // $aaaa,X 
            	r = ("SBC");
                break;
            case 0xF9:
                // $aaaa,Y 
            	r = ("SBC");
                break;
            case 0xE1:
                // ($aa,X) 
            	r = ("SBC");
                break;
            case 0xF1:
                // ($aa),Y 
            	r = ("SBC");
                break;

            /**
                SEC  -  Set Carry Flag
             */
            case 0x38:
            	r = ("SEC");
                break;

            /**
                SED  -  Set Decimal Mode
             */
            case 0xF8:
            	r = ("SED");
                break;

            /**
                SEI  -  Set Interrupt Disable
             */
            case 0x78:
            	r = ("SEI");
                break;

            /**
                STA  -  Store Accumulator in Memory
             */
                
            case 0x85:
                // $aa     
            	r = ("STA");
                break;
            case 0x95:
                // $aa,X
            	r = ("STA");
                break;
            case 0x8D:
                // $aaaa   
            	r = ("STA");
                break;
            case 0x9D:
                // $aaaa,X 
            	r = ("STA");
                break;
            case 0x99:
                // $aaaa,Y 
            	r = ("STA");
                break;
            case 0x81:
                // ($aa,X) 
            	r = ("STA");
                break;
            case 0x91:
                // ($aa),Y 
            	r = ("STA");
                break;

            /**
                STX  -  Store X in Memory
             */
            case 0x86:
                // $aa   
            	r = ("STX");
                break;
            case 0x96:
                // $aa,Y
            	r = ("STX");
            	break;
            case 0x8E:
                // $aaaa 
            	r = ("STX");
                break;

            /**
                STY  -  Store Y in Memory
             */
            case 0x84:
                // $aa   
            	r = ("STY");
                break;
            case 0x94:
                // $aa,X 
            	r = ("STY");
                break;

            /**
                TAX  -  Transfer Accumulator to X
             */
            case 0xAA:
            	r = ("TAX");
            	break;

            /**
                TAY  -  Transfer Accumulator to Y
             */
            case 0xA8:
            	r = ("TAY");
                break;

            /**
                TSX  -  Transfer Stack to X
             */
            case 0xBA:
            	r = ("TSX");
                break;

            /**
                TXA  -  Transfer X to Accumulator
             */
            case 0x8A:
            	r = ("TXA");
                break;

            /**
                TXS  -  Transfer X to Stack
             */
            case 0x9A:
            	r = ("TXS");
                break;

            /**
                TYA  -  Transfer Y to Accumulator
             */
            case 0x98:
            	r = ("TYA");
                break;
            
            /**
             * default -- only encountered for unemulated instructions.
             */
            default:
            	logger.error("ENCOUNTERED UNEMULATED INSTRUCTION: " + Integer.toHexString(instructionCode));
        }
        
        return r;
    }
}
