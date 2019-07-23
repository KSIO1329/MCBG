package me.ksio.mcbg.announcements;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import me.ksio.mcbg.Core;
import me.ksio.mcbg.GameManager;

public class News {
	BukkitScheduler sched;
	Core plugin;
	
	List<String> messages = new ArrayList<String>();
	
	public News(){
		plugin = Core.getInstance();
		sched = plugin.getServer().getScheduler();
		//startRunnables();
	}
	
	void randomMessage(){
		Random r = new Random();
		String message = messages.get(r.nextInt(messages.size()));
		for (Player p:plugin.getServer().getOnlinePlayers()){
			if (!GameManager.getInstance().isPlaying(p)){
				p.sendMessage(message);
			}
		}
	}
	
	
	void startRunnables(){
		 sched.scheduleSyncRepeatingTask(plugin, new Runnable() {
	            @Override
	            public void run() {
	            	if (messages.size() > 0)
	                randomMessage();
	            }
	        }, 0L, 6000);
	}
	public void setMessages(List<String> s){
		messages = s;
	}
}
