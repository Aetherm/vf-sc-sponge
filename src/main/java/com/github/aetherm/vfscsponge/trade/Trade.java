package com.github.aetherm.vfscsponge.trade;

import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.github.aetherm.vfscsponge.core.Central;

public class Trade
{
	public static void sell(Player player)
	{
		ItemStack sellItemStack = player.getItemInHand(HandTypes.MAIN_HAND).get();
		String sellItemName = TradeItems.getFromStack(sellItemStack).getItemName();
		
		int quantity = sellItemStack.getQuantity();
		
		int value;
		
		value = TradeItems.getFromStack(sellItemStack).getStackSellValue();
		
		TradeItems sellTradeItem = TradeItems.getFromStack(sellItemStack);
		
		if(sellTradeItem.singlyTradable())
		{
			value = sellTradeItem.getSinglySellValue()*quantity;
		}
		else
		{
			//check for a full stack in hand
			if(quantity == sellItemStack.getMaxStackQuantity())
			{
				value = sellTradeItem.getStackSellValue();
			}
			else
			{
				value = 0;
			}
		}
		
		if(value > 0)
		{
			player.sendMessage(Text.of("Selling ", TextColors.YELLOW, quantity," ", sellItemName, TextColors.NONE, " for ",
									TextColors.YELLOW, value, TextColors.NONE, " Gold!"));
				
			Central.getDatabaseOperations().updateGold(player.getUniqueId(), value);
			player.setItemInHand(HandTypes.MAIN_HAND, ItemStack.empty());
				
			player.sendMessage(Text.of("Your new balance is: ", TextColors.GREEN, Bank.fetchBalance(Central.getDatabaseOperations(), player)));
		}
		else
		{
			player.sendMessage(Text.of("Your trade offer is not possible D:\nPlease try a full stack or another item!"));
		}
	}
	
	public static void buy(Player player, ItemStack wantedItemStack)
	{	
		ItemType wantedItemType = wantedItemStack.getType();
		String itemName = TradeItems.getFromStack(wantedItemStack).getItemName();
		
		boolean present = player.getItemInHand(HandTypes.MAIN_HAND).isPresent();
		int remainingStackSpace = 0;
		boolean sameItem = false;
		
		ItemStack handStack;
		int handQuantity = 0;
		
		//check if the player holds an item
		if(present)
		{	
			handStack = player.getItemInHand(HandTypes.MAIN_HAND).get();
			
			sameItem = wantedItemType.equals(handStack.getType()) ? true : false;
			
			//check if it is the same item as the wanted
			if(sameItem)
			{
				handQuantity = handStack.getQuantity();
				//getting the remaining space in the stack
				remainingStackSpace = wantedItemType.getMaxStackQuantity() - handStack.getQuantity();
			}
				
		}
		
		//check if the player holds no item or the item in hand has enough capacity and is the same item as the wanted
		if(!present || sameItem && remainingStackSpace >= wantedItemStack.getQuantity())
		{
			TradeItems wantedTradeItem = TradeItems.getFromStack(wantedItemStack);
			
			int value;
			
			if(wantedTradeItem.singlyTradable())
			{
				value = wantedTradeItem.getSinglyBuyValue();
			}
			else
			{
				value = TradeItems.getFromStack(wantedItemStack).getStackBuyValue();
			}
			
			//check if item is purchasable
			if(value > 0)
			{
				//checking for enough gold
				if(value <= Central.getDatabaseOperations().fetchGold(player.getUniqueId()))
				{
					player.sendMessage(Text.of("Buying ", TextColors.YELLOW, wantedItemStack.getQuantity()," ", itemName, TextColors.NONE, " for ",
							TextColors.YELLOW, value, TextColors.NONE, " Gold!"));
					
					Central.getDatabaseOperations().updateGold(player.getUniqueId(), -value);
					player.setItemInHand(HandTypes.MAIN_HAND, ItemStack.builder().from(wantedItemStack).quantity(handQuantity + wantedItemStack.getQuantity()).build());
					player.sendMessage(Text.of("Your new balance is: ", TextColors.GREEN, Bank.fetchBalance(Central.getDatabaseOperations(), player)));
				}
				else
				{
					player.sendMessage(Text.of("You have not enough Gold! D:"));
				}
			}

		}
		else
		{
			player.sendMessage(Text.of("Please empty your main hand first!"));
		}
	}
	
}
