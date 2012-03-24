package com.lambelly.lambnes.platform;

import com.lambelly.lambnes.platform.cpu.*;
import com.lambelly.lambnes.platform.mappers.MMC1Mapper;
import com.lambelly.lambnes.platform.mappers.UnromMapper;
import com.lambelly.lambnes.cartridge.*;
import com.lambelly.lambnes.platform.interrupts.InterruptRequest;
import com.lambelly.lambnes.platform.interrupts.NesInterrupts;
import com.lambelly.lambnes.platform.ppu.*;
import com.lambelly.lambnes.platform.apu.*;
import com.lambelly.lambnes.platform.controllers.*;

import org.apache.log4j.*;


/**
 *
 * @author thomasmccarthy
 */
public class Platform
{
    private boolean run = true;
    private static Logger logger = Logger.getLogger(Platform.class);
	public static final int CPU_FREQUENCY = ((Double)(1.789 * 1000000)).intValue();
    public static final int EIGHT_BIT_MASK = 0xFF;
    public static final int SIXTEEN_BIT_MASK = 0xFFFF;
    private static long cycleCount = 0;
    private NesCpu cpu;
    private NesApu apu;
    private NesCpuMemory cpuMemory;
    private NesPpu ppu;
    private NesPpuMemory ppuMemory;
    private NesInterrupts interrupts;
    private NesControllerPorts controllerPorts;
    private Ines cartridge;
    
    
    private Platform()
    {
    	
    }
    
    public void init()
    {
    	if (this.getCartridge() != null)
    	{	
    		// establish mapper
    		// logger.debug("using mapper: " + this.getCartridge().getHeader().getMapperID());
    		
    		if (this.getCartridge().getHeader().getMapperID() > 0)
    		{
    	        //logger.info("program pages: " + this.getCartridge().getHeader().getProgramInstructionByte());
    	        //logger.info("chr-rom pages: " + this.getCartridge().getHeader().getPatternTileByte());
    	        //logger.info("program array length: " + this.getCartridge().getProgramInstructions().length);
    	        //logger.info("pattern array length: " + this.getCartridge().getPatternTiles().length);
    		}
    		
    		// TODO: this should probably be an enum or something.
    		if (this.getCartridge().getHeader().getMapperID() == 0)
    		{
    			//logger.info("using default mapper");
    		}
    		else if (this.getCartridge().getHeader().getMapperID() == 1)
    		{
    			//logger.info("using mapper MMC1");
    			this.getCpuMemory().setMapper(new MMC1Mapper(this.getCpuMemory()));
    		}
    		else if (this.getCartridge().getHeader().getMapperID() == 2)
    		{
    			//logger.info("using mapper unrom");
    			this.getCpuMemory().setMapper(new UnromMapper(this.getCpuMemory()));
    		}    		
    		else
    		{
    			throw new IllegalStateException("cartridge utilizes currently unsupported mapper: " + this.getCartridge().getHeader().getMapperID());
    		}
    		
    		this.getPpuMemory().establishMirroring();
    		this.getCpuMemory().init(this.getCartridge().getProgramInstructions());
    		this.getPpuMemory().setPatternTiles(this.getCartridge().getPatternTiles());
    		this.getInterrupts().init();
    	}
    	else
    	{
    		// no cartridge inserted
    		throw new IllegalStateException("no cartridge inserted");
    	}
    	
    	this.getInterrupts().addInterruptRequestToQueue(new InterruptRequest(InterruptRequest.interruptTypeReset));
    	this.getInterrupts().cycle();
    }
    
    public void power()
    {
    	this.init();
    	
    	
        while (isRun())
        {
    		int cyclesPassed = 0;
    		
        	// 1. cpu cycle
        	cyclesPassed += this.getCpu().processNextInstruction();
        	
        	// 2. Execute Interrupts.
        	cyclesPassed += this.getInterrupts().cycle();
        	
        	// 3. ppu cycle
        	this.getPpu().cycle(cyclesPassed); 

        	// 4 apu cycle
        	this.getApu().cycle(cyclesPassed);
        	
        	// 5. controller cycle 
        	this.getControllerPorts().cycle();
        	
        	addToCycleCount(cyclesPassed);
        }
        
        // shutdown
    }

	public boolean isRun()
	{
		return run;
	}

	public void setRun(boolean run)
	{
		this.run = run;
	}

	public static long getCycleCount()
	{
		return cycleCount;
	}

	private static void setCycleCount(long cycleCount)
	{
		Platform.cycleCount = cycleCount;
	}
	
	private static void addToCycleCount(int cycleCount)
	{
		Platform.cycleCount += cycleCount;
	}
	
	private static void incrementCycleCount()
	{
		Platform.cycleCount++;
	}

	public NesCpu getCpu()
    {
    	return cpu;
    }

	public void setCpu(NesCpu cpu)
    {
    	this.cpu = cpu;
    }

	public NesInterrupts getInterrupts()
    {
    	return interrupts;
    }

	public void setInterrupts(NesInterrupts interrupts)
    {
    	this.interrupts = interrupts;
    }

	public NesCpuMemory getCpuMemory()
    {
    	return cpuMemory;
    }

	public void setCpuMemory(NesCpuMemory cpuMemory)
    {
    	this.cpuMemory = cpuMemory;
    }

	public Ines getCartridge()
    {
    	return cartridge;
    }

	public void setCartridge(Ines cartridge)
    {
    	this.cartridge = cartridge;
    }

	public NesPpu getPpu()
    {
    	return ppu;
    }

	public void setPpu(NesPpu ppu)
    {
    	this.ppu = ppu;
    }

	public NesPpuMemory getPpuMemory()
    {
    	return ppuMemory;
    }

	public void setPpuMemory(NesPpuMemory ppuMemory)
    {
    	this.ppuMemory = ppuMemory;
    }

	public NesApu getApu()
    {
    	return apu;
    }

	public void setApu(NesApu apu)
    {
    	this.apu = apu;
    }

	public NesControllerPorts getControllerPorts()
    {
    	return controllerPorts;
    }

	public void setControllerPorts(NesControllerPorts controllerPorts)
    {
    	this.controllerPorts = controllerPorts;
    }
}
