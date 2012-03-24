package com.lambelly.lambnes.platform.apu;

import com.lambelly.lambnes.platform.Platform;
import com.lambelly.lambnes.platform.apu.registers.APUFrameCounterRegister;

public class NesApuFrameSequencer
{
	public static final int CYCLES_PER_FRAME_COUNTER_CLOCK = 3728;
	private int stepCounter = 0;
	private int lastClock = 0;
	private APUFrameCounterRegister apuFrameCounterRegister;
	
	private void NesApuFrameSequencer()
	{
		
	}
	
	public void cycle(int apuCycleCount)
	{
		// check clock
		if (apuCycleCount - this.getLastClock() == NesApuFrameSequencer.CYCLES_PER_FRAME_COUNTER_CLOCK)
		{
			if (this.getApuFrameCounterRegister().getSequencerMode() == APUFrameCounterRegister.SEQUENCER_MODE_4_STEP_SEQUENCE)
			{
				//increment step
				this.incrementStepCounter();
				
				// set lastClock
				this.setLastClock(apuCycleCount);
				
				if (this.getApuFrameCounterRegister().getSequencerMode() == APUFrameCounterRegister.SEQUENCER_MODE_4_STEP_SEQUENCE)
				{
					// quarter frame
					if (this.getStepCounter() >= 1 && this.getStepCounter() <= 4)
					{
						
					}
					
					// half frame
					if (this.getStepCounter() == 2 || this.getStepCounter() == 4)
					{
						
					}

					// TODO -- interrupt logic
					// interrupt
				}
				else
				{
					// quarter frame
					if (this.getStepCounter() == 1 || this.getStepCounter() == 2 || this.getStepCounter() == 3 || this.getStepCounter() == 5)
					{
						
					}
					
					// half frame
					if (this.getStepCounter() == 2 || this.getStepCounter() == 5)
					{
						
					}
				}
			}
		}
	}	

	public int getStepCounter()
    {
    	return stepCounter;
    }

	public void setStepCounter(int stepCounter)
    {
    	this.stepCounter = stepCounter;
    }
	
	public void incrementStepCounter()
	{
		++stepCounter;
	}
	
	public void resetStepCounter()
	{
		stepCounter = 0;
	}

	public int getLastClock()
    {
    	return lastClock;
    }

	public void setLastClock(int lastClock)
    {
    	this.lastClock = lastClock;
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
}
