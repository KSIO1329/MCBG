package me.ksio.mcbg.commands.executors;

import me.ksio.mcbg.Core;
import me.ksio.mcbg.commands.CommandManager;
import me.ksio.mcbg.commands.Node;
import me.ksio.mcbg.commands.Validation;
import me.ksio.mcbg.commands.VerifiedNode;
import me.ksio.mcbg.commands.offbool;
import me.ksio.mcbg.config.GunConfig;
import me.ksio.mcbg.guns.GunManager;
import me.ksio.mcbg.guns.SkinManager;
import me.ksio.mcbg.permissions.PermissionManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MainCommand implements CommandExecutor{

	Core plugin;
	public MainCommand(){
		this.plugin = Core.getInstance();
	}
	
	public static String prefix = ChatColor.DARK_GRAY + "[" + ChatColor.RED + "MC" + ChatColor.DARK_AQUA + "B" + ChatColor.GOLD + "G" + ChatColor.DARK_GRAY + "] ";
	public static String _positive = ChatColor.GREEN + "";
	public static String _negative = ChatColor.RED + "";
	public static String _neutral = ChatColor.GRAY + "";
	public static String _senderPrefix = ChatColor.RED + "";
	public static String _senderSuffix = ChatColor.DARK_GRAY + ChatColor.BOLD.toString() + " > ";
	public static String _reciever = ChatColor.YELLOW + "";
	public static String _item = ChatColor.WHITE + "";
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("mcbg")){
			if (args.length == 0) return false;
			CommandManager cm = CommandManager.getInstance();
			VerifiedNode vn = cm.getNode(label, args);
			if (vn == null) return false; 
			Node cmdn = vn.getNode();
			
			String node = cmdn.getBase();
			//
			if (!PermissionManager.getInstance().hasPermission(sender, cm.getPermission(node))) {
				sender.sendMessage(ChatColor.RED + "You do not have permission to use this command. Missing permission: " + _item + cm.getPermission(node) + _negative + ".");
				return true;
			}
			//
			if (vn.getValidation() != Validation.VALID) {
				vn.getValidation().sendMessage(sender, prefix);
				return true;
			} 
			//
			switch (Node.getFromNode(node)){
			case GET_ITEM:{
				if (sender instanceof Player){
					Player p = cm.getReciever(sender, node, args);
					String item = cm.getItem(node, args);
				if (GunManager.getInstance().validGun(item)){
					ItemStack i = GunManager.getInstance().getGun(item).getItem();
					p.getInventory().addItem(i);
					cm.sendMessage(sender, args, cmdn, prefix);
					//p.sendMessage(prefix + _positive + "Successfully given " + _item + item.toUpperCase() + _positive + ".");
					} else{
						p.sendMessage(prefix + _negative + "Invalid item name.");
					}
					return true;
				}
				break;
			}
			case GET_ITEM_AMOUNT:{
				if (sender instanceof Player){
					Player p = cm.getReciever(sender, node, args);
					String item = cm.getItem(node, args);
				if (GunManager.getInstance().validGun(item)){
					ItemStack i = GunManager.getInstance().getGun(item).getItem();
					int amount = cm.getAmount(node, args);
					for(int j = 0; j < amount; j++){
						p.getInventory().addItem(i);
					}
					cm.sendMessage(sender, args, cmdn, prefix);
					//p.sendMessage(prefix + _positive + "Successfully given " + _item + item.toUpperCase() + _positive + ".");
					} else{
						p.sendMessage(prefix + _negative + "Invalid item name.");
					}
					return true;
				}
				break;
			}
			case SKINSEL_ITEM:{
				if (sender instanceof Player){
					Player p = cm.getReciever(sender, node, args);
					String item = cm.getItem(node, args);
				if (SkinManager.Instance.selectSkin(p.getName(), item))
					cm.sendMessage(sender, args, cmdn, prefix);
					return true;
				}
				break;
			}
			case ALLSKINS_OFFBOOL:{
				if (sender instanceof Player){
					Player p = cm.getReciever(sender, node, args);
					offbool ob = cm.getOffBool(node, args);
				if (ob == offbool.TRUE || ob == offbool.FALSE){
					boolean b = Boolean.valueOf(ob.toString());
					plugin.config.allSkinsAdd(p.getName(), b);
					//cm.sendMessage(sender, args, cmdn, prefix);
					//p.sendMessage(prefix + _neutral + "Set the personal " + _item + "ALLSKINS" + _neutral + " mode to " + isPositive(b) + String.valueOf(b).toUpperCase() + _neutral + ".");
				}
				else
					plugin.config.allSkinsRemove(p.getName());
				cm.sendMessage(sender, args, cmdn, prefix);
				return true;
					//p.sendMessage(prefix + _neutral + "Removed personal setting for ALLSkins mode.");
				}
				break;
			}
			case NONEWS_BOOL:{
				if (sender instanceof Player){
					Player p = cm.getReciever(sender, node, args);
				boolean b = cm.getBoolean(node, args);
						plugin.config.noNewsSet(p.getName(), b);
						p.sendMessage(prefix + _neutral + "Your " + _item + "NONEWS" + _neutral + " mode has been set to " + isPositive(b) + String.valueOf(b).toUpperCase() + _neutral + ".");
				return true;
				}
				break;
			}
			case CONFIG_RELOAD:{
				sender.sendMessage(prefix + _neutral + "Reloading the configuration file...");
				if (plugin.config.Reload())
					sender.sendMessage(prefix + _positive + "Successfully reloaded the configuration file.");
				else
					sender.sendMessage(prefix + _negative + "Failed to reload the configuration file.");
				return true;
			}
			case CONFIG_SAVE:{
				sender.sendMessage(prefix + _neutral + "Saving the configuration file...");
				if (plugin.config.SaveConfig())
					sender.sendMessage(prefix + _positive + "Successfully saved the configuration file.");
				else
					sender.sendMessage(prefix + _negative + "Failed to save the configuration file.");
				return true;
			}
			case OPALLSKINS_BOOL:{
				boolean b = cm.getBoolean(node, args);
					plugin.config.opAllSkins = b;
					sender.sendMessage(prefix + _neutral + "Set the " + _item + "OPALLSKINS" + _neutral + " mode to " + isPositive(b) + String.valueOf(b).toUpperCase() + _neutral + ".");
					//sender.sendMessage(prefix + _neutral + "Set the " + _item + "OPALLSKINS" + _neutral + " mode to " + _negative + "FALSE" + _neutral + ".");
				return true;
			}
			case SKINADD_ITEM:{
				Player reciever = cm.getReciever(sender, node, args);
				String skin = cm.getItem(node, args);
				if (SkinManager.Instance.addSkin(reciever.getName(), skin)){
					cm.sendMessage(sender, args, cmdn, prefix);
					return true;
				}
				break;
			}
			case GIVE_PLAYER_ITEM:{
				Player reciever = cm.getReciever(sender, node, args);
				String item = cm.getItem(node, args);
				if (GunManager.getInstance().validGun(item)){
				ItemStack i = GunManager.getInstance().getGun(item).getItem();
				reciever.getInventory().addItem(i);
				cm.sendMessage(sender, args, cmdn, prefix);
				} else{
					sender.sendMessage(prefix + _negative + "Invalid item name.");
				}
				return true;
			}
			case GUNS_RELOAD:{
				if (GunConfig.Instance.PreLoad()){
					cm.sendMessage(sender, args, cmdn, prefix);
				}
				return true;
			}
			default:
				break;
			}
			
		}
		return false;
	}
	boolean sendNoPerm(Player s, String p){
		p = p.toLowerCase();
			Player player = (Player)s;
			if (player.hasPermission(p)) return true;
			player.sendMessage(prefix + _negative + "Missing permission: " + _item + p);
			return false;
	}
	boolean validPlayer(String p){
		for(Player player2:Bukkit.getOnlinePlayers())
			if (player2.getName().equalsIgnoreCase(p))
				return true;
		return false;
	}
	String isPositive(boolean b){
		if (b) return _positive;
		return _negative;
	}
		
}

