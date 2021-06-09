package be.alexandre01.inazuma_eleven.roles.alius;

import be.alexandre01.inazuma.uhc.InazumaUHC;
import be.alexandre01.inazuma.uhc.presets.IPreset;
import be.alexandre01.inazuma.uhc.presets.Preset;
import be.alexandre01.inazuma.uhc.roles.Role;
import be.alexandre01.inazuma.uhc.utils.PatchedEntity;
import be.alexandre01.inazuma_eleven.categories.Alius;
import be.alexandre01.inazuma_eleven.objects.LocalRaimon;
import be.alexandre01.inazuma_eleven.roles.solo.Byron;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class Nero extends Role implements Listener {

    private LocalRaimon local;
    private Boolean hadKilled = false;
    private Boolean hadCommand = false;

    public Nero(IPreset preset)
    {
        super("Nero", preset);
        addListener(this);
        setRoleCategory(Alius.class);

        addCommand("local", new command() {



            @Override
            public void a(String[] strings, Player player) {
                if(!hadCommand)
                {
                    hadCommand = true;
                    if(strings[0].equalsIgnoreCase("accept"))
                    {
                        if(local != null)
                        {
                            player.sendMessage("Le local de Raimon se trouve en X: " + local.x + " | Z: " + local.z);
                            PatchedEntity.setMaxHealthInSilent(player, player.getMaxHealth() - 4);
                            for(Player byron : inazumaUHC.rm.getRole(Byron.class).getPlayers())
                            {
                                byron.sendMessage("Nero a décidé de recevoir les coordonnées du local de raimon. Par conséquent vous allez vous aussi les recevoir");
                                byron.sendMessage("Le local de Raimon se trouve en X: " + local.x + " | Z: " + local.z);
                            }
                        }

                    }
                    if(strings[0].equalsIgnoreCase("refuse"))
                    {
                        PatchedEntity.setMaxHealthInSilent(player, player.getMaxHealth() + 4);
                    }
                }
            }
        });


    }

    public void MessageLocal(LocalRaimon localRaimon)
    {
        if(!hadKilled)
        {
            local = localRaimon;
            hadKilled = true;
            BaseComponent b = new TextComponent(Preset.instance.p.prefixName()+" Voulez-vous recevoir les coos du local ?");
            BaseComponent yes = new TextComponent("§a[OUI]");
            yes.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/local accept"));
            b.addExtra(yes);
            b.addExtra(" §7ou ");
            BaseComponent no = new TextComponent("§a[NON]");
            no.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/local refuse"));

            b.addExtra(no);

            for(Player player : getPlayers()){
                player.spigot().sendMessage(b);
            }
        }

    }

}
