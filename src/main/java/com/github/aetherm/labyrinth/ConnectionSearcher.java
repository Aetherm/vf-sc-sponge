package com.github.aetherm.labyrinth;

import java.util.ArrayList;

public class ConnectionSearcher
{
	private ArrayList<Cell> levelRoomCells = new ArrayList<Cell>();
	
	private ArrayList<ArrayList<Cell>> connections = new ArrayList<ArrayList<Cell>>();
	
	protected ConnectionSearcher(ArrayList<Cell> levelCells)
	{
		this.levelRoomCells = levelCells;
		
		searchConnections();
	}
	
	protected void searchConnections()
	{
		//list of all connections of a local cell
		ArrayList<Cell> connected = new ArrayList<Cell>();
		
		for(int i = 0; i < levelRoomCells.size(); i++)
		{
			connected = searchForConnections(levelRoomCells.get(i), null);
			
			if(connections.isEmpty())
			{
				connections.add(connected);
			}
			else
			{
				boolean contains = false;
				
				for(int j = 0; j < connections.size(); j++)
				{
					if(connections.get(j).containsAll(connected))
					{
						contains = true;
					}
				}
				
				if(!contains)
				{
					connections.add(connected);
				}
			}
		}
			
	}
	
	protected ArrayList<Cell> searchForConnections(Cell cell, ArrayList<Cell> ignore)
	{
		ArrayList<Cell> observedConnections = new ArrayList<Cell>();
		
		if(ignore != null)
		{
			observedConnections.addAll(ignore);
		}
		else
		{
			observedConnections.add(cell);
		}
		
		//iterating over local connections of one cell
		for(int i = 0; i < cell.getConnections().size(); i++)
		{
			Cell connection = cell.getConnections().get(i);
			
			//checking if the collected connections doesn't contain the now found one
			if(!observedConnections.contains(connection))
			{
				observedConnections.add(connection);
				
				ArrayList<Cell> externConnections = searchForConnections(connection, observedConnections);
				
				for(int j = 0; j < externConnections.size(); j++)
				{
					Cell foundConnection = externConnections.get(j);
					
					if(!observedConnections.contains(foundConnection))
					{
						observedConnections.add(foundConnection);
					}
				}

			}
		}
		
		return observedConnections;
	}
	
	protected ArrayList<ArrayList<Cell>> getConnections()
	{
		return connections;
	}
}
