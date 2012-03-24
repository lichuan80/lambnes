package com.lambelly.lambnes.gui;

public class LambNesAudioBuffer
{
	private byte[] buffer;
	private int bufferPointer = 0;
	
	public LambNesAudioBuffer(int sampleRate)
	{
		this.setBuffer(new byte[sampleRate]);
	}

	public void reset()
	{
		this.setBufferPointer(0);
	}
	
	public void addDataToBuffer(byte data)
	{
		this.buffer[this.getBufferPointer()] = data;
		this.incrementBufferPointer();
	}
	
	public byte[] getBuffer()
    {
    	return buffer;
    }

	public void setBuffer(byte[] buffer)
    {
    	this.buffer = buffer;
    }

	private void incrementBufferPointer()
	{
		bufferPointer++;
	}
	
	public int getBufferPointer()
    {
    	return bufferPointer;
    }

	public void setBufferPointer(int bufferPointer)
    {
    	this.bufferPointer = bufferPointer;
    }
}
