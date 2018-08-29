package net.aethermol.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import net.aethermol.Central;
import net.aethermol.trade.Bank;

public class BankInfoCommand implements CommandExecutor
{
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
	{
		if(src instanceof Player)
		{
			Player player = (Player) src;
			
			int balance = Bank.fetchBalance(Central.getDatabaseOperations(), player);
			
			player.sendMessage(Text.of("Your balance: ",TextColors.GREEN, balance));
			
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
