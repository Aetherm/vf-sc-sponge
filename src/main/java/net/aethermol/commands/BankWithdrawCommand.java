package net.aethermol.commands;

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

import net.aethermol.Central;
import net.aethermol.Messenger;
import net.aethermol.trade.Bank;

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
					int withdraw = 64;
					int balance = Bank.fetchBalance(Central.getDatabaseOperations(), player);
					
					if(balance > 0)
					{
						if(balance < withdraw)
						{
							withdraw = balance;
						}
					
						Bank.withdraw(Central.getDatabaseOperations(), player, withdraw);
						player.setItemInHand(HandTypes.MAIN_HAND, ItemStack.builder().itemType(ItemTypes.GOLD_NUGGET).quantity(withdraw).build());
						
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
