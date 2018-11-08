package com.github.aetherm.vfscsponge.dungeon;

import java.io.IOException;
import java.util.ArrayList;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.WorldArchetypes;

import com.flowpowered.math.vector.Vector3i;
import com.github.aetherm.labyrinth.DungeonMap;
import com.github.aetherm.labyrinth.MapBuilder;
import com.github.aetherm.labyrinth.MapDetails;
import com.github.aetherm.labyrinth.MapTools;
import com.github.aetherm.vfscsponge.core.Central;

public class DungeonWorld
{
	private World world;
	private Vector3i spawnPoint;
	
	MapDetails mapDetails;
	MapBuilder mapBuilder;
	DungeonMap dungeonMap;
	
	ArrayList<Integer> floor = new ArrayList<Integer>();
	
	private final static int WIDTH = 64;
	private final static int HEIGHT = 32;
	private final static int BLOCKS = 5;
	
	private final static int X_OFFSET = 0;
	private final static int Y_OFFSET = 32;
	private final static int Z_OFFSET = 0;
	
	
	private final static int GROUND = 8;
	
	public DungeonWorld(String name)
	{
		mapDetails = Central.mapDetails;
		
		mapBuilder = new MapBuilder(mapDetails, null);
		dungeonMap = new DungeonMap(mapBuilder);
		
		for(int i = 0; i < dungeonMap.getRoomCells().size(); i++)
		{
			floor.addAll(dungeonMap.getRoomCells().get(i).getFloorTiles());
			floor.addAll(dungeonMap.getRoomCells().get(i).getPathTiles());
		}
		
		spawnPoint = new Vector3i
					(
						MapTools.xValue(floor.get(0), WIDTH),
						Y_OFFSET + GROUND,
						MapTools.yValue(floor.get(0), WIDTH)
					);
		
		for(int i = 0; i < dungeonMap.getConnectionCells().size(); i++)
		{
			floor.addAll(dungeonMap.getConnectionCells().get(i).getPathTiles());
		}
		
		for(int i = 0; i < dungeonMap.getCorridors().size(); i++)
		{
			floor.addAll(dungeonMap.getCorridors().get(i).getTiles());
		}
		
		System.out.println(floor.toString());
		System.out.println(dungeonMap.toString());
		
//		spawnPoint = new Vector3i(0, 69, 0);
		world = createWorld(name);
		createDungeon();
	}
	
	private World createWorld(String name)
	{
		World world;
		
		try
		{
			Sponge.getGame().getServer().createWorldProperties(name, WorldArchetypes.THE_VOID).setSpawnPosition(spawnPoint);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		world = Sponge.getServer().loadWorld(name).orElse(null);
		
		return world;
	}

	private void createDungeon()
	{		
		for(int z = 0; z < HEIGHT; z++)
		{
			for(int x = 0; x < WIDTH; x++)
			{
				for(int y = 0; y < BLOCKS; y++)
				{
					if(!floor.contains(new Integer(WIDTH*z+x)) || y <= 1)
					createDungeonBlock(x, y, z);
				}
			}
		}
		
		world.setBlockType(spawnPoint.add(0, -1, 0), BlockTypes.GLOWSTONE);
	}
	
	private void createDungeonBlock(int x, int y, int z)
	{
		BlockType block = BlockTypes.DIRT;
		
		//setting the borders
		if(x == 0 || y == 0 || z == 0 || x == WIDTH-1 || z == HEIGHT -1)
		{
			block = BlockTypes.BEDROCK;
		}
		else if(y == 1)
		{
			block = BlockTypes.GRASS_PATH;
		}
		else if(y == BLOCKS-1)
		{
			block = BlockTypes.GRASS;
		}
		
		world.setBlockType(x+X_OFFSET, y+Y_OFFSET, z+Z_OFFSET, block);
	}
	
	public World getWorld()
	{
		return world;
	}
}
