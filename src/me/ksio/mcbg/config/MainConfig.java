package me.ksio.mcbg.config;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import me.ksio.mcbg.Core;
import me.ksio.mcbg.commands.executors.MainCommand;

public class MainConfig {

	public boolean opAllSkins;
	public List<String> allSkinsT = new ArrayList<String>();
	public List<String> allSkinsF = new ArrayList<String>();
	public List<String> noNews = new ArrayList<String>();
	Core plugin;
	FileConfiguration config;
	public MainConfig(){
		this.plugin = Core.getInstance();
		config = plugin.getConfig();
		InitialLoad();
	}
	void InitialLoad(){
		plugin.getConfig().options().copyDefaults(true);
		plugin.saveDefaultConfig();
		// do initial load for all others
		MessagesConfig msgs = new MessagesConfig();
		msgs.load();
		PlayerSkinConfig.Instance.Load();
		GunConfig.Instance.PreLoad();
		// load
		Load();
	}
	void Load(){
		opAllSkins = config.getBoolean("op.allSkins");
		MainCommand.prefix = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("chat.prefix"));
		allSkinsT = config.getStringList("settings.allSkins.t");
		allSkinsF = config.getStringList("settings.allSkins.f");
		noNews = config.getStringList("settings.nonews");
	}
	public boolean Reload() {
		try{
			plugin.reloadConfig();
			Load();
			return true;
		}
		catch (Exception e){
			return false;
		}
		
	}
	public boolean SaveConfig(){
		try{
		config.set("op.allSkins", opAllSkins);
		config.set("settings.allSkins.t", allSkinsT);
		config.set("settings.allSkins.f", allSkinsF);
		config.set("settings.nonews", noNews);
		plugin.saveConfig();
		return true;
		}
		catch (Exception e){
			return false;
		}
		// save all others
	}
	public void allSkinsAdd(String s, boolean b){
		if (b ){
			if (!allSkinsT.contains(s))
			allSkinsT.add(s);
			if (allSkinsF.contains(s)) allSkinsF.remove(s);
		}else{
			if (!allSkinsF.contains(s))
			allSkinsF.add(s);
			if (allSkinsT.contains(s)) allSkinsT.remove(s);
		}
	}
	public void allSkinsRemove(String s) {
		if (allSkinsT.contains(s))
			allSkinsT.remove(s);
		if (allSkinsF.contains(s))
			allSkinsF.remove(s);
	}
	public boolean noNewsToggle(String s){
		if (noNews.contains(s)){
			noNews.remove(s);
			return false;
		}
		noNews.add(s);
		return true;
	}
	public void noNewsSet(String s, boolean b){
		if (b && noNews.contains(s))
			noNews.remove(s);
		else if (!b && !noNews.contains(s))
			noNews.add(s);			
	}
}
