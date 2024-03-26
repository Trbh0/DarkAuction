package me.trbh.darkauction.npc;

import dev.sergiferry.playernpc.api.NPC;
import dev.sergiferry.playernpc.api.NPCLib;
import me.trbh.darkauction.DarkAuction;
import me.trbh.darkauction.listeners.DarkAuctionStart;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import static me.trbh.darkauction.utils.SomeCoolMethods.playSiriusSound;
import static me.trbh.darkauction.utils.SomeCoolMethods.siriusVoice;

public class SpawnSirius implements Listener {

    public static boolean isAuctioning = false;
    private static final Location siriusOverWorldLocation = new Location(Bukkit.getWorld(DarkAuction.getPlugin().getConfig().getString("sirius-world-name")),
            DarkAuction.getPlugin().getConfig().getDouble("sirius-location-x"),
            DarkAuction.getPlugin().getConfig().getDouble("sirius-location-y"),
            DarkAuction.getPlugin().getConfig().getDouble("sirius-location-z"),
            0, 0);
    private static Set<Player> signedInPlayers = new HashSet<>();
    private static NPC.Global npcSirius;
    private static boolean isSiriusVisible = false;

    public static void checkHour(){

        Bukkit.getScheduler().runTaskLater(DarkAuction.getPlugin(), () -> {

            NPCLib.getInstance().registerPlugin(DarkAuction.getPlugin());
            npcSirius = NPCLib.getInstance().generateGlobalNPC(DarkAuction.getPlugin(), "siriusMain", siriusOverWorldLocation);
            npcSirius.setText(ChatColor.GOLD + "Sirius", ChatColor.YELLOW + "" + ChatColor.BOLD + "CLICK");
            npcSirius.setGazeTrackingType(NPC.GazeTrackingType.NEAREST_PLAYER);
            npcSirius.setSkin("ewogICJ0aW1lc3RhbXAiIDogMTYxMjI5MDEyMjIxNSwKICAicHJvZmlsZUlkIiA6ICJkZGVkNTZlMWVmOGI0MGZlOGFkMTYyOTIwZjdhZWNkYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJEaXNjb3JkQXBwIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzY3YWUzZTI1MzlhNDQyNTE4MTMwMzcwZDNiMmQzYjE4MzdhNDkxZWUyM2Q1YWZmMjBmMDE1YWUyODRhMjhjOTAiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==",
                    "wxO8NN0XVV66aolUjL9qhturPPfas7lvoxTGCZ9jkV+ojvREtBS2fYuFTNaF5kB+SZdnZLphOrLBAHlyMIsGS7o+MhBmtlQblJBafdNHN5CsA5LpsbaHwj+WZjqSnzCV2qVVj2gY7w7HLlLV1PEPg97TcRS/SjIZND08257kJOkrCRxJBLGfQ8ZhQWl0p4JF5S4bn+kP754L1fyUx0kYWGstIZGmMcyQkY0aOI251bRapVa3rs+rc0QtOkRyy38OputzTaCREQ3m3fpCZ5lxG0IvR4NScXc3V1EJKwvsEpXUYsy1ElrrbYle+2sZvHdPS4JGYN8RLcVBiOHcncZ2xSSlep2O9rui/jtiTZAIvUGQX6Fn4RSRbeI9qinOr8qhGOnKiPsBXx18BNgCRNcj1rCmwAqmHGbP0eMZ/KYMoVlHZWlXBd1DMN/7duEoy5MV59+GK2sOO2Qkz1C+TeTKRV2gLe2tkTJMukUYS58m4BBZPavdY8RZAnzn0EP6pwWTrb1ms9ZA+CovfFTdGLh4fGuHjrhQBvdldQf00gbbDGcWnOoiiEfsZ/azW1VmB08XpIvRlLDTTgjmI1avwrd0BMe+oRNt9wOLFnUbPZ+weIKPTT/iyrhthG3dialYr+GgTJGJ/+QHKJY9gWkDIcDrl3Iei3Va1tuI2VvEtf9wcy8=");

            Bukkit.getScheduler().runTaskLater(DarkAuction.getPlugin(), () -> {

                isSiriusVisible = false;
                npcSirius.hide();

            }, 20L);

            Bukkit.getScheduler().scheduleSyncRepeatingTask(DarkAuction.getPlugin(), () -> {

                LocalTime currentTime = LocalTime.now();
                int currentHour = currentTime.getHour();
                int currentMinute = currentTime.getMinute();

                if (currentHour % 2 == 0 && currentMinute == 0) {

                    isSiriusVisible = true;
                    npcSirius.show();
                    Bukkit.getScheduler().runTaskLater(DarkAuction.getPlugin(), () -> npcSirius.getWorld().spawnParticle(Particle.SQUID_INK, npcSirius.getLocation(), 50), 15L);

                    Bukkit.getScheduler().runTaskLater(DarkAuction.getPlugin(), () -> {

                        isSiriusVisible = false;
                        npcSirius.hide();

                        if(signedInPlayers.size() >= DarkAuction.getPlugin().getConfig().getDouble("min-signed-in-players")){

                            for (Player signedPlayer : signedInPlayers){
                                signedPlayer.teleport(Bukkit.getWorld("world_dark_auction").getSpawnLocation());
                            }

                            signedInPlayers.clear();
                            isAuctioning = true;
                            DarkAuctionStart.runAuction();

                        }
                        else {
                            for (Player signedPlayer : signedInPlayers){

                                signedPlayer.sendMessage(siriusVoice("Not enough players signed up for the auction so I &ccancelled&r it. Hopefully next time more players will come!"));
                                playSiriusSound(signedPlayer);

                            }
                            signedInPlayers.clear();
                        }

                    }, 15 * 20L);

                }

            }, 0, 60 * 20L);

        }, 20L);

    }

    @EventHandler
    public void onNPCInteract(NPC.Events.Interact e){
        Player p = e.getPlayer();
        NPC npc = e.getNPC();

        if(npc.getSimpleID().equalsIgnoreCase("global_siriusmain") && isSiriusVisible){

            if(!signedInPlayers.contains(p)){
                p.sendMessage(siriusVoice("Are you here for the Auction? Only the richest players can enter."));
                playSiriusSound(p);
                signedInPlayers.add(p);
                p.sendMessage(siriusVoice("You signed up for the Auction!"));
            }
            else {
                p.sendMessage(siriusVoice("You are signed up for the Auction!"));
                playSiriusSound(p);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){

        if(!isSiriusVisible){
            Bukkit.getScheduler().runTaskLater(DarkAuction.getPlugin(), () -> npcSirius.hide(), 20L);
        }
    }
}
