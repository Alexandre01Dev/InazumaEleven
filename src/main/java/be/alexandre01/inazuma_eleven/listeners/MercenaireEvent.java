package be.alexandre01.inazuma_eleven.listeners;

import be.alexandre01.inazuma.uhc.InazumaUHC;
import be.alexandre01.inazuma.uhc.custom_events.episode.EpisodeChangeEvent;
import be.alexandre01.inazuma.uhc.presets.Preset;
import be.alexandre01.inazuma.uhc.roles.Role;
import be.alexandre01.inazuma.uhc.utils.Episode;
import be.alexandre01.inazuma.uhc.utils.TitleUtils;
import be.alexandre01.inazuma_eleven.categories.Alius;
import be.alexandre01.inazuma_eleven.categories.Raimon;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;

public class MercenaireEvent implements Listener {

    private final ArrayList<Role> usedRole = new ArrayList<>();

    @EventHandler
    public void onEpisode(EpisodeChangeEvent e){
        if(Episode.getEpisode() == 2){

            new BukkitRunnable(){
                @Override
                public void run(){

                    ArrayList<Role> alius = new ArrayList<>(InazumaUHC.get.rm.getRoleCategory(Alius.class).getRoles());

                    alius.remove(this);
                    for(Role used : usedRole){
                        alius.remove(used);
                    }
                    if(alius.isEmpty()){
                        return;
                    }

                    Collections.shuffle(alius);

                    System.out.println(alius.size());
                    for(Role role : alius){
                        boolean save = false;
                        for(Player player : role.getPlayers()){
                            if(player.isOnline()){
                                save = true;
                                break;
                            }
                        }
                        if(!save){
                            alius.remove(role);
                        }
                    }

                    if(alius.get(0) != null){
                            for(Player target : alius.get(0).getPlayers()){
                                target.sendMessage(Preset.instance.p.prefixName()+" §7Vous êtes le mercenaire.");
                            }
                        usedRole.add(alius.get(0));
                    }

                }
            }.runTaskLater(InazumaUHC.get, 20*60);

        }
    }

}
