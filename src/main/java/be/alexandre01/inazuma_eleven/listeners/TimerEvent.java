package be.alexandre01.inazuma_eleven.listeners;


import be.alexandre01.inazuma.uhc.InazumaUHC;
import be.alexandre01.inazuma.uhc.custom_events.timers.TimerCancelEvent;
import be.alexandre01.inazuma.uhc.custom_events.timers.TimerCreateEvent;
import be.alexandre01.inazuma.uhc.presets.Preset;
import be.alexandre01.inazuma.uhc.roles.Role;
import be.alexandre01.inazuma.uhc.state.GameState;
import be.alexandre01.inazuma.uhc.state.State;
import be.alexandre01.inazuma.uhc.timers.Timer;
import be.alexandre01.inazuma.uhc.timers.game.StartingTimer;
import be.alexandre01.inazuma_eleven.objects.Capitaine;
import be.alexandre01.inazuma_eleven.objects.Mercenaire;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.font.TextLayout;

public class TimerEvent implements Listener {
    @EventHandler
    public void onTimerCreate(TimerCreateEvent event){

    }

    @EventHandler
    public void onTimerCancel(TimerCancelEvent event){
        System.out.println("CancelTimer");
       if(event.getTimerName().equals("waitingTimer")){
           GameState.get().setTo(State.STARTING);
           return;
       }

       if(event.getTimerName().equals("stabilizationTimer")){
               Timer timer = InazumaUHC.get.tm.getTimer(StartingTimer.class);
               timer.runTaskTimer(InazumaUHC.get,0,20);
       }
       if(event.getTimerName().equals("startingTimer")){
           GameState.get().setTo(State.PLAYING);
           return;
       }
        if(event.getTimerName().equals("invicibilityTimer")){
            Preset.instance.pData.isInvisible = false;
            return;
        }
        if(event.getTimerName().equals("pvpTimer")){
            Mercenaire mercenaire = new Mercenaire();
            mercenaire.onPvP();
            InazumaUHC.get.rm.distributeRoles( InazumaUHC.get.getRemainingPlayers());
            for(Role role : Role.getRoles()){
                for(Player player : role.getPlayers()){
                    if(player.isOnline()){
                        role.spoilRole(player);
                        role.giveItem(player);
                    }
                }
            }
            Role.isDistributed = true;
        }
        if(event.getTimerName().equals("episodeTimer")){
            System.out.println("episodeTimer !");
            event.getTimer().runTaskTimerAsynchronously(InazumaUHC.get,0,20*10);
        }


       /* if(event.getTimerName().equals("delayedTimeChangeTimer")){
            DelayedTimeChangeTimer t = (DelayedTimeChangeTimer) InazumaUHC.get.tm.getTimer(DelayedTimeChangeTimer.class);
            if(t.getState().equals(DelayedTimeChangeTimer.State.DAY)){
                t.setState(DelayedTimeChangeTimer.State.NIGHT);
            }else {
                t.setState(DelayedTimeChangeTimer.State.DAY);
            }
            t.runTaskTimerAsynchronously(InazumaUHC.get,0,1);
            return;
        }*/
    }
}
