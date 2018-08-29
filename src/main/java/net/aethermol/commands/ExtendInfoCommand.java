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
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import net.aethermol.Central;

public class ExtendInfoCommand implements CommandExecutor
{
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
	{
		if(src instanceof Player)
		{
			Player player = (Player) src;
			
			int ownExtend = Central.getDatabaseOperations().fetchExtend(player.getUniqueId());
			int topExtend = Central.getDatabaseOperations().fetchGreatestExtend();
			Optional<User> optTopUser = Central.getUser(Central.getDatabaseOperations().fetchUUIDWithExtend(topExtend));
			
			if(optTopUser.isPresent())
			{
				String name = optTopUser.get().getName();
				
				src.sendMessage(Text.of("The border is now ",TextColors.YELLOW, (int) Central.getCustomWorldBorder().getBorderWide(), TextColors.NONE, " blocks wide."));
				src.sendMessage(Text.of("The diameter grew ",TextColors.YELLOW, (int) Central.getCustomWorldBorder().getAddedBorderWide(), TextColors.NONE, " blocks so far!"));
				
				src.sendMessage(Text.of("Your extension is: ", TextColors.YELLOW, ownExtend));
				src.sendMessage(Text.of("The most extend was done by ",TextColors.AQUA, name, TextColors.NONE, ": ", TextColors.YELLOW, topExtend, TextColors.NONE, "!" ));
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
