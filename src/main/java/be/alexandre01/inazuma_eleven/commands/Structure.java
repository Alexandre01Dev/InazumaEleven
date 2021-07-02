package be.alexandre01.inazuma_eleven.commands;

import be.alexandre01.inazuma_eleven.objects.LocalRaimon;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Structure extends Command {
    LocalRaimon localRaimon;
    public Structure(String s) {
        super(s);
    }

    @Override
    public boolean execute(CommandSender sender, String msg, String[] args) {


        if(args.length == 0){
            localRaimon = new LocalRaimon();
            System.out.println("Et ??");
            if(localRaimon.spawn()){
                sender.sendMessage("Le local a spawn");
            }else {
                sender.sendMessage("Le local n'a pas spawn");
            }
            return true;
        }

        if(args[0].equalsIgnoreCase("getPos")){
            Player player = (Player) sender;
            System.out.println( localRaimon.clipboard.getMinimumPoint().getBlockX());
            System.out.println( localRaimon.clipboard.getMinimumPoint().getBlockX());
            System.out.println(player.getLocation().getBlockX());
            System.out.println(localRaimon.location.getBlockX());
            int x = localRaimon.clipboard.getRegion().getMinimumPoint().getBlockX() + (player.getLocation().getBlockX()-localRaimon.location.getBlockX());
            int y = localRaimon.clipboard.getRegion().getMinimumPoint().getBlockY() +  (player.getLocation().getBlockY()-localRaimon.location.getBlockY());
            int z = localRaimon.clipboard.getRegion().getMinimumPoint().getBlockZ() +  (player.getLocation().getBlockZ()-localRaimon.location.getBlockZ());
            player.sendMessage("GetFromYourLoc:");
            player.sendMessage("X: "+ x);
            player.sendMessage("Y: "+ y);
            player.sendMessage("Z: "+ z);

        }


        return false;
    }
}
