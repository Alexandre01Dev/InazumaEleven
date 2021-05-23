package be.alexandre01.inazuma_eleven.listeners;

import be.alexandre01.inazuma.uhc.InazumaUHC;
import be.alexandre01.inazuma.uhc.presets.Preset;
import be.alexandre01.inazuma.uhc.roles.RoleCategory;
import be.alexandre01.inazuma_eleven.roles.alius.Caleb;
import be.alexandre01.inazuma_eleven.roles.alius.David;
import be.alexandre01.inazuma_eleven.roles.alius.Joseph;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class PierreAliusEvent implements Listener {

    private InazumaUHC inazumaUHC;

    public PierreAliusEvent(InazumaUHC inazumaUHC){

        this.inazumaUHC = inazumaUHC;
    }

    @EventHandler
    public void onPierreInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack it = event.getItem();
        RoleCategory roleCategory = inazumaUHC.rm.getRole(event.getPlayer()).getRoleCategory();

        if (it.getType() == Material.NETHER_STAR && it.hasItemMeta() && it.getItemMeta().hasDisplayName() && it.getItemMeta().getDisplayName().equalsIgnoreCase("§5§lPierre §lAlius")) {

            if((event.getAction() == Action.RIGHT_CLICK_AIR) || (event.getAction() == Action.RIGHT_CLICK_BLOCK)){

                if(!inazumaUHC.rm.getRole(player).getClass().equals(David.class) && !inazumaUHC.rm.getRole(player).getClass().equals(Joseph.class)){

                    Player menteur = player;

                    new BukkitRunnable() {
                        @Override
                        public void run() {

                            player.setMaxHealth(player.getMaxHealth()+2);
                            player.sendMessage(Preset.instance.p.prefixName()+" Vous venez de piéger §lCaleb pour avoir une §5§lPierre §lAlius§7, vous gagner donc §c§l1 §4❤§7 permanent§7, il sera dans 1 minute que vous lui avez mentis.");

                        }
                    }.runTaskLaterAsynchronously(inazumaUHC,20*60*1);

                    for(Player caleb : inazumaUHC.rm.getRole(Caleb.class).getPlayers()){
                        caleb.sendMessage(Preset.instance.p.prefixName()+ menteur + "vous a mentis, il a donc gagné §c§l1 §4❤§7 permanent§7.");
                    }

                }


            }
        }
    }
}
