/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lambelly.lambnes.test;

import org.junit.Test;
import static org.junit.Assert.*;
import com.lambelly.lambnes.test.utils.TestUtils;
import com.lambelly.lambnes.util.ArrayUtils;

/**
 *
 * @author thomasmccarthy
 */
public class TestUtilsTest
{
    @Test
    public void createTestArray()
    {
        int[] b = TestUtils.createTestIntArray(501);
        
        ArrayUtils.head(b, 10);
        ArrayUtils.foot(b, 10);
        ArrayUtils.printArray(b);

        assertEquals(501,b.length);
        assertEquals(0,b[0]);
        assertEquals(244,b[500]);
        assertEquals(0xF4,b[500]);
    }

}
