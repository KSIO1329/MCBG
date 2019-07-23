package me.ksio.mcbg.permissions;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PermissionManager {

	private static PermissionManager instance = new PermissionManager();
	
	private PermissionManager(){
		instance = this;
	}
	
	public static PermissionManager getInstance(){
		return instance;
	}
	
	public boolean hasPermission(CommandSender s, String perm){
		if (s instanceof Player){
			Player p = (Player)s;
			return p.hasPermission(perm);
		}
		return true;
	}
	public String missingPermission(Player p, String[] args){
		String base = "mcbg.";
		switch (args[0].toLowerCase()){
		case "config":{
			base+="config." + args[1];
			break;
		}
		default: {
			base+=args[0];
		}
		}
		return base.toUpperCase();
	}
}
