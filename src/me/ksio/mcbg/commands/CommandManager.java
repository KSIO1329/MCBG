package me.ksio.mcbg.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class CommandManager { 

	private static CommandManager instance = new CommandManager();
	private CommandManager(){
		instance = this;
	}
	public static CommandManager getInstance(){
		return instance;
	}
	
	public VerifiedNode getNode(String label, String[] args){
		List<Node> fromRoot = Node.getAllCommands(label);
		List<VerifiedNode> verified = new ArrayList<VerifiedNode>();
		if (fromRoot == null) return null;
		//List<Commands> cmds = Commands.getAllCommands(Commands.MCBG.getBase() + ".command.give");
		for (Node c : fromRoot){
			VerifiedNode node = new VerifiedNode(c, Validation.VALID);
			//c.setValid(Validation.VALID);
			String[] splits = c.getBase().split(":");
			if (splits.length != args.length+1) continue;
			//int nodeL = splits.length - 2;   
			boolean notFound = false;
			for (int i = 1; i < args.length+1; i++){
					if (splits[i].equalsIgnoreCase(args[i-1])) continue;
					if (splits[i].contains("%")){
						if (isValid(splits[i], args[i-1]) == Validation.VALID) continue;
						if (node.getValidation() == Validation.VALID)
						node.setValdiation(isValid(splits[i], args[i-1]));
					}
					notFound = true;
					break;				
			}
			if (notFound) {
				verified.add(node); 
				continue;
			}
			if (node.getValidation() == Validation.VALID){
				return node;
			}
		}
		if (verified.size() > 0) {
			return verified.get(0);
		}
		return null; 
	}
	private String getMessage(CommandSender sender, Node a, boolean b, String[] args){
		String msg = a.getMessage();
		if (!b){
			msg = msg.replaceAll("@.*?@", "");
			//msg = msg.replaceAll("#", "");
		} else {
			//msg = msg.replaceAll("#.*?#", "");
			msg = msg.replaceAll("@", "");
		}
		String node = a.getBase();
		//if (msg.contains("%i"))  
		msg = msg.replaceAll("%i", getItem(node, args).toUpperCase());
		//if (msg.contains("%s"))
		msg = msg.replace("%s", sender.getName());
		//if (msg.contains("%p"))
		msg = msg.replace("%p", getReciever(sender, node, args).getName());
		//if (msg.contains("%a"))
		msg = msg.replace("%a", getAmount(node, args) + "");
		//if (msg.contains("%b"))
		msg = msg.replace("%b", getBoolPrefix(getBoolean(node, args)));
		msg = msg.replace("%ob", getOffBoolPrefix(getOffBool(node, args)));
		msg = ChatColor.translateAlternateColorCodes('&', msg);
		return msg;
	}
	public void sendMessage(CommandSender sender,  String[] args, Node a, String prefix){
		String node = a.getBase();
		Player reciever = getReciever(sender, node, args);
		if (sender.equals(reciever)) sender.sendMessage(prefix + getMessage(sender, a, false, args));
		else{
			sender.sendMessage(prefix + getMessage(sender, a, false, args));
			reciever.sendMessage(prefix + getMessage(sender, a, true, args));
		}
	}
	String getBoolPrefix(boolean a){
		if (a) return ChatColor.GREEN + "TRUE";
		else return ChatColor.RED + "FALSE";
	}
	String getOffBoolPrefix(offbool a){
		if (a == offbool.TRUE) return ChatColor.GREEN + "TRUE"; 
		else if (a == offbool.FALSE) return ChatColor.RED + "FALSE";
		else return ChatColor.WHITE + "OFF";
	}
	public Validation isValid(String par, String arg){
			switch(par){
			case "%a":{
				if (!isInt(arg)) return Validation.INVALID_AMOUNT;
				break;
				}
			case "%p":{
				if (!isOnline(arg)) return Validation.INVALID_PLAYER;
				break;  
			}
			case "%b":{
				if (!isBoolean(arg)) return Validation.INVALID_BOOL;
			}
			case "%ob":{
				if (!isOffBoolean(arg)) return Validation.INVALID_OFFBOOL;
			}
		}
		return Validation.VALID;
	}
	public Player getReciever(CommandSender sender, String node, String[] args){
		String name = getObject(node, args, "%p");
		if (!name.equals(""))
		return Bukkit.getPlayer(name);
		return (Player)sender;
	}
	public String getItem(String node, String[] args){
		return getObject(node, args, "%i");
	}
	public int getAmount(String node, String[] args){
		try{
		return Integer.valueOf(getObject(node,args,"%a"));
		} catch (Exception e){
			return 1;
		}
	}
	public offbool getOffBool(String node, String[] args){
		String o = getObject(node, args, "%ob");
		if (isBoolean(o)) if (o.equalsIgnoreCase("true")) return offbool.TRUE; else return offbool.FALSE;
		else return offbool.OFF;
	}
	public boolean getBoolean(String node, String[] args){
		return Boolean.valueOf(getObject(node,args, "%b"));
	}
	public String getPermission(String node){
		String perm;
		String[] split = node.split(":");
		perm = split[0] + ".command";
		for (int i = 1; i < split.length; i++){
			if (!split[i].contains("%"))
			perm+="."+split[i];
		}
		return perm;
	}
	boolean isInt(String s){
		try {
			@SuppressWarnings("unused")
			int a = Integer.valueOf(s);
			return true;
		} catch (Exception e){
			return false;
		}
	}
	boolean isOnline(String s){
		for (Player p : Bukkit.getOnlinePlayers()){
			if (p.getName().equalsIgnoreCase(s)) return true;
		}
		return false;
	}
	boolean isBoolean(String s){
		if (s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false")) return true;
		return false;
	} 
	boolean isOffBoolean(String s){
		return isBoolean(s) || s.equalsIgnoreCase("off");
	}
	String getObject(String node, String[] args, String par){
		String[] splits = node.split(":");
		for (int i = 1; i < splits.length; i++){
			if (splits[i].equals(par)) return args[i-1];
		}
		return "";
	}
	
}
