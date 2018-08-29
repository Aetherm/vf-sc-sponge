package net.aethermol.commands;

import java.util.Locale;
import java.util.Optional;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import net.aethermol.trade.TradeItems;
import net.aethermol.trade.Value;

public class TradeInfoCommand implements CommandExecutor
{
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
	{
		if(src instanceof Player)
		{
			Player player = (Player) src;
			ItemStack handStack;
			ItemType itemType = null;
			
			Optional<TradeItems> tradeItem = args.<TradeItems>getOne(Text.of("item name"));
			
			if(tradeItem.isPresent())
			{
				itemType = TradeItems.getItemType(tradeItem.get());
			}
			
			if(player.getItemInHand(HandTypes.MAIN_HAND).isPresent() || itemType != null)
			{
				handStack = player.getItemInHand(HandTypes.MAIN_HAND).orElse(ItemStack.empty());
				
				//check if valid itemType already exists through args
				if(itemType == ItemTypes.NONE || itemType == null)
				{
					itemType = handStack.getType();
				}
				
				int quantity;
				
				//TEMPORARY
				if(itemType.equals(ItemTypes.DIAMOND) || itemType.equals(ItemTypes.EMERALD))
				{
					quantity = 1;
				}
				else
				{
					quantity = itemType.getMaxStackQuantity();
				}
				
				int buyValue = Value.getBuyValue(itemType, quantity);
				int sellValue = Value.getSellValue(itemType, quantity);
				
				//TODO
				if(buyValue >= 0)
				{
					player.sendMessage(Text.of("You can buy ", TextColors.YELLOW , quantity, " ", itemType.getTranslation().get(Locale.US), 
										TextColors.NONE," for ", TextColors.YELLOW, buyValue, TextColors.NONE, " Gold!"));
				}
				else
				{
					player.sendMessage(Text.of("You can't buy ", TextColors.YELLOW, itemType.getTranslation().get(Locale.US), TextColors.NONE, "!"));
				}
				
				if(sellValue >= 0)
				{
					player.sendMessage(Text.of("You can sell ", TextColors.YELLOW, quantity, " ", itemType.getTranslation().get(Locale.US),
							TextColors.NONE, " for ",TextColors.YELLOW, sellValue, TextColors.NONE, " Gold!"));
				}
				else
				{
					player.sendMessage(Text.of("You can't sell ", TextColors.YELLOW, itemType.getTranslation().get(Locale.US), TextColors.NONE, "!"));
				}
			}
			else
			{
				player.sendMessage(Text.of("You need an item in your main hand!"));
			}
			
			itemType = null;
		}
		else if(src instanceof ConsoleSource) {
		    src.sendMessage(Text.of("Command not available on the console!"));
		}
		else if(src instanceof CommandBlockSource)
		{
			src.sendMessage(Text.of("Command not available for command block!"));
		}
		
		return CommandResult.success();
	}
}
