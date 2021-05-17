package be.alexandre01.inazuma_eleven.roles.raimon;

import be.alexandre01.inazuma.uhc.InazumaUHC;
import be.alexandre01.inazuma.uhc.managers.damage.DamageManager;
import be.alexandre01.inazuma.uhc.presets.IPreset;
import be.alexandre01.inazuma.uhc.roles.Role;
import be.alexandre01.inazuma.uhc.utils.CustomComponentBuilder;
import be.alexandre01.inazuma.uhc.utils.PatchedEntity;
import be.alexandre01.inazuma.uhc.utils.PlayerUtils;
import be.alexandre01.inazuma_eleven.categories.Raimon;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Shawn extends Role implements Listener {
    public boolean fusionCommand = false;
    public boolean fusion = false;
    public BukkitTask bukkitTask;
    public ArrayList<Location> aidenLoc;
    public int ms = 0;
    public Shawn(IPreset preset) {
        super("Shawn Frost",preset);

        addDescription("§8- §7Votre objectif est de gagner avec §6§lRaimon");
        addDescription("§8- §7Vous possédez l’effet §6§lRésistance 1§7.");
        addDescription(" ");
        CustomComponentBuilder c = new CustomComponentBuilder("");
        c.append("§8- §7Vous possédez également le ");

        BaseComponent blizzardButton = new TextComponent("§3Blizzard Eternel §7*§8Curseur§7*");

        BaseComponent blizzardDesc = new TextComponent();
        blizzardDesc.addExtra("§e- §9Utilisation par §eEpisode\n");
        blizzardDesc.addExtra("§e- §9Donne au joueur §7ciblé, §8Slowness 2§9 pendant §a7 secondes");
        blizzardButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,blizzardDesc.getExtra().toArray(new BaseComponent[0])));
        c.append(blizzardButton);
        addDescription(c);
        addDescription(" ");
        addDescription("§8- §7Les attaques de §cTorch§7, §bGazelle§7 et §6Axel§7 ne vous atteignent pas.");

        setRoleToSpoil(Axel.class);
        setRoleToSpoil(Aiden.class);
        setRoleCategory(Raimon.class);
        onLoad(new load() {
            @Override
            public void a(Player player) {
                inazumaUHC.dm.addEffectPourcentage(player, DamageManager.EffectType.RESISTANCE,1,110);
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0,false,false), true);
            }

        });
        addCommand("fusion", new command() {
            @Override
            public void a(String[] strings, Player player) {
                if(!fusionCommand){
                    player.sendMessage("La commande est désactivé pour le moment. PD ");
                    return;
                }
                if(InazumaUHC.get.rm.getRole(Aiden.class).getPlayers().isEmpty()){
                    player.sendMessage("Aiden est mort :'(");
                    return;
                }

                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 0,false,false), true);
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 0,false,false), true);
                player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                PatchedEntity.setMaxHealthInSilent(player,player.getMaxHealth()-6);
                bukkitTask = new BukkitRunnable() {
                    @Override
                    public void run() {
                        System.out.println(inazumaUHC.rm.getRole(Aiden.class).getPlayers().stream()
                                .map(Entity::getLocation).collect(Collectors.toList()));
                        for(Player p : inazumaUHC.rm.getRole(Aiden.class).getPlayers()){
                            aidenLoc.add(p.getLocation());
                        }


                        for(Player p : getPlayers()){
                            for(Location location : aidenLoc){
                                if(p.getLocation().distance(location)/2 >= 15)
                                    continue;

                                ms += 1000;
                            }
                        }

                        if(ms >= 1000*20){
                            bukkitTask.cancel();
                            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0,false,false), true);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0,false,false), true);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0,false,false), true);
                            inazumaUHC.dm.addEffectPourcentage(player, DamageManager.EffectType.INCREASE_DAMAGE,1,110);
                            player.removePotionEffect(PotionEffectType.SLOW);
                            player.removePotionEffect(PotionEffectType.WEAKNESS);

                            player.getWorld().spigot().strikeLightningEffect(player.getLocation(),true);
                            int j = 0;
                            for (int i = 3; i < 6; i++) {
                                //x
                                Location location = player.getLocation().clone();
                                location.add(i,0,0);
                                if(j == 0){
                                    player.getWorld().spigot().strikeLightning(location,true);
                                    j+= 2;
                                    continue;
                                }
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        player.getWorld().spigot().strikeLightning(location,true);
                                    }
                                }.runTaskLaterAsynchronously(InazumaUHC.get,j);
                                j+= 2;
                            }
                            j=0;
                            for (int i = -3; i > -6; i--) {
                                //-x
                                Location location = player.getLocation().clone();
                                location.add(i,0,0);
                                if(j == 0){
                                    player.getWorld().spigot().strikeLightning(location,true);
                                    j+= 2;
                                    continue;
                                }
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        player.getWorld().spigot().strikeLightning(location,true);
                                    }
                                }.runTaskLaterAsynchronously(InazumaUHC.get,j);
                                j+= 2;
                            }
                            j=0;
                            for (int i = 3; i < 6; i++) {
                                //z
                                Location location = player.getLocation().clone();
                                location.add(0,0,i);
                                if(j == 0){
                                    player.getWorld().spigot().strikeLightning(location,true);
                                    j+= 2;
                                    continue;
                                }
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        player.getWorld().spigot().strikeLightning(location,true);
                                    }
                                }.runTaskLaterAsynchronously(InazumaUHC.get,j);
                                j+= 2;
                            }
                            j=0;
                            for (int i = -3; i > -6; i--) {
                               //-z
                                Location location = player.getLocation().clone();
                                location.add(0,0,i);
                                if(j == 0){
                                    player.getWorld().spigot().strikeLightning(location,true);
                                    j+= 2;
                                    continue;
                                }
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        player.getWorld().spigot().strikeLightning(location,true);
                                    }
                                }.runTaskLaterAsynchronously(InazumaUHC.get,j);
                                j+= 2;
                            }
                            for(Player target : PlayerUtils.getNearbyPlayersFromPlayer(player,50,50,50)){
                                target.sendMessage("Fusion de Shawn réussite");
                            }

                        }
                    }
                }.runTaskTimerAsynchronously(InazumaUHC.get,20,20);
            }
        });
    }



    public static void timer(){
        new BukkitRunnable() {
            @Override
            public void run() {
                Shawn shawn = (Shawn) InazumaUHC.get.rm.getRole(Shawn.class);
                shawn.getPlayers().forEach(player -> {
                    player.sendMessage("Fusion dispo");
                });
                shawn.fusionCommand = true;
            }
        }.runTaskLaterAsynchronously(InazumaUHC.get,20*30);
    }
}
