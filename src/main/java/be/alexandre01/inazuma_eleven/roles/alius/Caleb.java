package be.alexandre01.inazuma_eleven.roles.alius;


import be.alexandre01.inazuma.uhc.InazumaUHC;
import be.alexandre01.inazuma.uhc.custom_events.episode.EpisodeChangeEvent;
import be.alexandre01.inazuma.uhc.presets.IPreset;
import be.alexandre01.inazuma.uhc.presets.Preset;

import be.alexandre01.inazuma.uhc.roles.Role;
import be.alexandre01.inazuma.uhc.roles.RoleItem;
import be.alexandre01.inazuma.uhc.utils.ItemBuilder;
import be.alexandre01.inazuma.uhc.utils.PatchedEntity;
import be.alexandre01.inazuma.uhc.utils.ScoreboardUtil;
import be.alexandre01.inazuma_eleven.categories.Alius;
import be.alexandre01.inazuma_eleven.objects.Capitaine;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.Tuple;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Caleb extends Role implements Listener {
    private boolean hasChoose = false;
    private Player lastPlayer = null;
    private HashMap<Player,Integer> damages = new HashMap<>();
    private ArrayList<Player> usedRole = new ArrayList<>();
    private Scoreboard score = null;

    boolean b = false;
    BukkitTask s = null;
    public Caleb(IPreset preset) {
        super("Caleb Stonewall",preset);

        /*addDescription("§8- §7Votre objectif est de gagner avec §5§ll'§5§lAcadémie §5§lAlius");
        addDescription(" ");
        addDescription("§8- §7A chaque début d'§eEpisode§7, un choix s'offre à vous :");
        addDescription("§8- §7Faire perdre ou non 2 coeurs permanents durant §e1 Epsiode§7 à un mate random.");
        addDescription("§8- §7Afin d'obtenir §4§lForce 1 et §c§l2 §4❤§7 permanent pendant l'Episode.");
        addDescription("§c⚠§7 Vous avez §a5 minutes§7 à chaque début d'§eEpisode§7 pour faire votre choix.");
        addDescription("§8- §c§l⚠ §7Vous pouvez également faire §5/power §aaccept§7 ou §5/power §crefuse§7.");*/
        addDescription("https://app.gitbook.com/@inazumauhcpro/s/inazuma-gitbook/inazuma-eleven-uhc/roles/alius/caleb");

        setRoleCategory(Alius.class);
        Class<?> clazz = Capitaine.giveCapitaine(this.getClass());
        if(clazz != null)
            setRoleToSpoil(clazz);

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
                        }.runTaskLaterAsynchronously(inazumaUHC,20*60*5);
                    }
                },20*3);
            }
        });

        addListener(this);


        RoleItem roleItem = new RoleItem();
        ItemBuilder it = new ItemBuilder(Material.QUARTZ, 2).setName("§5§lPierre §lAlius");
        roleItem.setItemstack(it.toItemStack());
        for(Class<?> role : getClassRoles()){
            roleItem.getRolesToAccess().add(role);
        }

        roleItem.setDroppableItem(true);
        ArrayList<RoleItem.VerificationGeneration> verificationGenerations = new ArrayList<>();
        verificationGenerations.add(player -> {
            if(inazumaUHC.rm.getRole(player) instanceof Caleb){
                return false;
            }
            //TOUT LE MONDE
            return true;
        });
        ArrayList<Player> players = new ArrayList<>();
        verificationGenerations.add(player -> {
            if(!(inazumaUHC.rm.getRole(player)instanceof Joseph) && !(inazumaUHC.rm.getRole(player)instanceof David))
            {
                players.add(player);
                return true;
            }
            if(players.contains(player)){
                return false;
            }

            return true;
        });
        roleItem.deployVerificationsOnRightClick(verificationGenerations);
        roleItem.setRightClick(player ->
        {
            if(player.getItemInHand().getAmount() == 2)
            {
                player.getItemInHand().setAmount(1);
            }

            else if(player.getItemInHand().getAmount() == 1)
            {
                player.setItemInHand(new ItemStack(Material.AIR));
            }


            if(inazumaUHC.rm.getRole(player) instanceof David){
                David david = (David) inazumaUHC.rm.getRole(player);
                if(!david.useFirstPierre)
                {
                    if (player.hasPotionEffect(PotionEffectType.WEAKNESS))
                        player.removePotionEffect(PotionEffectType.WEAKNESS);
                    if(david.firstUse)
                        PatchedEntity.setMaxHealthInSilent(player, player.getMaxHealth() + 4);

                    david.useFirstPierre = true;
                    david.secondUse = false;
                }
                else
                {
                    PatchedEntity.setMaxHealthInSilent(player, player.getMaxHealth() + 2);
                    players.add(player);
                }

                return;
            }
            if(inazumaUHC.rm.getRole(player) instanceof Joseph){
                Joseph joseph = (Joseph) inazumaUHC.rm.getRole(player);
                if(!joseph.useFirstPierre)
                {
                    if (player.hasPotionEffect(PotionEffectType.WEAKNESS))
                        player.removePotionEffect(PotionEffectType.WEAKNESS);
                    if(joseph.firstUse)
                        PatchedEntity.setMaxHealthInSilent(player, player.getMaxHealth() + 2);

                    joseph.useFirstPierre = true;
                    joseph.secondUse = false;
                }
                else
                {
                    PatchedEntity.setMaxHealthInSilent(player, player.getMaxHealth() + 2);
                    players.add(player);
                }


                return;
            }

            PatchedEntity.setMaxHealthInSilent(player, player.getMaxHealth() + 2);
            player.sendMessage(Preset.instance.p.prefixName()+" Vous avez mentis à §5§lCaleb§7, vous avez donc gagné §c§l1 §4❤§7 permanent mais il sera tenu informé dans §a1 minute§7.");

            new BukkitRunnable() {
                @Override
                public void run() {
                    for(Player p : inazumaUHC.rm.getRole(Caleb.class).getPlayers()){
                        p.sendMessage(Preset.instance.p.prefixName() + "§c§l"+ player.getName() + "§7 vous a menti et a donc §agagné§7 §c§l1 §4❤§7 permanent.");
                    }
                }
            }.runTaskLaterAsynchronously(inazumaUHC,20*60);
            //TOUT LE MONDE
        });
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

                    while (choosedPlayer == null || getPlayers().contains(choosedPlayer)){
                        ArrayList<Role> c = new ArrayList<>(inazumaUHC.rm.getRoleCategory(Alius.class).getRoles());
                        c.removeIf(r -> r instanceof Caleb);
                        Collections.shuffle(c);

                        ArrayList<Player> p = new ArrayList<>();

                        if(c.isEmpty()){
                            break;
                        }

                        for(Role r : c){
                            System.out.println(r.getName());
                            for(Player player1 : r.getPlayers())
                            {
                                if(player1.isOnline())
                                    p.add(player1);
                            }
                        }
                        if(lastPlayer != null)
                            p.remove(lastPlayer);

                        if(p.isEmpty())
                        {
                            player.sendMessage("Tu as enlevés des coeurs a tout les joueurs de ton équipe.");
                            lastPlayer = null;
                            return;
                        }

                        Collections.shuffle(p);


                        if(p.get(0) != null){
                            choosedPlayer = p.get(0);
                            lastPlayer = choosedPlayer;
                            usedRole.add(choosedPlayer);
                            PatchedEntity.setMaxHealthInSilent(choosedPlayer,choosedPlayer.getMaxHealth()-4);
                            damages.put(choosedPlayer,4);
                            choosedPlayer.sendMessage(Preset.instance.p.prefixName()+" §5Caleb §7t'a enlevé 2 coeurs permanent durant cet épisode.");
                            player.sendMessage(Preset.instance.p.prefixName()+" §7tu as enlevé 2 coeurs a un membre de ton équipe.");
                            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0,false,false), true);
                            PatchedEntity.setMaxHealthInSilent(player,player.getMaxHealth()+2);
                            damages.put(player,-2);

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
            }.runTaskLaterAsynchronously(inazumaUHC,20*60*5);
        }
        }
}
