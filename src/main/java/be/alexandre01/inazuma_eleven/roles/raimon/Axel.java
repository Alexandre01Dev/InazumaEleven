package be.alexandre01.inazuma_eleven.roles.raimon;

import be.alexandre01.inazuma.uhc.InazumaUHC;
import be.alexandre01.inazuma.uhc.custom_events.player.PlayerInstantDeathEvent;
import be.alexandre01.inazuma.uhc.managers.damage.DamageManager;
import be.alexandre01.inazuma.uhc.presets.IPreset;

import be.alexandre01.inazuma.uhc.presets.Preset;
import be.alexandre01.inazuma.uhc.roles.Role;
import be.alexandre01.inazuma.uhc.roles.RoleItem;
import be.alexandre01.inazuma.uhc.utils.CustomComponentBuilder;
import be.alexandre01.inazuma.uhc.utils.ItemBuilder;
import be.alexandre01.inazuma.uhc.utils.PlayerUtils;
import be.alexandre01.inazuma_eleven.categories.Raimon;
import be.alexandre01.inazuma_eleven.objects.Mercenaire;
import be.alexandre01.inazuma_eleven.roles.alius.Gazelle;
import be.alexandre01.inazuma_eleven.roles.alius.Torch;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_8_R3.Tuple;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

public class Axel extends Role implements Listener {

    public boolean isSolo = false;
    int cc = 0;
    private HashMap<Player,Long> playersTag;
    Mercenaire mercenaire = new Mercenaire();
    public boolean feu = false;
    public boolean speed = false;


