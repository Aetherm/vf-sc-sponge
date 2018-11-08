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
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import com.github.aetherm.vfscsponge.core.Central;
import com.github.aetherm.vfscsponge.core.Messenger;
import com.github.aetherm.vfscsponge.trade.Bank;

public class BankWithdrawCommand implements CommandExecutor
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
			
			
				if(!player.getItemInHand(HandTypes.MAIN_HAND).isPresent())
				{
					int withdraw = 0;
					int balance = Bank.fetchBalance(Central.getDatabaseOperations(), player);
					
					ItemType goldType;
					int creditGoldType = 0;
					
					if(balance > 0)
					{
						//check if credit is enough for nuggets...
						if(balance <= 64)
						{
							goldType = ItemTypes.GOLD_NUGGET;
							creditGoldType = balance;
							withdraw = creditGoldType;
						}
						else
						{
							//...or for ingots...
							{
								goldType = ItemTypes.GOLD_INGOT;
								creditGoldType = balance / 9;
								withdraw = creditGoldType * 9;
							}
							//...or even blocks!
							if(balance > 576)
							{
								goldType = ItemTypes.GOLD_BLOCK;
								creditGoldType = balance / 81;
								withdraw = creditGoldType * 81;
							}
						}
						
					
						Bank.withdraw(Central.getDatabaseOperations(), player, withdraw);
						player.setItemInHand(HandTypes.MAIN_HAND, ItemStack.builder().itemType(goldType).quantity(creditGoldType).build());
						
						player.sendMessage(Text.of("Your old balance was: ", TextColors.YELLOW, balance));
						player.sendMessage(Text.of("Your new balance is: ", TextColors.GREEN, Bank.fetchBalance(Central.getDatabaseOperations(), player)));
					}
					else
					{
						player.sendMessage(Text.of("Hmm? Seems like there is no Gold in there! D:"));
					}
				}
				else
				{
					player.sendMessage(Text.of("You must empty your main hand first!"));
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
