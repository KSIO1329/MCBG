package me.ksio.mcbg.guns;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

public class PlayerGun {

	public final String Name;
	GunManager gm = GunManager.getInstance();
	private List<PlayerSkin> Skins = new ArrayList<PlayerSkin>();
	public PlayerGun(String name){
		Name=name;
	}
	public void addSkin(String name, int amount){
		PlayerSkin skin = new PlayerSkin(name, amount);
		int index = -1;
		for(int i = 0; i < Skins.size(); i++){
			if (Skins.get(i).Name.equalsIgnoreCase(name)) {
				index = i;
				break;
			}
		}
		if (index != -1){
			skin.Amount+=Skins.get(index).Amount;
			Skins.remove(index);
		}
		//if (Skins.contains(skin)) Skins.remove(skin);
		Skins.add(skin);
	}
	public PlayerSkin getSkin(String name){
		for(PlayerSkin skin:Skins){
			if (skin.Name.equalsIgnoreCase(name)) return skin;
		}
		return null;
	}
	public boolean contains(String name){
		for(PlayerSkin skin:Skins){
			if (skin.Name.equalsIgnoreCase(name)) return true;
		}
		return false;
	}
	public void subtractSkin(String name, int amount){
		PlayerSkin ps = null;
		boolean found = false;
		for (PlayerSkin skin:Skins){
			if (skin.Name.equalsIgnoreCase(name)) {
				ps = skin;
				found = true;
			}
		}
		if (found){
			Skins.remove(ps);
			ps.Amount-=amount;
			Skins.add(ps);
		}
	}
	public List<PlayerSkin> getSkins(){
		return Skins;
	}
	public List<ItemStack> getAllSkins(){
		List<ItemStack> i = new ArrayList<ItemStack>();
		for (PlayerSkin s:Skins){
			if (!gm.validSkin(Name + ":" + s.Name)) return null;
			ItemStack n = gm.getGun(Name).getSkin(Name + ":" + s.Name).getSkinItem(s.Amount);
			i.add(n);
		}
		return i;
	}
}
