package com.lambelly.lambnes.platform.cpu;

public class NesFlags implements Flags
{
    private boolean negative = false;
    private boolean overflow = false; // signed overflow
    private boolean brkCommand = false;
    private boolean decimalMode = false;
    private boolean irqDisable = false;
    private boolean zero = false;
    private boolean carry = false; // unsigned overflow

    public void resetFlags()
    {
    	this.setNegative(false);
    	this.setOverflow(false);
    	this.setBrkCommand(false);
    	this.setDecimalMode(false);
    	this.setIrqDisable(false);
    	this.setZero(false);
    	this.setCarry(false);
    }
    
    public String toString()
    {
    	return "negative: " + this.isNegative() + "\n" +
    		"overflow: " + this.isOverflow() + "\n" +
    		"brkCommand: " + this.isBrkCommand() + "\n" +
    		"decimalMode: " + this.isDecimalMode() + "\n" +
    		"irqDisable: " + this.isIrqDisable() + "\n" +
    		"zero: " + this.isZero() + "\n" +
    		"carry: " + this.isCarry();
    }
    
    /**
     * @return the negative
     */
    public boolean isNegative()
    {
        return negative;
    }

    /**
     * @param negative the negative to set
     */
    public void setNegative(boolean negative)
    {
        this.negative = negative;
    }

    /**
     * @return the overflow
     */
    public boolean isOverflow()
    {
        return overflow;
    }

    /**
     * @param overflow the overflow to set
     */
    public void setOverflow(boolean overflow)
    {
        this.overflow = overflow;
    }

    /**
     * @return the brkCommand
     */
    public boolean isBrkCommand()
    {
        return brkCommand;
    }

    /**
     * @param brkCommand the brkCommand to set
     */
    public void setBrkCommand(boolean brkCommand)
    {
        this.brkCommand = brkCommand;
    }

    /**
     * @return the decimalMode
     */
    public boolean isDecimalMode()
    {
        return decimalMode;
    }

    /**
     * @param decimalMode the decimalMode to set
     */
    public void setDecimalMode(boolean decimalMode)
    {
        this.decimalMode = decimalMode;
    }

    /**
     * @return the irqDisable
     */
    public boolean isIrqDisable()
    {
        return irqDisable;
    }

    /**
     * @param irqDisable the irqDisable to set
     */
    public void setIrqDisable(boolean irqDisable)
    {
        this.irqDisable = irqDisable;
    }

    /**
     * @return the zero
     */
    public boolean isZero()
    {
        return zero;
    }

    /**
     * @param zero the zero to set
     */
    public void setZero(boolean zero)
    {
        this.zero = zero;
    }

    /**
     * @return the carry
     */
    public boolean isCarry()
    {
        return carry;
    }

    /**
     * @param carry the carry to set
     */
    public void setCarry(boolean carry)
    {
        this.carry = carry;
    }    
}
