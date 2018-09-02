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
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
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
				
				ItemStack itemStack = wishedItemCB(src);
				
				Trade.buy(player, itemStack);
			}
			else
			{
				Messenger.getLogger().info("Command via CommandBlock failed: "+src.toString()+" Is the range set correctly?");
				return CommandResult.empty();
			}
		}
		
		return CommandResult.success();
	}
	
	private ItemStack wishedItemCB(CommandSource src)
	{
		ItemStack ret = null;
		
		CommandBlock cb = (CommandBlock) src;
		World world = cb.getWorld();
		Location<World> location = cb.getLocation().add(0, 3, 0);
		
		BlockType block = world.getBlockType(location.getBlockX(), location.getBlockY(), location.getBlockZ());
		
		//THIS PART IS JUST TEMPORARY
		//until I decide how players can set the item quantity to buy, ordinary items can only be bought in full stacks!
		switch(block.getName())
		{
		case "minecraft:coal_block": ret = ItemStack.builder().itemType(ItemTypes.COAL).quantity(64).build();
		break;
		case "minecraft:iron_block": ret = ItemStack.builder().itemType(ItemTypes.IRON_INGOT).quantity(64).build();
		break;
		case "minecraft:emerald_block": ret = ItemStack.builder().itemType(ItemTypes.EMERALD).quantity(1).build();
		break;
		case "minecraft:redstone_block": ret = ItemStack.builder().itemType(ItemTypes.REDSTONE).quantity(64).build();
		break;
		case "minecraft:diamond_block": ret = ItemStack.builder().itemType(ItemTypes.DIAMOND).quantity(1).build();
		break;
		case "minecraft:lapis_block": ret = ItemStack.builder().itemType(ItemTypes.DYE).add(Keys.DYE_COLOR, DyeColors.BLUE).quantity(64).build();
		break;
		case "minecraft:quartz_block": ret = ItemStack.builder().itemType(ItemTypes.QUARTZ).quantity(64).build();
		break;
		case "minecraft:glowstone": ret = ItemStack.builder().itemType(ItemTypes.GLOWSTONE_DUST).quantity(64).build();
		break;
		case "minecraft:clay": ret = ItemStack.builder().itemType(ItemTypes.CLAY_BALL).quantity(64).build();
		break;
		}
		
		return ret;
	}
	
}
