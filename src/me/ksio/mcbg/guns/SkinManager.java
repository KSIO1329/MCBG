package me.ksio.mcbg.guns;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers.ItemSlot;

import me.ksio.mcbg.Core;
import me.ksio.mcbg.config.MainConfig;


public class SkinManager {

	public static SkinManager Instance = new SkinManager();
	private SkinManager(){}
	static GunManager gm = GunManager.getInstance();
	HashMap<String, ArrayList<PlayerGun>> playerSkins = new HashMap<String, ArrayList<PlayerGun>>();
	
	HashMap<String, ArrayList<String>> selectedSkins = new HashMap<String, ArrayList<String>>();
	// Load iz .yml 
	// Dodamo skinove
	// Oduzmemo skinove
	public void addToMemory(String name, ArrayList<PlayerGun> ownedSkins, ArrayList<String> selectedSkinsn){
		playerSkins.put(name, ownedSkins);
		selectedSkins.put(name, selectedSkinsn);
	}
	int getPlayerGun(ArrayList<PlayerGun> ar, String name){
		for(int i = 0; i < ar.size(); i++){
			if (ar.get(i).Name.equals(name)) return i;
		}
		return -1;
	}
	public boolean isSelected(String s, String name){
		return selectedSkins.get(s).contains(name);
	}
	public ArrayList<PlayerGun> getGuns(String s){
		return playerSkins.get(s);
	}
	public ArrayList<String> getSelected(String s){
		return selectedSkins.get(s);
	}
	public boolean hasSkin(String p, String name){
		for(PlayerGun g:playerSkins.get(p)){
			String[] s = name.split(":");
			if (g.Name.equalsIgnoreCase(s[0]))
			if (g.contains(s[1])){
				return true;
			}
		}
		return false;
	}
	public boolean addSkin(String p, String name){
		ArrayList<PlayerGun> ar = playerSkins.get(p);
		if (!gm.validSkin(name)) return false;
		String[] split = name.split(":");
		int index = getPlayerGun(ar, split[0]);
		PlayerGun gun;
		if (index != -1){
			gun = ar.get(index);
			ar.remove(index);
		} else {
			gun = new PlayerGun(split[0]);
		}

		gun.addSkin(split[1], 1);
		ar.add(gun);
		//Bukkit.broadcastMessage(ar.size() + "");
		playerSkins.put(p, ar);
		return true;
	}
	
	public boolean removeSkin(String p, String name){
		ArrayList<PlayerGun> ar = playerSkins.get(p);
		String[] split = name.split(":");
		int index = getPlayerGun(ar, split[0]);
		if (index != -1)
		{
			ar.get(index).subtractSkin(split[1], 1);

		} else return false;
		playerSkins.put(p, ar);
		return true;
	}
	void updatePlayerInventory(Player p){
		p.updateInventory();
		ProtocolManager protocolManager = Core.getInstance().protocolManager;
		PacketContainer packet = new PacketContainer(PacketType.Play.Server.ENTITY_EQUIPMENT);
		if (p.getInventory().getItemInMainHand().getType() != Material.AIR){
		packet.getItemSlots().write(0, ItemSlot.MAINHAND);
		packet.getItemModifier().write(0, p.getInventory().getItemInMainHand());
		packet.getEntityModifier(p.getWorld()).write(0, p);
		}
		PacketContainer packet2 = new PacketContainer(PacketType.Play.Server.ENTITY_EQUIPMENT);
		if (p.getInventory().getItemInOffHand().getType() != Material.AIR){
		packet2.getItemSlots().write(0, ItemSlot.OFFHAND);
		packet2.getItemModifier().write(0, p.getInventory().getItemInOffHand());
		packet2.getEntityModifier(p.getWorld()).write(0, p);
		}
		for(Player s:Bukkit.getOnlinePlayers()){
			try {
				if (p.getInventory().getItemInMainHand().getType() != Material.AIR)
				protocolManager.sendServerPacket(s, packet);
				if (p.getInventory().getItemInOffHand().getType() != Material.AIR)
				protocolManager.sendServerPacket(s, packet2);
			} catch (Exception e) {
			}
		}
	}
	public boolean selectSkin(String p, String name){
		if (!gm.validSkin(name) && !name.split(":")[1].equalsIgnoreCase("default")) return false;
		MainConfig conf = Core.getInstance().config;
		if (!(hasSkin(p,name) || (Bukkit.getPlayer(p).isOp() && conf.opAllSkins && !conf.allSkinsF.contains(p)) || conf.allSkinsT.contains(p)) && !name.split(":")[1].equalsIgnoreCase("default")) return false;
		ArrayList<String> ss = selectedSkins.get(p);
		ArrayList<String> ss2 = new ArrayList<String>(ss);
		for(String s:ss){
			if (s.split(":")[0].equals(name.split(":")[0])) ss2.remove(s);
		}
		if (!name.split(":")[1].equalsIgnoreCase("default"))
		ss2.add(name);
		selectedSkins.put(p, ss2);
		updatePlayerInventory(Bukkit.getPlayer(p));
		return true;
	}
	public ItemStack toSkin(String s, ItemStack i){
		if (!gm.isGun(i)) return i;
		Gun gun = gm.getGun(i);
		if (gun == null) return i;
		Skin ps = getPlayerSkin(s, gun.getName());
		if (ps == null) return i;
		return ps.getSkinFromItem(i);
	}
	
	Skin getPlayerSkin(String s, String gunName){
		if (selectedSkins.containsKey(s)){
			String skin = null;
			for(String p:selectedSkins.get(s)){
				if (p.split(":")[0].equalsIgnoreCase(gunName)) skin = p;
			}
			if (skin != null){
				return gm.getGun(skin.split(":")[0]).getSkin(skin); 
			}
		}
		return null;
	}
	public ItemStack toDefault(ItemStack i){
		if (!gm.isGun(i)) return i;
		Gun gun = gm.getGun(i);
		if (gun == null) return i;
		return gun.getItemFromSkin(i);
	}
	public PlayerGun getPlayerGun(String p, String gun){
		if (!playerSkins.containsKey(p)) return null;
			//if (playerSkins.get(p).contains(gu))
		int index = getPlayerGun(playerSkins.get(p), gun);
		if (index < 0 || index >= playerSkins.get(p).size()) return null;
		return playerSkins.get(p).get(index);
		//return null;
	}
}
