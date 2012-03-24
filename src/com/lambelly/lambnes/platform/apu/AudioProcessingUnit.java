package com.lambelly.lambnes.platform.apu;

import com.lambelly.lambnes.platform.apu.registers.APUControlRegister;
import com.lambelly.lambnes.platform.apu.registers.APUPulse1ChannelRegister;
import com.lambelly.lambnes.platform.apu.registers.APUPulse1LengthCounterRegister;
import com.lambelly.lambnes.platform.apu.registers.APUPulse1SweepRegister;
import com.lambelly.lambnes.platform.apu.registers.APUPulse1TimerLowRegister;
import com.lambelly.lambnes.platform.apu.registers.APUFrameCounterRegister;

public interface AudioProcessingUnit
{
	public void cycle(int cpuCycleCount);

}
