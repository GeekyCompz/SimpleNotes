package uk.co.geekycompz.notes;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleNotes extends JavaPlugin{

	public Logger logger = Logger.getLogger("Minecraft");
	public FileConfiguration configFile;
	
	@Override
	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " Has Been Disabled!");
		saveDefaultConfig();
	}
	
	@Override
	public void onEnable() {
		// Creates the plugin folder directory
		String pluginFolder = this.getDataFolder().getAbsolutePath();
    	(new File(pluginFolder)).mkdirs();
		
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info(pdfFile.getName() + " Version " + pdfFile.getVersion() + " Has Been Enabled!"); 
		
		// Creates the CONSOLE notes file if not already exists
		createConsoleNotesFile();
		
		// Creates and sets the config file
		this.configFile = getConfig();
		this.configFile.options().copyHeader(true);
	    this.configFile.options().copyDefaults(true);
	    saveDefaultConfig();
	    // Updates the custom 'Prefix'
		String prefixer = this.configFile.getString("Prefix");
		this.configFile.set("Prefix", prefixer);
		
		// Allows '/notes' to be executed and enabled the JoinListener class
		this.getCommand("notes").setExecutor(new CmdExecutor(this));
		Bukkit.getPluginManager().registerEvents(new JoinListener(this), this);
	}
	
	// This creates the CONSOLE notes file if createConsoleNotesFile() is used (if not already exists)
	public void createConsoleNotesFile() {
		File PlayersFolder = new File(getDataFolder(), "Players");
		File CONSOLENotes = new File(getDataFolder() + File.separator + "Players", "CONSOLE.yml");
		
		// If the players notes file does not exist, this will create it
		if(!CONSOLENotes.exists()){
			try{
				getDataFolder().mkdir();
				PlayersFolder.mkdir();
				CONSOLENotes.createNewFile();
			}catch(IOException e){
				// If fails, send the console the following message
				getLogger().severe("[Notes] Couldn't create a notes file for the CONSOLE!");
			}
		}
	}
	
}
