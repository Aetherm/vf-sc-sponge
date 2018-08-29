package net.aethermol;

import org.spongepowered.api.world.World;

public class CustomWorldBorder
{	
	World world;
	
	public static final double DEFAULT_DIAMETER = 96;
	
	public CustomWorldBorder(World world)
	{	
		this.world = world;
		
		instantiate();
	}
	
	public void instantiate()
	{
		if(world.getWorldBorder().getCenter().getX() != 0 || world.getWorldBorder().getCenter().getY() != 0)
			world.getWorldBorder().setCenter(0, 0);
	}
	
	public void extendBorder(int amount)
	{
		double diameter = world.getWorldBorder().getDiameter();
		
		world.getWorldBorder().setDiameter(diameter + amount);
	}
	
	public double getBorderWide()
	{
		return world.getWorldBorder().getDiameter();
	}
	
	public double getAddedBorderWide()
	{
		return getBorderWide() - DEFAULT_DIAMETER;
	}
}
