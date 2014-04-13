package edu.virginia.cs.runner;

import java.util.ArrayList;
import java.util.List;

public class SpikeSystem {

	private List<SpikeObject> spikeList;
	
	public SpikeSystem()
	{
		spikeList = new ArrayList<SpikeObject>();
	}
	
	public void addSpikes(int startX, int startY, int number, float time)
	{
		int spikeType = (int)(Math.random() * 2);
		for (int i = 0; i < number; i++)
		{
			SpikeObject spike = new SpikeObject(i * 32 + startX, startY, 32, 24);
			spike.setShowTime(time);
			if (spikeType == 0)
				spike.setTextureName("spike");
			else
				spike.setTextureName("spike2");
			spikeList.add(spike);
		}
	}
	
	public List<SpikeObject> getSpikeList()
	{
		return spikeList;
	}
}
