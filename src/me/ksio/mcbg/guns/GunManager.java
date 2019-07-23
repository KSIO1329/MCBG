package me.ksio.mcbg.guns;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.shampaggon.crackshot.CSUtility;



public class GunManager {

	
	private List<Gun> guns = new ArrayList<Gun>();
	public CSUtility util = new CSUtility();
	private static GunManager instance = new GunManager();
	public static GunManager getInstance(){ 
		return instance;
	}
	
	private GunManager(){
	}
	
	/*public void LoadGuns(){
		Gun g;
		//
		g =  new Gun("akm", "AKM");
		g.addSkin(new Skin("akm:toxic", ChatColor.LIGHT_PURPLE + "AKM Toxic", 3, Rarity.LEGENDARY));
		//g.addSkin(new Skin("akm:test", ChatColor.RED + "AKM Toxic", 2));
		guns.add(g);
		
		guns.add(new Gun("m3a16", "M3A16"));
		g = new Gun("railgun", "Railgun");
		g.addSkin(new Skin("railgun:rainbow", ChatColor.LIGHT_PURPLE + "Rainbow Railgun", 1, Rarity.UNCOMMON));
		g.addSkin(new Skin("railgun:invert", ChatColor.LIGHT_PURPLE + "Inverted Railgun", 2, Rarity.UNCOMMON));
		guns.add(g);
		
	}*/
	public void addGun(Gun g){
		guns.add(g);
	}
	public int getNumberOfGuns(){
		return guns.size();
	}
	public Gun getGun(String name){
		for(Gun g:guns){
			if (g.getName().equalsIgnoreCase(name)) return g;
		}
		return null;
	}
	public boolean validSkin(String s){
		for(Gun g:guns){
			if (g.validSkin(s)) return true;
		}
		return false;
	}
	public boolean validGun(String s){
		for(Gun g:guns){
			if (g.getName().equals(s)) return true;
		}
		return false;
	}
	public boolean isGun(ItemStack i){
		if (util.getWeaponTitle(i) != null) return true;
		return false;
	}
	public Gun getGun(ItemStack i){
		for(Gun g:guns){
			if (g.getName().equalsIgnoreCase(util.getWeaponTitle(i))) return g;
		}
		return null;
	}
	public List<ItemStack> getGunItems(){
		List<ItemStack> l = new ArrayList<ItemStack>();
		for(Gun g:guns){
			l.add(g.getItem());
		}
		return l;
	}
	public void resetGuns(){
		guns = new ArrayList<Gun>();
	}
}
