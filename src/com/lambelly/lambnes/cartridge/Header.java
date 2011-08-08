/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.lambelly.lambnes.cartridge;

import com.lambelly.lambnes.util.*;

/**
 *
 * @author thomasmccarthy
 */
public class Header
{
    private boolean Nes = false;
    private int programInstructionByte = 0;
    private int patternTileByte = 0;
    private int mapperID = 0;
    private boolean horizontalMirroring = false;
	private boolean verticalMirroring = false;
    private boolean sramEnabled = false;
    private boolean trainerPresent = false;
    private boolean fourScreenMirroring = false;
    
    public Header(int[] rawData)
    {
        this.setNes(true);
        this.setProgramInstructionByte(rawData[4]);
        this.setPatternTileByte(rawData[5]);
        this.parseControlByte1(rawData[6]);
        this.setMapperID(this.parseMapper(rawData[6], rawData[7]));
    }
    
    private void parseControlByte1(int control)
    {
    	if (BitUtils.isBitSet(control, 0))
    	{
    		this.setVerticalMirroring(true);
    	}
    	else
    	{
    		this.setHorizontalMirroring(true);
    	}
    	
    	this.setSramEnabled(BitUtils.isBitSet(control, 1));
    	this.setTrainerPresent(BitUtils.isBitSet(control, 2));
    	this.setFourScreenMirroring(BitUtils.isBitSet(control, 3));
    }

    private int parseMapper(int controlBit1, int controlBit2)
    {
    	return (((controlBit1 & 0xF0) >> 4) | (controlBit2 & 0xF0));
    }
    
    /**
     * @return the Nes
     */
    public boolean isNes()
    {
        return Nes;
    }

    /**
     * @param Nes the Nes to set
     */
    public void setNes(boolean Nes)
    {
        this.Nes = Nes;
    }

    /**
     * @return the programInstructionByte
     */
    public int getProgramInstructionByte()
    {
        return programInstructionByte;
    }

    /**
     * @param programInstructionByte the programInstructionByte to set
     */
    public void setProgramInstructionByte(int programInstructionByte)
    {
        this.programInstructionByte = programInstructionByte;
    }

    /**
     * @return the patternTileByte
     */
    public int getPatternTileByte()
    {
        return patternTileByte;
    }

    /**
     * @param patternTileByte the patternTileByte to set
     */
    public void setPatternTileByte(int patternTileByte)
    {
        this.patternTileByte = patternTileByte;
    }
    
    public boolean isHorizontalMirroring()
	{
		return horizontalMirroring;
	}

	public void setHorizontalMirroring(boolean horizontalMirroring)
	{
		this.horizontalMirroring = horizontalMirroring;
	}

	public boolean isVerticalMirroring()
	{
		return verticalMirroring;
	}

	public void setVerticalMirroring(boolean verticalMirroring)
	{
		this.verticalMirroring = verticalMirroring;
	}

	public boolean isSramEnabled()
	{
		return sramEnabled;
	}

	public void setSramEnabled(boolean sramEnabled)
	{
		this.sramEnabled = sramEnabled;
	}

	public boolean isTrainerPresent()
	{
		return trainerPresent;
	}

	public void setTrainerPresent(boolean trainerPresent)
	{
		this.trainerPresent = trainerPresent;
	}

	public boolean isFourScreenMirroring()
	{
		return fourScreenMirroring;
	}

	public void setFourScreenMirroring(boolean fourScreenMirroring)
	{
		this.fourScreenMirroring = fourScreenMirroring;
	}

	public int getMapperID()
	{
		return mapperID;
	}

	public void setMapperID(int mapperID)
	{
		this.mapperID = mapperID;
	}    
}
