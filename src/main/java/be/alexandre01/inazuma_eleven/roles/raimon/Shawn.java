package be.alexandre01.inazuma_eleven.roles.raimon;

import be.alexandre01.inazuma.uhc.managers.damage.DamageManager;
import be.alexandre01.inazuma.uhc.presets.IPreset;
import be.alexandre01.inazuma.uhc.presets.Preset;

import be.alexandre01.inazuma.uhc.roles.Role;
import be.alexandre01.inazuma.uhc.roles.RoleItem;
import be.alexandre01.inazuma.uhc.utils.CustomComponentBuilder;
import be.alexandre01.inazuma.uhc.utils.ItemBuilder;
import be.alexandre01.inazuma_eleven.categories.Raimon;
import be.alexandre01.inazuma_eleven.roles.alius.Gazelle;
import be.alexandre01.inazuma_eleven.roles.alius.Torch;
import be.alexandre01.inazuma_eleven.roles.raimon.Axel;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.Tuple;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Shawn extends Role {
    public Shawn(IPreset preset) {
        super("Shawn Frost",preset);

        addDescription("§8- §7Votre objectif est de gagner avec §6§lRaimon");
        addDescription("§8- §7Vous possédez l’effet §6§lRésistance 1§7.");
        addDescription(" ");
        CustomComponentBuilder c = new CustomComponentBuilder("");
        c.append("§8- §7Vous possédez également le ");

        BaseComponent blizzardButton = new TextComponent("§3Blizzard Eternel §7*§8Curseur§7*");

        BaseComponent blizzardDesc = new TextComponent();
        blizzardDesc.addExtra("§e- §9Utilisation par §eEpisode\n");
        blizzardDesc.addExtra("§e- §9Donne au joueur §7ciblé, §8Slowness 2§9 pendant §a7 secondes");
        blizzardButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,blizzardDesc.getExtra().toArray(new BaseComponent[0])));
        c.append(blizzardButton);
        addDescription(c);
        addDescription(" ");
        addDescription("§8- §7Les attaques de §cTorch§7, §bGazelle§7 et §6Axel§7 ne vous atteignent pas.");

        setRoleToSpoil(Axel.class);
        setRoleToSpoil(Aiden.class);
        setRoleCategory(Raimon.class);
        onLoad(new load() {
            @Override
            public void a(Player player) {
                inazumaUHC.dm.addEffectPourcentage(player, DamageManager.EffectType.RESISTANCE,1,110);
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0,false,false), true);
            }

        });
    }
}
