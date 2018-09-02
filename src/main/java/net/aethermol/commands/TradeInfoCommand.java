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
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
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
			ItemType itemType = null;
			
			Optional<TradeItems> tradeItem = args.<TradeItems>getOne(Text.of("item name"));
			
			if(tradeItem.isPresent())
			{
				itemType = TradeItems.getItemType(tradeItem.get());
			
				ItemStack buyStack = ItemStack.builder().itemType(itemType).quantity(1).build();
				ItemStack sellStack = ItemStack.builder().itemType(itemType).quantity(1).build();
				
				int buyValue = Value.getBuyValue(buyStack);
				int sellValue = Value.getSellValue(sellStack);
				
				if(buyValue == -1)
				{
					buyStack = ItemStack.builder().itemType(itemType).quantity(64).build();
					buyValue = Value.getBuyValue(buyStack);
				}
				if(sellValue == -1)
				{
					sellStack = ItemStack.builder().itemType(itemType).quantity(64).build();
					sellValue = Value.getSellValue(sellStack);
				}
				
				if(buyValue >= 0)
				{
					player.sendMessage(Text.of("You can buy ", TextColors.YELLOW , buyStack.getQuantity(), " ", itemType.getTranslation().get(Locale.US), 
										TextColors.NONE," for ", TextColors.YELLOW, buyValue, TextColors.NONE, " Gold!"));
				}
				else
				{
					player.sendMessage(Text.of("You can't buy ", TextColors.YELLOW, itemType.getTranslation().get(Locale.US), TextColors.NONE, "!"));
				}
				
				if(sellValue >= 0)
				{
					player.sendMessage(Text.of("You can sell ", TextColors.YELLOW, sellStack.getQuantity(), " ", itemType.getTranslation().get(Locale.US),
							TextColors.NONE, " for ",TextColors.YELLOW, sellValue, TextColors.NONE, " Gold!"));
				}
				else
				{
					player.sendMessage(Text.of("You can't sell ", TextColors.YELLOW, itemType.getTranslation().get(Locale.US), TextColors.NONE, "!"));
				}
			
			}
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
