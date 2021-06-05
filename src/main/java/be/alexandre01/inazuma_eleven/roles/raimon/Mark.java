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

import java.util.HashMap;


public class Mark extends Role implements Listener {

    int level = 0;

    HashMap<Integer,Integer> expToUnlockNextLevel = new HashMap<>();
    HashMap<Integer,action> actionOnLevel = new HashMap<>();

    float toDecrement = 0;
    float death = 0;
    float time = 0;
    float minage = 0;
    float multiplicateur = 1;
    public int corrupttest = 0;
    public int inaboost = 0;
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
                multiplicateur += 0.25f;
                player.sendMessage(Preset.instance.p.prefixName()+" §7§lFélicitation, vous avez trouvé le §6§lCahier §7de §7§lDavis Evans, désormais tout vos points gagné serront §c§lmultiplié §7par §c§l125% !");
                player.playSound(player.getLocation(), Sound.ORB_PICKUP, 5,5);
            });
        roleItem.setItemstack(itemBuilder.toItemStack());
        addRoleItem(roleItem);

        for (int i = 0; i < 5; i++) {
            expToUnlockNextLevel.put(i,10);
        }

        actionOnLevel.put(1, player ->  {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0,false,false), true);
        });
        actionOnLevel.put(2, player ->  {
            corrupttest++;
        });
        actionOnLevel.put(3, player ->  {
            player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 2));
            player.updateInventory();
        });
        actionOnLevel.put(4, player ->  {
            player.setMaxHealth(player.getMaxHealth()+2);
        });
        actionOnLevel.put(5 , player ->  {
            corrupttest++;
            //Main Magique
        });
        actionOnLevel.put(6 , player ->  {
            player.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE, 2));
            player.updateInventory();
        });
        actionOnLevel.put(7 , player ->  {
            //Entrainement Darren SOON V1.?  ou Mate Random pour la V1
        });
        actionOnLevel.put(8 , player ->  {
            inaboost++;
            //Unlock Ina Boost
        });
        actionOnLevel.put(9 , player ->  {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1,false,false), true);
            inazumaUHC.dm.addEffectPourcentage(player, DamageManager.EffectType.RESISTANCE,2,120);
        });
        expToUnlockNextLevel.put(6,15);
        expToUnlockNextLevel.put(7,15);
        expToUnlockNextLevel.put(8,15);
        expToUnlockNextLevel.put(9,-1);



        onLoad(new load() {
            @Override
            public void a(Player player) {
                inazumaUHC.dm.addEffectPourcentage(player, DamageManager.EffectType.RESISTANCE,1,110);

                //Timer


                    new BukkitRunnable(){
                        @Override
                        public void run(){
                            float f = 2.5f * multiplicateur;
                            time = time + f;
                            player.sendMessage(Preset.instance.p.prefixName()+"§7Vous avez survécu §a10 minutes§7. De se fait, vous gagné §6"+Math.round((double) f*100)/ 100.0+" points§7.");
                            checkLevel(player);

                        }
                    }.runTaskTimerAsynchronously(InazumaUHC.getGet(), 20*60*10, 20*60*10);

                new BukkitRunnable(){
                    @Override
                    public void run(){
                        if (total < 100){
                            TitleUtils.sendActionBar(player,"§3§lEntrainement §f§l: §6§l" + Math.round((double) total*10)/ 10.0 + "§c/§6§l100" + " §7(§cNiveau §6§l" + level + "§7)");
                        } else {
                            TitleUtils.sendActionBar(player,"§7§lEntrainement §7Terminé");
                        }
                    }
                }.runTaskTimerAsynchronously(InazumaUHC.get, 20*2, 20*2);


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

        addCommand("ina boost", new command() {
            public int i = 0;
            @Override
            public void a(String[] args, Player player) {
                if(i >= inaboost){
                    player.sendMessage(Preset.instance.p.prefixName()+" Vous pouvez seulement utiliser qu'une fois le ina boost");
                    return;
                }
                for(Role role : inazumaUHC.rm.getRoleCategory(Raimon.class).getRoles()){
                    role.getPlayers().forEach(p -> {
                        p.setHealth(4);
                    });
                }
                player.sendMessage(Preset.instance.p.prefixName()+"Vous venez de Heal 2 Coueurs à tous les joueurs de Raimon.");
                i++;
            }
        });

    }

    public void checkLevel(Player player){
        total = death + time + minage;
        int xpToUnlock = expToUnlockNextLevel.get(level);

        if(total-toDecrement >= xpToUnlock){
            toDecrement += xpToUnlock;
            level++;
            actionOnLevel.get(level).a(player);
            player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1,1);
            player.sendMessage(Preset.instance.p.prefixName()+" Vous êtes passé au niveau supérieur !");
        }

        if (total < 100){
            TitleUtils.sendActionBar(player,"§3§lEntrainement §f§l: §6§l" + Math.round((double) total*10)/ 10.0 + "§c/§6§l100" + " §7(§cNiveau §6§l" + level + "§7)");
        } else {
            TitleUtils.sendActionBar(player,"§7§lEntrainement §7Terminé");
        }

    }

    @EventHandler
    public void onPlayerDeath(PlayerInstantDeathEvent event){
        RoleCategory roleCategory = inazumaUHC.rm.getRole(event.getPlayer()).getRoleCategory();
        Player killer = event.getKiller();
        Player killed = event.getPlayer();

        if (getPlayers().contains(killer)){
            checkLevel(killer);

            if(roleCategory.getClass().equals(Raimon.class) && !inazumaUHC.rm.getRole(killed.getUniqueId()).getClass().equals(Mark.class)){

                float c = 2f * multiplicateur;
                death = death + c;
                checkLevel(killer);

                for(Player player : inazumaUHC.rm.getRole(Mark.class).getPlayers()){

                    PatchedEntity.setMaxHealthInSilent(player,player.getMaxHealth()-1);

                    new BukkitRunnable(){
                        @Override
                        public void run(){

                            player.sendMessage(Preset.instance.p.prefixName()+" Vous avez tué un joueur de §6§lRaimon§7. Vous perdez donc §40.5❤§7 permanent et gagné§6 " + Math.round((double) c*100)/ 100.0+  " points§7.");

                        }

                    }.runTaskLater(InazumaUHC.get, 1);



                }

                for(Player player : getPlayers()){
                    checkLevel(player);
                }

                return;
            }

            else if(roleCategory.getClass().equals(Alius.class)){

                float c = 4f * multiplicateur;
                death = death + c;
                checkLevel(killer);


                new BukkitRunnable(){
                    @Override
                    public void run(){

                        killer.sendMessage(Preset.instance.p.prefixName()+" Vous avez tué un joueur de l'Académie ALius, vous gagné§6 " +Math.round((double) c*100)/ 100.0+  " points§7.");

                    }

                }.runTaskLater(InazumaUHC.get, 1);



                for(Player player : getPlayers()){
                    checkLevel(player);
                }

                return;
            }

            else if(roleCategory.getRoles().equals(Byron.class)){

                float c = 8f * multiplicateur;
                death = death + c;
                checkLevel(killer);


                new BukkitRunnable(){
                    @Override
                    public void run(){

                        killer.sendMessage(Preset.instance.p.prefixName()+" Vous avez tué §c§lByron§7, vous gagné§6 " +Math.round((double) c*100)/ 100.0+  " points§7.");

                    }

                }.runTaskLater(InazumaUHC.get, 1);



                for(Player player : getPlayers()){
                    checkLevel(player);
                }

                return;
            }
        }

        if(roleCategory.getClass().equals(Raimon.class) && !inazumaUHC.rm.getRole(killed.getUniqueId()).getClass().equals(Mark.class)){

            float b = 5f * multiplicateur;
            death = death + b;

            for(Player player : inazumaUHC.rm.getRole(Mark.class).getPlayers()){
                PatchedEntity.setMaxHealthInSilent(player,player.getMaxHealth()-1);

                new BukkitRunnable(){
                    @Override
                    public void run(){

                        player.sendMessage(Preset.instance.p.prefixName()+" Un joueur de §6§lRaimon§7 vient de mourir, vous perdez donc §40.5❤§7 permanent et gagné§6 " + Math.round((double) b*100)/ 100.0+  " points§7.");

                    }

                }.runTaskLater(InazumaUHC.get, 1);


            }

            for(Player player : getPlayers()){
                checkLevel(player);
            }
            return;
        }

        if(roleCategory.getClass().equals(Alius.class)){

            float d = 1f * multiplicateur;
            death = death + d;

            for(Player player : inazumaUHC.rm.getRole(Mark.class).getPlayers()){

                new BukkitRunnable(){
                    @Override
                    public void run(){

                        player.sendMessage(Preset.instance.p.prefixName()+" Un joueur de §5§ll'§5§lAcadémie §5§lAlius§7 vient de mourir, vous gagné §6 " +Math.round((double) d*100)/ 100.0+  " points§7.");

                    }

                }.runTaskLater(InazumaUHC.get, 1);



            }
            for(Player player : getPlayers()){
                checkLevel(player);
            }
            return;
        }

        if(roleCategory.getRoles().equals(Byron.class)){

            float a = 2.5f * multiplicateur;
            death = death + a;

            for(Player player : inazumaUHC.rm.getRole(Mark.class).getPlayers()){


                new BukkitRunnable(){
                    @Override
                    public void run(){

                        player.sendMessage(Preset.instance.p.prefixName()+" §c§lByron§7 est mort, vous gagné§6 " +Math.round((double) a*100)/ 100.0+  " points§7.");

                    }

                }.runTaskLater(InazumaUHC.get, 1);



            }

            for(Player player : getPlayers()){
                checkLevel(player);
            }
            return;
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

        if(block.getType().equals(Material.DIAMOND_ORE)){
            minage = minage + 1 * multiplicateur;
            checkLevel(player);
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

    public interface action{
        public void a(Player player);
    }
}