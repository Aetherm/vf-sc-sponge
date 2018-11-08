package com.github.aetherm.vfscsponge.commands;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

import com.github.aetherm.vfscsponge.trade.TradeItems;

public class CustomCommands
{
	public static void instantiate()
	{
			CommandSpec extendBorder = CommandSpec.builder()
			    .description(Text.of("Extends the world border in exchange for emeralds."))
			    .extendedDescription
			    (
			    		Text.of("The amount you sacrifice is linear to the extension of the WORLD BORDER.\n[ext = emerald * 2]")
			    )
			    .executor(new ExtendBorderCommand())
			    .arguments(GenericArguments.integer(Text.of("emeralds")))
			    .build();

			Sponge.getCommandManager().register(Sponge.getPluginManager().getPlugin("vf-sc").get(), extendBorder, "extend-border");
			
			CommandSpec extendInfo = CommandSpec.builder()
				    .description(Text.of("General WORLD BORDER extension information."))
				    .executor(new ExtendInfoCommand())
				    .build();

			Sponge.getCommandManager().register(Sponge.getPluginManager().getPlugin("vf-sc").get(), extendInfo, "extend-info");
			
			CommandSpec tradeBuy = CommandSpec.builder()
				    .description(Text.of("Buy stuff with gold!"))
				    .permission("vfsc.command.tradeBuy")
				    .executor(new TradeBuyCommand())
				    .arguments(GenericArguments.player(Text.of("player")))
				    .build();

			Sponge.getCommandManager().register(Sponge.getPluginManager().getPlugin("vf-sc").get(), tradeBuy, "trade-buy");
			
			CommandSpec tradeSell = CommandSpec.builder()
				    .description(Text.of("Sell stuff for gold!"))
				    .permission("vfsc.command.tradeSell")
				    .executor(new TradeSellCommand())
				    .arguments(GenericArguments.player(Text.of("player")))
				    .build();

			Sponge.getCommandManager().register(Sponge.getPluginManager().getPlugin("vf-sc").get(), tradeSell, "trade-sell");
			
			CommandSpec tradeInfoText = CommandSpec.builder()
				    .description(Text.of("Displays the value of an item!"))
				    .executor(new TradeInfoCommand())
				    .arguments(GenericArguments.enumValue(Text.of("item name"), TradeItems.class))
				    .build();

			Sponge.getCommandManager().register(Sponge.getPluginManager().getPlugin("vf-sc").get(), tradeInfoText, "trade-info");
			
			CommandSpec bankDeposit = CommandSpec.builder()
				    .description(Text.of("Deposit money!"))
				    .permission("vfsc.command.bankDeposit")
				    .executor(new BankDepositCommand())
				    .arguments(GenericArguments.player(Text.of("player")))
				    .build();

			Sponge.getCommandManager().register(Sponge.getPluginManager().getPlugin("vf-sc").get(), bankDeposit, "bank-deposit");
			
			CommandSpec bankWithdraw = CommandSpec.builder()
				    .description(Text.of("Withdraw money!"))
				    .permission("vfsc.command.bankWithdraw")
				    .executor(new BankWithdrawCommand())
				    .arguments(GenericArguments.player(Text.of("player")))
				    .build();

			Sponge.getCommandManager().register(Sponge.getPluginManager().getPlugin("vf-sc").get(), bankWithdraw, "bank-withdraw");
			
			CommandSpec bankInfo = CommandSpec.builder()
				    .description(Text.of("Displays your balance!"))
				    .executor(new BankInfoCommand())
				    .build();

			Sponge.getCommandManager().register(Sponge.getPluginManager().getPlugin("vf-sc").get(), bankInfo, "bank-info");
			
			//--------------------------------------------------
			//!!!for dungeon world testing only!!!
			//--------------------------------------------------
//			CommandSpec dungeonEnter = CommandSpec.builder()
//				    .description(Text.of("Entering a dungeon"))
//				    .executor(new DungeonEnterCommand())
//				    .arguments(GenericArguments.string(Text.of("world name")))
//				    .build();
//
//			Sponge.getCommandManager().register(Sponge.getPluginManager().getPlugin("vf-sc").get(), dungeonEnter, "dungeon-enter");
	}
	
}
