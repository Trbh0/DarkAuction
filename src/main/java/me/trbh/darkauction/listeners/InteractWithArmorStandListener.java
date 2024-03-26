package me.trbh.darkauction.listeners;

import me.trbh.darkauction.DarkAuction;
import me.trbh.darkauction.utils.InventoryUtils;
import me.trbh.darkauction.utils.SomeCoolMethods;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static me.trbh.darkauction.listeners.DarkAuctionStart.daMenu;

public class InteractWithArmorStandListener implements Listener {

    public static int roundNumber = 1;
    public static int maxRoundNumber = 3;
    public static Set<Player> playerInteracted = new HashSet<>();

    @EventHandler
    public void onInteractWithDisplay(PlayerArmorStandManipulateEvent e){

        Player p = e.getPlayer();
        playerInteracted.add(p);
        ItemStack item = e.getArmorStandItem();

        if(!item.hasItemMeta()){
            return;
        }

        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();

        if(!Objects.equals(container.get(DarkAuction.itemIdKey, PersistentDataType.STRING), "GLASS_DISPLAY")){
            return;
        }

        e.setCancelled(true);

        inventoryCheck();

        p.openInventory(daMenu);
    }

    public static Inventory previewInventory(){

        String name = DarkAuctionStart.itemInside.getItemMeta().getDisplayName();
        String itemName = ChatColor.translateAlternateColorCodes('&', "&r" + ChatColor.stripColor(name));
        String round = " - Round ";

        String fullName = itemName + round + InteractWithArmorStandListener.roundNumber;

        Inventory previewInventory = InventoryUtils.createInventory(fullName, 9 * 6);

        previewInventory.setItem(22, DarkAuctionStart.itemInside);
        InventoryUtils.addItemToInventory(previewInventory, Material.BARRIER, 1, 49, "&cClose", false);

        for (int i = 0; i < previewInventory.getSize(); i++) {
            if(previewInventory.getItem(i) == null){
                InventoryUtils.addItemToInventory(previewInventory, Material.BLACK_STAINED_GLASS_PANE, 1, i, " ", false);
            }
        }

        return previewInventory;
    }

    public static void inventoryCheck(){

        if(DarkAuctionStart.timeLeft == null || !DarkAuctionStart.timeLeft.isValid()){
            daMenu = InteractWithArmorStandListener.previewInventory();
        }
        else {
            String name = DarkAuctionStart.itemInside.getItemMeta().getDisplayName();
            String itemName = ChatColor.translateAlternateColorCodes('&', "&r" + ChatColor.stripColor(name));
            String round = " - Round ";

            String fullName = itemName + round + InteractWithArmorStandListener.roundNumber;

            if(daMenu == null || AuctionMenuClickListener.replacedInventory == null){
                daMenu = SomeCoolMethods.replaceableInv(InventoryUtils.createInventory(fullName, 9 * 6));
            }
            else {
                daMenu = AuctionMenuClickListener.replacedInventory;
            }
        }
    }
}
