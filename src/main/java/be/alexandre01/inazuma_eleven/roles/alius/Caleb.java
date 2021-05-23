package be.alexandre01.inazuma_eleven.roles.alius;


import be.alexandre01.inazuma.uhc.custom_events.episode.EpisodeChangeEvent;
import be.alexandre01.inazuma.uhc.presets.IPreset;
import be.alexandre01.inazuma.uhc.presets.Preset;

import be.alexandre01.inazuma.uhc.roles.Role;
import be.alexandre01.inazuma.uhc.roles.RoleCategory;
import be.alexandre01.inazuma.uhc.roles.RoleItem;
import be.alexandre01.inazuma.uhc.utils.ItemBuilder;
import be.alexandre01.inazuma.uhc.utils.PatchedEntity;
import be.alexandre01.inazuma_eleven.categories.Alius;
import be.alexandre01.inazuma_eleven.listeners.PierreAliusEvent;
import be.alexandre01.inazuma_eleven.roles.raimon.Aiden;
import be.alexandre01.inazuma_eleven.roles.raimon.Mark;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Caleb extends Role implements Listener {
    private boolean hasChoose = false;
    private Player lastPlayer = null;
    private HashMap<Player,Integer> damages = new HashMap<>();

    boolean b = false;
    BukkitTask s = null;
    public Caleb(IPreset preset) {
        super("Caleb Stonewall",preset);

        addDescription("§8- §7Votre objectif est de gagner avec §5§ll'§5§lAcadémie §5§lAlius");
        addDescription(" ");
        addDescription("§8- §7A chaque début d'§eEpisode§7, un choix s'offre à vous :");
        addDescription("§8- §7Faire perdre ou non 2 coeurs permanents durant §e1 Epsiode§7 à un mate random.");
        addDescription("§8- §7Afin d'obtenir §4§lForce 1 et §c§l2 §4❤§7§7 permanent pendant l'Episode.");
        addDescription("§c⚠§7 Vous avez §a5 minutes§7 à chaque début d'§eEpisode§7 pour faire votre choix.");
        addDescription("§8- §c§l⚠ §7Vous pouvez également faire §5/power §aaccept§7 ou §5/power §crefuse§7.");

        setRoleCategory(Alius.class);
        onLoad(new load() {
            @Override
            public void a(Player player) {
                Bukkit.getScheduler().runTaskLater(inazumaUHC, new Runnable() {
                    @Override
                    public void run() {

                        if(s != null){
                            s.cancel();
                            s = null;
                        }
                        BaseComponent b = new TextComponent(Preset.instance.p.prefixName()+" Voulez-vous recevoir votre force ?");
                        BaseComponent yes = new TextComponent("§a[OUI]");
                        yes.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/power accept"));
                        b.addExtra(yes);
                        b.addExtra(" §7ou ");
                        BaseComponent no = new TextComponent("§a[NON]");
                        no.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/power refuse"));

                        b.addExtra(no);

                        for(Player player : getPlayers()){
                            player.spigot().sendMessage(b);
                        }

                        player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                        for(Player d : damages.keySet()){
                            PatchedEntity.setMaxHealthInSilent(d,d.getMaxHealth()+damages.get(d));
                        }

                        damages.clear();
                        hasChoose = false;


                        s = new BukkitRunnable() {
                            @Override
                            public void run() {
                                if(!hasChoose){
                                    hasChoose = true;
                                    refuse(player);
                                }


                            }
                        }.runTaskLaterAsynchronously(inazumaUHC,20*60);
                    }
                },20*3);
            }
        });

        addListener(this);
        addListener(new PierreAliusEvent(inazumaUHC));

        RoleItem roleItem = new RoleItem();
        ItemBuilder it = new ItemBuilder(Material.QUARTZ, 2).setName("§5§lPierre §lAlius");
        roleItem.setItemstack(it.toItemStack());
        addRoleItem(roleItem);

                addCommand("power", new command() {
                    @Override
                    public void a(String[] args, Player player) {
                        if(hasChoose){
                            player.sendMessage(Preset.instance.p.prefixName()+" Vous ne pouvez pas utiliser cette commande pour le moment");
                            return;
                        }

                        if(args.length == 0){
                            player.sendMessage(Preset.instance.p.prefixName()+" Veuillez mettre §a/power §aaccept §7ou §a/power §crefuse");
                            return;
                        }

                        if(args[0].equalsIgnoreCase("accept")){



                            hasChoose = true;

                            Player choosedPlayer = null;

                            while (choosedPlayer == null || getPlayers().contains(choosedPlayer) || choosedPlayer == lastPlayer){
                                ArrayList<Role> c = new ArrayList<>(inazumaUHC.rm.getRoleCategory(Alius.class).getRoles());
                                c.removeIf(r -> r instanceof Caleb);
                                Collections.shuffle(c);

                                for(Role r : c){
                                    System.out.println(r.getName());
                                }
                                if(c.isEmpty()){
                                    break;
                                }
                                Role role = c.get(c.size()-1);
                                if(role == null)
                                    continue;
                                ArrayList<Player> p = new ArrayList<>(role.getPlayers());

                                Collections.shuffle(p);


                                if(p.get(0) != null){
                                    choosedPlayer = p.get(0);

                                    PatchedEntity.setMaxHealthInSilent(choosedPlayer,choosedPlayer.getMaxHealth()-4);
                                    damages.put(choosedPlayer,4);
                                    choosedPlayer.sendMessage(Preset.instance.p.prefixName()+" §5Caleb §7t'a enlevé 2 coeurs permanent durant cet épisode.");
                                    player.sendMessage(Preset.instance.p.prefixName()+" §7tu as enlevé 2 coeurs a un membre de ton équipe.");
                                    player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0,false,false), true);
                                    PatchedEntity.setMaxHealthInSilent(player,player.getMaxHealth()+4);
                                    damages.put(player,-4);

                                }
                            }


                            return;


                        }




                        if (args[0].equalsIgnoreCase("refuse")) {


                            player.sendMessage(Preset.instance.p.prefixName()+" Vous avez refusé d'avoir force cette Episode.");

                            hasChoose = true;
                            refuse(player);
                            return;
                        }



                        player.sendMessage(Preset.instance.p.prefixName()+" Veuillez mettre §a/power §aaccept §7ou §a/power §crefuse");
                    }
                });

    }


    private void refuse(Player player){

    }



    private boolean canDistribute(){
        ArrayList<Player> r = new ArrayList<>();
        for(Player p : inazumaUHC.getRemainingPlayers()){
            Role role = inazumaUHC.rm.getRole(p);
            if(role.getRoleCategory().getClass().equals(Alius.class) && role != Caleb.this ){
                r.add(p);
            }
        }

        if(r.size() > 1){
            return true;
        }
        return false;
    }


    @EventHandler
    public void onEpisode(EpisodeChangeEvent event){

        if(s != null){
            s.cancel();
            s = null;
        }
        BaseComponent b = new TextComponent(Preset.instance.p.prefixName()+" Voulez-vous recevoir votre force ?");
        BaseComponent yes = new TextComponent("§a[OUI]");
        yes.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/power accept"));
        b.addExtra(yes);
        b.addExtra(" §7ou ");
        BaseComponent no = new TextComponent("§a[NON]");
        no.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/power refuse"));

        b.addExtra(no);

        for(Player player : getPlayers()){
            player.spigot().sendMessage(b);
        }

        for(Player player : getPlayers()){
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            for(Player d : damages.keySet()){
                PatchedEntity.setMaxHealthInSilent(d,d.getMaxHealth()+damages.get(d));
            }

            damages.clear();
            hasChoose = false;


            s = new BukkitRunnable() {
                @Override
                public void run() {
                    if(!hasChoose){
                        hasChoose = true;
                        refuse(player);
                    }


                }
            }.runTaskLaterAsynchronously(inazumaUHC,20*60);
        }
        }
}
