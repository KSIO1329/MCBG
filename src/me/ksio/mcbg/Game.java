package me.ksio.mcbg;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;

import me.ksio.mcbg.commands.executors.MainCommand;

public class Game {

	private final int id;
	private int time = 0;
	private List<String> players = new ArrayList<String>();
	private MultiverseWorld world;
	private MVWorldManager mv;
	private String parent_world = "testgame";
	boolean stopped = true;
	boolean started = false;
	
	public Game (int id, MVWorldManager mv, String parent_world){
		this.id = id;
		this.mv = mv;
		this.parent_world = parent_world;
	}
	
	public int getID(){
		return id;
	}
	public World getWorld(){
		return world.getCBWorld();
	} 
	public boolean addPlayer(Player p){
		if (stopped) return false;
		if (players.contains(p.getName())) return false;
		p.setHealth(20);
		p.setInvulnerable(false);
		p.getInventory().clear();
		players.add(p.getName());
		p.teleport(world.getSpawnLocation());
		return true; 
	}
	public void removePlayer(Player p, boolean forced){
		if (players.contains(p.getName())){
		players.remove(p.getName());
		GameManager.getInstance().sendToHub(p);
		//p.teleport(hubWorld.getSpawnLocation());
		if (players.size() <= 1 && !forced){
			stop();
		}
		}
	}
	public void removePlayer(Player p){
		removePlayer(p, false);
	}
	public int getNumberOfPlayers(){
		return players.size();
	}
	
	public int getTime(){
		return time;
	}
	public boolean containsPlayer(Player p){
		if (players.contains(p.getName())) return true;
		return false;
	}
	public void forceLoad(){
		if (mv.hasUnloadedWorld("game" + id, true)){
			mv.deleteWorld("game" + id); 
		}
		if (mv.cloneWorld(parent_world, "game" + id)){
			mv.loadWorld("game" + id);
			world = mv.getMVWorld("game" + id);
		}
		time = 0;
		players = new ArrayList<String>();
		stopped = false;
		started = false;
	}
	public void softLoad(){
		if (!stopped){
			// NOT STOPPED !
		} else{
		forceLoad();
		}
	}
	public void start(){
		started = true;
	}
	public void stop(){
		stop(false);
	}
	public void stop(boolean forced){
		if (players.size() > 1 || (forced && players.size() > 0))
		while (players.size() > 0)
			removePlayer(Bukkit.getPlayer(players.get(0)), forced);
		else if (players.size() == 1) {
			Player winner = Bukkit.getPlayer(players.get(0));
			Bukkit.broadcastMessage(MainCommand.prefix + ChatColor.GOLD + winner.getName() + ChatColor.GRAY + " has just won on map " + ChatColor.WHITE + "GAME-" + id + ChatColor.GRAY + ".");
			stopped = true;
			Bukkit.getScheduler().scheduleSyncDelayedTask(Core.getInstance(), new Runnable() {
				@Override
	            public void run() {
	    			removePlayer(winner, true);
	    			mv.deleteWorld("game" + id);
	            }
	        }, 200L);
		}
		if (!stopped)
		mv.deleteWorld("game" + id);
		stopped = true;
		started = false;
	}
	public void updateTime(){
		time++;
	}
	public boolean isLoaded(){
		return stopped;
	}
}
