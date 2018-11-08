package com.github.aetherm.labyrinth;

import static com.github.aetherm.labyrinth.Direction.*;

import java.util.ArrayList;
import java.util.Random;

public class Cell implements MapObject
{
	private final MapBuilder mapBuilder;
	
	//top left corner of the cell
	private final int position;
	private final int gridX;
	private final int gridY;
	
	private final int width;
	private final int height;
	
	private final boolean chamber;
	
	private ArrayList<Integer> floorTiles = new ArrayList<Integer>();
	private ArrayList<Integer> pathTiles = new ArrayList<Integer>();
	private final ArrayList<Integer> tiles = new ArrayList<Integer>();
	
	private final ArrayList<Integer> exitsNorth = new ArrayList<Integer>();
	private final ArrayList<Integer> exitsEast = new ArrayList<Integer>();
	private final ArrayList<Integer> exitsSouth = new ArrayList<Integer>();
	private final ArrayList<Integer> exitsWest = new ArrayList<Integer>();
	
	private final int cutNorth;
	private final int cutEast;
	private final int cutSouth;
	private final int cutWest;
	
	private ArrayList<ExitClass> usedExits = new ArrayList<ExitClass>();
	private ArrayList<Integer> usedExitsTiles = new ArrayList<Integer>();
	private ArrayList<Cell> connections = new ArrayList<Cell>();
	
	private int cutNorthCalculated;
	private int cutEastCalculated;
	private int cutSouthCalculated;
	private int cutWestCalculated;
	
	private int cutWidthCalculated = 0;
	private int cutHeightCalculated = 0;
	
	private Random random;
	
	protected Cell(int gridX, int gridY, int position, MapBuilder mapBuilder, boolean chamber)
	{	
		this.mapBuilder = mapBuilder;
		
		this.position = position;
		width = mapBuilder.getCellWidth();
		height = mapBuilder.getCellHeight();
		
		this.gridX = gridX;
		this.gridY = gridY;
		
		this.chamber = chamber;
		
		random = mapBuilder.getSeededRandom();
		
		if(chamber)
		{
			//TODO catch invalid bounds!
			if(!(mapBuilder.getCutProbabilty() == 0))
			{
				if(random.nextInt(mapBuilder.getCutProbabilty()) == 0)
				{
					cutNorthCalculated = calculateCut(NORTH, height);
					cutEastCalculated = calculateCut(EAST, width);
					cutSouthCalculated = calculateCut(SOUTH, height);
					cutWestCalculated = calculateCut(WEST, width);
				}
			}
			
			if(width - cutEastCalculated - cutWestCalculated < 2)
			{
				cutEast = width - 2;
				cutWest = 0;
			}
			else
			{
				cutEast = cutEastCalculated;
				cutWest = cutWestCalculated;
			}
			
			if(height - cutNorthCalculated - cutSouthCalculated < 2)
			{
				cutNorth = height - 2;
				cutSouth = 0;
			}
			else
			{
				cutNorth = cutNorthCalculated;
				cutSouth = cutSouthCalculated;
			}
			
			for(int i = 0; i < height; i++)
			{
				for(int j = 0; j < width; j++)
				{
					tiles.add((position + j) + (mapBuilder.getMapWidth()*i));
				}
			}
		
			generateChamber(cutNorth, cutEast, cutSouth, cutWest);
		}
		else
		{
			cutNorth = 0;
			cutEast = 0;
			cutSouth = 0;
			cutWest = 0;
		}
		
		generateExits();
	}
	
