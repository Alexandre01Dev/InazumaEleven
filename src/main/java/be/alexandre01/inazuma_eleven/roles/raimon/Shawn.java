package be.alexandre01.inazuma_eleven.roles.raimon;

import be.alexandre01.inazuma.uhc.InazumaUHC;
import be.alexandre01.inazuma.uhc.custom_events.episode.EpisodeChangeEvent;
import be.alexandre01.inazuma.uhc.custom_events.player.PlayerInstantDeathEvent;
import be.alexandre01.inazuma.uhc.managers.damage.DamageManager;
import be.alexandre01.inazuma.uhc.presets.IPreset;
import be.alexandre01.inazuma.uhc.presets.Preset;
import be.alexandre01.inazuma.uhc.roles.Role;
import be.alexandre01.inazuma.uhc.roles.RoleItem;
import be.alexandre01.inazuma.uhc.utils.*;
import be.alexandre01.inazuma_eleven.categories.Raimon;
import be.alexandre01.inazuma_eleven.listeners.EpisodeEvent;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.Tuple;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Shawn extends Role implements Listener {
    public boolean fusionCommand = false;
    public boolean fusionRegen = false;
    public boolean fusion = false;
    public boolean aidenDeath = false;
    public int coups =0;
    public int i = 3;
    public BukkitTask bukkitTask;
    public ArrayList<Location> aidenLoc;
    public int ms = 0;
    public int totalms = 1000*20;
    public Shawn(IPreset preset) {
        super("Shawn Frost",preset);
        addListener(this);
        this.aidenLoc = new ArrayList<>();
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


        RoleItem roleItem = new RoleItem();
        ItemBuilder it = new ItemBuilder(Material.REDSTONE).setName("Transformation");
        roleItem.setItemstack(it.toItemStack());
        addRoleItem(roleItem);

        addCommand("fusion", new command() {
            @Override
            public void a(String[] strings, Player player) {
                if(!fusionCommand){
                    player.sendMessage("La commande est désactivé pour le moment. ");
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

                    Format m = new SimpleDateFormat("mm");
                    Format s = new SimpleDateFormat("ss");
                    @Override
                    public void run() {
                        ArrayList<Player> aidens = inazumaUHC.rm.getRole(Aiden.class).getPlayers();

                        aidenLoc.clear();
                        if(aidens.isEmpty()){
                            getPlayers().forEach(shawn -> {
                                shawn.sendMessage("Aiden est mort donc fusion cancel");
                            });
                            fusionRegen = true;
                            cancel();

                            for(Player player : inazumaUHC.rm.getRole(Shawn.class).getPlayers()){
                                player.sendMessage("§7Vous venez de commencer la fusion." );
                            }
                            for(Player player : inazumaUHC.rm.getRole(Aiden.class).getPlayers()){
                                player.sendMessage("§7Shawn a commencé la fusion." );
                            }
                        }
                        for(Player p : aidens){
                            aidenLoc.add(p.getLocation());
                        }

                        boolean paused = true;
                        for(Player p : getPlayers()){
                            for(Location location : aidenLoc){
                                if(p.getLocation().distance(location)/2 >= 15)
                                    continue;

                                paused = false;
                                ms += 1000;
                            }
                        }
                        int date = totalms - ms;

                        String minute = this.m.format(date);
                        String second = this.s.format(date);
                        StringBuilder sb = new StringBuilder();

                        sb.append("§6§lFusion §f§l: §3§l ");
                        sb.append(minute + "m ");
                        sb.append(second+"s");
                        if(paused)
                            sb.append(" §4(§cTROP LOIN§4)");




                        getPlayers().forEach(shawn -> {
                            TitleUtils.sendActionBar(shawn,sb.toString());
                        });
                        aidens.forEach(aiden -> {
                            TitleUtils.sendActionBar(aiden,sb.toString());
                        });



                        if(ms >= 1000*20){
                            bukkitTask.cancel();
                            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0,false,false), true);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0,false,false), true);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0,false,false), true);
                            inazumaUHC.dm.addEffectPourcentage(player, DamageManager.EffectType.INCREASE_DAMAGE,1,110);
                            player.removePotionEffect(PotionEffectType.SLOW);
                            player.removePotionEffect(PotionEffectType.WEAKNESS);
                            fusionRegen = true;
                            fusion = true;
                            player.getWorld().spigot().strikeLightningEffect(player.getLocation(),true);

                            for(Player shawn : getPlayers()){
                                inazumaUHC.invincibilityDamager.addPlayer(shawn,3000, EntityDamageEvent.DamageCause.LIGHTNING);
                            }
                            for(Player aiden : InazumaUHC.get.rm.getRole(Aiden.class).getPlayers()){
                                inazumaUHC.invincibilityDamager.addPlayer(aiden,3000,EntityDamageEvent.DamageCause.LIGHTNING);
                            }
                            int j = 0;
                            for (int i = 4; i < 7; i++) {
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
                            for (int i = -4; i > -7; i--) {
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
                                        player.getWorld().spigot().strikeLightning(location,false);
                                    }
                                }.runTaskLaterAsynchronously(InazumaUHC.get,j);
                                j+= 2;
                            }
                            j=0;
                            for (int i = 4; i < 7; i++) {
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
                            for (int i = -4; i > -7; i--) {
                               //-z
                                Location location = player.getLocation().clone();
                                location.add(0,0,i);
                                if(j == 0){
                                    player.getWorld().spigot().strikeLightning(location,false);
                                    j+= 2;
                                    continue;
                                }
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        player.getWorld().spigot().strikeLightning(location,false);
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

    @EventHandler
    public void onEpisode(EpisodeChangeEvent event){
        if(fusionRegen){
            if(i != 0){
                for(Player player : getPlayers()){
                    player.setMaxHealth(player.getMaxHealth()+2);
                }
                i--;
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerInstantDeathEvent event){
        Player player = event.getPlayer();
        Role role = inazumaUHC.rm.getRole(player);
        ArrayList<Player> p = inazumaUHC.rm.getRole(Aiden.class).getPlayers();
        if (fusion){
            if(p.contains(player)){
                if(p.size() == 1){
                    getPlayers().forEach(shawn -> {
                        shawn.removePotionEffect(PotionEffectType.SPEED);
                        shawn.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                    });
                }
            }
        }

        if(role.getClass() == Aiden.class){

            for(Player shawn : inazumaUHC.rm.getRole(Shawn.class).getPlayers()){

                new BukkitRunnable(){
                    @Override
                    public void run(){

                        shawn.sendMessage("Aiden vient de mourir, en conséquence vous perdez la fusion si elle était faites mais vous debloquer la Transformation en Aiden.");

                    }

                }.runTaskLater(InazumaUHC.get, 1);


            }
            aidenDeath = true;


                new BukkitRunnable(){
                    @Override
                    public void run(){
                        StringBuilder sb = new StringBuilder();
                        sb.append("§c§lTransformation:");
                        sb.append(" ");
                        int v = coups/5;
                        sb.append("§7[");
                        sb.append("§c");
                        for (int j = 0; j < v; j++) {
                            sb.append("|");
                        }
                        sb.append("§8");
                        for (int j = 0; j < 20-v; j++) {
                            sb.append("|");
                        }
                        sb.append("§7]");

                        for(Player shawn : getPlayers()){
                            TitleUtils.sendActionBar(shawn,sb.toString());
                        }

                        if (coups >= 100){
                            cancel();
                        }
                    }
                }.runTaskTimerAsynchronously(InazumaUHC.get, 20*2, 20*2);

        }



    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event){
        if(!aidenDeath)
            return;
        Entity entity = event.getEntity();
        Entity e_damager = event.getDamager();

        if ((entity instanceof Player) && (e_damager instanceof Player)){
            Player damager = (Player) e_damager;
            if(!getPlayers().contains(damager))
                return;
            switch (damager.getItemInHand().getType()){
                case DIAMOND_SWORD:
                    coups +=5;
                    break;

                case IRON_SWORD:
                    coups +=4;
                    break;

                case GOLD_SWORD:
                    coups +=3;
                    break;
            }
        }
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
