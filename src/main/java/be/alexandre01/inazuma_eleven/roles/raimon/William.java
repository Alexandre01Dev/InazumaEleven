package be.alexandre01.inazuma_eleven.roles.raimon;

import be.alexandre01.inazuma.uhc.InazumaUHC;

import be.alexandre01.inazuma.uhc.custom_events.episode.EpisodeChangeEvent;
import be.alexandre01.inazuma.uhc.custom_events.roles.RoleItemUseEvent;
import be.alexandre01.inazuma.uhc.presets.IPreset;
import be.alexandre01.inazuma.uhc.presets.Preset;

import be.alexandre01.inazuma.uhc.roles.Role;
import be.alexandre01.inazuma.uhc.roles.RoleCategory;
import be.alexandre01.inazuma.uhc.roles.RoleItem;
import be.alexandre01.inazuma.uhc.utils.ItemBuilder;
import be.alexandre01.inazuma_eleven.categories.Raimon;
import net.minecraft.server.v1_8_R3.Tuple;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collections;

public class William extends Role implements Listener {
    private final ArrayList<Role> usedRole = new ArrayList<>();
    private static William w = null;
    private int episode = 0;
    private boolean playerToSend = false;


    public William(IPreset preset) {
        super("William Glass",preset);
        addDescription("§8- §7Votre objectif est de gagner avec §6§lRaimon");
        addDescription("§8- §7Vous disposez de §8§lFaiblesse 1§7.");
        addDescription(" ");
        addDescription("§8- §7Lorsqu'un joueur utilise son pouvoir, vous recevrez un message disant quel personnage a utilisé son pouvoir.");
        addDescription(" ");
        addDescription("§8- §7Vous aurez un allié de confiance tous les §e2 épisodes.");

        William w = this;
        setRoleCategory(Raimon.class);


        onLoad(new load() {
            @Override
            public void a(Player player) {
                William.w = w;


                Bukkit.getScheduler().runTaskLater(inazumaUHC, () -> {
                    episodeChanged();
                },20);

                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 0,false,false), true);
            }

        });
        addListener(this);

        RoleItem roleItem = new RoleItem();
        ItemBuilder itemBuilder = new ItemBuilder(Material.GLASS_BOTTLE).setName("§b§lLunette");

        roleItem.setItemstack(itemBuilder.toItemStack());
        addRoleItem(roleItem);

    }


    @EventHandler
    public void onItemUse(RoleItemUseEvent event){
        Player player = event.getPlayer();

        RoleCategory roleCat  = InazumaUHC.get.rm.getRole(player).getRoleCategory();

        for(Player william : InazumaUHC.get.rm.getRole(William.class).getPlayers()){
            william.sendMessage(Preset.instance.p.prefixName()+"Le pouvoir "+event.getRoleItem().getItemStack().getItemMeta().getDisplayName()+"§7 a été utilisé");
        }
    }

    @EventHandler
    public void onEpisodeChangeEvent(EpisodeChangeEvent event){
        episode++;
        if(episode %2 == 0 && episode != 0){
            episodeChanged();
        }
    }
    private void episodeChanged(){
        ArrayList<Role> raimon = new ArrayList<>(InazumaUHC.get.rm.getRoleCategory(Raimon.class).getRoles());

        raimon.remove(this);
        for(Role used : usedRole){
            raimon.remove(used);
        }



        for (int i = 0; i < raimon.size(); i++)
        {
            Role role = raimon.get(i);
            boolean save = false;
            for(Player player : role.getPlayers())
            {
                if(player.isOnline())
                {
                    save = true;
                    break;
                }
            }
            if(!save)
            {
                raimon.remove(role);
            }
        }

        Collections.shuffle(raimon);

        System.out.println(raimon.size());

        if(raimon.isEmpty()){
            Bukkit.broadcastMessage("HOUIT");
            for (Player player : getPlayers()) {
                Bukkit.broadcastMessage("9");
                player.sendMessage(Preset.instance.p.prefixName() + " §7Vous connaissez tous vos mates pouvant être en §cvie§7.");
                playerToSend = true;
                return;
            }
        }


        if(!playerToSend){
            Bukkit.broadcastMessage("5");
            for(Player player : getPlayers()) {
                Bukkit.broadcastMessage("6");
                for (Role role : raimon) {
                    if (role.getPlayers().isEmpty())
                    {
                        Bukkit.broadcastMessage(raimon.toString());
                    }


                    int i = 0;
                    for (Player target : role.getPlayers())
                    {
                        i++;
                        if(i == 1)
                        {
                            Bukkit.broadcastMessage("7");
                            player.sendMessage(Preset.instance.p.prefixName() + " §7Vous savez désormais que " + target.getName() + " fait partie de l' " + raimon.get(0).getRoleCategory().getPrefixColor() + raimon.get(0).getRoleCategory().getName());
                            usedRole.add(raimon.get(0));
                        }
                    }
                }
            }
        }
    }
}
