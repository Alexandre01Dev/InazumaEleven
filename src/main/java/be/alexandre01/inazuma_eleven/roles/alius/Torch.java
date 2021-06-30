package be.alexandre01.inazuma_eleven.roles.alius;

import be.alexandre01.inazuma.uhc.InazumaUHC;
import be.alexandre01.inazuma.uhc.custom_events.episode.EpisodeChangeEvent;
import be.alexandre01.inazuma.uhc.custom_events.player.PlayerInstantDeathEvent;
import be.alexandre01.inazuma.uhc.managers.chat.Chat;
import be.alexandre01.inazuma.uhc.managers.damage.DamageManager;
import be.alexandre01.inazuma.uhc.presets.IPreset;
import be.alexandre01.inazuma.uhc.presets.Preset;

import be.alexandre01.inazuma.uhc.roles.Role;
import be.alexandre01.inazuma.uhc.roles.RoleItem;
import be.alexandre01.inazuma.uhc.utils.CustomComponentBuilder;
import be.alexandre01.inazuma.uhc.utils.ItemBuilder;
import be.alexandre01.inazuma_eleven.categories.Alius;
import be.alexandre01.inazuma_eleven.roles.raimon.*;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_8_R3.Tuple;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class Torch  extends Role implements Listener {
    private int i = 8;
    private int lastmsg = 0;
    World world;
    private Location loc;
    public int changesword = 0;

    public Torch(IPreset preset) {
        super("Torch",preset);

        addDescription("§8- §7Votre objectif est de gagner avec §5§ll'§5§lAcadémie §5§lAlius");
        addDescription("§8- §7Vous possédez l’effet §6§l§4§lForce 1 §7ainsi que §6§lFire Résistance§7.");
        addDescription(" ");
        CustomComponentBuilder c = new CustomComponentBuilder("");
        addDescription("§8- §7Vous disposez d'une commande qui est le §5/inachat§7 qui vous permettre de dialoguer avec votre frère §bGazelle§7, mais attention §c§lByron§7 peut lire vos messages...");
        addDescription(" ");
        c.append("§8- §7Vous disposez d'une épée nommé ");

        BaseComponent fire_swordButton = new TextComponent("§4§lEruption§7-§4§lSolaire §7*§8Curseur§7*");

        BaseComponent fire_swordDesc = new TextComponent();
        fire_swordDesc.addExtra("§e- §9Utilisation 5 coups par §eEpisode\n");
        fire_swordDesc.addExtra("§e- §9Met en feu le joueur frappé pendant §a3 secondes");
        fire_swordButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,fire_swordDesc.getExtra().toArray(new BaseComponent[0])));
        c.append(fire_swordButton);
        addDescription(c);
        addDescription(" ");
        addDescription("§8- §7Les attaques de §bGazelle§7, §6Axel§7 et §6Shawn§7 ne vous atteignent pas.");

        setRoleCategory(Alius.class);
        setRoleToSpoil(Xavier.class);
        addListener(this);

        RoleItem roleItem = new RoleItem();
        ItemBuilder itemBuilder = new ItemBuilder(Material.DIAMOND_SWORD);

        itemBuilder.setName("§4§lEruption§7-§4§lSolaire");
        itemBuilder.addEnchant(Enchantment.DAMAGE_ALL,2);
        itemBuilder.setUnbreakable();
        roleItem.setItemstack(itemBuilder.toItemStack());
        addRoleItem(roleItem);

        if(inazumaUHC.cm.getChat("InaChat") == null){
            for(Role role : Role.getRoles()){
                if(role.getClass() == Torch.class){
                    InazumaUHC.get.cm.addChat("InaChat", Chat.builder()
                            .chatName("§4§lINA§7-§3§lCHAT")
                            .prefixColor("§b§l")
                            .message("§7 ")
                            .separator("§8» ")
                            .build()

                    );
                }
            }
        }

        addCommand("inachat", new command() {
            @Override
            public void a(String[] args, Player player) {
                if(args.length == 0){
                    player.sendMessage(Preset.instance.p.prefixName()+"§c Veuillez mettre des arguments à votre message.");
                    return;
                }

                StringBuilder s = new StringBuilder();
                StringBuilder sPlayer = new StringBuilder();
                for (String m : args){
                    sPlayer.append(m).append(" ");
                    String playerName = player.getName().substring(0,player.getName().length()-2);
                    if(m.toLowerCase().contains(playerName.toLowerCase())){
                        ArrayList<Player> rem = new ArrayList<>(inazumaUHC.getRemainingPlayers());
                        rem.removeAll(inazumaUHC.rm.getRole(Gazelle.class).getPlayers());
                        rem.removeAll(inazumaUHC.rm.getRole(Torch.class).getPlayers());

                        if(!rem.isEmpty()){
                            m = rem.get( new Random().nextInt( inazumaUHC.getRemainingPlayers().size())).getName();
                        }else {
                            String[] x = {"bizarre","un peu bête","Torch","Gazelle","rien du tout","idiot"};
                            m = x[new Random().nextInt(x.length)];
                        }

                    }
                    s.append(m).append(" ");
                }
                player.sendMessage(inazumaUHC.cm.getChat("InaChat").generateMessage(player.getUniqueId(),sPlayer.toString()));
                inazumaUHC.cm.getChat("InaChat").sendMessageExceptUUID(player.getUniqueId(),s.toString(),player.getUniqueId());
            }
        });
        onLoad(new load() {
            @Override
            public void a(Player player) {
                inazumaUHC.dm.addEffectPourcentage(player, DamageManager.EffectType.INCREASE_DAMAGE,1,110);
                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0,false,false), true);
                Chat chat = inazumaUHC.cm.getChat("InaChat");
                if(chat != null){
                    chat.add(player.getUniqueId(),"§cTorch");
                }
            }
        });

        RoleItem colierAllius = new RoleItem();
        colierAllius.setItemstack(new ItemBuilder(Material.NETHER_STAR).setName("§d§lCollier§7§l-§5§lAlius").toItemStack());
        colierAllius.deployVerificationsOnRightClick(colierAllius.generateVerification(new Tuple<>(RoleItem.VerificationType.EPISODES,1)));
        colierAllius.setRightClick(player -> {
            Jude.collierAlliusNotif(player.getLocation());
            player.sendMessage(Preset.instance.p.prefixName()+" Vous rentrez en résonance avec la §8§lpierre§7§l-§5§lalius.");
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 90*20, 0,false,false), true);
        });
        addRoleItem(colierAllius);

    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player && event.getEntity() instanceof Player){
            Player torch = (Player) event.getDamager();
            Player player = (Player) event.getEntity();
            Role role = inazumaUHC.rm.getRole(torch);
            if(role != null){
                if(role.getClass().equals(Torch.class)){
                    if(!isValidItem(torch.getItemInHand()))
                        return;
                    if(getRoleItems().containsKey(torch.getItemInHand().getItemMeta().getDisplayName())){
                        if(i != 0){

                            if (changesword == 1){

                                if( !inazumaUHC.rm.getRole(player).getClass().equals(Torch.class) && !inazumaUHC.rm.getRole(player).getClass().equals(Axel.class) && !inazumaUHC.rm.getRole(player).getClass().equals(Shawn.class)){
                                    player.removePotionEffect(PotionEffectType.SLOW);
                                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*5, 1,true,false));
                                    player.sendMessage(Preset.instance.p.prefixName()+" §cTorch§7 vient d'utiliser son épée sur vous.");
                                    if(inazumaUHC.rm.getRole(player).getClass().equals(Hurley.class)){
                                        return;
                                    }
                                    player.setFireTicks(5*20);
                                }
                                if( inazumaUHC.rm.getRole(player).getClass().equals(Torch.class) && inazumaUHC.rm.getRole(player).getClass().equals(Axel.class) && inazumaUHC.rm.getRole(player).getClass().equals(Shawn.class)){
                                    player.sendMessage(Preset.instance.p.prefixName()+" §cTorch§7 vient d'utiliser son épée sur vous, mais en vain.");
                                }
                                i--;
                                torch.sendMessage(Preset.instance.p.prefixName()+" §7Vous venez d'utiliser votre §b§lBlizzard§7-§4§lEnflamm§7 sur §c" + player.getName() + "§7.Il vous reste §c" + i + " §7coups.");
                                return;
                            }

                            if( !inazumaUHC.rm.getRole(player).getClass().equals(Gazelle.class) && !inazumaUHC.rm.getRole(player).getClass().equals(Axel.class) && !inazumaUHC.rm.getRole(player).getClass().equals(Shawn.class) &&  !inazumaUHC.rm.getRole(player).getClass().equals(Hurley.class)){
                                player.sendMessage(Preset.instance.p.prefixName()+" §cTorch§7 vient d'utiliser son épée sur vous.");
                                player.setFireTicks(5*20);
                            }
                            if( inazumaUHC.rm.getRole(player).getClass().equals(Gazelle.class) && inazumaUHC.rm.getRole(player).getClass().equals(Axel.class) && inazumaUHC.rm.getRole(player).getClass().equals(Shawn.class) &&  inazumaUHC.rm.getRole(player).getClass().equals(Hurley.class)){
                                player.sendMessage(Preset.instance.p.prefixName()+" §cTorch§7 vient d'utiliser son épée sur vous, mais en vain.");
                            }
                            torch.sendMessage(Preset.instance.p.prefixName()+" §7Vous venez d'utiliser votre §4§lEruption§7-§4§lSolaire§7 sur §c" + player.getName() + "§7. Il vous reste §c" + (i-1) + " §7coups.");
                            i--;
                            if(i <= 0){
                                if (lastmsg == 0){
                                    if (changesword == 1){
                                        torch.sendMessage(Preset.instance.p.prefixName()+" §7Vous venez d'atteindre votre limite d'utilisation de votre §b§lBlizzard§7-§4§lEnflammé§7 pour cette §eépisode.");
                                    }
                                    if (changesword == 0){
                                        torch.sendMessage(Preset.instance.p.prefixName()+" §7Vous venez d'atteindre votre limite d'utilisation de votre §4§lEruption§7-§4§lSolaire§7 pour cette §eépisode.");
                                    }
                                    lastmsg++;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEpisodeChanged(EpisodeChangeEvent event){
        lastmsg = 0;
        if (changesword == 0){
            for(Player player : getPlayers()){
                player.sendMessage(Preset.instance.p.prefixName()+" §7Vous venez de récupérer toute les utilisation sur votre §4§lEruption§7-§4§lSolaire§4§7 (§c8 coups§7).");
            }
            this.i = 8;
        }
        else if(changesword != 0){

            for(Player player : getPlayers()){
                player.sendMessage(Preset.instance.p.prefixName()+" §7Vous venez de récupérer toute les utilisation sur votre §b§lBlizzard§7-§4§lEnflammé§7 (§b8 §4coups§7).");
            }
            this.i = 12;
        }

    }

    @EventHandler
    private void onDeath(PlayerInstantDeathEvent event)
    {
        Player player = event.getPlayer();

        if(inazumaUHC.rm.getRole(player.getUniqueId()).getClass().equals(Torch.class)){

            world = player.getWorld();
            loc = player.getLocation();
            loc.getBlock().setType(Material.REDSTONE_BLOCK);
            loc = loc.getBlock().getLocation();

            if(inazumaUHC.rm.getRole(Gazelle.class) != null)
            {
                for (Player gazelle : inazumaUHC.rm.getRole(Gazelle.class).getPlayers()) {

                    new BukkitRunnable(){
                        @Override
                        public void run(){

                            gazelle.sendMessage(Preset.instance.p.prefixName()+" §7Le cadavre de §c§lTorch§7 se trouve en X: " + loc.getX() + " Y: " + loc.getY() + " Z: " + loc.getZ());

                        }

                    }.runTaskLater(InazumaUHC.get, 1);


                }
            }

            new BukkitRunnable() {

                double var = 0;
                @Override
                public void run() {

                    if(loc.getBlock().getType() == Material.REDSTONE_BLOCK)
                    {
                        var += Math.PI / 12;

                        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.LAVA,true, (loc.getBlockX() + 0.6f), (float) (loc.getY() + 1.5),  (loc.getBlockZ() + 0.6f), 0, 0, 0, 0, 1);
                        for(Player online : Bukkit.getOnlinePlayers())
                            ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
                    }
                    else {
                        cancel();
                    }


                }
            }.runTaskTimerAsynchronously(inazumaUHC, 1, 3);

        }
    }

    private int floatToInt(float f)
    {
        return (int)f;
    }

    public Location getTorchLoc()
    {
        return new Location(world, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    @EventHandler
    public void onBlockItemGet(BlockBreakEvent e) {
        Block block = e.getBlock();
        if (block.getType() == Material.REDSTONE_BLOCK && floatToInt(block.getX()) == floatToInt(loc.getBlockX()) && floatToInt(block.getY()) == floatToInt(loc.getBlockY()) && floatToInt(block.getZ()) == floatToInt(loc.getBlockZ()))
        {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack it = event.getItem();
        Gazelle gazelle = (Gazelle) inazumaUHC.rm.getRole(Gazelle.class);




        if(it == null)
        {
            return;
        }


        if(it.hasItemMeta() && it.getItemMeta().hasDisplayName() && it.getItemMeta().getDisplayName().equalsIgnoreCase("§4§lEruption§7-§4§lSolaire"))
        {
            if(action == Action.RIGHT_CLICK_BLOCK)
            {
                Block block = event.getClickedBlock();
                if(block.getType() == Material.LAPIS_BLOCK)
                {
                    if(gazelle != null)
                    {
                        if (floatToInt(block.getX()) == gazelle.getGazelleLoc().getBlockX() && floatToInt(block.getY()) == gazelle.getGazelleLoc().getBlockY() && floatToInt(block.getZ()) == gazelle.getGazelleLoc().getBlockZ())
                        {
                            if(inazumaUHC.rm.getRole(player) instanceof Torch)
                            {

                                RoleItem roleItem = new RoleItem();
                                ItemMeta im = it.getItemMeta();
                                im.setDisplayName("§b§lBlizzard§7-§4§lEnflammé");
                                it.setItemMeta(im);
                                roleItem.setItemstack(it);
                                addRoleItem(roleItem);
                                block.setType(Material.AIR);
                                player.sendMessage(Preset.instance.p.prefixName()+" §7§lFélicitation§7, vous avez fusionné votre épée avec celle de §b§lGazelle§7, désormais votre épée en plus de ces pouvoirs de base, possède également celle de l'épée de §c§lGazelle§7.");
                                player.playSound(player.getLocation(), Sound.ORB_PICKUP, 5,5);
                                changesword++;
                            }
                        }
                    }
                }
            }
        }
    }

}
