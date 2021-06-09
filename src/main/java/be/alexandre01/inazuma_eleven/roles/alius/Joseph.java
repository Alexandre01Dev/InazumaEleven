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
import net.minecraft.server.v1_8_R3.Tuple;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Joseph extends Role {

    boolean firstUse = false;
    boolean secondUse = true;
    boolean specialUse = false;
    boolean useFirstPierre = false;
    int numberOfUse = 2;

    public Joseph(IPreset preset) {
        super("Joseph King",preset);

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
                    public void run() {
                        for(Player target : PlayerUtils.getNearbyPlayersFromPlayer(player,20,20,20))
                        {
                            if(inazumaUHC.rm.getRole(Caleb.class).getPlayers().contains(target) && inazumaUHC.rm.getRole(Caleb.class) != null)
                            {
                                if(player.hasPotionEffect(PotionEffectType.SPEED))
                                {
                                    player.removePotionEffect(PotionEffectType.SPEED);
                                }
                                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*5, 0));
                                TitleUtils.sendActionBar(player,"§7§lRaisonnance avec la §5§lPierre §lAlius");
                            }
                        }
                    }
                }.runTaskTimerAsynchronously(inazumaUHC, 1, 20*3);
            }
        });

        setRoleCategory(Alius.class);


        RoleItem morsure = new RoleItem();
        morsure.setItemstack(new ItemBuilder(Material.GHAST_TEAR).setName("§2Morsure§7-§2Sauvage").toItemStack());
        addRoleItem(morsure);
        morsure.setRightClick(player -> {

            if(!firstUse && !secondUse)
            {
                player.sendMessage("vous venez d'activer Manchot empreur apres recharge sans la premiere utilisation");
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60*20, 1));
                firstUse = true;
                specialUse = true;
                return;
            }

            if(!firstUse)
            {
                player.sendMessage("Vous venez d'activer la morsure sauvage");
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60*20, 1));
                firstUse = true;

                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        player.sendMessage("§cFin de votre capacite debut des problemes");
                        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 0));
                        PatchedEntity.setMaxHealthInSilent(player, player.getMaxHealth() - 4);
                    }
                }.runTaskLaterAsynchronously(InazumaUHC.getGet(), 60*20);

                return;
            }

            if(!secondUse)
            {
                if(player.hasPotionEffect(PotionEffectType.WEAKNESS))
                    player.removePotionEffect(PotionEffectType.WEAKNESS);
                
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60*20, 1));
                numberOfUse--;

                switch (numberOfUse)
                {
                    case 1 :
                        player.sendMessage("utilisation de la morsure sauvage apres recharge 1");
                        if (!specialUse)
                            PatchedEntity.setMaxHealthInSilent(player, player.getMaxHealth() + 2);

                        new BukkitRunnable()
                        {
                            @Override
                            public void run()
                            {
                                player.sendMessage("§cfin de votre capacite effet nefaste reduit");
                                PatchedEntity.setMaxHealthInSilent(player, player.getMaxHealth() - 4);
                            }
                        }.runTaskLaterAsynchronously(InazumaUHC.getGet(), 60*20);
                        break;

                    case 0 :
                        secondUse = true;
                        player.sendMessage("utilisation de la morsure sauvage apres recharge 2");
                        PatchedEntity.setMaxHealthInSilent(player, player.getMaxHealth() + 4);
                        if(inazumaUHC.rm.getRole(Jude.class) != null)
                        {
                            for(Player p : inazumaUHC.rm.getRole(Jude.class).getPlayers()){

                                p.sendMessage(Preset.instance.p.prefixName()+" Joseph a utilisé son item en x: " + player.getLocation().getBlockX() + " y: " + player.getLocation().getBlockY() + " z: " + player.getLocation().getBlockZ());

                            }
                        }

                        new BukkitRunnable()
                        {
                            @Override
                            public void run()
                            {
                                player.sendMessage("fin de votre capacite effets nefastes accrue");
                                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 0));
                                PatchedEntity.setMaxHealthInSilent(player, player.getMaxHealth() - 2 );
                            }
                        }.runTaskLaterAsynchronously(InazumaUHC.getGet(), 60*20);
                }
            }

        });

    }

}
