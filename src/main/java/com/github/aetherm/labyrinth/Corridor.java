package com.github.aetherm.labyrinth;

import java.util.ArrayList;
import java.util.Random;

/**
 * Contains informations like the {@link Cell}s that are connected through it and the contained tiles.
 * 
 */
public class Corridor implements MapObject
{
	private final Cell startCell;
	private final Cell endCell;
	
	private int positionStart;
	private int positionEnd;
	
	private final MapBuilder mapBuilder;
	
	private ArrayList<Integer> corridor = new ArrayList<Integer>(); 
	
	private Random random;
	
	/** 
	 * Create a new {@link Corridor} between two {@link Cell} instances and saves them in local variables
	 * 
	 * @param startCell - The starting cell that is connected to the corridor.
	 * @param direction - The way the path will be aligned.
	 * @param mapBuilder - Providing informations about the map.
	 */
	protected Corridor(Cell startCell, Direction direction, MapBuilder mapBuilder)
	{
		this.startCell = startCell;
		this.mapBuilder = mapBuilder;
		this.random = mapBuilder.getSeededRandom();
		
		switch(direction)
		{
		case NORTH:
			endCell = mapBuilder.getSpecificCell(startCell.getGridX(), startCell.getGridY()-1);
			connectToNorth();
			break;
		case EAST:
			endCell = mapBuilder.getSpecificCell(startCell.getGridX()+1, startCell.getGridY());
			connectToEast();
			break;
		case SOUTH:
			endCell = mapBuilder.getSpecificCell(startCell.getGridX(), startCell.getGridY()+1);
			connectToSouth();
			break;
		case WEST:
			endCell = mapBuilder.getSpecificCell(startCell.getGridX()-1, startCell.getGridY());
			connectToWest();
			break;
		default:
			this.endCell = null;
			break;
		}
		
		startCell.markExitAsUsed(positionStart, direction);
		endCell.markExitAsUsed(positionEnd, direction.getOpposite());
	}
	
	private void connectToNorth()
	{	
		positionStart = startCell.getExitsNorth().get(random.nextInt(startCell.getExitsNorth().size()));
		positionEnd = endCell.getExitsSouth().get(random.nextInt(endCell.getExitsSouth().size()));
		
		int distance = MapTools.yValue(positionStart, mapBuilder.getMapWidth()) - MapTools.yValue(positionEnd, mapBuilder.getMapWidth());
		
		int correctionOffset = 1;
		int correctionFactor = MapTools.xValue(positionStart, mapBuilder.getMapWidth()) - MapTools.xValue(positionEnd, mapBuilder.getMapWidth());
		
		correctionOffset *= (correctionFactor >= 0) ? -1 : 1;
		
		int finalFactor = 0;
		
		for(int i = 1; i < distance; i++)
		{
			if(i == 2)
			{
				for(int j = 0; j <= Math.abs(correctionFactor); j++)
				{
					corridor.add((positionStart - mapBuilder.getMapWidth() * i) + correctionOffset*j);
				}
				finalFactor = Math.abs(correctionFactor) * correctionOffset;
			}
			else
			{
				corridor.add((positionStart - mapBuilder.getMapWidth() * i) + finalFactor);
			}
		}
	}
	
	private void connectToEast()
	{	
		positionStart = startCell.getExitsEast().get(random.nextInt(startCell.getExitsEast().size()));
		positionEnd = endCell.getExitsWest().get(random.nextInt(endCell.getExitsWest().size()));
		
		int distance = MapTools.xValue(positionEnd, mapBuilder.getMapWidth()) - MapTools.xValue(positionStart, mapBuilder.getMapWidth());
		
		int correctionOffset = mapBuilder.getMapWidth();
		int correctionFactor = MapTools.yValue(positionStart, mapBuilder.getMapWidth()) - MapTools.yValue(positionEnd, mapBuilder.getMapWidth());
		
		correctionOffset *= (correctionFactor >= 0) ? -1 : 1;
		correctionFactor *= (correctionFactor >= 0) ? -1 : 1;
		
		int finalFactor = 0;
		
		for(int i = 1; i < distance ; i++)
		{
			if(i == 2)
			{
				for(int j = 0; j <= Math.abs(correctionFactor); j++)
				{
					corridor.add((positionStart + i) + (correctionOffset*j));
				}
				finalFactor = Math.abs(correctionFactor) * correctionOffset;
				
			}
			else
			{
				corridor.add((positionStart + i) + (finalFactor));
			}
		}

	}
	
	private void connectToSouth()
	{
		positionStart = startCell.getExitsSouth().get(random.nextInt(startCell.getExitsSouth().size()));
		positionEnd = endCell.getExitsNorth().get(random.nextInt(endCell.getExitsNorth().size()));
		
		int distance = MapTools.yValue(positionEnd, mapBuilder.getMapWidth()) - MapTools.yValue(positionStart, mapBuilder.getMapWidth());
		
		int correctionOffset = 1;
		int correctionFactor = MapTools.xValue(positionStart, mapBuilder.getMapWidth()) - MapTools.xValue(positionEnd, mapBuilder.getMapWidth());
		
		correctionOffset *= (correctionFactor >= 0) ? -1 : 1;
		
		int finalFactor = 0;
		
		for(int i = 1; i < distance; i++)
		{
			if(i == 2)
			{
				for(int j = 0; j <= Math.abs(correctionFactor); j++)
				{
					corridor.add((positionStart + mapBuilder.getMapWidth()*i) + correctionOffset*j);
				}
				finalFactor = Math.abs(correctionFactor) * correctionOffset;
			}
			else
			{
				corridor.add((positionStart + mapBuilder.getMapWidth()*i) + (finalFactor));
			}
		}
	}
	
	private void connectToWest()
	{	
		positionStart = startCell.getExitsWest().get(random.nextInt(startCell.getExitsWest().size()));
		positionEnd = endCell.getExitsEast().get(random.nextInt(endCell.getExitsEast().size()));
		
		int distance = MapTools.xValue(positionStart, mapBuilder.getMapWidth()) - MapTools.xValue(positionEnd, mapBuilder.getMapWidth());
		
		int correctionOffset = mapBuilder.getMapWidth();
		int correctionFactor = MapTools.yValue(positionStart, mapBuilder.getMapWidth()) - MapTools.yValue(positionEnd, mapBuilder.getMapWidth());
		
		correctionOffset *= (correctionFactor >= 0) ? -1 : 1;
		
		int finalFactor = 0;
		
		for(int i = 1; i < distance; i++)
		{
			if(i == 2)
			{
				for(int j = 0; j <= Math.abs(correctionFactor); j++)
				{
					corridor.add((positionStart-i) + (correctionOffset*j));
				}
				finalFactor = Math.abs(correctionFactor) * correctionOffset;
			}
			else
			{
				corridor.add((positionStart-i) + (finalFactor));
			}
		}
	}
	
	/**
	 * Gets all stored positions of the corridor.
	 * 
	 * @return stored positions
	 */
	@Override
	public ArrayList<Integer> getTiles()
	{
		return corridor;
	}
	
	/**
	 * Returns the start {@link Cell} of the {@link Corridor}.
	 * 
	 * @return
	 * The start Cell
	 */
	protected Cell getStartCell()
	{
		return startCell;
	}
	
	/**
	 * Returns the end {@link Cell} of the {@link Corridor}.
	 * 
	 * @return
	 * The end Cell
	 */
	protected Cell getEndCell()
	{
		return endCell;
	}
	
	protected int getPositionStart()
	{
		return positionStart;
	}
	
	protected int getPositionEnd()
	{
		return positionEnd;
	}
	
}
