package com.lambelly.lambnes.gui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import com.lambelly.lambnes.platform.PaletteColor;
import com.lambelly.lambnes.platform.Platform;

public class BackgroundPaletteLabel extends JLabel
{
	private int backgroundPaletteColorNumber = 0;
	
	public BackgroundPaletteLabel(int backgroundPaletteColorNumber)
	{
		this.setBackgroundPaletteColorNumber(backgroundPaletteColorNumber);
		this.setHorizontalTextPosition(JLabel.CENTER);
		this.setVerticalTextPosition(JLabel.BOTTOM);
		this.setText(Integer.toString(backgroundPaletteColorNumber));
		this.setOpaque(true);
		this.setBorder(LineBorder.createGrayLineBorder());
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		int palcol = Platform.getPpuMemory().getImagePalette()[this.getBackgroundPaletteColorNumber()];
		PaletteColor pc = Platform.getMasterPalette().getColor(palcol);
		this.setToolTipText(palcol + ": r" + pc.getRed() + "b" + pc.getBlue() + "g" + pc.getGreen());
		this.setBackground(new Color(pc.getColorInt()));
	}

	public int getBackgroundPaletteColorNumber()
	{
		return backgroundPaletteColorNumber;
	}

	public void setBackgroundPaletteColorNumber(int spritePaletteColorNumber)
	{
		this.backgroundPaletteColorNumber = spritePaletteColorNumber;
	}
}
