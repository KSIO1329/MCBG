package me.ksio.mcbg.guns;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class Skin {

	private String name;
	private int durability;
	private String displayName;
	private Rarity rarity;
	public Skin(String name, String displayName, int durability, Rarity rarity){
		this.name = name;
		this.displayName = displayName;
		this.durability = durability;
		this.rarity = rarity;
	}/*
	public Skin(Skin skin) {
		this.name = skin.name;
		this.displayName = skin.displayName;
		this.durability = skin.durability;
	}*/
	public String getName(){
		return name;
	}
	/*public String getDisplayName(){
		return displayName; 
	}
	public int getDurability(){
		return durability;
	}*/
	public ItemStack getSkinItem(int amount){
		ItemStack i = new ItemStack(Material.DIAMOND_HOE, 1, (byte)durability);
		ItemMeta meta = i.getItemMeta();
		meta.setDisplayName(displayName);
		meta.setUnbreakable(true);
		List<String> lore = new ArrayList<String>();
		lore.add("#" + name);
		lore.add(ChatColor.GREEN + "Rarity: " + rarity.getDisplayName());
		String prefix = ChatColor.GREEN + "";
		if (amount == 0) prefix = ChatColor.RED + "";
		lore.add(prefix + "Amount: " + ChatColor.WHITE + amount);
		meta.setLore(lore);
		i.setItemMeta(meta);
		return i;
	}
	public ItemStack getSkinItem(){
		return getSkinItem(0);
	}
	public ItemStack getSkinFromItem(ItemStack item){
		ItemStack i = new ItemStack(Material.DIAMOND_HOE, 1, (byte)durability);
		ItemMeta meta = i.getItemMeta();
		meta.setDisplayName(item.getItemMeta().getDisplayName());
		meta.setLore(item.getItemMeta().getLore());
		meta.setUnbreakable(true);
		i.setItemMeta(meta);
		return i;
	}
	enum Availability {
		ROLLING, EVENTS, LEGACY, VAULTED;
	}
	public enum Rarity {
		COMMON(ChatColor.GRAY + "Common"),
		UNCOMMON(ChatColor.WHITE + "Uncommon"),
		RARE(ChatColor.DARK_AQUA + "Rare"),
		EPIC(ChatColor.GOLD + "Epic"),
		LEGENDARY(ChatColor.LIGHT_PURPLE + ChatColor.BOLD.toString() + "Legendary"),
		EXTREME(ChatColor.RED + ChatColor.BOLD.toString() + "Extreme");

		String displayName;
		Rarity(String displayName){
			this.displayName = displayName;
		}
		
		public String getDisplayName() {
			return displayName;
		}
		public static Rarity getRarity(String s){
			for (Rarity r:values()){
				if (r.toString().equalsIgnoreCase(s)) return r;
			}
			return null;
		}
	}
}
