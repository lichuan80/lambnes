package com.lambelly.lambnes.gui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import com.lambelly.lambnes.platform.*;

public class SpritePaletteLabel extends JLabel
{
	private int spritePaletteColorNumber = 0;
	
	public SpritePaletteLabel(int spritePaletteColorNumber)
	{
		this.setSpritePaletteColorNumber(spritePaletteColorNumber);
		this.setHorizontalTextPosition(JLabel.CENTER);
		this.setVerticalTextPosition(JLabel.BOTTOM);
		this.setText(Integer.toString(spritePaletteColorNumber)); 
		this.setOpaque(true);
		this.setBorder(LineBorder.createGrayLineBorder());
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		int palcol = Platform.getPpuMemory().getSpritePalette()[this.getSpritePaletteColorNumber()];
		PaletteColor pc = Platform.getMasterPalette().getColor(palcol);
		this.setToolTipText(palcol + ": r" + pc.getRed() + "b" + pc.getBlue() + "g" + pc.getGreen());
		this.setBackground(new Color(pc.getColorInt()));
	}

	public int getSpritePaletteColorNumber()
	{
		return spritePaletteColorNumber;
	}

	public void setSpritePaletteColorNumber(int spritePaletteColorNumber)
	{
		this.spritePaletteColorNumber = spritePaletteColorNumber;
	}
}
