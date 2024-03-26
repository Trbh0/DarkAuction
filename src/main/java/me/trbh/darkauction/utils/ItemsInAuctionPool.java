package me.trbh.darkauction.utils;

import com.willfp.eco.core.items.Items;
import com.willfp.eco.core.items.builder.ItemStackBuilder;
import me.trbh.darkauction.DarkAuction;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static me.trbh.darkauction.listeners.AuctionMenuClickListener.highestBidValue;
import static me.trbh.darkauction.utils.SomeCoolMethods.customEnchantedBooksMeta;
import static me.trbh.darkauction.utils.SomeCoolMethods.setMainItem;

public class ItemsInAuctionPool {

    public static List<ItemStack> itemsList = new ArrayList<>();
    public static List<ItemStack> enchantsList = new ArrayList<>();

    public static ItemStack getPlasmaNucleus(){

        ItemStack itemInside = new ItemStackBuilder(Items.lookup("ecoitems:plasma_nucleus").getItem()).build();

        return itemInside;
    }

    public static ItemStack getMidasSword(){

        ItemStack itemStack = new ItemStackBuilder(Items.lookup("ecoitems:midas_sword").getItem()).build();

        Economy eco = DarkAuction.getEconomy();

        double bonusDamage;
        BigDecimal bd;
        int decimalPlaces = 2;
        String payed = eco.format(highestBidValue).substring(1);

        if(highestBidValue < 10_000){
            bd = BigDecimal.valueOf(((double) (highestBidValue)) / 5_000).setScale(decimalPlaces, RoundingMode.HALF_UP);
        }
        else if(highestBidValue < 25_000){
            bd = BigDecimal.valueOf(2 + (((double) (highestBidValue)) - 10_000) / 10_000).setScale(decimalPlaces, RoundingMode.HALF_UP);
        }
        else if(highestBidValue < 75_000){
            bd = BigDecimal.valueOf(3.5 + (((double) (highestBidValue)) - 25_000) / 20_000).setScale(decimalPlaces, RoundingMode.HALF_UP);
        }
        else if(highestBidValue < 250_000){
            bd = BigDecimal.valueOf(6 + (((double) (highestBidValue)) - 75_000) / 50_000).setScale(decimalPlaces, RoundingMode.HALF_UP);
        }
        else if(highestBidValue < 500_000){
            bd = BigDecimal.valueOf(9.5 + (((double) (highestBidValue)) - 250_000) / 100_000).setScale(decimalPlaces, RoundingMode.HALF_UP);
        }
        else {
            bd = BigDecimal.valueOf(12).setScale(decimalPlaces, RoundingMode.HALF_UP);
        }
        bonusDamage = bd.doubleValue();

        ItemMeta meta = itemStack.getItemMeta();
        List<String> itemInsideLore = meta.getLore();

        //so it doesn't return things like 16.0
        String amount15;
        String amount;

        if(bonusDamage == (int) bonusDamage){
            amount15 = String.valueOf((int) (15 + bonusDamage));
            amount = String.valueOf((int) bonusDamage);
        }
        else {
            amount15 = String.valueOf(15 + bonusDamage);
            amount = String.valueOf(bonusDamage);
        }

        for (int i = 0; i < itemInsideLore.size(); i++) {
            String currentString = itemInsideLore.get(i);
            currentString = currentString.replace("%15amount%", amount15);
            currentString = currentString.replace("%amount%", amount);
            currentString = currentString.replace("%payed%", payed);
            itemInsideLore.set(i, currentString);
        }
        meta.setLore(itemInsideLore);

        //add damage
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "set_damage", bonusDamage, AttributeModifier.Operation.ADD_NUMBER);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);

        itemStack.setItemMeta(meta);

        return itemStack;
    }

    public static ItemStack getWitherArtifact(){

        ItemStack itemStack = new ItemStackBuilder(Items.lookup("talismans:wither_artifact").getItem()).build();

        return itemStack;
    }

    public static ItemStack getNetherArtifact(){

        ItemStack itemStack = new ItemStackBuilder(Items.lookup("talismans:nether_artifact").getItem()).build();

        return itemStack;
    }

    public static ItemStack getEnderArtifact(){

        ItemStack itemStack = new ItemStackBuilder(Items.lookup("talismans:ender_artifact").getItem()).build();

        return itemStack;
    }

    public static ItemStack getGrowth6(){

        ItemStack itemInside;
        Material itemInsideMaterial = Material.ENCHANTED_BOOK;
        String itemInsideName = "&eEnchanted Book";
        List<String> itemInsideLore = new ArrayList<>();
        itemInsideLore.add(ChatColor.translateAlternateColorCodes('&', "&7Growth VI"));
        itemInsideLore.add(ChatColor.translateAlternateColorCodes('&', "&8Grants &a+7 &c❤ Health"));

        itemInside = setMainItem(
                itemInsideMaterial,
                ChatColor.translateAlternateColorCodes('&', itemInsideName),
                itemInsideLore);

        ItemMeta meta = customEnchantedBooksMeta(itemInside, "growth", 6);

        itemInside.setItemMeta(meta);

        return itemInside;
    }

    public static ItemStack getProtection6(){

        ItemStack itemInside = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta itemMeta = itemInside.getItemMeta();

        List<String> itemInsideLore = new ArrayList<>();
        itemInsideLore.add(ChatColor.translateAlternateColorCodes('&', "&8Gives a &a+6 🛡 Defense"));

        itemMeta.setLore(itemInsideLore);
        itemInside.setItemMeta(itemMeta);

        ItemMeta meta = customEnchantedBooksMeta(itemInside, "protection", 6);
        itemInside.setItemMeta(meta);

        return itemInside;
    }

    public static ItemStack getSharpness6(){

        ItemStack itemInside = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta itemMeta = itemInside.getItemMeta();

        List<String> itemInsideLore = new ArrayList<>();
        itemInsideLore.add(ChatColor.translateAlternateColorCodes('&', "&8Deals &a4 &8bonus melee damage"));

        itemMeta.setLore(itemInsideLore);
        itemInside.setItemMeta(itemMeta);

        ItemMeta meta = customEnchantedBooksMeta(itemInside, "sharpness", 6);
        itemInside.setItemMeta(meta);

        return itemInside;
    }

    public static ItemStack getPower6(){

        ItemStack itemInside = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta itemMeta = itemInside.getItemMeta();

        List<String> itemInsideLore = new ArrayList<>();
        itemInsideLore.add(ChatColor.translateAlternateColorCodes('&', "&8Gives a &a175% &8bonus arrow damage"));

        itemMeta.setLore(itemInsideLore);
        itemInside.setItemMeta(itemMeta);

        ItemMeta meta = customEnchantedBooksMeta(itemInside, "power", 6);
        itemInside.setItemMeta(meta);

        return itemInside;
    }
}
