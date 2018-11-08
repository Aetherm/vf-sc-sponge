package com.github.aetherm.labyrinth;

import java.util.ArrayList;

/**
 * Contains methods to connect {@link Cell}s with others.
 * All known will be taken from an instance of {@link mapBuilder}.
 * Holds created {@link Corridor}s that constitute the connections
 * 
 */

public class Connector
{
	private final ArrayList<Cell> chamberCells;
	private final ArrayList<Cell> connectionCells;
	
	private ArrayList<Corridor> corridors = new ArrayList<Corridor>();
	
	private final MapBuilder mapBuilder;
	
	/**
	 * Create a new {@link Connector} that sets {@link Cell}s to connect them. 
	 * 
	 * @param mapBuilder
	 * the instance that holds the information
	 */
	protected Connector(MapBuilder mapBuilder)
	{
		this.mapBuilder = mapBuilder;
		
		this.chamberCells = mapBuilder.getChamberCells();
		this.connectionCells = mapBuilder.getConnectionCells();
	}

	/**
	 * Connects two cells by creating a {@link Corridor} instance
	 * 
	 * @param startCell
	 * the cell where the corridor will start
	 * @param endCell
	 * the cell where the corridor will end
	 * @return
	 * true if successful, false if not
	 */
	protected boolean connect(Cell startCell, Cell endCell)
	{	
		boolean success = false;
		int factorX = startCell.getGridX() - endCell.getGridX();
		int factorY = startCell.getGridY() - endCell.getGridY();
		
		if(factorX == 0 && factorY != 0)
		{
				if(factorY > 0)
				{
					connectToNorthRow(startCell, factorY);
				}
				else
				{
					connectToSouthRow(startCell, factorY);
				}
				
				success = true;
		}
		
		if(factorX != 0 && factorY == 0)
		{
				if(factorX > 0)
				{
					connectToWestRow(startCell, factorX);
				}
				else
				{
					connectToEastRow(startCell, factorX);
				}
				
				success = true;
		}
		
		if(factorX != 0 && factorY != 0)
		{
			if(factorX > 0)
			{
				connectToWestRow(startCell, factorX);
			}
			else
			{
				connectToEastRow(startCell, factorX);
			}
			
			//direction inverted, since starting from endCell
			if(factorY > 0)
			{
				connectToSouthRow(endCell, factorY);
			}
			else
			{
				connectToNorthRow(endCell, factorY);
			}
		}
		
		return success;
	}
	
	protected void connectToNorthRow(Cell startCell, int factor)
	{
		Corridor corridor;
		
		factor = Math.abs(factor);
		
		for(int i = 0; i < factor; i++)
		{
			corridor = new Corridor(startCell, Direction.NORTH, mapBuilder);
			
			startCell = corridor.getEndCell();
			
			corridors.add(corridor);
		}
	}
	
	protected void connectToEastRow(Cell startCell, int factor)
	{
		Corridor corridor;
		
		factor = Math.abs(factor);
		
		for(int i = 0; i < factor; i++)
		{
			corridor = new Corridor(startCell, Direction.EAST, mapBuilder);
			
			startCell = corridor.getEndCell();
			
			corridors.add(corridor);
		}
	}
	
	protected void connectToSouthRow(Cell startCell, int factor)
	{
		Corridor corridor;
		
		factor = Math.abs(factor);
		
		for(int i = 0; i < factor; i++)
		{
			corridor = new Corridor(startCell, Direction.SOUTH, mapBuilder);
			
			startCell = corridor.getEndCell();
			
			corridors.add(corridor);
		}
	}
	
	protected void connectToWestRow(Cell startCell, int factor)
	{
		Corridor corridor;
		
		factor = Math.abs(factor);
		
		for(int i = 0; i < factor; i++)
		{
			corridor = new Corridor(startCell, Direction.WEST, mapBuilder);
			
			startCell = corridor.getEndCell();
			
			corridors.add(corridor);
		}
	}
	
	protected void connectUsedCellExits()
	{
		for(int i = 0; i < connectionCells.size(); i++)
		{
			connectionCells.get(i).connectUsedExits();
		}
		
		for(int i = 0; i < chamberCells.size(); i++)
		{
			chamberCells.get(i).connectUsedExits();
		}
	}
	
	protected Corridor getCorridorByTile(int tile)
	{
		Corridor corridor;
		
		for(int i = 0; i < corridors.size(); i++)
		{
			corridor = corridors.get(i);
			
			if(corridor.getTiles().contains(tile))
			{
				return corridor;
			}
		}
		return null;
	}
	
	/**
	 * Returns all {@link Corridor} instances created by the class.
	 * 
	 * @return all created {@link Corridor} instances
	 */
	protected ArrayList<Corridor> getCorridors()
	{
		return corridors;
	}
}
