package be.alexandre01.inazuma_eleven.roles.raimon;

import be.alexandre01.inazuma.uhc.custom_events.episode.EpisodeChangeEvent;
import be.alexandre01.inazuma.uhc.presets.IPreset;
import be.alexandre01.inazuma.uhc.presets.Preset;
import be.alexandre01.inazuma.uhc.roles.Role;
import be.alexandre01.inazuma.uhc.roles.RoleItem;
import be.alexandre01.inazuma.uhc.timers.game.StabilizationTimer;
import be.alexandre01.inazuma.uhc.utils.ItemBuilder;
import be.alexandre01.inazuma.uhc.utils.PlayerUtils;
import be.alexandre01.inazuma.uhc.worlds.executors.ArrowToCenter;
import be.alexandre01.inazuma_eleven.categories.Raimon;
import be.alexandre01.inazuma_eleven.objects.Sphere;
import be.alexandre01.inazuma_eleven.roles.alius.Gazelle;
import be.alexandre01.inazuma_eleven.roles.alius.Torch;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArrow;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.time.LocalDate;
import java.util.ArrayList;

public class Erik extends Role implements Listener {

    Arrow specialArrow = null;
    BukkitTask arrowTask;
    Vector vector = null;
    World world = null;
    Location location;
    float force = 0;
    boolean timer = false;
    boolean canShoot = true;
    BukkitTask particleTask;
    float r = 54;
    float g = 141;
    float b = 227;


