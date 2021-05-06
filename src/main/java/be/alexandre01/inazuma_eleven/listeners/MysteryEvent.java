package be.alexandre01.inazuma_eleven.listeners;

import be.alexandre01.inazuma.uhc.InazumaUHC;
import be.alexandre01.inazuma.uhc.presets.Preset;
import be.alexandre01.inazuma.uhc.utils.LocationUtils;
import be.alexandre01.inazuma.uhc.utils.NPC;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class MysteryEvent implements Listener{
    public MysteryEvent(){
        int r = new Random().nextInt( Preset.instance.p.getBordureTime() + 1 - Preset.instance.p.getBordureTime()/3) +( Preset.instance.p.getBordureTime()/3);

        new BukkitRunnable() {
            @Override
            public void run() {
                if(InazumaUHC.get.getRemainingPlayers() == null)
                    return;
                if(InazumaUHC.get.getRemainingPlayers().isEmpty())
                    return;

                Player player = InazumaUHC.get.getRemainingPlayers().get(new Random().nextInt( InazumaUHC.get.getRemainingPlayers().size()));

                if(player == null)
                    return;
                Location location = player.getLocation();
                location.setYaw(location.getYaw()/1.4f);
                location.setPitch(location.getPitch()/1.4f);
                Location vloc = location.getDirection().multiply(-1).multiply(10).toLocation(player.getWorld());

                Location floc = LocationUtils.getTop(vloc);
                NPC npc = new NPC(player,"¿¿?",floc);
                String tex = "ewogICJ0aW1lc3RhbXAiIDogMTYyMDEyMTg0NDgxNywKICAicHJvZmlsZUlkIiA6ICIzZjM4YmViZGYwMWQ0MjNkYWI4MjczZjUwNGFiNGEyNyIsCiAgInByb2ZpbGVOYW1lIiA6ICJjazM0Nzk0MjM1NzUzNzMxIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzk2ZTM2ZTVhOGVlODNjYTZlOThiOWMwMWQ3MTU4YjAxMjg4MGI0ZmQ4MTg4MzY4MTI4MzJkODk4NTg4YzM1NDMiCiAgICB9CiAgfQp9";
                String sig = "MJKxQzVO4R++YNxCpzB41AOH+Ctc+Q6+n8lk8Q2+RH7od3OrC/7R1llYkgvLgIYEPm/MRimVs73AcMtifqJfPQ7l6348NTpfTR+UpxgfqGpMLeY+ZbQSUWmdIE0LCfl9I8oYFOHOHv398UM4cfha1klyHtdfMUnKmavT6gMY73oZc1mSp+TKLinmqIATqfHsGUBshr+7LHC92Lf4GYoN2eFA1dqZv/v3jmV4Spm9CsepUTdjM9l+0NlOFOuqdulAH/zD+xETAxoLFl6uiH9qCuUHOjNXcK7QyRrKkZpuutbDm/AvaDKP3ggNczuNskmTA+av3eyTp6sGEntemmot3jr0s4FXGvNfCMkPeuN2B3Vz6T1bKbsO0vg0KlbnjwX8hgap17/WhFviQ0ukq9xnqNsH+GVEyuVJMDgLbreUzHV+7K2l1MFt1dwyJRTXTKyCPUibpcVps66RIyWvq9dHQElDBmZcu6FoB6mh6QKwZlL0ruCv9NPPMoV9FkvFghDBwd3dNCNUfOtc7fi8cO6JlLO0XIibOWsL/OUQF2dBrENKJY+nwOyXDWvpebn0mlh14VohMR/4s+ZT5TUMXmpj3F0D8nE72bIx/qOkiNtxNOwikIHVCb6WCy1i2++ctjkyO7zDIf/eteY1HOD3uKpE3q6G6+rx2k8z6XS3jKEngJM=";
                npc.changeSkin(tex,sig);

                npc.spawn();
                npc.rotateToPlayer(player,floc);

                npc.setOnFire();
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        npc.destroy();
                    }
                }.runTaskLaterAsynchronously(InazumaUHC.get,20*3);
            }
        }.runTaskLater(InazumaUHC.get,r*20L);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        if(event.getMessage().contains("666")){
            event.getPlayer().sendMessage("§cJe pense que tu devrais éviter de dire cela. Il t'observe de loin.");
        }
    }



}
