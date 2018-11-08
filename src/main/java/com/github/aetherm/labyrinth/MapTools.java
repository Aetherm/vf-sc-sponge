package com.github.aetherm.labyrinth;

import java.util.ArrayList;

public class MapTools
{
	public static int xValue(int tile, int mapWidth)
	{
		int x = -1;
		
		x = tile - (tile / mapWidth) * mapWidth;
		
		return x;
	}
	
	public static int yValue(int tile, int mapWidth)
	{
		int y = -1;
		
		y = (tile / mapWidth);
		
		return y;
	}
	
	protected static boolean shareX(int tileA, int tileB, int mapWidth)
	{
		if(xValue(tileA, mapWidth) == xValue(tileB, mapWidth))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	protected static boolean shareY(int tileA, int tileB, int mapWidth)
	{
		if(yValue(tileA, mapWidth) == yValue(tileB, mapWidth))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	protected static int getTile(int x, int y, int mapWidth, int mapHeight)
	{
		int tile;
		
		if(x > -1 && y > -1 && x < mapWidth && y < mapHeight)
		{
			tile = x + mapWidth * y;
		}
		else
			tile = -1;
		
		return tile;
	}
	
	protected static ArrayList<Integer> connectTiles(int tileStart, int tileEnd, int mapWidth, ArrayList<Integer> ignore)
	{
		ArrayList<Integer> connectionTiles = new ArrayList<Integer>();
		ignore = (ignore == null)? ignore = new ArrayList<Integer>() : ignore ;
		
		int startX = xValue(tileStart, mapWidth);
		int startY = yValue(tileStart, mapWidth);
		
		int endX = xValue(tileEnd, mapWidth);
		int endY = yValue(tileEnd, mapWidth);
		
		int savedTile = tileStart;
		int difference;
		
		if(startX > endX)
		{
			difference = startX-endX;
			
			for(int i = 0; i <= difference; i++)
			{
				savedTile = tileStart - i;
				if(!ignore.contains(savedTile))
				{
					connectionTiles.add(savedTile);
					ignore.add(savedTile);
				}
				
			}
			
		}
		else
		{
			difference = endX-startX;
			
			for(int i = 0; i <= difference; i++)
			{
				savedTile = tileStart + i;
				if(!ignore.contains(savedTile))
				{
					connectionTiles.add(savedTile);
					ignore.add(savedTile);
				}
				
			}
		}
		
		if(startY > endY)
		{
			difference = startY - endY;
			
			for(int i = 0; i < difference; i++)
			{
				savedTile = savedTile - (mapWidth);
				if(!ignore.contains(savedTile))
				{
					connectionTiles.add(savedTile);
					ignore.add(savedTile);
				}

			}
		}
		else
		{
			difference = endY - startY;
			
			for(int i = 0; i < difference; i++)
			{
				savedTile = savedTile + (mapWidth);
				if(!ignore.contains(savedTile))
				{
					connectionTiles.add(savedTile);
					ignore.add(savedTile);
				}
				

			}
		}
		
		return connectionTiles;
	}
}
