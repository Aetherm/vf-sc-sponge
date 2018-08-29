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
import org.spongepowered.api.text.Text;

import net.aethermol.Messenger;
import net.aethermol.trade.Trade;

public class TradeSellCommand implements CommandExecutor
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
			
				if(!player.getItemInHand(HandTypes.OFF_HAND).isPresent())
				{
					if(player.getItemInHand(HandTypes.MAIN_HAND).isPresent())
					{
						Trade.sell(player);
					}
					else
					{
						player.sendMessage(Text.of("You need something to sell in your hand!"));
					}
				}
				else
				{
					player.sendMessage(Text.of("Your off hand needs to be empty!"));
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
