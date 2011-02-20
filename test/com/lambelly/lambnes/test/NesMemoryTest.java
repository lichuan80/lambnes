/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lambelly.lambnes.test;

import com.lambelly.lambnes.test.utils.TestUtils;
import com.lambelly.lambnes.util.BitUtils;
import com.lambelly.lambnes.platform.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.log4j.*;


/**
 *
 * @author thomasmccarthy
 */
public class NesMemoryTest
{
    private static Logger logger = Logger.getLogger(NesMemoryTest.class);

    public NesMemoryTest()
    {
    }

    @BeforeClass
    public static void setUpClass() throws Exception
    {
    }

    @AfterClass
    public static void tearDownClass() throws Exception
    {
    }

    @Before
    public void setUp()
    {
    	// make sure platform has been instantiated
    	Platform p = Platform.getInstance();
    	
    	// establish memories
    	Platform.getCpuMemory().setZeroPage(TestUtils.createTestIntArray(256));
    	Platform.getCpuMemory().setStackMemory(TestUtils.createTestIntArray(256));
    	Platform.getCpuMemory().setRam(TestUtils.createTestIntArray(1536));
    	//Platform.getCpuMemory().setInputOutput1(TestUtils.createTestIntegerArray(8));
    	Platform.getCpuMemory().setInputOutput2(TestUtils.createTestIntegerArray(32));
    	Platform.getCpuMemory().setExpansionRam(TestUtils.createTestIntArray(8160));
    	Platform.getCpuMemory().setSram(TestUtils.createTestIntArray(8192));
    	Platform.getCpuMemory().setProgramInstructions(TestUtils.createTestIntArray(32768));
    	Platform.getCpuMemory().resetCounters();
    }

    @After
    public void tearDown()
    {
    }

    @Test
    public void testImmediate()
    {
        int zero = Platform.getCpuMemory().getImmediateValue();
        assertEquals(0,zero);
        assertEquals(0x8001,Platform.getCpuMemory().getProgramCounter());
        int one = Platform.getCpuMemory().getImmediateValue();
        assertEquals(1,one);
    }

    @Test
    public void testAbsolute()
    {
        logger.debug("program counter: " + Platform.getCpuMemory().getProgramCounter());
        int test1 = Platform.getCpuMemory().getAbsoluteValue();
        assertFalse(10 == test1);
        assertFalse(100 == test1);
        assertEquals(0,test1);
        assertEquals(0x8002,Platform.getCpuMemory().getProgramCounter());
        
        int test2 = Platform.getCpuMemory().getAbsoluteValue();
        assertEquals(2,test2);
        Platform.getCpuMemory().getPrgRomLowerBank()[4] = 0xFE;
        Platform.getCpuMemory().getPrgRomLowerBank()[5] = 0x02;
     
        int test3 = Platform.getCpuMemory().getAbsoluteValue();
        assertEquals(254, test3);
    }
    
    @Test
    public void testAbsoluteX()
    {
    	// 0x1C0
    	Platform.getCpu().setX(0xC0);
    	int test1 = Platform.getCpuMemory().getAbsoluteIndexedXValue(); 	
    	assertEquals(0xC0,test1);
    }

    @Test
    public void testAbsoluteY()
    {
    	// 0x1C0
    	Platform.getCpu().setY(0xC0);
    	int test1 = Platform.getCpuMemory().getAbsoluteIndexedXValue(); 	
    	assertEquals(0xC0,test1);
    }    
    
    @Test
    public void zeroPage()
    {
    	int test1 = Platform.getCpuMemory().getZeroPageValue();
    	assertEquals(0,test1);
    	int test2 = Platform.getCpuMemory().getZeroPageValue();
    	assertEquals(1,test2);
    	
    	Platform.getCpuMemory().getZeroPage()[2] = 0xF;
    	int test3 = Platform.getCpuMemory().getZeroPageValue();
    	assertEquals(0xF,test3);
    }
    
    @Test
    public void testZeroPageIndexedIndirect()
    {
    	logger.debug("testing zero page indexed indirect");
    	Platform.getCpu().setX(0x0A);
    	int test1 = Platform.getCpuMemory().getIndexedIndirectXValue();
    	// loads 11 into memory, which points at 0B0A
    	assertEquals(0x0A, test1);
    	
    	int test2 = Platform.getCpuMemory().getIndexedIndirectXValue();
    	// loads 12 into memory which points at 0C0B
    	assertEquals(0x0B, test2);
    }
    
    @Test
    public void ZeroPageXIndexed()
    {
    	Platform.getCpu().setX(3);
    	int test1 = Platform.getCpuMemory().getZeroPageIndexedXValue();
    	assertEquals(3,test1);
    	int test2 = Platform.getCpuMemory().getZeroPageIndexedXValue();
    	assertEquals(4,test2);
    	
    	Platform.getCpu().setX(0xFF);
    	int test3 = Platform.getCpuMemory().getZeroPageIndexedXValue();
    	assertEquals(1,test3);
    }
    
    @Test
    public void ZeroPageYIndexed()
    {
    	Platform.getCpu().setY(3);
    	int test1 = Platform.getCpuMemory().getZeroPageIndexedYValue();
    	assertEquals(3,test1);
    	int test2 = Platform.getCpuMemory().getZeroPageIndexedYValue();
    	assertEquals(4,test2);
    	
    	Platform.getCpu().setY(0xFF);
    	int test3 = Platform.getCpuMemory().getZeroPageIndexedYValue();
    	assertEquals(1,test3);
    }    
    
    @Test
    public void RelativeAddress()
    {
    	Platform.getCpuMemory().setProgramCounter(0x800E);
    	Platform.getCpuMemory().setMemoryFromHexAddress(0x800E, 0xFB);
    	int address = Platform.getCpuMemory().getRelativeAddress();
    	logger.debug((byte)0xFb);
    	assertEquals(32778, address);
    	logger.debug("address is now: " + Integer.toHexString(address));
    }
    
    @Test
    public void pushStack()
    {
    	Platform.getCpuMemory().pushStack(0x89);
    	assertEquals(0xFF, Platform.getCpuMemory().getStackPointer());
    	int value = Platform.getCpuMemory().popStack();
    	assertEquals(0, Platform.getCpuMemory().getStackPointer());
    	assertEquals(0x89, value);
    	
    	// try pushing address.
    	int address = 0xabcd;
    	int a[] = BitUtils.splitAddress(address);
    	Platform.getCpuMemory().pushStack(a[1]);
    	Platform.getCpuMemory().pushStack(a[0]);
    	
    	assertEquals(0xcd, Platform.getCpuMemory().popStack());
    	assertEquals(0xab, Platform.getCpuMemory().popStack());
    }
    
    @Test
    public void readFrom0x2002()
    {
    	logger.debug(Platform.getCpuMemory().getMemoryFromHexAddress(0x2002));
    }    
}