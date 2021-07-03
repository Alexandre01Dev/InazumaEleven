package be.alexandre01.inazuma_eleven.objects;

import be.alexandre01.inazuma.uhc.InazumaUHC;
import be.alexandre01.inazuma.uhc.presets.Preset;
import be.alexandre01.inazuma.uhc.utils.ItemBuilder;
import be.alexandre01.inazuma_eleven.InazumaEleven;
import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.object.schematic.Schematic;
import com.sk89q.worldedit.*;
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
import org.checkerframework.checker.units.qual.C;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class LocalRaimon {
    public int x;
    public int z;
    public Location min;
    public Location location;
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

            chunks.removeIf(chunk -> Math.abs(chunk.getX()*16) > 300 && Math.abs(chunk.getZ()*16) > 300 ||  Math.abs(chunk.getX()) > Preset.instance.p.getBorderSize(org.bukkit.World.Environment.NORMAL)-50 && Math.abs(chunk.getX()) > Preset.instance.p.getBorderSize(org.bukkit.World.Environment.NORMAL)-50 );


        System.out.println("OK3");
        Collections.shuffle(chunks);
       Chunk chunk = chunks.get(0);


        ClipboardFormat clipboardFormat = ClipboardFormat.findByFile(new File(InazumaUHC.get.getDataFolder().getAbsolutePath()+"/schematics/fawetest.fawe"));
        try {
            Schematic schematic = clipboardFormat.load(new File(InazumaUHC.get.getDataFolder().getAbsolutePath()+"/schematics/fawetest.fawe"));
            World world  =FaweAPI.getWorld(InazumaUHC.get.worldGen.defaultWorld.getName()) ;
            clipboard = schematic.getClipboard();



            location = new Location(chunk.getWorld(),chunk.getX()*16, 255, chunk.getZ()*16);
            synchronized (location){
                for (int i = 255; i > 0 ; i--) {
                    location.setY(i);
                    if(location.getBlock().getType()!= Material.AIR){
                        location.setY(i+1);
                        break;
                    }
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

            Selection selection = new CuboidSelection(location.getWorld(),getLocationFromSchematic(22,0,-10), getLocationFromSchematic(-4,18,12));

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
                Location location = new Location(this.location.getWorld(),this.min.getBlockX()+1.5d,clipboard.getMinimumPoint().getBlockY()+this.location.getY() + 0,this.min.getBlockZ()-5.5d);
                System.out.println(location);


                CasierManager.init();
                CasierManager casierManager = CasierManager.get();

                CasierManager.Casier force = new CasierManager.Casier();
                force.itemStack = new ItemBuilder(Material.BOOK).setName("§7§lManuel de §4§lForce").toItemStack();
                force.border = new ItemBuilder(Material.STAINED_GLASS_PANE).setWoolColor(DyeColor.RED).toItemStack();
                force.setupInventory();

                CasierManager.Casier vitesse = new CasierManager.Casier();
                vitesse.itemStack = new ItemBuilder(Material.BOOK).setName("§7§lManuel de §b§lVitesse").toItemStack();
                vitesse.border = new ItemBuilder(Material.STAINED_GLASS_PANE).setWoolColor(DyeColor.CYAN).toItemStack();
                vitesse.setupInventory();

                CasierManager.Casier resistance = new CasierManager.Casier();
                resistance.itemStack = new ItemBuilder(Material.BOOK).setName("§7§lManuel de §8§lRésistance").toItemStack();
                resistance.border = new ItemBuilder(Material.STAINED_GLASS_PANE).setWoolColor(DyeColor.GRAY).toItemStack();
                resistance.setupInventory();

                CasierManager.Casier vie = new CasierManager.Casier();
                vie.itemStack = new ItemBuilder(Material.BOOK).setName("§7§lManuel de §d§lVie").toItemStack();
                vie.border = new ItemBuilder(Material.STAINED_GLASS_PANE).setWoolColor(DyeColor.PINK).toItemStack();
                vie.setupInventory();

                CasierManager.Casier mark = new CasierManager.Casier();
                mark.itemStack = new ItemBuilder(Material.BOOK).setName("§6§lCahier de §7§lDavid §lEvans").toItemStack();
                mark.border = new ItemBuilder(Material.STAINED_GLASS_PANE).setWoolColor(DyeColor.ORANGE).toItemStack();
                mark.setupInventory();

                ArrayList<CasierManager.Casier> casiers = new ArrayList<>(Arrays.asList(force, vitesse , resistance, vie, mark));

                Collections.shuffle(casiers);

                for (int i = 0; i < casiers.size(); i++) {
                    CasierManager.Casier casier = casiers.get(i);
                    casier.deploy(location);
                    casierManager.addCasier(casier);
                    location.add(1,0,0);
                }

                Location l = getLocationFromSchematic(6,2,6);
                Painting painting = (Painting) l.getWorld().spawnEntity(l,EntityType.PAINTING);
                painting.setFacingDirection(BlockFace.SOUTH);
                painting.setArt(Art.BURNINGSKULL);
                l.getBlock().setType(Material.PAINTING);

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
}
