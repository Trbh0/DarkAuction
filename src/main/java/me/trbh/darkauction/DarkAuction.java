package me.trbh.darkauction;

import me.trbh.darkauction.listeners.*;
import me.trbh.darkauction.npc.NPCClass;
import me.trbh.darkauction.npc.SpawnSirius;
import me.trbh.darkauction.utils.SomeCoolMethods;
import me.trbh.darkauction.listeners.ChangeWorldListener;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class DarkAuction extends JavaPlugin {

    private static DarkAuction plugin;

    private static Economy econ = null;
    public static NamespacedKey itemIdKey;

    public static DarkAuction getPlugin(){
        return plugin;
    }

    @Override
    public void onEnable() {

        saveDefaultConfig();

        plugin = this;

        itemIdKey = new NamespacedKey(this, "id");

        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getServer().getPluginManager().registerEvents(new PreventInteractingInDarkAuction(), this);
        getServer().getPluginManager().registerEvents(new InteractWithArmorStandListener(), this);
        getServer().getPluginManager().registerEvents(new AuctionMenuClickListener(), this);
        getServer().getPluginManager().registerEvents(new SpawnSirius(), this);
        getServer().getPluginManager().registerEvents(new ChangeWorldListener(), this);
        getServer().getPluginManager().registerEvents(new NPCClass(), this);
        getServer().getPluginManager().registerEvents(new MidasSwordHandListener(), this);

        SpawnSirius.checkHour();
        Bukkit.getScheduler().runTaskLater(getPlugin(), NPCClass::spawnNPCs, 20L);
        Bukkit.getScheduler().runTaskLater(getPlugin(), () -> SomeCoolMethods.daWorld = Bukkit.getWorld("world_dark_auction"), 20L);

    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy(){
        return econ;
    }
}
