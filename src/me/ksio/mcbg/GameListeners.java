package me.ksio.mcbg;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class GameListeners implements Listener{

	@EventHandler
	public void onLeave(PlayerQuitEvent e){
		if (GameManager.getInstance().isPlaying(e.getPlayer())){
			GameManager.getInstance().getGame(e.getPlayer()).removePlayer(e.getPlayer());
		}
	}
	@EventHandler
	public void onDeath(EntityDamageEvent e){
		if (!(e.getEntity() instanceof Player)) return;
		Player p = (Player) e.getEntity();
		if (p.getHealth() - e.getFinalDamage() <= 0)
		if (GameManager.getInstance().isPlaying(p)){
			GameManager.getInstance().getGame(p).removePlayer(p);
			e.setCancelled(true);
		}
	}
	@EventHandler
	public void onTeleport(PlayerTeleportEvent e){
		if (GameManager.getInstance().isPlaying(e.getPlayer()) && e.getTo().getWorld() != GameManager.getInstance().getGame(e.getPlayer()).getWorld()){
			GameManager.getInstance().getGame(e.getPlayer()).removePlayer(e.getPlayer());
		} 
	}
	
}
