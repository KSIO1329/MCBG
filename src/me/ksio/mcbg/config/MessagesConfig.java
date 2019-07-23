package me.ksio.mcbg.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.ksio.mcbg.Core;

public class MessagesConfig {

	File messages;
	FileConfiguration messagesConfig;
	
	Core core;
	void preLoad(){
        messages= new File(core.getDataFolder(), "messages.yml");

        if (!messages.exists()) {
            messages.getParentFile().mkdirs();
            core.saveResource("messages.yml", false);
        }
        messagesConfig = new YamlConfiguration();
        try {
            messagesConfig.load(messages);
            messagesConfig.options().copyDefaults(true);
            messagesConfig.save(messages);
        } catch (Exception e) {
        }
	}
	public void load(){
		this.core = Core.getInstance();
		preLoad();

		List<String> list = messagesConfig.getStringList("messages");
		List<String> newList = new ArrayList<String>();
		for (String s : list){
			newList.add(ChatColor.translateAlternateColorCodes('&', s));
		}
		core.announcements.setMessages(newList);
	}
}
