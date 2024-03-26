package me.trbh.darkauction.listeners;

import me.trbh.darkauction.DarkAuction;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.Collections;

import static me.trbh.darkauction.listeners.InteractWithArmorStandListener.playerInteracted;
import static me.trbh.darkauction.listeners.InteractWithArmorStandListener.roundNumber;
import static me.trbh.darkauction.utils.ItemsInAuctionPool.*;
import static me.trbh.darkauction.utils.SomeCoolMethods.*;

public class DarkAuctionStart {

    public static ItemStack itemInside;
    public static Item itemInsideOnGround;
    public static ArmorStand title;
    public static ArmorStand glassStand;
    public static ArmorStand highestBid;
    public static ArmorStand timeLeft;
    public static ArmorStand clickToBid;
    public static Inventory daMenu;
    public static Location standLocation;
    public static int siriusCountdown = 5;
    private static int timerId = 0;
    private static BukkitTask previewTimerId;
    private static final Location auctionLocation = new Location(Bukkit.getWorld("world_dark_auction"), 17, 4, 17);
    private static int bottleSmasherId;
    private static int bottlesSmashed = 0;

    public static void runAuction() {

        itemsList.add(getPlasmaNucleus());
        itemsList.add(getMidasSword());
        itemsList.add(getWitherArtifact());
        itemsList.add(getNetherArtifact());
        itemsList.add(getEnderArtifact());
        enchantsList.add(getGrowth6());
        enchantsList.add(getProtection6());
        enchantsList.add(getSharpness6());
        enchantsList.add(getPower6());
        Collections.shuffle(itemsList);
        Collections.shuffle(enchantsList);

        roundNumber = 1;

        standLocation = auctionLocation;

        Bukkit.getScheduler().runTaskLater(DarkAuction.getPlugin(), () -> {

            daWorld.getPlayers().forEach(player -> player.sendMessage(siriusVoice("Come on down, to the basement, the auction is about to begin!")));
            playSiriusSound(daWorld);

            Bukkit.getScheduler().runTaskLater(DarkAuction.getPlugin(), () -> {

                daWorld.getPlayers().forEach(player -> player.sendMessage(siriusVoice("Welcome to the &5Dark Auction&r! I hope everyone's ready, the first item is about to come out!")));
                playSiriusSound(daWorld);

                bottlesSmashed = 0;
                bottleSmasherId = Bukkit.getScheduler().scheduleSyncRepeatingTask(DarkAuction.getPlugin(), () -> {

                    if (bottlesSmashed >= 40) {
                        Bukkit.getScheduler().cancelTask(bottleSmasherId);
                    }

                    double potX = randomIntInRange(7, 27) + 0.5;
                    double potY = randomIntInRange(6, 10) + 0.5;
                    double potZ = randomIntInRange(9, 25) + 0.5;
                    throwPotion(daWorld, new Location(daWorld, potX, potY, potZ));

                    bottlesSmashed++;

                }, 0, 2L);

                Bukkit.getScheduler().runTaskLater(DarkAuction.getPlugin(), () -> auctionCycle(standLocation), 10 * 20L);

            }, 20 * 20L);

        }, 5 * 20L);

    }

    private static ArmorStand armorStandProps(Location location, double y, String text) {

        Location titleLocation = new Location(Bukkit.getWorld("world_dark_auction"),
                location.getBlockX() + 0.5,
                location.getBlockY() - y,
                location.getBlockZ() + 0.5);

        ArmorStand armorStand = (ArmorStand) Bukkit.getWorld("world_dark_auction").spawnEntity(titleLocation, EntityType.ARMOR_STAND);
        armorStand.setCustomName(ChatColor.translateAlternateColorCodes('&', text));
        armorStand.setInvulnerable(true);
        armorStand.setInvisible(true);
        armorStand.setGravity(false);
        armorStand.setCustomNameVisible(true);

        return armorStand;
    }

