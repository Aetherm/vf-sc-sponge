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

public class ExtendBorderCommand implements CommandExecutor
{
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
	{
		if(src instanceof Player)
		{
		    Player player = (Player) src;
		    int sacrAmount = args.<Integer>getOne("emeralds").get();
		    
		    if(sacrAmount > 0)
		    {
			    if(player.getItemInHand(HandTypes.MAIN_HAND).isPresent())
			    {
			    	int quantity = player.getItemInHand(HandTypes.MAIN_HAND).get().getQuantity();
			    	
			    	if(quantity >= sacrAmount)
			    	{
			    		double oldBorder = player.getWorld().getWorldBorder().getDiameter();
			    		double newBorder = oldBorder;
			    		int borderDifference;
			    		String singular = "EMERALD";
			    		String plural = "EMERALDS";
			    		
			    		String emerald = singular;
			    		if(sacrAmount > 1)
			    			emerald = plural;
			    		
			    		player.setItemInHand(HandTypes.MAIN_HAND, ItemStack.of(ItemTypes.EMERALD, quantity - sacrAmount));
			    		
			    		player.sendMessage(Text.of(TextColors.YELLOW,"WorldBorder extended in exchange for ",TextColors.YELLOW,sacrAmount,TextColors.DARK_GREEN,(" "+emerald),"!\n",
					    TextColors.NONE,"Old diameter: ",TextColors.YELLOW, oldBorder));
					    extendBorder(calcAmount(sacrAmount));
					    newBorder = player.getWorld().getWorldBorder().getDiameter();
					    player.sendMessage(Text.of(TextColors.NONE,"New diameter: ",TextColors.YELLOW, newBorder));
					    borderDifference = (int) (newBorder-oldBorder);
					    player.sendMessage(Text.of(TextColors.NONE,"Difference: ",TextColors.GREEN, borderDifference));
					    player.sendMessage(Text.of(TextColors.NONE,"Your added extend: ",TextColors.GREEN, borderDifference/2));
					    Central.getDatabaseOperations().updateExtend(player.getUniqueId(), borderDifference/2);
			    	}
			    	else
			    	{
			    		player.sendMessage(Text.of("You have not enough emeralds! D:"));
			    	}
			    }
			    else
			    {
			    	player.sendMessage(Text.of("You need emeralds in your hand!"));
			    }
		    }
		    else
		    {
		    	player.sendMessage(Text.of("The amount must be greater than zero!"));
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
	
	private int calcAmount(int quantity)
	{
		int ret = 0;
		if(quantity > 0)
		{
			ret = quantity*2;
		}
		return ret;
	}
	
	private void extendBorder(int amount)
	{
		Central.getCustomWorldBorder().extendBorder(amount);
	}
}
