package be.alexandre01.inazuma_eleven.objects;

import be.alexandre01.inazuma.uhc.InazumaUHC;
import be.alexandre01.inazuma.uhc.custom_events.player.PlayerDelayedMoveEvent;
import be.alexandre01.inazuma.uhc.managers.damage.DamageManager;
import be.alexandre01.inazuma.uhc.presets.Preset;
import be.alexandre01.inazuma.uhc.roles.RoleItem;
import be.alexandre01.inazuma.uhc.utils.ItemBuilder;
import be.alexandre01.inazuma.uhc.utils.WeaponItem;
import be.alexandre01.inazuma.uhc.worlds.utils.Cuboid;
import be.alexandre01.inazuma_eleven.InazumaEleven;
import be.alexandre01.inazuma_eleven.roles.raimon.Mark;
import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.object.schematic.Schematic;
import com.sk89q.worldedit.*;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.blocks.BlockType;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.registry.WorldData;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.units.qual.C;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.*;

public class LocalRaimon implements Listener {
    public int x;
    public int z;
    Selection selection;
    public Location min;
    public Location location;
    public ArrayList<Player> playersEnterring = new ArrayList<>();
    public Location max;
    public Clipboard clipboard;
    public boolean spawn(){
        System.out.println("Dacodac");
        ChunksData c = ChunksData.get();
        System.out.println("OK1");
        System.out.println("BIOMES "+ c.chunksByBiome.entrySet());

        if(!c.chunksByBiome.containsKey(Biome.PLAINS)){
            return spawnRandomized();
        }
        System.out.println("OK2");
        ArrayList<Chunk> chunks = new ArrayList<>(c.chunksByBiome.get(Biome.PLAINS));


            if(chunks.isEmpty()){
                return spawnRandomized();
            }

            chunks.removeIf(chunk -> (Math.abs(chunk.getX()*16) > 300 && Math.abs(chunk.getZ()*16) > 300) ||  Math.abs(chunk.getX()) > Preset.instance.p.getBorderSize(org.bukkit.World.Environment.NORMAL)-50 && Math.abs(chunk.getX()) > Preset.instance.p.getBorderSize(org.bukkit.World.Environment.NORMAL)-50 );


        System.out.println("OK3");
        Collections.shuffle(chunks);
       Chunk chunk = chunks.get(0);

        location = new Location(chunk.getWorld(),chunk.getX()*16, 255, chunk.getZ()*16);

        return spawn(location);
    }

