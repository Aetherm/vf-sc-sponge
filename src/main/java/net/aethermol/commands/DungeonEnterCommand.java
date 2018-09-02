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
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.World;

import net.aethermol.dungeon.DungeonWorld;

public class DungeonEnterCommand implements CommandExecutor
{
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
	{
		if(src instanceof Player)
		{
			Player player = (Player) src;
			World world;
			
			Optional<String> worldName = args.<String>getOne(Text.of("world name"));
			if(worldName.isPresent())
			{
			
				world = DungeonWorld.createWorld(worldName.get());
				
				player.transferToWorld(world);
				
				player.sendMessage(Text.of("Send to world: ",TextColors.YELLOW, world.getName()));
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
