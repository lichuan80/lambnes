package com.lambelly.lambnes.gui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import com.lambelly.lambnes.platform.NesMasterColor;

public class PaletteLabel extends JLabel
{
	private NesMasterColor backgroundColor = null;
	
	public PaletteLabel(NesMasterColor color)
	{
		this.setBackgroundColor(color);
		this.setHorizontalTextPosition(JLabel.CENTER);
		this.setVerticalTextPosition(JLabel.BOTTOM);
		//this.setText(Integer.toString(spritePaletteColorNumber)); 
		this.setOpaque(true);
		this.setBorder(LineBorder.createGrayLineBorder());
		this.setToolTipText("r" + this.getBackgroundColor().getRed() + "b" + this.getBackgroundColor().getBlue() + "g" + this.getBackgroundColor().getGreen());
		this.setBackground(new Color(this.getBackgroundColor().getColorInt()));
	}
	
	public void refreshBackground(NesMasterColor color)
	{
		this.setBackgroundColor(color);
		this.setToolTipText("r" + this.getBackgroundColor().getRed() + "b" + this.getBackgroundColor().getBlue() + "g" + this.getBackgroundColor().getGreen());
		this.setBackground(new Color(this.getBackgroundColor().getColorInt()));
	}

	public NesMasterColor getBackgroundColor()
	{
		return backgroundColor;
	}

	public void setBackgroundColor(NesMasterColor color)
	{
		backgroundColor = color;
	}
}
