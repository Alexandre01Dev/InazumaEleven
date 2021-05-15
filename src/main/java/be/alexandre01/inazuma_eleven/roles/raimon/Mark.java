package be.alexandre01.inazuma_eleven.roles.raimon;

import be.alexandre01.inazuma.uhc.InazumaUHC;
import be.alexandre01.inazuma.uhc.custom_events.player.PlayerInstantDeathEvent;
import be.alexandre01.inazuma.uhc.managers.damage.DamageManager;
import be.alexandre01.inazuma.uhc.presets.IPreset;
import be.alexandre01.inazuma.uhc.presets.Preset;
import be.alexandre01.inazuma.uhc.roles.Role;
import be.alexandre01.inazuma.uhc.roles.RoleCategory;
import be.alexandre01.inazuma.uhc.roles.RoleItem;
import be.alexandre01.inazuma.uhc.utils.*;
import be.alexandre01.inazuma_eleven.categories.Alius;
import be.alexandre01.inazuma_eleven.categories.Raimon;
import be.alexandre01.inazuma_eleven.roles.solo.Byron;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;


public class Mark extends Role implements Listener {

    int niveau = 0;
    float death = 0;
    float time = 0;
    float minage = 0;
    float multiplicateur = 1;
    public int corrupttest = 0;
    float total = death + time + minage;

