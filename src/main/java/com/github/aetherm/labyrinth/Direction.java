package com.github.aetherm.labyrinth;

public enum Direction
{
	NORTH, EAST, SOUTH, WEST;
	
	
	private Direction opposite;
	static
	{
        NORTH.opposite = SOUTH;
        SOUTH.opposite = NORTH;
        EAST.opposite = WEST;
        WEST.opposite = EAST;
    }
	public Direction getOpposite()
	{
		 return opposite;
	}
}
