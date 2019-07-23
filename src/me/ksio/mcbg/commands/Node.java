package me.ksio.mcbg.commands;

import java.util.ArrayList;
import java.util.List;


public enum Node {
	GET_ITEM("mcbg:get:%i", "", "&aSuccessfully given &f%i&a."),
	GET_ITEM_AMOUNT("mcbg:get:%i:%a", "", "&aSuccessfully given &f%ix%a&a."),
	GIVE_PLAYER_ITEM_AMOUNT("mcbg:give:%p:%i:%a", "", "@&c%s&8 > @&aSuccessfully given &f%ix%a&a to &e%p&a."),
	GIVE_PLAYER_ITEM("mcbg:give:%p:%i", "", "@&c%s&8 > @&aSuccessfully given &f%i&a to &e%p&a."),
	SKINSEL_ITEM("mcbg:skinsel:%i","", "&aSuccessfully selected &f%i &aas the currently used skin."), 
	ALLSKINS_OFFBOOL("mcbg:allskins:%ob","", "&7Set the personal &fALLSKINS&7 mode to %ob&7."),
	NONEWS_BOOL("mcbg:nonews:%b","", ""),
	CONFIG_RELOAD("mcbg:config:reload","", ""),
	CONFIG_SAVE("mcbg:config:save","", ""),
	OPALLSKINS_BOOL("mcbg:opallskins:%b","", ""),
	SKINADD_ITEM("mcbg:skinadd:%i","", "&aAdded &f%i &ato your skins."),
	SKINADD_ITEM_AMOUNT("mcbg:skinadd:%i:%a","", ""),
	SKINADD_PLAYER_ITEM("mcbg:skinadd:%p:%i","", ""),
	SKINADD_PLAYER_ITEM_AMOUNT("mcbg:skinadd:%p:%i:%a","",""),
	GUNS_RELOAD("mcbg:guns:reload", "" ,"&aReloaded all guns from config file.")
	;
	
	private String base; 
	private String usage;
	private String success;
	Node(String base, String usage, String success){
		this.base= base;
		this.usage = usage;
		this.success = success;
	}
	public String getBase(){
		return base;
	}
	public String value(){
		return base;
	}
	public String getMessage(){
		return success;
	}
	public static List<Node> getAllCommands(String node){
		List<Node> cmds = new ArrayList<Node>(); 
		for (Node c :Node.values()){ 
			//if (!c.getBase().contains(".")) continue;
			String[] splits = c.getBase().split(":"); 
			if (splits[0].equalsIgnoreCase(node)) 
				cmds.add(c);
		}
		if (cmds.size() > 0) return cmds;
		return null;
	}
	public String getUsage(){
		return usage;
	}
	public static Node getFromNode(String node){
		for (Node c :Node.values()){ 
			if (c.getBase().equalsIgnoreCase(node)) 
				return c;
		}
		return null;
	}
}
