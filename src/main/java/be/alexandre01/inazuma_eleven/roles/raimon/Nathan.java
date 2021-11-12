package be.alexandre01.inazuma_eleven.roles.raimon;

import be.alexandre01.inazuma.uhc.InazumaUHC;
import be.alexandre01.inazuma.uhc.presets.IPreset;
import be.alexandre01.inazuma.uhc.presets.Preset;
import be.alexandre01.inazuma.uhc.roles.Role;
import be.alexandre01.inazuma.uhc.roles.RoleItem;
import be.alexandre01.inazuma.uhc.utils.CustomComponentBuilder;
import be.alexandre01.inazuma.uhc.utils.ItemBuilder;
import be.alexandre01.inazuma.uhc.utils.LocationUtils;
import be.alexandre01.inazuma.uhc.utils.TitleUtils;
import be.alexandre01.inazuma_eleven.categories.Raimon;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.Tuple;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class Nathan extends Role implements Listener {

    int endurance = 0;
    boolean isOverkill = false;

    public Nathan(IPreset preset) {
        super("Nathan Swift",preset);
        setRoleCategory(Raimon.class);
        addDescription("https://blog.inazumauhc.fr/inazuma-eleven-uhc/roles/raimon/nathan-swift");
        addListener(this);
        /*addDescription("§8- §7Votre objectif est de gagner avec §6§lRaimon");
        addDescription("§8- §7Vous possédez l’effet §b§lSpeed 1§7.");
        addDescription(" ");
        CustomComponentBuilder c = new CustomComponentBuilder("");
        c.append("§8- §7Vous possédez également le ");

        BaseComponent speedButton = new TextComponent("§b§lSwitch §lSpeed§7 §7*§8Curseur§7*");

        BaseComponent speedDesc = new TextComponent();
        speedDesc.addExtra("§e- §9Utilisation §6illimité §7[Cooldown de §a10 minutes§7]\n");
        speedDesc.addExtra("§e- §9Donne §b§lSpeed 2§7 pendant §a2 minutes 30");
        speedButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,speedDesc.getExtra().toArray(new BaseComponent[0])));
        c.append(speedButton);
        addDescription(c);
        addDescription(" ");
        CustomComponentBuilder d = new CustomComponentBuilder("");
        d.append("§8- §7Vous possédez également le ");

        BaseComponent dashButton = new TextComponent("§b§lDribble Rafale§7 §7*§8Curseur§7*");

        BaseComponent dashDesc = new TextComponent();
        dashDesc.addExtra("§e- §9Utilisation §6illimité §7[Cooldown de §a10 minutes§7]\n");
        dashDesc.addExtra("§e- §9Donne un dash de §510 blocks");
        dashButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,dashDesc.getExtra().toArray(new BaseComponent[0])));
        d.append(dashButton);
        addDescription(d);
        addDescription(" ");*/

        onLoad(new load() {
            @Override
            public void a(Player player) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0,false,false), true);


                /* new BukkitRunnable(){
                    @Override
                    public void run(){
                        int endurance = 0;
                        endurance = endurance +1;

                    }
                }.runTaskTimerAsynchronously(InazumaUHC.getGet(), 20*30, 20*30);

                new BukkitRunnable(){
                    @Override
                    public void run(){
                            TitleUtils.sendActionBar(player,"§6§lEndurance §f§l: §3§l " + endurance + "§7/§3100");
                    }
                }.runTaskTimerAsynchronously(InazumaUHC.getGet(), 20*2, 20*2);*/

            }
        });


        RoleItem roleItem = new RoleItem();
        ItemBuilder itemBuilder = new ItemBuilder(Material.SUGAR).setName("§b§lSwitch §lSpeed");
        roleItem.setItemstack(itemBuilder.toItemStack());

        roleItem.deployVerificationsOnRightClick(roleItem.generateVerification(new Tuple<>(RoleItem.VerificationType.COOLDOWN,60*10)));
        //if (endurance >= 50)

        roleItem.setRightClick(player -> {
            player.sendMessage(Preset.instance.p.prefixName()+" Vous venez d'utiliser la §b§lSwitch §lSpeed§7.");
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 150*20, 1,false,false), true);
            addEffectAfter(player, 150 * 20, new action() {
                @Override
                public void a() {
                    player.sendMessage(Preset.instance.p.prefixName() + " Vous êtes essoufflé...");
                }
            }, new PotionEffect(PotionEffectType.SLOW_DIGGING, 30*20, 0,false,false), new PotionEffect(PotionEffectType.SLOW, 15*20, 1,false,false));
            addEffectAfter(player, 180 * 20, new action() {
                @Override
                public void a() {
                    player.sendMessage(Preset.instance.p.prefixName() + " Vous avez récupéré votre speed...");
                }
            }, new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
        });
        addRoleItem(roleItem);

        //if (endurance >= 25)
        RoleItem dribble_rafale = new RoleItem();
        ItemBuilder dr = new ItemBuilder(Material.FEATHER).setName("§b§lDribble Rafale");
        dribble_rafale.setItemstack(dr.toItemStack());
        addRoleItem(dribble_rafale);

        dribble_rafale.deployVerificationsOnRightClick(roleItem.generateVerification(new Tuple<>(RoleItem.VerificationType.COOLDOWN,60*10)));
        dribble_rafale.setRightClick(player -> {
            //OLD DASH
            /*Location location = player.getLocation().clone();
            location.setPitch(location.getPitch()/8f);

            player.setVelocity( location.getDirection().normalize().multiply(2.5d));
            InazumaUHC.get.noFallDamager.addPlayer(player,1000*4);*/

            isOverkill = true;
            new BukkitRunnable() {
                int second = 0;
                @Override
                public void run() {
                    second ++;

                    if(second == 15){
                        TitleUtils.sendActionBar(player,"§cTu n'es plus invincible");
                        isOverkill = false;
                        cancel();
                    }else {
                        TitleUtils.sendActionBar(player,"§eTu es invincible pendant "+ second+" seconde(s)");
                    }
                }
            }.runTaskTimer(InazumaUHC.get,0,20);
        });
        //if (endurance >= 75)

    }



    private void addEffectAfter(Player player,long l,action a, PotionEffect... potionEffects){
        Bukkit.getScheduler().runTaskLaterAsynchronously(inazumaUHC, new Runnable() {
            @Override
            public void run() {
                for (PotionEffect p: potionEffects){
                    player.addPotionEffect(p, true);
                }
                a.a();
            }
        },l);
    }

    @EventHandler
    public void onDamageReceived(EntityDamageByEntityEvent event){
        if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            if(getPlayers().contains(player)){
                if(isOverkill){
                    if(event.getDamager() instanceof Player){
                        Player damager = (Player) event.getDamager();
                        event.setCancelled(true);
                        int x = new Random().nextInt(10-5) + 5;
                        int z = new Random().nextInt(10-5) + 5;

                        if(new Random().nextBoolean()){
                            x *= -1;
                        }
                        if(new Random().nextBoolean()){
                            z *= -1;
                        }

                        Location location = damager.getLocation();
                        location.add(x,0,z);
                        boolean find = false;
                        int aY = location.getBlockY();
                        for (int i = -7; i < 7; i++) {
                            location.setY(aY+i);
                            if(location.getBlock().getType() == Material.AIR){
                                find = true;
                                break;
                            }
                        }

                        if(!find){
                            LocationUtils.getTop(location);
                        }

                        player.teleport(location);

                        location.getWorld().playSound(location, Sound.ENDERMAN_TELEPORT,1,1);
                    }
                }
            }

        }
    }

    public interface action{
        public void a();
    }
}