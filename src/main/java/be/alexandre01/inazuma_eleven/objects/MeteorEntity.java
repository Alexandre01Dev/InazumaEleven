package be.alexandre01.inazuma_eleven.objects;

import be.alexandre01.inazuma.uhc.InazumaUHC;
import be.alexandre01.inazuma.uhc.utils.CustomSkeleton;
import be.alexandre01.inazuma.uhc.utils.PlayerUtils;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBlock;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.util.*;

import static net.minecraft.server.v1_8_R3.Material.AIR;

public class MeteorEntity extends EntityFireball implements Listener {
    private  boolean explosion = false;
    private float speedMod = 1.34F;
    private float explosionRadius = 9F;
    private float trailPower = 2F;
    private float brightness = 10F;
    ArrayList<FallingBlock> list = new ArrayList<>();
    Location location = null;
    public MeteorEntity(World world) {
        super(world);

    }

    @Override
    public void a(MovingObjectPosition movingObjectPosition) {
        Location calc = location.clone();
        for (int i = 200; i > 0; i--) {
            calc.setY(i);
            if(calc.getBlock().getType() != org.bukkit.Material.AIR){
                break;
            }
        }

        List<Player> p = PlayerUtils.getNearbyPlayers(calc,12,12,12);
        if(!p.isEmpty()){
            for(Player player : p){
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 8*20, 0,false,false), true);
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5*20, 0,false,false), true);
                InazumaUHC.get.invincibilityDamager.addPlayer(player,1000, EntityDamageEvent.DamageCause.ENTITY_EXPLOSION);
                InazumaUHC.get.invincibilityDamager.addPlayer(player,1000, EntityDamageEvent.DamageCause.BLOCK_EXPLOSION);

                Vector vector = player.getLocation().subtract(calc).toVector().normalize().multiply(6).multiply(1/(calc.distance(player.getLocation())/2.5D));

                vector.add(new Vector(0,1,0));
                if(vector.getY() > 3)
                    vector.setY(3);


                player.setVelocity(vector);
            }
        }

        List<Player> p2 = PlayerUtils.getNearbyPlayers(calc,24,24,24);
        if(!p2.isEmpty()){
            p2.removeAll(p);
            for(Player player : p2){
                Vector vector = player.getLocation().subtract(calc).toVector().normalize().multiply(6).multiply(1/(calc.distance(player.getLocation())/1.5D));
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 2*20, 0,false,false), true);
                vector.add(new Vector(0,0.6,0));
                if(vector.getY() > 2)
                    vector.setY(2);

                player.setVelocity(vector);
            }


        }




        location.getWorld().spigot().playEffect(calc, Effect.EXPLOSION_HUGE, 0,0,0f,0f,0f,0f,3,50);


        for (int i = 0; i < 360; i++) {
            location.getWorld().spigot().playEffect(calc, Effect.FLAME, 0,0,2f,2f,2f,2f,5,50);
            location.getWorld().spigot().playEffect(calc, Effect.CLOUD, 0,0,4f,4f,4f,4f,5,50);
        }
        calc.add(0,-2,0);
        Sphere sphere = new Sphere(calc,5);
        int j = 0;
        for(Block block : sphere.getBlocks()){
            FallingBlock b = block.getWorld().spawnFallingBlock(block.getLocation(),block.getType(),block.getData());
            //world.setAir(new BlockPosition(block.getX(),block.getY(),block.getZ()));
            block.setType(org.bukkit.Material.AIR);
            Vector vector = b.getLocation().subtract(location).toVector().normalize().multiply(4).multiply(1/(location.distance(b.getLocation())/2.5D));
            if(j%4 == 0){
                vector.multiply((1.5D) + (2.8D - 1.5D) * new Random().nextDouble());
            }
            b.setVelocity(vector);

            if(j % 8 == 0){
                list.add(b);
            }
            j++;
        }

       Explosion explosion = new Explosion(world,this, calc.getX(), calc.getY(), calc.getZ(), explosionRadius,true,true);
        explosion.a();
        explosion.a(true);
        List<BlockPosition> pos = explosion.getBlocks();
        Collections.shuffle(pos);
        int m = 30;
        if(pos.size() < 30)
            m = pos.size();
        int i = 0;


        new BukkitRunnable() {
            @Override
            public void run() {
                for(FallingBlock b : list){
                    b.remove();
                }
                HandlerList.unregisterAll(MeteorEntity.this);
            }
        }.runTaskLater(InazumaUHC.get,20*4);


        die();
        location.add(0,-4,0);
        Sphere meteor = new Sphere(calc,2);
        for(Block block : meteor.getBlocks()){
            if(new Random().nextBoolean()){
                block.setType(org.bukkit.Material.NETHERRACK);
            }else{
                block.setType(org.bukkit.Material.COAL_BLOCK);
            }

        }
    }

    @Override
    public void t_()
    {
        Explosion explosion = new Explosion(world,this, locX, locY, locZ, trailPower,true,false);
        explosion.a();
        explosion.a(false);
        List<BlockPosition> pos = explosion.getBlocks();
        Collections.shuffle(pos);
        int m = 10;
        if(pos.size() < 10)
            m = pos.size();


        int i = 0;
        for(BlockPosition blockPos : pos.subList(0,m)){
            Block block = this.getBukkitEntity().getWorld().getBlockAt(blockPos.getX(),blockPos.getY(),blockPos.getZ());
            org.bukkit.Material mat = block.getType();
            block.setType(org.bukkit.Material.AIR);

            FallingBlock b = block.getWorld().spawnFallingBlock(block.getLocation(),mat,block.getData());
            list.add(b);
            b.setHurtEntities(false);

            double x = (-1.5D) + (1.5D - (-1.5D)) * new Random().nextDouble();
            double y = 0.2D + (1.5D - 0.2D) * new Random().nextDouble();
            double z = (-1.5D) + (1.5D - (-1.5D)) * new Random().nextDouble();
            org.bukkit.util.Vector v = new org.bukkit.util.Vector(x,y,z);
            b.setVelocity(v);
            i++;
        }


        motX *= speedMod;
        motY *= speedMod;
        motZ *= speedMod;
        super.t_();
    }

    @Override
    public float c(float f)
    {
        return this.brightness;
    }

    private Object getPrivateField(String fieldName, Class clazz, Object a) {
        Object o = null;
        Field field;
        try {
            field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            o = field.get(a);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return o;
    }

    private void setPrivateField(String fieldName, Class clazz, Object a, Object e) {
        Field field;
        try {
            field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(a, e);
        } catch (NoSuchFieldException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    public static MeteorEntity spawn(Location loc) {
        World w = ((CraftWorld) loc.getWorld()).getHandle();
        MeteorEntity f = new MeteorEntity(w);
        Bukkit.getPluginManager().registerEvents(f,InazumaUHC.get);
        f.location = loc;
        f.setPosition(loc.getX(), loc.getY(), loc.getZ());
        w.addEntity(f, CreatureSpawnEvent.SpawnReason.CUSTOM);
        f.explosion = true;
        BukkitTask b = new BukkitRunnable() {
            double phi = 0;
            @Override
            public void run() {

                if(!f.isAlive())
                    cancel();
                Location location = f.getBukkitEntity().getLocation().clone();

                location.getWorld().spigot().playEffect(location, Effect.EXPLOSION, 0,0,0f,0f,0f,0f,3,50);
                location.getWorld().spigot().playEffect(location, Effect.FLAME, 0,0,1f,1f,1f,1f,1,50);





                for (int degree = 0; degree < 360; degree++) {
                    double radians = Math.toRadians(degree);
                    double x = Math.cos(radians);
                    double z = Math.sin(radians);
                    location.add(x,0,z);
                    location.subtract(x,0,z);
                }
            }
        }.runTaskTimer(InazumaUHC.get,20,20);
        //f.setPrivateField("a",EntitySkeleton.class, f,);
        return f;
    }

    @EventHandler
    public void onDamageEntity(EntityDamageEvent event){
        System.out.println(event.getEntity());
        if(event.getEntity() instanceof FallingBlock){
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onBlockChange(EntityChangeBlockEvent event){
        if(event.getEntity() instanceof FallingBlock){
            Block block = event.getBlock();
            FallingBlock fallingBlock = (FallingBlock) event.getEntity();
            if(list.contains(fallingBlock)){
                event.setCancelled(true);
                int i = new Random().nextInt(5);
                if(i >= 4){
                 block.getRelative(BlockFace.DOWN).setType(org.bukkit.Material.SOUL_SAND);
                    if(new Random().nextBoolean())
                       block.setType(org.bukkit.Material.FIRE);
                }else{
                    block.setType(org.bukkit.Material.FIRE);
                    block.getWorld().spigot().strikeLightningEffect(block.getLocation(),true);
                }
                list.remove(fallingBlock);
            }
        }
    }
}
