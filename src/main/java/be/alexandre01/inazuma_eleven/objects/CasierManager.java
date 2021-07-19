package be.alexandre01.inazuma_eleven.objects;

import be.alexandre01.inazuma.uhc.InazumaUHC;
import be.alexandre01.inazuma.uhc.custom_events.player.PlayerDelayedMoveEvent;
import be.alexandre01.inazuma.uhc.presets.Preset;
import be.alexandre01.inazuma.uhc.roles.Role;
import be.alexandre01.inazuma.uhc.roles.RoleCategory;
import be.alexandre01.inazuma.uhc.roles.RoleItem;
import be.alexandre01.inazuma.uhc.utils.ItemBuilder;
import be.alexandre01.inazuma_eleven.categories.Alius;
import be.alexandre01.inazuma_eleven.categories.Raimon;
import be.alexandre01.inazuma_eleven.roles.solo.Byron;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_8_R3.event.CraftEventFactory;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

@Data
public class CasierManager implements Listener {

    private static CasierManager instance;
    private boolean isRegistered = false;
    private List<Casier> casiers = new ArrayList<>();
    private HashMap<String,Casier> itemstacksName = new HashMap<>();
    private List<ArmorStand> armorStands = new ArrayList<>();
    private HashMap<Inventory, Casier> inventories = new HashMap<>();
    private HashMap<Block, Casier> blocks = new HashMap<>();
    private LocalRaimon linkedLocalRaimon = null;
    private ArrayList<Player> playersTakeBook = new ArrayList<>();
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
        itemstacksName.put(casier.itemStack.getItemMeta().getDisplayName(),casier);
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
            Player player = (Player) event.getWhoClicked();

