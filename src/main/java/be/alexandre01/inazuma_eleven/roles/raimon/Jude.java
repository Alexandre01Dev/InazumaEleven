package be.alexandre01.inazuma_eleven.roles.raimon;

import be.alexandre01.inazuma.uhc.InazumaUHC;
import be.alexandre01.inazuma.uhc.custom_events.player.PlayerInstantDeathEvent;
import be.alexandre01.inazuma.uhc.presets.IPreset;
import be.alexandre01.inazuma.uhc.presets.Preset;
import be.alexandre01.inazuma.uhc.roles.Role;
import be.alexandre01.inazuma.uhc.utils.ScoreboardUtil;
import be.alexandre01.inazuma_eleven.categories.Raimon;
import be.alexandre01.inazuma_eleven.objects.LocalRaimon;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Date;
import java.util.HashMap;


public class Jude extends Role implements Listener {

    private Scoreboard score = null;
    private boolean isStructureSpawned = false;
    private HashMap<Player,Long> playersTag;
    public Jude(IPreset preset) {
        super("Jude Sharp",preset);
        playersTag = new HashMap<>();
        setRoleCategory(Raimon.class);
        addListener(this);

        addDescription("§8- §7Votre objectif est de gagner avec §6§lRaimon");
        addDescription("§8- §7Vous possédez l’effet §b§lSpeed 1§7.");
        addDescription("§8- §7Vous voyez également la §4vie§7 des joueurs au-dessus de leurs têtes.");
        addDescription("§8- §7Vous recevrez les coordonnées approximatives à chaque utilisation d'un §d§lCollier§7§l-§5§lAlius§7.");


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
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0,false,false), true);
                      player.setScoreboard(score);
                      setHealth(player);
                    for(Player opposants : Bukkit.getOnlinePlayers()){
                        setHealth(opposants);
                    }
                    }
                   });
                }
        });


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
    public void onDamageByJude(EntityDamageByEntityEvent event){
        if(isStructureSpawned)
            return;


        if(event.getEntity() instanceof Player && event.getDamager() instanceof Player ){
            Player damager = (Player) event.getDamager();

            if(!getPlayers().contains(damager))
                return;

            Player victim = (Player) event.getEntity();

            playersTag.put(victim,new Date().getTime());

        }
    }

    @EventHandler
    public void onDeathDuringTag(PlayerInstantDeathEvent event){
        Player player = event.getPlayer();
        if(playersTag.containsKey(player)){
            if(new Date().getTime()-playersTag.get(player) > 30000){
                playersTag.remove(player);
                return;
            }

            for(Player p : getPlayers()){

                new BukkitRunnable(){
                    @Override
                    public void run(){

                        p.sendMessage(Preset.instance.p.prefixName()+" "+ player+" vient de mourir, vous allez recevoir les cordonnées dans 5 minutes.");

                    }

                }.runTaskLater(InazumaUHC.get, 10);


                LocalRaimon localRaimon = new LocalRaimon();
                localRaimon.spawn();

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        p.sendMessage(Preset.instance.p.prefixName()+"§e Les coordonnées sont en X:"+localRaimon.x+" | Z:"+localRaimon.z);
                    }
                }.runTaskLaterAsynchronously(InazumaUHC.get,20*60*5);
            }
        }
    }
    @EventHandler
    public void onHeart(EntityRegainHealthEvent event){
        if(event.getEntity() instanceof Player){
            Player player = (Player) event.getEntity();
            setHealth(player,player.getHealth()+event.getAmount());
        }
    }

    public static void collierAlliusNotif(Location location){
        int x = (int) ((location.getBlockX()) / 100);
        int z = (int) ((location.getBlockZ()) / 100);
        int roundedX =  x * 100;
        int roundedZ = z * 100;

        Jude jude = (Jude) InazumaUHC.get.rm.getRole(Jude.class);

        if(jude == null)
            return;

        for(Player player : jude.getPlayers()){
            player.sendMessage(Preset.instance.p.prefixName()+" §7Un §eCollier-Allius§7 vient d'être utilisé approxivement  §e"+roundedX+"X §c|§e"+ roundedZ+"Z");
        }
    }
}