	protected int calculateCut(Direction direction, int maxCellCut)
	{
		int cut;
		int minCellCut = mapBuilder.getMinCellCut();
		
		cut = mapBuilder.getSeededRandom().nextInt(maxCellCut + 1);
		if(cut < minCellCut)
			cut = minCellCut;
		
		switch(direction)
		{
		case NORTH:
			if(height - (cut + cutHeightCalculated) >= mapBuilder.getMinChamberHeight())
			{
				cutHeightCalculated += cut;
			}
			else
			{
				cut = (height - mapBuilder.getMinChamberHeight()) - cutHeightCalculated;
				
				cutHeightCalculated += cut;
			}
			break;
		case EAST:
			if(width - (cut + cutWidthCalculated) >= mapBuilder.getMinChamberWidth())
			{
				cutWidthCalculated += cut;
			}
			else
			{
				cut = (width - mapBuilder.getMinChamberWidth()) - cutWidthCalculated;
				
				cutWidthCalculated += cut;
			}
			break;
		case SOUTH:
			if(height - (cut + cutHeightCalculated) >= mapBuilder.getMinChamberHeight())
			{
				cutHeightCalculated += cut;
			}
			else
			{
				cut = (height - mapBuilder.getMinChamberHeight()) - cutHeightCalculated;
				
				cutHeightCalculated += cut;
			}
			break;
		case WEST:
			if(width - (cut + cutWidthCalculated) >= mapBuilder.getMinChamberWidth())
			{
				cutWidthCalculated += cut;
			}
			else
			{
				cut = (width - mapBuilder.getMinChamberWidth()) - cutWidthCalculated;
				
				cutWidthCalculated += cut;
			}
			break;
		default:
			break;
		}
		
		return cut;
	}
	
	protected void generateExits()
	{
		//probability = 1/pWidth-1
		int pWidth = 8;
		//probability = 1/pHeight-1
		int pHeight = 8;
		int p = 0;
		
		while(exitsNorth.isEmpty() || exitsEast.isEmpty() || exitsSouth.isEmpty() || exitsWest.isEmpty())
		{
			
			//north
			if(exitsNorth.isEmpty())
			{
				for(int i = 0; i < width; i++)
				{
					if(i > cutWest && i < width - cutEast)
					{
						p = random.nextInt(pWidth);
						if(p == 0)
						{
							exitsNorth.add(position + i);
							i++;
						}
					}
				}
			}
			
			//east
			if(exitsEast.isEmpty())
			{
				for(int i = 0; i < height; i++)
				{
					if(i > cutNorth && i < height - cutSouth)
					{
						p = random.nextInt(pHeight);
						if(p == 0)
						{
							exitsEast.add((position + (width-1)) + (i* mapBuilder.getMapWidth()));
							i++;
						}
					}
				}
			}
			
			
			//south
			if(exitsSouth.isEmpty())
			{
				for(int i = 0; i < width; i++)
				{
					if(i > cutWest && i < width - cutEast)
					{
						p = random.nextInt(pWidth);
						if(p == 0)
						{
							exitsSouth.add((position + mapBuilder.getMapWidth()*(height-1)) + i);
							i++;
						}
					}
				}
			}
			
			//west
			if(exitsWest.isEmpty())
			{
				for(int i = 0; i < height; i++)
				{
					if(i > cutNorth && i < height - cutSouth)
					{
						p = random.nextInt(pHeight);
						if(p == 0)
						{
							exitsWest.add(position + (i * mapBuilder.getMapWidth()));
							i++;;
						}
					}
				}
			}
		}
	}
	
	protected void generateChamber(int cutNorth, int cutEast, int cutSouth, int cutWest)
	{
		int start;
		Integer[] room = tiles.toArray(new Integer[tiles.size()]);
		
		for(int i = 0; i < cutNorth; i++)
		{
			start = i * width;
			for(int j = 0; j < width; j++)
			{
				room[start + j] = -1;
			}
		}
		
		for(int i = 0; i < cutEast; i++)
		{
			start = width-1 - i;
			for(int j = 0; j < height; j++)
			{
				room[start+(j*width)] = -1;
			}
		}
		
		for(int i = 0; i < cutSouth; i++)
		{
			start = width * (height-1 - i);
			for(int j = 0; j < width; j++)
			{
				room[start+j] = -1;
			}
		}
		
		for(int i = 0; i < cutWest; i++)
		{
			start = i;
			for(int j = 0; j < height; j++)
			{
				room[start + j*width] = -1;
			}
		}
		
		for(int i = 0; i < room.length; i++)
		{
			if(room[i] > -1)
			{
				floorTiles.add(room[i]);
			}
		}
	}
	
