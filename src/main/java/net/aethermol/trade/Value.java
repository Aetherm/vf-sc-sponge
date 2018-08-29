package net.aethermol.trade;

import org.spongepowered.api.item.ItemType;

public class Value
{
	//wanted ItemType as reference but that doesn't seem to work with switch statement :/
	//any ideas?
	final static String COAL = "minecraft:coal";
	final static String IRON_INGOT = "minecraft:iron_ingot";
	final static String EMERALD = "minecraft:emerald";
	final static String REDSTONE = "minecraft:redstone";
	final static String DIAMOND = "minecraft:diamond";
//	final static String LAPIS_LAZULI = "LAPIS_LAZULI";
	
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
	
	public static int getSellValue(ItemType itemType, int quantity)
	{	
		int ret;
		
		
		if(quantity == itemType.getMaxStackQuantity())
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
//			case LAPIS_LAZULI: ret = LAPIS_LAZULI_VALUE;
//			break;
			default: ret = -1;
			}
		}
		else
		{
			switch(itemType.getName())
			{
			case DIAMOND: ret = DIAMOND_VALUE/64*quantity;
			break;
			default: ret = -1;
			}
		}
		
		return ret;
	}
	
	public static int getBuyValue(ItemType itemType, int quantity)
	{
		int ret;
		
		if(quantity == itemType.getMaxStackQuantity())
		{
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
			default: ret = -1;
			}
		}
		else
		{
			switch(itemType.getName())
			{
			case DIAMOND: ret = DIAMOND_VALUE/64*quantity;
			break;
			case EMERALD: ret = EMERALD_VALUE/64*quantity;
			break;
			default: ret = -1;
			}
		}
		
		return ret;
	}
	
}