    public Erik(IPreset iPreset)
    {
        super("Erik", iPreset);



        setRoleCategory(Raimon.class);
        setRoleToSpoil(Bobby.class);
        addListener(this);

        addDescription("https://blog.inazumauhc.fr/inazuma-eleven-uhc/roles/raimon/eric");

        RoleItem roleItem = new RoleItem();
        roleItem.setItemstack(new ItemBuilder(Material.BOW).setName("??l??7Tir-??3H??licopt??re").toItemStack());
      // roleItem.deployVerificationsOnRightClick(roleItem.generateVerification(new Tuple<>(RoleItem.VerificationType.EPISODES,1)));
        roleItem.setPlaceableItem(true);
        addRoleItem(roleItem);

        RoleItem danse = new RoleItem();
        danse.setItemstack(new ItemBuilder(Material.BLAZE_ROD).setName("??l??cDanse??7-??eArdente").toItemStack());
        danse.deployVerificationsOnRightClickOnPlayer(danse.generateVerification(new Tuple<>(RoleItem.VerificationType.EPISODES,1)));
        danse.setRightClickOnPlayer(15,new RoleItem.RightClickOnPlayer() {
            @Override
            public void execute(Player player, Player rightClicked) {
                player.sendMessage(Preset.instance.p.prefixName()+"Vous avez utilis?? votre ??l??cDanse??7-??eArdente??7 sur ??c"+ rightClicked.getName());
                particleTask = new BukkitRunnable() {
                    double var = 0;
                    @Override
                    public void run() {
                        var += Math.PI / 8;
                        Location loc = rightClicked.getLocation();
                        double x = Math.cos(var);
                        double z = Math.sin(var);

                        rightClicked.setFireTicks(20*2);

                        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.FLAME,true, (float) (loc.getX() + x), (float) (rightClicked.getEyeLocation().getY() - 0.7), (float) (loc.getZ() + z), 0, 0, 0, 0, 1);
                        for(Player online : PlayerUtils.getNearbyPlayers(rightClicked.getLocation(), 100, 100, 100)) {
                            ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
                        }
                    }
                }.runTaskTimerAsynchronously(inazumaUHC, 1,1);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        particleTask.cancel();
                    }
                }.runTaskLaterAsynchronously(inazumaUHC, 20*30);
            }





        });
        addRoleItem(danse);
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent event)
    {
        if(event.getEntity() instanceof Player && event.getProjectile() instanceof Arrow)
        {
            Player player = (Player)event.getEntity();
            Arrow arrow = (Arrow)event.getProjectile();
            vector = event.getProjectile().getVelocity();
            force = event.getForce();


            if(event.getBow().hasItemMeta() && event.getBow().getItemMeta().hasDisplayName() && event.getBow().getItemMeta().getDisplayName().equalsIgnoreCase("??l??7Tir-??3H??licopt??re"))
            {
                if(timer || !canShoot)
                {
                    return;
                }
                canShoot = false;
                world = ((CraftWorld) player.getWorld()).getHandle();
                specialArrow = arrow;
                arrowRespawn();
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        timer = true;
                        arrowTask.cancel();
                    }
                }.runTaskLaterAsynchronously(inazumaUHC, 30);
            }
        }

    }
    void arrowRespawn()
    {
        Location arrowLoc = specialArrow.getLocation();


        arrowTask = new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getScheduler().scheduleSyncDelayedTask(inazumaUHC, () -> destructionArrow(specialArrow, world, 3));
                PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.REDSTONE, true, ((float) specialArrow.getLocation().getX()), (float) (specialArrow.getLocation().getY()),  ((float) specialArrow.getLocation().getZ()), r / 255,g / 255, b / 255, 1, 0);
                ArrayList<Player> nearbyPlayers = PlayerUtils.getNearbyPlayers(specialArrow.getLocation(), 150, 50, 150);
                if(!nearbyPlayers.isEmpty())
                {
                    for(Player online : nearbyPlayers)
                    {
                        ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
                    }
                }


                if(specialArrow.isOnGround())
                {
                    System.out.println("fleche plant??");
                    specialArrow.remove();
                    timer = false;
                    cancel();
                }

            }
        }.runTaskTimerAsynchronously(inazumaUHC, 2, 1);

        System.out.println("avant refleche");

    }

    @EventHandler
    void onHit(ProjectileHitEvent event)
    {
        if(event.getEntity() instanceof Arrow)
        {
            System.out.println("c'est une fleche");
            Arrow arrow = (Arrow)event.getEntity();
            if(arrow == specialArrow)
            {
                System.out.println("on ground");
                location = arrow.getLocation();
                specialArrow = arrow;
                arrowTask.cancel();
                arrow.remove();

                if(!timer)
                {
                    destructionArrow(specialArrow, world, 5);
                    specialArrow = getPlayers().get(0).getWorld().spawnArrow(location, vector, force * 2, 0);
                    destructionArrow(specialArrow, world, 3);
                    arrowTask = new BukkitRunnable() {
                        @Override
                        public void run() {

                            location = specialArrow.getLocation();
                            Bukkit.getScheduler().scheduleSyncDelayedTask(inazumaUHC, () -> destructionArrow(specialArrow, world, 3));
                            PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.REDSTONE, true, ((float) location.getX()), (float) (location.getY()), ((float) location.getZ()), r / 255, g / 255, b / 255, 1, 0);
                            ArrayList<Player> nearbyPlayers = PlayerUtils.getNearbyPlayers(specialArrow.getLocation(), 150, 50, 150);
                            if(!nearbyPlayers.isEmpty())
                            {
                                for(Player online : nearbyPlayers)
                                {
                                    ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
                                }
                            }
                            if(specialArrow.isOnGround())
                            {
                                arrowTask.cancel();
                            }
                        }
                    }.runTaskTimerAsynchronously(inazumaUHC, 1, 1);
                }
            }
        }

    }

    void secondArrow()
    {
        new BukkitRunnable() {
            @Override
            public void run() {
                System.out.println(specialArrow.getLocation());
                Location location = specialArrow.getLocation();
                Vector vector1 = specialArrow.getLocation().getDirection().normalize().multiply(1);
                location.add(vector1);
                Bukkit.getScheduler().scheduleSyncDelayedTask(inazumaUHC, () -> destructionArrow(specialArrow, world, 5));
                Bukkit.getScheduler().scheduleSyncDelayedTask(inazumaUHC, () -> specialArrow = getPlayers().get(0).getWorld().spawnArrow(location, vector, force * 2, 0));
                Bukkit.getScheduler().scheduleSyncDelayedTask(inazumaUHC, () -> destructionArrow(specialArrow, world, 3));
                Location arrowLoc = specialArrow.getLocation();

                arrowTask = new BukkitRunnable() {
                    @Override
                    public void run() {
                        Bukkit.getScheduler().scheduleSyncDelayedTask(inazumaUHC, () -> destructionArrow(specialArrow, world, 3));
                        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.REDSTONE, true, ((float) arrowLoc.getX()), (float) (arrowLoc.getY()), ((float) arrowLoc.getZ()), r / 255, g / 255, b / 255, 1, 0);
                        ArrayList<Player> nearbyPlayers = PlayerUtils.getNearbyPlayers(specialArrow.getLocation(), 150, 50, 150);
                        if(!nearbyPlayers.isEmpty())
                        {
                            for(Player online : nearbyPlayers)
                            {
                                ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
                            }
                        }
                        if(specialArrow.isOnGround())
                        {
                            timer = false;
                            arrowTask.cancel();
                        }
                    }
                }.runTaskTimerAsynchronously(inazumaUHC, 1, 1);
            }
        }.runTaskLaterAsynchronously(inazumaUHC, 2);

    }

    void destructionArrow(Arrow finalArrow, World world, int radius)
    {
        float r = 9;
        float g = 96;
        float b = 235;
        Sphere sphere = new Sphere(finalArrow.getLocation(), radius);
        int i = 0;
        ArrayList<Player> nearbyPlayers = PlayerUtils.getNearbyPlayers(finalArrow.getLocation(), 150, 50, 150);
        for(Block block : sphere.getBlocks())
        {
            if(block.getType() == Material.WATER || block.getType() == Material.BEDROCK)
                continue;

            Location loc = block.getLocation();
            if(i % 4 == 0)
            {
                if(r == 9)
                {
                    r = 5;
                    g = 254;
                    b = 247;
                }
                else {
                    r = 9;
                    g = 96;
                    b = 235;
                }
                PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.REDSTONE, true, ((float) loc.getX()), (float) (loc.getY()), ((float) loc.getZ()), r / 255, g / 255, b / 255, 1, 0);

                if(!nearbyPlayers.isEmpty())
                {
                    for(Player online : nearbyPlayers)
                    {
                        ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
                    }
                }
            }

            world.setAir(new BlockPosition(block.getX(), block.getY(), block.getZ()));
            //block.setType(Material.AIR);
            if(finalArrow.isOnGround())
            {
                timer = false;
                arrowTask.cancel();
            }
            i++;
        }
    }

    @EventHandler
    void onInteract(PlayerInteractEvent event)
    {

        if(inazumaUHC.rm.getRole(event.getPlayer()) instanceof Erik)
        {
            ItemStack it = event.getItem();
            if(it == null)
                return;

            if(!canShoot && it.getType() == Material.BOW && it.hasItemMeta() && it.getItemMeta().hasDisplayName() && it.getItemMeta().getDisplayName().equalsIgnoreCase("??l??7Tir-??3H??licopt??re"))
            {
                if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
                {
                    event.getPlayer().sendMessage(Preset.instance.p.prefixName() + "??cVous avez d??j?? utilis?? votre ??l??7Tir-??3H??licopt??re ??r??ccet ??pisode");
                }
            }
        }

    }
    @EventHandler
    void onEpisodeChange(EpisodeChangeEvent event)
    {
        timer = false;
        canShoot = true;
    }
}
