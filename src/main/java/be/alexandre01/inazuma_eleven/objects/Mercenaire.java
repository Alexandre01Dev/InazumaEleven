package be.alexandre01.inazuma_eleven.objects;

import be.alexandre01.inazuma.uhc.InazumaUHC;
import be.alexandre01.inazuma.uhc.custom_events.episode.EpisodeChangeEvent;
import be.alexandre01.inazuma.uhc.presets.Preset;
import be.alexandre01.inazuma.uhc.roles.Role;
import be.alexandre01.inazuma.uhc.timers.game.PVPTimer;
import be.alexandre01.inazuma.uhc.timers.utils.DateBuilderTimer;
import be.alexandre01.inazuma.uhc.timers.utils.MSToSec;
import be.alexandre01.inazuma.uhc.utils.CustomComponentBuilder;
import be.alexandre01.inazuma.uhc.utils.Episode;
import be.alexandre01.inazuma.uhc.utils.TitleUtils;
import be.alexandre01.inazuma_eleven.categories.Alius;
import be.alexandre01.inazuma_eleven.categories.Raimon;
import be.alexandre01.inazuma_eleven.categories.Solo;
import be.alexandre01.inazuma_eleven.roles.alius.Torch;
import be.alexandre01.inazuma_eleven.roles.raimon.Aiden;
import be.alexandre01.inazuma_eleven.roles.raimon.Axel;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;

public class Mercenaire{



    public ArrayList<Player> list;

    public void onPvP(){

        Axel axel = (Axel) InazumaUHC.get.rm.getRole(Axel.class);;
        if (axel == null){
            return;
        }



            new BukkitRunnable(){
                @Override
                public void run(){

                    boolean hasFound = false;

                    while(!hasFound){

                        ArrayList<Role> alius = new ArrayList<>(InazumaUHC.get.rm.getRoleCategory(Alius.class).getRoles());

                        Collections.shuffle(alius);

                        if (alius.isEmpty()){
                            return;
                        }

                        Role role = alius.get(0);

                        if (role.getPlayers().isEmpty()){
                            alius.remove(role);
                            continue;
                        }



                        Player target = role.getPlayers().get(0);

                        InazumaUHC.get.spectatorManager.getPlayers().contains(target);

                        if (InazumaUHC.get.spectatorManager.getPlayers().contains(target)){
                            alius.remove(role);
                            continue;
                        }

                        hasFound = true;


                        target.sendMessage(Preset.instance.p.prefixName()+" §7Vous êtes le §c§lMercenaire§7 de §d§lJulia§7.");
                        target.sendMessage(Preset.instance.p.prefixName()+" §7Votre objectif est de faire en sorte qu'§c§lAlex§7 ne soit plus avec §6§lRaimon§7.");
                        target.sendMessage(" ");
                        BaseComponent mercenaireButton = new TextComponent("§8- §7Vous avez une commande : " + "§5/kidnapping");

                        BaseComponent mercenaireDesc = new TextComponent();
                        mercenaireDesc.addExtra("§e- §7[Cooldown de §a10 minutes§7]\n");
                        mercenaireDesc.addExtra("§e- §9Permet de forcer le départ d'§cAxel Blaze§9 de §6Raimon§9 afin qu'il win solo\n");
                        mercenaireDesc.addExtra("§c⚠§9 Vous devez être à moins de §515 blocks§9 d'§cAxel Blaze§9 pour effectuer la commande. §c⚠\n");
                        mercenaireDesc.addExtra("§c⚠ §cAxel§9 aura une liste de pseudo qui baissera quand il tue des joueurs. §c⚠\n");
                        mercenaireDesc.addExtra("§c⚠§9 S'il participe à votre mort, il reviendra chez §6Raimon§9. §c⚠");
                        mercenaireButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,mercenaireDesc.getExtra().toArray(new BaseComponent[0])));
                        //target.spigot().sendMessage(mercenaireButton);
                        target.sendMessage(" ");
                        target.sendMessage(mercenaireButton);

                        role.addDescription(mercenaireButton);

                        role.addCommand("kidnapping", new Role.command() {
                            public boolean kidnacommand = false;
                            @Override
                            public void a(String[] args, Player player) {
                                if (kidnacommand) {
                                    player.sendMessage("Vous ne pouvez pas faire la commande.");
                                    return;
                                }
                                        Location mercenaireloc = player.getLocation();
                                        Location axelloc = null;

                                        for(Player axel : InazumaUHC.get.rm.getRole(Axel.class).getPlayers()){
                                            axelloc = axel.getLocation();
                                        }

                                        if (axelloc == null){

                                            player.sendMessage(Preset.instance.p.prefixName()+"co axel = null, mort?");
                                            return;

                                        }


                                        if (mercenaireloc.distance(axelloc) /2 <= 15){

                                            kidnacommand = true;
                                            player.sendMessage(Preset.instance.p.prefixName()+"Axel quiterra Raimon dans 2 minutes.");

                                            Axel r_axel = (Axel) InazumaUHC.get.rm.getRole(Axel.class);





                                                new BukkitRunnable(){
                                                    @Override
                                                    public void run(){

                                                        r_axel.setRoleCategory(Solo.class);


                                                        ArrayList<Player> players = new ArrayList<>(InazumaUHC.get.getRemainingPlayers());

                                                        list = new ArrayList<>();

                                                        Collections.shuffle(players);

                                                        for (Player player : players) {

                                                            if (r_axel.getPlayers().contains(player) || role.getPlayers().contains(player))
                                                                continue;

                                                            list.add(player);

                                                            if (list.size() == 3){
                                                                break;
                                                            }

                                                        }

                                                        list.add(player);

                                                        Collections.shuffle(list);

                                                        r_axel.addCommand("mercenaire", new Role.command() {

                                                            @Override
                                                            public void a(String[] strings, Player player) {

                                                                player.sendMessage("test");
                                                                sendList(player);

                                                            }
                                                        });

                                                        for(Player axel : r_axel.getPlayers()){

                                                        axel.sendMessage(Preset.instance.p.prefixName()+"Votre soeur a était pris en otage, pour quel n'est pas de problème, vous quittez Raimon.");
                                                        axel.sendMessage(Preset.instance.p.prefixName()+"Si vous espérez re venir dans Raimon, vous devez retrouver et participer à la mort du Mercenaire qui se cache dans la liste suivante :");
                                                        sendList(axel);

                                                        }
                                                    }

                                            }.runTaskLaterAsynchronously(InazumaUHC.get, 20*60*2);



                                        }


                            }
                        });

                    }






                }
            }.runTaskLaterAsynchronously(InazumaUHC.get, 20*10);



        }

        private void sendList(Player player){

        StringBuilder sb = new StringBuilder();

            for (int i = 0; i < list.size(); i++) {

                Player target = list.get(i);

                if(!InazumaUHC.get.getRemainingPlayers().contains(target))
                    continue;
                sb.append(target.getName());
                if (i < list.size()-1){
                    sb.append(", ");
                }
            }
            player.sendMessage("VoICI lA LISte Des joUEurS : ");
            player.sendMessage(sb.toString());
        }

}
