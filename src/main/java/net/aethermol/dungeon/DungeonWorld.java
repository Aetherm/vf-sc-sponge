package net.aethermol.dungeon;

import java.io.IOException;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.WorldArchetypes;

public class DungeonWorld
{
	public static World createWorld(String name)
	{
		World world;
		
		try
		{
			Sponge.getGame().getServer().createWorldProperties(name, WorldArchetypes.THE_VOID);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		world = Sponge.getServer().loadWorld(name).orElse(null);
		
		return world;
	}
}
