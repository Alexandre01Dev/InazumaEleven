package be.alexandre01.inazuma_eleven.listeners;

import be.alexandre01.inazuma.uhc.InazumaUHC;
import be.alexandre01.inazuma.uhc.custom_events.player.PlayerInstantDeathEvent;
import be.alexandre01.inazuma.uhc.custom_events.roles.RoleItemTargetEvent;
import be.alexandre01.inazuma.uhc.custom_events.roles.RoleItemUseEvent;
import be.alexandre01.inazuma.uhc.managers.RejoinManager;
import be.alexandre01.inazuma.uhc.presets.Preset;
import be.alexandre01.inazuma.uhc.roles.Role;
import be.alexandre01.inazuma.uhc.state.GameState;
import be.alexandre01.inazuma.uhc.state.State;
import be.alexandre01.inazuma.uhc.utils.TitleUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerEvent implements Listener {
    private GameState gameState;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (gameState == null) {
            gameState = GameState.get();
        }
        Player player = event.getPlayer();


        if (gameState.contains(State.PLAYING) || gameState.contains(State.STOPPING)) {
            event.setJoinMessage(Preset.instance.p.prefixName() +"§a" + player.getName() + "§e a rejoint la partie");
            return;
        }
        if (GameState.get().contains(State.WAITING) || (GameState.get().contains(State.PREPARING))) {
            event.setJoinMessage("");
            Bukkit.getOnlinePlayers().forEach((p) -> {
                TitleUtils.sendActionBar(p, "§a" + player.getName() + "§e a rejoint la partie");
            });
            return;
        }

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        if (gameState == null) {
            gameState = GameState.get();
        }
        Player player = event.getPlayer();

        if (GameState.get().contains(State.PLAYING) || GameState.get().contains(State.STOPPING)) {
            event.setQuitMessage(Preset.instance.p.prefixName() +"§c" + player.getName() + "§e a quitté la partie");
            return;
        }
        if (GameState.get().contains(State.WAITING) || (GameState.get().contains(State.PREPARING))) {
            event.setQuitMessage("");
            Bukkit.getOnlinePlayers().forEach((p) -> {
                TitleUtils.sendActionBar(p, "§c" + player.getName() + "§e a quitté la partie");
            });
            return;
        }
    }

    @EventHandler
    public void onPlayerInstantDeath(PlayerInstantDeathEvent event) {
        Player player = event.getPlayer();
        event.getDrops().add(new ItemStack(Material.GOLDEN_APPLE));
        if (!Role.isDistributed) {
            Bukkit.broadcastMessage(Preset.instance.p.prefixName() + " §c§l" + player.getName() + "§7 vient de mourir (PVE).");
            InazumaUHC.get.getRejoinManager().onKilled(player);
            event.getDrops().clear();

            new BukkitRunnable(){
                @Override
                public void run(){

                    RejoinManager r = InazumaUHC.get.getRejoinManager();
                    r.revivePlayer(player);
                    player.sendMessage(Preset.instance.p.prefixName()+" §aVous venez d'être ressuscité.");
                    InazumaUHC.get.invincibilityDamager.addPlayer(player, 1000*7);
                    player.sendMessage(Preset.instance.p.prefixName()+" §eVous avez 7 secondes d'invincibilité.");
                    r.teleportRandom(player);
                    player.setExp(player.getExp()/2);
                    player.setLevel(player.getLevel()/2);

                }

            }.runTaskLater(InazumaUHC.get, 5);

        }
    }

    @EventHandler
    public void AnimalSpiritEndermanNoPearlDamage(PlayerTeleportEvent event){
        Player p = event.getPlayer();
        if(event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL){
            event.setCancelled(true);
            p.setNoDamageTicks(1);
            p.teleport(event.getTo());
        }
    }


}
