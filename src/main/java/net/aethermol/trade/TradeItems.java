package net.aethermol.trade;

import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;

public enum TradeItems
{
	COAL, IRON, EMERALD, REDSTONE, DIAMOND;
	
	public static ItemType getItemType(TradeItems tradeItem)
	{
		ItemType ret;
		
		switch(tradeItem)
		{
		case COAL:
			ret = ItemTypes.COAL;
			break;
		case IRON:
			ret = ItemTypes.IRON_INGOT;
			break;
		case EMERALD:
			ret = ItemTypes.EMERALD;
			break;
		case REDSTONE:
			ret = ItemTypes.REDSTONE;
			break;
		case DIAMOND:
			ret = ItemTypes.DIAMOND;
			break;
		default: ret = ItemTypes.NONE;
		}
		
		return ret;
	}
}
