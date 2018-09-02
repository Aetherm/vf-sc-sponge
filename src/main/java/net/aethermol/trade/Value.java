package net.aethermol.trade;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;

public class Value
{
	//wanted ItemType as reference but that doesn't seem to work with switch statement :/
	//any ideas?
	final static String COAL = "minecraft:coal";
	final static String IRON_INGOT = "minecraft:iron_ingot";
	final static String EMERALD = "minecraft:emerald";
	final static String REDSTONE = "minecraft:redstone";
	final static String DIAMOND = "minecraft:diamond";
	final static String DYE = "minecraft:dye";
	
	//added with 0.2.1
	final static String QUARTZ = "minecraft:quartz";
	final static String GLOWSTONE_DUST = "minecraft:glowstone_dust";
	final static String CLAY_BALL = "minecraft:clay_ball";
	
	//all values represent the value of a full stack in gold nuggets!
	//e.g.: 64 coal = 10 gold nuggets
	//Please note: All items have to be traded in complete stacks (Prices not divisible by 64),
	//except diamonds and emeralds for now!
	//Prices are the relation of gold/resource occurrence in the world!
	final static int COAL_VALUE = 10;
	final static int IRON_INGOT_VALUE = 58;
	final static int EMERALD_VALUE = 704;
	final static int REDSTONE_VALUE = 20;
	final static int DIAMOND_VALUE = 384;
	final static int LAPIS_LAZULI_VALUE = 61;
	
	//added with 0.2.1
	final static int QUARTZ_VALUE = 54;
	final static int GLOWSTONE_DUST_VALUE = 128;
	final static int CLAY_BALL_VALUE = 16;
	
	public static int getSellValue(ItemStack itemStack)
	{	
		int ret;
		
		ItemType itemType = itemStack.getType();
		
		if(itemStack.getQuantity() == itemStack.getMaxStackQuantity())
		{	
			switch(itemType.getName())
			{
			case COAL: ret = COAL_VALUE;
			break;
			case IRON_INGOT: ret = IRON_INGOT_VALUE;
			break;
			//no emeralds for sale, to enforce use for expansion!
//			case EMERALD: ret = EMERALD_VALUE;
//			break;
			case REDSTONE: ret = REDSTONE_VALUE;
			break;
			case DIAMOND: ret = DIAMOND_VALUE;
			break;
			case DYE: 
				if(itemStack.get(Keys.DYE_COLOR).orElse(null).equals(DyeColors.BLUE))
				{
					ret = LAPIS_LAZULI_VALUE;
				}
				else
				{
					ret = -1;
				}
			break;
			case QUARTZ: ret = QUARTZ_VALUE;
			break;
			//no glow-stone!
			case CLAY_BALL: ret = CLAY_BALL_VALUE;
			break;
			default: ret = -1;
			}
		}
		else
		{
			switch(itemType.getName())
			{
			case DIAMOND: ret = DIAMOND_VALUE/64*itemStack.getQuantity();
			break;
			default: ret = -1;
			}
		}
		
		return ret;
	}
	
	public static int getBuyValue(ItemStack itemStack)
	{
		int ret;
		
		ItemType itemType = itemStack.getType();
		
		if(itemStack.getQuantity() == itemStack.getMaxStackQuantity())
		{
			itemType = itemStack.getType();
			
			switch(itemType.getName())
			{
			case COAL: ret = COAL_VALUE;
			break;
			case IRON_INGOT: ret = IRON_INGOT_VALUE;
			break;
			case EMERALD: ret = EMERALD_VALUE;
			break;
			case REDSTONE: ret = REDSTONE_VALUE;
			break;
			case DIAMOND: ret = DIAMOND_VALUE;
			break;
			case DYE: 
				if(itemStack.get(Keys.DYE_COLOR).orElse(null).equals(DyeColors.BLUE))
				{
					ret = LAPIS_LAZULI_VALUE;
				}
				else
				{
					ret = -1;
				}
			break;
			case QUARTZ: ret = QUARTZ_VALUE;
			break;
			case GLOWSTONE_DUST: ret = GLOWSTONE_DUST_VALUE;
			break;
			case CLAY_BALL: ret = CLAY_BALL_VALUE;
			break;
			default: ret = -1;
			}
		}
		else
		{
			switch(itemType.getName())
			{
			case DIAMOND: ret = DIAMOND_VALUE/64*itemStack.getQuantity();
			break;
			case EMERALD: ret = EMERALD_VALUE/64*itemStack.getQuantity();
			break;
			default: ret = -1;
			}
		}
		
		return ret;
	}
}
