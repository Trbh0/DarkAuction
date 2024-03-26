package me.trbh.darkauction.utils;

import me.trbh.darkauction.listeners.DarkAuctionStart;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static me.trbh.darkauction.listeners.DarkAuctionStart.glassStand;
import static me.trbh.darkauction.listeners.DarkAuctionStart.timeLeft;

public class SomeCoolMethods {

    public static final List<Integer> slotNumbers = Arrays.asList(9, 18, 27, 36, 45, 46, 47, 38, 29, 20, 21, 22, 31, 40, 49, 50, 51, 42, 33, 24, 25, 26, 35, 44, 53);
    public static int startAmount = 5000;
    public static int amount = startAmount;
    public static int jump = 1;
    public static int startMultiplier = 1;
    public static int multiplier = startMultiplier;
    public static World daWorld;

    public static int extractCoinsValue(String string) {

        Pattern pattern = Pattern.compile("\\b\\d{1,3}(?:,\\d{3})*(?:\\.\\d+)?\\b");
        Matcher matcher = pattern.matcher(string);

        if (matcher.find()) {
            String match = matcher.group();
            match = match.replaceAll(",", "");
            return Integer.parseInt(match);
        }

        return 0;
    }

    public static int extractNumber(String input) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }

        return 0;
    }

    public static String removeNumber(String input) {
        Pattern pattern = Pattern.compile("\\s*\\d+$");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return input.substring(0, matcher.start());
        }

        return input;
    }

    public static int getIndexByValue(List<Integer> list, int value) {

        for (int i = 0; i < list.size(); i++) {

            if (list.get(i) == value) {
                return i;
            }
        }

        return -1;
    }

    public static void firstPage(Inventory inventory) {

        amount = startAmount;
        multiplier = startMultiplier;

        pageTheSame(inventory);

    }

    public static Inventory replaceableInv(Inventory inventory) {
        inventory.setItem(4, DarkAuctionStart.itemInside);
        InventoryUtils.addItemToInventory(inventory, Material.CLOCK, extractNumber(timeLeft.getCustomName()), 5, "&eTime Left: &b" + extractNumber(timeLeft.getCustomName()), false);

        firstPage(inventory);

        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null) {
                InventoryUtils.addItemToInventory(inventory, Material.BLACK_STAINED_GLASS_PANE, 1, i, " ", false);
            }
        }

        return inventory;
    }

    public static void nextPage(Inventory inventory) {

        amount -= 5 * 10 * multiplier;

        pageTheSame(inventory);
    }

    private static void pageTheSame(Inventory inventory) {

        int iron0Threshold = 7_500;
        int ironThreshold = 10_000;
        int gold0Threshold = 15_000;
        int goldThreshold = 30_000;
        int diamond0Threshold = 55_000;
        int diamondThreshold = 105_000;
        int emerald0Threshold = 155_000;
        boolean glint;
        Material oreMaterial;

        for (int i = 0; i < slotNumbers.size(); i++) {

            if (amount < iron0Threshold) {
                oreMaterial = Material.IRON_INGOT;
                glint = false;
            }
            else if (amount < ironThreshold) {
                glint = true;
                oreMaterial = Material.IRON_INGOT;
            }
            else if (amount < gold0Threshold) {
                glint = false;
                oreMaterial = Material.GOLD_INGOT;
            }
            else if (amount < goldThreshold) {
                glint = true;
                oreMaterial = Material.GOLD_INGOT;
            }
            else if (amount < diamond0Threshold) {
                glint = false;
                oreMaterial = Material.DIAMOND;
            }
            else if (amount < diamondThreshold) {
                glint = true;
                oreMaterial = Material.DIAMOND;
            }
            else if (amount < emerald0Threshold) {
                glint = false;
                oreMaterial = Material.EMERALD;
            }
            else {
                glint = true;
                oreMaterial = Material.EMERALD;
            }

            if ((i + 1) % 5 == 0) {
                multiplier += jump;
            }

            InventoryUtils.addItemToInventory(
                    inventory,
                    oreMaterial,
                    i + 1,
                    slotNumbers.get(i),
                    "&6Bid " + String.format(Locale.US, "%,d", amount) + " Coins",
                    glint,
                    ChatColor.YELLOW + "Click to place bid!");
            amount += 5 * 10 * multiplier;
        }
    }

    public static void spawnFirework() {
        Firework fw = glassStand.getWorld().spawn(glassStand.getLocation().add(0, 2, 0), Firework.class);
        FireworkMeta fwMeta = fw.getFireworkMeta();
        fwMeta.addEffect(FireworkEffect.builder().withColor(Color.RED).with(FireworkEffect.Type.STAR).withFlicker().build());
        fwMeta.setPower(1);
        fw.setFireworkMeta(fwMeta);
    }

    public static ItemStack setMainItem(Material material, String displayName, List<String> loreList) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName(displayName);

        itemMeta.setLore(loreList);

        item.setItemMeta(itemMeta);

        return item;
    }

    public static String siriusVoice(String str) {

        return ChatColor.translateAlternateColorCodes('&', "&e[NPC] &6Sirius&r: " + str);
    }

    public static void playSiriusSound(World world) {

        Random random = new Random();

        for (Player player : world.getPlayers()) {

            int randomNumber = random.nextInt(2);

            if (randomNumber == 0) {
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 0.5F, 1.0F);
            }
            else {
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 0.5F, 1.0F);
            }
        }

    }

    public static void playSiriusSound(Player player) {

        Random random = new Random();

        int randomNumber = random.nextInt(2);

        if (randomNumber == 0) {
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 0.5F, 1.0F);
        }
        else {
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 0.5F, 1.0F);
        }

    }

    public static void throwPotion(World world, Location location) {

        ItemStack splashPotion = new ItemStack(Material.SPLASH_POTION);
        PotionMeta meta = (PotionMeta) splashPotion.getItemMeta();
        meta.setBasePotionData(new PotionData(PotionType.WATER));
        splashPotion.setItemMeta(meta);
        ThrownPotion thrownPotion = world.spawn(location, ThrownPotion.class);
        thrownPotion.setItem(splashPotion);

    }

    public static int randomIntInRange(int min, int max) {

        Random random = new Random();
        return random.nextInt(max - min + 1) + min;

    }

    public static ItemMeta customEnchantedBooksMeta(ItemStack item, String enchantName, int level) {

        Enchantment enchant = Enchantment.getByKey(NamespacedKey.minecraft(enchantName.toLowerCase()));

        if (enchant == null) {
            System.out.println();
            System.out.println("Enchantment is null");
            System.out.println();
        }
        assert enchant != null;

        ItemMeta meta = item.getItemMeta();

        if (meta instanceof EnchantmentStorageMeta) {
            ((EnchantmentStorageMeta) meta).addStoredEnchant(enchant, level, true);
        }
        //meta.addEnchant(enchant, level, true); it was duplicating the name of the enchantment

        return meta;
    }

    public static String parseEnchantmentName(String input) {

        Pattern pattern = Pattern.compile("\\[(.*?)\\]=(\\d+)");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {

            String enchantmentInfo = matcher.group(1);
            int level = Integer.parseInt(matcher.group(2));

            String[] parts = enchantmentInfo.split(",");
            String enchantmentName = parts[0].split(":")[1];

            enchantmentName = enchantmentName.substring(0, 1).toUpperCase() + enchantmentName.substring(1);

            String romanLevel = convertToRoman(level);

            return enchantmentName + " " + romanLevel;
        }
        else {
            return "Invalid input";
        }
    }

    public static String convertToRoman(int num) {
        if (num < 1 || num > 3999) {
            return "Invalid number";
        }

        String[] romanSymbols = {"I", "IV", "V", "IX", "X", "XL", "L", "XC", "C", "CD", "D", "CM", "M"};
        int[] values = {1, 4, 5, 9, 10, 40, 50, 90, 100, 400, 500, 900, 1000};
        StringBuilder romanNumeral = new StringBuilder();

        int i = values.length - 1;
        while (num > 0) {
            if (num >= values[i]) {
                romanNumeral.append(romanSymbols[i]);
                num -= values[i];
            }
            else {
                i--;
            }
        }

        return romanNumeral.toString();
    }
}
