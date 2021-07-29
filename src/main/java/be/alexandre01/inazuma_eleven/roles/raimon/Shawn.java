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
import be.alexandre01.inazuma_eleven.listeners.CustomGlasses;
import be.alexandre01.inazuma_eleven.listeners.EpisodeEvent;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.Tuple;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
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
    public int totalms = 1000*60*10;
    public boolean transformation = false;
    public boolean isUsing = false;

    public Shawn(IPreset preset) {
        super("Shawn Frost",preset);
        setRoleToSpoil(Aiden.class);
        setRoleCategory(Raimon.class);
        addListener(this);
        this.aidenLoc = new ArrayList<>();
        /*addDescription("§8- §7Votre objectif est de gagner avec §6§lRaimon");
        addDescription("§8- §7Vous possédez l’effet §6§lRésistance 1§7.");
        addDescription(" ");
        addDescription("§8- §7Les attaques de §cTorch§7, §bGazelle§7 et §6Axel§7 ne vous atteignent pas.");*/


        onLoad(new load() {
            @Override
            public void a(Player player) {
                inazumaUHC.dm.addEffectPourcentage(player, DamageManager.EffectType.RESISTANCE,1,110);
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0,false,false), true);
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0,false,false), true);

                if (InazumaUHC.get.rm.getRole(Aiden.class).getPlayers().size() == 0){
                    aidenDeath = true;
                    return;
                }
            }

        });


        RoleItem roleItem = new RoleItem();
        final String texture = "ewogICJ0aW1lc3RhbXAiIDogMTYyNTQ5MTUwMzg3NSwKICAicHJvZmlsZUlkIiA6ICJjNjc3MGJjZWMzZjE0ODA3ODc4MTU0NWRhMGFmMDI1NCIsCiAgInByb2ZpbGVOYW1lIiA6ICJDVUNGTDE2IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzEyYThmMWZmYjY1N2Y2YzgzZDk3MmQ4MzhkNjMyMzczN2FhOTA2OTBkOGQxOWVhNjEzMzRhNDY3ZTNlZThlNDciCiAgICB9CiAgfQp9";
        ItemStack it = new CustomHead(texture,"§c§lTransformation").toItemStack();
        roleItem.setItemstack(it);
        addRoleItem(roleItem);

        roleItem.deployVerificationsOnRightClick(  roleItem.generateMultipleVerification(new ArrayList<>(Arrays.asList(new RoleItem.VerificationGeneration() {
            @Override
            public boolean verification(Player player) {
                if(!transformation)
                    player.sendMessage("§c§lRAGE§8» §cVous devez être en rage pour pouvoir utiliser ce pouvoir.");
                return transformation;
            }
        }, new RoleItem.VerificationGeneration() {
            @Override
            public boolean verification(Player player) {
                if(isUsing)
                    player.sendMessage("§c§lRAGE§8» §cVous utilisez déjà ce pouvoir.");
                return !isUsing;
            }
        })),new Tuple<>(RoleItem.VerificationType.COOLDOWN,60*10)));

        roleItem.setRightClick(player -> {
            player.playSound(player.getLocation(), Sound.PISTON_RETRACT,1,1);
            float walkspeed = player.getWalkSpeed();
            isUsing = true;
            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*60*2, 0, false, false), true);
            inazumaUHC.dm.addEffectPourcentage(player, DamageManager.EffectType.INCREASE_DAMAGE,1,115);
            player.setWalkSpeed(walkspeed+0.025F);
            new BukkitRunnable() {
                @Override
                public void run() {
                    inazumaUHC.dm.addEffectPourcentage(player, DamageManager.EffectType.INCREASE_DAMAGE,1,110);
                }
            }.runTaskLaterAsynchronously(inazumaUHC,20*30);

            new BukkitRunnable() {
                @Override
                public void run() {
                    player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0,false,false), true);
                    player.setWalkSpeed(walkspeed);
                    transformation = false;
                    isUsing = false;
                    player.sendMessage("§c§lRAGE§8» §c§lAiden§7 ne vous possède plus.");

                    coups = 0;
                }
            }.runTaskLaterAsynchronously(inazumaUHC,20*60*2);
        });




        addCommand("fusion", new command() {
            @Override
            public void a(String[] strings, Player player) {
                if(!fusionCommand){
                    player.sendMessage("§6§lFusion§8»§7 Vous pouvez faire la §5§lcommande§7 uniquement au bout de §a§l1h30§7 de jeu.");
                    return;
                }
                if(InazumaUHC.get.rm.getRole(Aiden.class).getPlayers().size() == 0)
                    return;

                if(InazumaUHC.get.rm.getRole(Aiden.class).getPlayers().isEmpty()){
                    player.sendMessage("§6§lFusion§8»§7 §c§lAiden§4 est mort, il est donc impossible de faire la fusion.");
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
                                player.sendMessage("§6§lFusion§8»§7 §c§lAiden§4 est mort, la §6§lfusion§7 s'est donc stropé.");
                            });
                            fusionRegen = true;
                            cancel();

                            for(Player shawn : inazumaUHC.rm.getRole(Shawn.class).getPlayers()){
                                shawn.sendMessage("§6§lFusion§8»§7 Vous venez de commencer la §6§lfusion§7 avec §c§lAiden§7.");
                            }
                            for(Player aiden : inazumaUHC.rm.getRole(Aiden.class).getPlayers()){
                                aiden.sendMessage("§6§lFusion§8»§7 §bShawn§7 a commencé la §6§lfusion§7 avec vous.");
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



                        if(ms >= 1000*60*10){
                            bukkitTask.cancel();
                            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0,false,false), true);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0,false,false), true);
                            inazumaUHC.dm.addEffectPourcentage(player, DamageManager.EffectType.INCREASE_DAMAGE,1,110);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0,false,false), true);
                            player.setMaxHealth(player.getMaxHealth()+6);
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
                                player.sendMessage("§6§lFusion§8»§7 Vous avez §6§lfusionnés§7 avec §c§lAiden§7, de ce fait vous gagnez §4§lForce§7, §b§lSpeed§7 ainsi que §6§lRésistance§7.");
                                target.sendMessage("§6§lFusion§8»§7 §b§lShawn§7 vient de fusionner avec §c§lAiden§7 !");
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
        ArrayList<Player> p;
        if(inazumaUHC.rm.getRole(Aiden.class) != null)
            p = inazumaUHC.rm.getRole(Aiden.class).getPlayers();
        else
            return;

        if (fusion){
            if(p.contains(player)){
                if(p.size() == 1){
                    getPlayers().forEach(shawn -> {
                        shawn.removePotionEffect(PotionEffectType.SPEED);
                        shawn.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                    });
                }
            }
        }

        if(role.getClass() == Aiden.class){

            for(Player shawn : inazumaUHC.rm.getRole(Shawn.class).getPlayers()){

                new BukkitRunnable(){
                    @Override
                    public void run(){
                        shawn.sendMessage("§c§lRAGE§8»§7 §c§lAiden§7 vient de mourir, en conséquence vous perdez la §6§lfusion§7 si elle était faites mais vous débloquez la §c§lTransformation§7 en §c§lAiden§7.");
                    }

                }.runTaskLater(InazumaUHC.get, 1);


            }
            aidenDeath = true;

                new BukkitRunnable(){
                    @Override
                    public void run(){
                        if(!isUsing){
                            refreshActionBar();
                        }else{
                            for(Player shawn : getPlayers()){
                                TitleUtils.sendActionBar(shawn,"§cVo§ku§cs ête§ks§c en RA§kG§cE !");
                            }
                        }

                    }
                }.runTaskTimerAsynchronously(InazumaUHC.get, 20*1, 20*1);

        }



    }


    public void refreshActionBar(){
        StringBuilder sb = new StringBuilder();
        int v = coups/4;
        if(coups >= 100){
            v = 100/4;
        }
        int i = new Random().nextInt(10);

        if (coups >= 100){

            sb.append("§c§lTr§ka§cn§ks§cfo§kr§cmati§ko§cn :");
            sb.append(" ");

            sb.append("§7[");
            sb.append("§c");
            for (int j = 0; j < v; j++) {
                if(i % 3 == 0){
                    sb.append("§k");
                }
                sb.append("|");
                sb.append("§c");
                i++;
            }
            sb.append("§7]");

            for(Player shawn : getPlayers()){
                TitleUtils.sendActionBar(shawn,sb.toString());
            }
            return;
        }
        sb.append("§c§lTransformation :");
        sb.append(" ");
        sb.append("§7[");
        sb.append("§c");
        for (int j = 0; j < v; j++) {
            sb.append("|");
        }
        sb.append("§8");
        for (int j = 0; j < 25-v; j++) {
            sb.append("|");
        }
        sb.append("§7]");

        for(Player shawn : getPlayers()){
            TitleUtils.sendActionBar(shawn,sb.toString());
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
                    coups +=3;
                    break;

                case IRON_SWORD:
                    coups +=2;
                    break;

            }
            if(!isUsing)
                refreshActionBar();
            if(!transformation){
                if(coups >= 100){
                    Shawn shawn = (Shawn) InazumaUHC.get.rm.getRole(Shawn.class);
                    if(shawn.getPlayers().isEmpty())
                        return;
                    shawn.getPlayers().forEach(player -> {
                        player.sendMessage("§c§lRAGE§8»§7 §c§lAiden§7 est prêt à déclencher sa §c§lrage§7.");
                    });
                    transformation = true;
                }
            }


        }
    }

    public static void timer(){
        new BukkitRunnable() {
            @Override
            public void run() {
                Shawn shawn = (Shawn) InazumaUHC.get.rm.getRole(Shawn.class);
                Aiden aiden = (Aiden) InazumaUHC.get.rm.getRole(Aiden.class);
                if(shawn.getPlayers().isEmpty())
                    return;
                shawn.getPlayers().forEach(player -> {
                    player.sendMessage("§6§lFusion§8»§7 §c§lAiden§7 est prêt à §6§lfusionner§7 avec vous, faites §5/fusion§7 pour §6§lfusionner§7 avec.");
                });
                aiden.getPlayers().forEach(player -> {
                    player.sendMessage("§6§lFusion§8»§7 §b§lShawn§7 est prêt à §6§lfusionner§7 avec vous.");
                });
                shawn.fusionCommand = true;
            }
        }.runTaskLaterAsynchronously(InazumaUHC.get,20*90*60);
    }


}
