package be.alexandre01.inazuma_eleven.categories;

import be.alexandre01.inazuma.uhc.roles.RoleCategory;

public class Alius extends RoleCategory {
    public Alius(String category, String prefixColor, String winMessage) {
        super(category, prefixColor, winMessage);
        setDeathMessage(new String[]{"§5§lL'§lAcadémie §lAlius§7 vient de perdre un joueur.","§5§l%player% §7était §f: §l%role%"});
    }
}
