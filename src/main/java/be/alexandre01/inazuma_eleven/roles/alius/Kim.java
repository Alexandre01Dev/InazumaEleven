package be.alexandre01.inazuma_eleven.roles.alius;

import be.alexandre01.inazuma.uhc.InazumaUHC;
import be.alexandre01.inazuma.uhc.managers.damage.DamageManager;
import be.alexandre01.inazuma.uhc.presets.IPreset;
import be.alexandre01.inazuma.uhc.presets.Preset;

import be.alexandre01.inazuma.uhc.roles.Role;
import be.alexandre01.inazuma.uhc.roles.RoleItem;
import be.alexandre01.inazuma.uhc.utils.CustomComponentBuilder;
import be.alexandre01.inazuma.uhc.utils.Episode;
import be.alexandre01.inazuma.uhc.utils.ItemBuilder;
import be.alexandre01.inazuma.uhc.utils.TitleUtils;
import be.alexandre01.inazuma_eleven.InazumaEleven;
import be.alexandre01.inazuma_eleven.categories.Alius;
import be.alexandre01.inazuma_eleven.roles.raimon.Jude;
import lombok.Setter;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.Item;
import net.minecraft.server.v1_8_R3.Tuple;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Kim extends Role implements Listener {
    private int damage = 0;

    @Setter  Location location = null;
    public Kim(IPreset preset) {
        super("Kim Powell",preset);
        addDescription("§8- §7Votre objectif est de gagner avec §5§ll'§5§lAcadémie §5§lAlius");
        addDescription("§8- §7Vous possédez l’effet §6§l§4§lForce 1§7.");
        addDescription(" ");
        addDescription("§8- §7Vous disposez du §d§lCollier§7§l-§5§lAlius§7 qui vous donnera §b§lSpeed 1 §7et §6§lRésistance 1§7 (NERF) pendant §a1 minute 30§7.");
        addDescription(" ");
        CustomComponentBuilder c = new CustomComponentBuilder("");
        c.append("§8- §7Vous avez une commande nommée ");

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
        setRoleCategory(Alius.class);
        onLoad(new load() {
            @Override
            public void a(Player player) {

                new BukkitRunnable(){
                    @Override
                    public void run(){
                        TitleUtils.sendActionBar(player,"§3§lHealth §f§l: §6§l" + damage + "§6§l100%");
                    }
                }.runTaskTimerAsynchronously(InazumaUHC.getGet(), 20*2, 20*2);


            }
        });


        RoleItem volSword = new RoleItem();
        ItemBuilder volAlius = new ItemBuilder(Material.DIAMOND_SWORD).setName("Vol");
        volSword.setItemstack(volAlius.toItemStack());
        addRoleItem(volSword);

        RoleItem healSword = new RoleItem();
        ItemBuilder healAlius = new ItemBuilder(Material.DIAMOND_SWORD).setName("Heal");
        healSword.setItemstack(healAlius.toItemStack());
        healSword.deployVerificationsOnRightClick(healSword.generateVerification(new Tuple<>(RoleItem.VerificationType.COOLDOWN,5)));
        healSword.setRightClickOnPlayer(10,(player, rightClicked) -> {

            if(damage>=10){
                player.sendMessage(Preset.instance.p.prefixName()+" Vous avez regen de 1 coeur " + rightClicked);
                rightClicked.sendMessage(Preset.instance.p.prefixName()+ "Kim Powell vous a heal.");
                rightClicked.setHealth(2);
                damage = damage - 10;
            }
            else if(damage<10){
                player.sendMessage(Preset.instance.p.prefixName()+"Il vous faut minimum 10% pour healh un joueur.");
            }

        });

        healSword.deployVerificationsOnRightClick(healSword.generateVerification(new Tuple<>(RoleItem.VerificationType.COOLDOWN,10)));
        healSword.setRightClick(player -> {

            if(damage>=10){
                player.sendMessage(Preset.instance.p.prefixName()+" Vous vous êtes regen de 1 coeur ");
                player.setHealth(2);
                damage = damage - 10;

            }
            else if(damage<10){
                player.sendMessage(Preset.instance.p.prefixName()+"Il vous faut minimum 10% pour vous healh.");
            }

        });
        addRoleItem(healSword);







    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof  Player){
        Player player = (Player) e.getEntity();
        String itemname = player.getInventory().getItemInHand().getItemMeta().getDisplayName();
            if (player.getInventory().getItemInHand().getType() == Material.DIAMOND_SWORD && itemname.equalsIgnoreCase("Vol")){
                if(damage>=100){
                    player.sendMessage(Preset.instance.p.prefixName()+" Votre Heal Sword est remplit.");
                    return;
                }
                else if (damage<100){
                    damage = damage +5;
                    return;
                }
            }
        }
    }


}
