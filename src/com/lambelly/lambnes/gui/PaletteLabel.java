package com.lambelly.lambnes.gui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import com.lambelly.lambnes.platform.*;

public class PaletteLabel extends JLabel
{
	private int paletteMemoryAddress = 0;
	
	public PaletteLabel(int paletteMemoryAddress)
	{
		this.setPaletteMemoryAddress(paletteMemoryAddress);
		this.setHorizontalTextPosition(JLabel.CENTER);
		this.setVerticalTextPosition(JLabel.BOTTOM);
		//this.setText(Integer.toString(spritePaletteColorNumber)); 
		this.setOpaque(true);
		this.setBorder(LineBorder.createGrayLineBorder());
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		int palcol = Platform.getPpuMemory().getMemoryFromHexAddress(this.getPaletteMemoryAddress());
		PaletteColor pc = Platform.getMasterPalette().getColor(palcol);
		this.setToolTipText(palcol + ": r" + pc.getRed() + "b" + pc.getBlue() + "g" + pc.getGreen());
		this.setBackground(new Color(pc.getColorInt()));
	}

	public int getPaletteMemoryAddress()
	{
		return paletteMemoryAddress;
	}

	public void setPaletteMemoryAddress(int paletteMemoryAddress)
	{
		this.paletteMemoryAddress = paletteMemoryAddress;
	}


}
