package me.trbh.darkauction.listeners;

import me.trbh.darkauction.DarkAuction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import static me.trbh.darkauction.utils.SomeCoolMethods.daWorld;

public class ChangeWorldListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWorldChanged(PlayerChangedWorldEvent e) {

        Player p = e.getPlayer();

        if (e.getFrom() == daWorld) {

            Bukkit.getScheduler().runTaskLater(DarkAuction.getPlugin(), () -> {

                for (Player players : p.getWorld().getPlayers()) {
                    p.showPlayer(DarkAuction.getPlugin(), players);
                    players.showPlayer(DarkAuction.getPlugin(), p);
                }

            }, 20L);

        }

        Bukkit.getScheduler().runTaskLater(DarkAuction.getPlugin(), () -> {

            if (p.getWorld() == daWorld) {
                for (Player players : daWorld.getPlayers()) {
                    p.hidePlayer(DarkAuction.getPlugin(), players);
                    players.hidePlayer(DarkAuction.getPlugin(), p);
                }
            }

        }, 25 * 20L);

    }
}
