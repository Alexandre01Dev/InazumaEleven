package be.alexandre01.inazuma_eleven.categories;

import be.alexandre01.inazuma.uhc.roles.RoleCategory;

public class Solo extends RoleCategory {
    public Solo(String category, String prefixColor, String winMessage) {
        super(category, prefixColor, winMessage);
        setDeathMessage(new String[]{"§4L'ange déchu§7 s'est abattu sur §cl'envoyé§7 des §6Dieux,","§e§l%player% §7était §f: §l%role%"});


    }
}
