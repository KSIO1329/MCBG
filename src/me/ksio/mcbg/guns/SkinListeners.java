package me.ksio.mcbg.guns;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.ItemSlot;

import me.ksio.mcbg.Core;
import me.ksio.mcbg.config.PlayerSkinConfig;

public class SkinListeners implements Listener{

	SkinManager sm = SkinManager.Instance;
	ProtocolManager protocolManager;
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		joinStuff(e.getPlayer());
	}
	@EventHandler
	public void onLeave(PlayerQuitEvent e){
		if (!PlayerSkinConfig.Instance.SavePlayer(e.getPlayer())){
			Bukkit.broadcastMessage("Error while saving skin for :" + e.getPlayer().getName());
		}
	}
	public void joinStuff(Player p){
		// Load Skins Data
		PlayerSkinConfig.Instance.LoadPlayer(p);
		p.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(16);
		//p.getAttribute(Attribute.);
		//p.setFoodLevel(20);
	}
	public void packetListener(){
		protocolManager = Core.getInstance().protocolManager;
		protocolManager.addPacketListener(
				new PacketAdapter(Core.getInstance(), ListenerPriority.NORMAL, PacketType.Play.Server.SET_SLOT, PacketType.Play.Server.WINDOW_ITEMS, PacketType.Play.Server.ENTITY_EQUIPMENT, PacketType.Play.Client.SET_CREATIVE_SLOT, PacketType.Play.Server.NAMED_SOUND_EFFECT) {
				    @Override
				    public void onPacketSending(PacketEvent event) {
				        // Item packets (id: 0x29)

			        	PacketContainer p = event.getPacket();
				        if (event.getPacketType() == PacketType.Play.Server.SET_SLOT) {
				        	if (p.getIntegers().read(0) == 0){
				        	//Bukkit.broadcastMessage(p.getItemModifier().read(0).getType() + " " + p.getIntegers().read(1));//    > event.getPlayer().getInventory().getSize() )   p.getItemListModifier().read(0).size() 
				        	p.getItemModifier().write(0, stripHashTags(sm.toSkin(event.getPlayer().getName(), p.getItemModifier().read(0))));
				        	}
				        	else{
				        		if (p.getIntegers().read(1) > event.getPlayer().getOpenInventory().getTopInventory().getSize() || p.getIntegers().read(1) == -1)
				        		p.getItemModifier().write(0, stripHashTags(sm.toSkin(event.getPlayer().getName(), p.getItemModifier().read(0))));
				        		else {
				        			p.getItemModifier().write(0, stripHashTags(sm.toDefault(p.getItemModifier().read(0))));
				        		}
				        	}
				        } else if (event.getPacketType() == PacketType.Play.Server.WINDOW_ITEMS){
				        	//if (p.getItemListModifier().read(0).size() == 46){
				        	int size = p.getItemListModifier().read(0).size();
				        	List<ItemStack> neww = new ArrayList<ItemStack>();
				        	for (int i = 0; i < event.getPlayer().getOpenInventory().getTopInventory().getSize(); i++){
				        		neww.add(p.getItemListModifier().read(0).get(i));
				        	}
				        	
				        	for (int i = 0 + event.getPlayer().getOpenInventory().getTopInventory().getSize(); i < size; i++)
				        	{ 
				        		neww.add(stripHashTags(sm.toSkin(event.getPlayer().getName(), p.getItemListModifier().read(0).get(i))));
				        	}
				        	p.getItemListModifier().write(0, neww);
				        	//}
				        	
				        } else if (event.getPacketType() == PacketType.Play.Server.ENTITY_EQUIPMENT){
				        	if (p.getItemSlots().read(0) == ItemSlot.MAINHAND || p.getItemSlots().read(0) == ItemSlot.OFFHAND){
				        		ItemStack item = p.getItemModifier().read(0);
				        		p.getItemModifier().write(0, sm.toSkin(p.getEntityModifier(event).read(0).getName(), item));
				        	}
				        } else if (event.getPacketType() == PacketType.Play.Server.NAMED_SOUND_EFFECT){
				        	if (p.getSoundEffects().read(0) == Sound.ENTITY_ARROW_HIT || p.getSoundEffects().read(0) == Sound.ENTITY_ARROW_HIT_PLAYER){
				        		event.setCancelled(true);
				        	}
				        }
				        
				    }
				    @Override
				    public void onPacketReceiving(PacketEvent event){
				    	PacketContainer p = event.getPacket();
				    	if (p.getType() == PacketType.Play.Client.SET_CREATIVE_SLOT){
				    		p.getItemModifier().write(0, sm.toDefault(p.getItemModifier().read(0)));
				    	}
				    	
				    }
				});
	}
	ItemStack stripHashTags(ItemStack i){
		if (!i.hasItemMeta()) return i;
		ItemMeta meta = i.getItemMeta();
		if (!meta.hasLore()) return i;
		List<String> lore =meta.getLore();
		List<String> ints = new ArrayList<String>(lore);
		for(int j = 0; j < lore.size(); j++){
			if (lore.get(j).contains("#"))  ints.remove(lore.get(j));
		}
		meta.setLore(ints);
		meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
		i.setItemMeta(meta);
		return i;
	}
	int indexConverter(int index){
		int newIndex = index;
		if (index > 35) newIndex-=36;
		else if (index == 45) newIndex = 39;
		else if (index < 9) newIndex +=36;
		return newIndex;
	}
	@EventHandler
	public void onClick(InventoryClickEvent e){
		if (!(e.getWhoClicked() instanceof Player)) return;
		Player p = (Player) e.getWhoClicked();
		final String s = p.getName();
		//Bukkit.broadcastMessage(e.getCurrentItem().getType() + " " + e.getCursor().getType());
		if (e.getClickedInventory() == null || e.getCurrentItem() == null) return;
		if (!(e.getClickedInventory().getName() != null && e.getCurrentItem().getType() != Material.AIR)) return;
		// This was just to see if it was an actual menu
		int page = 1;
		if (ChatColor.stripColor(e.getClickedInventory().getName()).toLowerCase().contains("page")){
			page = Integer.valueOf(ChatColor.stripColor(e.getClickedInventory().getName()).toLowerCase().split("page ")[1]);
		}
		SkinGUI gui = SkinGUI.getInstance();
		if (ChatColor.stripColor(e.getClickedInventory().getName()).toLowerCase().contains("select gun")){
			// Enpty slots in the inventory, with the bottom row being the arrows and back key
			int size = e.getClickedInventory().getSize() - 9;
			if (e.getSlot() < size){
				String name = getGunName(e.getCurrentItem()).toLowerCase();
				p.openInventory(gui.getSkinList(name, size, 1, s));
			
			} else{
				switch (e.getSlot()){
				case 45:{
					if (!isArrow(e.getCurrentItem(), e.getCursor(), ItemsGUI.leftArrow)){
						e.setCancelled(true);
						return;
					}
					if (page > 1){
						page--;
						p.openInventory(gui.getGunList(page, size));
						// Otvori drugu stranu!!!
					}
					e.setCancelled(true);
					break;
				}
				case 49:{
					p.closeInventory();
					break;
				}
				case 53:{
					if (!isArrow(e.getCurrentItem(), e.getCursor(), ItemsGUI.leftArrow)){
						e.setCancelled(true);
						return;
					}
						page++;
						p.openInventory(gui.getGunList(page, size));
						// Otvori drugu stranu!!!
					e.setCancelled(true);
					break;
				}
				default:	e.setCancelled(true);
				}
			}
		} else if (ChatColor.stripColor(e.getClickedInventory().getName()).toLowerCase().contains("select skin")){
			if (e.getSlot() < 45){
				String name = "";
				try{
					name=e.getCurrentItem().getItemMeta().getLore().get(0).replaceAll("#", "");
					String msg = "";
					if (sm.isSelected(s, name)){
						msg = ChatColor.RED + "This skin is already selected!";
					} else
						msg = ChatColor.GREEN + "You successfully changed your skin!";
					if(!sm.selectSkin(s, name)) msg = ChatColor.RED + "You do not own this skin.";
					p.sendMessage(msg);
					p.closeInventory();
				}catch(Exception e1){	}
			}
			else {
				String gunName = ChatColor.stripColor(e.getClickedInventory().getName()).split(": ")[1].split(" -")[0].toLowerCase();
				int size = 45;
				switch (e.getSlot()){
				case 45:{
					if (!isArrow(e.getCurrentItem(), e.getCursor(), ItemsGUI.leftArrow)){
						e.setCancelled(true);
						return;
					}
					if (page > 1){
						page--;
						p.openInventory(gui.getSkinList(gunName, size, page, s));
					}
					e.setCancelled(true);
					break;
				}
				case 49:{
					p.closeInventory();
					p.openInventory(gui.getGunList(1, size));
					break;
				}
				case 53:{
					if (!isArrow(e.getCurrentItem(), e.getCursor(), ItemsGUI.leftArrow)){
						e.setCancelled(true);
						return;
					}
						page++;
						p.openInventory(gui.getSkinList(gunName, size, page, s));
					e.setCancelled(true);
					break;
				}
				default:
				{
					e.setCancelled(true);
					break;
				}
				}
			}
			}
	
	}
	static boolean isArrow(ItemStack c, ItemStack ci, ItemStack a){
		return c.getType() == a.getType() || ci.getType() == a.getType();
	}
	String getGunName(ItemStack i){
		return ChatColor.stripColor(GunManager.getInstance().util.getWeaponTitle(i));
	}
}
