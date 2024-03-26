package me.trbh.darkauction.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class InventoryUtils {

    public static Inventory createInventory(String name, int size) {
        if (size % 9 != 0) {
            throw new IllegalArgumentException("Inventory size must be a multiple of 9.");
        }

        Inventory inventory = Bukkit.createInventory(null, size, name);
        return inventory;
    }

    public static void addItemToInventory(Inventory inventory, Material material, int amount, int slot, String itemName, Boolean setGlint, String... lore) {
        ItemStack itemStack = new ItemStack(material, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();

        if (itemName != null) {
            itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemName));
        }

        if (lore != null && lore.length > 0) {
            for (int i = 0; i < lore.length; i++) {
                lore[i] = MessageUtils.hex(lore[i]);
            }
            itemMeta.setLore(Arrays.asList(lore));
        }

        if(setGlint){
            itemMeta.addEnchant(Enchantment.DURABILITY, 0, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        itemStack.setItemMeta(itemMeta);

        if (slot >= 0 && slot < inventory.getSize()) {
            inventory.setItem(slot, itemStack);
        }
        else {
            inventory.addItem(itemStack);
        }
    }

    public static String clickedInvName(InventoryView inventoryView) {
        return inventoryView.getTitle();
    }
}
