package be.alexandre01.inazuma_eleven.roles.raimon;

import be.alexandre01.inazuma.uhc.custom_events.episode.EpisodeChangeEvent;
import be.alexandre01.inazuma.uhc.custom_events.player.PlayerInstantDeathEvent;
import be.alexandre01.inazuma.uhc.presets.IPreset;
import be.alexandre01.inazuma.uhc.roles.Role;
import be.alexandre01.inazuma.uhc.roles.RoleItem;
import be.alexandre01.inazuma.uhc.utils.ItemBuilder;
import be.alexandre01.inazuma.uhc.utils.PatchedEntity;
import be.alexandre01.inazuma.uhc.utils.PlayerUtils;
import be.alexandre01.inazuma_eleven.categories.Raimon;
import net.minecraft.server.v1_8_R3.ItemGoldenApple;
import net.minecraft.server.v1_8_R3.Tuple;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class Bobby extends Role implements Listener
{
    Player hurt = null;

    public Bobby( IPreset iPreset) {
        super("Bobby", iPreset);

        setRoleCategory(Raimon.class);
        addListener(this);




        RoleItem roleItem = new RoleItem();
        ItemBuilder ib = new ItemBuilder(Material.MAGMA_CREAM).setName("§c§lScie§r-§c§lCirculaire");
        roleItem.setItemstack(ib.toItemStack());
        //roleItem.deployVerificationsOnRightClick(roleItem.generateVerification(new Tuple<>(RoleItem.VerificationType.EPISODES,1)));
        roleItem.setRightClick(player -> {
            if(player.getLocation().getYaw() >= 0 && player.getLocation().getYaw() <= 180)
            {
                for(Player target : PlayerUtils.getNearbyPlayers(new Location(player.getWorld(), player.getLocation().getBlockX() + 2, player.getLocation().getBlockY(), player.getLocation().getBlockZ()),3,3,3))
                {
                    Bukkit.broadcastMessage(player.getLocation().getBlockX() + 2 + "  " + player.getLocation().getBlockY() + "   " + player.getLocation().getBlockZ());
                    Location location = target.getLocation();
                    location.setY(player.getLocation().getY());
                    Vector v = location.toVector().subtract(player.getLocation().toVector()).normalize().multiply(2.7).add(new Vector(0, 1, 0));
                    target.setVelocity(v);
                }
            }
            else if(player.getLocation().getYaw() <= 0 && player.getLocation().getYaw() >= -180)
            {
                for(Player target : PlayerUtils.getNearbyPlayers(new Location(player.getWorld(), player.getLocation().getBlockX() - 2, player.getLocation().getBlockY(), player.getLocation().getBlockZ()), 3, 3,3))
                {
                    Bukkit.broadcastMessage(player.getLocation().getBlockX() - 2 + "  " + player.getLocation().getBlockY() + "   " + player.getLocation().getBlockZ());
                    Location location = target.getLocation();
                    location.setY(player.getLocation().getY());
                    Vector v = location.toVector().subtract(player.getLocation().toVector()).normalize().multiply(2.7).add(new Vector(0, 1, 0));
                    target.setVelocity(v);
                }
            }

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
}
