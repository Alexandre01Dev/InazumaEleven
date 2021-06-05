package be.alexandre01.inazuma_eleven.roles.raimon;

import be.alexandre01.inazuma.uhc.InazumaUHC;
import be.alexandre01.inazuma.uhc.custom_events.player.PlayerInstantDeathEvent;
import be.alexandre01.inazuma.uhc.managers.damage.DamageManager;
import be.alexandre01.inazuma.uhc.presets.IPreset;
import be.alexandre01.inazuma.uhc.presets.Preset;

import be.alexandre01.inazuma.uhc.roles.Role;
import be.alexandre01.inazuma.uhc.roles.RoleItem;
import be.alexandre01.inazuma.uhc.utils.CustomComponentBuilder;
import be.alexandre01.inazuma.uhc.utils.ItemBuilder;
import be.alexandre01.inazuma_eleven.categories.Raimon;
import be.alexandre01.inazuma_eleven.roles.alius.Gazelle;
import be.alexandre01.inazuma_eleven.roles.alius.Torch;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.Tuple;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Aiden extends Role implements Listener {

    private Player shawnKiller = null;
    public Aiden(IPreset preset) {
        super("Aiden Frost",preset);
        setRoleCategory(Raimon.class);
        addListener(this);
        addDescription("§8- §7Votre objectif est de gagner avec §6§lRaimon");
        addDescription("§8- §7Vous possédez l’effet §4§lForce 1§7.");
        addDescription(" ");
        CustomComponentBuilder c = new CustomComponentBuilder("");
        c.append("§8- §7Vous possédez également le ");

        BaseComponent blizzardButton = new TextComponent("§3Blizzard Eternel §7*§8Curseur§7*");

        BaseComponent blizzardDesc = new TextComponent();
        blizzardDesc.addExtra("§e- §9Utilisation par §eEpisode\n");
        blizzardDesc.addExtra("§e- §9Donne au joueur §7ciblé, §8Slowness 2§9 pendant §a7 secondes");
        blizzardButton.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,blizzardDesc.getExtra().toArray(new BaseComponent[0])));
        c.append(blizzardButton);
        addDescription(c);
        addDescription(" ");
        addDescription("§8- §7Les attaques de §bShawn§7 ne vous atteignent pas.");

        setRoleToSpoil(Shawn.class);
        onLoad(new load() {
            @Override
            public void a(Player player) {
                inazumaUHC.dm.addEffectPourcentage(player, DamageManager.EffectType.INCREASE_DAMAGE,1,110);
                player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0,false,false), true);
            }

        });

        ItemBuilder itemBuilder = new ItemBuilder(Material.PACKED_ICE);
        itemBuilder.setName("§3Blizzard Eternel");

        RoleItem blizzardEternel = new RoleItem();


        blizzardEternel.deployVerificationsOnRightClickOnPlayer(blizzardEternel.generateVerification(new Tuple<>(RoleItem.VerificationType.EPISODES,1)));
        blizzardEternel.setRightClickOnPlayer(15,new RoleItem.RightClickOnPlayer() {
            @Override
            public void execute(Player player, Player rightClicked) {
                player.sendMessage(Preset.instance.p.prefixName()+"Vous avez utilisé votre §3Blizzard Eternel§7 sur §c"+ rightClicked.getName());
                if(!inazumaUHC.rm.getRole(rightClicked).getClass().equals(Torch.class) && !inazumaUHC.rm.getRole(rightClicked).getClass().equals(Axel.class) &&  !inazumaUHC.rm.getRole(rightClicked).getClass().equals(Gazelle.class)){
                    rightClicked.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*10,1));
                    rightClicked.sendMessage(Preset.instance.p.prefixName()+"§7Vous avez été touché par le §3Blizzard Eternel");
                }
                if(inazumaUHC.rm.getRole(rightClicked).getClass().equals(Torch.class) && inazumaUHC.rm.getRole(rightClicked).getClass().equals(Axel.class) &&  inazumaUHC.rm.getRole(rightClicked).getClass().equals(Gazelle.class)){
                    rightClicked.sendMessage(Preset.instance.p.prefixName()+"Vous avez été touché par le §3Blizzard Eternel§7, mais en vain");
                }
            }
        });

        blizzardEternel.setItemstack(itemBuilder.toItemStack());
        addRoleItem(blizzardEternel);
        addListener(this);
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerInstantDeathEvent event){
        Player killer = event.getKiller();
        Player killed = event.getPlayer();
        Role role = inazumaUHC.rm.getRole(killed);
        if(role.getClass() == Shawn.class){
            shawnKiller = killer;
            int i = 0;
            for(Player player : inazumaUHC.rm.getRole(Aiden.class).getPlayers()){
                player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
                player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,Integer.MAX_VALUE,0,false,false), true);

                if (i == 0){
                    i++;

                    new BukkitRunnable(){
                        @Override
                        public void run(){
                            player.sendMessage(Preset.instance.p.prefixName()+"Shawn a été tué par " + shawnKiller.getName() + ", vous devez désormais le tuer afin de perdre l'effet Weakness et récupérer votre Force.");

                        }

                    }.runTaskLaterAsynchronously(InazumaUHC.get, 1);

                }

            }
        }

        if(shawnKiller != null && shawnKiller == killed){

            if (inazumaUHC.rm.getRole(killer.getUniqueId()).getClass().equals(Aiden.class)){
                for(Player player : inazumaUHC.rm.getRole(Aiden.class).getPlayers()){

                    player.removePotionEffect(PotionEffectType.WEAKNESS);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0,false,false), true);

                    new BukkitRunnable(){
                        @Override
                        public void run(){

                            player.sendMessage(Preset.instance.p.prefixName()+"§7Vous venez de tuer §4§l" + shawnKiller.getName() + " §7vous avez donc perdu Weakness et re gagné votre Force§7." );
                            shawnKiller = null;
                        }

                    }.runTaskLaterAsynchronously(InazumaUHC.get, 1);



                }
            }
              else if (!inazumaUHC.rm.getRole(killer.getUniqueId()).getClass().equals(Aiden.class)){
                    for(Player player : inazumaUHC.rm.getRole(Aiden.class).getPlayers()){

                        new BukkitRunnable(){
                            @Override
                            public void run(){

                                player.sendMessage(Preset.instance.p.prefixName()+"§7Le tueur de Shawn est mort par un autre joueur que vous, en conséquence vous gardez Weakness" );
                                shawnKiller = null;

                            }

                        }.runTaskLater(InazumaUHC.get, 1);



                    }
              }

        }


    }

    }
