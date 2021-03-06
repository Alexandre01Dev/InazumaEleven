package be.alexandre01.inazuma_eleven.roles.raimon;

import be.alexandre01.inazuma.uhc.InazumaUHC;
import be.alexandre01.inazuma.uhc.managers.player.PlayerMovementManager;
import be.alexandre01.inazuma.uhc.presets.IPreset;
import be.alexandre01.inazuma.uhc.presets.Preset;

import be.alexandre01.inazuma.uhc.roles.Role;
import be.alexandre01.inazuma.uhc.roles.RoleItem;
import be.alexandre01.inazuma.uhc.utils.*;
import be.alexandre01.inazuma_eleven.categories.Raimon;
import be.alexandre01.inazuma_eleven.roles.alius.Gazelle;
import be.alexandre01.inazuma_eleven.roles.alius.Janus;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Scotty extends Role {

    boolean hasTrap = false;
    int currentBall;


    public Scotty(IPreset preset) {
        super("Scotty Banyan",preset);
        setRoleCategory(Raimon.class);
        addDescription("https://blog.inazumauhc.fr/inazuma-eleven-uhc/roles/raimon/scotty-banyan");
        /*addDescription("§8- §7Votre objectif est de gagner avec §6§lRaimon");
        addDescription("§8- §7Vous disposez de §b§lSpeed 1§7");
        addDescription(" ");
        addDescription("§8- §7Étant très malicieux, vous disposez de 2 pièges qui sont les suivants :");
        CustomComponentBuilder c = new CustomComponentBuilder("");
        c.append("§8- §7");

        BaseComponent bananaButton = new TextComponent("§ePeau de banane");

        BaseComponent bananaDesc = new TextComponent();
        bananaDesc.addExtra("§e- §9Utilisation selon le nombre d'§eitem\n");
        bananaDesc.addExtra("§e- §9Donne au joueur marchant dessus §8Blindness 10§9 et §8Slowness 10§9 durant §a4 secondes.");
        bananaButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,bananaDesc.getExtra().toArray(new BaseComponent[0])));
        c.append(bananaButton);
        addDescription(c);

        CustomComponentBuilder d = new CustomComponentBuilder("");

        d.append("§8- §7");

        BaseComponent casierButton = new TextComponent("§eCasiers Cadenet");

        BaseComponent casierDesc = new TextComponent();
        casierDesc.addExtra("§e- §9Utilisation selon le nombre d'§eitem\n");
        casierDesc.addExtra("§e- §9Téléporte le joueur marchant dessus pendant §a20 secondes.");
        casierButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,casierDesc.getExtra().toArray(new BaseComponent[0])));
        d.append(casierButton);
        addDescription(d);*/



        onLoad(new load() {
            @Override
            public void a(Player player) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0,false,false), true);

                /*new BukkitRunnable(){
                    @Override
                    public void run(){

                        for(Player target : PlayerUtils.getNearbyPlayersFromPlayer(player,15,15,15)) {

                            target.sendMessage("§3§lScotty§8»§3 Hihihihi");
                            player.sendMessage("§3§lScotty§8»§3 Hihihihi");

                        }

                    }

                }.runTaskTimerAsynchronously(InazumaUHC.get, 1,20*60*5);*/

            }


        });


        addCommand("trap", new command() {
            @Override
            public void a(String[] strings, Player player) {
                if(strings[0].equalsIgnoreCase("yes"))
                {
                    hasTrap = true;
                    if(inazumaUHC.rm.getRole(Janus.class) != null)
                    {
                        Janus janus = (Janus) inazumaUHC.rm.getRole(Janus.class);
                        janus.trappedBalls.set(currentBall, true);
                        player.sendMessage(Preset.instance.p.prefixName() + "§aPiège de ballons réussi avec succès ! §7Quand Janus s'y téléportera il recevra l'effet slowness I pendant 30 secondes et vous recevrez les coordonnées du ballon 10 secondes après la téléportation");
                    }
                }
                else if(strings[0].equalsIgnoreCase("no"))
                {
                    player.sendMessage(Preset.instance.p.prefixName() + "§7Vous avez décidé de ne pas pieger ce ballon");
                }
            }
        });

        RoleItem roleItem = new RoleItem();
        roleItem.setItemstack(new ItemBuilder(Material.STRING,3).setName("§ePeau de banane").toItemStack());
        roleItem.setPlaceableItem(false);

        RoleItem cadenet = new RoleItem();
        cadenet.setItemstack(new ItemBuilder(Material.STRING,2).setName("§eCasiers Cadenet").toItemStack());

        cadenet.setPlaceableItem(false);


        roleItem.setPlaceBlock(new RoleItem.PlaceBlock() {
            @Override
            public void execute(Player player, Block block) {
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


                player.sendMessage(Preset.instance.p.prefixName()+" §aPiège posé ! §e| §7X:"+ block.getLocation().getBlockX()+"§8| §7Y:"+block.getLocation().getBlockY()+ "§8| §7Z:"+block.getLocation().getBlockZ() );
                Block finalBlock = block;
                inazumaUHC.playerMovementManager.addBlockLocation(block.getLocation(), new PlayerMovementManager.action() {
                    @Override
                    public void a(Player player) {
                        if(!getPlayers().contains(player)){
                            player.sendMessage(Preset.instance.p.prefixName()+" §cVous venez de marcher sur une peau de banane.");
                            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 4*20, 9,false,false), true);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 4*20, 9,false,false), true);
                            inazumaUHC.playerMovementManager.remBlockLocation(finalBlock.getLocation());

                            for(Player s : getPlayers()){
                                s.sendMessage(Preset.instance.p.prefixName()+ "Un joueur vient de glisser sur un de vos pièges.");
                            }
                        }
                    }
                });

            }
        });
        addRoleItem(roleItem);


        cadenet.setPlaceBlock(new RoleItem.PlaceBlock() {
            @Override
            public void execute(Player player, Block block) {

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


                player.sendMessage(Preset.instance.p.prefixName()+" §aPiège posé ! §e| §7X:"+ block.getLocation().getBlockX()+"§8| §7Y:"+block.getLocation().getBlockY()+ "§8| §7Z:"+block.getLocation().getBlockZ() );
                Block finalBlock = block;
                inazumaUHC.playerMovementManager.addBlockLocation(block.getLocation(), new PlayerMovementManager.action() {
                    @Override
                    public void a(Player player) {
                        if(!getPlayers().contains(player)){
                            player.sendMessage(Preset.instance.p.prefixName()+" §cVous venez d'être enfermé dans un casier'.");

                            addPlayerLock(player);
                            inazumaUHC.playerMovementManager.remBlockLocation(finalBlock.getLocation());

                            for(Player s : getPlayers()){
                                s.sendMessage(Preset.instance.p.prefixName()+ "Un joueur vient d'être enfermer sur un de vos pièges.");
                            }
                        }
                    }
                });

            }
        });
        cadenet.setSlot(7);
        addRoleItem(cadenet);

    }

    private void addPlayerLock(Player player){
        NPC npc = new NPC(player,player.getName(),player.getLocation());
        npc.spawn();
        npc.setCamera(player,true);
        Location pLoc = player.getLocation();
        double y = pLoc.getY();
        pLoc.setY(1);
        player.teleport(pLoc);
        player.playSound(player.getLocation(), Sound.FALL_SMALL,1,1);
        pLoc.setY(y);
        new BukkitRunnable() {
            int sec = 0;
            @Override
            public void run() {
                switch (sec){
                    case 0:
                        TitleUtils.sendTitle(player,0,30,0,"§eYou got §eS§3c§co§2t§6t§ae§4d","( ﾟ▽ﾟ)/");
                        break;
                    case 1:
                        TitleUtils.sendTitle(player,0,30,0,"§eYou got §9S§ec§2o§at§4t§2e§bd","\\\\(ﾟ▽ﾟ )");
                        break;
                    case 2:
                        TitleUtils.sendTitle(player,0,30,0,"§eYou got §3S§5c§7o§bt§at§ee§cd","( ﾟ▽ﾟ)/");
                        break;

                    case 3:
                        TitleUtils.sendTitle(player,0,30,0,"§eYou got §2S§6c§eo§at§at§9e§rd","\\\\(ﾟ▽ﾟ )");
                        break;
                    case 4:
                        TitleUtils.sendTitle(player,0,30,0,"§eYou got §9S§ec§2o§at§4t§2e§bd","( ﾟ▽ﾟ)/");
                        break;
                    case 5:
                        TitleUtils.sendTitle(player,0,30,0,"§eAttends je te libère?","dans 3");
                        break;
                    case 8:
                        TitleUtils.sendTitle(player,0,30,0,"§eAttends je te libère?","dans 2");
                        break;
                    case 9:
                        TitleUtils.sendTitle(player,0,30,0,"§eAttends je te libère?","dans 1");
                        break;
                    case 10:
                        TitleUtils.sendTitle(player,0,30,0,"§eAttends je te libère?","dans 2");
                        break;
                    case 11:
                        TitleUtils.sendTitle(player,0,30,0,"§eAttends je te libère?","dans 3");
                        break;
                    case 12:
                        TitleUtils.sendTitle(player,0,30,0,"§f."," ");
                        break;
                    case 13:
                        TitleUtils.sendTitle(player,0,30,0,"§f.."," ");
                        break;
                    case 14:
                        TitleUtils.sendTitle(player,0,30,0,"§f..."," ");
                        break;
                    case 15:
                        TitleUtils.sendTitle(player,0,30,0,"§fToujours bloqué ?"," ");
                        break;

                    case 17:
                        TitleUtils.sendTitle(player,0,30,0,"§eHmm","ça s'ouvre pas, dommage");
                        break;
                    case 19:
                        TitleUtils.sendTitle(player,0,30,0,"§cLol","Je m'amuse bien avec toi.");
                        break;
                    case 20:
                        Bukkit.getScheduler().scheduleSyncDelayedTask(inazumaUHC, new Runnable() {
                            @Override
                            public void run() {
                                player.teleport(pLoc);
                                player.sendMessage(Preset.instance.p.prefixName()+ "§aVous êtes libéré vous même et je sais que vous détestez Scotty dorénavent.");
                                npc.setCamera(player,false);
                                npc.destroy();
                            }
                        });
                        cancel();
                }


                sec++;
            }
        }.runTaskTimerAsynchronously(inazumaUHC,20,20);
    }


    public void trapBall(int i, boolean isNear)
    {

        currentBall = i;
        if(!isNear)
        {
            for(Player scotty : getPlayers())
            {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        scotty.sendMessage(Preset.instance.p.prefixName()+" Janus vient de poser on ballon.");

                    }
                }.runTaskLaterAsynchronously(inazumaUHC, 20*60*2);
            }
        }

        if(!hasTrap && isNear)
        {
            for(Player scotty : getPlayers())
            {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        BaseComponent b = new TextComponent(Preset.instance.p.prefixName()+" Vous êtes passé à côté d'un ballon de Janus. §7Vouez-vous le piéger ?");
                        b.addExtra("");
                        BaseComponent yes = new TextComponent("§a[§2OUI§a]");
                        yes.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/trap yes"));
                        b.addExtra(yes);
                        b.addExtra(" §7ou ");
                        BaseComponent no = new TextComponent("§c[§4NON§c]");
                        no.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/trap no"));

                        b.addExtra(no);

                        scotty.spigot().sendMessage(b);
                    }
                }.runTaskLaterAsynchronously(inazumaUHC,20*60);

            }
        }
    }


}

