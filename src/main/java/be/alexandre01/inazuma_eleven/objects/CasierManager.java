package be.alexandre01.inazuma_eleven.objects;

import be.alexandre01.inazuma.uhc.InazumaUHC;
import be.alexandre01.inazuma.uhc.utils.ItemBuilder;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
public class CasierManager implements Listener {

    private static CasierManager instance;
    private boolean isRegistered = false;
    private List<Casier> casiers = new ArrayList<>();
    private HashMap<Inventory,Casier> inventories = new HashMap<>();
    private HashMap<Block,Casier> blocks = new HashMap<>();

    public static void init(){
        instance = new CasierManager();
    }
    public static CasierManager get(){
        return instance;
    }

    public void addCasier(Casier casier){
        casier.blocks.forEach(block -> {
            blocks.put(block,casier);

        });
        inventories.put(casier.inventory,casier);
        casiers.add(casier);

        if(!isRegistered){
            Bukkit.getPluginManager().registerEvents(this, InazumaUHC.get);
            isRegistered = true;
        }
    }


    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        Block block = event.getClickedBlock();
        if(block != null){
            if(blocks.containsKey(block)){
                Casier casier = blocks.get(block);
                casier.action.a(event.getPlayer(),casier);
            }
        }
    }


    @EventHandler
    public void onClickInventory(InventoryClickEvent event){
        Inventory inventory = event.getClickedInventory();
        if(inventory == null)
            return;

        if(inventories.containsKey(inventory)){
            Casier casier = inventories.get(inventory);
            ItemStack itemStack = event.getCurrentItem();
            if(itemStack != null){
                if(casier.itemStack != itemStack){
                    event.setCancelled(true);
                }
            }
        }
    }

    public static class Casier {
        private action action = new action() {
            @Override
            public boolean a(Player player, Casier casier) {
                player.openInventory(casier.inventory);
                return true;
            }
        };
        public ItemStack itemStack;
        public ItemStack border;
        public Inventory inventory;
        public ArmorStand as;
        public ArrayList<Block> blocks = new ArrayList<>();
        public Casier(Location location) {
            as = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);


            as.setArms(true);

            as.setItemInHand(new ItemBuilder(Material.COMPASS).setName("casier").toItemStack());
            as.setGravity(false);
            as.setBasePlate(false);

            blocks.add(location.getBlock());
            location.getBlock().setType(Material.BARRIER);
            blocks.add(location.getBlock().getRelative(BlockFace.UP));
            location.getBlock().getRelative(BlockFace.UP).setType(Material.BARRIER);

        }

        public Casier() {
        }



        public void deploy(Location location){
            as = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);


            as.setArms(true);

            as.setItemInHand(new ItemBuilder(Material.COMPASS).setName("casier").toItemStack());

            as.setGravity(false);

            as.setBasePlate(false);

            blocks.add(location.getBlock());
            location.getBlock().setType(Material.BARRIER);
            blocks.add(location.getBlock().getRelative(BlockFace.UP));
            location.getBlock().getRelative(BlockFace.UP).setType(Material.BARRIER);

            System.out.println("AS ! "+location);
        }

        public void setupInventory(){
            inventory = Bukkit.createInventory(null, 36);
            int[] principalPos = {
                    getCase(1,1), getCase(2,1), getCase(1,2),
                    getCase(8,1),getCase(9,1), getCase(9,2),
                    getCase(1,3),getCase(1,4), getCase(2,4),
                    getCase(8,4),getCase(9,4),getCase(9,3)};

            int itemLoc = getCase(5,2);
            inventory.setItem(itemLoc,itemStack);
            for(int i : principalPos){
                inventory.setItem(i,border);
            }

        }


        public void setItem(ItemStack itemStack){
            this.itemStack = itemStack;
        }
        //CALCULS
        public int getCase(int slot, int raw){
            return (slot-1)+((raw-1)*9);
        }
    }


    public interface action{
        boolean a(Player player,Casier casier);
    }

}

