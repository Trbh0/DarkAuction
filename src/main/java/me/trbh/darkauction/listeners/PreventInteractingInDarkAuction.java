package me.trbh.darkauction.listeners;

import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import static me.trbh.darkauction.utils.SomeCoolMethods.daWorld;

public class PreventInteractingInDarkAuction implements Listener {

    @EventHandler
    public void onItemDespawn(ItemDespawnEvent e){

        if(e.getEntity().getItemStack().equals(DarkAuctionStart.itemInside)){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){

        if(e.getBlock().getWorld() == daWorld){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onFramePlace(HangingPlaceEvent e){

        if(e.getEntity().getWorld() == daWorld){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onFrameUse(PlayerInteractEntityEvent e) {

        if(e.getRightClicked() instanceof ItemFrame && e.getRightClicked().getWorld() == daWorld) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerBreak(BlockBreakEvent e){

        if(e.getBlock().getWorld() == daWorld){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onFrameBreak(HangingBreakEvent e){

        if(e.getEntity().getWorld() == daWorld){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onFrameHitItem(EntityDamageByEntityEvent e) {

        if (e.getEntity() instanceof ItemFrame) {
            if (e.getDamager() instanceof Player) {
                if(e.getEntity().getWorld() == daWorld){
                    e.setCancelled(true);
                }
            }
            if (e.getDamager() instanceof Projectile) {
                if (((Projectile) e.getDamager()).getShooter() instanceof Player) {
                    if(e.getEntity().getWorld() == daWorld){
                        e.getDamager().remove();
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent e){

        if(e.getPlayer().getWorld() == daWorld){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent e){

        if(e.getEntity().getWorld() == daWorld){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEmptyBucket(PlayerBucketEmptyEvent e){

        if(e.getPlayer().getWorld() == daWorld){
            e.setCancelled(true);
        }
    }
}
