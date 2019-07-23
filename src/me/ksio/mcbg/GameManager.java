package me.ksio.mcbg;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.onarandombox.MultiverseCore.MultiverseCore;

public class GameManager {

	
	private MultiverseCore mvcore;
	private GameManager(){}
	private String hub = "hubtest";
	
	private static GameManager instance = new GameManager();
	
	public static GameManager getInstance(){
		return instance;
	}
	
	private ArrayList<Game> games = new ArrayList<Game>();
	
	public boolean isPlaying(Player p){
		if (getGame(p) == null){
			return false;
		}
		return true;
	}
	public Game getGame(int id){
		for (Game g : games){
			if (g.getID() == id) return g;
		}
		return null;
	}
	public Game getGame(Player p){
		for (Game g : games){
			if (g.containsPlayer(p)) return g;
		}
		return null;
	}
	public void setup() {
        mvcore = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
        for(int i = 1; i <= 2; i++){
			addGame(i, "testgame");
		}
	}
	public boolean removeGame(int id){
		Game game = getGame(id);
		if (game != null){
			game.stop();
			games.remove(game);
			return true;
		}
		return false;
	}
	public boolean addGame(int id, String parent_world){
		if (getGame(id) != null) return false;
		Game game = new Game(id, mvcore.getMVWorldManager(), parent_world);
		games.add(game);
		return true;
	}
	public Game getBestGame(){
		int maxP = -1;
		Game r = null;
		for (Game g : games){
			if (g.getNumberOfPlayers() > maxP && !g.started){
				r = g;
				maxP = g.getNumberOfPlayers();
			}
		}
		return r;
	}
	public void updateTime(){
		for (Game g : games){
			g.updateTime();
		}
	}
	public boolean requestJoin(Game game, Player p){
		game.softLoad();
		if (!game.started)
		return game.addPlayer(p);
		return false;
	}
	public boolean requestJoin(Player p){
		Game game = getBestGame();
		if (game == null)
			return false;
		return requestJoin(game, p);		
		//return false;
	}
	public int getNumberOfGames(){
		return games.size();
	}
	public void sendToHub(Player p){
		p.setHealth(20);
		p.setInvulnerable(true);
		p.getInventory().clear();
		p.teleport(mvcore.getMVWorldManager().getMVWorld(hub).getSpawnLocation());
	}
	
}
