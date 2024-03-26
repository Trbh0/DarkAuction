package me.trbh.darkauction.listeners;

import com.willfp.ecoskills.api.EcoSkillsAPI;
import com.willfp.ecoskills.api.modifiers.ModifierOperation;
import com.willfp.ecoskills.api.modifiers.StatModifier;
import com.willfp.ecoskills.stats.Stats;
import me.trbh.darkauction.DarkAuction;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MidasSwordHandListener implements Listener {

    private final String MIDAS_SWORD_DISPLAY_NAME = ChatColor.GOLD + "Midas' Sword";
    private final Pattern STRENGTH_PATTERN = Pattern.compile("Strength: \\+([0-9]+(?:\\.[0-9]+)?)");
    private ItemStack droppedMidas;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onItemPickup(EntityPickupItemEvent e){
        if(e.isCancelled()){
            return;
        }

        if(!isMidasSword(e.getItem().getItemStack())){
            return;
        }

        Player p = (Player) e.getEntity();
        ItemStack itemPicked = e.getItem().getItemStack();
        Bukkit.getScheduler().runTaskLater(DarkAuction.getPlugin(), () -> {

            if(itemPicked.equals(p.getInventory().getItemInMainHand())){

                EcoSkillsAPI.addStatModifier(p, new StatModifier(UUID.randomUUID(), Stats.INSTANCE.getByID("strength"), extractStrength(p.getInventory().getItemInMainHand()), ModifierOperation.ADD));
            }
        }, 1L);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();

        if(!isMidasSword(p.getInventory().getItemInMainHand())){
            return;
        }

        EcoSkillsAPI.addStatModifier(p, new StatModifier(UUID.randomUUID(), Stats.INSTANCE.getByID("strength"), extractStrength(p.getInventory().getItemInMainHand()), ModifierOperation.ADD));
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryDrop(PlayerDropItemEvent e){
        if(e.isCancelled()){
            return;
        }

        if(!isMidasSword(e.getItemDrop().getItemStack())){
            return;
        }

        droppedMidas = e.getItemDrop().getItemStack();

        EcoSkillsAPI.addStatModifier(e.getPlayer(), new StatModifier(UUID.randomUUID(), Stats.INSTANCE.getByID("strength"), -extractStrength(e.getItemDrop().getItemStack()), ModifierOperation.ADD));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChangeSlot(PlayerItemHeldEvent e){
        Player p = e.getPlayer();

        ItemStack oldItem = p.getInventory().getItem(e.getPreviousSlot());
        ItemStack newItem = p.getInventory().getItem(e.getNewSlot());

        if(isMidasSword(newItem)){
            EcoSkillsAPI.addStatModifier(e.getPlayer(), new StatModifier(UUID.randomUUID(), Stats.INSTANCE.getByID("strength"), extractStrength(newItem), ModifierOperation.ADD));
        }

        if(isMidasSword(oldItem)){
            EcoSkillsAPI.addStatModifier(e.getPlayer(), new StatModifier(UUID.randomUUID(), Stats.INSTANCE.getByID("strength"), -extractStrength(oldItem), ModifierOperation.ADD));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryClick(InventoryClickEvent e){
        if(e.isCancelled()){
            return;
        }

        Player p = (Player) e.getWhoClicked();

        if(isMidasSword(e.getCurrentItem())){

            if(p.getInventory().getItemInMainHand().hasItemMeta()){

                if(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase(MIDAS_SWORD_DISPLAY_NAME)
                && e.getAction() != InventoryAction.DROP_ONE_SLOT && e.getAction() != InventoryAction.DROP_ALL_SLOT){

                    EcoSkillsAPI.addStatModifier(p, new StatModifier(UUID.randomUUID(), Stats.INSTANCE.getByID("strength"), -extractStrength(p.getInventory().getItemInMainHand()), ModifierOperation.ADD));
                }

            }

            if(e.isShiftClick()){

                Bukkit.getScheduler().runTaskLater(DarkAuction.getPlugin(), () -> {

                    if(!p.getInventory().getItemInMainHand().hasItemMeta()){
                        return;
                    }

                    if(!p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase(MIDAS_SWORD_DISPLAY_NAME)){
                        return;
                    }

                    EcoSkillsAPI.addStatModifier(p, new StatModifier(UUID.randomUUID(), Stats.INSTANCE.getByID("strength"), extractStrength(p.getInventory().getItemInMainHand()), ModifierOperation.ADD));

                }, 1L);

            }
        }

        if (isMidasSword(e.getCursor())){

            ItemStack cursorItem = e.getCursor();

            Bukkit.getScheduler().runTaskLater(DarkAuction.getPlugin(), () -> {

                if(!p.getInventory().getItemInMainHand().hasItemMeta()) {
                    return;
                }

                if(!p.getInventory().getItemInMainHand().getItemMeta().equals(cursorItem.getItemMeta())){
                    return;
                }

                EcoSkillsAPI.addStatModifier(p, new StatModifier(UUID.randomUUID(), Stats.INSTANCE.getByID("strength"), extractStrength(cursorItem), ModifierOperation.ADD));

            }, 1L);
        }

        if(e.getAction().equals(InventoryAction.HOTBAR_SWAP)){

            if(isMidasSword(e.getCurrentItem())){

                Bukkit.getScheduler().runTaskLater(DarkAuction.getPlugin(), () -> {

                    if(!isMidasSword(p.getInventory().getItemInMainHand())){
                        return;
                    }

                    EcoSkillsAPI.addStatModifier(p, new StatModifier(UUID.randomUUID(), Stats.INSTANCE.getByID("strength"), extractStrength(p.getInventory().getItemInMainHand()), ModifierOperation.ADD));

                }, 1L);

            }
            else {

                if(isMidasSword(p.getInventory().getItemInMainHand())){

                    ItemStack itemBefore = p.getInventory().getItemInMainHand();

                    Bukkit.getScheduler().runTaskLater(DarkAuction.getPlugin(), () -> {

                        ItemStack itemAfter = p.getInventory().getItemInMainHand();

                        if(itemBefore.equals(itemAfter)){
                            return;
                        }

                        EcoSkillsAPI.addStatModifier(p, new StatModifier(UUID.randomUUID(), Stats.INSTANCE.getByID("strength"), -extractStrength(itemBefore), ModifierOperation.ADD));

                    }, 1L);
                }
                else {

                    Bukkit.getScheduler().runTaskLater(DarkAuction.getPlugin(), () -> {

                        if(!isMidasSword(p.getInventory().getItemInMainHand())){
                            return;
                        }

                        EcoSkillsAPI.addStatModifier(p, new StatModifier(UUID.randomUUID(), Stats.INSTANCE.getByID("strength"), extractStrength(p.getInventory().getItemInMainHand()), ModifierOperation.ADD));

                    }, 1L);
                }
            }
        }

        if(e.getAction() == InventoryAction.DROP_ONE_SLOT || e.getAction() == InventoryAction.DROP_ALL_SLOT){

            if(isMidasSword(p.getInventory().getItemInMainHand())){
                return;
            }

            Bukkit.getScheduler().runTaskLater(DarkAuction.getPlugin(), () -> EcoSkillsAPI.addStatModifier(p, new StatModifier(UUID.randomUUID(), Stats.INSTANCE.getByID("strength"), extractStrength(droppedMidas), ModifierOperation.ADD)), 1L);
        }

        if(e.getAction() == InventoryAction.DROP_ONE_CURSOR || e.getAction() == InventoryAction.DROP_ALL_CURSOR){

            Bukkit.getScheduler().runTaskLater(DarkAuction.getPlugin(), () -> EcoSkillsAPI.addStatModifier(p, new StatModifier(UUID.randomUUID(), Stats.INSTANCE.getByID("strength"), extractStrength(droppedMidas), ModifierOperation.ADD)), 1L);
        }
    }

    private boolean isMidasSword(ItemStack item) {
        return item != null && item.hasItemMeta() && item.getItemMeta().getDisplayName().equalsIgnoreCase(MIDAS_SWORD_DISPLAY_NAME);
    }

    private double extractStrength(ItemStack item){

        for (String str : item.getItemMeta().getLore()) {

            Matcher matcher = STRENGTH_PATTERN.matcher(ChatColor.stripColor(str));

            if (matcher.find()) {
                String strengthString = matcher.group(1);
                return Double.parseDouble(strengthString);
            }
        }

        return 0.0;
    }
}
