package com.lambelly.lambnes.platform.interrupts;

import org.apache.log4j.*;

import java.util.Vector;
import com.lambelly.lambnes.platform.cpu.NesCpu;
import com.lambelly.lambnes.platform.cpu.NesCpuMemory;
import com.lambelly.lambnes.util.BitUtils;
import com.lambelly.lambnes.util.NumberConversionUtils;

public class NesInterrupts
{
	final static int NMI_INTERRUPT_ADDRESS = 0xFFFA;
	final static int RESET_INTERRUPT_ADDRESS = 0xFFFC;
	final static int IRQ_BRK_INTERRUPT_ADDRESS = 0xFFFE;
	private static int nmiInterruptJumpAddress = 0;
	private static int resetInterruptJumpAddress = 0;
	private static int irqBrkInterruptJumpAddress = 0;
	private NesCpu cpu;
	private NesCpuMemory cpuMemory;
	
	private Vector<InterruptRequest> interruptRequestQueue = new Vector<InterruptRequest>(); 

	private Logger logger = Logger.getLogger(NesInterrupts.class);
	
	public NesInterrupts()
	{	
		
	}
	
	public void init()
	{
		// set NMI jump address
		setNmiInterruptJumpAddress(this.getJumpAddress(NesInterrupts.NMI_INTERRUPT_ADDRESS));
		
		// set irq jump address
		setIrqBrkInterruptJumpAddress(this.getJumpAddress(NesInterrupts.IRQ_BRK_INTERRUPT_ADDRESS));
		
		// set reset jump address
		setResetInterruptJumpAddress(this.getJumpAddress(NesInterrupts.RESET_INTERRUPT_ADDRESS));
	}
	
	public int cycle()
	{
		int cyclesPassed = 0;
		
		// check for interrupts
		InterruptRequest ir = null;
		ir = this.getNextInterruptRequest();
		if (ir != null)
		{
			// push the program counter and status register on to the stack
        	int a[] = BitUtils.splitAddress(this.getCpuMemory().getProgramCounter());
        	this.getCpuMemory().pushStack(a[1]);
        	this.getCpuMemory().pushStack(a[0]);
        	this.getCpu().pushStatus(false);
        	
			// set the interrupt disable flag
        	this.getCpu().getFlags().setIrqDisable(true);
			
			// check NMI interrupt
			if (ir.getInterruptType() == InterruptRequest.interruptTypeNMI)
			{
				// logger.debug("NMI Interrupt: leaving " + Integer.toHexString(this.getCpuMemory().getProgramCounter()) + " and going to " + Integer.toHexString(this.getNmiInterruptJumpAddress()));
				// execute the interrupt handling routine
				this.getCpuMemory().setProgramCounter(getNmiInterruptJumpAddress());
			}

			// check irq interrupt
			if (ir.getInterruptType() == InterruptRequest.interruptTypeIRQ)
			{
				// logger.debug("IRQ interrupt");
			}
			
			// check reset interrupt			
			if (ir.getInterruptType() == InterruptRequest.interruptTypeReset)
			{
				// logger.debug("RESET Interrupt: " + Integer.toHexString(getResetInterruptJumpAddress()));
				// execute the interrupt handling routine
				this.getCpuMemory().setProgramCounter(getResetInterruptJumpAddress());
			}
		}
		
		return cyclesPassed;
	}
	
	private int getJumpAddress(int baseAddress)
	{
		// logger.debug("baseAddress: " + Integer.toHexString(baseAddress));

		int lowerByte = this.getCpuMemory().getMemoryFromHexAddress(baseAddress);
		int higherByte = this.getCpuMemory().getMemoryFromHexAddress(baseAddress + 1);
		// logger.debug("lb: " + lowerByte);
		// logger.debug("hb: " + higherByte);
		String hs = Integer.toHexString(higherByte) + NumberConversionUtils.generateHexStringWithleadingZeros(lowerByte,2);
		// logger.debug("interrupt address: " + hs);
		return Integer.parseInt(hs,16);
	}

	public static int getNmiInterruptJumpAddress()
	{
		return nmiInterruptJumpAddress;
	}

	public static void setNmiInterruptJumpAddress(int nmiInterruptJumpAddress)
	{
		NesInterrupts.nmiInterruptJumpAddress = nmiInterruptJumpAddress;
	}

	public static int getResetInterruptJumpAddress()
	{
		return resetInterruptJumpAddress;
	}

	public static void setResetInterruptJumpAddress(int resetInterruptJumpAddress)
	{
		NesInterrupts.resetInterruptJumpAddress = resetInterruptJumpAddress;
	}

	public static int getIrqBrkInterruptJumpAddress()
	{
		return irqBrkInterruptJumpAddress;
	}

	public static void setIrqBrkInterruptJumpAddress(int irqBrkInterruptJumpAddress)
	{
		NesInterrupts.irqBrkInterruptJumpAddress = irqBrkInterruptJumpAddress;
	}

	public Vector<InterruptRequest> getInterruptRequestQueue()
	{
		return interruptRequestQueue;
	}

	public void setInterruptRequestQueue(
			Vector<InterruptRequest> interruptRequestQueue)
	{
		this.interruptRequestQueue = interruptRequestQueue;
	}
	
	public void addInterruptRequestToQueue(InterruptRequest interruptRequest)
	{
		this.interruptRequestQueue.add(interruptRequest);
	}
	
	private InterruptRequest getNextInterruptRequest()
	{
		InterruptRequest ir = null;
		if (this.getInterruptRequestQueue().size() > 0)
		{
			ir = this.getInterruptRequestQueue().get(0);
			this.getInterruptRequestQueue().remove(0);
		}
		
		return ir;
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
	
}
