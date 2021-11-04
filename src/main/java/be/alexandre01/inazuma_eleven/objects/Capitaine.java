package be.alexandre01.inazuma_eleven.objects;

import be.alexandre01.inazuma.uhc.InazumaUHC;
import be.alexandre01.inazuma.uhc.custom_events.player.PlayerInstantDeathEvent;
import be.alexandre01.inazuma.uhc.presets.Preset;
import be.alexandre01.inazuma.uhc.roles.Role;
import be.alexandre01.inazuma_eleven.categories.Alius;
import be.alexandre01.inazuma_eleven.roles.alius.*;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Capitaine implements Listener{

    @Getter
    private static Capitaine instance;

    public ArrayList<Class<?>> capitaineToSpoil= new ArrayList<>();
    public HashMap<Class<?>, Class<?>> linkedCapitaine = new HashMap<>();

    public ArrayList<Role> capitaineList = new ArrayList<>();

    public HashMap<Role, String[]> mdCommand = new HashMap<>();

    public static void init()
    {
        instance = new Capitaine();
    }

    public void giveMdCommand(){
        try {
                InazumaUHC.get.registerCommand("cdm", new Command("cdm") {
                    @Override
                    public boolean execute(CommandSender sender, String msg, String[] args) {
                        if(!(sender instanceof Player))return false;

                        Player player = (Player) sender;
                        Role r = InazumaUHC.get.rm.getRole(player);
                        if(r != null){
                            if(capitaineList.contains(r)){
                                if(args.length == 0){
                                    player.sendMessage(Preset.instance.p.prefixName() + " Veuillez mettre un message après la commande.");
                                    return false;
                                }
                                player.sendMessage(Preset.instance.p.prefixName() + " §aMessage enregistré !");
                                mdCommand.put(InazumaUHC.get.rm.getRole(player), args);
                            }else {
                                player.sendMessage("§cVous n'êtes pas capitaine et vous ne pouvez donc éxecuter cette commande !");
                            }
                        }
                        return true;
                    }
                });

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    void onDeath(PlayerInstantDeathEvent event){
        if(capitaineList.contains(InazumaUHC.get.rm.getRole(event.getPlayer()))){
            capitaineList.remove(InazumaUHC.get.rm.getRole(event.getPlayer()));
        }
        if(mdCommand.containsKey(InazumaUHC.get.rm.getRole(event.getPlayer()))){

            ArrayList<Player> players = new ArrayList<>();

            for (Role role : capitaineList){
                players.addAll(role.getPlayers());
            }

            for (Player player : players){
                player.sendMessage(mdCommand.get(InazumaUHC.get.rm.getRole(event.getPlayer())));
            }
        }
    }



    public static void addCapitaine(Class<?>... roles)
    {
        Capitaine capitaine = instance;
        capitaine.capitaineToSpoil.addAll(Arrays.asList(roles));
        for (Class<?> role : roles){
            capitaine.capitaineList.add(InazumaUHC.get.rm.getRole(role));
        }
    }


    public static Class<?> giveCapitaine(Class<?> role, Class<?> bannedRole)
    {
        Capitaine capitaine = instance;
        Class<?> returnedRole = null;

        for (int i = 0; i < capitaine.capitaineToSpoil.size(); i++) {

            Class<?> randomRole = capitaine.capitaineToSpoil.get(i);
            if(randomRole == bannedRole || randomRole == role)
                continue;

            if(capitaine.linkedCapitaine.containsKey(randomRole) && capitaine.linkedCapitaine.get(randomRole) == role)
            {
                continue;
            }

            if(capitaine.linkedCapitaine.containsKey(role))
            {
                return null;
            }

            returnedRole = randomRole;
            break;
        }

        capitaine.capitaineToSpoil.remove(returnedRole);
        return returnedRole;
    }

    public static Class<?> giveCapitaine(Class<?> role)
    {
        Capitaine capitaine = instance;
        Class<?> returnedRole = null;

        for (int i = 0; i < capitaine.capitaineToSpoil.size(); i++) {

            Class<?> randomRole = capitaine.capitaineToSpoil.get(i);

            if(capitaine.linkedCapitaine.containsKey(randomRole) && capitaine.linkedCapitaine.get(randomRole) == role)
            {
                continue;
            }

            if(capitaine.linkedCapitaine.containsKey(role))
            {
                System.out.println("la c'est finito parce que t'as deja");
                return null;
            }

            System.out.println("je te présente l'élu : " + randomRole);
            returnedRole = randomRole;
            break;
        }

        capitaine.capitaineToSpoil.remove(returnedRole);
        return returnedRole;
    }

}


