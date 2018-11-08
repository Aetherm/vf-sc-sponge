package com.github.aetherm.labyrinth;

import java.util.ArrayList;

public interface MapObject
{	
	/**
	 * Gets all stored positions of the MapObject 
	 * 
	 * @return stored positions
	 */
	public default ArrayList<Integer> getTiles()
	{
		return null;
	}
}