    public static void auctionCycle(Location location) {

        daMenu = null;
        AuctionMenuClickListener.replacedInventory = null;
        jump = 1;
        AuctionMenuClickListener.isRunning = false;
        highestBid = null;
        siriusCountdown = 5;
        AuctionMenuClickListener.highestBidder = null;

        previewTimerId = Bukkit.getScheduler().runTaskTimer(DarkAuction.getPlugin(), () -> {

            if (siriusCountdown == 0) {

                previewTimerId.cancel();

                Bukkit.getScheduler().runTaskLater(DarkAuction.getPlugin(), () -> {

                    InteractWithArmorStandListener.inventoryCheck();

                    for (Player pInter : playerInteracted) {
                        if (pInter.getWorld().getName().equals("world_dark_auction")) {
                            pInter.openInventory(daMenu);
                        }
                    }
                    playerInteracted.clear();

                }, 1L);

            }

            for (Player pInter : daWorld.getPlayers()) {

                pInter.sendMessage(siriusVoice("The bidding will start in &b" + siriusCountdown + "&r..."));
                playSiriusSound(pInter);

            }

            siriusCountdown--;

        }, 0, 20L);

        Economy eco = DarkAuction.getEconomy();
        //item inside as a drop
        if (roundNumber == 2) {
            itemInside = enchantsList.get(roundNumber - 1);
        }
        else {
            itemInside = itemsList.get(roundNumber - 1);
        }

        Location tempLocation = new Location(Bukkit.getWorld("world_dark_auction"),
                location.getBlockX() + 0.5,
                location.getBlockY(),
                location.getBlockZ() + 0.5);
        itemInsideOnGround = tempLocation.getWorld().dropItem(tempLocation, itemInside);
        itemInsideOnGround.setVelocity(new Vector(0, 0, 0));

        //glass
        Location glassBlockLocation = new Location(Bukkit.getWorld("world_dark_auction"),
                location.getBlockX() + 0.5,
                location.getBlockY() - 1.38,
                location.getBlockZ() + 0.5);

        glassStand = (ArmorStand) Bukkit.getWorld("world_dark_auction").spawnEntity(glassBlockLocation, EntityType.ARMOR_STAND);
        ItemStack glassItem = new ItemStack(Material.GLASS);
        ItemMeta glassMeta = glassItem.getItemMeta();

        PersistentDataContainer glassContainer = glassMeta.getPersistentDataContainer();
        glassContainer.set(DarkAuction.itemIdKey, PersistentDataType.STRING, "GLASS_DISPLAY");
        glassItem.setItemMeta(glassMeta);

        glassStand.getEquipment().setHelmet(glassItem);
        glassStand.setInvulnerable(true);
        glassStand.setInvisible(true);
        glassStand.setGravity(false);
        glassStand.setAI(false);

        String titleText;
        if (itemInside.getType().equals(Material.ENCHANTED_BOOK)) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) itemInside.getItemMeta();
            String name = String.valueOf(meta.getStoredEnchants());
            name = parseEnchantmentName(name);
            titleText = ChatColor.DARK_PURPLE + name;
        }
        else {
            titleText = itemInside.getItemMeta().getDisplayName();
        }

        String highestBidText = "Highest Bid: " + ChatColor.GOLD + eco.format(startAmount).substring(1) + " Coins";
        String timeLeftText = "Time Left: &b15";
        String clickToBidText = "&eClick to bid!";

        //title
        title = armorStandProps(location, 0.1, titleText);

        //highest bid
        highestBid = armorStandProps(location, 0.5, highestBidText);

        //click to bid
        clickToBid = armorStandProps(location, 1.3, clickToBidText);

        Bukkit.getScheduler().runTaskLater(DarkAuction.getPlugin(), () -> {

            //time left
            timeLeft = armorStandProps(location, 0.9, timeLeftText);

            timerId = Bukkit.getScheduler().scheduleSyncRepeatingTask(DarkAuction.getPlugin(), () -> {

                if (extractNumber(timeLeft.getCustomName()) == 1) {

                    Bukkit.getScheduler().cancelTask(timerId);

                    InteractWithArmorStandListener.roundNumber++;

                    Bukkit.getScheduler().runTaskLater(DarkAuction.getPlugin(), () -> {

                        if (InteractWithArmorStandListener.roundNumber <= InteractWithArmorStandListener.maxRoundNumber) {
                            auctionCycle(standLocation);
                        }
                        else {

                            daWorld.getPlayers().forEach(player -> player.sendMessage(siriusVoice("Alright show's over folks! Come back in &b6 nights&r for the next auction.")));
                            playSiriusSound(daWorld);

                            playerInteracted.clear();

                            Bukkit.getScheduler().runTaskLater(DarkAuction.getPlugin(), () -> {

                                daWorld.getPlayers().forEach(player -> player.teleport(Bukkit.getWorld(DarkAuction.getPlugin().getConfig().getString("sirius-world-name")).getSpawnLocation()));

                            }, 5 * 20L);
                        }

                    }, 3 * 20L);

                    glassStand.remove();
                    title.remove();
                    highestBid.remove();
                    timeLeft.remove();
                    clickToBid.remove();
                    itemInsideOnGround.remove();

                    spawnFirework();

                }

                timeLeft.setCustomName(removeNumber(timeLeft.getCustomName()) + (extractNumber(timeLeft.getCustomName()) - 1));

            }, 20L, 20L);

        }, 5 * 20L);


    }
}
