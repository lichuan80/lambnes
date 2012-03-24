/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lambelly.lambnes.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

import com.lambelly.lambnes.platform.cpu.NesCpu;
import com.lambelly.lambnes.platform.cpu.NesCpuMemory;
import com.lambelly.lambnes.test.utils.TestUtils;
import com.lambelly.lambnes.util.ArrayUtils;

/**
 *
 * @author thomasmccarthy
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:beans.xml"})
public class TestUtilsTest
{
	@Autowired
	private TestUtils testUtils;
	
    @Test
    public void createTestArray()
    {
        int[] b = this.getTestUtils().createTestIntArray(501);
        
        ArrayUtils.head(b, 10);
        ArrayUtils.foot(b, 10);
        ArrayUtils.printArray(b);

        assertEquals(501,b.length);
        assertEquals(0,b[0]);
        assertEquals(244,b[500]);
        assertEquals(0xF4,b[500]);
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
