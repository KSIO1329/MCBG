package me.ksio.mcbg.guns;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.shampaggon.crackshot.CSUtility;

public class Gun {
	
	private String name;
	private String displayName;
	private List<Skin> skins = new ArrayList<Skin>();
	
	private CSUtility util = new CSUtility();
	
	public Gun(String name){
		this.name = name;
		//String dn = ChatColor.stripColor(util.getWeaponTitle(util.generateWeapon(name)));
		this.displayName = ChatColor.WHITE + ChatColor.stripColor(util.getWeaponName(util.generateWeapon(name)));//ChatColor.WHITE + displayName;
	}
	public ItemStack getItem(){
		return util.generateWeapon(name);
	}
	public String getName(){
		return name;
	}
	public void addSkin(Skin s){
		skins.add(s);
	}
	public Skin getSkin(String name){
		for (Skin s : skins){
			if (s.getName().equals(name))
				return s;
		}
		return null;
	}
	public int getNumberOfSkins(){
		return skins.size();
	}
	public boolean validSkin(String name){
		for(Skin s:skins){
			if (s.getName().equals(name)){
				return true;
			}
		}
		return false;
	}
	public ItemStack getItemFromSkin(ItemStack x) {
		ItemStack i = getItem();
		ItemMeta meta = i.getItemMeta();
		meta.setDisplayName(x.getItemMeta().getDisplayName());
		i.setItemMeta(meta);
		return i;
	}
	public ItemStack getGUIItem(){
		ItemStack i = getItem();
		ItemMeta meta = i.getItemMeta();
		meta.setDisplayName(displayName + " Default");
		List<String> lore = new ArrayList<String>();
		lore.add("#" + name + ":default");
		meta.setLore(lore);
		i.setItemMeta(meta);
		return i;
	}
	public List<ItemStack> getAllSkinItems(){
		List<ItemStack> i = new ArrayList<ItemStack>();
		for(Skin s:skins){
			i.add(s.getSkinItem());
		}
		return i;
	}
}
