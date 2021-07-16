package be.alexandre01.inazuma_eleven.objects;

import be.alexandre01.inazuma.uhc.InazumaUHC;
import be.alexandre01.inazuma.uhc.custom_events.episode.EpisodeChangeEvent;
import be.alexandre01.inazuma.uhc.custom_events.player.PlayerInstantDeathEvent;
import be.alexandre01.inazuma.uhc.presets.Preset;
import be.alexandre01.inazuma.uhc.roles.Role;
import be.alexandre01.inazuma.uhc.roles.RoleItem;
import be.alexandre01.inazuma.uhc.timers.game.PVPTimer;
import be.alexandre01.inazuma.uhc.timers.utils.DateBuilderTimer;
import be.alexandre01.inazuma.uhc.timers.utils.MSToSec;
import be.alexandre01.inazuma.uhc.utils.*;
import be.alexandre01.inazuma_eleven.categories.Alius;
import be.alexandre01.inazuma_eleven.categories.Raimon;
import be.alexandre01.inazuma_eleven.categories.Solo;
import be.alexandre01.inazuma_eleven.roles.alius.Gazelle;
import be.alexandre01.inazuma_eleven.roles.alius.Torch;
import be.alexandre01.inazuma_eleven.roles.raimon.*;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_8_R3.Tuple;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

public class Mercenaire{



    public ArrayList<Player> list;
    boolean hasAxel = true;
    public Player mercenaire;
    public int ms = 0;
    public int totalms = 300000;
    boolean axelAlive;
    BukkitTask particlesTask;
    BukkitTask fireTask;

