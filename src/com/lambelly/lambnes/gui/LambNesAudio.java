package com.lambelly.lambnes.gui;

import javax.sound.sampled.*;
import org.apache.log4j.*;

public class LambNesAudio
{
	private Logger logger = Logger.getLogger(LambNesAudio.class);
	private boolean soundEnabled = false;
	private SourceDataLine sourceDataLine = null;
	private LambNesAudioBuffer audioDataBuffer = null;
	
	public LambNesAudio(int sampleRate)
	{
		this.setAudioDataBuffer(new LambNesAudioBuffer(sampleRate));
        AudioFormat format = new AudioFormat(
        	sampleRate,
            16,//bit
            1,//channel
            true,//signed
            false //little endian
        );
        
        int samplesPerFrame = (int) Math.ceil((sampleRate * 2) / 60.);
        
        try
        {
        	this.setSourceDataLine(AudioSystem.getSourceDataLine(format));
        	this.getSourceDataLine().open(format, samplesPerFrame * 8); //create 4 frame audio buffer
        	this.getSourceDataLine().start();
        }
        catch(Exception e)
        {
        	logger.error("unable to open audio source data line");
        }
	}
	
	public void outputFrame()
	{
		if (this.isSoundEnabled())
		{
			this.getSourceDataLine().write(this.getAudioDataBuffer().getBuffer(), 0, this.getAudioDataBuffer().getBufferPointer());
		}
		
		this.getAudioDataBuffer().reset();
	}

	public boolean isSoundEnabled()
    {
    	return soundEnabled;
    }

	public void setSoundEnabled(boolean soundEnabled)
    {
    	this.soundEnabled = soundEnabled;
    }

	public SourceDataLine getSourceDataLine()
    {
    	return sourceDataLine;
    }

	public void setSourceDataLine(SourceDataLine sourceDataLine)
    {
    	this.sourceDataLine = sourceDataLine;
    }

	public LambNesAudioBuffer getAudioDataBuffer()
    {
    	return audioDataBuffer;
    }

	public void setAudioDataBuffer(LambNesAudioBuffer audioDataBuffer)
    {
    	this.audioDataBuffer = audioDataBuffer;
    }
	
}
