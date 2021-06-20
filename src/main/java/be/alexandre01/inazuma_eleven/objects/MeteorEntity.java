package be.alexandre01.inazuma_eleven.objects;

import be.alexandre01.inazuma.uhc.InazumaUHC;
import be.alexandre01.inazuma.uhc.utils.CustomSkeleton;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.block.CraftBlock;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.util.*;

import static net.minecraft.server.v1_8_R3.Material.AIR;

public class MeteorEntity extends EntityFireball implements Listener {
    private  boolean explosion = false;
    private float speedMod = 1.1F;
    private float explosionRadius = 9F;
    private float trailPower = 2F;
    private float brightness = 10F;
    ArrayList<FallingBlock> list = new ArrayList<>();
    public MeteorEntity(World world) {
        super(world);
        Bukkit.getPluginManager().registerEvents(this,InazumaUHC.get);
    }

    @Override
    public void a(MovingObjectPosition movingObjectPosition) {
        Location location = movingObjectPosition.entity.getBukkitEntity().getLocation();
        location.add(0,-2,0);
        Sphere sphere = new Sphere(location,3);
        for(Block block : sphere.getBlocks()){
            FallingBlock b = block.getWorld().spawnFallingBlock(block.getLocation(),block.getData(),block.getData());
            world.setAir(new BlockPosition(block.getX(),block.getY(),block.getZ()));

            if(new Random().nextBoolean())
                b.setFireTicks(100);
            Vector vector = b.getLocation().subtract(location).toVector().normalize().multiply(4).multiply(1/(location.distance(b.getLocation())/2.5D));
            b.setVelocity(vector);

        }

       Explosion explosion = new Explosion(world,this, locX, locY, locZ, explosionRadius,true,true);
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
        location.add(0,-2,0);
        Sphere meteor = new Sphere(location,2);
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
        f.setPosition(loc.getX(), loc.getY(), loc.getZ());
        w.addEntity(f, CreatureSpawnEvent.SpawnReason.CUSTOM);
        f.explosion = true;
        //f.setPrivateField("a",EntitySkeleton.class, f,);
        return f;
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
