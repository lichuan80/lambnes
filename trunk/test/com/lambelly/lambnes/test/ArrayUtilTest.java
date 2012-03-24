/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lambelly.lambnes.test;

import static org.junit.Assert.*;
import com.lambelly.lambnes.util.ArrayUtils;
import com.lambelly.lambnes.test.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 *
 * @author thomasmccarthy
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:beans.xml"})
public class ArrayUtilTest
{
	@Autowired
	private TestUtils testUtils;
	
    @Test
    public void subarray()
    {
        int[] programInstructions = this.getTestUtils().createTestIntArray(32768);

        int[] lower = org.apache.commons.lang.ArrayUtils.subarray(programInstructions, 0, 16384);
        int[] upper = org.apache.commons.lang.ArrayUtils.subarray(programInstructions, 16384, 32769);

        System.out.println("lower: " + lower.length);
        assertEquals(16384,lower.length);
        assertEquals(16384,upper.length);
    }

    @Test
    public void bufferArray()
    {
        int[] original = {0x0f, (byte)0xff};
        int[] buffered = ArrayUtils.bufferArray(original, 16384, (byte)0xbb);
        
        assertEquals(16384,buffered.length);
        assertEquals((byte)0xbb,buffered[original.length+1]);
    }

	public TestUtils getTestUtils()
    {
    	return testUtils;
    }

	public void setTestUtils(TestUtils testUtils)
    {
    	this.testUtils = testUtils;
    }
}
