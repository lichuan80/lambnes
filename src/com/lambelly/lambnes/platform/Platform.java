/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lambelly.lambnes.platform;

import java.util.Scanner;

import com.lambelly.lambnes.platform.cpu.*;
import com.lambelly.lambnes.platform.ppu.*;

import org.apache.log4j.*;

/**
 *
 * @author thomasmccarthy
 */
public class Platform
{
    private static NesCpuMemory cpuMemory = null;
    private static NesPpuMemory ppuMemory = null;
    private static CentralProcessingUnit cpu = null;
    private static PictureProcessingUnit ppu = null;
    private static Platform instance = null;
    private static Palette MasterPalette = null;
    private static boolean run = true;
    private static Logger logger = Logger.getLogger(Platform.class);
	private static final int CPU_FREQUENCY = ((Double)(1.789 * 1000000)).intValue();
	private static final int REFRESH_RATE = 60;
	private static final int NUM_SCANLINES_PER_FRAME = 262;
	private static final double NUM_CYCLES_PER_SCANLINE = (Platform.CPU_FREQUENCY / Platform.REFRESH_RATE) / Platform.NUM_SCANLINES_PER_FRAME;
    public static final int EIGHT_BIT_MASK = 0xFF;
    public static final String DEFAULT_ROM = "rom.zip";

    protected Platform()
    {
    	try
    	{
	    	Platform.setCpuMemory(new NesCpuMemory());
	        Platform.setCpu(new NesCpu());
	        Platform.setPpu(new NesPpu());
	        Platform.setPpuMemory(new NesPpuMemory());
	        Platform.setMasterPalette(new Palette());
    	}
    	catch(Exception e)
    	{
    		logger.fatal("boot exception: " + e.getMessage(),e);
    	}
    }
    
    public static void power()
    {
    	int cycleCount = 0;
    	int scanlineCount = 0;
    	
        while (isRun())
        {
        	while (cycleCount < Platform.CPU_FREQUENCY)
        	{
        		logger.debug("\nCycle Count: " + cycleCount);
        		logger.debug("\nScanline Count: " + scanlineCount);
	        	logger.debug("\n" + Platform.getCpu().getFlags());
	        	logger.debug("\n" + Platform.getCpu());
	        	
	        	// 1. Fetch the opcode from the ROM.
	        	// 2. Execute the opcode.
	
	        	Platform.getCpu().processNextInstruction();
	        	
	        	//3. Execute Interrupts.
	        	//FFFAh = NMI (VBlank)
	        	//FFFCh = RESET
	        	//FFFEh = IRQ/BRK (software)
	
	        	
	        	//4. Read/Write to memory.
	        	Platform.getPpu().doRegisterReadsWrites();
	        	
	        	//5. Do the cyclic tasks
	        	logger.debug("scanlineTest: " + cycleCount % Platform.NUM_CYCLES_PER_SCANLINE);
	        	if (cycleCount % Platform.NUM_CYCLES_PER_SCANLINE == 0)
	        	{
	        		scanlineCount++;
	        		logger.debug("scanline");
	        		
	        		if (scanlineCount > 240 && scanlineCount <= 262)
	        		{
	        			// vblank
	        			Platform.getPpu().getPpuStatusRegister().setVblank(true);
	        		}
	        		else if (scanlineCount > 262)
	        		{
	        			// reset scanlineCount (new frame)
	        			Platform.getPpu().getPpuStatusRegister().setVblank(false);
	        			scanlineCount = 1; 
	        		}
	        		else
	        		{
	        			// draw scanline
	        		}
	        	}
	        	
	        	cycleCount++;
	        	
	        	if (false) // manual cpu step
	        	{
	        		Scanner scanner = new Scanner(System.in);
	        		String s = scanner.nextLine();
	        	}
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
}
