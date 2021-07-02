package be.alexandre01.inazuma_eleven.objects;

import be.alexandre01.inazuma.uhc.InazumaUHC;
import be.alexandre01.inazuma.uhc.presets.Preset;
import be.alexandre01.inazuma_eleven.InazumaEleven;
import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.object.schematic.Schematic;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.registry.WorldData;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

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
        if(!c.chunksByBiome.containsKey(Biome.PLAINS)){
            return spawnRandomized();
        }
        System.out.println("OK2");
        ArrayList<Chunk> chunks = new ArrayList<>(c.chunksByBiome.get(Biome.PLAINS));

        synchronized (chunks){
            if(chunks.isEmpty()){
                return spawnRandomized();
            }
            //chunks.removeIf(chunk -> Math.abs(chunk.getX()) < 300 && Math.abs(chunk.getX()) < 300 ||  Math.abs(chunk.getX()) > Preset.instance.p.getBorderSize(World.Environment.NORMAL)-30 && Math.abs(chunk.getX()) > Preset.instance.p.getBorderSize(World.Environment.NORMAL)-30 );
        }

        System.out.println("OK3");

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


            location.setY(location.getY()+40); //25
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

            //Selection selection = new CuboidSelection(p.getWorld(),loc, loc2);
            //CuboidSelection cuboidSelection =

          //  Operation o = editSession.commit();
            Operation operation = new ClipboardHolder(clipboard,world.getWorldData())
                    .createPaste(editSession,world.getWorldData())
                    .to(Vector.toBlockPoint(location.getX(),location.getY(),location.getZ()))
                    .ignoreAirBlocks(true)
                    .build();


            try { // This simply completes our paste and then cleans up.
                Operations.complete(operation);
                editSession.flushQueue();
                Location location = new Location(this.location.getWorld(),this.min.getBlockX()-52,clipboard.getMinimumPoint().getBlockY()+this.location.getY() - 24,this.min.getBlockZ()+43);
                System.out.println(location);

                location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
                location.getBlock().setType(Material.REDSTONE_BLOCK);

            } catch (WorldEditException e) { // If worldedit generated an exception it will go here
                for(Player player : Bukkit.getOnlinePlayers()){
                    player.sendMessage(ChatColor.RED + "OUPSI! La génération d'une structure s'est mal passé");
                }

                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }



        return false;
    }

    private boolean spawnRandomized(){

        return false;
    }
}
