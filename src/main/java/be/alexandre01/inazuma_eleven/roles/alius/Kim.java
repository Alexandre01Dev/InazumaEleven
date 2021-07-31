package be.alexandre01.inazuma_eleven.roles.alius;

import be.alexandre01.inazuma.uhc.InazumaUHC;
import be.alexandre01.inazuma.uhc.managers.damage.DamageManager;
import be.alexandre01.inazuma.uhc.presets.IPreset;
import be.alexandre01.inazuma.uhc.presets.Preset;

import be.alexandre01.inazuma.uhc.roles.Role;
import be.alexandre01.inazuma.uhc.roles.RoleItem;
import be.alexandre01.inazuma.uhc.utils.*;
import be.alexandre01.inazuma_eleven.InazumaEleven;
import be.alexandre01.inazuma_eleven.categories.Alius;
import be.alexandre01.inazuma_eleven.roles.raimon.Jude;
import lombok.Setter;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.Item;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_8_R3.Tuple;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;

import static org.bukkit.event.entity.EntityDamageEvent.DamageCause.ENTITY_ATTACK;

public class Kim extends Role implements Listener {
    private int points = 0;
    private int timer = 0;
    private Scoreboard score = null;
    private HashMap<Player,Long> playersTag;


    @Setter  Location location = null;
    public Kim(IPreset preset) {
        super("Kim Powell",preset);
        setRoleCategory(Alius.class);
        setRoleToSpoil(Xavier.class);
        addDescription("https://blog.inazumauhc.fr/inazuma-eleven-uhc/roles/alius/kim-powell");
        /*addDescription("§8- §7Votre objectif est de gagner avec §5§ll'§5§lAcadémie §5§lAlius");
        addDescription("§8- §7Vous possédez l’effet §6§l§4§lForce 1§7.");
        addDescription(" ");
        addDescription("§8- §7Vous disposez du §d§lCollier§7§l-§5§lAlius§7 qui vous donnera §b§lSpeed 1 §7et §6§lRésistance 1§7 (NERF) pendant §a1 minute 30§7.");
        addDescription(" ");
        CustomComponentBuilder c = new CustomComponentBuilder("");
        c.append("§8- §7Vous avez une commande nommée ");

        BaseComponent inaballtpButton = new TextComponent("§5/inaballtp §7(§9Pseudo§7) §7*§8Curseur§7*");

        BaseComponent inaballtpDesc = new TextComponent();
        inaballtpDesc.addExtra("§e- §9Utilisation 2 fois uniquement §7[Cooldown par §eEpisode§7]\n");
        inaballtpDesc.addExtra("§e- §9Téléporte le joueur de votre choix a votre ballon\n");
        inaballtpDesc.addExtra("§e- §cAttention§9, vous devrez trouver §5Janus§9 afin qu'il vous donne les coordonnées du ballon");
        inaballtpButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,inaballtpDesc.getExtra().toArray(new BaseComponent[0])));
        c.append(inaballtpButton);
        addDescription(c);;
        addDescription(" ");
        addDescription("§8- §7Vous pouvez également voir ou se situent les différents ballons de §5Janus§7 avec le §5/inaball§7.");*/


        addListener(this);

        onLoad(new load() {
            @Override
            public void a(Player player) {

                Bukkit.getScheduler().scheduleSyncDelayedTask(InazumaUHC.get, new Runnable() {
                    @Override
                    public void run() {
                        score = ScoreboardUtil.get.clone(Bukkit.getScoreboardManager().getMainScoreboard(),true);

                        if(score.getObjective("showhealth")==null){
                            Objective o = score.registerNewObjective("showhealth","dummy");
                            System.out.println("SHOWHEALTH SETUP");
                            o.setDisplayName("%§c❤");
                            o.setDisplaySlot(DisplaySlot.BELOW_NAME);
                        }
                        player.setScoreboard(score);
                        setHealth(player);
                        for(Player opposants : Bukkit.getOnlinePlayers()){
                            setHealth(opposants);
                        }
                    }
                });

                new BukkitRunnable(){
                    @Override
                    public void run(){

                        TitleUtils.sendActionBar(player,"§c§lVie §f§l: §4§l" + points + "§5§l%");
                    }
                }.runTaskTimerAsynchronously(InazumaUHC.getGet(), 20*1, 20*1);


            }
        });


        RoleItem volSword = new RoleItem();
        ItemBuilder volAlius = new ItemBuilder(Material.DIAMOND_SWORD).setName("Vol");
        volAlius.setUnbreakable();
        volAlius.addEnchant(Enchantment.DAMAGE_ALL,2);
        volSword.setItemstack(volAlius.toItemStack());
        addRoleItem(volSword);

        RoleItem healSword = new RoleItem();
        ItemBuilder healAlius = new ItemBuilder(Material.DIAMOND_SWORD).setName("Vie");
        healAlius.setUnbreakable();
        healSword.setItemstack(healAlius.toItemStack());
        healSword.deployVerificationsOnRightClick(healSword.generateVerification(new Tuple<>(RoleItem.VerificationType.COOLDOWN,10)));

        healSword.setRightClick(player -> {

            if(points>=10){
                if (player.getHealth() >= player.getMaxHealth()){
                    player.sendMessage(Preset.instance.p.prefixName()+" Vous ne pouvez pas vous heal car vous êtes full hp");
                    return;
                }
                if (player.getHealth()+2 > player.getMaxHealth()){
                    player.setHealth(player.getHealth());

                    player.sendMessage(Preset.instance.p.prefixName()+" Vous vous êtes regen de 1 coeur ");
                    points = points - 10;
                    return;
                }
                if (player.getHealth()+2 < player.getMaxHealth()){
                    player.sendMessage(Preset.instance.p.prefixName()+" Vous vous êtes regen de 1 coeur ");
                    player.setHealth(player.getHealth()+2);
                    points = points - 10;
                }
            }
            else{
                player.sendMessage(Preset.instance.p.prefixName()+"Il vous faut minimum 10% pour vous healh.");
            }
        });
        addRoleItem(healSword);

    }

