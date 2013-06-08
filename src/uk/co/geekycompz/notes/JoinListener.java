package uk.co.geekycompz.notes;

import java.io.File;
import java.io.IOException;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener{
	
	SimpleNotes plugin;
	
	JoinListener(SimpleNotes plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		// Gets the players username who joined
		String player = event.getPlayer().getName();
		
		File PlayersFolder = new File(plugin.getDataFolder(), "Players");
		File PlayerNotes = new File(plugin.getDataFolder() + File.separator + "Players", player + ".yml");
		
		// If the players notes file does not exist, this will create it
		if(!PlayerNotes.exists()){
			try{
				plugin.getDataFolder().mkdir();
				PlayersFolder.mkdir();
				PlayerNotes.createNewFile();
			}catch(IOException e){
				// If fails, send the console the following message
				plugin.getLogger().severe("[Notes] Couldn't create a notes file for " + player + "!");
			}
		}
	}
	
}
