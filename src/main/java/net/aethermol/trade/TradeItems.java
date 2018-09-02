package net.aethermol.trade;

import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;

public enum TradeItems
{
	COAL, IRON, EMERALD, REDSTONE, DIAMOND, LAPIS, QUARTZ, GLOWSTONE_DUST, CLAY_BALL;
	
	//TODO fix return type
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
			//FIXME because of this:
		case LAPIS:
			ret = ItemTypes.DYE;
			break;
		case QUARTZ:
			ret = ItemTypes.QUARTZ;
			break;
		case GLOWSTONE_DUST:
			ret = ItemTypes.GLOWSTONE_DUST;
			break;
		case CLAY_BALL:
			ret = ItemTypes.CLAY_BALL;
			break;
		default: ret = ItemTypes.NONE;
		}
		
		return ret;
	}
	
}
