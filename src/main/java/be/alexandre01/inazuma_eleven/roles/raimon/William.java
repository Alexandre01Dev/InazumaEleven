package be.alexandre01.inazuma_eleven.roles.raimon;

import be.alexandre01.inazuma.uhc.InazumaUHC;

import be.alexandre01.inazuma.uhc.custom_events.episode.EpisodeChangeEvent;
import be.alexandre01.inazuma.uhc.custom_events.roles.RoleItemUseEvent;
import be.alexandre01.inazuma.uhc.presets.IPreset;
import be.alexandre01.inazuma.uhc.presets.Preset;

import be.alexandre01.inazuma.uhc.roles.Role;
import be.alexandre01.inazuma.uhc.roles.RoleCategory;
import be.alexandre01.inazuma.uhc.roles.RoleItem;
import be.alexandre01.inazuma.uhc.utils.CustomComponentBuilder;
import be.alexandre01.inazuma.uhc.utils.ItemBuilder;
import be.alexandre01.inazuma_eleven.categories.Raimon;
import net.md_5.bungee.api.chat.BaseComponent;
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

import java.util.ArrayList;
import java.util.Collections;

public class William extends Role implements Listener {
    private final ArrayList<Role> usedRole = new ArrayList<>();
    private static William w = null;
    private int episode = 0;
    private boolean playerToSend = false;


    public William(IPreset preset) {
        super("William Glass",preset);
        setRoleCategory(Raimon.class);
        addDescription("§8- §7Votre objectif est de gagner avec §6§lRaimon");
        addDescription("§8- §7Vous disposez de §8§lFaiblesse 1§7.");
        addDescription(" ");
        CustomComponentBuilder c = new CustomComponentBuilder("");
        c.append("§8- §7Vous possédez également un paire de ");

        BaseComponent lunetteButton = new TextComponent("§b§lLunette");

        BaseComponent lunetteDesc = new TextComponent();
        lunetteDesc.addExtra("§e- §9Utilisation §6unique\n");
        lunetteDesc.addExtra("§e- §9Permet de renvoyer une technique utilisé sur vous si vous avez l'item dans votre hotbar\n");
        lunetteDesc.addExtra("§e- §9Une fois utlisé, elle se casse mais vous pouvez la réparer (/craft) §c⚠");
        lunetteButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,lunetteDesc.getExtra().toArray(new BaseComponent[0])));
        c.append(lunetteButton);
        addDescription(c);
        addDescription(" ");
        addDescription("§8- §7Lorsqu'un joueur utilise son pouvoir, vous recevrez un message disant quel personnage a utilisé son pouvoir.");
        addDescription(" ");
        addDescription("§8- §7Vous aurez un allié de confiance tous les §e2 épisodes.");
        addDescription(" ");

        William w = this;


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

        RoleItem lunetteItem = new RoleItem();
        ItemBuilder lunetteBuilder = new ItemBuilder(Material.PRISMARINE_SHARD).setName("§b§lLunette");
        lunetteBuilder.setLore("");

        lunetteItem.setItemstack(lunetteBuilder.toItemStack());
        addRoleItem(lunetteItem);

        RoleItem lunettecasséItem = new RoleItem();
        ItemBuilder lunettecasséBuilder = new ItemBuilder(Material.PRISMARINE_CRYSTALS).setName("§b§lLunette §c§lCassé");
        lunettecasséBuilder.setLore("");

        lunettecasséItem.setItemstack(lunettecasséBuilder.toItemStack());
        addRoleItem(lunettecasséItem);




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


        for (Role role : raimon) {
            for (Player player : role.getPlayers()) {
                if (!player.isOnline()) {
                    usedRole.add(role);
                }
            }
        }

        for(Role used : usedRole){
            raimon.remove(used);
        }

        if(raimon.isEmpty()){
            return;
        }
        Collections.shuffle(raimon);

        System.out.println(raimon.size());




        for(Player player : getPlayers()) {
            for (Role role : raimon) {
                if (role.getPlayers().isEmpty())
                {
                    continue;
                }

                for (Player target : role.getPlayers())
                {
                    player.sendMessage(Preset.instance.p.prefixName() + " §7Vous savez désormais que " + target.getName() + " fait partie de l' " + raimon.get(0).getRoleCategory().getPrefixColor() + raimon.get(0).getRoleCategory().getName());
                    usedRole.add(role);
                    raimon.remove(role);
                    return;
                }
            }

            if (!playerToSend){
                player.sendMessage(Preset.instance.p.prefixName() + " §7Vous connaissez tous vos mates pouvant être en §cvie§7.");
                playerToSend = true;
                return;
            }


        }
    }
}
