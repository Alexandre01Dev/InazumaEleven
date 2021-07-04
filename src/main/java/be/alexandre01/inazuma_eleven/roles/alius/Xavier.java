package be.alexandre01.inazuma_eleven.roles.alius;

import be.alexandre01.inazuma.uhc.InazumaUHC;
import be.alexandre01.inazuma.uhc.custom_events.episode.EpisodeChangeEvent;
import be.alexandre01.inazuma.uhc.custom_events.player.PlayerInstantDeathEvent;
import be.alexandre01.inazuma.uhc.managers.damage.DamageManager;
import be.alexandre01.inazuma.uhc.presets.IPreset;
import be.alexandre01.inazuma.uhc.presets.Preset;

import be.alexandre01.inazuma.uhc.roles.Role;
import be.alexandre01.inazuma.uhc.roles.RoleItem;
import be.alexandre01.inazuma.uhc.timers.utils.DateBuilderTimer;
import be.alexandre01.inazuma.uhc.timers.utils.MSToSec;
import be.alexandre01.inazuma.uhc.utils.*;
import be.alexandre01.inazuma_eleven.InazumaEleven;
import be.alexandre01.inazuma_eleven.categories.Alius;
import be.alexandre01.inazuma_eleven.objects.MeteorEntity;
import be.alexandre01.inazuma_eleven.objects.Sphere;
import be.alexandre01.inazuma_eleven.roles.raimon.Darren;
import be.alexandre01.inazuma_eleven.roles.raimon.Jack;
import be.alexandre01.inazuma_eleven.roles.raimon.Jude;
import be.alexandre01.inazuma_eleven.roles.raimon.Mark;
import be.alexandre01.inazuma_eleven.roles.solo.Byron;
import be.alexandre01.inazuma_eleven.timer.DelayedTimeChangeTimer;
import lombok.Setter;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_8_R3.Tuple;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Xavier extends Role implements Listener {
    private boolean reunion = false;
    private int number = 0;
    private int i = 0;
    private Inventory inventory;
    private boolean shootArrow = false;
    private int episode;
    boolean meteor = false;
    boolean markDeath = false;
    boolean darrenAccept = false;
    @Setter
    private Block block = null;
     @Setter  Location location = null;
    public Xavier(IPreset preset) {
        super("Xavier Foster",preset);
        addDescription("§8- §7Votre objectif est de gagner avec §5§ll'§5§lAcadémie §5§lAlius");
        addDescription("§8- §7Vous possédez l’effet §6§l§4§lForce 1§7.");
        addDescription(" ");
        addDescription("§8- §7Vous disposez du §d§lCollier§7§l-§5§lAlius§7 qui vous donnera §b§lSpeed 1 §7et §6§lRésistance 1§7 (NERF) pendant §a1 minute 30§7.");
        addDescription(" ");
        CustomComponentBuilder c = new CustomComponentBuilder("");
        c.append("§8- §7Vous avez une commande nommée ");

        RoleItem meteor = new RoleItem();
        ItemBuilder it = new ItemBuilder(Material.BOW).setName("Météore Géant");
        meteor.setItemstack(it.toItemStack());
        meteor.setPlaceableItem(true);
        addRoleItem(meteor);

        BaseComponent inaballtpButton = new TextComponent("§5/inaballtp §7(§9Pseudo§7) §7*§8Curseur§7*");

        BaseComponent inaballtpDesc = new TextComponent();
        inaballtpDesc.addExtra("§e- §9Utilisation 2 fois uniquement §7[Cooldown par §eEpisode§7]\n");
        inaballtpDesc.addExtra("§e- §9Téléporte le joueur de votre choix a votre ballon\n");
        inaballtpDesc.addExtra("§e- §cAttention§9, vous devrez trouver §5Janus§9 afin qu'il vous donne les coordonnées du ballon");
        inaballtpButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,inaballtpDesc.getExtra().toArray(new BaseComponent[0])));
        c.append(inaballtpButton);
        addDescription(c);;
        addDescription(" ");
        addDescription("§8- §7Vous pouvez également voir ou se situent les différents ballons de §5Janus§7 avec le §5/inaball§7.");


        addListener(this);
        setRoleToSpoil(Bellatrix.class);
        setRoleToSpoil(Janus.class);
        setRoleCategory(Alius.class);

        onLoad(new load() {
            @Override
            public void a(Player player) {
                    inazumaUHC.dm.addEffectPourcentage(player, DamageManager.EffectType.INCREASE_DAMAGE,1,110);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0,false,false), true);
            }
        });
        addCommand("mr",(args, player) -> {
            MeteorEntity meteorEntity = MeteorEntity.spawn(player.getLocation());
            CraftEntity craftEntity = meteorEntity.getBukkitEntity();
            craftEntity.setVelocity(new Vector(0,-1,0));
        });

        /*addCommand("reunion start", new command() {
            @Override
            public void a(String[] args, Player player) {
                List<Player> list = new ArrayList<>();
                list.addAll(inazumaUHC.rm.getRole(Xavier.class).getPlayers());
                list.addAll(inazumaUHC.rm.getRole(Janus.class).getPlayers());
                list.addAll(inazumaUHC.rm.getRole(Dvalin.class).getPlayers());
                list.addAll(inazumaUHC.rm.getRole(Torch.class).getPlayers());
                list.addAll(inazumaUHC.rm.getRole(Gazelle.class).getPlayers());
                list.addAll(inazumaUHC.rm.getRole(Caleb.class).getPlayers());
                Bellatrix bellatrix = (Bellatrix) inazumaUHC.rm.getRole(Bellatrix.class);
                if (bellatrix.accepted){
                    list.addAll(bellatrix.getPlayers());
                }

                player.sendMessage("Vous avez start la réunion, tous les joueurs qui accepteront seront tp dans 5 minutes.");

                for(Player players :  list){
                    BaseComponent b = new TextComponent( Preset.instance.p.prefixName()+ getName() + " à start la réunion vous avez 5 minutes pour accepter.\n");
                    b.addExtra("§7Voulez vous la rejoindre ?");
                    BaseComponent yes = new TextComponent("§a[OUI]");
                    yes.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/reunion accept"));
                    b.addExtra(yes);
                    b.addExtra(" §7ou ");
                    BaseComponent no = new TextComponent("§a[NON]");
                    no.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/reunion refuse"));

                    b.addExtra(no);

                    players.spigot().sendMessage(b);

                    new BukkitRunnable() {

                        @Override
                        public void run() {

                            reunion = true;
                        }
                    }.runTaskLaterAsynchronously(inazumaUHC, 20*60*5);


                }

            }
        });

        addCommand("reunion", new command() {
            @Override
            public void a(String[] args, Player player) {

                if (reunion){
                    player.sendMessage("reunion deja start");
                }

                if(args.length == 0){
                    player.sendMessage(Preset.instance.p.prefixName()+" Veuillez mettre §a/reunion accept §7ou §a/reunion refuse");
                    return;
                }

                if(args[0].equalsIgnoreCase("accept")){
                    player.sendMessage(Preset.instance.p.prefixName()+" Vous avez accepté de rejoindre la réunion.");
                    accept(player);
                    number++;

                    new BukkitRunnable() {

                        @Override
                        public void run() {

                            List<Player> list = new ArrayList<>();
                            list.addAll(inazumaUHC.rm.getRole(Xavier.class).getPlayers());
                            list.addAll(inazumaUHC.rm.getRole(Janus.class).getPlayers());
                            list.addAll(inazumaUHC.rm.getRole(Dvalin.class).getPlayers());
                            list.addAll(inazumaUHC.rm.getRole(Torch.class).getPlayers());
                            list.addAll(inazumaUHC.rm.getRole(Gazelle.class).getPlayers());
                            list.addAll(inazumaUHC.rm.getRole(Caleb.class).getPlayers());
                            Bellatrix bellatrix = (Bellatrix) inazumaUHC.rm.getRole(Bellatrix.class);
                            if (bellatrix.accepted){
                                list.addAll(bellatrix.getPlayers());
                            }

                            TitleUtils.sendActionBar(player,"Réunion :"  + number + "/" + list.size());

                        }
                    }.runTaskTimerAsynchronously(inazumaUHC, 20*1, 20*1);
                    return;
                }
                if (args[0].equalsIgnoreCase("refuse")) {
                    player.sendMessage(Preset.instance.p.prefixName()+" Vous avez refusé de rejoindre la réunion.");
                    return;
                }
                player.sendMessage(Preset.instance.p.prefixName()+" Veuillez mettre §a/reunion accept §7ou §a/reunion refuse");
            }
        });*/

        RoleItem roleItem = new RoleItem();
        ItemBuilder itemBuilder = new ItemBuilder(Material.NETHER_STAR).setName("§d§lCollier§7§l-§5§lAlius");
        roleItem.setItemstack(itemBuilder.toItemStack());
        roleItem.deployVerificationsOnRightClick(roleItem.generateVerification(new Tuple<>(RoleItem.VerificationType.EPISODES,1)));
        roleItem.setRightClick(player -> {
            Jude.collierAlliusNotif(player.getLocation());
            Jack.nearAliusActivation(player.getLocation());
            player.sendMessage(Preset.instance.p.prefixName()+" Vous rentrez en résonance avec la §8§lpierre§7§l-§5§lalius.");
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60*2*20, 0,false,false), true);
        });
        addRoleItem(roleItem);
        setRoleToSpoil(Bellatrix.class, Janus.class);
        addCommand("inaball", new command() {
            @Override
            public void a(String[] args, Player player) {
                player.openInventory(inventory);
            }
        });
        addCommand("inaballtp", new command() {
            @Override
            public void a(String[] args, Player player) {
                if(args.length == 0){
                    player.sendMessage(Preset.instance.p.prefixName()+"§c Veuillez mettre /inaballtp [Joueur]");
                    return;
                }
                if(i >= 2){
                    player.sendMessage(Preset.instance.p.prefixName()+ " §cTu ne peux téléporter quelqu'un que 2x en total");

                    return;
                }
                if(Episode.getEpisode() == episode){
                    player.sendMessage(Preset.instance.p.prefixName()+ " §cTu ne peux téléporter quelqu'un que 1x tout les épisodes.");

                    return;
                }

                String arg = args[0];
                Player p = Bukkit.getPlayer(arg);
                if(p == null){
                    player.sendMessage(Preset.instance.p.prefixName()+"§c Le joueur n'existe pas");
                }

                if(!canTeleportPlayer(p)){
                    player.sendMessage(Preset.instance.p.prefixName()+" §cVous ne pouvez pas téléporter le joueur à votre ballon, car celui-ci est obstrué par plus de 3 blocks.");
                }else {
                    player.sendMessage(Preset.instance.p.prefixName()+" §aLe joueur a bien été téléporté.");
                    episode = Episode.getEpisode();
                    i++;
                }
            }
        });
    }
    private void accept(Player player){

        player.teleport(player.getLocation());

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        if(inazumaUHC.rm.getRole(player) instanceof Xavier){
        if(inventory == null){
            return;
        }
        if(event.getClickedInventory() == null){
            return;
        }
        if(event.getClickedInventory().getName() == null){
            return;
        }
        if(!event.getClickedInventory().getName().equals(inventory.getName()))
            return;
        switch (event.getSlot()){
            case 10:
            case 12:
            case 14:
                player.sendMessage(Preset.instance.p.prefixName()+" §cCe ballon est réservé à Janus.");
                break;
            case 16:
                onClick(player);
                break;
        }
        }
    }
    private boolean canTeleportPlayer(Player player){
        Location tpLoc = getTop(location);
        if(tpLoc == null){
            return false;
        }

        if(getPlayers().contains(player)){
            player.playSound(player.getLocation(), Sound.NOTE_PLING,5,1);
            player.sendMessage(Preset.instance.p.prefixName()+ " §cVous allez être téléporté à votre §5ballon§c dans §a10 secondes§c.");
            return true;
        }
        player.playSound(player.getLocation(), Sound.NOTE_PLING,5,1);
        player.sendMessage(Preset.instance.p.prefixName()+ " §c⚠ Attention, vous allez être téléporté au §5ballon§c de §5Xavier§c dans §a10 secondes§c en X:" + tpLoc.getBlockX() + "Z:" + tpLoc.getBlockZ());


        new BukkitRunnable(){
            @Override
            public void run(){

                player.teleport(tpLoc);
                InazumaUHC.get.invincibilityDamager.addPlayer(player, 1000);
                player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1,1);

            }

            }.runTaskLaterAsynchronously(InazumaUHC.get, 20*10);

        return true;

    }
    private void onClick(Player player){
        if(Episode.getEpisode() == this.episode){
            player.sendMessage(Preset.instance.p.prefixName()+ " §cTu ne peux te téléporter que tout les épisodes.");

            return;
        }

        if(location != null){
          if(!canTeleportPlayer(player)){
              player.sendMessage(Preset.instance.p.prefixName()+" §cVous ne pouvez pas vous téléportez à votre ballon, car celui-ci est obstrué par plus de 3 blocks.");
              return;
          }else {
              this.episode = Episode.getEpisode();
              i++;
          }
        }
        player.sendMessage(Preset.instance.p.prefixName()+ " §cLe ballon n'existe pas");

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

    @EventHandler
    public void onShoot(EntityShootBowEvent event){
        if(event.getEntity() instanceof  Player){
            Player player = (Player) event.getEntity();
            if(getPlayers().contains(player)){
                shootArrow = true;
            }
        }
    }

    @EventHandler
    public void onCollision(ProjectileHitEvent event){
        if(!shootArrow)
            return;
        if(event.getEntity() instanceof Arrow){
            Arrow arrow = (Arrow) event.getEntity();
            ProjectileSource projectileSource = arrow.getShooter();
            if(projectileSource instanceof Player){
                Player player = (Player) projectileSource;

                if (getPlayers().contains(player)) {
                    meteorDestruction(arrow.getLocation());
                }
            }
        }
    }

    @EventHandler
    public void onCollisionWithEntity(EntityDamageByEntityEvent event){

    }

    public void spawnMeteor(Location location){
        location.setY(150);
        MeteorEntity meteorEntity = MeteorEntity.spawn(location);
        meteorEntity.getBukkitEntity().setVelocity(new Vector(0,-0.5,0));
        new BukkitRunnable() {
            @Override
            public void run() {

                DelayedTimeChangeTimer delayedTimeChangeTimer = (DelayedTimeChangeTimer) InazumaUHC.get.tm.getTimer(DelayedTimeChangeTimer.class);
                if(delayedTimeChangeTimer.isRunning){
                    delayedTimeChangeTimer.cancel();
                }
                delayedTimeChangeTimer.setState(DelayedTimeChangeTimer.State.NIGHT);

                delayedTimeChangeTimer.runTaskTimerAsynchronously(InazumaUHC.get,0,1);
                meteor = false;
            }
        }.runTaskLaterAsynchronously(inazumaUHC,20*6);
    }
    public void meteorDestruction(Location location){
        if(meteor)
            return;

        meteor = true;
        DelayedTimeChangeTimer delayedTimeChangeTimer = (DelayedTimeChangeTimer) InazumaUHC.get.tm.getTimer(DelayedTimeChangeTimer.class);
        if(delayedTimeChangeTimer.isRunning){
            delayedTimeChangeTimer.cancel();
        }
        delayedTimeChangeTimer.setState(DelayedTimeChangeTimer.State.DAY);
        delayedTimeChangeTimer.runTaskTimerAsynchronously(InazumaUHC.get,0,1);

        new BukkitRunnable() {
            int i = 3;
            @Override
            public void run() {
                i--;
                if(i==2){
                    location.getWorld().playSound(location,Sound.ZOMBIE_WOODBREAK,1,1f);
                    Sphere sphere = new Sphere(location,2);
                    int j = 0;
                    for(Block block : sphere.getBlocks()){
                        if(j % 2 == 0){
                            if(block.getType() != Material.AIR)
                              block.setType(Material.NETHERRACK);
                        }
                        j++;
                    }
                }
                if(i==1){
                    location.getWorld().playSound(location,Sound.ZOMBIE_WOODBREAK,1,0.8f);
                    Sphere sphere = new Sphere(location,6);
                    int j = 0;
                    for(Block block : sphere.getBlocks()){
                        if(j % 6 == 0){
                            if(block.getType() != Material.AIR)
                                if(new Random().nextBoolean()){
                                    block.setType(Material.NETHERRACK);
                                }else {
                                    block.setType(Material.SOUL_SAND);
                                }
                        }
                        j++;
                    }
                }
                if(i == 0){
                    spawnMeteor(location);
                    cancel();
                }

            }
        }.runTaskTimer(inazumaUHC,20*1,20*1);
    }

    long l;
    private State state;
    private long delay;
    private long initialDelay;
    public enum State{
        DAY,NIGHT;
    }

    @EventHandler
    void onDeath(PlayerInstantDeathEvent event)
    {
        if(inazumaUHC.rm.getRole(event.getPlayer()) instanceof Mark)
        {
            markDeath = true;
        }
    }

    @EventHandler
    void onEpisodeChange(EpisodeChangeEvent event)
    {
        Darren darren = (Darren)inazumaUHC.rm.getRole(Darren.class);
        if(darren == null)
        {
            Bukkit.broadcastMessage("pas de darren");
            return;
        }


        if(!darren.accepted)
        {
            Bukkit.broadcastMessage("darren bah il est mechant et il a pas accepté");
            return;
        }


        for(Player player : getPlayers())
        {
            for (Player target : darren.getPlayers())
            {
                player.sendMessage(Preset.instance.p.prefixName() + "Darren se trouve en X: " + target.getLocation().getBlockX() + " Y: " + target.getLocation().getBlockY() + " Z: " + target.getLocation().getBlockZ());
            }
        }
    }

   /* public void timeDeploy(){
        World world;
        long from;
        long timesToExecute;
        long to;
        long addition;
        long timeCalc = 0;

        if(state.equals(State.DAY)){
            from = 1000L;
            to = 20000L;
        }else {
            from = 20000L;
            to = 1000L;
        }
        if(from > to){
            timeCalc = 24000L-from+to;
        }else {
            timeCalc = to-from;
        }

        org.bukkit.World w = InazumaUHC.get.worldGen.defaultWorld;
        timesToExecute = delay/initialDelay;
        addition = timeCalc/timesToExecute;

        w.setFullTime(from);

        new BukkitRunnable() {
            @Override
            public void run() {
                w.setFullTime(world.getTime()+addition);
                if(timesToExecute == 0){
                    w.setFullTime(to);
                    cancel();
                }
                timesToExecute--;
            }
        }.runTaskTimer()



    }*/

}
