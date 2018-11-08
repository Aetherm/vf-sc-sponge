package com.github.aetherm.vfscsponge.database;

import javax.sql.DataSource;

public class DatabaseConnection
{
	private DataSource dataSource;
	
	private String username;
	private String password;
	private String dbName;
	
	private boolean connected;
	
	private String dbURL;
	
	public DatabaseConnection(String username, String password, String dbName)
	{
		this.username = username;
		this.password = password;
		this.dbName = dbName;
		
		dbURL = "jdbc:h2:tcp://localhost/./database/"+dbName;
	}
	
	public String getDBURL()
	{
		return dbURL;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public String getPassword()
	{
		return password;
	}
	
	public DataSource getDataSource()
	{
		return dataSource;
	}

	public String getDbName()
	{
		return dbName;
	}

	public boolean isConnected()
	{
		return connected;
	}
	
}
