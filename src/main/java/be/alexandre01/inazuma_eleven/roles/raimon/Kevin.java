package be.alexandre01.inazuma_eleven.roles.raimon;

import be.alexandre01.inazuma.uhc.InazumaUHC;
import be.alexandre01.inazuma.uhc.presets.IPreset;
import be.alexandre01.inazuma.uhc.presets.Preset;

import be.alexandre01.inazuma.uhc.roles.Role;
import be.alexandre01.inazuma.uhc.timers.utils.DateBuilderTimer;
import be.alexandre01.inazuma.uhc.timers.utils.MSToSec;
import be.alexandre01.inazuma.uhc.utils.CustomComponentBuilder;
import be.alexandre01.inazuma.uhc.utils.TitleUtils;
import be.alexandre01.inazuma_eleven.categories.Raimon;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Kevin extends Role {
    public Kevin(IPreset preset) {
        super("Kevin Dragonfly",preset);
        setRoleCategory(Raimon.class);
        addDescription("https://blog.inazumauhc.fr/inazuma-eleven-uhc/roles/raimon/kevin-dragonfly");
        /*addDescription("§8- §7Votre objectif est de gagner avec §6§lRaimon");
        CustomComponentBuilder c = new CustomComponentBuilder("");
        c.append("§8- §7Vous avez une commande nommée ");

        BaseComponent intimidateButton = new TextComponent("§5/intimidate §7(§9Pseudo§7) §7*§8Curseur§7*");

        BaseComponent intimidateDesc = new TextComponent();
        intimidateDesc.addExtra("§e- §9Utilisation 3 fois uniquement §7[Cooldown par de §a45 secondes§7]\n");
        intimidateDesc.addExtra("§e- §9Donne au joueur §7ciblé, §8Faiblesse§9 et §8Slowness 1§7 pendant §a20 secondes");
        intimidateButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,intimidateDesc.getExtra().toArray(new BaseComponent[0])));
        c.append(intimidateButton);
        addDescription(c);*/

        addCommand("intimidate", new command() {
        public int i = 0;
        public DateBuilderTimer dateBuilderTimer = new DateBuilderTimer(0);

            @Override
            public void a(String[] args, Player player) {
                dateBuilderTimer.loadComplexDate();

                if(dateBuilderTimer.getDate().getTime() > 0){
                    player.sendMessage(Preset.instance.p.prefixName()+"§c Tu dois attendre "+ dateBuilderTimer.getLongBuild());
                    return;
                }

                if(args.length == 0){
                    player.sendMessage(Preset.instance.p.prefixName()+" Merci de précisez le nom du joueur.");
                    return;
                }

                Player target = Bukkit.getPlayer(args[0]);
                if(target == null){
                    player.sendMessage(Preset.instance.p.prefixName()+" Le joueur n'est pas en game.");
                    return;
                }
                if(target == player){
                    player.sendMessage(Preset.instance.p.prefixName()+" Vous essayez de vous intimider vous même mais en vain. Vous êtes (un peu) chelou.");
                    return;
                }

                if(i > 3){
                    player.sendMessage(Preset.instance.p.prefixName()+"§c Vous avez dépassé le nombre d'utilisation de cette commande");
                    return;
                }

                if(InazumaUHC.get.rm.getRole(target) instanceof William){
                    Player near = William.williamLunette(player,target);
                    if(near != null){
                        target = near;
                    }
                }
                target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,20,0,false,false), true);
                TitleUtils.sendActionBar(target, Preset.instance.p.prefixName()+" Tu as été intimidé par Kevin!");
                target.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 30*20, 0,false,false), true);
                target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10*20, 1,false,false), true);
                player.sendMessage(Preset.instance.p.prefixName()+"Vous avez intimidé "+target.getName()+".");
                target.sendMessage(Preset.instance.p.prefixName()+"Kevin vous a intimidé.");
                dateBuilderTimer = new DateBuilderTimer(MSToSec.toMili(45));
                i++;
            }
        });
    }
}
