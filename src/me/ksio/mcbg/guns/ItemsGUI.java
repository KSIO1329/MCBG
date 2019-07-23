package me.ksio.mcbg.guns;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemsGUI {

	public static ItemStack leftArrow = new ItemStack(Material.PAPER, 1);
	public static ItemStack rightArrow = new ItemStack(Material.PAPER, 1);
	public static ItemStack backButton = new ItemStack(Material.DOUBLE_PLANT, 1);
	public static ItemStack blankGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1);
			
	public static void loadGuiItems(){
		ItemMeta meta = null;
		// left Arrow
		meta = leftArrow.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Previous Page");
		leftArrow.setItemMeta(meta);
		meta = null;
		// Right Arrow
		meta = rightArrow.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Next Page");
		rightArrow.setItemMeta(meta);
		meta = null;
		// Back Button
		meta = backButton.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Go Back");
		backButton.setItemMeta(meta);
		meta = null;
		//Blank Glass
		meta = blankGlass.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "");
		blankGlass.setItemMeta(meta);
		blankGlass.setDurability((short) 7);
		meta = null;
	}
}


