package com.github.aetherm.labyrinth;

public class MapDetails
{	
	final int mapWidth;
	final int mapHeight;
	
	final int cellsX;
	final int cellsY;
	
	final int mapBorderX;
	final int mapBorderY;

	final int cellWidth;
	final int cellHeight;
	
	final int minChamberWidth;
	final int minChamberHeight;
	
	final int createCellOdds;
	final int cellMinimum;
	
	final int minCellCut;
	/**
	 * the probability that the size of a {@link Cell} is reduced 
	 */
	final int cutProbability;
	
	
	public MapDetails
	(
		int mapWidth, int mapHeight, int cellsX, int cellsY, int mapBorderX, int mapBorderY, int cellWidth,
		int cellHeight, int minChamberWidth, int minChamberHeight, int createCellOdds, int cellMinimum,
		int minCellCut, int cutProbability
	)
	{	
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		
		this.cellsX = cellsX;
		this.cellsY = cellsY;
		
		this.mapBorderX = mapBorderX;
		this.mapBorderY = mapBorderY;
		
		this.cellWidth = cellWidth;
		this.cellHeight = cellHeight;
		
		this.minChamberWidth = minChamberWidth;
		this.minChamberHeight = minChamberHeight;
		
		this.createCellOdds = createCellOdds;
		this.cellMinimum = cellMinimum;
		
		this.minCellCut = minCellCut;
		
		this.cutProbability = cutProbability;
	}
	
	/**
	 * @return the mapWidth
	 */
	public int getMapWidth()
	{
		return mapWidth;
	}

	/**
	 * @return the mapHeight
	 */
	public int getMapHeight()
	{
		return mapHeight;
	}

	/**
	 * @return the cellsX
	 */
	public int getCellsX()
	{
		return cellsX;
	}

	/**
	 * @return the cellsY
	 */
	public int getCellsY()
	{
		return cellsY;
	}

	/**
	 * @return the mapBorderX
	 */
	public int getMapBorderX()
	{
		return mapBorderX;
	}

	/**
	 * @return the mapBorderY
	 */
	public int getMapBorderY()
	{
		return mapBorderY;
	}

	/**
	 * @return the cellWidth
	 */
	public int getCellWidth()
	{
		return cellWidth;
	}

	/**
	 * @return the cellHeight
	 */
	public int getCellHeight()
	{
		return cellHeight;
	}

	/**
	 * @return the minChamberWidth
	 */
	public int getMinChamberWidth()
	{
		return minChamberWidth;
	}

	/**
	 * @return the minChamberHeight
	 */
	public int getMinChamberHeight()
	{
		return minChamberHeight;
	}

	/**
	 * @return the createCellOdds
	 */
	public int getCreateCellOdds()
	{
		return createCellOdds;
	}


	/**
	 * @return the cellMinimum
	 */
	public int getCellMinimum()
	{
		return cellMinimum;
	}

	/**
	 * @return the minCellCut
	 */
	public int getMinCellCut()
	{
		return minCellCut;
	}

	/**
	 * @return the cutProbability
	 */
	public int getCutProbability()
	{	
		return cutProbability;
	}
}
