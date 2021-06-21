package be.alexandre01.inazuma_eleven.roles.alius;

import be.alexandre01.inazuma.uhc.InazumaUHC;
import be.alexandre01.inazuma.uhc.custom_events.player.PlayerInstantDeathEvent;
import be.alexandre01.inazuma.uhc.managers.damage.DamageManager;
import be.alexandre01.inazuma.uhc.presets.IPreset;
import be.alexandre01.inazuma.uhc.presets.Preset;


import be.alexandre01.inazuma.uhc.roles.Role;
import be.alexandre01.inazuma.uhc.roles.RoleItem;
import be.alexandre01.inazuma.uhc.utils.Episode;
import be.alexandre01.inazuma.uhc.utils.ItemBuilder;
import be.alexandre01.inazuma.uhc.utils.PlayerUtils;
import be.alexandre01.inazuma.uhc.utils.Tracker;
import be.alexandre01.inazuma_eleven.InazumaEleven;
import be.alexandre01.inazuma_eleven.categories.Alius;
import be.alexandre01.inazuma_eleven.categories.Raimon;
import be.alexandre01.inazuma_eleven.categories.Solo;
import be.alexandre01.inazuma_eleven.roles.raimon.Hurley;
import be.alexandre01.inazuma_eleven.roles.raimon.Jude;
import be.alexandre01.inazuma_eleven.roles.raimon.Mark;
import be.alexandre01.inazuma_eleven.roles.raimon.Shawn;
import be.alexandre01.inazuma_eleven.roles.solo.Byron;
import lombok.Setter;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_8_R3.Tuple;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Bellatrix extends Role implements Listener {
    private boolean xeneDead = false;
    public ArrayList<Player> list;
    private UUID uuid = null;
    private boolean revenge = false;
    private boolean hasChoose = false;
    private Inventory inventory;
    private int episode;
    private int i = 0;
    @Setter
    private Block block = null;
    @Setter
    Location location = null;
    public Bellatrix(IPreset preset) {
        super("Bellatrix",preset);

        addDescription("§8- §7Votre objectif est de gagner avec §5§ll'§5§lAcadémie §5§lAlius");
        addDescription("§8- §7Vous possédez l’effet §b§lSpeed 1§.");
        addDescription(" ");
        addDescription("§8- §7Vous disposez du §d§lCollier§7§l-§5§lAlius§7 qui vous donnera §6§lRésistance 1§7 (NERF) pendant §a1 minute 30§7.");
        addDescription(" ");
        addDescription("§8- §7Lors de la mort de §5Xavier§7, 2 choix s'offrent à vous, qui sont de le §aremplacer§7 ou §cnon§7.");
        addDescription("§8- §7Vous pouvez également faire §5/xene §aaccept§9 ou §5/xene §crefuse.");
        addDescription(" ");
        addDescription("§8- §7Si vous §aacceptez§7 de le remplacer, vous perdrez §c§l3 §4❤§7 permanent§7.");
        addDescription("§8- §7Alors, toute §5§ll'§5§lAcadémie §5§lAlius§7 vous connaîtra et inversement.");
        addDescription("§8- §7Cependant si vous §aacceptez§7, le capitaine de l'équipe §6§lRaimon§7 vous connaîtra.");
        addDescription(" ");
        addDescription("§8- §7Vous pouvez également §crefuser§7 cette demande.");
        addDescription("§8- §7Si vous §crefusez§7 de le remplacer, vous obtiendrez l’effet §4§lForce 1 et §c§l1 §4❤§7§7 permanent.");
        addDescription(" ");
        addDescription("§8- §7Une annonces sera faites comme quoi vous avez §crefusé§7 de le remplacer.");

        setRoleCategory(Alius.class);

        addListener(this);
        onLoad(new load() {
            @Override
            public void a(Player player) {
                    inazumaUHC.dm.addEffectPourcentage(player, DamageManager.EffectType.RESISTANCE,1,110);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0,false,false), true);
            }
        });
        RoleItem colierAllius = new RoleItem();
        colierAllius.setItemstack(new ItemBuilder(Material.NETHER_STAR).setName("§d§lCollier§7§l-§5§lAlius").toItemStack());
        colierAllius.deployVerificationsOnRightClick(colierAllius.generateVerification(new Tuple<>(RoleItem.VerificationType.EPISODES,1)));
        colierAllius.setRightClick(player -> {
            Jude.collierAlliusNotif(player.getLocation());
            player.sendMessage(Preset.instance.p.prefixName()+" Vous rentrez en résonance avec la §8§lpierre§7§l-§5§lalius.");
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 90*20, 0,false,false), true);
        });
        addRoleItem(colierAllius);

    addCommand("xene", new command() {
        @Override
        public void a(String[] args, Player player) {
            if(!xeneDead || hasChoose){
                player.sendMessage(Preset.instance.p.prefixName()+" Vous ne pouvez pas utiliser cette commande pour le moment");
                return;
            }

            if(args.length == 0){
                player.sendMessage(Preset.instance.p.prefixName()+" Veuillez mettre §a/xene accept §7ou §a/xene refuse");
                return;
            }

            if(args[0].equalsIgnoreCase("accept")){
                hasChoose = true;
                accept(player);
                return;
            }
            if (args[0].equalsIgnoreCase("refuse")) {
                hasChoose = true;
                refuse(player);
                return;
            }
            player.sendMessage(Preset.instance.p.prefixName()+" Veuillez mettre §a/xene accept §7ou §a/xene refuse");
        }
    });


}
    private void refuse(Player player){
        String message;
        player.setMaxHealth(player.getMaxHealth()+2);
        //+ 1 use de mété
        player.sendMessage(Preset.instance.p.prefixName()+" Vous avez refusé de remplacer Xavier, vous gagnez donc 1 coeur permanent et 1 utilisation de la météorite.");
        player.setMaxHealth(player.getMaxHealth()+2);
        revenge = true;

        message = Preset.instance.p.prefixName()+" §cBellatrix a §c§lrefusé de remplacer Xavier !";

        Bukkit.broadcastMessage(message);

    }
    private void accept(Player player){


        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 0,false,false), true);

        new BukkitRunnable() {

            @Override
            public void run() {

                if(PlayerUtils.getNearbyPlayersFromPlayer(player,20,20,20).size() >= 3) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*5, 0,false,false), true);
                }

            }
        }.runTaskTimerAsynchronously(inazumaUHC, 20*1, 20*1);


        List<Role> alius = new ArrayList<>(InazumaUHC.get.rm.getRoleCategory(Alius.class).getRoles());

        alius.remove(this);
        Collections.shuffle(alius);

        for (int j = 0; j < alius.size(); j++) {

            Role role = alius.get(i);
            if (role.getPlayers().isEmpty()){
                alius.remove(role);
                continue;
            }

        }

        alius = alius.subList(0,5);

        Collections.shuffle(alius);

        for (Role role: alius) {
            role.getPlayers();
        }


        player.sendMessage("Voici la liste de vos 6 mates : " + alius);

        for(Player byron : inazumaUHC.rm.getRole(Byron.class).getPlayers()){

            ArrayList<Player> players = new ArrayList<>(InazumaUHC.get.getRemainingPlayers());

        }


    }


    @EventHandler
    public void onDeath(PlayerInstantDeathEvent event){
        Player player = event.getPlayer();
        Player killer = event.getKiller();

        Role role = inazumaUHC.rm.getRole(player);

        //XAVIER DEATH ✝
        if(role.getClass() == Xavier.class){

            new BukkitRunnable(){
                @Override
                public void run(){

                    uuid = player.getUniqueId();
                    xeneDead = true;
                    for(Player players : getPlayers()){
                        BaseComponent b = new TextComponent( Preset.instance.p.prefixName()+ role.getRoleCategory().getPrefixColor()+role.getName()+"§7 vient de mourir.\n");
                        b.addExtra("§7Souhaitez vous le remplacer ?");
                        BaseComponent yes = new TextComponent("§a[OUI]");
                        yes.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/xene accept"));
                        b.addExtra(yes);
                        b.addExtra(" §7ou ");
                        BaseComponent no = new TextComponent("§a[NON]");
                        no.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/xene refuse"));

                        b.addExtra(no);

                        players.spigot().sendMessage(b);
                        new BukkitRunnable() {
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

            }.runTaskLater(InazumaUHC.get, 1);


        }
    }
}
