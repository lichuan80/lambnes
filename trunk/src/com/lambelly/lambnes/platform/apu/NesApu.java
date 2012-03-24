package com.lambelly.lambnes.platform.apu;

import com.lambelly.lambnes.platform.apu.registers.*;
import com.lambelly.lambnes.platform.ppu.NesPpuMemory;

import javax.sound.sampled.*;

import org.apache.log4j.Logger;

public class NesApu implements AudioProcessingUnit
{
	private static final int APU_SAMPLE_RATE = 44100;
	private static final int APU_CYCLES_PER_SAMPLE = 1789773 / APU_SAMPLE_RATE;
	
    private APUControlRegister apuControlRegister; // 0x4015
    private APUFrameCounterRegister apuFrameCounterRegister; // 0x4017
    private APUPulse1ChannelRegister apuPulse1ChannelRegister; // 0x4000
    private APUPulse1LengthCounterRegister apuPulse1LengthCounterRegister; // 0x4003
    private APUPulse1SweepRegister apuPulse1SweepRegister; // 0x4001
    private APUPulse1TimerLowRegister apuPulse1TimerLowRegister; // 0x4002
	
	private Logger logger = Logger.getLogger(NesApu.class);
	
	private int apuCycle = 0;
	
	public void cycle(int cpuCycleCount)
	{
		this.doRegisterReadsWrites();
		
        for (int cycle = 0; cycle < cpuCycleCount; cycle++) 
        {
            //++remainder;
            //clockdmc();
        	
            if (this.getApuCycle() % 7458 == 0) 
            {
                clockFrameCounter();
            }
            
            if ((this.getApuCycle() % NesApu.APU_CYCLES_PER_SAMPLE) < 1) 
            {
                //not quite right - there's a non-integer # cycles per sample.
                //timers[0].clock(remainder);
                //timers[1].clock(remainder);
                //if (lengthctr[2] > 0 && linearctr > 0) {
                //    timers[2].clock(remainder);
                //}
                //timers[3].clock(remainder);
                //int mixvol = getOutputLevel();
                //if (expnSound != null) {
                //    expnSound.clock(remainder);
                //}
                //remainder = 0;
                //ai.outputSample(mixvol);
            }
            
            this.incrementApuCycle();
        }
	}
	
	private void clockFrameCounter()
	{
		
	}

	public void doRegisterReadsWrites()
	{
		this.getApuControlRegister().cycle();
		this.getApuFrameCounterRegister().cycle();
		this.getApuPulse1ChannelRegister().cycle();
		this.getApuPulse1LengthCounterRegister().cycle();
		this.getApuPulse1SweepRegister().cycle();
		this.getApuPulse1TimerLowRegister().cycle();
	}

	public int getApuCycle()
    {
    	return apuCycle;
    }

	public void setApuCycle(int apuCycle)
    {
    	this.apuCycle = apuCycle;
    }
	
	public void incrementApuCycle()
	{
		++this.apuCycle;
	}

	public APUControlRegister getApuControlRegister()
    {
    	return apuControlRegister;
    }

	public void setApuControlRegister(APUControlRegister apuControlRegister)
    {
    	this.apuControlRegister = apuControlRegister;
    }

	public APUFrameCounterRegister getApuFrameCounterRegister()
    {
    	return apuFrameCounterRegister;
    }

	public void setApuFrameCounterRegister(
            APUFrameCounterRegister apuFrameCounterRegister)
    {
    	this.apuFrameCounterRegister = apuFrameCounterRegister;
    }

	public APUPulse1ChannelRegister getApuPulse1ChannelRegister()
    {
    	return apuPulse1ChannelRegister;
    }

	public void setApuPulse1ChannelRegister(
            APUPulse1ChannelRegister apuPulse1ChannelRegister)
    {
    	this.apuPulse1ChannelRegister = apuPulse1ChannelRegister;
    }

	public APUPulse1LengthCounterRegister getApuPulse1LengthCounterRegister()
    {
    	return apuPulse1LengthCounterRegister;
    }

	public void setApuPulse1LengthCounterRegister(
            APUPulse1LengthCounterRegister apuPulse1LengthCounterRegister)
    {
    	this.apuPulse1LengthCounterRegister = apuPulse1LengthCounterRegister;
    }

	public APUPulse1SweepRegister getApuPulse1SweepRegister()
    {
    	return apuPulse1SweepRegister;
    }

	public void setApuPulse1SweepRegister(
            APUPulse1SweepRegister apuPulse1SweepRegister)
    {
    	this.apuPulse1SweepRegister = apuPulse1SweepRegister;
    }

	public APUPulse1TimerLowRegister getApuPulse1TimerLowRegister()
    {
    	return apuPulse1TimerLowRegister;
    }

	public void setApuPulse1TimerLowRegister(
            APUPulse1TimerLowRegister apuPulse1TimerLowRegister)
    {
    	this.apuPulse1TimerLowRegister = apuPulse1TimerLowRegister;
    }
}
