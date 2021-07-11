package be.alexandre01.inazuma_eleven.roles.raimon;

import be.alexandre01.inazuma.uhc.custom_events.episode.EpisodeChangeEvent;
import be.alexandre01.inazuma.uhc.custom_events.player.PlayerInstantDeathEvent;
import be.alexandre01.inazuma.uhc.presets.IPreset;
import be.alexandre01.inazuma.uhc.roles.Role;
import be.alexandre01.inazuma.uhc.roles.RoleItem;
import be.alexandre01.inazuma.uhc.utils.ItemBuilder;
import be.alexandre01.inazuma.uhc.utils.PatchedEntity;
import be.alexandre01.inazuma.uhc.utils.PlayerUtils;
import be.alexandre01.inazuma.uhc.utils.TitleUtils;
import be.alexandre01.inazuma_eleven.categories.Raimon;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_8_R3.Tuple;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Bobby extends Role implements Listener
{
    Player hurt = null;
    HashMap<Integer, List<Integer>> locations = new HashMap<Integer, List<Integer>>();
    ArrayList<Location> particlesLoc = new ArrayList<>();
    BukkitTask bt = null;

    public Bobby( IPreset iPreset) {
        super("Bobby", iPreset);

        setRoleCategory(Raimon.class);
        setRoleToSpoil(Erik.class);
        addListener(this);




        RoleItem roleItem = new RoleItem();
        ItemBuilder ib = new ItemBuilder(Material.MAGMA_CREAM).setName("§c§lScie§r-§c§lCirculaire");
        roleItem.setItemstack(ib.toItemStack());
        //roleItem.deployVerificationsOnRightClick(roleItem.generateVerification(new Tuple<>(RoleItem.VerificationType.EPISODES,1)));
        roleItem.setRightClick(player -> {

            Location loc = player.getLocation();
            loc.setPitch(0);
            Vector vector = loc.getDirection().normalize().multiply(3);

            Location rotate = loc.clone();
            rotate.setPitch(0);
            /*rotate.setYaw(rotate.getYaw()+90);
            Vector rotateVector = rotate.getDirection().normalize().multiply(3);
            rotate.add(rotateVector.getX(), 0, rotateVector.getZ());

            Location rotateInverse = loc.clone();
            rotateInverse.setYaw(rotateInverse.getYaw()+(90*3));
            Vector rotateInverseVector = rotateInverse.getDirection().normalize().multiply(3);
            rotateInverse.add(rotateInverseVector.getX(), 0, rotateInverseVector.getZ());

            //rotate.setYaw(loc.getYaw() + 90);
            rotate.getBlock().setType(Material.BEDROCK);
            System.out.println(rotate);

            rotateInverse.setPitch(0);
            //rotateInverse.setYaw(loc.getYaw() - 90);
            rotateInverse.getBlock().setType(Material.BEDROCK);
            */


            rotate.setYaw(rotate.getYaw() - 40);

            for (int i = 0; i < 80; i++) {
                rotate.setYaw(rotate.getYaw() + 1);
                for (int j = 0; j < 11; j++) {
                    Vector vec = rotate.getDirection().normalize().multiply(j);
                    Location location = rotate.clone();
                    location.add(vec.getX(), 0, vec.getZ());
                    List<Integer> a;
                    if(locations.containsKey(location.getBlockX()))
                    {
                        a = locations.get(location.getBlockX());

                    }
                    else {
                        a = new ArrayList<>();
                    }

                    if(locations.containsKey(location.getBlockX()) && a.contains(location.getBlockZ()))
                    {
                        continue;
                    }
                    a.add(location.getBlockZ());
                    locations.put(location.getBlockX(), a);
                }
            }
            for(Location location : particles(player.getLocation()))
            {
                float r = 252;
                float g = 94;
                float b = 3;
                PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.REDSTONE,true, (float) (location.getX()), (float) (location.getY()), (float) (location.getZ()), r / 255,g / 255, b / 255, 1, 0);

                for(Player online : Bukkit.getOnlinePlayers()) {
                    ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
                }
            }
            for (Player near : PlayerUtils.getNearbyPlayersFromPlayer(player, 10, 10, 10))
            {
                Location nearLocation = near.getLocation().clone();
                System.out.println(locations);
                System.out.println(nearLocation.getBlockX());
                System.out.println(nearLocation.getBlockX());
                if(locations.containsKey(nearLocation.getBlockX()))
                {
                    if(locations.get(nearLocation.getBlockX()).contains(nearLocation.getBlockZ()))
                    {
                        System.out.println(near.getName());
                        Vector v = near.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().multiply(4);
                        v.add(new Vector(0,1,0));
                        if(v.getY() > 2)
                            v.setY(2);
                        near.setVelocity(v);
                    }
                }
            }

            locations.clear();


            /*
            rotateVector = rotate.getDirection().normalize().multiply(2);
            rotate.add(rotateVector.getX(), 0, rotateVector.getZ());
            rotate.getBlock().setType(Material.BEDROCK);
            rotateVector = rotate.getDirection().normalize().multiply(3);
            rotate.add(rotateVector.getX(), 0, rotateVector.getZ());
            rotate.getBlock().setType(Material.BEDROCK);
            rotateVector = rotate.getDirection().normalize().multiply(4);
            rotate.add(rotateVector.getX(), 0, rotateVector.getZ());
            rotate.getBlock().setType(Material.BEDROCK);
            rotateVector = rotate.getDirection().normalize().multiply(5);
            rotate.add(rotateVector.getX(), 0, rotateVector.getZ());
            rotate.getBlock().setType(Material.BEDROCK);*/



            //A gauche
           /* rotateInverse.setYaw(rotateInverse.getYaw() + 90);
            for (int i = 0; i < 10; i++) {
                Vector vec = rotateInverse.getDirection().normalize().multiply(i);
                Location location = rotateInverse.clone().add(vec.getX(), 0, vec.getZ());
                location.getBlock().setType(Material.BEDROCK);
                reverseLocations.add(location);
            }
            rotate.setYaw(rotate.getYaw() - 90);

            //A droite
            for (int i = 0; i < 10; i++) {
                Vector vec = rotate.getDirection().normalize().multiply(i);
                Location location = rotate.clone().add(vec.getX(), 0, vec.getZ());
                location.getBlock().setType(Material.BEDROCK);
            }

            rotate.setYaw(rotate.getYaw() - 90);

            for (int i = 1; i < 8; i++) {
                Vector vec = rotate.getDirection().normalize().multiply(i);
                Location location = rotate.clone().add(vec.getX(), 0, vec.getZ());
                System.out.println(location);
                if(reverseLocations.contains(location))
                    break;

                location.getBlock().setType(Material.BEDROCK);
                locations.add(location);
                //allLocations(location, vec);
                System.out.println(location);
            }
            rotate.setYaw(rotate.getYaw() - 5);
            for (int i = 1; i < 8; i++) {
                Vector vec = rotate.getDirection().normalize().multiply(i);
                Location location = rotate.clone().add(vec.getX(), 0, vec.getZ());
                System.out.println(location);
                if(reverseLocations.contains(location))
                    break;

                location.getBlock().setType(Material.BEDROCK);
                locations.add(location);
                //allLocations(location, vec);
                System.out.println(location);
            }
            rotate.setYaw(rotate.getYaw() + 5);

            System.out.println("les locations sont : " + locations);

            /*for (int i = 1; i < locations.size(); i++) {
                Vector vec = rotate.getDirection().normalize().multiply(i);
                Location location = locations.get(i).clone().add(vec.getX(), 0, vec.getZ());
                System.out.println(location);
                if(reverseLocations.contains(location))
                    break;

                location.getBlock().setType(Material.BEDROCK);
                locations.add(location);
                //allLocations(location, vec);
                System.out.println(location);
            }*/

            /*for(Location location : locations)
            {
                allLocations(location);
            }*/

        });


        RoleItem reduceAbsorption = new RoleItem();
        reduceAbsorption.setItemstack(new ItemBuilder(Material.BONE).setName("§c§lTacle de la Mort").toItemStack());
        reduceAbsorption.deployVerificationsOnRightClick(reduceAbsorption.generateVerification(new Tuple<>(RoleItem.VerificationType.USAGES,1)));
        reduceAbsorption.setRightClickOnPlayer(5, new RoleItem.RightClickOnPlayer() {
            @Override
            public void execute(Player player, Player rightClicked) {
                hurt = rightClicked;
                PatchedEntity.setMaxHealthInSilent(rightClicked, rightClicked.getMaxHealth() - 4);
            }
        });

        addRoleItem(roleItem);
        addRoleItem(reduceAbsorption);
    }

    @EventHandler
    public void onEat(EpisodeChangeEvent event)
    {
        if(hurt != null)
        {
            PatchedEntity.setMaxHealthInSilent(hurt, hurt.getMaxHealth() + 4);
        }
    }

    @EventHandler
    public void onDeath(PlayerInstantDeathEvent event)
    {
        if(event.getPlayer() == hurt)
        {
            hurt = null;
        }
    }

    @EventHandler
    public void onSwitchItem(PlayerItemHeldEvent event)
    {
        ItemStack is = event.getPlayer().getInventory().getItem(event.getNewSlot());
        if(bt != null)
        {
            bt.cancel();
        }
        if(is == null)
        {
            return;
        }


        if(is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().equalsIgnoreCase("§c§lScie§r-§c§lCirculaire"))
        {
            bt = new BukkitRunnable() {
                @Override
                public void run() {
                    for(Location location : particles(event.getPlayer().getLocation()))
                    {
                        TitleUtils.sendActionBar(event.getPlayer(), "§aSeulement vous voyez ces particules");
                        float r = 252;
                        float g = 94;
                        float b = 3;
                        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.REDSTONE,true, (float) (location.getX()), (float) (location.getY()), (float) (location.getZ()), r / 255,g / 255, b / 255, 1, 0);

                        ((CraftPlayer) event.getPlayer()).getHandle().playerConnection.sendPacket(packet);

                    }
                }
            }.runTaskTimerAsynchronously(inazumaUHC, 1, 6);
        }
        else {
            if(bt != null)
                bt.cancel();
        }
    }

    ArrayList<Location> particles(Location location)
    {
        ArrayList<Location> particlesLocation = new ArrayList<>();
        Location first = location.clone();
        first.setPitch(0);
        first.setYaw(first.getYaw() - 40);
        Location second = location.clone();
        second.setPitch(0);
        second.setYaw(second.getYaw() + 40);
        Vector vector = first.getDirection().normalize().multiply(10);
        Location longestFirst = first.clone().add(vector.getX(), 0, vector.getZ());
        longestFirst.setPitch(0);
        longestFirst.setYaw(longestFirst.getYaw() + 130);
        forParticles(particlesLocation, first, -1);

        forParticles(particlesLocation, second, -1);

        forLongParticles(particlesLocation, longestFirst, -1);

        forParticles(particlesLocation, first, 2.5);

        forParticles(particlesLocation, second, 2.5);

        forLongParticles(particlesLocation, longestFirst, 2.5);

        return particlesLocation;
        /*location.add(vector.getX(), 0, vector.getZ());
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.DRIP_WATER,true, (float) (location.getX()), (float) (location.getY()), (float) (location.getZ()), 0, 0, 0, 0, 1);

        for(Player online : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
        }
        location.setYaw(location.getYaw() + 120);
        vector = location.getDirection().normalize().multiply(9);
        location.add(vector.getX(), 0, vector.getZ());
        packet = new PacketPlayOutWorldParticles(EnumParticle.DRIP_WATER,true, (float) (location.getX()), (float) (location.getY()), (float) (location.getZ()), 0, 0, 0, 0, 1);

        for(Player online : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
        }*/

    }

    private void forParticles(ArrayList<Location> particlesLocation, Location second, double y) {
        for (int i = 0; i < 100; i++) {
            forAll(particlesLocation, second, y, i);
        }
    }
    private void forLongParticles(ArrayList<Location> particlesLocation, Location second, double y) {
        for (int i = 0; i < 129; i++) {
            forAll(particlesLocation, second, y, i);
        }
    }

    private void forAll(ArrayList<Location> particlesLocation, Location second, double y, int i) {
        Location increaseLocation = second.clone();
        increaseLocation.setPitch(0);
        increaseLocation.setY(increaseLocation.getY() + y);
        Vector lowVector = increaseLocation.getDirection().normalize().multiply(i/10);
        increaseLocation.add(lowVector.getX(), 0, lowVector.getZ());
        particlesLocation.add(increaseLocation);
    }

    /*void allLocations(Location rotate)
    {
        rotate.setYaw(rotate.getYaw() + 90);
        for (int i = 1; i < 8; i++) {
            Vector vec = rotate.getDirection().normalize().multiply(i);
            Location location = rotate.clone().add(vec.getX(), 0, vec.getZ());
            System.out.println(location);
            if(location.getBlock().getType() == Material.BEDROCK)
                break;

            location.getBlock().setType(Material.BEDROCK);
            System.out.println(location);
        }
    }*/

}
