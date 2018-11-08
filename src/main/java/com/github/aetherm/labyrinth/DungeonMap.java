package com.github.aetherm.labyrinth;

import java.util.ArrayList;

public class DungeonMap
{	
	private char[] map;
	
	private ArrayList<Cell> roomCells = new ArrayList<Cell>();
	private ArrayList<Cell> connectionCells = new ArrayList<Cell>();
	private ArrayList<Corridor> corridors = new ArrayList<Corridor>();
	private MapBuilder mapBuilder;
	
	private int builderSeed;
	private int id;
	
	private int width;
	private int height;
	
	public DungeonMap(MapBuilder mapBuilder)
	{
		this.mapBuilder = mapBuilder;
	
		map = new char[mapBuilder.getMapWidth()*mapBuilder.getMapHeight()];
		
		mapBuilder.createFloor();
		
		roomCells = mapBuilder.getChamberCells();
		connectionCells = mapBuilder.getConnectionCells();
		corridors = mapBuilder.getCorridors();
		builderSeed = mapBuilder.getBuilderSeed();
		id = mapBuilder.getMapID();
		width = mapBuilder.getMapWidth();
		height = mapBuilder.getMapHeight();
		
		clearMap();
		convertToMap();
		
		mapBuilder.refresh();
	}
	
	
	/**
	 * @return the roomCells
	 */
	public ArrayList<Cell> getRoomCells()
	{
		return roomCells;
	}


	/**
	 * @return the connectionCells
	 */
	public ArrayList<Cell> getConnectionCells()
	{
		return connectionCells;
	}


	/**
	 * @return the corridors
	 */
	public ArrayList<Corridor> getCorridors()
	{
		return corridors;
	}
	

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}


	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}


	private void clearMap()
	{	
		for(int i = 0; i < map.length; i++)
		{
			map[i] = '#';
		}
	}
	
	private void convertToMap()
	{	
		for(int i = 0; i < roomCells.size(); i++)
		{
			for(int j = 0; j < roomCells.get(i).getFloorTiles().size(); j++)
			{	
				map[roomCells.get(i).getFloorTiles().get(j)] = ' ';
			}
			
			for(int j = 0; j < roomCells.get(i).getPathTiles().size(); j++)
			{	
				map[roomCells.get(i).getPathTiles().get(j)] = ';';
			}
			
			for(int j = 0; j < roomCells.get(i).getUsedExits().size(); j++)
			{
//				map[roomCells.get(i).getUsedExits().get(j)] = '/';
			}
		}
		
		for(int i = 0; i < connectionCells.size(); i++)
		{
			for(int j = 0; j < connectionCells.get(i).getPathTiles().size(); j++)
			{	
				map[connectionCells.get(i).getPathTiles().get(j)] = ';';
			}
		}
		
		for(int i = 0; i < corridors.size(); i++)
		{
			for(int j = 0; j < corridors.get(i).getTiles().size(); j++)
			{
				map[corridors.get(i).getTiles().get(j)] = ':';
			}
		}
	}
	
	/**
	 * @return the seed
	 */
	public int getBuilderSeed() {
		return builderSeed;
	}


	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}


	@Override
	public String toString()
	{
		String ret = "";
	
		ret += "Seed: "+mapBuilder.getBuilderSeed()+"\n";
		ret += "ID: "+id+"\n";
		
		for(int i = 0; i < mapBuilder.getMapHeight(); i++)
		{
			for(int j = 0; j < mapBuilder.getMapWidth(); j++)
			{
				ret += map[((i* mapBuilder.getMapWidth()) + j)];
				ret += ' ';
				
				if(j ==  mapBuilder.getMapWidth()-1)
				{
					ret += "\n";
				}
			}
		}
		
		return ret;
	}
}
