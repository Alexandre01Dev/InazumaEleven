package be.alexandre01.inazuma_eleven.roles.alius;

import be.alexandre01.inazuma.uhc.InazumaUHC;
import be.alexandre01.inazuma.uhc.custom_events.episode.EpisodeChangeEvent;
import be.alexandre01.inazuma.uhc.managers.damage.DamageManager;
import be.alexandre01.inazuma.uhc.presets.IPreset;
import be.alexandre01.inazuma.uhc.presets.Preset;

import be.alexandre01.inazuma.uhc.roles.Role;
import be.alexandre01.inazuma.uhc.roles.RoleItem;
import be.alexandre01.inazuma.uhc.utils.CustomComponentBuilder;
import be.alexandre01.inazuma.uhc.utils.ItemBuilder;
import be.alexandre01.inazuma.uhc.utils.PatchedEntity;
import be.alexandre01.inazuma_eleven.InazumaEleven;
import be.alexandre01.inazuma_eleven.categories.Alius;
import be.alexandre01.inazuma_eleven.roles.raimon.Jude;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.Tuple;
import org.bukkit.Bukkit;
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

public class David extends Role implements Listener {

    public boolean firstUse = false;
    boolean secondUse = true;
    boolean specialUse = false;
    boolean useFirstPierre = false;
    int numberOfUse = 2;
    private BukkitTask bukkitTask;
    public David(IPreset preset) {
        super("David Samford",preset);

        addDescription("§8- §7Votre objectif est de gagner avec §5§ll'§5§lAcadémie §5§lAlius");
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
        addDescription(c);;


        addListener(this);
        setRoleCategory(Alius.class);
        onLoad(new load() {
            @Override
            public void a(Player player) {
                onLoad(new load() {
                    @Override
                    public void a(Player player) {
                        inazumaUHC.dm.addEffectPourcentage(player, DamageManager.EffectType.INCREASE_DAMAGE,2,120);
                    }

                });
            }
        });


        RoleItem roleItem = new RoleItem();
        ItemBuilder it = new ItemBuilder(Material.NETHER_STAR).setName("§c§lManchot §c§lEmpereur §4§lN°1");
        roleItem.setItemstack(it.toItemStack());
        addRoleItem(roleItem);
        roleItem.setRightClick(player -> {

            if(!firstUse && !secondUse)
            {
                player.sendMessage("vous venez d'activer Manchot empreur apres recharge sans la premiere utilisation");
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60*20, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60*20, 0));
                firstUse = true;
                specialUse = true;
                return;
            }

            if(!firstUse)
            {
                player.sendMessage("Vous venez d'activer Manchot empreur");
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60*20, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60*20, 0));
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

                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60*20, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60*20, 0));
                numberOfUse--;

                switch (numberOfUse)
                {
                    case 1 :
                        player.sendMessage("utilisation du manchot empreur apres recharge 1");

                        new BukkitRunnable()
                        {
                            @Override
                            public void run()
                            {
                                player.sendMessage("§cfin de votre capacite effet nefaste reduit");
                                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 300*20, 0));
                                PatchedEntity.setMaxHealthInSilent(player, player.getMaxHealth() -2);
                            }
                        }.runTaskLaterAsynchronously(InazumaUHC.getGet(), 60*20);
                        break;

                    case 0 :
                        secondUse = true;
                        player.sendMessage("utilisation du manchot empreur apres recharge 2");
                        PatchedEntity.setMaxHealthInSilent(player, player.getMaxHealth() + 2);
                        if(inazumaUHC.rm.getRole(Jude.class) != null)
                        {
                            for(Player p : inazumaUHC.rm.getRole(Jude.class).getPlayers()){

                                p.sendMessage(Preset.instance.p.prefixName()+" David a utilisé son item en x: " + player.getLocation().getBlockX() + " y: " + player.getLocation().getBlockY() + " z: " + player.getLocation().getBlockZ());

                            }
                        }


                        new BukkitRunnable()
                        {
                            @Override
                            public void run()
                            {
                                player.sendMessage("fin de votre capacite effets nefastes accrue");
                                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 0));
                                PatchedEntity.setMaxHealthInSilent(player, player.getMaxHealth() -4 );
                            }
                        }.runTaskLaterAsynchronously(InazumaUHC.getGet(), 60*20);
                }
            }
        });

    }
}
