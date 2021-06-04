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

    private final ArrayList<Role> usedRole = new ArrayList<>();

    public void onPvP(){

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
                        target.sendMessage(" ");
                        BaseComponent mercenaireButton = new TextComponent("§6- §7Vous avez une commande : " + "§5/ina kidnapping");

                        BaseComponent mercenaireDesc = new TextComponent();
                        mercenaireDesc.addExtra("§e- §7[Cooldown de §a10 minutes§7]\n");
                        mercenaireDesc.addExtra("§e- §9Permet de forcer le départ d'§cAxel Blaze§9 de §6Raimon§9 afin qu'il win solo\n");
                        mercenaireDesc.addExtra("§c⚠§9 Vous devez être à moins de §55 blocks§9 d'§cAxel Blaze§9 pour effectuer la commande. §c⚠\n");
                        mercenaireDesc.addExtra("§c⚠ §cAxel§9 aura une liste de pseudo qui baissera quand il tue des joueurs. §c⚠\n");
                        mercenaireDesc.addExtra("§c⚠§9 S'il participe à votre mort, il reviendra chez §6Raimon§9. §c⚠");
                        mercenaireButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,mercenaireDesc.getExtra().toArray(new BaseComponent[0])));
                        //target.spigot().sendMessage(mercenaireButton);
                        target.sendMessage(" ");

                        role.addDescription(mercenaireButton);

                        role.addCommand("ina", new Role.command() {
                            @Override
                            public void a(String[] args, Player player) {


                                if(args.length == 0){
                                    player.sendMessage(Preset.instance.p.prefixName()+" Veuillez mettre §5/ina kidnapping");
                                    return;
                                }

                                    if(args[0].equalsIgnoreCase("kidnapping")){

                                        Location mercenaireloc = player.getLocation();
                                        Location axelloc = null;

                                        for(Player axel : InazumaUHC.get.rm.getRole(Axel.class).getPlayers()){

                                            axelloc = player.getLocation();

                                        }

                                        if (axelloc == null){

                                            player.sendMessage("co axel = null, mort?");
                                            return;

                                        }

                                        if (mercenaireloc.distance(axelloc) /2 <= 15){

                                            player.sendMessage("Axel quiterra Raimon dans 2 minutes.");

                                            for(Player axel : InazumaUHC.get.rm.getRole(Axel.class).getPlayers()){

                                                new BukkitRunnable(){
                                                    @Override
                                                    public void run(){

                                                        axel.sendMessage("Votre soeur a était pris en otage, pour quel n'est pas de problème, vous quittez Raimon.");

                                                    }

                                            }.runTaskLater(InazumaUHC.get, 20*60*2);

                                                axel.sendMessage("Si vous espérez re venir dans Raimon, vous devez retrouver et participer à la mort du Mercenaire qui se cache dans la liste suivante :");



                                            }


                                        }
                                    }


                            }
                        });

                    }






                }
            }.runTaskLater(InazumaUHC.get, 20*10);

        }

}
