package be.alexandre01.inazuma_eleven.listeners;

import be.alexandre01.inazuma.uhc.InazumaUHC;
import be.alexandre01.inazuma.uhc.custom_events.player.PlayerInstantDeathEvent;
import be.alexandre01.inazuma_eleven.categories.Alius;
import be.alexandre01.inazuma_eleven.categories.Raimon;
import be.alexandre01.inazuma_eleven.categories.Solo;
import be.alexandre01.inazuma_eleven.roles.raimon.Axel;
import be.alexandre01.inazuma_eleven.roles.solo.Byron;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class WinEvent implements Listener {

    @EventHandler
    public void onKill(PlayerInstantDeathEvent event){

        if(InazumaUHC.get.rm.getRoleCategory(Alius.class).getRoles().isEmpty() && InazumaUHC.get.rm.getRoleCategory(Solo.class).getRoles().isEmpty()){
            Bukkit.broadcastMessage("win Raimon");
            return;
        }
        if(InazumaUHC.get.rm.getRoleCategory(Raimon.class).getRoles().isEmpty() && InazumaUHC.get.rm.getRoleCategory(Solo.class).getRoles().isEmpty()){
            Bukkit.broadcastMessage("win Alius");
            return;
        }
        if(InazumaUHC.get.rm.getRoleCategory(Raimon.class).getRoles().isEmpty() && InazumaUHC.get.rm.getRoleCategory(Alius.class).getRoles().isEmpty()){
            if(InazumaUHC.get.rm.getRole(Axel.class).getPlayers().size() == 1 && InazumaUHC.get.rm.getRole(Axel.class).getRoleCategory().equals(Solo.class) && InazumaUHC.get.rm.getRole(Byron.class).getPlayers().isEmpty()){
                Bukkit.broadcastMessage("win Axel");
                return;
            }
            if(InazumaUHC.get.rm.getRole(Byron.class).getPlayers().size() == 1 && InazumaUHC.get.rm.getRole(Byron.class).getRoleCategory().equals(Solo.class) && InazumaUHC.get.rm.getRole(Axel.class).getPlayers().isEmpty()){
                Bukkit.broadcastMessage("win Byron");
                return;
            }
        }
    }
}
