package be.alexandre01.inazuma_eleven.roles.raimon;

import be.alexandre01.inazuma.uhc.presets.IPreset;
import be.alexandre01.inazuma.uhc.presets.Preset;

import be.alexandre01.inazuma.uhc.roles.Role;
import be.alexandre01.inazuma.uhc.roles.RoleItem;
import be.alexandre01.inazuma.uhc.timers.utils.DateBuilderTimer;
import be.alexandre01.inazuma.uhc.timers.utils.MSToSec;
import be.alexandre01.inazuma.uhc.utils.CustomComponentBuilder;
import be.alexandre01.inazuma.uhc.utils.ItemBuilder;
import be.alexandre01.inazuma.uhc.utils.TitleUtils;
import be.alexandre01.inazuma_eleven.categories.Raimon;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.MobEffectList;
import net.minecraft.server.v1_8_R3.Tuple;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.potion.CraftPotionEffectType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Hurley extends Role implements Listener {

    public Hurley(IPreset preset) {
        super("Hurley Kane",preset);
        addListener(this);
        setRoleCategory(Raimon.class);

            addDescription("§8- §7Votre objectif est de gagner avec §6§lRaimon");
            addDescription("§8- §7Vous possédez l’effet §b§lSpeed 1 §7ainsi qu'un livre §3Depth Strider II§7.");
            addDescription(" ");
            CustomComponentBuilder c = new CustomComponentBuilder("");
            c.append("§8- §7Vous possédez également l' ");

            BaseComponent seaeffectButton = new TextComponent("§3§lAqua §3§lSea §7*§8Curseur§7*");

            BaseComponent seaeffectDesc = new TextComponent();
            seaeffectDesc.addExtra("§e- §9Utilisation 2 fois uniquement\n");
            seaeffectDesc.addExtra("§e- §9Permet de voir les §deffets§9 d'un joueur §9[§525 blocks§9]");
            seaeffectButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,seaeffectDesc.getExtra().toArray(new BaseComponent[0])));
            c.append(seaeffectButton);
            addDescription(c);
            addDescription(" ");
            addDescription("§8- §7Toutes les attaques de §4feu§7 ne vous atteignent pas.");

        onLoad(new load() {
            @Override
            public void a(Player player) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0,false,false), true);
            }
        });

        RoleItem depthItem = new RoleItem();
        ItemBuilder depthBuilder = new ItemBuilder(Material.ENCHANTED_BOOK);
        depthBuilder.addEnchant(Enchantment.DEPTH_STRIDER,2);
        depthBuilder.setName("§eEnchanted Book");
        depthItem.setItemstack(depthBuilder.toItemStack());
        depthItem.setPlaceableItem(true);

        RoleItem roleItem = new RoleItem();
        ItemBuilder itemBuilder = new ItemBuilder(Material.BUCKET).setName("§7§lSceau §7§lDe §c§lVie");

        roleItem.setItemstack(itemBuilder.toItemStack());
        addRoleItem(roleItem);

            addCommand("ina sea", new command() {
                public int i = 0;

                @Override
                public void a(String[] args, Player player) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if(target == null){
                        player.sendMessage(Preset.instance.p.prefixName()+" Le joueur n'est pas en game.");
                        return;
                    }
                    if(target == player){
                        player.sendMessage(Preset.instance.p.prefixName()+" Vous essayez de voir vos effets mais en vain. Vous êtes (un peu) chelou.");
                        return;
                    }

                    if(i > 2){
                        player.sendMessage(Preset.instance.p.prefixName()+"§c Vous avez dépassé le nombre d'utilisation de cette commande");
                        return;
                    }
                    player.sendMessage(Preset.instance.p.prefixName()+" Voici les effets de §e"+target.getName()+"§7:");
                    for(PotionEffect potionEffect : target.getActivePotionEffects()){
                        MobEffectList m = ((CraftPotionEffectType)potionEffect.getType()).getHandle();
                        String e = m.a().replace("potion.","");
                        String firstLetter = e.substring(0,1).toUpperCase();
                        String afterLetter = e.substring(1);
                        StringBuilder sb = new StringBuilder();
                        for (int j = 0; j < afterLetter.length(); j++) {
                            char ch = afterLetter.charAt(j);
                            if(Character.isUpperCase(ch)){
                                sb.append(" "+ Character.toLowerCase(ch));
                                continue;
                            }
                            sb.append(ch);

                        }

                        player.sendMessage("§e-§9 "+  firstLetter+sb.toString());
                    }
                    i++;
                    player.sendMessage(Preset.instance.p.prefixName()+" §cAttention§7, celui-ci sera prévenu dans 1 minute et 30 secondes que votre rôle a regardé ses effets.");
                    Bukkit.getScheduler().runTaskLaterAsynchronously(inazumaUHC, new BukkitRunnable() {
                        @Override
                        public void run() {
                            target.sendMessage(Preset.instance.p.prefixName() +" Vous venez d'apprendre qu'"+getRoleCategory().getPrefixColor()+getName()+"§7 connait désormais vos effets.");
                        }
                    }, 20 * 90);

                }
            });
        addRoleItem(depthItem);
    }
}
