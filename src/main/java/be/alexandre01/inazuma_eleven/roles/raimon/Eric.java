package be.alexandre01.inazuma_eleven.roles.raimon;

import be.alexandre01.inazuma.uhc.presets.IPreset;
import be.alexandre01.inazuma.uhc.roles.Role;
import be.alexandre01.inazuma.uhc.roles.RoleItem;
import be.alexandre01.inazuma.uhc.utils.ItemBuilder;
import be.alexandre01.inazuma.uhc.worlds.executors.ArrowToCenter;
import be.alexandre01.inazuma_eleven.categories.Raimon;
import be.alexandre01.inazuma_eleven.objects.Sphere;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Eric extends Role implements Listener {

    BukkitTask arrowTask;


    public Eric (IPreset iPreset)
    {
        super("Eric", iPreset);



        setRoleCategory(Raimon.class);
        addListener(this);

        RoleItem roleItem = new RoleItem();
        roleItem.setItemstack(new ItemBuilder(Material.BOW).setName("Tir Pegase").toItemStack());
        roleItem.setPlaceableItem(true);
        addRoleItem(roleItem);

    }

    @EventHandler
    public void onShoot(EntityShootBowEvent event)
    {
        if(event.getEntity() instanceof Player && event.getProjectile() instanceof Arrow)
        {
            Player player = (Player)event.getEntity();
            Arrow arrow = (Arrow)event.getProjectile();

            if(event.getBow().hasItemMeta() && event.getBow().getItemMeta().hasDisplayName() && event.getBow().getItemMeta().getDisplayName().equalsIgnoreCase("Tir Pegase"))
            {
                World world = ((CraftWorld) player.getWorld()).getHandle();

                arrowTask = new BukkitRunnable() {
                    int i = 0;
                    @Override
                    public void run() {
                        i++;
                        Sphere sphere = new Sphere(arrow.getLocation(), 3);
                        sphere.getBlocks().forEach(block -> {
                            if(block.getType() == Material.WATER || block.getType() == Material.BEDROCK)
                                return;

                            world.setAir(new BlockPosition(block.getX(), block.getY(), block.getZ()));
                            //block.setType(Material.AIR);
                        });

                        if(i == 20)
                        {
                            cancel();
                        }
                    }
                }.runTaskTimer(inazumaUHC, 3, 1);

            }
        }
    }
}
