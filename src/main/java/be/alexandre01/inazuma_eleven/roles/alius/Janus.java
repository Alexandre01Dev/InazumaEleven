package be.alexandre01.inazuma_eleven.roles.alius;

import be.alexandre01.inazuma.uhc.InazumaUHC;
import be.alexandre01.inazuma.uhc.managers.damage.DamageManager;
import be.alexandre01.inazuma.uhc.presets.IPreset;
import be.alexandre01.inazuma.uhc.presets.Preset;

import be.alexandre01.inazuma.uhc.roles.Role;
import be.alexandre01.inazuma.uhc.roles.RoleItem;
import be.alexandre01.inazuma.uhc.utils.*;
import be.alexandre01.inazuma_eleven.InazumaEleven;
import be.alexandre01.inazuma_eleven.categories.Alius;
import be.alexandre01.inazuma_eleven.objects.Capitaine;
import be.alexandre01.inazuma_eleven.roles.raimon.Jack;
import be.alexandre01.inazuma_eleven.roles.raimon.Jude;
import be.alexandre01.inazuma_eleven.roles.raimon.Scotty;
import net.minecraft.server.v1_8_R3.Tuple;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class  Janus extends Role implements Listener {
    ArrayList<Location> ballonsLoc = new ArrayList<>();
    public ArrayList<Boolean> ballsAvailable = new ArrayList<>();
    public ArrayList<Boolean> trappedBalls = new ArrayList<>();
    ArrayList<Boolean> notifBalls = new ArrayList<>();
    int i = 0;
    int choosedBall = 0;
    Location xavierBall;
    Block xavierBlock;
    HashMap<Block,Location> ballonsBlock = new HashMap<>();
    InazumaEleven inazumaEleven;
    int episode = 0;
    Inventory inventory;
    String texture = "ewogICJ0aW1lc3RhbXAiIDogMTYxNTczMzg1MTExMywKICAicHJvZmlsZUlkIiA6ICJhMjk1ODZmYmU1ZDk0Nzk2OWZjOGQ4ZGE0NzlhNDNlZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJWaWVydGVsdG9hc3RpaWUiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjhiMjBmMWNmMWQ2YzRmYWJhN2Q1ZGIzY2RlMjkxMTNkZDIwZDA0MDdmNGY3NzkxNTViZmJlYTY4ZGZhNTM1ZiIKICAgIH0KICB9Cn0";
    String textureXavier = "ewogICJ0aW1lc3RhbXAiIDogMTYxNTc0NzUzMzc0NSwKICAicHJvZmlsZUlkIiA6ICI3MmNiMDYyMWU1MTA0MDdjOWRlMDA1OTRmNjAxNTIyZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJNb3M5OTAiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2MyZGM3Mjk0OTQzNTlhZGVjOTNkMGZkZGFmMGVmMzE2OTNjMDdmMjg3NmFkOWM1NzcyNzQ3NDhkNjZmYjczOCIKICAgIH0KICB9Cn0=";
    public Janus(IPreset preset) {

        super("Janus",preset);
        setRoleCategory(Alius.class);
        addDescription("https://app.gitbook.com/@inazumauhcpro/s/inazuma-gitbook/inazuma-eleven-uhc/roles/alius/janus");
        /*addDescription("§8- §7Votre objectif est de gagner avec §5§ll'§5§lAcadémie §5§lAlius");
        addDescription("§8- §7Vous possédez l’effet §b§lSpeed 1§.");
        addDescription(" ");
        addDescription("§8- §7Vous disposez de 3 ballons que vous pourrez placer et vous y téléporter une fois par épisode avec le §5/inaball§7");
        addDescription(" ");
        addDescription("§8- §7Et également un ballon réservé à §5Xavier§7, ou vous devrez lui donner les coordonnées et il pourra y téléporter 2 joueurs durant la game.");*/

        Class<?> clazz = Capitaine.giveCapitaine(this.getClass());
        if(clazz != null)
            setRoleToSpoil(clazz);

        addListener(this);

        if(inazumaEleven != null)
            inventory = inazumaEleven.getBallonInv().toInventory();

        onLoad(new load() {
            @Override
            public void a(Player player) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0,false,false), true);
            }
        });

        RoleItem roleItem = new RoleItem();
        ItemBuilder itemBuilder = new ItemBuilder(Material.NETHER_STAR).setName("§d§lCollier§7§l-§5§lAlius");
        roleItem.setItemstack(itemBuilder.toItemStack());
        roleItem.deployVerificationsOnRightClick(roleItem.generateVerification(new Tuple<>(RoleItem.VerificationType.EPISODES,1)));
        roleItem.setRightClick(player -> {
            Jude.collierAlliusNotif(player.getLocation());
            Jack.nearAliusActivation(player.getLocation());
            player.sendMessage(Preset.instance.p.prefixName()+" Vous rentrez en résonance avec la §8§lpierre§7§l-§5§lalius.");
            inazumaUHC.dm.addEffectPourcentage(player, DamageManager.EffectType.INCREASE_DAMAGE,1,110);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0,false,false), true);
        });


        inazumaEleven = (InazumaEleven) preset;

        ballsAvailable.add(false);
        ballsAvailable.add(false);
        ballsAvailable.add(false);

        trappedBalls.add(false);
        trappedBalls.add(false);
        trappedBalls.add(false);

        notifBalls.add(false);
        notifBalls.add(false);
        notifBalls.add(false);

        RoleItem ballons = new RoleItem();
        CustomHead customHead = new CustomHead(texture,"§eBallons");
        ItemStack itemStack = customHead.toItemStack();
        itemStack.setAmount(3);
        ballons.setItemstack(itemStack);
        ballons.setSlot(8);
        ballons.setPlaceBlock(new RoleItem.PlaceBlock() {
            @Override
            public void execute(Player player, Block block) {
                if(i >= 3){
                    player.sendMessage(Preset.instance.p.prefixName()+" §c§lBUG ! La limite de ballons à déjà été atteint.");
                    return;
                }


                if(block.getLocation().getBlockY() <= 60){
                    player.sendMessage(Preset.instance.p.prefixName()+" §cVous ne pouvez pas mettre ce block en dessous de la couche 61.");
                    return;
                }
                Location tpLoc = getTop(block.getLocation());
                if(tpLoc == null){
                    player.sendMessage(Preset.instance.p.prefixName()+" §cVous ne pouvez pas mettre ce block en dessous de 3 blocks ou plus.");
                    return;
                }

                block.setType(Material.SKULL);

                CustomHead.toSkull(block,customHead.getTexture());
                ItemStack itemStack = player.getItemInHand();
                Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(InazumaUHC.get, new Runnable() {
                    public void run() {
                        int amount = itemStack.getAmount()-1;
                        if(amount == 0){
                            player.setItemInHand(new ItemStack(Material.AIR));
                            return;
                        }

                        itemStack.setAmount(itemStack.getAmount()-1);
                        player.setItemInHand(itemStack);
                    }
                }, 1L);
                ballonsLoc.add(tpLoc);
                ballonsBlock.put(block,tpLoc);
                ItemStack clone = itemStack.clone();
                ItemMeta cloneMeta = clone.getItemMeta();
                switch (i){
                    case 0:
                        clone.setAmount(1);
                        cloneMeta.setDisplayName("§7Ballon n°§e1");
                        cloneMeta.setLore(Arrays.asList("§fX §7: §2"+block.getLocation().getBlockX() + "","§fY §7: §2"+block.getLocation().getBlockY()+ "","§fZ §7: §2" +block.getLocation().getBlockZ()));
                        clone.setItemMeta(cloneMeta);
                        i++;
                        break;
                    case 1:
                        clone.setAmount(2);
                        cloneMeta.setDisplayName("§7Ballon n°§e2");
                        cloneMeta.setLore(Arrays.asList("§fX §7: §2"+block.getLocation().getBlockX() + "","§fY §7: §2"+block.getLocation().getBlockY()+ "","§fZ §7: §2" +block.getLocation().getBlockZ()));
                        clone.setItemMeta(cloneMeta);
                        i++;
                        break;
                    case 2:
                        clone.setAmount(3);
                        cloneMeta.setDisplayName("§7Ballon n°§e3");
                        cloneMeta.setLore(Arrays.asList("§fX §7: §2"+block.getLocation().getBlockX() + "","§fY §7: §2"+block.getLocation().getBlockY()+ "","§fZ §7: §2" +block.getLocation().getBlockZ()));
                        clone.setItemMeta(cloneMeta);
                        i++;
                        break;
                }
                player.sendMessage(Preset.instance.p.prefixName()+"§7Ballon n°§e"+(i)+" §aposé ! §e| §7X:"+ block.getLocation().getBlockX()+"§8| §7Y:"+block.getLocation().getBlockY()+ "§8| §7Z:"+block.getLocation().getBlockZ());
                if(inazumaUHC.rm.getRole(Scotty.class) != null)
                {
                    Scotty scotty = (Scotty) inazumaUHC.rm.getRole(Scotty.class);
                    scotty.trapBall(i-1, false);
                }
            }
        });
        addRoleItem(ballons);

        RoleItem ballonsXavier = new RoleItem();
        CustomHead customHeadXavier = new CustomHead(textureXavier,"§7Ballon de §5§lXavier");
        ItemStack itemStackXavier = customHeadXavier.toItemStack();
        itemStackXavier.setAmount(1);
        ballonsXavier.setItemstack(itemStackXavier);
        ballonsXavier.setSlot(7);
        ballonsXavier.setPlaceBlock(new RoleItem.PlaceBlock() {
            int xavierI = 0;
            @Override
            public void execute(Player player, Block block) {
                if(xavierI >= 3){
                    player.sendMessage(Preset.instance.p.prefixName()+" §c§lBUG ! La limite de ballons à déjà été atteint.");
                    return;
                }


                if(block.getLocation().getBlockY() <= 60){
                    player.sendMessage(Preset.instance.p.prefixName()+" §cVous ne pouvez pas mettre ce block en dessous de la couche 61.");
                    return;
                }
                Location tpLoc = getTop(block.getLocation());
                if(tpLoc == null){
                    player.sendMessage(Preset.instance.p.prefixName()+" §cVous ne pouvez pas mettre ce block en dessous de 3 blocks ou plus.");
                    return;
                }

                block.setType(Material.SKULL);

                CustomHead.toSkull(block,customHeadXavier.getTexture());
                ItemStack itemStack = player.getItemInHand();
                Bukkit.getServer().getScheduler().runTaskLaterAsynchronously(InazumaUHC.get, new Runnable() {
                    public void run() {
                        int amount = itemStack.getAmount()-1;
                        if(amount == 0){
                            player.setItemInHand(new ItemStack(Material.AIR));
                            return;
                        }

                        itemStack.setAmount(itemStack.getAmount()-1);
                        player.setItemInHand(itemStack);
                    }
                }, 1L);
                xavierBall = tpLoc;
                xavierBlock = block;
                ItemStack clone = itemStack.clone();
                ItemMeta cloneMeta = clone.getItemMeta();
                switch (xavierI){
                    case 0:
                        clone.setAmount(1);
                        cloneMeta.setDisplayName("§7Ballon de §5§lXavier");
                        clone.setItemMeta(cloneMeta);
                        for(Role role : Role.getRoles()){
                            if(role instanceof Xavier){
                                Xavier xavier = (Xavier) role;
                                xavier.setLocation(xavierBall);
                                xavier.setBlock(xavierBlock);
                                xavier.getPlayers().forEach(x -> {
                                    x.sendMessage(Preset.instance.p.prefixName()+" §cTon ballon vient de se poser.");
                                });

                            }
                            if(role instanceof Bellatrix){
                                Bellatrix bellatrix = (Bellatrix) role;
                                bellatrix.setLocation(xavierBall);
                                bellatrix.setBlock(xavierBlock);
                                bellatrix.getPlayers().forEach(x -> {
                                    x.sendMessage(Preset.instance.p.prefixName()+" §cTon ballon vient de se poser.");
                                });
                            }
                        }
                        break;
                }
                xavierI++;
                player.sendMessage(Preset.instance.p.prefixName()+" §7Ballon de §5§lXavier§a posé ! §e| §7X:"+ block.getLocation().getBlockX()+"§8| §7Y:"+block.getLocation().getBlockY()+ "§8| §7Z:"+block.getLocation().getBlockZ() );
            }
        });
        addRoleItem(ballonsXavier);

        RoleItem selector = new RoleItem();
        selector.setItemstack(new ItemBuilder(Material.NETHER_BRICK_ITEM).setName("Sélecteur de Ballons").toItemStack());
        selector.setLeftClick(new RoleItem.LeftClick() {
            @Override
            public void execute(Player player) {

                if(i == 0)
                {
                    player.sendMessage(Preset.instance.p.prefixName() + "§cVous n'avez posé aucun ballon");
                    return;
                }


                if(choosedBall == i)
                {
                    player.sendMessage(Preset.instance.p.prefixName() + "§7Ballon n°§e 1 §asélectionné !");
                    TitleUtils.sendActionBar(player,"§7Le ballon n°§e1 §7est §asélectionné !");
                    choosedBall = 1;
                    return;
                }

                choosedBall++;
                player.sendMessage(Preset.instance.p.prefixName() + "§7Ballon n°§e " + choosedBall + " §asélectionné !");
                TitleUtils.sendActionBar(player,"§7Le ballon n°§e" + choosedBall + "§7 est §asélectionné !");
            }
        });
        selector.setRightClick(new RoleItem.RightClick() {
            @Override
            public void execute(Player player) {

                if(choosedBall == 0)
                    return;

                if(ballsAvailable.get(choosedBall - 1))
                {
                    player.sendMessage(Preset.instance.p.prefixName() + "§cCe ballon est cassé, tu ne peux pas t'y téléporté");
                    return;
                }

                onClick(player, choosedBall - 1);

                if(trappedBalls.get(choosedBall - 1))
                {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20*30, 0));
                    Location loc = player.getLocation();
                    if(inazumaUHC.rm.getRole(Scotty.class) != null)
                    {
                        player.sendMessage(Preset.instance.p.prefixName() + "Vous vous êtes téléprté à un ballon piégé. Vous recevez l'effet Slowness I pendant 30 secondes et Scotty recevra les coordonnées du ballon dans 10 secondes");
                        for(Player scotty : inazumaUHC.rm.getRole(Scotty.class).getPlayers())
                        {
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    scotty.sendMessage(Preset.instance.p.prefixName() + "Janus s'est téléporté à un ballon piégé. Voici ses coordonnées X: " + loc.getBlockX() + " Y: " + loc.getBlockY() + " Z: " + loc.getBlockZ());
                                }
                            }.runTaskLaterAsynchronously(inazumaUHC, 20*10);
                        }
                    }
                }



            }
        });

        addRoleItem(selector);

        new BukkitRunnable() {
            @Override
            public void run() {

                for(Location location : ballonsLoc)
                {
                    for( Player nearbyPlayer : PlayerUtils.getNearbyPlayers(location, 50, 50,50))
                    {
                        if(inazumaUHC.rm.getRole(nearbyPlayer) instanceof Scotty)
                        {
                            int locPositon = ballonsLoc.indexOf(location);
                            if(!trappedBalls.get(locPositon) && !notifBalls.get(locPositon))
                            {
                                Scotty scotty = (Scotty) inazumaUHC.rm.getRole(nearbyPlayer);
                                scotty.trapBall(ballonsLoc.indexOf(location), true);
                                notifBalls.set(locPositon, true);
                            }
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(inazumaUHC, 1, 20*7);


    }

    @EventHandler
    public void onSwitchItem(PlayerItemHeldEvent event)
    {
        ItemStack is = event.getPlayer().getInventory().getItem(event.getNewSlot());
        if(is == null)
            return;

        if(is.hasItemMeta() && is.getItemMeta().hasDisplayName() && is.getItemMeta().getDisplayName().equalsIgnoreCase("Sélecteur de Ballons"))
        {
            if(i != 0)
                TitleUtils.sendActionBar(event.getPlayer(),"§7Le ballon n°§e" + choosedBall + "§7 est §asélectionné !");
        }
    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){

       if(ballonsBlock.containsKey(event.getBlock())){
        event.setCancelled(true);
        event.getBlock().setType(Material.AIR);
        for(Player player : getPlayers()){
            player.sendMessage(Preset.instance.p.prefixName()+" §cUn de tes ballons ce sont cassé, tu as donc perdu 1 coeur permanent.");
            PatchedEntity.setMaxHealthInSilent(player, player.getMaxHealth()-2);

        }
           ItemStack barrier = new ItemBuilder(Material.BARRIER).setName("§cCassé").toItemStack();
        switch (ballonsLoc.indexOf(ballonsBlock.get(event.getBlock()))){
            case 0:
                ballsAvailable.set(0, true);
                break;
            case 1:
                ballsAvailable.set(1, true);
                break;
            case 2:
                ballsAvailable.set(2, true);
                break;

        }
        ballonsLoc.remove(ballonsBlock.get(event.getBlock()));
        ballonsBlock.remove(event.getBlock());
       }

        if(event.getBlock().equals(xavierBlock)){
            for(Player player : getPlayers()){
                player.sendMessage(Preset.instance.p.prefixName()+" §cUn de tes ballons c'est cassé, tu as donc perdu 1 coeur permanent.");
                PatchedEntity.setMaxHealthInSilent(player, player.getMaxHealth()-2);

            }
            Xavier xavier = (Xavier) inazumaUHC.rm.getRole(Xavier.class);
            if(xavier != null){
                for(Player player :  inazumaUHC.rm.getRole(Xavier.class).getPlayers()){
                    player.sendMessage(Preset.instance.p.prefixName()+" §cTon ballon c'est cassé.");

                }
                xavier.setBlock(null);
                xavier.setLocation(null);
            }

            ItemStack barrier = new ItemBuilder(Material.BARRIER).setName("§cCassé").toItemStack();
        }

    }
    private void onClick(Player player,int i){
        if(Episode.getEpisode() == this.episode){
            player.sendMessage(Preset.instance.p.prefixName()+ " §cVous vous êtes déja téléporté cette épisode.");
            return;
        }

        if(ballonsLoc.size() > i){
            Location tpLoc = getTop(ballonsLoc.get(i));
            if(tpLoc == null){
                player.sendMessage(Preset.instance.p.prefixName()+" §cVous ne pouvez pas vous téléportez à votre ballon, car celui-ci est obstrué par plus de 3 blocks.");
                return;
            }

            player.teleport(tpLoc);
            InazumaUHC.get.invincibilityDamager.addPlayer(player, 1000);
            player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1,1);
            player.sendMessage(Preset.instance.p.prefixName()+ " §cVous vous êtes téléporte au §7Ballon n°§e"+(i+1));
            this.episode = Episode.getEpisode();
            return;
        }
            player.sendMessage(Preset.instance.p.prefixName()+ " §cLe §7Ballon n°§e"+(i+1)+" n'existe pas");
    }


    private Location getTop(Location location){
        Location cLoc = location.clone();
        int t = 0;
        int b = 0;
        int a = 0;
        for (int j = 1; j < 255-cLoc.getBlockY(); j++) {
            if(a >= 2){
                cLoc.add(0,-2,0);
                return cLoc;
            }
            cLoc.add(0,j,0);
            if(!cLoc.getWorld().getBlockAt(cLoc).getType().equals(Material.AIR)){
                t++;
                b++;
                a = 0;
                if(t > 2){
                    return null;
                }

            }else {
                a++;
                b++;
            }
        }
        return cLoc;
    }

}
