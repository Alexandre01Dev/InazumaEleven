package be.alexandre01.inazuma_eleven.commands;

import be.alexandre01.inazuma_eleven.objects.LocalRaimon;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class Structure extends Command {
    public Structure(String s) {
        super(s);
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        LocalRaimon localRaimon = new LocalRaimon();
        if(localRaimon.spawn()){
          commandSender.sendMessage("Le local a spawn");
        }else {
            commandSender.sendMessage("Le local n'a pas spawn");
        }
        return false;
    }
}
