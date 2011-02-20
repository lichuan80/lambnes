/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lambelly.lambnes.test;

import org.junit.Test;
import org.apache.log4j.*;

/**
 *
 * @author thomasmccarthy
 */
public class CycleTest
{
    private Logger logger = Logger.getLogger(CycleTest.class);

    @Test
    public void countCycles() throws Exception
    {	
    	double cpuFrequencyShort = 1.789;
    	Double d = cpuFrequencyShort * 1000000;
    	int cpuFrequency = d.intValue();
    	int refreshRate = 60;
    	int numScanlinePerFrame = 262;
    	double numCyclesPerScanline = (cpuFrequency / refreshRate) / numScanlinePerFrame;
    	int cycleCount = 0;
    	long starttime = 0; // variable declared
    	long timeleft = 0;
    	long endtime = 0;
    	long waittime = 0;
    	
    	logger.debug(numCyclesPerScanline);
    	
    	//... 
    	// for the first time, remember the timestamp
    	logger.debug(System.currentTimeMillis());
    	do
    	{
	    	if (cycleCount == 0) 
	    	{ 
	    	    starttime = System.currentTimeMillis();
	    	    endtime = starttime + 1000;
	    	} 
	    	// the next timestamp we want to wake up 
	    	//logger.debug(1);
	    	//waittime =  ((cpuFrequency - cycleCount) / (endtime - System.currentTimeMillis()));
	    	//logger.debug(waittime);
	    	//starttime += waittime;
	    	// Wait until the desired next time arrives using nanosecond 
	    	// accuracy timer (wait(time) isn't accurate enough on most platforms)  
	    	//LockSupport.parkNanos((long)(Math.max(0,  
	    	//    starttime - System.currentTimeMillis()) * 1000000));
	    	
	    	if (cycleCount % numCyclesPerScanline == 0)
	    	{
	    		logger.debug("scanline");
	    	}
	    	
	    	cycleCount++;
    	} while (System.currentTimeMillis() < endtime);
    	logger.debug("cycleCount: " + cycleCount);
    	logger.debug(System.currentTimeMillis());
    }
}