	protected void connectUsedExits()
	{
		ArrayList<Integer> buffer = new ArrayList<Integer>();
		
		if(!usedExitsTiles.isEmpty())
		{
			if(chamber)
			{
				int tile;
				int x, y;
				
				for(int i = 0; i < exitsNorth.size(); i++)
				{
					tile = exitsNorth.get(i);
					
					if(usedExitsTiles.contains(tile))
					{
						x = MapTools.xValue(tile, mapBuilder.getMapWidth());
						y = MapTools.yValue(tile, mapBuilder.getMapWidth());
						
						for(int j = 0; j < cutNorth; j++)
						{
							pathTiles.add(MapTools.getTile(x, y+j, mapBuilder.getMapWidth(), mapBuilder.getMapHeight()));
						}
					}
				}
				
				for(int i = 0; i < exitsEast.size(); i++)
				{
					tile = exitsEast.get(i);
					
					if(usedExitsTiles.contains(tile))
					{
						x = MapTools.xValue(tile, mapBuilder.getMapWidth());
						y = MapTools.yValue(tile, mapBuilder.getMapWidth());
						
						for(int j = 0; j < cutEast; j++)
						{
							pathTiles.add(MapTools.getTile(x-j, y, mapBuilder.getMapWidth(), mapBuilder.getMapHeight()));
						}
					}
				}
				
				for(int i = 0; i < exitsSouth.size(); i++)
				{
					tile = exitsSouth.get(i);
					
					if(usedExitsTiles.contains(tile))
					{
						x = MapTools.xValue(tile, mapBuilder.getMapWidth());
						y = MapTools.yValue(tile, mapBuilder.getMapWidth());
						
						for(int j = 0; j < cutSouth; j++)
						{
							pathTiles.add(MapTools.getTile(x, y-j, mapBuilder.getMapWidth(), mapBuilder.getMapHeight()));
						}
					}
				}
				
				for(int i = 0; i < exitsWest.size(); i++)
				{
					tile = exitsWest.get(i);
					
					if(usedExitsTiles.contains(tile))
					{
						x = MapTools.xValue(tile, mapBuilder.getMapWidth());
						y = MapTools.yValue(tile, mapBuilder.getMapWidth());
						
						for(int j = 0; j < cutWest; j++)
						{
							pathTiles.add(MapTools.getTile(x+j, y, mapBuilder.getMapWidth(), mapBuilder.getMapHeight()));
						}
					}
				}
			}
			else
			{
				int exit = usedExitsTiles.get(0);
				for(int i = 1; i < usedExitsTiles.size(); i++)
				{
					buffer = MapTools.connectTiles(exit, usedExitsTiles.get(i), mapBuilder.getMapWidth(), floorTiles);
					pathTiles.addAll(buffer);
				}
			}
		}
	}
	
	class ExitClass
	{
		int tile;
		Direction direction;
		
		private ExitClass(int tile, Direction direction)
		{
			this.tile = tile;
			this.direction = direction;
		}
		
		private int getTile()
		{
			return tile;
		}
		
		private Direction getDirection()
		{
			return direction;
		}
	}
	
	protected void addConnection(Cell connection)
	{
		connections.add(connection);
	}
	
	protected void markExitAsUsed(int pos, Direction direction)
	{	
		boolean alreadyUsed = false;
		
		for(int i = 0; i < usedExits.size(); i++)
		{
			ExitClass exit = usedExits.get(i);
			
			if((exit.getTile() == pos && exit.getDirection() == direction))
			{	
				alreadyUsed = true;
			}
		}
		
		if(!alreadyUsed)
		{
			usedExits.add(new ExitClass(pos, direction));
			usedExitsTiles.add(pos);
		}
	}
	
	@Override
	public ArrayList<Integer> getTiles()
	{
		return tiles;
	}
	
	public ArrayList<Integer> getFloorTiles()
	{
		return floorTiles;
	}
	
	public ArrayList<Integer> getPathTiles()
	{
		return pathTiles;
	}
	
	protected ArrayList<Cell> getConnections()
	{
		return connections;
	}
	
	protected int getWidth()
	{
		return width;
	}
	
	protected int getHeight()
	{
		return height;
	}
	
	protected int getPosition()
	{
		return position;
	}
	
	protected int getGridX()
	{
		return gridX;
	}
	
	protected int getGridY()
	{
		return gridY;
	}

	protected boolean isChamberCell()
	{
		return chamber;
	}
	
	protected ArrayList<Integer> getExitsNorth() {
		return exitsNorth;
	}

	protected ArrayList<Integer> getExitsEast() {
		return exitsEast;
	}

	protected ArrayList<Integer> getExitsSouth() {
		return exitsSouth;
	}

	protected ArrayList<Integer> getExitsWest() {
		return exitsWest;
	}
	
	protected ArrayList<Integer> getUsedExits()
	{
		return usedExitsTiles;
	}
}
