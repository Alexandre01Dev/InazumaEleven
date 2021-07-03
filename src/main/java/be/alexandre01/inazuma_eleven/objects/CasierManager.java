package be.alexandre01.inazuma_eleven.objects;

import be.alexandre01.inazuma.uhc.InazumaUHC;
import be.alexandre01.inazuma.uhc.utils.ItemBuilder;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
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
    private List<ArmorStand> armorStands = new ArrayList<>();
    private HashMap<Inventory, Casier> inventories = new HashMap<>();
    private HashMap<Block, Casier> blocks = new HashMap<>();

    public static void init() {
        instance = new CasierManager();
    }

    public static CasierManager get() {
        return instance;
    }

    public void addCasier(Casier casier) {
        casier.blocks.forEach(block -> {
            blocks.put(block, casier);

        });
        inventories.put(casier.inventory, casier);
        armorStands.add(casier.as);
        casiers.add(casier);


        if (!isRegistered) {
            Bukkit.getPluginManager().registerEvents(this, InazumaUHC.get);
            isRegistered = true;
        }
    }


    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block != null) {
            if (blocks.containsKey(block)) {
                Casier casier = blocks.get(block);
                casier.action.a(event.getPlayer(), casier);
            }
        }
    }

    @EventHandler
    public void onInteractWithArmorStand(PlayerInteractEntityEvent event){
        if(event.getRightClicked() instanceof ArmorStand){
            ArmorStand armorStand = (ArmorStand) event.getRightClicked();
            if(armorStands.contains(armorStand)){
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onInteractWithArmorStand(EntityDamageByEntityEvent event){
        if(event.getEntity() instanceof ArmorStand){
            ArmorStand armorStand = (ArmorStand) event.getEntity();
            if(armorStands.contains(armorStand)){
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onInteractWithArmorStand(EntityDamageEvent event){
        if(event.getEntity() instanceof ArmorStand){
            ArmorStand armorStand = (ArmorStand) event.getEntity();
            if(armorStands.contains(armorStand)){
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onInteractWithArmorStand(PlayerInteractAtEntityEvent event){
        if(event.getRightClicked() instanceof ArmorStand){
            ArmorStand armorStand = (ArmorStand) event.getRightClicked();
            if(armorStands.contains(armorStand)){
                System.out.println("Cancelled AT ArmorStand RIGHTCLICKED.");
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onClickInventory(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory == null)
            return;

        if (inventories.containsKey(inventory)) {
            Casier casier = inventories.get(inventory);
            ItemStack itemStack = event.getCurrentItem();

            if (itemStack == null) {
                event.setCancelled(true);
                return;
            }

            if (itemStack.getItemMeta() == null) {
                event.setCancelled(true);
                return;
            }

            if (itemStack.getItemMeta().getDisplayName() == null) {
                event.setCancelled(true);
                return;
            }


            if (!casier.itemStack.getItemMeta().getDisplayName().equals(itemStack.getItemMeta().getDisplayName())) {
                event.setCancelled(true);
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


        public void deploy(Location location) {
            as = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);


            as.setArms(true);

            as.setItemInHand(new ItemBuilder(Material.COMPASS).setName("casier").toItemStack());

            as.setGravity(false);

            as.setBasePlate(false);

            blocks.add(location.getBlock());
            location.getBlock().setType(Material.BARRIER);
            blocks.add(location.getBlock().getRelative(BlockFace.UP));
            location.getBlock().getRelative(BlockFace.UP).setType(Material.BARRIER);

            System.out.println("AS ! " + location);
        }

        public void setupInventory() {
            inventory = Bukkit.createInventory(null, 45, "Casier");
            int[] principalPos = {
                    getCase(1, 1), getCase(2, 1), getCase(1, 2),
                    getCase(8, 1), getCase(9, 1), getCase(9, 2),
                    getCase(1, 4), getCase(1, 5), getCase(2, 5),
                    getCase(8, 5), getCase(9, 5), getCase(9, 4)};

            int itemLoc = getCase(5, 3);
            inventory.setItem(itemLoc, itemStack);
            for (int i : principalPos) {
                inventory.setItem(i, border);
            }


        }


        public void setItem(ItemStack itemStack) {
            this.itemStack = itemStack;
        }

        //CALCULS
        public int getCase(int slot, int raw) {
            return (slot - 1) + ((raw - 1) * 9);
        }
    }


    public interface action {
        boolean a(Player player, Casier casier);
    }

}

