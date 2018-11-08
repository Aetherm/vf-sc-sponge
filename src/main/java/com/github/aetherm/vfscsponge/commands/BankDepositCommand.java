package com.github.aetherm.vfscsponge.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.github.aetherm.vfscsponge.core.Central;
import com.github.aetherm.vfscsponge.core.Messenger;
import com.github.aetherm.vfscsponge.trade.Bank;

public class BankDepositCommand implements CommandExecutor
{
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
	{
		if(src instanceof Player)
		{
			 src.sendMessage(Text.of("Command not available for players!"));
		}
		else if(src instanceof ConsoleSource) {
		    src.sendMessage(Text.of("Command not available on the console!"));
		}
		else if(src instanceof CommandBlockSource)
		{	
			Player player;
			if(args.<Player>getOne(Text.of("player")).isPresent())
			{
				player = args.<Player>getOne(Text.of("player")).get();
				
				int balance = Bank.fetchBalance(Central.getDatabaseOperations(), player);
				int amount = 0;
				
				if(player.getItemInHand(HandTypes.MAIN_HAND).isPresent())
				{
					ItemStack itemStack = player.getItemInHand(HandTypes.MAIN_HAND).get();
					if(itemStack.getType() == ItemTypes.GOLD_INGOT)
					{
						amount = itemStack.getQuantity()*9;
						
						player.setItemInHand(HandTypes.MAIN_HAND, ItemStack.empty());
						Bank.deposit(Central.getDatabaseOperations(), player, amount);
						
						player.sendMessage(Text.of("Your old balance was: ", TextColors.YELLOW, balance));
						player.sendMessage(Text.of("Your new balance is: ", TextColors.GREEN, Bank.fetchBalance(Central.getDatabaseOperations(), player)));
					}
					else if(itemStack.getType() == ItemTypes.GOLD_NUGGET)
					{
						amount = itemStack.getQuantity();
						
						player.setItemInHand(HandTypes.MAIN_HAND, ItemStack.empty());
						Bank.deposit(Central.getDatabaseOperations(), player, amount);
						
						player.sendMessage(Text.of("Your old balance was: ", TextColors.YELLOW, balance));
						player.sendMessage(Text.of("Your new balance is: ", TextColors.GREEN, Bank.fetchBalance(Central.getDatabaseOperations(), player)));
					}
					else if(itemStack.getType() == ItemTypes.GOLD_BLOCK)
					{
						amount = itemStack.getQuantity()*81;
						
						player.setItemInHand(HandTypes.MAIN_HAND, ItemStack.empty());
						Bank.deposit(Central.getDatabaseOperations(), player, amount);
						
						player.sendMessage(Text.of("Your old balance was: ", TextColors.YELLOW, balance));
						player.sendMessage(Text.of("Your new balance is: ", TextColors.GREEN, Bank.fetchBalance(Central.getDatabaseOperations(), player)));
					}
					else
					{
						player.sendMessage(Text.of("You need at least one Gold nugget in your hand!"));
					}
				}
				else
				{
					player.sendMessage(Text.of("You need some Gold in your hand!"));
				}
			}
			else
			{
				Messenger.getLogger().info("Command via CommandBlock failed: "+src.toString()+" Is the range set correctly?");
				return CommandResult.empty();
			}
			
		}
		return CommandResult.success();
	}
}
