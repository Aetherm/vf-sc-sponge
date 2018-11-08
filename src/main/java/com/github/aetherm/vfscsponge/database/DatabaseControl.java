package com.github.aetherm.vfscsponge.database;

import java.sql.SQLException;

import org.h2.tools.Server;

import com.github.aetherm.vfscsponge.core.Messenger;

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
