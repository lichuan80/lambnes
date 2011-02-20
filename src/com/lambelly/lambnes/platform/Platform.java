/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lambelly.lambnes.platform;

import com.lambelly.lambnes.platform.cpu.*;
import com.lambelly.lambnes.cartridge.*;
import com.lambelly.lambnes.platform.interrupts.InterruptRequest;
import com.lambelly.lambnes.platform.interrupts.NesInterrupts;
import com.lambelly.lambnes.platform.ppu.*;
import com.lambelly.lambnes.platform.controllers.*;
import com.lambelly.lambnes.util.ArrayUtils;

import org.apache.log4j.*;

/**
 *
 * @author thomasmccarthy
 */
public class Platform
{
	private static Cartridge cartridge = null;
    private static NesCpuMemory cpuMemory = null;
    private static NesPpuMemory ppuMemory = null;
    private static CentralProcessingUnit cpu = null;
    private static PictureProcessingUnit ppu = null;
    private static NesInterrupts nesInterrupts = null;
    private static NesControllerPorts controllerPorts = null;
    private static Platform instance = null;
    private static Palette MasterPalette = null;
    private static boolean run = true;
    private static Logger logger = Logger.getLogger(Platform.class);
	public static final int CPU_FREQUENCY = ((Double)(1.789 * 1000000)).intValue();
    public static final int EIGHT_BIT_MASK = 0xFF;

    protected Platform()
    {
    	try
    	{
	    	Platform.setCpuMemory(new NesCpuMemory());
	        Platform.setCpu(new NesCpu());
	        Platform.setPpu(new NesPpu());
	        Platform.setControllerPorts(new NesControllerPorts());
	        Platform.setPpuMemory(new NesPpuMemory());
	        Platform.setMasterPalette(new Palette());
    	}
    	catch(Exception e)
    	{
    		if(logger.isDebugEnabled())
    		{
    			logger.fatal("boot exception: " + e.getMessage(),e);
    		}
    	}
    }
    
    public static void power()
    {
    	// power up initialization
    	if(logger.isDebugEnabled())
    	{
	    	logger.debug("power up initialization");
	    	logger.debug("loading cartridge");
    	}
    	
    	if (Platform.getCartridge() != null)
    	{
    		Platform.getPpuMemory().establishMirroring();
    		Platform.getCpuMemory().setProgramInstructions(Platform.getCartridge().getProgramInstructions());
    		if(logger.isDebugEnabled())
    		{
    			logger.debug("pattern tiles head:");
    		}
    		ArrayUtils.head(Platform.getCartridge().getPatternTiles(), 16);
    		Platform.getPpuMemory().setPatternTiles(Platform.getCartridge().getPatternTiles());
    	}
    	else
    	{
    		// no cartridge inserted
    		throw new IllegalStateException("no cartridge inserted");
    	}
    	
    	int cycleCount = 0;
    	Platform.setNesInterrupts(new NesInterrupts());
    	Platform.getNesInterrupts().addInterruptRequestToQueue(new InterruptRequest(InterruptRequest.interruptTypeReset));
    	Platform.getNesInterrupts().cycle();
    	
        while (isRun())
        {
        	while (cycleCount < Platform.CPU_FREQUENCY && isRun())
        	{
        		if(logger.isDebugEnabled())
        		{
	        		logger.debug("\nCycle Count: " + cycleCount);
		        	logger.debug("\n" + Platform.getCpu().getFlags());
		        	logger.debug("\n" + Platform.getCpu());
        		}
	        	
	        	// 1. cpu cycle
	        	Platform.getCpu().processNextInstruction();
	        	
	        	// 2. Execute Interrupts.
	        	Platform.getNesInterrupts().cycle();
	        	
	        	// 3. ppu cycle
	        	Platform.getPpu().cycle(cycleCount); 

	        	// 4. controller cycle 
	        	Platform.getControllerPorts().cycle();
	        	
	        	cycleCount++;
        	}
        }
        
        // shutdown
    }

    public static Platform getInstance()
    {
        if(instance == null)
        {
            instance = new Platform();
        }
        return instance;
    }

    /**
     * @return the programMemory
     */
    public static NesCpuMemory getCpuMemory()
    {
        return cpuMemory;
    }

    /**
     * @param aProgramMemory the programMemory to set
     */
    private static void setCpuMemory(NesCpuMemory aCpuMemory)
    {
        cpuMemory = aCpuMemory;
    }

    /**
     * @return the characterMemory
     */
    public static NesPpuMemory getPpuMemory()
    {
        return ppuMemory;
    }

    /**
     * @param aCharacterMemory the characterMemory to set
     */
    private static void setPpuMemory(NesPpuMemory aPpuMemory)
    {
        ppuMemory = aPpuMemory;
    }

    /**
     * @return the cpu
     */
    public static CentralProcessingUnit getCpu()
    {
        return cpu;
    }

    /**
     * @param aCpu the cpu to set
     */
    private static void setCpu(CentralProcessingUnit aCpu)
    {
        cpu = aCpu;
    }

    /**
     * @return the ppu
     */
    public static PictureProcessingUnit getPpu()
    {
        return ppu;
    }

    /**
     * @param aPpu the ppu to set
     */
    private static void setPpu(PictureProcessingUnit aPpu)
    {
        ppu = aPpu;
    }

	public static Palette getMasterPalette()
	{
		return MasterPalette;
	}

	public static void setMasterPalette(Palette masterPalette)
	{
		MasterPalette = masterPalette;
	}

	public static boolean isRun()
	{
		return run;
	}

	public static void setRun(boolean run)
	{
		Platform.run = run;
	}

	public static NesInterrupts getNesInterrupts()
	{
		return nesInterrupts;
	}

	public static void setNesInterrupts(NesInterrupts nesInterrupts)
	{
		Platform.nesInterrupts = nesInterrupts;
	}

	public static Cartridge getCartridge()
	{
		return cartridge;
	}

	public static void setCartridge(Cartridge cartridge)
	{
		Platform.cartridge = cartridge;
	}

	public static NesControllerPorts getControllerPorts()
	{
		return controllerPorts;
	}

	public static void setControllerPorts(NesControllerPorts controllerPorts)
	{
		Platform.controllerPorts = controllerPorts;
	}
}
