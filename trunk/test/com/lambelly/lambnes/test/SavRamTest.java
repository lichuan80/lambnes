/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lambelly.lambnes.test;

import org.junit.*;
import com.lambelly.lambnes.util.*;
import com.lambelly.lambnes.cartridge.*;
import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.platform.ppu.NesPpuMemory;
import com.lambelly.lambnes.util.SavRamIOUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.apache.log4j.*;
import java.io.FileNotFoundException;

/**
 *
 * @author thomasmccarthy
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:beans.xml"})
public class SavRamTest
{
    private Logger logger = Logger.getLogger(SavRamTest.class);

    @Before
    public void initialize() throws FileNotFoundException
    {

    }
    
    @Test
    public void testOut() throws Exception
    {
    	int[] a = {1,2,3,4,5,6,7,8,9,0};
    	SavRamIOUtil.writeSavRam(a, "test");
    }
    
    @Test
    public void testIn() throws Exception
    {
    	int[] a;
    	a = SavRamIOUtil.readSavRam("test");
    	
    	for (int i : a)
    	{
    		logger.debug(i);
    	} 	
    }
}
