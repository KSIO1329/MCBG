package me.ksio.mcbg.guns;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.ksio.mcbg.Core;

public class SkinGUI {
	
	Core plugin = Core.getInstance();
	SkinManager sm = SkinManager.Instance;
	GunManager gm = GunManager.getInstance();
	private static SkinGUI instance = new SkinGUI();
	private SkinGUI(){	}
	public static SkinGUI getInstance(){
		return instance;
	}
	public Inventory getGunList(int page, int size){
		Inventory pInv = Bukkit.getServer().createInventory(null, 54, ChatColor.RED + "Select Gun - Page " + page); 
		int start = (page-1) * size;

		int end = (page) * size;
		List<ItemStack> guns = gm.getGunItems();
		
		for(int i = start; (i%size != 0 || i == start) && i < gm.getNumberOfGuns() && i < end; i++){
			if (guns.get(i) != null)
			pInv.addItem(guns.get(i));
		}
		if (page > 1)
			pInv.setItem(45, ItemsGUI.leftArrow);
		else
			pInv.setItem(45, ItemsGUI.blankGlass);
		pInv.setItem(46, ItemsGUI.blankGlass);
		pInv.setItem(47, ItemsGUI.blankGlass);
		pInv.setItem(48, ItemsGUI.blankGlass);
		pInv.setItem(49, ItemsGUI.backButton);
		pInv.setItem(50, ItemsGUI.blankGlass);
		pInv.setItem(51, ItemsGUI.blankGlass);
		pInv.setItem(52, ItemsGUI.blankGlass);
		if (page < gm.getNumberOfGuns()/size+1)
			pInv.setItem(53, ItemsGUI.rightArrow);
		else
			pInv.setItem(53, ItemsGUI.blankGlass);
		return pInv;
	}
	public Inventory getSkinList(String name, int size, int page, String s){
		//ArrayList<Integer> ar = SkinManager.playerSkins.get(s); 
		PlayerGun gun = sm.getPlayerGun(s, name);
		//p.closeInventory();
		//Gun gun = gm.getGun(id);
		List<ItemStack> allPossibleSkins = new ArrayList<ItemStack>();
		allPossibleSkins.add(gm.getGun(name).getGUIItem());
		if (gun != null)
		allPossibleSkins.addAll(gun.getAllSkins());
		allPossibleSkins.addAll(getOtherSkins(name, allPossibleSkins));
		// REST
		Inventory pInv = Bukkit.getServer().createInventory(null, 54, ChatColor.RED + "Select skin: " + name.toUpperCase() + " - Page " + page);
		pInv.clear();
		int start = (page-1) * size;
		int end = (page) * size;
		for(int i = start; i < end && i < allPossibleSkins.size(); i++){
			pInv.addItem(allPossibleSkins.get(i));
		}
		if (page > 1)
			pInv.setItem(45, ItemsGUI.leftArrow);
		else
			pInv.setItem(45, ItemsGUI.blankGlass);
		pInv.setItem(46, ItemsGUI.blankGlass);
		pInv.setItem(47, ItemsGUI.blankGlass);
		pInv.setItem(48, ItemsGUI.blankGlass);
		pInv.setItem(49, ItemsGUI.backButton);
		pInv.setItem(50, ItemsGUI.blankGlass);
		pInv.setItem(51, ItemsGUI.blankGlass);
		pInv.setItem(52, ItemsGUI.blankGlass);
		if (page <= allPossibleSkins.size()/size)
			pInv.setItem(53, ItemsGUI.rightArrow);
		else
			pInv.setItem(53, ItemsGUI.blankGlass);
		return pInv;
	}
	List<ItemStack> getOtherSkins(String gunName, List<ItemStack> used){
		List<String> names = new ArrayList<String>();
		List<ItemStack> r = new ArrayList<ItemStack>();
		for(ItemStack i:used){
		names.add(i.getItemMeta().getLore().get(0).replaceAll("#", ""));
		}
		if (gm.getGun(gunName) != null)
		for(ItemStack i:gm.getGun(gunName).getAllSkinItems()){
			if(!names.contains(i.getItemMeta().getLore().get(0).replaceAll("#", ""))) r.add(i);
		}
		return r;
	}
}
