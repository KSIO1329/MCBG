package me.ksio.mcbg.commands.executors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ksio.mcbg.Core;
import me.ksio.mcbg.guns.SkinGUI;

public class GunsCommand implements CommandExecutor{
	
	Core plugin;
	public GunsCommand(){
		this.plugin = Core.getInstance();
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		//Check if the command sender is a Player
		if (sender instanceof Player){
			//Make a Player variable by casting the sender to Player
			Player p = (Player) sender;
			//Check if the command label is "turned"
			if (label.equalsIgnoreCase("guns")){
				//Switch for all the arguments of the command, /turned args[0] args[1] ... args[n]
					p.openInventory(SkinGUI.getInstance().getGunList(1, 45));
					return true;				
			}
		}
		return false;
	}
}