    public Axel(IPreset preset) {
        super("??6Axel Blaze",preset);
        playersTag = new HashMap<>();
        setRoleCategory(Raimon.class);
        addDescription("https://blog.inazumauhc.fr/inazuma-eleven-uhc/roles/raimon/axel-blaze");
        addListener(this);
        /*addDescription("??8- ??7Votre objectif est de gagner avec ??6??lRaimon");
        addDescription("??8- ??7Vous poss??dez l???effet ??4??lForce 1 ??7ainsi que ??6??lFire R??sistance??7.");
        addDescription(" ");
        CustomComponentBuilder c = new CustomComponentBuilder("");
        c.append("??8- ??7Vous poss??dez ??galement la ");

        BaseComponent tornadeButton = new TextComponent("??4??lTornade ??c??lDe ??4??lFeu??7 ??7*??8Curseur??7*");

        BaseComponent tornadeDesc = new TextComponent();
        tornadeDesc.addExtra("??e- ??9Utilisation ??6illimit?? ??7[Cooldown de ??a10 minutes??7]\n");
        tornadeDesc.addExtra("??e- ??9Donne ??b??lSpeed 2??7 pendant ??a1 minute 30");
        tornadeButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,tornadeDesc.getExtra().toArray(new BaseComponent[0])));
        c.append(tornadeButton);
        addDescription(c);
        addDescription(" ");
        addDescription("??8- ??7A chaque ??4??lkill??7, vous gagnerez ??e1 gapple ??7suppl??mentaire.");
        addDescription(" ");
        addDescription("??8- ??7Les attaques de ??cTorch??7, ??bGazelle??7 et ??6Shawn??7 ne vous atteignent pas.");*/

        onLoad(new load() {
            @Override
            public void a(Player player) {
                inazumaUHC.dm.addEffectPourcentage(player, DamageManager.EffectType.INCREASE_DAMAGE,1,110);
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0,false,false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0,false,false), true);
            }
        });
        RoleItem roleItem = new RoleItem();
        ItemBuilder itemBuilder = new ItemBuilder(Material.BLAZE_ROD).setName("??4??lTornade ??c??lDe ??4??lFeu");
        roleItem.setItemstack(itemBuilder.toItemStack());

        roleItem.deployVerificationsOnRightClick(roleItem.generateVerification(new Tuple<>(RoleItem.VerificationType.COOLDOWN,60*10)));

        roleItem.setRightClick(player -> {
            feu = true;
            if(!isSolo){
                new BukkitRunnable(){
                    @Override
                    public void run(){

                        feu = false;
                        player.setWalkSpeed(0.2F);
                        cc = 0;

                    }
                }.runTaskLater(inazumaUHC,20*90);

            }

            if(!isSolo){
                player.sendMessage(Preset.instance.p.prefixName()+" Vous venez d'utiliser la ??4??lTornade ??c??lDe ??4??lFeu??7.");
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 90*20, 0,false,false), true);
            }
            else{
                player.sendMessage(Preset.instance.p.prefixName()+" Vous venez d'utiliser la ??4??lTornade ??c??lDe ??4??lFeu??7.");
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 90*20, 1,false,false), true);
            }

        });
        addRoleItem(roleItem);
    }

    @EventHandler
    public void onKillEvent(PlayerInstantDeathEvent event){
        Player killer = event.getKiller();
        if(killer != null){
            if (inazumaUHC.rm.getRole(killer) == this){

                if(isSolo)
                {
                    if(mercenaire != null && mercenaire.list != null && mercenaire.list.size() > 1)
                    {
                        Collections.shuffle(mercenaire.list);
                        if(mercenaire.list.get(0) == mercenaire.mercenaire)
                        {
                            mercenaire.list.remove(1);
                        }
                        else mercenaire.list.remove(0);
                    }
                }

                new BukkitRunnable(){
                    @Override
                    public void run(){

                        killer.sendMessage(Preset.instance.p.prefixName()+" Vous recevez une pomme d'or suppl??mentaire pour avoir fait un kill.");

                    }

                }.runTaskLater(InazumaUHC.get, 1);


                killer.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));
                killer.updateInventory();
            }
        }
    }

    @EventHandler
    public void onDamageByAxel(EntityDamageByEntityEvent event){

        if(isSolo)
        {
            if(event.getEntity() instanceof Player && event.getDamager() instanceof Player ){
                Player damager = (Player) event.getDamager();

                if(!getPlayers().contains(damager))
                    return;

                Player victim = (Player) event.getEntity();

                playersTag.put(victim,new Date().getTime());

            }
        }


    }

    @EventHandler
    public void onDeathDuringTag(PlayerInstantDeathEvent event) {
        Player player = event.getPlayer();
        if (playersTag.containsKey(player)) {
            if (new Date().getTime() - playersTag.get(player) > 30000) {
                if(player == mercenaire.mercenaire)
                {
                    setRoleCategory(Raimon.class);
                    for (Player axel : getPlayers())
                    {
                        axel.playSound(axel.getLocation(), Sound.LEVEL_UP, 1, 1);
                        new BukkitRunnable() {
                            @Override
                            public void run() {

                                axel.sendMessage(Preset.instance.p.prefixName() + "Vous avez tu?? votre m??ga giga super poto le merce. Vous revenez donc ?? Raimon. Oh mince c'est plus drole !");
                            }
                        }.runTaskLaterAsynchronously(inazumaUHC, 1);
                    }
                }

                playersTag.remove(player);
                return;
            }
        }
        else{
            if(player == mercenaire.mercenaire)
            {
                for (Player axel : getPlayers())
                {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            axel.sendMessage(Preset.instance.p.prefixName() + "Ta vie c'est de la merde ta soeur elle est morte et le gars qui l'a tu?? aussi donc reste tout seul et chichi");
                        }
                    }.runTaskLaterAsynchronously(inazumaUHC, 1);

                }

            }
        }
    }

    @EventHandler
    public void onBurning(EntityDamageByEntityEvent event) {

        if(event.getDamager() instanceof Player)
        {
            Player axel = (Player) event.getDamager();
            Entity entity = event.getEntity();
            Role role = inazumaUHC.rm.getRole(axel);
            float walkspeed = axel.getWalkSpeed();
            if(role.getClass().equals(Axel.class)){

                if(feu){
                    if(entity instanceof Player)
                    {
                        Player player = (Player)entity;
                        if(!inazumaUHC.rm.getRole(player).getClass().equals(Gazelle.class) && !inazumaUHC.rm.getRole(player).getClass().equals(Axel.class) && !inazumaUHC.rm.getRole(player).getClass().equals(Shawn.class) &&  !inazumaUHC.rm.getRole(player).getClass().equals(Hurley.class)&&  !inazumaUHC.rm.getRole(player).getClass().equals(Aiden.class)){
                            player.setFireTicks(20*5);
                        }
                    }
                    else {
                        entity.setFireTicks(20*5);
                    }


                    if(!isSolo){
                        if(cc== 0){
                            axel.setWalkSpeed(walkspeed+0.025F);
                            speed = true;
                            cc = 1;
                        }

                    }
                }
            }
        }
    }

    private void addEffectAfter(Player player, long l, Nathan.action a, PotionEffect... potionEffects){
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
    public interface action{
        public void a();
    }
}

