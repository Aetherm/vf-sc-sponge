package net.aethermol.trade;

import java.util.Locale;

import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import net.aethermol.Central;

public class Trade
{
	public static void sell(Player player)
	{
		ItemStack itemStack = player.getItemInHand(HandTypes.MAIN_HAND).get();
		ItemType itemType = itemStack.getType();
		
		int quantity = itemStack.getQuantity();
		
		int value;
		
		value = Value.getSellValue(itemType, quantity);
			
		if(value > 0)
		{
			player.sendMessage(Text.of("Selling ", TextColors.YELLOW, quantity," ", itemStack.getType().getTranslation().get(Locale.US), TextColors.NONE, " for ",
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
	
	public static void buy(Player player, ItemType itemType)
	{
		//this variable will late determinate the buying quantity
		final int wantedQuantity = 1;
		
		boolean present = player.getItemInHand(HandTypes.MAIN_HAND).isPresent();
		int quantity = 0;
		int remainingStackSpace = 0;
		boolean sameItem = false;
		ItemStack handStack;
		
		if(present)
		{
			handStack = player.getItemInHand(HandTypes.MAIN_HAND).get();
			
			sameItem = itemType.equals(handStack.getType()) ? true : false;
			
			if(sameItem)
			{
				quantity = handStack.getQuantity();
				remainingStackSpace = itemType.getMaxStackQuantity() - handStack.getQuantity();
			}
				
		}
		
		if(!present || sameItem && remainingStackSpace >= wantedQuantity)
		{
			int value;
			
			if(itemType != ItemTypes.DIAMOND && itemType != ItemTypes.EMERALD)
			{
				//THIS PART IS JUST TEMPORARY
				//until I decide how players can set the item quantity to buy, ordinary items can only be bought in full stacks!
				
				value = Value.getBuyValue(itemType, 64);
				//check if item is purchasable
				if(value > 0)
				{
					//checking for enough gold
					if(value <= Central.getDatabaseOperations().fetchGold(player.getUniqueId()))
					{
						player.sendMessage(Text.of("Buying ", TextColors.YELLOW, 64," ", itemType.getTranslation().get(Locale.US), TextColors.NONE, " for ",
								TextColors.YELLOW, value, TextColors.NONE, " Gold!"));
						
						Central.getDatabaseOperations().updateGold(player.getUniqueId(), -value);
						player.setItemInHand(HandTypes.MAIN_HAND, ItemStack.of(itemType, 64));
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
				//THIS PART IS JUST TEMPORARY
				//until I decide how players can set the item quantity to buy, rare items can only be bought singly
				
				value = Value.getBuyValue(itemType, wantedQuantity);
				
				//checking for enough gold
				if(value <= Central.getDatabaseOperations().fetchGold(player.getUniqueId()))
				{
					player.sendMessage(Text.of("Buying ", TextColors.YELLOW, wantedQuantity," ", itemType.getTranslation().get(Locale.US), TextColors.NONE, " for ",
							TextColors.YELLOW, value, TextColors.NONE, " Gold!"));
					
					Central.getDatabaseOperations().updateGold(player.getUniqueId(), -value);
					
					player.setItemInHand(HandTypes.MAIN_HAND, ItemStack.of(itemType, quantity + wantedQuantity));
					
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
