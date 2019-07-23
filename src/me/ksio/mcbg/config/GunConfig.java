package me.ksio.mcbg.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.ksio.mcbg.Core;
import me.ksio.mcbg.guns.Gun;
import me.ksio.mcbg.guns.GunManager;
import me.ksio.mcbg.guns.Skin;
import me.ksio.mcbg.guns.Skin.Rarity;

public class GunConfig {

	
	File file;
	FileConfiguration config;
	GunManager gm = GunManager.getInstance();
	Core core;
	public static GunConfig Instance = new GunConfig();
	private GunConfig(){}
	public boolean PreLoad(){
		core = Core.getInstance();
		file = new File(core.getDataFolder(), "Guns.yml");
		boolean exists = !file.exists();
        if (exists) {
        	file.getParentFile().mkdirs();
            core.saveResource("Guns.yml", false);
        }
        config = new YamlConfiguration();
        try {
        	config.load(file);
        	if (exists){
        	config.options().copyDefaults(true);
        	config.save(file);
        	}
        } catch (Exception e) {
        }
		return Load();
	}
	boolean Load(){
		try{
			gm.resetGuns();
		Gun g;
		Skin s;
		List<String> ourGuns = new ArrayList<String>();
		for(String a:config.getKeys(false)){
			//String displayName = config.getString(a + ".Display_Name");
			g = new Gun(a.toLowerCase());
			ourGuns.add(a.toLowerCase());
			String path = a + ".Skins";
			if (config.isConfigurationSection(path))
			for(String skin:config.getConfigurationSection(path).getKeys(false)){
				String displayNameSkin = config.getString(path+"."+skin+".Display_Name");
				int durability = config.getInt(path+"."+skin+".Durability");
				String rar = config.getString(path+"."+skin+".Rarity");
				Rarity rarity = Rarity.getRarity(rar);
				if (rarity == null){
					// ERROR
					continue;
				}
				s = new Skin(a.toLowerCase() + ":" + skin.toLowerCase(),ChatColor.translateAlternateColorCodes('&', displayNameSkin),durability,rarity);
				g.addSkin(s);		
			}
			gm.addGun(g);
		}
		List<String> theirGuns = gm.util.getAllWeapons();
		for(String a:theirGuns){
			if (ourGuns.contains(a.toLowerCase())) continue;
			gm.addGun(new Gun(a.toLowerCase()));
		}
		return true;
		} catch(Exception e){
			return false;
		}
	}
	
	
}
