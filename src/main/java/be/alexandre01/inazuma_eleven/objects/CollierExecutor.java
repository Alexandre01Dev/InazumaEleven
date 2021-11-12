package be.alexandre01.inazuma_eleven.objects;

import be.alexandre01.inazuma.uhc.InazumaUHC;
import be.alexandre01.inazuma.uhc.managers.damage.DamageManager;
import be.alexandre01.inazuma.uhc.presets.Preset;
import be.alexandre01.inazuma.uhc.roles.RoleItem;
import be.alexandre01.inazuma.uhc.utils.Episode;
import be.alexandre01.inazuma.uhc.utils.ItemBuilder;
import be.alexandre01.inazuma.uhc.utils.PatchedEntity;
import be.alexandre01.inazuma_eleven.roles.raimon.Jack;
import be.alexandre01.inazuma_eleven.roles.raimon.Jude;
import net.minecraft.server.v1_8_R3.Tuple;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;

public class CollierExecutor extends RoleItem {
    int episode = 0;
    PotionEffectType[] potionEffects = new PotionEffectType[]{PotionEffectType.INCREASE_DAMAGE,PotionEffectType.DAMAGE_RESISTANCE,PotionEffectType.SPEED};
    PotionEffectType actualPotionEffect;


    public CollierExecutor(){
        InazumaUHC i = InazumaUHC.get;
        setItemstack(new ItemBuilder(Material.NETHER_STAR).setName("§d§lCollier§7§l-§5§lAlius").toItemStack());
        deployVerificationsOnRightClick(generateVerification(new Tuple<>(RoleItem.VerificationType.COOLDOWN,3)));
        setRightClick(new RoleItem.RightClick() {
            boolean activate = false;
            BukkitTask bukkitTask = null;
            int defaultRemaining = 3*6;
            int remaining = defaultRemaining;
            Integer episode = null;
            @Override
            public void execute(Player player) {
                Jude.collierAlliusNotif(player.getLocation());
                Jack.nearAliusActivation(player.getLocation());

                if(episode != Episode.getEpisode()){
                    episode = Episode.getEpisode();
                    remaining = defaultRemaining;
                    int i = new Random().nextInt((potionEffects.length - 1));
                    actualPotionEffect = potionEffects[i];
                }


                if(!activate){
                    player.playSound(player.getLocation(),"block.lever.click",1,1);
                    player.sendMessage(Preset.instance.p.prefixName()+" Vous rentrez en résonance avec la §8§lpierre§7§l-§5§lalius.");
                    //Placer l'effet à désirer
                    double addition = 1;
                    if(actualPotionEffect == PotionEffectType.INCREASE_DAMAGE){
                        if(i.dm.getIncreased_damage().containsKey(player)){
                            addition = i.dm.getEffectPourcentage(player, DamageManager.EffectType.INCREASE_DAMAGE);
                        }
                        if(Capitaine.getInstance().capitaineList.contains(i.rm.getRole(player))){
                            addition += 0.07;
                        }else{
                            addition += 0.05;
                        }
                        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,Integer.MAX_VALUE, 0,false,false), true);
                        i.dm.addEffectPourcentage(player, DamageManager.EffectType.INCREASE_DAMAGE,1, (int) (addition*100));
                    }
                    if(actualPotionEffect == PotionEffectType.DAMAGE_RESISTANCE){
                        if(i.dm.getIncreased_damage().containsKey(player)){
                            addition = i.dm.getEffectPourcentage(player, DamageManager.EffectType.RESISTANCE);
                        }
                        if(Capitaine.getInstance().capitaineList.contains(i.rm.getRole(player))){
                            addition += 0.07;
                        }else{
                            addition += 0.05;
                        }
                        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,Integer.MAX_VALUE, 0,false,false), true);
                        i.dm.addEffectPourcentage(player, DamageManager.EffectType.RESISTANCE,1, (int) (addition*100));
                    }
                    if(actualPotionEffect == PotionEffectType.SPEED){
                        float walkSpeed =  player.getWalkSpeed();
                        if(Capitaine.getInstance().capitaineList.contains(i.rm.getRole(player))){
                            walkSpeed += 0.02;
                        }else{
                            walkSpeed+= 0.01;
                        }

                        player.setWalkSpeed(walkSpeed);
                    }
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 90*20, 0,false,false), true);
                    bukkitTask = new BukkitRunnable() {
                        @Override
                        public void run() {
                            remaining--;
                            if(remaining == 0){
                                player.sendMessage(Preset.instance.p.prefixName()+" Vous avez surchargé l'utilisation de la §8§lpierre§7§l-§5§lalius, vous prendrez des dégats toutes les 10 secondes");
                            }
                            if(remaining < 0){
                                PatchedEntity.damage(player,2,true);
                            }
                        }
                    }.runTaskTimerAsynchronously(InazumaUHC.get,0,20*10);
                }else {
                    player.playSound(player.getLocation(),"block.lever.click",1,1);
                    double addition = 1;
                    if(actualPotionEffect == PotionEffectType.INCREASE_DAMAGE){
                        if(i.dm.getIncreased_damage().containsKey(player)){
                            addition = i.dm.getEffectPourcentage(player, DamageManager.EffectType.INCREASE_DAMAGE);
                        }
                        if(Capitaine.getInstance().capitaineList.contains(i.rm.getRole(player))){
                            addition -= 0.07;
                        }else{
                            addition -= 0.05;
                        }
                        player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                        i.dm.addEffectPourcentage(player, DamageManager.EffectType.INCREASE_DAMAGE,1, (int) (addition*100));
                    }
                    if(actualPotionEffect == PotionEffectType.DAMAGE_RESISTANCE){
                        if(i.dm.getIncreased_damage().containsKey(player)){
                            addition = i.dm.getEffectPourcentage(player, DamageManager.EffectType.RESISTANCE);
                        }
                        if(Capitaine.getInstance().capitaineList.contains(i.rm.getRole(player))){
                            addition -= 0.07;
                        }else{
                            addition -= 0.05;
                        }
                        player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                        i.dm.addEffectPourcentage(player, DamageManager.EffectType.RESISTANCE,1, (int) (addition*100));
                    }
                    if(actualPotionEffect == PotionEffectType.SPEED){
                        float walkSpeed =  player.getWalkSpeed();
                        if(Capitaine.getInstance().capitaineList.contains(i.rm.getRole(player))){
                            walkSpeed -= 0.02;
                        }else{
                            walkSpeed-= 0.01;
                        }

                        player.setWalkSpeed(walkSpeed);
                    }
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 90*20, 0,false,false), true);
                    bukkitTask = new BukkitRunnable() {
                        @Override
                        public void run() {
                            remaining--;
                            if(remaining == 0){
                                player.sendMessage(Preset.instance.p.prefixName()+" Vous avez surchargé l'utilisation de la §8§lpierre§7§l-§5§lalius, vous prendrez des dégats toutes les 10 secondes");
                            }
                            if(remaining < 0){
                                PatchedEntity.damage(player,2,true);
                            }
                        }
                    }.runTaskTimerAsynchronously(InazumaUHC.get,0,20*10);
                    if(bukkitTask != null){
                        bukkitTask.cancel();
                        if(remaining < 0){
                            remaining = 0;
                        }
                    }
                }

            }
        });
    }
}
