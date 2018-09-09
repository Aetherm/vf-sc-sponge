package net.aethermol.commands;

import java.util.Optional;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import net.aethermol.trade.TradeItems;

public class TradeInfoCommand implements CommandExecutor
{
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
	{
		if(src instanceof Player)
		{
			Player player = (Player) src;
			ItemStack sampleStack = null;
			
			Optional<TradeItems> optTradeItem = args.<TradeItems>getOne(Text.of("item name"));
			
			if(optTradeItem.isPresent())
			{
				TradeItems tradeItem = optTradeItem.get();
				
				sampleStack = tradeItem.getSampleStack();
			
				ItemStack buyStack = ItemStack.builder().from(sampleStack).quantity(1).build();
				ItemStack sellStack = ItemStack.builder().from(sampleStack).quantity(1).build();
				
				int buyValue;
				int sellValue;
				
				if(tradeItem.singlyTradable())
				{
					buyValue = tradeItem.getSinglyBuyValue();
					sellValue = tradeItem.getSinglySellValue();
				}
				else
				{
					buyValue = tradeItem.getStackBuyValue();
					sellValue = tradeItem.getStackSellValue();
				}
				
				if(buyValue >= 0)
				{
					player.sendMessage(Text.of("You can buy ", TextColors.YELLOW , buyStack.getQuantity(), " ", tradeItem.getItemName(), 
										TextColors.NONE," for ", TextColors.YELLOW, buyValue, TextColors.NONE, " Gold!"));
				}
				else
				{
					player.sendMessage(Text.of("You can't buy ", TextColors.YELLOW, tradeItem.getItemName(), TextColors.NONE, "!"));
				}
				
				if(sellValue >= 0)
				{
					player.sendMessage(Text.of("You can sell ", TextColors.YELLOW, sellStack.getQuantity(), " ", tradeItem.getItemName().toString(),
							TextColors.NONE, " for ",TextColors.YELLOW, sellValue, TextColors.NONE, " Gold!"));
				}
				else
				{
					player.sendMessage(Text.of("You can't sell ", TextColors.YELLOW, tradeItem.getItemName(), TextColors.NONE, "!"));
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
