package be.alexandre01.inazuma_eleven.roles.alius;

import be.alexandre01.inazuma.uhc.InazumaUHC;
import be.alexandre01.inazuma.uhc.managers.damage.DamageManager;
import be.alexandre01.inazuma.uhc.presets.IPreset;
import be.alexandre01.inazuma.uhc.presets.Preset;

import be.alexandre01.inazuma.uhc.roles.Role;
import be.alexandre01.inazuma.uhc.roles.RoleItem;
import be.alexandre01.inazuma.uhc.utils.*;
import be.alexandre01.inazuma_eleven.categories.Alius;
import be.alexandre01.inazuma_eleven.roles.raimon.Jude;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_8_R3.Tuple;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Joseph extends Role {

    boolean firstUse = false;
    boolean secondUse = true;
    boolean specialUse = false;
    boolean useFirstPierre = false;
    int numberOfUse = 2;

    public Joseph(IPreset preset) {
        super("Joseph King",preset);
        setRoleCategory(Alius.class);
        addDescription("§8- §7Votre objectif est de gagner avec §5§ll'§5§lAcadémie §5§lAlius");
        CustomComponentBuilder c = new CustomComponentBuilder("");
        c.append("§8- §7Vous avez un iteme nommée ");

        BaseComponent morsureButton = new TextComponent("§2Morsure§7-§2Sauvage §7*§8Curseur§7*");

        BaseComponent morsureDesc = new TextComponent();
        morsureDesc.addExtra("§e- §9Utilisation 3 fois uniquement §7[Cooldown de §a10 minutes§7]\n");
        morsureDesc.addExtra("§e- §9Vous donnera §6§lRésistance 2§9 pendant §a2 minutes\n");
        morsureDesc.addExtra("§e- §9La première utilisation vous mettra §8§lFaiblesse 1§7 pendants §a2 minutes\n");
        morsureDesc.addExtra("§e- §9La deuxieme utilisation vous mettra §8§lFaiblesse 1§7 pendants §a5 minutes\n");
        morsureDesc.addExtra("§e- §9La troisieme utilisation vous mettra §8§lFaiblesse 1§7 permanent");
        morsureButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,morsureDesc.getExtra().toArray(new BaseComponent[0])));
        c.append(morsureButton);
        addDescription(c);



        onLoad(new load() {
            @Override
            public void a(Player player) {
                inazumaUHC.dm.addEffectPourcentage(player, DamageManager.EffectType.RESISTANCE,2,120);

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




        RoleItem morsure = new RoleItem();
        morsure.setItemstack(new ItemBuilder(Material.GHAST_TEAR).setName("§2Morsure§7-§2Sauvage").toItemStack());
        morsure.deployVerificationsOnRightClick(morsure.generateVerification(new Tuple<>(RoleItem.VerificationType.EPISODES,1)));
        addRoleItem(morsure);
        morsure.setRightClick(player -> {

            if(!firstUse && !secondUse)
            {
                player.sendMessage(Preset.instance.p.prefixName()+" Vous venez d'utiliser la §2Morsure§7-§2Sauvage§7.");
                bloodParticles(player);
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60*20, 1, false, false), true);
                firstUse = true;
                specialUse = true;
                return;
            }

            if(!firstUse)
            {
                player.sendMessage(Preset.instance.p.prefixName()+" Vous venez d'utiliser la §2Morsure§7-§2Sauvage§7.");
                bloodParticles(player);
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60*20, 1, false, false), true);
                firstUse = true;

                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        player.sendMessage(Preset.instance.p.prefixName()+" La §2Morsure§7-§2Sauvage§7 vous a énormément affaibli et vous avez donc désormais §8§lWeakness I§7 pendant §a5 minutes§7 et perdu §c§l2 §4❤§7 permanent. Retrouvez §5§lCaleb§7 afin de remédier à ce §cproblème§7.");
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
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60*20, 1, false, false), true);
                numberOfUse--;

                switch (numberOfUse)
                {
                    case 1 :
                        player.sendMessage(Preset.instance.p.prefixName()+" Vous venez d'utiliser la §2Morsure§7-§2Sauvage§7.");
                        if (!specialUse)
                            PatchedEntity.setMaxHealthInSilent(player, player.getMaxHealth() + 2);

                        new BukkitRunnable()
                        {
                            @Override
                            public void run()
                            {
                                player.sendMessage(Preset.instance.p.prefixName()+" La §2Morsure§7-§2Sauvage§7 vous a énormément affaibli et vous avez donc désormais perdu §c§l2 §4❤§7 permanent.");
                                PatchedEntity.setMaxHealthInSilent(player, player.getMaxHealth() - 4);
                            }
                        }.runTaskLaterAsynchronously(InazumaUHC.getGet(), 60*20);
                        break;

                    case 0 :
                        secondUse = true;
                        player.sendMessage(Preset.instance.p.prefixName()+" Vous venez d'utiliser la §2Morsure§7-§2Sauvage§7.");
                        PatchedEntity.setMaxHealthInSilent(player, player.getMaxHealth() + 4);
                        if(inazumaUHC.rm.getRole(Jude.class) != null)
                        {
                            for(Player p : inazumaUHC.rm.getRole(Jude.class).getPlayers()){

                                p.sendMessage(Preset.instance.p.prefixName()+" §5§lJoseph§7 a utilisé son item en x: " + player.getLocation().getBlockX() + " y: " + player.getLocation().getBlockY() + " z: " + player.getLocation().getBlockZ());

                            }
                        }

                        new BukkitRunnable()
                        {
                            @Override
                            public void run()
                            {
                                player.sendMessage(Preset.instance.p.prefixName()+" La §2Morsure§7-§2Sauvage§7 vous a énormément affaibli et vous avez donc désormais §8§lWeakness I§7 permanent et perdu §c§l2 §4❤§7 permanent.");
                                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 0, false, false), true);
                                PatchedEntity.setMaxHealthInSilent(player, player.getMaxHealth() - 2 );
                            }
                        }.runTaskLaterAsynchronously(InazumaUHC.getGet(), 60*20);
                }
            }

        });

    }

    void bloodParticles(Player player)
    {
        player.getWorld().playEffect(player.getEyeLocation().add(0, -0.3, 0), Effect.STEP_SOUND, Material.REDSTONE_BLOCK);
    }


}
