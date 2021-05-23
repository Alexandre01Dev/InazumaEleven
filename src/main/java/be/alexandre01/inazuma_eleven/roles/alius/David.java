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
import be.alexandre01.inazuma_eleven.categories.Alius;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.Tuple;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

public class David extends Role implements Listener {
    boolean hasChoose = false;
    boolean accepted = false;
    boolean refuse = false;
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
                        inazumaUHC.dm.addEffectPourcentage(player, DamageManager.EffectType.RESISTANCE,1,120);
                    }

                });
            }
        });

    }
}
