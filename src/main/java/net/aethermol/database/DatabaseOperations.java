package net.aethermol.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZonedDateTime;
import java.util.UUID;

import org.spongepowered.api.Sponge;

import net.aethermol.Messenger;

public class DatabaseOperations
{
	private String dbURL;
	private String username;
	private String password;
	
	private static String tableName = "VFSTreasure";
	private static String playerName = "player_name";
	private static String playerUUID = "player_uuid";
	private static String playerGold = "gold";
	private static String playerExtend = "extend";
	private static String playerLastJoin = "last_join";
	
	private static String createTable = "CREATE TABLE "+tableName+" ("+playerName+" VARCHAR(16),"+playerUUID+" UUID, "+playerGold+" INT, "+playerExtend+" INT, "+playerLastJoin+" DATETIME)";
	
	public DatabaseOperations(DatabaseConnection dbc)
	{
		dbURL = dbc.getDBURL();
		username = dbc.getUsername();
		password = dbc.getPassword();
	}
	
	public DatabaseOperations(String dbURL, String username, String password)
	{
		this.dbURL = dbURL;
		this.username = username;
		this.password = password;
	}
	
	public void tryCreateTable()
	{
		String sql = createTable;
		
	    Connection conn;
	    Statement stmt;
		try
		{
			conn = DriverManager.getConnection(dbURL, username, password);
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);
			
			Messenger.getLogger().info("Created Table: "+tableName);
			
			conn.close();
			
		} catch (SQLException e)
		{
			if(e.getErrorCode() == 42101) //table already exists
			{
				Messenger.getLogger().info("Table "+tableName+" already exists!");
			}
			else
			{
				e.printStackTrace();
			}
		}
	}
	
	public void addPlayerIfNotPresent(UUID uuid)
	{
		if(!isUUIDPresent(uuid))
		{
			addPlayer(uuid);
			Messenger.getLogger().info(playerUUID+" was not present, created new entry in: "+tableName+"!");
		}
	}
	
	private void addPlayer(UUID uuid)
	{
		String name = "";
		
		if(Sponge.getServer().getPlayer(uuid).isPresent())
		{
			name = Sponge.getServer().getPlayer(uuid).get().getName();
		}
				
		String sql = "INSERT INTO "+tableName+" ("+playerName+","+playerUUID+","+playerGold+","+playerExtend+")"
					+ "VALUES ('"+name+"','"+uuid.toString()+"',"+0+","+0+")";
		
		Connection conn;
	    Statement stmt;
		try
		{
			conn = DriverManager.getConnection(dbURL, username, password);
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);

			conn.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public boolean isUUIDPresent(UUID uuid)
	{
		boolean ret = false;
		
		String sql = "SELECT "+playerUUID+" FROM "+tableName+" WHERE "+playerUUID+" = '"+uuid.toString()+"'";
		
			    try (
			    		Connection conn = DriverManager.getConnection(dbURL, username, password);
			    		
			        	PreparedStatement stmt = conn.prepareStatement(sql);
			         	ResultSet results = stmt.executeQuery();
			    	)
			    {
			        if(results.next())
			        {
			        	ret = true;
			        }
		
			    } catch (SQLException e)
			    {
					e.printStackTrace();
				}
			    
		return ret;
	}
	
	public void updatePlayerName(UUID uuid, String name)
	{			
		String sql = "UPDATE "+tableName+" SET "+playerName+" = '"+name+"' WHERE "+playerUUID+" = '"+uuid.toString()+"'";
		
		Connection conn;
	    Statement stmt;
		try
		{
			conn = DriverManager.getConnection(dbURL, username, password);
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);

			conn.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public String fetchPlayerName(UUID uuid)
	{
		String ret = "";
		
		String sql = "SELECT "+playerName+" FROM "+tableName+" WHERE "+playerUUID+" = '"+uuid.toString()+"'";
		
	    try (
	    		Connection conn = DriverManager.getConnection(dbURL, username, password);
	    		
	        	PreparedStatement stmt = conn.prepareStatement(sql);
	         	ResultSet results = stmt.executeQuery();
	    	)
	    {
	        while(results.next())
	        {
	        	ret = results.getString(1);
	        }

	    } catch (SQLException e)
	    {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	public void updateLastJoin(UUID uuid, ZonedDateTime zdt)
	{			
		String sql = "UPDATE "+tableName+" SET "+playerLastJoin+" = '"+zdt+"' WHERE "+playerUUID+" = '"+uuid.toString()+"'";
		
		Connection conn;
	    Statement stmt;
		try
		{
			conn = DriverManager.getConnection(dbURL, username, password);
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);

			conn.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public String fetchLastJoin(UUID uuid)
	{
		String ret = "";
		
		String sql = "SELECT "+playerLastJoin+" FROM "+tableName+" WHERE "+playerUUID+" = '"+uuid.toString()+"'";
		
	    try (
	    		Connection conn = DriverManager.getConnection(dbURL, username, password);
	    		
	        	PreparedStatement stmt = conn.prepareStatement(sql);
	         	ResultSet results = stmt.executeQuery();
	    	)
	    {
	        while(results.next())
	        {
	        	ret = results.getString(1);
	        }

	    } catch (SQLException e)
	    {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	public void updateExtend(UUID uuid, int change)
	{
		int extend = fetchExtend(uuid) + change;
		
		String sql = "UPDATE "+tableName+" SET "+playerExtend+" = "+extend+" WHERE "+playerUUID+" = '"+uuid.toString()+"'";
		
		Connection conn;
	    Statement stmt;
		try
		{
			conn = DriverManager.getConnection(dbURL, username, password);
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);

			conn.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public UUID fetchUUIDWithExtend(int extend)
	{
		UUID ret = null;
		
		String sql = "SELECT "+playerUUID+" FROM "+tableName+" WHERE "+playerExtend+" = "+ extend;
		
	    try (
	    		Connection conn = DriverManager.getConnection(dbURL, username, password);
	    		
	        	PreparedStatement stmt = conn.prepareStatement(sql);
	         	ResultSet results = stmt.executeQuery();
	    	)
	    {
	        while(results.next())
	        {
	        	ret = UUID.fromString(results.getString(1));
	        }

	    } catch (SQLException e)
	    {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	public int fetchGreatestExtend()
	{
		int ret = -1;
		
		String sql = "SELECT MAX ("+playerExtend+") FROM "+tableName;
		
	    try (
	    		Connection conn = DriverManager.getConnection(dbURL, username, password);
	    		
	        	PreparedStatement stmt = conn.prepareStatement(sql);
	         	ResultSet results = stmt.executeQuery();
	    	)
	    {
	        while(results.next())
	        {
	        	ret = results.getInt(1);
	        }

	    } catch (SQLException e)
	    {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	public int fetchExtend(UUID uuid)
	{
		int ret = -1;
		
		String sql = "SELECT "+playerExtend+" FROM "+tableName+" WHERE "+playerUUID+" = '"+uuid.toString()+"'";
		
	    try (
	    		Connection conn = DriverManager.getConnection(dbURL, username, password);
	    		
	        	PreparedStatement stmt = conn.prepareStatement(sql);
	         	ResultSet results = stmt.executeQuery();
	    	)
	    {
	        while(results.next())
	        {
	        	ret = results.getInt(1);
	        }

	    } catch (SQLException e)
	    {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	public void updateGold(UUID uuid, int change)
	{
		int gold = fetchGold(uuid) + change;
		
		String sql = "UPDATE "+tableName+" SET "+playerGold+" = "+gold+" WHERE "+playerUUID+" = '"+uuid.toString()+"'";
		
		Connection conn;
	    Statement stmt;
		try
		{
			conn = DriverManager.getConnection(dbURL, username, password);
			stmt = conn.createStatement();
			stmt.executeUpdate(sql);

			conn.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public int fetchGold(UUID uuid)
	{
		int ret = -1;
		
		String sql = "SELECT "+playerGold+" FROM "+tableName+" WHERE "+playerUUID+" = '"+uuid.toString()+"'";
		
	    try (
	    		Connection conn = DriverManager.getConnection(dbURL, username, password);
	    		
	        	PreparedStatement stmt = conn.prepareStatement(sql);
	         	ResultSet results = stmt.executeQuery();
	    	)
	    {
	        while(results.next())
	        {
	        	ret = results.getInt(1);
	        }

	    } catch (SQLException e)
	    {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	//I tried other things but everything failed, so...
	public boolean isTablePresent()
	{
		//rookie testing if table is accessible
		boolean ret = true;
		
		String sql = "SELECT 1 FROM "+tableName+";";
		
	    try (
	    		Connection conn = DriverManager.getConnection(dbURL, username, password);
	    		
	        	PreparedStatement stmt = conn.prepareStatement(sql);
	         	ResultSet results = stmt.executeQuery();
	    	)
	    {

	    } catch (SQLException e)
	    {
			ret = false;
		}
		
		return ret;
	}
	
}
