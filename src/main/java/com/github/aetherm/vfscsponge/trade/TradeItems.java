package com.github.aetherm.vfscsponge.trade;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStack.Builder;

public enum TradeItems
{
	VOID("void", -1, -1, false),
	COAL("coal", 10, 10, false),
	IRON("iron", 58, 58, false),
	EMERALD("emerald", 704, -1, true),
	REDSTONE("redstone", 20, 20, false),
	DIAMOND("diamond", 384, 384, true),
	LAPISLAZULI("lapislazuli", 61, 61, false),
	QUARTZ("quartz", 54, 54, false),
	GLOWSTONE_DUST("glowstone dust", 128, -1, true),
	CLAY_BALL("clay ball", 16, 4, false);
	
	private String name;
	private int buyValue;
	private int sellValue;
	private boolean singlyTradable;
	
	private TradeItems(String name, int buyValue, int sellValue, boolean singlyTrade)
	{
		this.name = name;
		this.buyValue = buyValue;
		this.sellValue = sellValue;
		this.singlyTradable = singlyTrade;
	}
	
	public ItemStack getSampleStack()
	{
		ItemStack ret;
		
		switch(this)
		{
		case COAL:
			ret = ItemStack.builder().itemType(ItemTypes.COAL).build();
			break;
		case IRON:
			ret = ItemStack.builder().itemType(ItemTypes.IRON_INGOT).build();
			break;
		case EMERALD:
			ret = ItemStack.builder().itemType(ItemTypes.EMERALD).build();
			break;
		case REDSTONE:
			ret = ItemStack.builder().itemType(ItemTypes.REDSTONE).build();
			break;
		case DIAMOND:
			ret = ItemStack.builder().itemType(ItemTypes.DIAMOND).build();
			break;
		case LAPISLAZULI:
			ret = ItemStack.builder().itemType(ItemTypes.DYE).add(Keys.DYE_COLOR, DyeColors.BLUE).build();
			break;
		case QUARTZ:
			ret = ItemStack.builder().itemType(ItemTypes.QUARTZ).build();
			break;
		case GLOWSTONE_DUST:
			ret = ItemStack.builder().itemType(ItemTypes.GLOWSTONE_DUST).build();
			break;
		case CLAY_BALL:
			ret = ItemStack.builder().itemType(ItemTypes.CLAY_BALL).build();
			break;
		default:
			ret = ItemStack.builder().itemType(ItemTypes.NONE).build();
		}
		
		return ret;
	}
	
	public static TradeItems getFromStack(ItemStack itemStack)
	{
		TradeItems ret = null;
		
		Value<DyeColor> color = itemStack.getValue(Keys.DYE_COLOR).orElse(null);
		
		//a stack builder containing the item type of the argument
		Builder argumentSampleBuilder = ItemStack.builder().itemType(itemStack.getType());
		
		//filling the builder with more values
		if(color != null)
		{
			argumentSampleBuilder.add(Keys.DYE_COLOR, color.getDirect().get());
		}
		
		//and getting the corresponding stack
		ItemStack argumentSampleStack = argumentSampleBuilder.build();
		
		//iterating over all available TradeItems
		for(TradeItems tradeItem : TradeItems.values())
		{
			
			ItemStack compareStack = tradeItem.getSampleStack();
			
			if(compareStack.toString().equals(argumentSampleStack.toString()))
			{
				ret = tradeItem;
			}
		}
		
		if(ret == null)
		{
			ret = VOID;
		}
		
		return ret;		
	}
	
	public String getItemName()
	{
		return name;
	}
	
	public int getSinglyBuyValue()
	{
		if(buyValue != -1)
			return buyValue / this.getSampleStack().getMaxStackQuantity();
		else
			return -1;
	}
	
	public int getSinglySellValue()
	{
		if(sellValue != -1)
			return sellValue / this.getSampleStack().getMaxStackQuantity();
		else
			return -1;
	}
	
	public int getStackBuyValue()
	{
		return buyValue;
	}
	
	public int getStackSellValue()
	{
		return sellValue;
	}
	
	public boolean singlyTradable()
	{
		return singlyTradable;
	}
}
