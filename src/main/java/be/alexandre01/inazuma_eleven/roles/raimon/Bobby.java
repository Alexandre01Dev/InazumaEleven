package be.alexandre01.inazuma_eleven.roles.raimon;

import be.alexandre01.inazuma.uhc.presets.IPreset;
import be.alexandre01.inazuma.uhc.roles.Role;
import be.alexandre01.inazuma.uhc.roles.RoleItem;
import be.alexandre01.inazuma.uhc.utils.ItemBuilder;
import be.alexandre01.inazuma_eleven.categories.Raimon;
import net.minecraft.server.v1_8_R3.Tuple;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class Bobby extends Role implements Listener {


    public Bobby( IPreset iPreset) {
        super("Bobby", iPreset);

        setRoleCategory(Raimon.class);
        addListener(this);
        RoleItem roleItem = new RoleItem();
        ItemBuilder ib = new ItemBuilder(Material.MAGMA_CREAM).setName("§1§lTacle de La Mort");
        roleItem.setItemstack(ib.toItemStack());
        //roleItem.deployVerificationsOnRightClick(roleItem.generateVerification(new Tuple<>(RoleItem.VerificationType.EPISODES,1)));

        roleItem.setRightClickOnPlayer(10, new RoleItem.RightClickOnPlayer(){
            @Override
            public void execute(Player player, Player rightClicked)
            {
                Location location = rightClicked.getLocation();
                location.setY(player.getLocation().getY());
                Vector v = location.toVector().subtract(player.getLocation().toVector()).normalize().multiply(2.7).add(new Vector(0, 1, 0));
                rightClicked.setVelocity(v);
            }
        });

        addRoleItem(roleItem);
    }
}
