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
import be.alexandre01.inazuma_eleven.objects.Capitaine;
import be.alexandre01.inazuma_eleven.roles.raimon.Axel;
import be.alexandre01.inazuma_eleven.roles.raimon.Jack;
import be.alexandre01.inazuma_eleven.roles.raimon.Jude;
import be.alexandre01.inazuma_eleven.roles.raimon.Shawn;
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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;

public class Gazelle extends Role implements Listener {
    private int i = 6;
    private int lastmsg = 0;
    private Location loc;
    public int changesword = 0;
    World world;
    public Gazelle(IPreset preset) {
        super("Gazelle",preset);
        addDescription("https://app.gitbook.com/@inazumauhcpro/s/inazuma-gitbook/inazuma-eleven-uhc/roles/alius/gazelle");
        /*addDescription("§8- §7Votre objectif est de gagner avec §5§ll'§5§lAcadémie §5§lAlius");
        addDescription("§8- §7Vous possédez l’effet §6§l§4§lForce 1 §7ainsi que §6§lHaste 1§7.");
        addDescription(" ");
        CustomComponentBuilder c = new CustomComponentBuilder("");
        addDescription("§8- §7Vous disposez d'une commande qui est le §5/inachat§7 qui vous permettre de dialoguer avec votre frère §cTorch§7, mais attention §c§lByron§7 peut lire vos messages...");
        addDescription(" ");
        c.append("§8- §7Vous disposez d'une épée nommé ");

        BaseComponent fire_swordButton = new TextComponent("§b§lImpact§7-§b§lNordique §7*§8Curseur§7*");

        BaseComponent fire_swordDesc = new TextComponent();
        fire_swordDesc.addExtra("§e- §9Utilisation 8 coups par §eEpisode\n");
        fire_swordDesc.addExtra("§e- §9Donne aujoueur frappé §8§lSlowness 2 pendant §a5 secondes");
        fire_swordButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,fire_swordDesc.getExtra().toArray(new BaseComponent[0])));
        c.append(fire_swordButton);
        addDescription(c);
        addDescription(" ");
        addDescription("§8- §7Les attaques de §cTorch§7, §6Axel§7 et §6Shawn§7 ne vous atteignent pas.");*/
        Class<?> clazz = Capitaine.giveCapitaine(this.getClass(), Torch.class);
        if(clazz != null)
            setRoleToSpoil(clazz);


        //A retirer ???
        setRoleToSpoil(Xavier.class);
        setRoleCategory(Alius.class);
        addListener(this);
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
        RoleItem roleItem = new RoleItem();
        ItemBuilder itemBuilder = new ItemBuilder(Material.DIAMOND_SWORD);

        itemBuilder.setName("§b§lImpact§7-§b§lNordique");
        itemBuilder.addEnchant(Enchantment.DAMAGE_ALL,2);
        itemBuilder.setUnbreakable();
        roleItem.setItemstack(itemBuilder.toItemStack());

        addRoleItem(roleItem);

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

                        if((rem.size() > 10)){
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
                player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, 0,false,false), true);
                Chat chat = inazumaUHC.cm.getChat("InaChat");
                if(chat != null){
                    chat.add(player.getUniqueId(),"§bGazelle");
                }
            }
        });

        RoleItem colierAllius = new RoleItem();
        colierAllius.setItemstack(new ItemBuilder(Material.NETHER_STAR).setName("§d§lCollier§7§l-§5§lAlius").toItemStack());
        colierAllius.deployVerificationsOnRightClick(colierAllius.generateVerification(new Tuple<>(RoleItem.VerificationType.EPISODES,1)));
        colierAllius.setRightClick(player -> {
            Jude.collierAlliusNotif(player.getLocation());
            Jack.nearAliusActivation(player.getLocation());
            player.sendMessage(Preset.instance.p.prefixName()+" Vous rentrez en résonance avec la §8§lpierre§7§l-§5§lalius.");
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*60*2, 0,false,false), true);
        });
        addRoleItem(colierAllius);

    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player && event.getEntity() instanceof Player){
            Player gazelle = (Player) event.getDamager();
            Player player = (Player) event.getEntity();
            Role role = inazumaUHC.rm.getRole(gazelle);
            if(role != null){
                if(role.getClass().equals(Gazelle.class)){
                    if(!isValidItem(gazelle.getItemInHand()))
                        return;
                    if(getRoleItems().containsKey(gazelle.getItemInHand().getItemMeta().getDisplayName())){
                        if(this.i != 0){

                            if (changesword == 1){

                                if( !inazumaUHC.rm.getRole(player).getClass().equals(Torch.class) && !inazumaUHC.rm.getRole(player).getClass().equals(Axel.class) && !inazumaUHC.rm.getRole(player).getClass().equals(Shawn.class)){
                                    player.removePotionEffect(PotionEffectType.SLOW);
                                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*5, 1,true,false));
                                    player.setFireTicks(3*20);
                                    player.sendMessage(Preset.instance.p.prefixName()+" §bGazelle§7 vient d'utiliser son épée sur vous.");
                                }
                                if( inazumaUHC.rm.getRole(player).getClass().equals(Torch.class) && inazumaUHC.rm.getRole(player).getClass().equals(Axel.class) && inazumaUHC.rm.getRole(player).getClass().equals(Shawn.class)){
                                    player.sendMessage(Preset.instance.p.prefixName()+" §bGazelle§7 vient d'utiliser son épée sur vous, mais en vain.");
                                }
                                i--;
                                gazelle.sendMessage(Preset.instance.p.prefixName()+" §7Vous venez d'utiliser votre §b§lBlizzard§7-§4§lEnflamm§7 sur §c" + player.getName() + "§7.Il vous reste §c" + i + " §7coups.");
                                return;
                            }

                            if( !inazumaUHC.rm.getRole(player).getClass().equals(Torch.class) && !inazumaUHC.rm.getRole(player).getClass().equals(Axel.class) && !inazumaUHC.rm.getRole(player).getClass().equals(Shawn.class)){
                                player.removePotionEffect(PotionEffectType.SLOW);
                                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*5, 1,true,false));
                                player.sendMessage(Preset.instance.p.prefixName()+" §bGazelle§7 vient d'utiliser son épée sur vous.");
                            }
                            if( inazumaUHC.rm.getRole(player).getClass().equals(Torch.class) && inazumaUHC.rm.getRole(player).getClass().equals(Axel.class) && inazumaUHC.rm.getRole(player).getClass().equals(Shawn.class)){
                                player.sendMessage(Preset.instance.p.prefixName()+" §bGazelle§7 vient d'utiliser son épée sur vous, mais en vain.");
                            }
                            gazelle.sendMessage(Preset.instance.p.prefixName()+" §7Vous venez d'utiliser votre §b§lImpact§7-§b§lNordique§7 sur §c" + player.getName() + "§7.Il vous reste §c" + (i-1) + " §7coups.");
                            i--;
                        }

                        if(i <= 0){
                            if (lastmsg == 0){
                                if (changesword == 1){
                                    gazelle.sendMessage(Preset.instance.p.prefixName()+" §7Vous venez d'atteindre votre limite d'utilisation de votre §b§lBlizzard§7-§4§lEnflammé§7 pour cette §eépisode.");
                                }
                                if (changesword == 0){
                                    gazelle.sendMessage(Preset.instance.p.prefixName()+" §7Vous venez d'atteindre votre limite d'utilisation de votre §b§lImpact§7-§b§lNordique§7 pour cette §eépisode.");
                                }
                                lastmsg++;
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
                player.sendMessage(Preset.instance.p.prefixName()+" §7Vous venez de récupérer toute les utilisation sur votre §b§lImpact§7-§b§lNordique§7 (§b6 coups§7).");
            }
            this.i = 6;
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

        if(inazumaUHC.rm.getRole(player.getUniqueId()).getClass().equals(Gazelle.class)){

            world = player.getWorld();
            loc = player.getLocation();
            loc.getBlock().setType(Material.LAPIS_BLOCK);
            player.getWorld().playSound(loc, Sound.SPLASH, 1, 1);

            if(inazumaUHC.rm.getRole(Torch.class) != null)
            {
                for (Player torch : inazumaUHC.rm.getRole(Torch.class).getPlayers()) {

                    new BukkitRunnable(){
                        @Override
                        public void run(){

                            torch.sendMessage(Preset.instance.p.prefixName()+" §7Le cadavre de §b§lGazelle§7 se trouve en X: " + (float)loc.getBlockX() + " Y: " + (float)loc.getBlockY() + " Z: " + (float)loc.getBlockZ());

                        }

                    }.runTaskLater(InazumaUHC.get, 1);

                }
            }


            new BukkitRunnable() {

                double var = 0;
                @Override
                public void run() {

                    if(loc.getBlock().getType() == Material.LAPIS_BLOCK)
                    {
                        var += Math.PI / 12;

                        float r = 1;
                        float g = 255;
                        float b = 255;

                        float secondR = 255;

                        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.REDSTONE, true, (loc.getBlockX()), (float) (loc.getBlockY() + Math.sin(var) + 2),  (loc.getBlockZ()), r / 255,g / 255, b / 255, 1, 0);
                        PacketPlayOutWorldParticles redstonePacket = new PacketPlayOutWorldParticles(EnumParticle.REDSTONE, true, (loc.getBlockX() + 1), (float) (loc.getBlockY() + Math.sin(var) + 2),  (loc.getBlockZ() + 1), r / 255,g / 255, b / 255, 1, 0);
                        PacketPlayOutWorldParticles snowballPacket = new PacketPlayOutWorldParticles(EnumParticle.REDSTONE, true, (loc.getBlockX() + 1), (float) (loc.getBlockY() + Math.sin(var) + 2),  (loc.getBlockZ()), (float) 1 / 255,(float) 1 / 255, b / 255, 1, 0);
                        PacketPlayOutWorldParticles snowballPackette = new PacketPlayOutWorldParticles(EnumParticle.REDSTONE, true, (loc.getBlockX()), (float) (loc.getBlockY() + Math.sin(var) + 2),  (loc.getBlockZ() + 1), (float) 1 / 255,(float) 1 / 255, b / 255, 1, 0);
                        PacketPlayOutWorldParticles snowball = new PacketPlayOutWorldParticles(EnumParticle.SNOWBALL, true, (loc.getBlockX() + 0.5f), (float) (loc.getBlockY() + Math.sin(var) + 2),  (loc.getBlockZ() + 0.5f), 0,0, 0, 0, 1);
                        for(Player online : Bukkit.getOnlinePlayers())
                        {
                            ((CraftPlayer) online).getHandle().playerConnection.sendPacket(packet);
                            ((CraftPlayer) online).getHandle().playerConnection.sendPacket(snowballPacket);
                            ((CraftPlayer) online).getHandle().playerConnection.sendPacket(snowballPackette);
                            ((CraftPlayer) online).getHandle().playerConnection.sendPacket(redstonePacket);
                            ((CraftPlayer) online).getHandle().playerConnection.sendPacket(snowball);
                        }

                    }
                    else {
                        cancel();
                    }


                }
            }.runTaskTimerAsynchronously(inazumaUHC, 1, 1);

        }
    }

    private int floatToInt(float f)
    {
        return (int)f;
    }


    @EventHandler
    public void onBlockItemGet(BlockBreakEvent e) {
        Block block = e.getBlock();
        if (block.getType() == Material.LAPIS_BLOCK && floatToInt(block.getX()) == floatToInt(loc.getBlockX()) && floatToInt(block.getY()) == floatToInt(loc.getBlockY()) && floatToInt(block.getZ()) == floatToInt(loc.getBlockZ()))
        {
            e.setCancelled(true);
        }
    }

    public Location getGazelleLoc()
    {
        return new Location(world, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack it = event.getItem();
        Torch torch = (Torch) inazumaUHC.rm.getRole(Torch.class);




        if(it == null)
        {
            return;
        }


        if(it.hasItemMeta() && it.getItemMeta().hasDisplayName() && it.getItemMeta().getDisplayName().equalsIgnoreCase("§b§lImpact§7-§b§lNordique"))
        {
            if(action == Action.RIGHT_CLICK_BLOCK)
            {
                Block block = event.getClickedBlock();
                if(block.getType() == Material.REDSTONE_BLOCK)
                {
                    if(torch != null)
                    {
                        if (floatToInt(block.getX()) == torch.getTorchLoc().getBlockX() && floatToInt(block.getY()) == torch.getTorchLoc().getBlockY() && floatToInt(block.getZ()) == torch.getTorchLoc().getBlockZ())
                        {
                            if(inazumaUHC.rm.getRole(player) instanceof Gazelle)
                            {

                                RoleItem roleItem = new RoleItem();
                                ItemMeta im = it.getItemMeta();
                                im.setDisplayName("§b§lBlizzard§7-§4§lEnflammé");
                                it.setItemMeta(im);
                                roleItem.setItemstack(it);
                                addRoleItem(roleItem);
                                block.setType(Material.AIR);
                                player.sendMessage(Preset.instance.p.prefixName()+" §7§lFélicitation§7, vous avez fusionné votre épée avec celle de §c§lTorch§7, votre épée a §aévolué§7 pour devenir le §b§lBlizzard§7-§4§lEnflammé§7 !");
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
