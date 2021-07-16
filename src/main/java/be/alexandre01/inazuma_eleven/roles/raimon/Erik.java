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
    float r = 54;
    float g = 141;
    float b = 227;


    public Erik(IPreset iPreset)
    {
        super("Erik", iPreset);



        setRoleCategory(Raimon.class);
        addListener(this);

        RoleItem roleItem = new RoleItem();
        roleItem.setItemstack(new ItemBuilder(Material.BOW).setName("§l§7Tir-§3Pegase").toItemStack());
        roleItem.setPlaceableItem(true);
        roleItem.setRightClick(player -> {
            if(timer)
            {

            }
        });
        addRoleItem(roleItem);

        RoleItem danse = new RoleItem();
        danse.setItemstack(new ItemBuilder(Material.BLAZE_ROD).setName("§l§cDanse§7-§eArdente").toItemStack());
        danse.deployVerificationsOnRightClick(danse.generateVerification(new Tuple<>(RoleItem.VerificationType.COOLDOWN,60*10)));
        danse.setRightClick(player -> {
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 20*150, 0, false, false), true);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20*150, 0, false, false), true);

            new BukkitRunnable() {
                double var = 0;
                @Override
                public void run() {
                    var += Math.PI / 8;
                    Location loc = player.getLocation();
                    double x = Math.cos(var);
                    double z = Math.sin(var);

                    PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.FLAME,true, (float) (loc.getX() + x), (float) (player.getEyeLocation().getY() - 0.7), (float) (loc.getZ() + z), 0, 0, 0, 0, 1);
                    for(Player online : PlayerUtils.getNearbyPlayers(player.getLocation(), 100, 100, 100)) {
                        ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
                    }
                }
            }.runTaskTimerAsynchronously(inazumaUHC, 1,1);

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


            if(event.getBow().hasItemMeta() && event.getBow().getItemMeta().hasDisplayName() && event.getBow().getItemMeta().getDisplayName().equalsIgnoreCase("§l§7Tir-§3Pegase"))
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
                    System.out.println("fleche planté");
                    specialArrow.remove();
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
                            System.out.println("flechheehdehjerejkhkhdgrk");
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

            if(!canShoot && it.getType() == Material.BOW && it.hasItemMeta() && it.getItemMeta().hasDisplayName() && it.getItemMeta().getDisplayName().equalsIgnoreCase("§l§7Tir-§3Pegase"))
            {
                if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
                {
                    event.getPlayer().sendMessage(Preset.instance.p.prefixName() + "§cVous avez déjà utilisé votre §l§7Tir-§3Pegase §r§ccet épisode");
                }
            }
        }

    }
    @EventHandler
    void onEpisodeChange(EpisodeChangeEvent event)
    {
        canShoot = true;
    }
}
