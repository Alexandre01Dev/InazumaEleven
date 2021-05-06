package be.alexandre01.inazuma_eleven.objects;

import org.bukkit.Chunk;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;

public class ChunksData {
    private static ChunksData instance;
    public static void init(){
        instance = new ChunksData();
    }
    public static ChunksData get(){
        return instance;
    }

    public HashMap<Biome, ArrayList<Chunk>> chunksByBiome;

    public ChunksData(){
        chunksByBiome = new HashMap<>();
    }

    public void addChunk(Chunk chunk){
        Biome biome = chunk.getWorld().getBiome(chunk.getX()*16,chunk.getZ()*16);
        if(!chunksByBiome.containsKey(biome)){
            chunksByBiome.put(biome,new ArrayList<>());
        }

        ArrayList<Chunk> c = chunksByBiome.get(biome);
        c.add(chunk);

        chunksByBiome.put(biome, c);
    }
}
