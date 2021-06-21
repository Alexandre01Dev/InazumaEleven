package be.alexandre01.inazuma_eleven.listeners;

import be.alexandre01.inazuma.uhc.utils.CustomShapedRecipe;
import be.alexandre01.inazuma.uhc.utils.ItemBuilder;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.CraftingManager;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

import static org.bukkit.Bukkit.getServer;

public class CustomGlasses implements Listener {

    public void craftCustom() {

        ItemStack glasses = new ItemStack(Material.PRISMARINE_SHARD, 1);
        ItemMeta glassesM = glasses.getItemMeta();
        glassesM.setDisplayName("§b§lLunette");
        glassesM.setLore(Arrays.asList(" "));
        glasses.setItemMeta(glassesM);

        CustomShapedRecipe glasses1 = new CustomShapedRecipe(glasses);

        glasses1.shape("ABA","CDC","BEB");

        glasses1.setIngredient('A', Material.STICK);
        glasses1.setIngredient('B', Material.GOLD_INGOT);
        glasses1.setIngredient('C', Material.GLASS);
        glasses1.setIngredient('D', new ItemBuilder(Material.PRISMARINE_CRYSTALS).setName("§b§lLunette §c§lCassé").toItemStack());
        glasses1.setIngredient('E', Material.IRON_INGOT);

        getServer().addRecipe(glasses1);


    }

}
