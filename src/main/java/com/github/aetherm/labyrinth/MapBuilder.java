package com.github.aetherm.labyrinth;

import java.util.ArrayList;
import java.util.Random;

/**
 * Contains all elements of a single map
 * 
 */

public class MapBuilder
{	
	private final int mapWidth;
	private final int mapHeight;

	//the amount of cells on the abscissa (x)
	private int cellsX;
	//the amount of cells on the ordinate (y)
	private int cellsY;
	
	private final int cellWidth;
	private final int cellHeight;
	
	private final int mapBorderX;
	private final int mapBorderY;
	
	private final int createCellOdds;
	//the minimum amount of chamber cells to be created, note that this does not affect connection cells!
	private final int cellMinimum;
	
	private final int minCellCut;
	private final int cutProbabilty;
	
	private final int minChamberWidth;
	private final int minChamberHeight;
	
	//the distance between left side/upper side to next left side/upper side
	private final int distanceX;
	private final int distanceY;
	
	private final Random seededRandom;
	private final int builderSeed;
	
	private ArrayList<Corridor> corridors = new ArrayList<Corridor>();
	private ArrayList<Cell> chamberCells = new ArrayList<Cell>();
	private ArrayList<Cell> connectionCells = new ArrayList<Cell>();
	private Connector connector;
	private ConnectionSearcher connectionSearcher;
	
	//holds the value for the id of the current map
	private int mapID = 0;
	
	public MapBuilder(MapDetails mapDetails, String seed)
	{	
		this.mapWidth = mapDetails.getMapWidth();
		this.mapHeight = mapDetails.getMapHeight();
		this.cellWidth = mapDetails.getCellWidth();
		this.cellHeight = mapDetails.getCellHeight();
		this.cellsX = mapDetails.getCellsX();
		this.cellsY = mapDetails.getCellsY();
		this.cellMinimum = mapDetails.getCellMinimum();
		this.createCellOdds = mapDetails.getCreateCellOdds();
		this.mapBorderX = mapDetails.getMapBorderX();
		this.mapBorderY = mapDetails.getMapBorderY();
		this.minCellCut = mapDetails.getMinCellCut();
		this.cutProbabilty = mapDetails.getCutProbability();
		this.minChamberWidth = mapDetails.getMinChamberWidth();
		this.minChamberHeight = mapDetails.getMinChamberHeight();
		
		if(seed != null)
		{
			boolean isNumber;
			try
			{
				Integer.parseInt(seed);
				isNumber = true;
			}
			catch(NumberFormatException exception)
			{
				isNumber = false;
			}
			
			if(isNumber)
			{
				builderSeed = Integer.parseInt(seed);
			}
			else
			{
				builderSeed = seed.hashCode();
			}
		}
		else
		{
			Random random = new Random();
			builderSeed = random.nextInt();
		}
		
		seededRandom = new Random(builderSeed);
		
		distanceX = (int) Math.round((double)mapWidth / (double)cellsX);
		distanceY = (int) Math.round((double)mapHeight / (double)cellsY);
	}
	
	public void refresh()
	{
		corridors.clear();
		chamberCells.clear();
		connectionCells.clear();
		
		mapID++;
	}
	
	/**
	 * Creates the elements of the {@link MapBuilder} instance in dependency of its settings.
	 */
	protected void createFloor()
	{
		layCells();
		
		connectCells();
		
		connectionSearcher = new ConnectionSearcher(chamberCells);
		
		if(connectionSearcher.getConnections().size() > 1)
			addMissingLinks();
		
		connector.connectUsedCellExits();
		
		corridors = connector.getCorridors();
	}
	
	/**
	 * Connects all {@link Cell}s of local groups with other local groups.
	 * This is necessary since all cells with accessible tiles (chambers) should be connected to each other.
	 */
	private void addMissingLinks()
	{
		Cell startCell;
		Cell endCell;
		
		startCell = connectionSearcher.getConnections().get(0).get(0);
		
		for(int i = 1; i < connectionSearcher.getConnections().size(); i++)
		{
			endCell = connectionSearcher.getConnections().get(i).get(0);
			
			connector.connect(startCell, endCell);
		}
			
	}
	
	/**
	 * Sets all {@link Cell}s in dependency of the settings of {@link MapBuilder}
	 */
	private void layCells()
	{
		Cell cell;
		
		boolean enough = false;
		
		//probability = 1 / createCellOdd
		int p = createCellOdds;
		
		//random number between 0 and inclusive p
		int r;
		
		int position;
		
		while((cellsX * (cellWidth+3) - 3) + mapBorderX*2 > mapWidth)
		{
			cellsX--;
		}
		
		while((cellsY * (cellHeight+3) - 3) + mapBorderX*2 > mapHeight)
		{
			cellsY--;
		}
		
		while(!enough)
		{
			//height
			for(int y = 0; y < cellsY; y++)
			{
				//width
				for(int x = 0; x < cellsX; x++)
				{
					r = seededRandom.nextInt(p);
					
					position = (x * (cellWidth+3) + mapBorderX) + (y * mapWidth*(cellHeight+3) + (mapBorderY * mapWidth));
					
					if(r == 0)
					{
						if(MapTools.shareY(position, position + cellWidth + mapBorderX, mapWidth))
						{
							if(position+(cellHeight * mapWidth) < mapWidth*mapHeight)
							{
								cell = new Cell(x, y, position , this, true);
								chamberCells.add(cell);
							}
						}
					}
					else
					{
						cell = new Cell(x, y, position , this, false);
						connectionCells.add(cell);
					}
				}
			}
			
			if(chamberCells.size() <= cellMinimum)
			{
				chamberCells.clear();
				connectionCells.clear();
			}
			else
			{
				enough = true;
			}
		}
	}
	
