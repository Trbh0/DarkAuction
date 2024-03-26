package me.trbh.darkauction.listeners;

import me.trbh.darkauction.DarkAuction;
import me.trbh.darkauction.utils.ItemsInAuctionPool;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static me.trbh.darkauction.listeners.DarkAuctionStart.*;
import static me.trbh.darkauction.listeners.InteractWithArmorStandListener.playerInteracted;
import static me.trbh.darkauction.utils.SomeCoolMethods.*;

public class AuctionMenuClickListener implements Listener {

    public static Player highestBidder;
    public static Inventory replacedInventory;
    public static boolean isRunning = false;
    public static int highestBidValue = startAmount;
    private BukkitTask timersId;
    private List<Material> ores = Arrays.asList(Material.IRON_INGOT, Material.GOLD_INGOT, Material.DIAMOND, Material.EMERALD);

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (DarkAuctionStart.itemInside == null) {
            return;
        }

        String name = DarkAuctionStart.itemInside.getItemMeta().getDisplayName();
        String itemName = ChatColor.translateAlternateColorCodes('&', "&r" + ChatColor.stripColor(name));
        String round = " - Round ";
        String fullName = itemName + round;

        if (!e.getView().getTitle().startsWith(fullName)) {
            return;
        }

        if (e.getCurrentItem() == null) {
            return;
        }

        Player p = (Player) e.getWhoClicked();

        if (e.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', "&cClose"))) {
            p.closeInventory();
        }

        e.setCancelled(true);

        if (timeLeft == null || !timeLeft.isValid()) {
            return;
        }

        replacedInventory = e.getInventory();

        ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) playerHead.getItemMeta();

        meta.setOwner(p.getName());
        meta.setDisplayName(e.getCurrentItem().getItemMeta().getDisplayName());

        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + ChatColor.stripColor(p.getDisplayName()) + ChatColor.RED + " has already bid this amount!");

        meta.setLore(lore);
        playerHead.setItemMeta(meta);

        Economy eco = DarkAuction.getEconomy();

        double clickedValue = extractCoinsValue(e.getCurrentItem().getItemMeta().getDisplayName());
        int clickedIndex = e.getSlot();

        if (slotNumbers.contains(clickedIndex) && ores.contains(e.getCurrentItem().getType())) {

            int slot = getIndexByValue(slotNumbers, clickedIndex);

            if (eco.getBalance(p) >= clickedValue) {

                replacedInventory.setItem(clickedIndex, playerHead);

                for (int i = 0; i < slot; i++) {

                    ItemStack barrier = new ItemStack(Material.BARRIER, i + 1);
                    ItemMeta barMeta = barrier.getItemMeta();
                    barMeta.setDisplayName(replacedInventory.getItem(slotNumbers.get(i)).getItemMeta().getDisplayName());

                    ArrayList<String> barLore = new ArrayList<>();
                    barLore.add(ChatColor.RED + "Someone else has already made a higher bid than this!");

                    barMeta.setLore(barLore);
                    barrier.setItemMeta(barMeta);

                    if (replacedInventory.getItem(slotNumbers.get(i)).getType().equals(Material.PLAYER_HEAD)) {

                        for (int j = 0; j < slot + 1; j++) {

                            if (Objects.equals(replacedInventory.getItem(slotNumbers.get(j)).getItemMeta().getLore(), replacedInventory.getItem(slotNumbers.get(slot)).getItemMeta().getLore()) && j != slot) {

                                replacedInventory.setItem(slotNumbers.get(j), barrier);

                            }
                        }
                    }
                    else {
                        replacedInventory.setItem(slotNumbers.get(i), barrier);
                    }

                }

                for (Player player : daWorld.getPlayers()) {

                    player.sendMessage(siriusVoice(p.getDisplayName() + " placed a bid of &6" + eco.format(clickedValue).substring(1) + " Coins&r!"));
                    playSiriusSound(player);
                }

                highestBidder = p;

                highestBidValue = extractCoinsValue(e.getCurrentItem().getItemMeta().getDisplayName());
                highestBid.setCustomName("Highest Bid: " + ChatColor.GOLD + eco.format(highestBidValue).substring(1) + " Coins");

                if (replacedInventory.getItem(slotNumbers.get(24)).getType().equals(Material.PLAYER_HEAD)) {

                    ItemStack lastToFirst = replacedInventory.getItem(slotNumbers.get(24));

                    Bukkit.getScheduler().scheduleSyncDelayedTask(DarkAuction.getPlugin(), () -> {

                        jump++;
                        nextPage(replacedInventory);
                        replacedInventory.setItem(slotNumbers.get(0), lastToFirst);

                    }, (2 * 20L));

                }

                timeLeft.setCustomName(removeNumber(timeLeft.getCustomName()) + 10);

                if (replacedInventory.getItem(4).getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "Midas' Sword")) {
                    replacedInventory.setItem(4, ItemsInAuctionPool.getMidasSword());
                    itemInside = ItemsInAuctionPool.getMidasSword();
                }

            }
            else {
                p.sendMessage(ChatColor.RED + "You don't have enough coins to bid this amount!");
                p.sendMessage(ChatColor.RED + "You need " + ChatColor.GOLD + (clickedValue - eco.getBalance(p)) + " coins" + ChatColor.RED + " extra to bid!");
                p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 0.3F);
            }
        }

        if (!isRunning) {

            timersId = Bukkit.getScheduler().runTaskTimer(DarkAuction.getPlugin(), () -> {

                if (extractNumber(timeLeft.getCustomName()) == 0) {

                    timersId.cancel();
                    for (Player pInter : Bukkit.getOnlinePlayers()) {
                        if (pInter.getWorld().getName().equals("world_dark_auction")) {
                            pInter.closeInventory();
                        }
                    }
                    playerInteracted.clear();

                    if (highestBidder != null) {

                        EconomyResponse response = eco.withdrawPlayer(highestBidder, highestBidValue);

                        if (response.transactionSuccess()) {

                            ItemStack itemToGive = itemInside;
                            highestBidder.getInventory().addItem(itemToGive);

                        }
                    }

                    // moved to DarkAuctionStart lines 226-256

                }

                String timerName = replacedInventory.getItem(5).getItemMeta().getDisplayName();
                String timerNameText = removeNumber(timerName);

                ItemStack newClock = new ItemStack(Material.CLOCK, extractNumber(timeLeft.getCustomName()));
                ItemMeta newClockMeta = newClock.getItemMeta();
                newClockMeta.setDisplayName(timerNameText + extractNumber(timeLeft.getCustomName()));
                newClock.setItemMeta(newClockMeta);

                replacedInventory.setItem(5, newClock);

                isRunning = true;

            }, 0, 5L);

        }


    }
}
