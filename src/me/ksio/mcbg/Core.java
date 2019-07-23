package me.ksio.mcbg;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import me.ksio.mcbg.announcements.News;
import me.ksio.mcbg.commands.executors.GameCommand;
import me.ksio.mcbg.commands.executors.GunsCommand;
import me.ksio.mcbg.commands.executors.MainCommand;
import me.ksio.mcbg.config.MainConfig;
import me.ksio.mcbg.config.PlayerSkinConfig;
import me.ksio.mcbg.guns.ItemsGUI;
import me.ksio.mcbg.guns.SkinListeners;


public class Core extends JavaPlugin{
	
	public MainConfig config = null;
	PluginLogger logger = new PluginLogger(this);
	public News announcements;
	private static Core instance;
	public ProtocolManager protocolManager;
	public void onEnable(){
		instance = this;
		//
		protocolManager = ProtocolLibrary.getProtocolManager();
		//Guns command
		getCommand("guns").setExecutor(new GunsCommand());
		getCommand("mcbg").setExecutor(new MainCommand());
		getCommand("game").setExecutor(new GameCommand());
		//Load GUIItems
		ItemsGUI.loadGuiItems();
		//Load Guns
		//Guns.LoadGuns();
		//Load Gun Skins
		//GunSkins.LoadSkins();
		//GunManager.getInstance().LoadGuns(); 
		//
		announcements = new News();
		//Load config files
		config = new MainConfig();   
		GameManager.getInstance().setup();

		//Listeners
		loadListeners();
		//Runnables 
		startRunnable();
		logger.info("Successfully enabled " + getDescription().getName() + " v" + getDescription().getVersion());
		
		
	}
	
	public void onDisable(){
		if(!config.SaveConfig())
			logger.info("Error while saving config for " + getDescription().getName() + " v" + getDescription().getVersion());
		else
			logger.info("Successfully disabled " + getDescription().getName() + " v" + getDescription().getVersion());
		for(Player p:getServer().getOnlinePlayers()){
			if (!PlayerSkinConfig.Instance.SavePlayer(p)){
				Bukkit.broadcastMessage("Error while saving skin for :" + p.getName());
			}
		}
	}
	
	void loadListeners(){
		SkinListeners skinListeners = new SkinListeners();
		skinListeners.packetListener(); 
		getServer().getPluginManager().registerEvents(skinListeners, this);
		getServer().getPluginManager().registerEvents(new GameListeners(), this);
		preventErrors(skinListeners);
	}
	
	void startRunnable(){
        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
            	GameManager.getInstance().updateTime();
            }
        }, 0L, 20L); 
	}
	void preventErrors(SkinListeners lClass){
		for(Player p:this.getServer().getOnlinePlayers()){
			lClass.joinStuff(p);
		}
	}
	public static Core getInstance(){
		return instance;
	}
}