    public void onPvP(){

        new BukkitRunnable() {
            @Override
            public void run() {
                if (InazumaUHC.get.rm.getRole(Axel.class) == null)
                    return;

                new BukkitRunnable(){
                    @Override
                    public void run(){

                        boolean hasFound = false;
                        ArrayList<Role> alius = new ArrayList<>(InazumaUHC.get.rm.getRoleCategory(Alius.class).getRoles());
                        while(!hasFound){

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

                            if (role.getEliminatedPlayers().contains(target)){
                                alius.remove(role);
                                continue;
                            }

                            hasFound = true;

                            mercenaire = role.getPlayers().get(0);


                            target.sendMessage(Preset.instance.p.prefixName()+" §7Vous êtes le §c§lMercenaire§7 de §d§lJulia§7.");
                            target.sendMessage("§8- §7Vous disposez de §c§l2 §4❤§7 permanents supplémentaires des qu'Axel quitte Raimon et quand il va quiiter.");
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

                            role.setRoleToSpoil(Axel.class);

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

                                        axelAlive = true;
                                        kidnacommand = true;
                                        target.setMaxHealth(target.getMaxHealth()+2);
                                        player.sendMessage(Preset.instance.p.prefixName()+"Axel quiterra Raimon dans 5 minutes et vous gagnez 1 coeur.");

                                        new BukkitRunnable() {
                                            Format m = new SimpleDateFormat("mm");
                                            Format s = new SimpleDateFormat("ss");
                                            @Override
                                            public void run() {

                                                if(axelAlive)
                                                {
                                                    ms += 1000;

                                                    if(ms >= totalms){
                                                        TitleUtils.sendActionBar(target,"Axel a quitté Raimon");
                                                        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 5,5);
                                                        cancel();
                                                    }

                                                    int date = totalms - ms;

                                                    String minute = this.m.format(date);
                                                    String second = this.s.format(date);
                                                    StringBuilder sb = new StringBuilder();

                                                    sb.append("§7Départ d'§c§lAxel §f§l: §a§l ");
                                                    sb.append(minute + "m");
                                                    sb.append(second+"s");

                                                    TitleUtils.sendActionBar(target,sb.toString());
                                                }



                                            }
                                        }.runTaskTimerAsynchronously(InazumaUHC.get, 20*1, 20*1);



                                        Axel r_axel = (Axel) InazumaUHC.get.rm.getRole(Axel.class);


                                        new BukkitRunnable(){
                                            @Override
                                            public void run(){

                                                target.setMaxHealth(target.getMaxHealth()+2);
                                                target.sendMessage(Preset.instance.p.prefixName()+"Axel a quitté Raimon et vous gagnez 1 coeur.");
                                                r_axel.setRoleCategory(Solo.class);
                                                r_axel.isSolo = true;

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

                                                for(Player axel : r_axel.getPlayers()){

                                                    axel.sendMessage(Preset.instance.p.prefixName()+" Votre soeur a était pris en otage, pour quel n'est pas de problème, vous quittez Raimon.");
                                                    axel.sendMessage(Preset.instance.p.prefixName()+" Si vous espérez re venir dans Raimon, vous devez retrouver et participer à la mort du Mercenaire qui se cache dans la liste suivante :");
                                                    sendList(axel);

                                                    RoleItem TempeteDeFeu = new RoleItem();
                                                    TempeteDeFeu.setItemstack(new ItemBuilder(Material.BLAZE_POWDER).setName("Tempête de Feu").toItemStack());
                                                    TempeteDeFeu.deployVerificationsOnRightClick(TempeteDeFeu.generateVerification(new Tuple<>(RoleItem.VerificationType.EPISODES,1)));
                                                    TempeteDeFeu.setRightClick(player -> {
                                                        player.sendMessage(Preset.instance.p.prefixName()+" Vous venez d'activer votre Tornade de Feu.");

                                                            for(Player target : PlayerUtils.getNearbyPlayersFromPlayer(axel,15,15,15)) {
                                                                World world = axel.getWorld();
                                                                //world.createExplosion(axel.getLocation(),1f,false);
                                                                Bukkit.broadcastMessage(player.getLocation().getBlockX() + 2 + "  " + player.getLocation().getBlockY() + "   " + player.getLocation().getBlockZ());
                                                                Location location = target.getLocation();
                                                                location.setY(player.getLocation().getY());
                                                                Vector v = target.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().multiply(4);
                                                                v.add(new Vector(0,1,0));
                                                                if(v.getY() > 2)
                                                                    v.setY(2);
                                                                if(v.getX() > 2)
                                                                    v.setX(2);
                                                                if(v.getY() > 2)
                                                                    v.setY(2);
                                                                target.setVelocity(v);
                                                                target.setFireTicks(20 * 5);


                                                                if (InazumaUHC.get.rm.getRole(player).getClass().equals(Gazelle.class) && InazumaUHC.get.rm.getRole(player).getClass().equals(Torch.class) && InazumaUHC.get.rm.getRole(player).getClass().equals(Shawn.class) && InazumaUHC.get.rm.getRole(player).getClass().equals(Hurley.class)) {
                                                                    return;
                                                                }
                                                            }

                                                            particlesTask = new BukkitRunnable() {

                                                                double var = 0;
                                                                Location loc, first, second;
                                                                @Override
                                                                public void run() {

                                                                    Location loc = player.getLocation();
                        /*int radius = 2;

                        for(double y = 0; y <= 50; y+=0.05) {
                            double x = radius * Math.cos(y);
                            double z = radius * Math.sin(y);
                            PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.BARRIER,true, (float) (loc.getX() + x), (float) (loc.getY() + y), (float) (loc.getZ() + z), 0, 0, 0, 0, 1);
                            for(Player online : Bukkit.getOnlinePlayers()) {
                                ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
                            }
                        }*/

                                                                    var += Math.PI / 8;

                                                                    double x = Math.cos(var) / 2;
                                                                    double y = Math.sin(var) / 2 + 0.5;
                                                                    double z = Math.sin(var) / 2;

                                                                    double x2 = Math.cos(var + Math.PI) / 2;
                                                                    double y2 = Math.sin(var) / 2 + 0.5;
                                                                    double z2 = Math.sin(var + Math.PI) / 2;

                                                                    loc = axel.getLocation();
                                                                    first = loc.clone().add(x, y, z);
                                                                    second = loc.clone().add(x2, y2, z2);

                                                                    PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.FLAME,true, (float) (first.getX() + x), (float) (first.getY() + y), (float) (first.getZ() + z), 0, 0, 0, 0, 1);
                                                                    PacketPlayOutWorldParticles reversPacket = new PacketPlayOutWorldParticles(EnumParticle.FLAME,true, (float) (second.getX() + x2), (float) (second.getY() + y2), (float) (second.getZ() + z2), 0, 0, 0, 0, 1);
                                                                    for(Player online : Bukkit.getOnlinePlayers()) {
                                                                        ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
                                                                        ((CraftPlayer) online).getHandle().playerConnection.sendPacket(reversPacket);
                                                                    }

                                                                    //axel.getWorld().spigot().playEffect(first, Effect.FLAME, 0,0,0f,0f,0f,0f,1,1);
                                                                    //axel.getWorld().spigot().playEffect(second, Effect.FLAME, 0,0,0f,0f,0f,0f,1,1);
                                                                }
                                                            }.runTaskTimerAsynchronously(InazumaUHC.get, 1, 1);


                                                            fireTask = new BukkitRunnable() {
                                                                @Override
                                                                public void run() {
                                                                    for (Player target : PlayerUtils.getNearbyPlayersFromPlayer(axel, 5,5,5))
                                                                    {
                                                                        target.setFireTicks(20*5);
                                                                    }
                                                                }
                                                            }.runTaskTimerAsynchronously(InazumaUHC.get, 20, 20);

                                                        new BukkitRunnable() {
                                                            @Override
                                                            public void run() {
                                                                particlesTask.cancel();
                                                                fireTask.cancel();
                                                            }
                                                        }.runTaskLaterAsynchronously(InazumaUHC.get, 20*60*5);

                                                    });
                                                    r_axel.addRoleItem(TempeteDeFeu);
                                                    r_axel.giveItem(axel, TempeteDeFeu);



                                                }

                                                r_axel.addCommand("mercenaire", new Role.command() {

                                                    @Override
                                                    public void a(String[] strings, Player player) {

                                                        sendList(player);

                                                    }
                                                });

                                                r_axel.loadCommands();

                                            }
                                        }.runTaskLater(InazumaUHC.get, 20*60*5);
                                    }
                                }
                            });
                            role.loadCommands();
                        }
                    }
                }.runTaskLater(InazumaUHC.get, 20*60);

            }

        }.runTaskLater(InazumaUHC.get, 1);
    }

    private void sendList(Player player){
        StringBuilder sb = new StringBuilder();

        if(list.size() == 1)
        {
            player.sendMessage(Preset.instance.p.prefixName() + " Vous avez désormais toutes les informations");
            player.sendMessage(Preset.instance.p.prefixName() + " le Mercenaire est : " + list.get(0).getName());
            return;
        }

        for (int i = 0; i < list.size(); i++) {

            Player target = list.get(i);

            if(!InazumaUHC.get.getRemainingPlayers().contains(target))
                continue;
            sb.append(target.getName());
            if (i < list.size()-1){
                sb.append(", ");
            }
        }
        player.sendMessage(Preset.instance.p.prefixName()+" Voici la liste des joueurs dans laquelle se trouve le Mercenaire : ");
        player.sendMessage(sb.toString());
    }

    @EventHandler
    void onDeath(PlayerInstantDeathEvent event)
    {
        if(InazumaUHC.get.rm.getRole(event.getPlayer()) instanceof Axel)
            axelAlive = false;
    }

}
