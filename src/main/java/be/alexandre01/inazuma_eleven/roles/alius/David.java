package be.alexandre01.inazuma_eleven.roles.alius;

import be.alexandre01.inazuma.uhc.InazumaUHC;
import be.alexandre01.inazuma.uhc.custom_events.episode.EpisodeChangeEvent;
import be.alexandre01.inazuma.uhc.managers.damage.DamageManager;
import be.alexandre01.inazuma.uhc.presets.IPreset;
import be.alexandre01.inazuma.uhc.presets.Preset;

import be.alexandre01.inazuma.uhc.roles.Role;
import be.alexandre01.inazuma.uhc.roles.RoleItem;
import be.alexandre01.inazuma.uhc.utils.*;
import be.alexandre01.inazuma_eleven.InazumaEleven;
import be.alexandre01.inazuma_eleven.categories.Alius;
import be.alexandre01.inazuma_eleven.roles.raimon.Jude;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.Tuple;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import javax.sound.midi.Patch;
import java.util.List;

public class David extends Role implements Listener {

    public boolean firstUse = false;
    boolean secondUse = true;
    boolean specialUse = false;
    boolean useFirstPierre = false;
    int numberOfUse = 2;
    private BukkitTask bukkitTask;
    public David(IPreset preset) {
        super("David Samford",preset);

        /*addDescription("§8- §7Votre objectif est de gagner avec §5§ll'§5§lAcadémie §5§lAlius");
        addDescription(" ");
        CustomComponentBuilder c = new CustomComponentBuilder("");
        c.append("§8- §7En échange de §c§l2 §4❤§7§7 permanent :");

        BaseComponent manchotButton = new TextComponent("§c§lManchot §c§lEmpereur §4§lN°1 §7*§8Curseur§7*");

        BaseComponent manchotDesc = new TextComponent();
        manchotDesc.addExtra("§e- §c§l⚠ §9Vous pouvez également faire §5/manchot §aaccept§9 ou §5/manchot §crefuse\n");
        manchotDesc.addExtra("§e- §9Utilisation par §eEpisode\n");
        manchotDesc.addExtra("§e- §9Donne §4§lForce BOOST§7 et §b§lSpeed 1§9 pendant §a2 minutes \n");
        manchotDesc.addExtra("§e- §c⚠§9 vous perdrez §4 0.5 ❤§7§7 permanent chaque §eEpisode\n");
        manchotDesc.addExtra("§e- §c⚠§9 Vous avez §a5 minutes§9 à chaque début d'§eEpisode§9 pour le prendre");
        manchotButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,manchotDesc.getExtra().toArray(new BaseComponent[0])));
        c.append(manchotButton);
        addDescription(c);;*/
        addDescription("https://app.gitbook.com/@inazumauhcpro/s/inazuma-gitbook/inazuma-eleven-uhc/roles/alius/david");

        addListener(this);
        setRoleCategory(Alius.class);
        onLoad(new load() {
            @Override
            public void a(Player player) {

                inazumaUHC.dm.addEffectPourcentage(player, DamageManager.EffectType.INCREASE_DAMAGE,2,120);

                new BukkitRunnable() {
                    @Override
                    public void run(){
                        List<Player> list = PlayerUtils.getNearbyPlayersFromPlayer(player,20,20,20);
                        if (list.isEmpty()){
                            return;
                        }
                        for(Player target : list)
                        {
                            if(inazumaUHC.rm.getRole(Caleb.class) == null)
                                break;
                            if(inazumaUHC.rm.getRole(Caleb.class).getPlayers().contains(target))
                            {
                                if(player.hasPotionEffect(PotionEffectType.SPEED))
                                {
                                    player.removePotionEffect(PotionEffectType.SPEED);
                                }
                                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*5, 0, false, false), true);
                                TitleUtils.sendActionBar(player,"§7§lRaisonnance avec la §5§lPierre §lAlius");
                            }
                        }
                    }
                }.runTaskTimerAsynchronously(inazumaUHC, 1, 20*3);
            }
        });


        RoleItem manchot = new RoleItem();
        manchot.setItemstack(new ItemBuilder(Material.NETHER_WARTS).setName("§c§lManchot §c§lEmpereur §4§lN°1").toItemStack());
        manchot.deployVerificationsOnRightClick(manchot.generateVerification(new Tuple<>(RoleItem.VerificationType.EPISODES,1)));
        addRoleItem(manchot);
        manchot.setRightClick(player -> {

            if(!firstUse && !secondUse)
            {
                player.sendMessage(Preset.instance.p.prefixName()+" Vous venez d'utiliser le §c§lManchot §c§lEmpereur §4§lN°1§7.");
                bloodParticles(player);
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60*20, 1, false, false), true);
                firstUse = true;
                specialUse = true;
                return;
            }

            if(!firstUse)
            {
                player.sendMessage(Preset.instance.p.prefixName()+" Vous venez d'utiliser le §c§lManchot §c§lEmpereur §4§lN°1§7.");
                bloodParticles(player);
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60*20, 1, false, false), true);
                firstUse = true;

                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        player.sendMessage(Preset.instance.p.prefixName()+" Le §c§lManchot §c§lEmpereur §4§lN°1§7 vous a énormément affaibli et vous avez donc désormais §8§lWeakness I§7 pendant §a5 minutes§7 et perdu §c§l2 §4❤§7 permanent. Retrouvez §5§lCaleb§7 afin de remédier à ce §cproblème§7.");
                        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 0, false, false), true);
                        PatchedEntity.setMaxHealthInSilent(player, player.getMaxHealth() - 4);
                    }
                }.runTaskLaterAsynchronously(InazumaUHC.getGet(), 60*20);

                return;
            }

            if(!secondUse)
            {
                if(player.hasPotionEffect(PotionEffectType.WEAKNESS))
                    player.removePotionEffect(PotionEffectType.WEAKNESS);

                bloodParticles(player);
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60*20, 1, false, false), true);
                numberOfUse--;

                switch (numberOfUse)
                {
                    case 1 :
                        player.sendMessage(Preset.instance.p.prefixName()+" Vous venez d'utiliser le §c§lManchot §c§lEmpereur §4§lN°1§7.");

                        new BukkitRunnable()
                        {
                            @Override
                            public void run()
                            {
                                player.sendMessage(Preset.instance.p.prefixName()+" Le §c§lManchot §c§lEmpereur §4§lN°1§7 vous a énormément affaibli et vous avez donc désormais perdu §c§l2 §4❤§7 permanent.");
                                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 300*20, 0, false, false), true);
                                PatchedEntity.setMaxHealthInSilent(player, player.getMaxHealth() -2);
                            }
                        }.runTaskLaterAsynchronously(InazumaUHC.getGet(), 60*20);
                        break;

                    case 0 :
                        secondUse = true;
                        player.sendMessage(Preset.instance.p.prefixName()+" Vous venez d'utiliser le §c§lManchot §c§lEmpereur §4§lN°1§7.");
                        PatchedEntity.setMaxHealthInSilent(player, player.getMaxHealth() + 2);
                        if(inazumaUHC.rm.getRole(Jude.class) != null)
                        {
                            for(Player p : inazumaUHC.rm.getRole(Jude.class).getPlayers()){

                                p.sendMessage(Preset.instance.p.prefixName()+" §5§lDavid§7 a utilisé son item en x: " + player.getLocation().getBlockX() + " y: " + player.getLocation().getBlockY() + " z: " + player.getLocation().getBlockZ());

                            }
                        }


                        new BukkitRunnable()
                        {
                            @Override
                            public void run()
                            {
                                player.sendMessage(Preset.instance.p.prefixName()+" Le §c§lManchot §c§lEmpereur §4§lN°1§7 vous a énormément affaibli et vous avez donc désormais §8§lWeakness I§7 permanent et perdu §c§l2 §4❤§7 permanent.");
                                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 0, false, false), true);
                                PatchedEntity.setMaxHealthInSilent(player, player.getMaxHealth() -4 );
                            }
                        }.runTaskLaterAsynchronously(InazumaUHC.getGet(), 60*20);
                }
            }
        });

        addRoleItem(manchot);

    }

    void bloodParticles(Player player)
    {
        player.getWorld().playEffect(player.getEyeLocation().add(0, -0.3, 0), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
    }

}
