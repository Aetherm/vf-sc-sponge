package net.aethermol.database;

import java.sql.SQLException;

import org.h2.tools.Server;
import net.aethermol.Messenger;

public class DatabaseControl
{
	private Server server;
	
	public DatabaseControl()
	{
		try
		{
			server = Server.createTcpServer("-tcpAllowOthers");
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public void startDatabase()
	{
		// start the TCP Server
		try
		{
			server.start();
			Messenger.getLogger().info("Database started!\nDB Port: "+server.getPort());
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public void stopDatabase()
	{
		// stop the TCP Server
		server.stop();
	}
	
	
}
