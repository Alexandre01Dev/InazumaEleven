package be.alexandre01.inazuma_eleven.categories;

import be.alexandre01.inazuma.uhc.roles.RoleCategory;

public class Raimon extends RoleCategory {
    public Raimon(String category, String prefixColor) {
        super(category, prefixColor);
        setDeathMessage(new String[]{"§6§lRaimon§7 vient de perdre un joueur","§e%player% §7était §f: %role%"});
    }
}
