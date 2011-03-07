package com.lambelly.lambnes.platform.cpu;

public interface Flags
{
	public void resetFlags();
	public boolean isIrqDisable();
	public void setIrqDisable(boolean irqDisable);
	public void setCarry(boolean carry);
}
