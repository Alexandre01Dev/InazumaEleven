package be.alexandre01.inazuma_eleven.listeners;

import be.alexandre01.inazuma_eleven.objects.ChunksData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

public class ChunkSaver implements Listener {
    ChunksData chunksData;
    public ChunkSaver(){
        ChunksData.init();
        chunksData = ChunksData.get();
    }

    @EventHandler
    public void onChunk(ChunkLoadEvent event){
        if(event.isNewChunk()){
            chunksData.addChunk(event.getChunk());
        }
    }
}
