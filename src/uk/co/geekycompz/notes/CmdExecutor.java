package uk.co.geekycompz.notes;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

public class CmdExecutor implements CommandExecutor{

	SimpleNotes plugin;
	
	CmdExecutor(SimpleNotes plugin){
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		String prefix = plugin.configFile.getString("Prefix");
		
		File PlayersFolder = new File(plugin.getDataFolder(), "Players");
		File PlayerNotes = new File(plugin.getDataFolder() + File.separator + "Players", sender.getName() + ".yml");
		YamlConfiguration playernotesyml = YamlConfiguration.loadConfiguration(PlayerNotes);
		
		// If the players notes file does not exist, this will create it
		if(!PlayerNotes.exists()){
			try{
				plugin.getDataFolder().mkdir();
				PlayersFolder.mkdir();
				PlayerNotes.createNewFile();
			}catch(IOException e){
				// If fails, send the console the following message
				plugin.getLogger().severe("[Notes] Couldn't create a notes file for " + sender.getName() + "!");
			}
		}
		
		
		// Checks if '/notes reload' is executed and reloads the plugin
		if(args.length == 1 && args[0].equalsIgnoreCase("reload")){
			if(sender.hasPermission("notes.reload")){
				// This reloads the config file
				plugin.onEnable();
				plugin.reloadConfig();
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + " §aSuccessfully Reloaded Configuration Files.");
			}else{
				// If no permission send this message
				sender.sendMessage("§cYou do not have permission.");
			}
		}else if(args.length >=2 && args[0].equalsIgnoreCase("reload")){
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + " §4§lUsage: §c/notes reload");
		}
		
		
		// Checks if the the sub-command is wrong and sends a usage message
		if(args.length >= 1){
			if(!(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?") || args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("view") || args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("reload"))){
				if(sender.hasPermission("notes.help")){
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + " §4§lError: §cThe command '§o" + args[0] + "§c' could not be found.");
				}else{
					sender.sendMessage("§cYou do not have permission.");
				}
			}
		}
		
		
		// Checks if '/notes help' or '/notes ?' is executed
		if(args.length == 1 && args[0].equalsIgnoreCase("help") || args.length == 1 && args[0].equalsIgnoreCase("?") || args.length == 0 || args[0] == null){
			if(sender.hasPermission("notes.help")){
				// Displays the 'Notes' help menu to the player
				sender.sendMessage("§f§o=-=-=-=-=-=-=-=-=-=-=-§6§lNotes§f§o-=-=-=-=-=-=-=-=-=-=-=");
				sender.sendMessage("§5/notes help §f- §dDisplays the help menu for 'Notes'.");
				sender.sendMessage("§5/notes reload §f- §dReloads the entire plugin.");
				sender.sendMessage("§5/notes list §f- §dShows a list of all your notes.");
				sender.sendMessage("§5/notes view §f- §dView a note from your account.");
				sender.sendMessage("§5/notes add §f- §dAdd a note to your account.");
				sender.sendMessage("§5/notes delete §f- §dDelete a note from your account.");
				sender.sendMessage("§e§o<> §f- §dThis states that it is required.");
				sender.sendMessage("§e§o[] §f- §dThis states that it is optional.");
				sender.sendMessage("§f§o=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
			}else{
				sender.sendMessage("§cYou do not have permission.");
			}
		}
		
		
		// Checks if '/notes list [player]' is executed
		if(args.length == 2 && args[0].equalsIgnoreCase("list")){
			if(sender.hasPermission("notes.list.others")){
				File ListDesiredPlayer = new File(plugin.getDataFolder() + File.separator + "Players", args[1] + ".yml");
				YamlConfiguration listdesiredplayeryml = YamlConfiguration.loadConfiguration(ListDesiredPlayer);
				if(ListDesiredPlayer.exists()){
					// Loads all of the notes from the player that was chosen
					String allnotes = listdesiredplayeryml.getConfigurationSection("").getKeys(false).toString();
				
					if(allnotes.equalsIgnoreCase("[]")){
						// Tells the executor that the player has no notes
						sender.sendMessage("§f§o=-=-=-=-=-=-=-=-=§6§lYourNotes§f§o=-=-=-=-=-=-=-=-=");
						sender.sendMessage("§cThis Player Currently Has No Notes.");
						sender.sendMessage("§f§o=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
					}else{
						// Sends all of the notes from the desired player to the executor
						sender.sendMessage("§f§o=-=-=-=-=-=-=-=-=§6§lYourNotes§f§o=-=-=-=-=-=-=-=-=");
						sender.sendMessage("§e" + allnotes.toString());
						sender.sendMessage("§f§o=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
					}
				}else{
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + " §cThe player '§o" + args[2] + "§c' does not exist.");
				}
			}else{
				sender.sendMessage("§cYou do not have permission.");
			}
		}
		
		
		// Checks if '/notes list' is executed
		if(args.length == 1 && args[0].equalsIgnoreCase("list")){
			if(sender.hasPermission("notes.list")){
				// Loads all of the executor's note titles
				String allnotes = playernotesyml.getConfigurationSection("").getKeys(false).toString();
				
				if(allnotes.equalsIgnoreCase("[]")){
					// Sends a message that tells them they have no notes
					sender.sendMessage("§f§o=-=-=-=-=-=-=-=-=§6§lYourNotes§f§o=-=-=-=-=-=-=-=-=");
					sender.sendMessage("§cYou Currently Have No Notes, §o'/notes add'.");
					sender.sendMessage("§f§o=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
				}else{
					// Sends all of the executor's note titles to them
					sender.sendMessage("§f§o=-=-=-=-=-=-=-=-=§6§lYourNotes§f§o=-=-=-=-=-=-=-=-=");
					sender.sendMessage("§e" + allnotes.toString());
					sender.sendMessage("§f§o=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
				}
			}else{
				sender.sendMessage("§cYou do not have permission.");
			}
		}
		if(args.length >=3 && args[0].equalsIgnoreCase("list")){
			if(sender.hasPermission("notes.list")){
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + " §4§lUsage: §c/notes list [player]");
			}else{
				sender.sendMessage("§cYou do not have permission.");
			}
		}
		
		
		// Checks if '/notes view <title> [player]' is executed
		if(args.length == 3 && args[0].equalsIgnoreCase("view")){
			if(sender.hasPermission("notes.view.others")){
				// Gets the desired players notes file
				File ViewDesiredPlayer = new File(plugin.getDataFolder() + File.separator + "Players", args[2] + ".yml");
				YamlConfiguration viewdesiredplayeryml = YamlConfiguration.loadConfiguration(ViewDesiredPlayer);
				if(ViewDesiredPlayer.exists()){
					// Checks if the note exists
					if(viewdesiredplayeryml.contains(args[1])){
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + " §f" + args[1] + "§8: §7" + viewdesiredplayeryml.getString(args[1]));
					}else{
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + " §cThe note you requested does not exist in §o" + args[2] + " §caccount!");
					}
				}else{
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + " §cThe player '§o" + args[2] + "§c' does not exist.");
				}
			}else{
				sender.sendMessage("§cYou do not have permission.");
			}
		}
		
		
		// Checks if '/notes view <title>' is executed
		if(args.length == 2 && args[0].equalsIgnoreCase("view")){
			if(sender.hasPermission("notes.view")){
				// Checks if the note exists
				if(playernotesyml.contains(args[1])){
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + " §f" + args[1] + "§8: §7" + playernotesyml.getString(args[1]));
				}else{
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + " §cThe note you requested does not exist in your account!");
				}
			}else{
				sender.sendMessage("§cYou do not have permission.");
			}
		}
		// Checks if '/notes view' is executed and sends a usage message
		if(args.length == 1 && args[0].equalsIgnoreCase("view") || args.length >=4 && args[0].equalsIgnoreCase("view")){
			if(sender.hasPermission("notes.view")){
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + " §4§lUsage: §c/notes view <title> [player]");
			}else{
				sender.sendMessage("§cYou do not have permission.");
			}
		}
		
		
		// Checks if '/notes add <title> <message>' is executed
		if(args.length >= 3 && args[0].equalsIgnoreCase("add")){
			if(sender.hasPermission("notes.add")){
				// Links all the arguments from the message into one
				StringBuilder sb = new StringBuilder();
				for (int i = 2; i < args.length; i++){
					sb.append(args[i]);
					if (i<args.length-1)
						sb.append(" ");
				}
			
				String title = args[1];
				String message = sb.toString(); // Sets the message to a string and is linked to the variable 'message'
				// Saves the note to the players yml file
				playernotesyml.set(title, message);
			
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + " §2Successfully added the note '§a" + args[1] + "§2'.");
				
				// Saves the players note file
				try {
					playernotesyml.save(PlayerNotes);
				} catch (IOException e) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + " §4§lError: §cCould not save §o" + sender.getName() + " §c's notes file.");
				}
			}else{
				sender.sendMessage("§cYou do not have permission.");
			}
		}
		
		
		// Checks if '/notes add <title>' or '/notes add' is executed and sends a usage message
		if(args.length == 2 && args[0].equalsIgnoreCase("add") || args.length == 1 && args[0].equalsIgnoreCase("add")){
			if(sender.hasPermission("notes.add")){
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + " §4§lUsage: §c/notes add <title> <message>");
			}else{
				sender.sendMessage("§cYou do not have permission.");
			}
		}
		
		
		// Checks if '/notes delete <note> [player]' is executed
		if(args.length == 3 && args[0].equalsIgnoreCase("delete")){
			if(sender.hasPermission("notes.delete.others")){
				File DeleteDesiredPlayer = new File(plugin.getDataFolder() + File.separator + "Players", args[2] + ".yml");
				YamlConfiguration deletedesiredplayeryml = YamlConfiguration.loadConfiguration(DeleteDesiredPlayer);
				if(DeleteDesiredPlayer.exists()){
					if(deletedesiredplayeryml.contains(args[1])){
						String title = args[1];
						// Removes the key (note) from the players yml file
						deletedesiredplayeryml.set(title, null);
						
						// Saves the stated players note file
						try {
							deletedesiredplayeryml.save(DeleteDesiredPlayer);
						} catch (IOException e) {
							sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + " §4§lError: §cCould not save §o" + sender.getName() + " §c's notes file.");
						}
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + " §2Successfully deleted the note '§a" + title + "§2' from §o" + args[2] + "§2's account.");
					}else{
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + " §cThat note does not exist in §o" + args[2] + "§c's account!");
					}
				}else{
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + " §cThe player '§o" + args[2] + "§c' does not exist.");
				}
			}else{
				sender.sendMessage("§cYou do not have permission.");
			}
		}
		
		
		// Checks if '/notes delete <note>' is executed
		if(args.length == 2 && args[0].equalsIgnoreCase("delete")){
			if(sender.hasPermission("notes.delete")){
				if(playernotesyml.contains(args[1])){
					String title = args[1];
				
					// Removes the key (note) from the players yml file
					playernotesyml.set(title, null);
			
					// Saves the players note file
					try {
						playernotesyml.save(PlayerNotes);
					} catch (IOException e) {
						sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + " §4§lError: §cCould not save §o" + sender.getName() + " §c's notes file.");
					}
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + " §2Successfully deleted the note '§a" + title + "§2'.");
				}else{
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + " §cThat note does not exist in your account!");
				}
			}else{
				sender.sendMessage("§cYou do not have permission.");
			}
		}
		// Checks if '/notes delete' is executed and sends a usage message
		if(args.length == 1 && args[0].equalsIgnoreCase("delete") || args.length >=4 && args[0].equalsIgnoreCase("delete")){
			if(sender.hasPermission("notes.delete")){
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix) + " §4§lUsage: §c/notes delete <note> [player]");
			}else{
				sender.sendMessage("§cYou do not have permission.");
			}
		}
		return true;
	}
}
