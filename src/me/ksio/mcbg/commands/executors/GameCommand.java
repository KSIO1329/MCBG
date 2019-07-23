package me.ksio.mcbg.commands.executors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ksio.mcbg.Core;
import me.ksio.mcbg.GameManager;

public class GameCommand implements CommandExecutor{

	Core plugin;
	public GameCommand(){
		this.plugin = Core.getInstance();
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("game")){
			if (args.length > 0){
				GameManager gm = GameManager.getInstance();
				String args0 = args[0].toLowerCase();
				switch (args0){
				case "add":{
					if (args.length > 1){
						if (isInt(args[1])){
						int id = getInt(args[1]);
						if (gm.addGame(id, "testgame"))
						return true;
						}
						else return false;
					}
					return false;
				}
				case "join":{
					if (!(sender instanceof Player)) return false;
					if (args.length == 1){
						if (gm.getNumberOfGames() > 0) gm.requestJoin((Player)sender);
					}
					if (args.length > 1){
						if (isInt(args[1])){
							int id = getInt(args[1]);
							if (gm.requestJoin(gm.getGame(id), (Player)sender))
							return true;
							else return false;
							}
							else return false;
					}
					return true;
				}
				case "stop":{
					if (sender instanceof Player){
						if (args.length == 1){
						Player p = (Player) sender;
						if (gm.isPlaying(p)){
							gm.getGame(p).stop();
						}
						}
					}
					return true;
				}
				case "forcestop":{
					if (sender instanceof Player){
						if (args.length == 1){
						Player p = (Player) sender;
						if (gm.isPlaying(p)){
							gm.getGame(p).stop(true);
						}
						}
					}
					return true;
				}
				case "forceload":{
					if (args.length > 1){
						if (isInt(args[1])){
						int id = getInt(args[1]);
						if (gm.getGame(id) != null) gm.getGame(id).forceLoad();
						return true;
						}
						else return false;
					}
					return false;
				}
				case "load":{
					if (args.length > 1){
						if (isInt(args[1])){
						int id = getInt(args[1]);
						if (gm.getGame(id) != null) {gm.getGame(id).softLoad();
						
						return true;
						}
						}
						else return false;
					}
					return false;
				}
				case "start":{
					if (args.length > 1){
						if (isInt(args[1])){
						int id = getInt(args[1]);
						if (gm.getGame(id) != null) {gm.getGame(id).start();
						
						return true;
						}
						}
						else return false;
					}
					return false;
				}
				}
			}
			
			
			
			
			
			
			
			
		}	
		return false;
	
	}
	int getInt(String s){
		return Integer.valueOf(s);
	}
	boolean isInt(String s){
		try{
			Integer.valueOf(s);
			return true;
		} catch (Exception e){
			return false;
		}
	}
	
}
