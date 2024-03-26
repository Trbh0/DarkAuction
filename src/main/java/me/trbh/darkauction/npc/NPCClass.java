package me.trbh.darkauction.npc;

import dev.sergiferry.playernpc.api.NPC;
import dev.sergiferry.playernpc.api.NPCLib;
import me.trbh.darkauction.DarkAuction;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static me.trbh.darkauction.utils.SomeCoolMethods.playSiriusSound;
import static me.trbh.darkauction.utils.SomeCoolMethods.siriusVoice;

public class NPCClass implements Listener {

    public static void spawnNPCs(){

        final Location siriusDALocation = new Location(Bukkit.getWorld("world_dark_auction"), 17.5, 5, 13.5, 0, 0);
        final Location guard1Location = new Location(Bukkit.getWorld("world_dark_auction"), 13.5, 5, 8.5, 0, 0);
        final Location guard2Location = new Location(Bukkit.getWorld("world_dark_auction"), 21.5, 5, 8.5, 0, 0);

        Bukkit.getScheduler().runTaskLater(DarkAuction.getPlugin(), () -> {

            //sirius
            NPC.Global npcSirius = NPCLib.getInstance().generateGlobalNPC(DarkAuction.getPlugin(), "sirius", siriusDALocation);
            npcSirius.setText(ChatColor.GOLD + "Sirius");
            npcSirius.setGazeTrackingType(NPC.GazeTrackingType.NEAREST_PLAYER);
            npcSirius.setSkin("ewogICJ0aW1lc3RhbXAiIDogMTYxMjI5MDEyMjIxNSwKICAicHJvZmlsZUlkIiA6ICJkZGVkNTZlMWVmOGI0MGZlOGFkMTYyOTIwZjdhZWNkYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJEaXNjb3JkQXBwIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzY3YWUzZTI1MzlhNDQyNTE4MTMwMzcwZDNiMmQzYjE4MzdhNDkxZWUyM2Q1YWZmMjBmMDE1YWUyODRhMjhjOTAiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==",
                    "wxO8NN0XVV66aolUjL9qhturPPfas7lvoxTGCZ9jkV+ojvREtBS2fYuFTNaF5kB+SZdnZLphOrLBAHlyMIsGS7o+MhBmtlQblJBafdNHN5CsA5LpsbaHwj+WZjqSnzCV2qVVj2gY7w7HLlLV1PEPg97TcRS/SjIZND08257kJOkrCRxJBLGfQ8ZhQWl0p4JF5S4bn+kP754L1fyUx0kYWGstIZGmMcyQkY0aOI251bRapVa3rs+rc0QtOkRyy38OputzTaCREQ3m3fpCZ5lxG0IvR4NScXc3V1EJKwvsEpXUYsy1ElrrbYle+2sZvHdPS4JGYN8RLcVBiOHcncZ2xSSlep2O9rui/jtiTZAIvUGQX6Fn4RSRbeI9qinOr8qhGOnKiPsBXx18BNgCRNcj1rCmwAqmHGbP0eMZ/KYMoVlHZWlXBd1DMN/7duEoy5MV59+GK2sOO2Qkz1C+TeTKRV2gLe2tkTJMukUYS58m4BBZPavdY8RZAnzn0EP6pwWTrb1ms9ZA+CovfFTdGLh4fGuHjrhQBvdldQf00gbbDGcWnOoiiEfsZ/azW1VmB08XpIvRlLDTTgjmI1avwrd0BMe+oRNt9wOLFnUbPZ+weIKPTT/iyrhthG3dialYr+GgTJGJ/+QHKJY9gWkDIcDrl3Iei3Va1tuI2VvEtf9wcy8=");

            //guard1
            NPC.Global npc1 = NPCLib.getInstance().generateGlobalNPC(DarkAuction.getPlugin(), "guard1", guard1Location);
            npc1.setText(ChatColor.RED + "Bob");
            npc1.setGazeTrackingType(NPC.GazeTrackingType.NEAREST_PLAYER);
            npc1.setSkin("ewogICJ0aW1lc3RhbXAiIDogMTcwNTM2MDA1MTM2NywKICAicHJvZmlsZUlkIiA6ICJjYjYxY2U5ODc4ZWI0NDljODA5MzliNWYxNTkwMzE1MiIsCiAgInByb2ZpbGVOYW1lIiA6ICJWb2lkZWRUcmFzaDUxODUiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjAxMGNhYzcwOWNiMzhiODcyZTg1NGQwNjBkYzRmYTYwMzZiNDMxNTZlMzMxMjRlNDhkOWQ3OTY0ODZjZGMxNyIKICAgIH0KICB9Cn0=",
                    "lNyXzKOAxgIFxap446FoHZbHQjxtaypRt+YGLPQGo49pLFFjRnL3pEBVpZ35e9Dlur1ySfqQ0+0FNjB0OTkc+SMkcznsPt2jOuSRiv71YyKICSypR4Ip/BlZ3HvxAw7HS2n7axuTXu2fl1KrBbk7UUWbR6Adjl/vfWLkwx8B9ysuo1oGKoydovkPRvlPfoGY/uHueo3haGr7E799TbasnLZQltfUzO3EEQA/dhwvO1fKDokEaaYSR6hPy88x5MHABZYuV+N59CzJCS8R0d+Iy7UVpUlAr90dtcf4dH2l/TsRpv1A4ZpYjy+rHo1LGz9e1/FCfFZoGYnvOXk5S+FFRDJIpt844+CY5LEekD7Hx5okyWobaSIwal2xwqVGHYgJtJ89akTUjdzGQuersEIY+BZjXUxIO2K90jSQgA7+iQ30r7eXF0U+9T/Gx7zdq2x+XH2ucuNZFYxh56plct0Y2UM3psBSwTHplm/TBEOXxFNROX2BNR4qo5SdQlOaQb0GnPaKWtVutmgrdL7ggAjqatKaPby2SXFS/TwMnRUI1oBFXdEqBiQGUUTEonbr9263XS01TXu/s+CRR9IKmj6vCC2piu7fP6I2z0yJhlODe6TTy6PNN1k9D5nZw2wJaZyqxSfmC7yCseTGrlTOYyiYrZwEaOGL6ZzRRpqxx+ptopk=");

            //guard2
            NPC.Global npc2 = NPCLib.getInstance().generateGlobalNPC(DarkAuction.getPlugin(), "guard2", guard2Location);
            npc2.setText(ChatColor.RED + "Bob");
            npc2.setGazeTrackingType(NPC.GazeTrackingType.NEAREST_PLAYER);
            npc2.setSkin("ewogICJ0aW1lc3RhbXAiIDogMTcwNTM2MDA1MTM2NywKICAicHJvZmlsZUlkIiA6ICJjYjYxY2U5ODc4ZWI0NDljODA5MzliNWYxNTkwMzE1MiIsCiAgInByb2ZpbGVOYW1lIiA6ICJWb2lkZWRUcmFzaDUxODUiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjAxMGNhYzcwOWNiMzhiODcyZTg1NGQwNjBkYzRmYTYwMzZiNDMxNTZlMzMxMjRlNDhkOWQ3OTY0ODZjZGMxNyIKICAgIH0KICB9Cn0=",
                    "lNyXzKOAxgIFxap446FoHZbHQjxtaypRt+YGLPQGo49pLFFjRnL3pEBVpZ35e9Dlur1ySfqQ0+0FNjB0OTkc+SMkcznsPt2jOuSRiv71YyKICSypR4Ip/BlZ3HvxAw7HS2n7axuTXu2fl1KrBbk7UUWbR6Adjl/vfWLkwx8B9ysuo1oGKoydovkPRvlPfoGY/uHueo3haGr7E799TbasnLZQltfUzO3EEQA/dhwvO1fKDokEaaYSR6hPy88x5MHABZYuV+N59CzJCS8R0d+Iy7UVpUlAr90dtcf4dH2l/TsRpv1A4ZpYjy+rHo1LGz9e1/FCfFZoGYnvOXk5S+FFRDJIpt844+CY5LEekD7Hx5okyWobaSIwal2xwqVGHYgJtJ89akTUjdzGQuersEIY+BZjXUxIO2K90jSQgA7+iQ30r7eXF0U+9T/Gx7zdq2x+XH2ucuNZFYxh56plct0Y2UM3psBSwTHplm/TBEOXxFNROX2BNR4qo5SdQlOaQb0GnPaKWtVutmgrdL7ggAjqatKaPby2SXFS/TwMnRUI1oBFXdEqBiQGUUTEonbr9263XS01TXu/s+CRR9IKmj6vCC2piu7fP6I2z0yJhlODe6TTy6PNN1k9D5nZw2wJaZyqxSfmC7yCseTGrlTOYyiYrZwEaOGL6ZzRRpqxx+ptopk=");

        }, 20L);

    }

    @EventHandler
    public void onNPCInteract(NPC.Events.Interact e){
        Player p = e.getPlayer();
        NPC npc = e.getNPC();

        if(npc.getSimpleID().equalsIgnoreCase("global_sirius")){
            p.sendMessage(siriusVoice("Shhh, keep it down!"));
            playSiriusSound(p);
        }
        else if(npc.getSimpleID().equalsIgnoreCase("global_guard1") || (npc.getSimpleID().equalsIgnoreCase("global_guard2"))){
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e[NPC] &cBob&r: Oi, get back in line!"));
            playSiriusSound(p);
        }
    }

}
