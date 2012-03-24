package com.lambelly.lambnes.test;

import com.lambelly.lambnes.platform.cpu.NesCpu;
import com.lambelly.lambnes.platform.cpu.NesCpuAddressingModes;
import com.lambelly.lambnes.platform.cpu.NesCpuMemory;
import com.lambelly.lambnes.test.utils.TestUtils;
import com.lambelly.lambnes.util.BitUtils;
import com.lambelly.lambnes.platform.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
import org.apache.log4j.*;

/**
 *
 * @author thomasmccarthy
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:beans.xml"})
public class NesMemoryTest
{
	@Autowired
	private NesCpu cpu;
	@Autowired
	private NesCpuMemory cpuMemory;
	@Autowired
	private NesCpuAddressingModes addressingModes;
	@Autowired
	private TestUtils testUtils;
    private static Logger logger = Logger.getLogger(NesMemoryTest.class);

    
    public NesMemoryTest()
    {
    }

    @Before
    public void setUp() throws Exception
    {
    	// establish memories
    	this.getTestUtils().createTestPlatform();
    }

    @Test
    public void testImmediate()
    {
        int zero = this.getAddressingModes().getImmediateValue();
        assertEquals(0,zero);
        assertEquals(0x8001,this.getCpuMemory().getProgramCounter());
        int one = this.getAddressingModes().getImmediateValue();
        assertEquals(1,one);
    }

    @Test
    public void testAbsolute()
    {
        logger.debug("program counter: " + this.getCpuMemory().getProgramCounter());
        int test1 = this.getAddressingModes().getAbsoluteValue();
        assertFalse(10 == test1);
        assertFalse(100 == test1);
        assertEquals(0,test1);
        assertEquals(0x8002,this.getCpuMemory().getProgramCounter());
        
        int test2 = this.getAddressingModes().getAbsoluteValue();
        assertEquals(2,test2);
        this.getCpuMemory().setMemoryFromHexAddress(0x8004,0xFE);
        this.getCpuMemory().setMemoryFromHexAddress(0x8005,0x02);
     
        int test3 = this.getAddressingModes().getAbsoluteValue();
        assertEquals(254, test3);
    }
    
    @Test
    public void testAbsoluteX()
    {
    	// 0x1C0
    	this.getCpu().setX(0xC0);
    	int test1 = this.getAddressingModes().getAbsoluteIndexedXValue(); 	
    	assertEquals(0xC0,test1);
    }

    @Test
    public void testAbsoluteY()
    {
    	// 0x1C0
    	this.getCpu().setY(0xC0);
    	int test1 = this.getAddressingModes().getAbsoluteIndexedXValue(); 	
    	assertEquals(0xC0,test1);
    }    
    
    @Test
    public void zeroPage()
    {
    	int test1 = this.getAddressingModes().getZeroPageValue();
    	assertEquals(0,test1);
    	int test2 = this.getAddressingModes().getZeroPageValue();
    	assertEquals(1,test2);
    	
    	this.getCpuMemory().setMemoryFromHexAddress(0x0002,0xF);
    	int test3 = this.getAddressingModes().getZeroPageValue();
    	assertEquals(0xF,test3);
    }
    
    @Test
    public void testZeroPageIndexedIndirect()
    {
    	logger.debug("testing zero page indexed indirect");
    	this.getCpu().setX(0x0A);
    	int test1 = this.getAddressingModes().getIndexedIndirectXValue();
    	// loads 11 into memory, which points at 0B0A
    	assertEquals(0x0A, test1);
    	
    	int test2 = this.getAddressingModes().getIndexedIndirectXValue();
    	// loads 12 into memory which points at 0C0B
    	assertEquals(0x0B, test2);
    }
    
    @Test
    public void ZeroPageXIndexed()
    {
    	this.getCpu().setX(3);
    	int test1 = this.getAddressingModes().getZeroPageIndexedXValue();
    	assertEquals(3,test1);
    	int test2 = this.getAddressingModes().getZeroPageIndexedXValue();
    	assertEquals(4,test2);
    	
    	this.getCpu().setX(0xFF);
    	int test3 = this.getAddressingModes().getZeroPageIndexedXValue();
    	assertEquals(1,test3);
    }
    
    @Test
    public void ZeroPageYIndexed()
    {
    	this.getCpu().setY(3);
    	int test1 = this.getAddressingModes().getZeroPageIndexedYValue();
    	assertEquals(3,test1);
    	int test2 = this.getAddressingModes().getZeroPageIndexedYValue();
    	assertEquals(4,test2);
    	
    	this.getCpu().setY(0xFF);
    	int test3 = this.getAddressingModes().getZeroPageIndexedYValue();
    	assertEquals(1,test3);
    }    
    
    @Test
    public void RelativeAddress()
    {
    	this.getCpuMemory().setProgramCounter(0x800E);
    	this.getCpuMemory().setMemoryFromHexAddress(0x800E, 0xFB);
    	int address = this.getAddressingModes().getRelativeAddress();
    	assertEquals(32778, address);
    	// fb == -5
    	// 800F - 5 = 800A
    	logger.debug("address is now: " + Integer.toHexString(address));
    }
    
    @Test
    public void pushStack()
    {
    	this.getCpuMemory().pushStack(0x89);
    	assertEquals(0xFF, this.getCpuMemory().getStackPointer());
    	int value = this.getCpuMemory().popStack();
    	assertEquals(0, this.getCpuMemory().getStackPointer());
    	assertEquals(0x89, value);
    	
    	// try pushing address.
    	int address = 0xabcd;
    	int a[] = BitUtils.splitAddress(address);
    	this.getCpuMemory().pushStack(a[1]);
    	this.getCpuMemory().pushStack(a[0]);
    	
    	assertEquals(0xcd, this.getCpuMemory().popStack());
    	assertEquals(0xab, this.getCpuMemory().popStack());
    }
    
    @Test
    public void readFrom0x2002()
    {
    	logger.debug(this.getCpuMemory().getMemoryFromHexAddress(0x2002));
    }

	public NesCpu getCpu()
    {
    	return cpu;
    }

	public void setCpu(NesCpu cpu)
    {
    	this.cpu = cpu;
    }

	public NesCpuMemory getCpuMemory()
    {
    	return cpuMemory;
    }

	public void setCpuMemory(NesCpuMemory cpuMemory)
    {
    	this.cpuMemory = cpuMemory;
    }

	public TestUtils getTestUtils()
    {
    	return testUtils;
    }

	public void setTestUtils(TestUtils testUtils)
    {
    	this.testUtils = testUtils;
    }

	public NesCpuAddressingModes getAddressingModes()
    {
    	return addressingModes;
    }

	public void setAddressingModes(NesCpuAddressingModes addressingModes)
    {
    	this.addressingModes = addressingModes;
    }    
}