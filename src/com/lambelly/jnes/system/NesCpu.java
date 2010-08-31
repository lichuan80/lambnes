/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lambelly.jnes.system;

/**
 *
 * @author thomasmccarthy
 */
public class NesCpu implements CentralProcessingUnit
{
    public NesCpu()
    {

    }

    public void processInstruction(byte b)
    {
        switch (b)
        {
            /**
                ADC  -  Add to Accumulator with Carry
             */
            case 0x69:
                // #aa
                break;
            case 0x65:
                // aa
                break;
            case 0x75:
                // aa,X
                break;
            case 0x6D:
                // aaaa
                break;
            case 0x7D:
                // aaaa,X
                break;

            /**
                AND  -  AND Memory with Accumulator
             */
            case 0x29:
                // #aa
                break;
            case 0x25:
                // aa
                break;
            case 0x35:
                // aa,X
                break;
            case 0x2D:
                // aaaa
                break;
            case 0x3D:
                // aaaa,X
                break;
            case 0x39:
                // aaaa,Y
                break;
            case 0x21:
                // (aa,X)
                break;
            case 0x31:
                // (aa),Y
                break;

            /**
                ASL  -  Arithmatic Shift Left
             */
            case 0x0A:
                // A
                break;
            case 0x06:
                // aa
                break;
            case 0x16:
                // aa,X
                break;
            case 0x0E:
                // aaaa
                break;

            case 0x1E:
                //aaaa,X
                break;

            /**
                BCC  -  Branch on Carry Clear
             */
            case (byte) 0x90:
                // aa
                break;

            /**
                BCS  -  Branch on Carry Set
             */
            case (byte)0xC0:
                // aa
                break;

            /**
                BEQ  -  Branch Zero Set
             */
            case (byte)0xF0:
                // aa
                break;

            /**
                BIT  -  Test Bits in Memory with Accumulator
             */
            case 0x24:
                // aa
                break;
            case 0x2C:
                // aaaa
                break;

            /**
                BMI  -  Branch on Result Minus
             */
            case 0x30:
                // aa
                break;

            /**
                BNE  -  Branch on Z reset
             */

            case (byte)0xD0:
                // aa
                break;
                
            /**
                BPL  -  Branch on Result Plus (or Positive)
             */
            case 0x10:
                // aa
                break;

            /**
                BRK  -  Force a Break
             */
            case 0x00:
                break;
                
            /**
                BVC  -  Branch on Overflow Clear
             */
            case 0x50:
                // aa
                break;

            /**
                BVS  -  Branch on Overflow Set
             */
            case 0x70:
                // aa
                break;

            /**
                CLC  -  Clear Carry Flag
             */
            case 0x18:
                break;

            /**
                CLD  -  Clear Decimal Mode
             */
            case (byte)0xD8:
                break;
                
            /**
                CLI  -  Clear Interrupt Disable
             */
            case 0x58:
                break;

            /**
                CLV  -  Clear Overflow Flag
             */
            case (byte)0xB8:
                break;


            /**
                CMP  -  Compare Memory and Accumulator
             */
            case (byte)0xC9:
                // #aa
                break;
            case (byte)0xC5:
                // aa
                break;
            case (byte)0xD5:
                // aa,X
                break;
            case (byte)0xCD:
                // aaaa
                break;
            case (byte)0xDD:
                // aaaa,X
                break;
            case (byte)0xD9:
                // aaaa,Y
                break;
            case (byte)0xC1:
                // (aa,X)
                break;
            case (byte)0xD1:
                // (aa),Y
                break;
                
            /**
                CPX  -  Compare Memory and X register
             */
            case (byte)0xE0:
                // #aa
                break;
            case (byte)0xE4:
                // aa
                break;
            case (byte)0xEC:
                // aaaa
                break;

            /**
                DEC  -  Decrement Memory by One
             */
            case (byte)0xC6:
                // aa
                break;
            case (byte)0xD6:
                // aa,X
                break;
            case (byte)0xCE:
                // aaaa
                break;
            case (byte)0xDE:
                // aaaa,X
                break;
                
            /**
                DEX  -  Decrement X
             */
            case (byte)0xCA:
                break;

            /**
                DEY  -  Decrement Y
             */
            case (byte)0x88:
                break;

            /**
                EOR  -  Exclusive-OR Memory with Accumulator
             */
            case 0x49:
                // #aa
                break;
            case 0x45:
                // $aa
                break;
            case 0x55:
                // $aa,X
                break;
            case 0x4D:
                // $aaaa
                break;
            case 0x5D:
                // $aaaa,X
                break;
            case 0x59:
                // $aaaa,Y
                break;
            case 0x41:
                // ($aa,X)
                break;
            case 0x51:
                // ($aa),Y
                break;

            /**
                INC  -  Increment Memory by one
             */
            case (byte)0xE6:
                // $aa
                break;
            case (byte)0xF6:
                //  $aa,X
                break;
            case (byte)0xEE:
                // $aaaa
                break;
            case (byte)0xFE:
                // $aaaa,X
                break;

            /**
                INX  -  Increment X by one
             */
            case (byte)0xE8:
                break;

            /**
                INY  -  Increment Y by one
             */
            case (byte)0xC8:
                break;

            /**
                JMP  -  Jump
             */
            case 0x4C:
                // $aaaa
                break;
            case 0x6C:
                // ($aaaa)
                break;

            /**
                JSR  -  Jump to subroutine
             */
            case 0x20:
                // $aaaa
                break;
                
            /**
                LDA  -  Load Accumulator with memory
             */
            case (byte)0xA9:
                // #aa
                break;
            case (byte)0xA5:
                // $aa
                break;
            case (byte)0xB5:
                // $aa,X
                break;
            case (byte)0xAD:
                // $aaaa
                break;
            case (byte)0xBD:
                // $aaaa,X
                break;
            case (byte)0xB9:
                //  $aaaa,Y
                break;
            case (byte)0xA1:
                //  ($aa,X)
                break;
            case (byte)0xB1:
                // ($aa),Y
                break;

            /**
                LDX  -  Load X with Memory
             */
            case (byte)0xA2:
                // #aa      
                break;
            case (byte)0xA6:
                // $aa      
                break;
            case (byte)0xB6:
                // $aa,Y    
                break;
            case (byte)0xAE:
                // $aaaa    
                break;
            case (byte)0xBE:
                // $aaaa,Y  
                break;

            /**
                LDY  -  Load Y with Memory
             */
            case (byte)0xA0:
                // #aa      
                break;
            case (byte)0xA4:
                // $aa      
                break;
            case (byte)0xB4:
                // $aa,X    
                break;
            case (byte)0xAC:
                // $aaaa    
                break;
            case (byte)0xBC:
                // $aaaa,X  
                break;

            /**
                LSR  -  Logical Shift Right
             */
            case 0x4A:
                // A        
                break;
            case 0x46:
                // $aa      
                break;
            case 0x56:
                // $aa,X    
                break;
            case 0x4E:
                // $aaaa    
                break;
            case 0x5E:
                // $aaaa,X  
                break;

            /**
                NOP  -  No Operation
             */
            case (byte)0xEA:
                break;

            /**
                ORA  -  OR Memory with Accumulator
             */
            case 0x09:
                // #aa 
                break;
            case 0x05:
                // $aa 
                break;
            case 0x15:
                // $aa,X   
                break;
            case 0x0D:
                // $aaaa   
                break;
            case 0x1D:
                // $aaaa,X 
                break;
            case 0x19:
                // $aaaa,Y 
                break;
            case 0x01:
                // ($aa,X) 
                break;
            case 0x11:
                // ($aa),Y 
                break;

            /**
                PHA  -  Push Accumulator on Stack
             */
            case 0x48:
                break;

            /**
                PHP  -  Push Processor Status on Stack
             */
            case 0x08:
                break;

            /**
                PLA  -  Pull Accumulator from Stack
             */
            case 0x68:
                break;

            /**
                PLP  -  Pull Processor Status from Stack
             */
            case 0x28:
                break;

            /**
                ROL  -  Rotate Left
             */
            case 0x2A:
                // A       
                break;
            case 0x26:
                // $aa     
                break;
            case 0x36:
                // $aa,X   
                break;
            case 0x2E:
                // $aaaa   
                break;
            case 0x3E:
                // $aaaa,X 
                break;

            /**
                ROR  -  Rotate Right
             */
            case 0x6A:
                // A       
                break;
            case 0x66:
                // $aa     
                break;
            case 0x76:
                // $aa,X   
                break;
            case 0x6E:
                // $aaaa   
                break;
            case 0x7E:
                // $aaaa,X 
                break;

            /**
                RTI  -  Return from Interrupt
             */
            case 0x40:
                break;

            /**
                RTS  -  Return from Subroutine
             */
            case 0x60:
                break;

            /**
                SBC  -  Subtract from Accumulator with Carry
             */
            case (byte)0xE9:
                // #aa     
                break;
            case (byte)0xE5:
                // $aa     
                break;
            case (byte)0xF5:
                // $aa,X   
                break;
            case (byte)0xED:
                // $aaaa   
                break;
            case (byte)0xFD:
                // $aaaa,X 
                break;
            case (byte)0xF9:
                // $aaaa,Y 
                break;
            case (byte)0xE1:
                // ($aa,X) 
                break;
            case (byte)0xF1:
                // ($aa),Y 
                break;

            /**
                SEC  -  Set Carry Flag
             */
            case 0x38:
                break;

            /**
                SED  -  Set Decimal Mode
             */
            case (byte)0xF8:
                break;

            /**
                SEI  -  Set Interrupt Disable
             */
            case 0x78:
                break;

            /**
                STA  -  Store Accumulator in Memory
             */
            case (byte)0x85:
                // $aa     
                break;
            case (byte)0x95:
                // $aa,X   
                break;
            case (byte)0x8D:
                // $aaaa   
                break;
            case (byte)0x9D:
                // $aaaa,X 
                break;
            case (byte)0x99:
                // $aaaa,Y 
                break;
            case (byte)0x81:
                // ($aa,X) 
                break;
            case (byte)0x91:
                // ($aa),Y 
                break;

            /**
                STX  -  Store X in Memory
             */
            case (byte)0x86:
                // $aa   
                break;
            case (byte)0x96:
                // $aa,Y 
                break;
            case (byte)0x8E:
                // $aaaa 
                break;

            /**
                STY  -  Store Y in Memory
             */
            case (byte)0x84:
                // $aa   
                break;
            case (byte)0x94:
                // $aa,X 
                break;
            case (byte)0x8C:
                // $aaaa 
                break;

            /**
                TAX  -  Transfer Accumulator to X
             */
            case (byte)0xAA:
                break;

            /**
                TAY  -  Transfer Accumulator to Y
             */

            case (byte)0xA8:
                break;

            /**
                TSX  -  Transfer Stack to X
             */
            case (byte)0xBA:
                break;

            /**
                TXA  -  Transfer X to Accumulator
             */
            case (byte)0x8A:
                break;

            /**
                TXS  -  Transfer X to Stack
             */
            case (byte)0x9A:
                break;

            /**
                TYA  -  Transfer Y to Accumulator
             */
            case (byte)0x98:
                break;
        }
    }
}
