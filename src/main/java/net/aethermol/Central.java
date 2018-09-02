package net.aethermol;

import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.World;

import com.google.common.collect.ImmutableMap;
import net.aethermol.commands.CustomCommands;
import net.aethermol.database.DatabaseConnection;
import net.aethermol.database.DatabaseControl;
import net.aethermol.database.DatabaseOperations;

import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameConstructionEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.TimeZone;
import java.util.UUID;

import org.slf4j.LoggerFactory;

@Plugin(id = "vf-sc", name = "VF Server Control", version = "0.2.1", description = "Holds the world together")

public class Central
{	
	@SuppressWarnings("unused")
	private static Messenger messenger;
	private static CustomWorldBorder customWorldBorder;
	
	private static DatabaseControl dbctrl;
	private static DatabaseConnection dbc;
	private static DatabaseOperations dbo;
	
	private final static boolean DEBUG = false;
	
	public void onConstruction(GameConstructionEvent event)
	{
		//setting the JVM time
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}
	
	@Listener
    public void onServerStart(GameStartedServerEvent event)
	{	
		messenger = new Messenger(new CustomLogger(LoggerFactory.getLogger("vf-sc")));
		
		//if debug then accessing a remote database
		if(!DEBUG)
		{	
			//accessing the embedded h2 database
			dbctrl = new DatabaseControl();
			dbctrl.startDatabase();
		}
			
		Server server = null;
		
		dbc = new DatabaseConnection("vfsc-plugin", "bosskitty", "VFSDB");
		dbo = new DatabaseOperations(dbc);
		
		dbo.tryCreateTable();
		
		if(!dbo.isTablePresent())
		{	
			Messenger.getLogger().error("Database not found! Shutting down the server!");
			Sponge.getServer().shutdown();
		}
		else
		{
			Messenger.getLogger().info("Connected to Database!");
			
			if(Sponge.isServerAvailable())
			{
				server = Sponge.getServer();
			}
			//unlikely error but, hey
			else
			{
				//what could I do anyway?
				Messenger.getLogger().error("Server not present!");
			}
			
			Optional<World> optWorld = server.getWorld("world");
			
			World world = null;
			if(optWorld.isPresent())
			{
				world = optWorld.get();
			}
			//unlikely error, same here
			else
			{
				Messenger.getLogger().error("Optional World not present!");
			}
				
			customWorldBorder = new CustomWorldBorder(world);
			CustomCommands.instantiate();
		}
    }
	
	@Listener
	public void onStoppingEvent(GameStoppingEvent event)
	{
		if(!DEBUG)
		{
			dbctrl.stopDatabase();
		}
	}
	
	@Listener
	public void onJoin(ClientConnectionEvent.Join event)
	{
		ZonedDateTime zdt = ZonedDateTime.now(ZoneOffset.UTC);
		
		Player player = event.getTargetEntity();
		String serverName = "VF-Server-beta";
		
		Instant firstPlayed = player.firstPlayed().get();
	    Instant lastPlayed = player.lastPlayed().get();
		
	    Text message;
	    
	    if(firstPlayed.getEpochSecond() == lastPlayed.getEpochSecond())
	    {
	    	message = Messenger.FIRST_JOIN_MESSAGE.apply(ImmutableMap.of(
	                "server", Text.of(serverName), "player", Text.of(player.getName())
	        )).build();
	    }
	    else
	    {
		    message = Messenger.JOIN_MESSAGE.apply(ImmutableMap.of(
	                "server", Text.of(serverName), "player", Text.of(player.getName())
	        )).build();
	    }
	    
        event.setMessage(message);
        
        dbo.addPlayerIfNotPresent(player.getUniqueId());
        
        dbo.updateLastJoin(player.getUniqueId(), zdt);
        //player name gets updated each time they join
        dbo.updatePlayerName(player.getUniqueId(), player.getName());
	}
	
	public static CustomWorldBorder getCustomWorldBorder()
	{
		return customWorldBorder;
	}
	
	public static DatabaseOperations getDatabaseOperations()
	{
		return dbo;
	}
	
	public static Optional<User> getUser(UUID uuid)
	{
	    Optional<UserStorageService> userStorage = Sponge.getServiceManager().provide(UserStorageService.class);
	    return userStorage.get().get(uuid);
	}
}
