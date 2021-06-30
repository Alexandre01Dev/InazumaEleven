package be.alexandre01.inazuma_eleven.roles.raimon;

import be.alexandre01.inazuma.uhc.InazumaUHC;
import be.alexandre01.inazuma.uhc.presets.IPreset;
import be.alexandre01.inazuma.uhc.presets.Preset;

import be.alexandre01.inazuma.uhc.roles.Role;
import be.alexandre01.inazuma.uhc.roles.RoleItem;
import be.alexandre01.inazuma.uhc.utils.ItemBuilder;
import be.alexandre01.inazuma.uhc.utils.LocationUtils;
import be.alexandre01.inazuma.uhc.utils.PlayerUtils;
import be.alexandre01.inazuma.uhc.utils.TitleUtils;
import be.alexandre01.inazuma.uhc.worlds.utils.Cuboid;
import be.alexandre01.inazuma_eleven.categories.Raimon;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;
import org.inventivetalent.packetlistener.PacketListenerAPI;
import org.inventivetalent.packetlistener.handler.PacketHandler;
import org.inventivetalent.packetlistener.handler.ReceivedPacket;
import org.inventivetalent.packetlistener.handler.SentPacket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Jack extends Role implements Listener {
    private BukkitTask b;
    private boolean isSneakTimer = false;
    private boolean invisible = false;
    private PacketHandler packetHandler = null;
    private boolean register;
    public Jack(IPreset preset) {
        super("Jack Wallside",preset);
        addDescription("§8- §7Votre objectif est de gagner avec §6§lRaimon");
        addDescription("§8- §7Vous disposez de §c§l3 §4❤§7 permanents supplémentaires.");
        addDescription(" ");
        addDescription("§8- §7Lorsque vous vous trouvez proche d'un joueur ayant activé son collier-alius dans un rayon de 20 blocks, étant très peureux vous aurez §b§lSpeed 1§7 pendant 1 minute.");
        addDescription(" ");
        addDescription("§8- §7Si vous restez accroupi, au bout de 10 secondes vous deviendrez invisible sauf si un joueur se trouve dans un rayon de 20 blocks durant les 10 secondes.");
        addDescription("§8- §7Il vous suffira de vous desneak pour redevenir visible (Votre armure sera invisible ainsi que vos items dans votre main.");

        setRoleCategory(Raimon.class);
        onLoad(new load() {
            @Override
            public void a(Player player) {
                        player.setMaxHealth(26);
                        player.setHealth(26);
            }
        });
        packetHandler = new PacketHandler() {
            ItemStack itemStack = CraftItemStack.asNMSCopy(new org.bukkit.inventory.ItemStack(Material.AIR));
            @Override
            public void onSend(SentPacket sentPacket) {

                if(sentPacket.getPacketName().equalsIgnoreCase("PacketPlayOutEntityEquipment")){
                    int a = (int) sentPacket.getPacketValue("a");
                    System.out.println(getPlayers().stream().filter(player -> player.getEntityId() == a).count());
                    if(getPlayers().stream().filter(player -> player.getEntityId() == a).collect(Collectors.toList()).size() != 0){
                        System.out.println(sentPacket.getPlayer());

                        System.out.println(sentPacket.getPlayer().getEntityId());
                        System.out.println(a);
                        System.out.println("abc");

                       // inazumaUHC.invisibilityInventory.setInventoryInvisibleToOther(sentPacket.getPlayer(),b);
                        sentPacket.setPacketValue("c",itemStack);
                    }

                }
            }

            @Override
            public void onReceive(ReceivedPacket receivedPacket) {

            }


        };

        RoleItem roleItem = new RoleItem();
        roleItem.setItemstack(new ItemBuilder(Material.CARROT_ITEM).setName("Mur").toItemStack());
        roleItem.setRightClick(new RoleItem.RightClick() {
            @Override
            public void execute(Player player) {
                Location location = player.getLocation();
                Block block = location.clone().add(location.getDirection().multiply(-5)).getBlock();
                System.out.println(block);
                System.out.println(block.getType());
                Location bLocation = block.getLocation();
                for (int y = bLocation.getBlockY(); y > 0; y--) {
                  bLocation.setY(y);
                  if(bLocation.getBlock().getType() != Material.AIR){
                      bLocation.setY(y+1);
                      break;
                  }
                }
                System.out.println("Bloc > "+bLocation);
                Vector vector = location.getDirection().normalize().multiply(5);
                System.out.println("Vec"+location.clone().add(vector));
                Location rotate = location.clone();
                rotate.setPitch(0);
                rotate.setYaw(rotate.getYaw()+90);
                Location rotateInverse = location.clone();
                rotateInverse.setYaw(rotateInverse.getYaw()+(90*3));
                Vector borderMin = rotate.getDirection().normalize();
                Vector borderMax = rotateInverse.getDirection().normalize();
                System.out.println("VECY+ "+borderMax.getY());

                ArrayList<Location> allBlocks = new ArrayList<>();
                for (int i = 1; i < 20; i++) {
                    allBlocks.add(bLocation.clone().add(borderMin.clone().multiply(i)));
                    allBlocks.add(bLocation.clone().add(borderMax.clone().multiply(i)));
                }
                Material[] bannedMat = {Material.AIR,Material.YELLOW_FLOWER,Material.LONG_GRASS,Material.getMaterial(37),Material.RED_MUSHROOM,Material.BROWN_MUSHROOM,Material.CROPS,Material.DOUBLE_PLANT,Material.WATER,Material.STATIONARY_WATER};
                ArrayList<Location> locs = new ArrayList<>();
                int yMin = 255;
                for(Location ab : allBlocks){
                    for (int y = ab.getBlockY(); y > 0; y--) {
                        ab.setY(y);
                        if(!Arrays.asList(bannedMat).contains(ab.getBlock().getType())){
                            if(yMin > y){
                                yMin = y;
                            }
                            break;
                        }
                    }
                }
                for(Location ab : allBlocks){
                    ab.setY(yMin);

                    for (int i = ab.getBlockY(); i < (bLocation.getBlockY()+20); i++) {
                        System.out.println("ADD " + ab.getBlockY() +"/ " + ab);
                        Location loc = ab.clone();
                        loc.setY(i);
                        if(Arrays.asList(bannedMat).contains(loc.getBlock().getType())){
                            locs.add(loc);
                        }
                        //loc.getBlock().setType(Material.WOOD);
                    }
                }

                Collections.shuffle(locs);

                new BukkitRunnable() {
                    int i = 0;
                    int d = locs.size()/2;
                    int s = 0;
                    @Override
                    public void run() {
                        Material[] mat = {Material.WOOD, Material.MOSSY_COBBLESTONE,Material.DIRT,Material.LOG};
                        int k = 0;
                        if(d > locs.size()-1){
                            d = locs.size();
                        }
                        for(Location loc : locs.subList(i, d)){
                            loc.getBlock().setType(mat[k]);
                            if(k >= mat.length-1){
                                k = 0;
                            }

                            k++;
                        }
                        Sound[] sounds = {Sound.ANVIL_USE,Sound.ZOMBIE_WOODBREAK};
                        bLocation.getWorld().playSound(bLocation,sounds[s],5,1);
                        if(s >= mat.length){
                            s = 0;
                        }
                        s++;
                        if(locs.size()-1 <= d){
                            cancel();
                        }

                        i = d;
                        d += d;


                    }
                }.runTaskTimer(inazumaUHC,5L,20L);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for(Location l : locs){
                            l.getBlock().setType(Material.AIR);
                        }
                    }
                }.runTaskLater(InazumaUHC.get,20*30);


            }
        });
        addRoleItem(roleItem);
        addListener(this);
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageByEntityEvent event){
        if(event.getEntity() instanceof Player && event.getDamager() instanceof Player){
            Player player = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();


            if(getPlayers().contains(damager)){
                if (invisible){
                    if(register){
                        PacketListenerAPI.removePacketHandler(packetHandler);
                        register = false;
                    }
                    damager.removePotionEffect(PotionEffectType.INVISIBILITY);
                    inazumaUHC.invisibilityInventory.setInventoryToInitialToOther(damager);
                    isSneakTimer = false;
                    Team t = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(damager.getName());
                    t.setNameTagVisibility(NameTagVisibility.ALWAYS);
                    invisible = false;
                    System.out.println("invisible tap !");
                    b.cancel();
                }
                damager.removePotionEffect(PotionEffectType.SLOW);
                damager.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*3,0,false,false),true);
            }
        }
    }

    @EventHandler
    public void onChangeItemSlot(PlayerItemHeldEvent event){
      /*  if(invisible){
            Player player = event.getPlayer();
            if(getPlayers().contains(player)){
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        inazumaUHC.invisibilityInventory.setInventoryInvisibleToOther(player,0);
                    }
                }.runTaskLaterAsynchronously(InazumaUHC.get,1);

            }
        }*/
    }
    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event){
        event.setAsync(true);
        if(event.isSneaking() && !isSneakTimer){
            Player player = event.getPlayer();
            if(!getPlayers().contains(player)){
                return;
            }


            isSneakTimer = true;
            if(b != null){
                try {
                    b.cancel();
                }catch (Exception ignored){

                }

            }
             b = new BukkitRunnable() {
                int i = 10;
                @Override
                public void run() {
                    if(!player.isSneaking()){
                        invisible = false;
                        player.removePotionEffect(PotionEffectType.INVISIBILITY);
                        inazumaUHC.invisibilityInventory.setInventoryToInitialToOther(player);
                        isSneakTimer = false;
                        Team t = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(player.getName());
                        if(t == null){
                            if(register){
                                PacketListenerAPI.removePacketHandler(packetHandler);
                                register = false;
                            }
                            b.cancel();
                            return;
                        }
                        t.setNameTagVisibility(NameTagVisibility.ALWAYS);
                        if(register){
                            PacketListenerAPI.removePacketHandler(packetHandler);
                            register = false;
                        }
                        b.cancel();
                        return;
                    }
                    if(!invisible){
                        if(!PlayerUtils.getNearbyPlayersFromPlayer(player,20,12,20).isEmpty() && i < 10){
                            player.sendMessage(Preset.instance.p.prefixName()+"Il y a un joueur prêt de toi, tu ne peux pas utiliser ta technique");
                            TitleUtils.sendActionBar(player,"§cIl y a un joueur prêt de toi");
                            isSneakTimer = false;
                            if(register){
                                PacketListenerAPI.removePacketHandler(packetHandler);
                                register = false;
                            }
                            b.cancel();
                            return;
                        }
                    }

                    if (i > 0){
                        TitleUtils.sendActionBar(player,"§7Vous allez être Invisible dans §f" + i + " §7seconde(s)");
                    }
                    if(i == 0){
                        invisible = true;
                        TitleUtils.sendActionBar(player,"§7Vous êtes camouflé");
                        player.sendMessage(Preset.instance.p.prefixName()+" Vous êtes camouflé.");
                        inazumaUHC.invisibilityInventory.setInventoryInvisibleToOther(player);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0,false,false), true);
                        Player p = event.getPlayer();
                        Team t = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(player.getName());
                        t.setNameTagVisibility(NameTagVisibility.NEVER);
                        packetHandler();
                    }
                    i--;
                }
            }.runTaskTimerAsynchronously(inazumaUHC,20,20);
        }
    }
    public void packetHandler(){
        register = true;
        PacketListenerAPI.addPacketHandler(packetHandler);
}
}