	/**
	 * Connects all cells that share the same abscissa or ordinate
	 */
	private void connectCells()
	{
		connector = new Connector(this);
			
		Cell startCell;
		int startGridX;
		int startGridY;
			
		Cell endCell;
		int endGridX;
		int endGridY;
			
		//getting the cells to start and work with
		for(int i = 0; i < chamberCells.size(); i++)
		{
			startCell = chamberCells.get(i);		
			
			startGridX = startCell.getGridX();
			startGridY = startCell.getGridY();
			
			//searching for the cells to connect to
			for(int j = 0; j < chamberCells.size(); j++)
			{		
				//excluding the current start cell
				if(j == i)
				{
					//do nothing
				}
				else
				{
					endCell = chamberCells.get(j);
					endGridX = endCell.getGridX();
					endGridY = endCell.getGridY();
						
					if(endGridX > startGridX && (endGridY == startGridY))
					{	
						if(connector.connect(startCell, endCell))
						{
							startCell.addConnection(endCell);
							endCell.addConnection(startCell);
						}
					}
						
					if(endGridY < startGridY && (endGridX == startGridX))
					{
						if(connector.connect(startCell, endCell))
						{
							startCell.addConnection(endCell);
							endCell.addConnection(startCell);
						}
					}
					
				}
				
			}
		}
	}
	
	/**
	 * Returns a specific {@link Cell} for a given x and y value.
	 * Note that a result is only given if the coordinates exactly match the point zero (top left edge) of the {@link Cell}!
	 * 
	 * @param gridX
	 * the x value
	 * @param gridY
	 * the y value
	 * @return
	 * if positive the wanted {@link Cell}, else {@link null}
	 */
	protected Cell getSpecificCell(int gridX, int gridY)
	{
		Cell cell;
		
		for(int i = 0; i < chamberCells.size(); i++)
		{
			cell = chamberCells.get(i);
			
			if(cell.getGridX() == gridX && cell.getGridY() == gridY)
			{
				return cell;
			}
		}
		
		for(int i = 0; i < connectionCells.size(); i++)
		{
			cell = connectionCells.get(i);
			
			if(cell.getGridX() == gridX && cell.getGridY() == gridY)
			{
				return cell;
			}
		}
		
		return null;
	}
	
	protected int getMapID()
	{
		return mapID;
	}
	
	protected int getMapWidth()
	{
		return mapWidth;
	}

	protected int getMapHeight()
	{
		return mapHeight;
	}

	protected int getCellsX()
	{
		return cellsX;
	}

	protected int getCellsY()
	{
		return cellsY;
	}

	protected int getCellWidth()
	{
		return cellWidth;
	}
	
	protected int getCellHeight()
	{
		return cellHeight;
	}

	protected int getMapBorderX()
	{
		return mapBorderX;
	}

	protected int getMapBorderY()
	{
		return mapBorderY;
	}

	protected int getCellMinimum()
	{
		return cellMinimum;
	}

	protected int getDistanceX()
	{
		return distanceX;
	}

	protected int getDistanceY()
	{
		return distanceY;
	}
	
	protected int getMinCellCut()
	{
		return minCellCut;
	}
	
	protected int getCutProbabilty()
	{
		return cutProbabilty;
	}

	protected int getBuilderSeed()
	{
		return builderSeed;
	}
	
	protected Random getSeededRandom()
	{
		return seededRandom;
	}
	
	/**
	 * @return the minChamberWidth
	 */
	protected int getMinChamberWidth()
	{
		return minChamberWidth;
	}

	/**
	 * @return the minChamberHeight
	 */
	protected int getMinChamberHeight()
	{
		return minChamberHeight;
	}

	@SuppressWarnings("unchecked")
	protected ArrayList<Cell> getChamberCells()
	{
		return (ArrayList<Cell>) chamberCells.clone();
	}
	
	@SuppressWarnings("unchecked")
	protected ArrayList<Cell> getConnectionCells()
	{
		return (ArrayList<Cell>) connectionCells.clone();
	}
	
	@SuppressWarnings("unchecked")
	protected ArrayList<Corridor> getCorridors()
	{
		return (ArrayList<Corridor>) corridors.clone();
	}
	
}
