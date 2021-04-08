package be.alexandre01.inazuma_eleven.roles.raimon;

import be.alexandre01.inazuma.uhc.custom_events.player.PlayerInstantDeathEvent;
import be.alexandre01.inazuma.uhc.managers.damage.DamageManager;
import be.alexandre01.inazuma.uhc.presets.IPreset;

import be.alexandre01.inazuma.uhc.presets.Preset;
import be.alexandre01.inazuma.uhc.roles.Role;
import be.alexandre01.inazuma.uhc.roles.RoleItem;
import be.alexandre01.inazuma.uhc.utils.CustomComponentBuilder;
import be.alexandre01.inazuma.uhc.utils.ItemBuilder;
import be.alexandre01.inazuma_eleven.categories.Raimon;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.Tuple;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Axel extends Role implements Listener {
    public Axel(IPreset preset) {
        super("Axel Blaze",preset);
        setRoleCategory(Raimon.class);
        setRoleToSpoil(Mark.class);

        addListener(this);
        addDescription("§8- §7Votre objectif est de gagner avec §6§lRaimon");
        addDescription("§8- §7Vous possédez l’effet §6§l§4§lForce 1 §7ainsi que §6§lFire Résistance§7.");
        addDescription(" ");
        CustomComponentBuilder c = new CustomComponentBuilder("");
        c.append("§8- §7Vous possédez également la ");

        BaseComponent tornadeButton = new TextComponent("§4§lTornade §c§lDe §4§lFeu§7 §7*§8Curseur§7*");

        BaseComponent tornadeDesc = new TextComponent();
        tornadeDesc.addExtra("§e- §9Utilisation §6illimité §7[Cooldown de §a10 minutes§7]\n");
        tornadeDesc.addExtra("§e- §9Donne §b§lSpeed 2§7 pendant §a1 minute 30");
        tornadeButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,tornadeDesc.getExtra().toArray(new BaseComponent[0])));
        c.append(tornadeButton);
        addDescription(c);
        addDescription(" ");
        addDescription("§8- §7A chaque §4§lkill§7, vous gagnerez §e1 gapple §7supplémentaire.");
        addDescription(" ");
        addDescription("§8- §7Les attaques de §cTorch§7, §bGazelle§7 et §6Shawn§7 ne vous atteignent pas.");

        onLoad(new load() {
            @Override
            public void a(Player player) {
                inazumaUHC.dm.addEffectPourcentage(player, DamageManager.EffectType.INCREASE_DAMAGE,1,110);
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0,false,false), true);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0,false,false), true);
            }
        });
        RoleItem roleItem = new RoleItem();
        ItemBuilder itemBuilder = new ItemBuilder(Material.BLAZE_ROD).setName("§4§lTornade §c§lDe §4§lFeu");
        roleItem.setItemstack(itemBuilder.toItemStack());

        roleItem.deployVerificationsOnRightClick(roleItem.generateVerification(new Tuple<>(RoleItem.VerificationType.COOLDOWN,60*10)));

        roleItem.setRightClick(player -> {
            player.sendMessage(Preset.instance.p.prefixName()+" Vous venez d'utiliser la §4§lTornade §c§lDe §4§lFeu§7.");
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 90*20, 1,false,false), true);
        });
        addRoleItem(roleItem);
    }

    @EventHandler
    public void onKillEvent(PlayerInstantDeathEvent event){
        Player killer = event.getKiller();
        if(killer != null){
            if (inazumaUHC.rm.getRole(killer) == this){
                killer.sendMessage(Preset.instance.p.prefixName()+" Tu reçois une pomme en or");
                killer.getInventory().addItem(new ItemStack(Material.GOLDEN_APPLE));
                killer.updateInventory();
            }
        }
    }
}