    public Mark(IPreset preset) {
        super("Mark Evans",preset);
        setRoleCategory(Raimon.class);
       // setRoleToSpoil(Victoria);
        addListener(this);
        addDescription("§8- §7Votre objectif est de gagner avec §6§lRaimon");
        addDescription("§8- §7Vous possédez l’effet §6§lRésistance 1§7.");
        addDescription(" ");
        addDescription("§8- §7A chaque mort d'un joueur de §6§lRaimon§7, vous perdrez §c§l0.5 §4❤§7 permanent.");
        addDescription("§8- §7A votre mort, tous les joueurs de §6§lRaimon§7 auront §8Faiblesse 1 §7pendant §a 2 minutes§7.");
        addDescription(" ");
        CustomComponentBuilder c = new CustomComponentBuilder("");
        c.append("§8- §7Vous avez une commande nommée ");

        BaseComponent corruptButton = new TextComponent("§5/corrupt §7*§8Curseur§7*");

        BaseComponent corruptDesc = new TextComponent();
        corruptDesc.addExtra("§e- §9Utilisation 2 fois uniquement\n");
        corruptDesc.addExtra("§e- §9Savoir si il y a des joueurs de l'§5§lAcadémie §5§lAlius §9[§515 blocks§9]");
        corruptButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,corruptDesc.getExtra().toArray(new BaseComponent[0])));
        c.append(corruptButton);
        addDescription(c);
        addDescription(" ");
        addDescription("§8- §7Si §5Bellatrix§7 accepte de remplacer §5Xavier§7, vous aurez son pseudo.");




        RoleItem roleItem = new RoleItem();
        ItemBuilder itemBuilder = new ItemBuilder(Material.BOOK).setName("§6§lCahier de §7§lDavid §lEvans");

            roleItem.setRightClick(player -> {
                roleItem.updateItem(new ItemStack(Material.AIR));
                player.updateInventory();
                multiplicateur = 1.25f;
                player.sendMessage(Preset.instance.p.prefixName()+" §7§lFélicitation, vous avez trouvé le §6§lCahier §7de §7§lDavis Evans, désormais tout vos points gagné serront §c§lmultiplié §7par §c§l125% !");
                player.playSound(player.getLocation(), Sound.ORB_PICKUP, 5,5);
            });
        roleItem.setItemstack(itemBuilder.toItemStack());
        addRoleItem(roleItem);

        onLoad(new load() {
            @Override
            public void a(Player player) {
                inazumaUHC.dm.addEffectPourcentage(player, DamageManager.EffectType.RESISTANCE,1,110);

                //Timer


                    new BukkitRunnable(){
                        @Override
                        public void run(){

                            time = time + 2.5f * multiplicateur;
                            player.sendMessage(Preset.instance.p.prefixName()+" Vous gagné §62.5 points§7.");

                        }
                    }.runTaskTimerAsynchronously(InazumaUHC.getGet(), 20*60*10, 20*60*10);



                    new BukkitRunnable(){
                        @Override
                        public void run(){

                            float total = death + time + minage;
                            int niveau = 0;

                            if (total >= 10){
                                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0,false,false), true);
                                niveau++;

                                if (total >= 20){
                                    corrupttest++;
                                    niveau++;

                                    if (total >= 30){
                                        player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 2));
                                        player.updateInventory();
                                        niveau++;

                                        if (total >= 40){
                                            player.setMaxHealth(player.getMaxHealth()+2);
                                            niveau++;

                                            if (total >= 50){
                                                corrupttest++;
                                                niveau++;
                                                //Main Magique

                                                if (total >= 60){
                                                    player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 2));
                                                    player.updateInventory();
                                                    niveau++;

                                                    if (total >= 75){
                                                        niveau++;
                                                        //Entrainement Darren SOON V1.?

                                                        if (total >= 90){
                                                            niveau++;
                                                            //Unlock Ina Boost

                                                            if (total >= 100){
                                                                niveau++;
                                                                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1,false,false), true);
                                                                inazumaUHC.dm.addEffectPourcentage(player, DamageManager.EffectType.RESISTANCE,2,120);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }


                            if (total < 1000){

                                TitleUtils.sendActionBar(player,"§3§lEntrainement §f§l: §c§l" + total + "/100" + " (Niveau " + niveau + ")");

                            }

                            else if (total >= 1000){

                                TitleUtils.sendActionBar(player,"§7§lEntrainement §7Terminé");
                                cancel();

                            }



                        }
                    }.runTaskTimerAsynchronously(InazumaUHC.getGet(), 0 , 20);

                if (niveau != 0){
                    if (niveau == 1){
                        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1,1);
                        player.sendMessage(Preset.instance.p.prefixName()+" Vous êtes passé au niveau supérieur !");
                        if (niveau == 2){
                            player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1,1);
                            player.sendMessage(Preset.instance.p.prefixName()+" Vous êtes passé au niveau supérieur !");
                            if (niveau == 3){
                                player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1,1);
                                player.sendMessage(Preset.instance.p.prefixName()+" Vous êtes passé au niveau supérieur !");
                                if (niveau == 4){
                                    player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1,1);
                                    player.sendMessage(Preset.instance.p.prefixName()+" Vous êtes passé au niveau supérieur !");
                                    if (niveau == 5){
                                        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1,1);
                                        player.sendMessage(Preset.instance.p.prefixName()+" Vous êtes passé au niveau supérieur !");
                                        if (niveau == 6){
                                            player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1,1);
                                            player.sendMessage(Preset.instance.p.prefixName()+" Vous êtes passé au niveau supérieur !");
                                            if (niveau == 7){
                                                player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1,1);
                                                player.sendMessage(Preset.instance.p.prefixName()+" Vous êtes passé au niveau supérieur !");
                                                if (niveau == 8){
                                                    player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1,1);
                                                    player.sendMessage(Preset.instance.p.prefixName()+" Vous êtes passé au niveau supérieur !");
                                                    if (niveau == 9){
                                                        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1,1);
                                                        player.sendMessage(Preset.instance.p.prefixName()+" Vous êtes passé au niveau supérieur !");
                                                        if (niveau == 10){
                                                            player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1,1);
                                                            player.sendMessage(Preset.instance.p.prefixName()+" Vous êtes passé au niveau supérieur !");
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                }



        }
        );


        addCommand("corrupt", new command() {
            public int i = 0;
            @Override
            public void a(String[] args, Player player) {
                if(i >= corrupttest){
                    player.sendMessage(Preset.instance.p.prefixName()+" Vous avez dépassé le nombre d'utilisation de cette commande");
                    return;
                }
                int a = 0;
                for(Player p : PlayerUtils.getNearbyPlayersFromPlayer(player,15,15,15)){
                        if(inazumaUHC.rm.getRole(p).getRoleCategory() == null){
                            System.out.println(inazumaUHC.rm.getRole(p).getName());
                            continue;
                        }
                        if(inazumaUHC.rm.getRole(p).getRoleCategory().getClass().equals(Alius.class)){
                            a++;
                        }
                }
                if( a == 0){
                    player.sendMessage(Preset.instance.p.prefixName()+"Il n'y a aucun joueur(s) de l'Académie-Alius autour de vous.");
                }
                if( a == 1){
                    player.sendMessage(Preset.instance.p.prefixName()+"Il y a un joueur de l'Académie-Alius autour de vous.");
                }
                if( a > 1){
                    player.sendMessage(Preset.instance.p.prefixName()+"Il y a des joueurs de l'Académie-Alius autour de vous.");
                }

                i++;
            }
        });

    }

    @EventHandler
    public void onPlayerDeath(PlayerInstantDeathEvent event){
        RoleCategory roleCategory = inazumaUHC.rm.getRole(event.getPlayer()).getRoleCategory();
        Player killer = event.getKiller();
        Player killed = event.getPlayer();

        if (inazumaUHC.rm.getRole(killer.getUniqueId()).getRoleCategory().equals(Raimon.class) && !(inazumaUHC.rm.getRole(killer.getUniqueId()).getClass().equals(Mark.class))){
            death = death + 3 * multiplicateur;
        }

        if (inazumaUHC.rm.getRole(killer.getUniqueId()).getClass().equals(Mark.class)){
            death = death + 4 * multiplicateur;
        }

        if(roleCategory.getRoles().equals(Byron.class)){

            death = death + 2.5f * multiplicateur;

            for(Player player : inazumaUHC.rm.getRole(Mark.class).getPlayers()){

                player.sendMessage(Preset.instance.p.prefixName()+" Bryon est mort, vous gagné §625 points§7.");
            }
        }
        if(roleCategory.getClass().equals(Raimon.class)){
            death = death + 5 * multiplicateur;
            for(Player player : inazumaUHC.rm.getRole(Mark.class).getPlayers()){
                PatchedEntity.setMaxHealthInSilent(player,player.getMaxHealth()-1);

                player.sendMessage(Preset.instance.p.prefixName()+" Un joueur de §6Raimon§7 vient de mourir, vous perdez donc §4❤§7 permanent et gagné §650 points§7.");
            }
        }
        if(getPlayers().contains(event.getPlayer())){
            for(Role role : inazumaUHC.rm.getRoleCategory(Raimon.class).getRoles()){
                role.getPlayers().forEach(p -> {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,60*2,0),true);
                });
            }
        }
    }
    @EventHandler
    public void onBlockDestroy(BlockBreakEvent event){
        Player player = event.getPlayer();
        Block block = event.getBlock();
        int diamonds = 0;

        if(block.getType().equals(Material.DIAMOND_ORE)){
            minage = minage + 1 * multiplicateur;
            diamonds++;

        }
    }



        //Particule
       /* @EventHandler
        public void Action (PlayerInteractEvent event){
        Player player = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_AIR){
            new BukkitRunnable(){
                double phi = 0;
                public void run(){
                    phi += Math.PI/16;
                    double x; double y; double z;
                    Location loc = player.getLocation();
                    for (double t = 0; t <= 2*Math.PI ; t +=  Math.PI/16 ){
                        for (double i = 0; i <= 1; i += 1){
                            x = 0.15*(2*Math.PI-t)*cos(t + phi + i*Math.PI);
                            y = 0.5*t;
                            z = 0.15*(2*Math.PI-t)*sin( t + phi + i*Math.PI);
                            loc.add(x,y,z);
                            player.playEffect(loc, Effect.SPELL, 3);
                            loc.subtract(x,y,z);
                        }
                    }
                    if (phi > 10*Math.PI){
                        this.cancel();
                    }
                }
            }.runTaskTimer(InazumaUHC.get, 0, 1);
        }
        }*/
}