    public boolean spawn(Location location){
        this.location = location;
        ClipboardFormat clipboardFormat = ClipboardFormat.findByFile(new File(InazumaUHC.get.getDataFolder().getAbsolutePath()+"/schematics/fawetest.fawe"));
        try {
            Schematic schematic = clipboardFormat.load(new File(InazumaUHC.get.getDataFolder().getAbsolutePath()+"/schematics/fawetest.fawe"));
            World world  =FaweAPI.getWorld(InazumaUHC.get.worldGen.defaultWorld.getName()) ;
            clipboard = schematic.getClipboard();





                for (int i = 255; i > 0 ; i--) {
                    location.setY(i);
                    if(location.getBlock().getType()!= Material.AIR){
                        location.setY(i+1);
                        break;
                    }
                }

            System.out.println("Dimension "+clipboard.getDimensions().getY());
            System.out.println("Dimension X "+clipboard.getDimensions().getX());
            System.out.println("Dimension Z "+clipboard.getDimensions().getZ());
            System.out.println("Reg MaxP X"+clipboard.getRegion().getMaximumPoint().getX());
            System.out.println("Reg MaxP Z"+clipboard.getRegion().getMaximumPoint().getZ());
            System.out.println("Reg MinP Z"+clipboard.getRegion().getMinimumPoint().getZ());


            location.setY(location.getY()+10); //25
            //EditSession editSession = schematic.paste(world,Vector.toBlockPoint(location.getX(),location.getY(),location.getZ()));
            EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1);
            System.out.println(editSession.getMaxY());


            //  editSession.setSize(9999);

            System.out.println(location.getX()+"|"+location.getY()+"|"+location.getZ());
            x = location.getBlockX();
            z = location.getBlockZ();

            min = new Location(location.getWorld(),clipboard.getMinimumPoint().getBlockX() + location.getX(),clipboard.getMaximumPoint().getBlockY()+ location.getY() - 64 ,clipboard.getMinimumPoint().getBlockZ() + location.getZ());
            max = new Location(location.getWorld(),clipboard.getMaximumPoint().getBlockX() + location.getX(),clipboard.getMaximumPoint().getBlockY()+ location.getY(),clipboard.getMaximumPoint().getBlockZ() + location.getZ());
            double centerX = (1/2) * (min.getX() + max.getX());
            double centerZ = (1/2) * (min.getZ() + max.getZ());

            System.out.println("Center X "+ centerX);
            System.out.println("Center Z "+ centerZ);

            System.out.println(min);
            System.out.println(max);

             selection = new CuboidSelection(location.getWorld(),getLocationFromSchematic(22,0,-10), getLocationFromSchematic(-4,18,12));

            CuboidSelection cuboidSelection = (CuboidSelection) selection;
            Region region = cuboidSelection.getRegionSelector().getRegion();
            editSession.setBlocks(region,new BaseBlock(0, 0));
            editSession.flushQueue();

            editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(world, -1);


            //  Operation o = editSession.commit();
            Operation operation = new ClipboardHolder(clipboard,world.getWorldData())
                    .createPaste(editSession,world.getWorldData())
                    .to(Vector.toBlockPoint(location.getX(),location.getY(),location.getZ()))
                    .ignoreAirBlocks(true)
                    .build();


            try { // This simply completes our paste and then cleans up.
                Operations.complete(operation);
                editSession.flushQueue();
                Location locationD = new Location(this.location.getWorld(),this.min.getBlockX()+1.5d,clipboard.getMinimumPoint().getBlockY()+this.location.getY() + 0,this.min.getBlockZ()-5.5d);
                System.out.println(locationD);


                CasierManager.init();
                CasierManager casierManager = CasierManager.get();
                casierManager.setLinkedLocalRaimon(this);

                System.out.println("Livres");
                CasierManager.Casier force = new CasierManager.Casier();
                force.setItemStack(new ItemBuilder(Material.BOOK).setName("§7§lManuel de §4§lForce").toItemStack());
                force.armorStandItem = new ItemBuilder(Material.SPONGE).setName("§cRouge").toItemStack();
                force.border = new ItemBuilder(Material.STAINED_GLASS_PANE).setWoolColor(DyeColor.RED).toItemStack();
                force.setupInventory();
                System.out.println("Rightclick ?");
                force.setRightClick((player, casier) -> {
                    if(!player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE))
                        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,Integer.MAX_VALUE, 0,false,false), true);
                    HashMap<Player, Double> increased_damage = InazumaUHC.get.dm.getIncreased_damage();
                    if(increased_damage.containsKey(player)){
                        InazumaUHC.get.dm.addEffectPourcentage(player, DamageManager.EffectType.INCREASE_DAMAGE,1, (int) (((increased_damage.get(player)*100))+5));
                    }else {
                        InazumaUHC.get.dm.addEffectPourcentage(player, DamageManager.EffectType.INCREASE_DAMAGE,1,105);
                    }
                    return true;
                });
                CasierManager.Casier vitesse = new CasierManager.Casier();
                vitesse.setItemStack( new ItemBuilder(Material.BOOK).setName("§7§lManuel de §b§lVitesse").toItemStack());
                vitesse.border = new ItemBuilder(Material.STAINED_GLASS_PANE).setWoolColor(DyeColor.CYAN).toItemStack();
                vitesse.armorStandItem = new ItemBuilder(Material.SPONGE).setName("§bBleu").toItemStack();
                vitesse.setupInventory();

                vitesse.setRightClick((player, casier) -> {
                    System.out.println("Click wow");
                    player.setWalkSpeed(player.getWalkSpeed()+0.255F);
                    return true;
                });

                CasierManager.Casier resistance = new CasierManager.Casier();
                resistance.setItemStack(new ItemBuilder(Material.BOOK).setName("§7§lManuel de §8§lRésistance").toItemStack());
                resistance.border = new ItemBuilder(Material.STAINED_GLASS_PANE).setWoolColor(DyeColor.GRAY).toItemStack();
                resistance.setupInventory();
                resistance.armorStandItem = new ItemBuilder(Material.SPONGE).setName("§7Gris").toItemStack();
                resistance.setRightClick((player, casier) -> {
                    System.out.println("Click wow");
                    HashMap<Player, Double> resistanceH =InazumaUHC.get.dm.getResistance();
                    if(!player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE))
                        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,Integer.MAX_VALUE, 0,false,false), true);
                    if(resistanceH.containsKey(player)){
                        InazumaUHC.get.dm.addEffectPourcentage(player, DamageManager.EffectType.INCREASE_DAMAGE,1, (int) (((resistanceH.get(player)*100))+5));
                    }else {
                        InazumaUHC.get.dm.addEffectPourcentage(player, DamageManager.EffectType.INCREASE_DAMAGE,1,105);
                    }
                    return true;
                });

                CasierManager.Casier vie = new CasierManager.Casier();
                vie.setItemStack(new ItemBuilder(Material.BOOK).setName("§7§lManuel de §d§lVie").toItemStack());
                vie.border = new ItemBuilder(Material.STAINED_GLASS_PANE).setWoolColor(DyeColor.PINK).toItemStack();
                vie.armorStandItem = new ItemBuilder(Material.SPONGE).setName("§dRose").toItemStack();
                vie.setupInventory();
                vie.setRightClick((player, casier) -> {
                    player.setMaxHealth(player.getMaxHealth()+2);
                    return true;
                });
                CasierManager.Casier mark = new CasierManager.Casier();

                mark.setItemStack(new ItemBuilder(Material.BOOK).setName("§6§lCahier de §7§lDavid §lEvans").toItemStack());
                mark.border = new ItemBuilder(Material.STAINED_GLASS_PANE).setWoolColor(DyeColor.ORANGE).toItemStack();
                mark.armorStandItem = new ItemBuilder(Material.SPONGE).setName("§6Orange").toItemStack();

                mark.canUse = new Class[]{Mark.class};
                mark.setupInventory();
                mark.setRightClick((player, casier) -> {
                    casier.roleItem.updateItem(new ItemStack(Material.AIR));
                    player.updateInventory();
                    Mark m = (Mark) InazumaUHC.get.rm.getRole(player);
                    m.multiplicateur += 0.25f;
                    player.sendMessage(Preset.instance.p.prefixName()+" §7§lFélicitation, vous avez trouvé le §6§lCahier §7de §7§lDavis Evans, désormais tout vos points gagné serront §c§lmultiplié §7par §c§l125% !");
                    player.playSound(player.getLocation(), Sound.ORB_PICKUP, 5,5);
                    return true;
                });
                mark.setAction((player, casier) -> {
                    ItemStack itemStack = player.getItemInHand();
                    final String s = Preset.instance.p.prefixName()+"ce casier se trouve cadenacé, vous ne pouvez pas l'ouvrir, il vous faut la clef.";
                    if(itemStack == null){
                        player.sendMessage(s);
                        return false;
                    }

                    if(!itemStack.hasItemMeta()){
                        player.sendMessage(s);
                        return false;
                    }

                    if(itemStack.getItemMeta().getDisplayName() == null){
                        player.sendMessage(s);
                        return false;
                    }

                    if(!player.getItemInHand().getItemMeta().getDisplayName().equals("§eClef du casier")){
                        player.sendMessage(s);
                        return false;
                    }



                    final double x = player.getLocation().getBlockX();
                    final double y = player.getLocation().getBlockY();
                    final double z = player.getLocation().getBlockZ();
                    final float yaw = player.getLocation().getYaw();
                    final float pitch = player.getLocation().getPitch();

                    player.sendMessage(Preset.instance.p.prefixName()+" §aOuverture du casier dans 5 secondes...");
                    new BukkitRunnable() {
                        int i = 0;
                        @Override
                        public void run() {
                            player.getWorld().playSound(player.getLocation(),Sound.STEP_LADDER,1.5f,0.2f);
                            if(player.getLocation().getBlockX() != x || player.getLocation().getBlockY() != y || player.getLocation().getBlockZ() != z
                                    || Math.abs(player.getLocation().getYaw()-yaw) > 60 || Math.abs(player.getLocation().getPitch()-pitch) > 60){

                                player.sendMessage(Preset.instance.p.prefixName()+" §cVous avez trop bougé, vous n'arrivez pas à bien insérer la clef. Veuillez réessayer.");
                                cancel();
                            }

                            if(i == 5){
                                casier.open(player);
                                cancel();
                            }
                            i++;
                        }
                    }.runTaskTimer(InazumaUHC.get,20,20);
                    return true;
                });
                ArrayList<CasierManager.Casier> casiers = new ArrayList<>(Arrays.asList(force, vitesse , resistance, vie, mark));

                Collections.shuffle(casiers);

                casierManager.setLinkedLocalRaimon(this);
                for (int i = 0; i < casiers.size(); i++) {
                    CasierManager.Casier casier = casiers.get(i);
                    casier.deploy(locationD);
                    casierManager.addCasier(casier);
                    locationD.add(1,0,0);
                }

                Location l = getLocationFromSchematic(6,2,6);
                Painting painting = (Painting) l.getWorld().spawnEntity(l,EntityType.PAINTING);
                painting.setFacingDirection(BlockFace.SOUTH);
                painting.setArt(Art.BURNINGSKULL);
                l.getBlock().setType(Material.PAINTING);


                Cuboid cuboid = new Cuboid(getLocationFromSchematic(-30,-16,33),getLocationFromSchematic(40,28,-33));

                for(Player player : Bukkit.getOnlinePlayers()){
                    if(cuboid.contains(player.getLocation())){
                        player.teleport(getLocationFromSchematic(10,0,17,-179f,2.2f));
                        player.sendMessage("§cComme la structure vient d'apparaitre sur vous, vous venez d'être téléporté à l'entrée de celle-ci !");

                    }
                }
                return true;
            } catch (WorldEditException e) { // If worldedit generated an exception it will go here
                for(Player player : Bukkit.getOnlinePlayers()){
                    player.sendMessage(ChatColor.RED + "OUPSI! La génération d'une structure s'est mal passé");
                }

                e.printStackTrace();
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IncompleteRegionException e) {
            e.printStackTrace();
        }


        return false;
    }

    private boolean spawnRandomized(){

        return false;
    }

    public Location getLocationFromSchematic(double x, double y, double z){
       return new Location(this.location.getWorld(),this.min.getBlockX()+x,clipboard.getMinimumPoint().getBlockY()+this.location.getY() + y,this.min.getBlockZ() + z);
    }
    public Location getLocationFromSchematic(double x, double y, double z,float yaw,float pitch){
        return new Location(this.location.getWorld(),this.min.getBlockX()+x,clipboard.getMinimumPoint().getBlockY()+this.location.getY() + y,this.min.getBlockZ() + z,yaw,pitch);
    }

    @EventHandler
    public void onMobSpawnOnStructure(CreatureSpawnEvent event){

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
}
