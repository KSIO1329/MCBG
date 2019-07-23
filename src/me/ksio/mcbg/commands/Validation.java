package me.ksio.mcbg.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;


public enum Validation {
	INVALID_PLAYER("&cInvalid player name!"),
	INVALID_ITEM(""),
	INVALID_AMOUNT("&cInvalid amount, please use numbers!"),
	INVALID_BOOL("&cInvalid boolean, please use &aTRUE&c or &4FALSE&c!"),
	INVALID_OFFBOOL("&cInvalid boolean, please use &aTRUE&c, &4FALSE&c or &7OFF&c!"),
	VALID("");
	
	String msg;
	Validation(String s){
		msg = s;
	}
	public void sendMessage(CommandSender sender, String prefix){
		sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', msg));
	}
	public void setMessage(String s){
		msg = s;
	}
}
