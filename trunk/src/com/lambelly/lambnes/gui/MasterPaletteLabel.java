package com.lambelly.lambnes.gui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import com.lambelly.lambnes.platform.Platform;

public class MasterPaletteLabel extends JLabel
{
	private int masterPaletteColorNumber = 0;
	
	public MasterPaletteLabel(int masterPaletteColorNumber)
	{
		this.setMasterPaletteColorNumber(masterPaletteColorNumber);
		this.setHorizontalTextPosition(JLabel.CENTER);
		this.setVerticalTextPosition(JLabel.BOTTOM);
		this.setText(Integer.toString(masterPaletteColorNumber));
		this.setOpaque(true);
		this.setBackground(new Color(Platform.getMasterPalette().getColor(this.getMasterPaletteColorNumber()).getColorInt()));
		this.setBorder(LineBorder.createGrayLineBorder());
	}

	public int getMasterPaletteColorNumber()
	{
		return masterPaletteColorNumber;
	}

	public void setMasterPaletteColorNumber(int masterPaletteColorNumber)
	{
		this.masterPaletteColorNumber = masterPaletteColorNumber;
	}
}
