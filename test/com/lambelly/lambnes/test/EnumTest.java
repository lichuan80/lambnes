/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lambelly.lambnes.test;

import static org.junit.Assert.*;
import com.lambelly.lambnes.platform.cpu.Instruction;

import org.junit.Test;
import org.apache.log4j.*;
import java.io.FileNotFoundException;

/**
 *
 * @author thomasmccarthy
 */
public class EnumTest
{
	private Logger logger = Logger.getLogger(EnumTest.class);
	
    @Test
    public void header() throws FileNotFoundException
    {
        Instruction i = Instruction.ADC_ABSOLUTE_Y;
        assertEquals(0x79, i.getOpCode());
        
        Instruction i2 = Instruction.get(0x69);
        logger.debug(i2.name());
    }
}
