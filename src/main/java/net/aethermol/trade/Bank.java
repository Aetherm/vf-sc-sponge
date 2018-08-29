package net.aethermol.trade;

import org.spongepowered.api.entity.living.player.Player;

import net.aethermol.database.DatabaseOperations;

public class Bank
{
	public static void deposit(DatabaseOperations dbo, Player player, int amount)
	{	
		dbo.updateGold(player.getUniqueId(), amount);
	}
	
	public static boolean withdraw(DatabaseOperations dbo, Player player, int amount)
	{
		int balance = dbo.fetchGold(player.getUniqueId());
		
		if(balance >= amount)
		{
			dbo.updateGold(player.getUniqueId(), -amount);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static int fetchBalance(DatabaseOperations dbo, Player player)
	{
		int balance = dbo.fetchGold(player.getUniqueId());
		
		return balance;
	}
}
