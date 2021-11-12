package be.alexandre01.inazuma_eleven.categories;

import be.alexandre01.inazuma.uhc.roles.RoleCategory;

public class Raimon extends RoleCategory {
    public Raimon(String category, String prefixColor, String winMessage) {
        super(category, prefixColor, winMessage);
        setDeathMessage(new String[]{"§6§lRaimon§7 vient de perdre un joueur.","§6§l%player% §7était §f: §l%role%"});
    }
}
