package net.aethermol.commands;

import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.tileentity.CommandBlock;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import net.aethermol.Messenger;
import net.aethermol.trade.Trade;

public class TradeBuyCommand implements CommandExecutor
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
			
				ItemType itemType = wishedItemCB(src);
				
				Trade.buy(player, itemType);
			}
			else
			{
				Messenger.getLogger().info("Command via CommandBlock failed: "+src.toString()+" Is the range set correctly?");
				return CommandResult.empty();
			}
		}
		
		return CommandResult.success();
	}

	private ItemType wishedItemCB(CommandSource src)
	{
		ItemType ret = null;
		
		CommandBlock cb = (CommandBlock) src;
		World world = cb.getWorld();
		Location<World> location = cb.getLocation().add(0, 3, 0);
		
		BlockType block = world.getBlockType(location.getBlockX(), location.getBlockY(), location.getBlockZ());
		
		switch(block.getName())
		{
		case "minecraft:coal_block": ret = ItemTypes.COAL;
		break;
		case "minecraft:iron_block": ret = ItemTypes.IRON_INGOT;
		break;
		case "minecraft:emerald_block": ret = ItemTypes.EMERALD;
		break;
		case "minecraft:redstone_block": ret = ItemTypes.REDSTONE;
		break;
		case "minecraft:diamond_block": ret = ItemTypes.DIAMOND;
		break;
		}
		
		return ret;
	}
	
}