    private void setHealth(Player player){
        int pourcentage  = (int) ((100*Math.round(player.getHealth())) /Math.round(player.getMaxHealth()));
        Score s = score.getObjective("showhealth").getScore(player);
        s.setScore(pourcentage);
    }
    private void setHealth(Player player,double health){
        int pourcentage  = (int) ((100*Math.round(health)) /Math.round(player.getMaxHealth()));
        Score s = score.getObjective("showhealth").getScore(player);
        s.setScore(pourcentage);
    }
    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            setHealth(player,player.getHealth()-event.getFinalDamage());
        }
    }
    @EventHandler
    public void onHeart(EntityRegainHealthEvent event){
        if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            setHealth(player,player.getHealth()+event.getAmount());
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {

        if(event.getDamager() instanceof Player && event.getEntity() instanceof Player){
            Player kim = (Player) event.getDamager();
            Player damaged = (Player) event.getEntity();
            Role role = inazumaUHC.rm.getRole(kim);
            if(role != null){
                if(role.getClass().equals(Kim.class)){
                    if(!isValidItem(kim.getItemInHand()))
                        return;

                    if(kim.getItemInHand().hasItemMeta() && kim.getItemInHand().getItemMeta().hasDisplayName()){
                        if(kim.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("Vol")) {
                            if(points>=100){
                                kim.sendMessage(Preset.instance.p.prefixName()+" Votre Heal Sword est remplit.");
                            }
                            else{
                                points = points +5;
                            }
                        }
                        else if(kim.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase("Vie")) {
                            event.setDamage(0);
                            if(points>=10){
                                if (damaged.getHealth() >= damaged.getMaxHealth()){
                                    kim.sendMessage(Preset.instance.p.prefixName()+" Vous ne pouvez pas heal" + damaged.getCustomName() + " car il est full vie.");
                                    return;
                                }
                                if (timer == 0){
                                    if (damaged.getHealth() + 2 > damaged.getMaxHealth()){
                                        damaged.setHealth(damaged.getMaxHealth());
                                        kim.sendMessage(Preset.instance.p.prefixName()+" Vous avez §csoigné§7  " + damaged.getName() + ".");
                                        damaged.sendMessage(Preset.instance.p.prefixName()+" §5§lKim §lPowell§7 vous a §csoigné§7.");
                                        points = points - 10;

                                        timer = 5;

                                        new BukkitRunnable() {
                                            @Override
                                            public void run() {

                                                timer--;
                                                if(timer == 0){
                                                    cancel();
                                                }

                                            }
                                        }.runTaskTimerAsynchronously(inazumaUHC, 20, 20);
                                    }
                                    else {
                                        kim.sendMessage(Preset.instance.p.prefixName()+" Vous avez §csoigné§7  " + damaged.getName() + ".");
                                        damaged.sendMessage(Preset.instance.p.prefixName()+" §5§lKim §lPowell§7 vous a §csoigné§7.");
                                        damaged.setHealth(damaged.getHealth() + 2);
                                        damaged.playSound(damaged.getLocation(), Sound.LAVA_POP, 1, 2);




                                        points = points - 10;

                                        timer = 5;

                                        new BukkitRunnable() {
                                            @Override
                                            public void run() {

                                                timer--;
                                                if(timer == 0){
                                                    cancel();
                                                }
                                            }
                                        }.runTaskTimerAsynchronously(inazumaUHC, 20, 20);
                                    }
                                }
                                else if(timer>0){
                                    kim.sendMessage(Preset.instance.p.prefixName()+" Merci d'attendre " + timer + " secondes avant de heal un joueur.");
                                }
                            }
                            else{
                                kim.sendMessage(Preset.instance.p.prefixName()+"Il vous faut minimum 10% pour heal un joueur.");
                            }

                        }


                    }
                }
            }
        }

    }
}