            if(playersTakeBook.contains(player)){
                player.sendMessage("§cVous avez déjà récupéré un livre.");
                event.setCancelled(true);
                return;
            }
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
                return;
            }

            player.closeInventory();
            casier.roleItem.getPlayersHaveItem().add(player);
            InazumaUHC.get.rm.getRole(player).getRoleItems().put(casier.itemStack.getItemMeta().getDisplayName(),casier.roleItem);
            player.getInventory().addItem(itemStack);
            player.updateInventory();
            casier.empty = true;

            Role role = InazumaUHC.get.rm.getRole(player);
            if(!(role instanceof Byron)){
                playersTakeBook.add(player);
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event){
        Item item = event.getItemDrop();
        if(item.getItemStack() != null){
            if(!item.getItemStack().hasItemMeta())
                return;
            if(!item.getItemStack().getItemMeta().hasDisplayName())
                return;
            String n = item.getItemStack().getItemMeta().getDisplayName();
            if(itemstacksName.containsKey(n)){

                new BukkitRunnable() {
                    int i = 0;
                    @Override
                    public void run() {
                        i += 5;
                        if(item.isDead()){
                            onItemComeback(n);
                            cancel();
                        }

                        if(i == 60){
                            item.remove();
                            onItemComeback(n);
                            cancel();
                        }

                    }
                }.runTaskTimer(InazumaUHC.get,20*5,20*5);

            }

        }

    }
    @EventHandler
    public void onPlayerDelayedMove(PlayerDelayedMoveEvent event){
        if(linkedLocalRaimon == null) return;
        if(linkedLocalRaimon.selection != null){
            Player player = event.getPlayer();
            if(linkedLocalRaimon.playersEnterring.contains(player)){
                return;
            }

            if(linkedLocalRaimon.selection.contains(player.getLocation())){
                linkedLocalRaimon.playersEnterring.add(player);
            }
        }
    }

    public void onItemComeback(String n){
        Casier casier = itemstacksName.get(n);
        casier.setupInventory();
        casier.empty = false;
        if(linkedLocalRaimon == null) return;
        for(Player player : linkedLocalRaimon.playersEnterring){
            player.sendMessage(Preset.instance.p.prefixName()+ "Un manuel est retourné dans son casier.");
        }
    }

    public static class Casier {

        public ArrayList<Class<?>> roleAccess = new ArrayList<>();

        @Getter @Setter private action action = new action() {
            @Override
            public boolean a(Player player, Casier casier) {
                casier.open(player);
                return true;
            }
        };
        @Getter private rightClick rightClick = new rightClick() {
            @Override
            public boolean a(Player player, Casier casier) {
                System.out.println("OLD");
                return true;
            }
        };
        public boolean empty = false;
        public Class<?>[] canUse;
        public RoleItem roleItem;
        public ItemStack itemStack;
        public ItemStack border;
        public Inventory inventory;
        public ArmorStand as;
        public ItemStack armorStandItem = new ItemBuilder(Material.COMPASS).setName("casier").toItemStack();
        public ArrayList<Block> blocks = new ArrayList<>();

        public Casier(Location location) {
            as = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);


            as.setArms(true);

            as.setItemInHand(armorStandItem);
            as.setGravity(false);
            as.setBasePlate(false);

            blocks.add(location.getBlock());
            location.getBlock().setType(Material.BARRIER);
            blocks.add(location.getBlock().getRelative(BlockFace.UP));
            location.getBlock().getRelative(BlockFace.UP).setType(Material.BARRIER);

            roleItem = new RoleItem();
        }

        public Casier() {
            roleItem = new RoleItem();

        }

        public void open(Player player){
            if(!empty){
                player.openInventory(inventory);
            }else {
                player.sendMessage(Preset.instance.p.prefixName()+"§c Le casier est vide !");
            }
        }

        public void deploy(Location location) {
            as = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);


            as.setArms(true);

            as.setItemInHand(armorStandItem);

            as.setGravity(false);

            as.setBasePlate(false);

            blocks.add(location.getBlock());
            location.getBlock().setType(Material.BARRIER);
            blocks.add(location.getBlock().getRelative(BlockFace.UP));
            location.getBlock().getRelative(BlockFace.UP).setType(Material.BARRIER);
            List<Class<?>> all = InazumaUHC.get.rm.getRoleCategory(Raimon.class).getRoles().stream().map(role -> role.getClass()).collect(Collectors.toList());
            all.addAll(InazumaUHC.get.rm.getRoleCategory(Alius.class).getRoles().stream().map(role -> role.getClass()).collect(Collectors.toList()));

            roleItem.setRolesToAccess(new ArrayList<>(all));
            if(canUse == null){
                List<Class<?>> c = InazumaUHC.get.rm.getRoleCategory(Raimon.class).getRoles().stream().map(role -> role.getClass()).collect(Collectors.toList());
                roleAccess = new ArrayList<>(c);
            }else {
                roleAccess = new ArrayList<Class<?>>(Arrays.asList(canUse));
            }

            roleItem.setDroppableItem(true);

            roleItem.deployVerificationsOnRightClick(new ArrayList<>(Collections.singletonList(new RoleItem.VerificationGeneration() {
                @Override
                public boolean verification(Player player) {
                    if(roleAccess.contains(InazumaUHC.get.rm.getRole(player).getClass())){
                        return true;
                    }
                    player.sendMessage(Preset.instance.p.prefixName()+" Vous n'avez pas le droit d'utiliser cet item.");
                    return false;
                }
            })));
            roleItem.setRightClick(new RoleItem.RightClick() {
                @Override
                public void execute(Player player) {
                    if(!rightClick.a(player,Casier.this)) return;

                    Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(InazumaUHC.get, new Runnable() {
                        public void run() {
                                player.setItemInHand(new ItemStack(Material.AIR));
                        }
                    }, 1L);
                }
            });

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

        public void setItemStack(ItemStack itemStack){
            this.itemStack = itemStack;
            roleItem.setItemstack(itemStack);
        }

        //CALCULS
        public int getCase(int slot, int raw) {
            return (slot - 1) + ((raw - 1) * 9);
        }

        public void setRightClick(rightClick rightClick){
            System.out.println("Set rightclick");
            this.rightClick = rightClick;
        }
    }

    public interface action {
        boolean a(Player player, Casier casier);
    }
    public interface rightClick {
        boolean a(Player player, Casier casier);
    }

}

