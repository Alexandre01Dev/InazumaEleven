package be.alexandre01.inazuma_eleven.commands;

import be.alexandre01.inazuma.uhc.InazumaUHC;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

public class Particles extends Command {
    private HashMap<Player, BukkitTask> bukkitTasks = new HashMap<>();
    public Particles(String particles) {
        super(particles);
    }

    @Override
    public boolean execute(CommandSender sender, String msg, String[] args) {
        if(!sender.hasPermission("particules.admin")){
            return false;
        }
        if(sender instanceof Player){
            Player player = (Player) sender;
            BukkitTask b = null;
            if(args[0].equalsIgnoreCase("stop")){
                bukkitTasks.get(player).cancel();
                bukkitTasks.remove(player);
            }

            if(args[0].equalsIgnoreCase("1")){
                 b = new BukkitRunnable() {
                    @Override
                    public void run() {
                        Location location = player.getLocation().clone();
                        for (int degree = 0; degree < 360; degree++) {
                            double radians = Math.toRadians(degree);
                            double x = Math.cos(radians);
                            double z = Math.sin(radians);
                            location.add(x,0,z);
                            location.getWorld().spigot().playEffect(location, Effect.FLAME, 0,0,0f,0f,0f,0f,1,1);
                            location.subtract(x,0,z);
                        }
                    }
                }.runTaskTimer(InazumaUHC.get,20,20);

            }
            if(args[0].equalsIgnoreCase("2")){
                b = new BukkitRunnable() {
                    int x = 0;
                    int z = 0;
                    double y = 0;
                    boolean reverseY = false;
                    boolean mix = false;
                    @Override
                    public void run() {
                        Location location = player.getLocation().clone();
                        x+=1;
                        z+=1;

                        y+= 0.1d;

                        System.out.println(y);
                        if(y > 4.5){
                            reverseY = true;
                        }

                        if(y <= 0){
                            reverseY = false;
                        }

                        for (int i = 0; i < 10; i++) {
                            double radiansX = Math.toRadians(x+(1*i));
                            double radiansZ = Math.toRadians(z+(1*i));
                            double x = Math.cos(radiansX);
                            double z = Math.sin(radiansZ);
                            double y = this.y+(0.1*i);
                            location.add(x,y,z);
                            if(mix){
                                location.getWorld().spigot().playEffect(location, Effect.FLAME, 0,0,0f,0f,0f,0f,1,1);
                                mix = false;
                            }else {
                                location.getWorld().spigot().playEffect(location, Effect.WATERDRIP, 0,0,0f,0f,0f,0f,1,1);
                                mix = true;
                            }
                            location.subtract(x,y,z);
                        }


                    }
                }.runTaskTimer(InazumaUHC.get,1,1);

            }
            if(b != null)
                bukkitTasks.put(player,b);
        }
        return false;
    }
}
