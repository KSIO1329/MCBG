package me.ksio.mcbg.config;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.ksio.mcbg.Core;
import me.ksio.mcbg.guns.GunManager;
import me.ksio.mcbg.guns.PlayerGun;
import me.ksio.mcbg.guns.PlayerSkin;
import me.ksio.mcbg.guns.SkinManager;

public class PlayerSkinConfig {

	File config;
	FileConfiguration skinsConfig;
	SkinManager sm = SkinManager.Instance;
	Core core;
	
	public static PlayerSkinConfig Instance = new PlayerSkinConfig();
	private PlayerSkinConfig(){
		
	}
	public void Load(){
		this.core = Core.getInstance();
		preLoad();
	}
	void preLoad(){
		config= new File(core.getDataFolder(), "PlayerSkinData.yml");

        if (!config.exists()) {
        	config.getParentFile().mkdirs();
            core.saveResource("PlayerSkinData.yml", false);
        }
        skinsConfig = new YamlConfiguration();
        try {
        	skinsConfig.load(config);
        	skinsConfig.options().copyDefaults(true);
        	skinsConfig.save(config);
        } catch (Exception e) {
        }
	}
	public void LoadPlayer(Player p){
		String name = p.getName();
		ArrayList<PlayerGun> ownedSkins = loadPlayerGuns(name);
		ArrayList<String> selectedSkins = validateSelected((ArrayList<String>) skinsConfig.getStringList("skins." + name + ".selectedskins"));
		if (selectedSkins == null) selectedSkins = new ArrayList<String>();
		sm.addToMemory(name, ownedSkins, selectedSkins);
	}

	ArrayList<PlayerGun> loadPlayerGuns(String playerName){
		ArrayList<PlayerGun> ownedSkins = new ArrayList<PlayerGun>();
		String path = "skins." + playerName + ".skins";
		if (!skinsConfig.contains(path)) return ownedSkins;
		Bukkit.getPlayer(playerName).sendMessage(ChatColor.GREEN + "Getting skin data from file...");
		ConfigurationSection sec = skinsConfig.getConfigurationSection(path);
		for(String s:sec.getKeys(false)){
			List<String> list = skinsConfig.getStringList(path + "." + s);
			PlayerGun gun = new PlayerGun(s);
			for(String skin:list){
				
				String skinName = skin.split(":")[0];
				if (GunManager.getInstance().validSkin(gun.Name + ":" + skinName)){
				int Amount = Integer.valueOf(skin.split(":")[1]);
				Bukkit.getPlayer(playerName).sendMessage("Added " + gun.Name + ":" + skinName + " > " + Amount);
				//PlayerSkin ps = new PlayerSkin(skinName,Amount);
				gun.addSkin(skinName, Amount);
				} else{
					Bukkit.getPlayer(playerName).sendMessage("Error while adding " + gun.Name + ":" + skinName + ", skipping.");
				}
			}
			ownedSkins.add(gun);
		}
		return ownedSkins;
	}
	ArrayList<String> validateSelected(ArrayList<String> a){
		ArrayList<String> b = new ArrayList<String>();
		for (String s:a){
			if (GunManager.getInstance().validSkin(s)) b.add(s);
		}
		return b;
	}
	public boolean SavePlayer(Player p){
			try{
				String name = p.getName();
				boolean savedSkins = savePlayerGuns(name, sm.getGuns(name));
				//ArrayList<String> playerSkins = getStringFromPlayerSkin(SkinManager.playerSkins.get(name));  playerSkins.size() > 0 || 
				ArrayList<String> selectedSkins = sm.getSelected(name);
				
				if (selectedSkins.size() > 0 || savedSkins){
				//skinsConfig.set("skins." + name + ".skins", playerSkins);
				skinsConfig.set("skins." + name + ".selectedskins", selectedSkins);
				} else {
					skinsConfig.set("skins." + name, null);
				}
				skinsConfig.save(config);
				return true;
			}
			catch (Exception e){
				return false;
			}
			// save all others
		
	}
	boolean savePlayerGuns(String playerName, ArrayList<PlayerGun> a){
		HashMap<String, List<String>> map = new HashMap<String, List<String>>();
		for(PlayerGun g:a){
			//String[] split = s.Name.split(":");
			String gunName = g.Name;
			for(PlayerSkin s:g.getSkins()){
				String skin = s.Name + ":" + s.Amount;
				if (map.containsKey(gunName)){
					List<String> list = map.get(gunName);
					list.add(skin);
					map.put(gunName, list);
				} else {
					List<String> list = new ArrayList<String>();
					list.add(skin);
					map.put(gunName, list);
				}
			}
			
			
		}
		for(String gun:map.keySet()){
			skinsConfig.set("skins." + playerName + ".skins." + gun, map.get(gun));
		}
		if (map.size() == 0) return false;
		return true;
	}
	
}
