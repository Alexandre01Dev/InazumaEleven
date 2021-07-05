package be.alexandre01.inazuma_eleven.scoreboards;

import be.alexandre01.inazuma.uhc.presets.Preset;
import be.alexandre01.inazuma.uhc.presets.PresetData;
import be.alexandre01.inazuma.uhc.presets.normal.Normal;
import be.alexandre01.inazuma.uhc.scoreboard.IScoreBoard;
import be.alexandre01.inazuma.uhc.scoreboard.ObjectiveSign;
import be.alexandre01.inazuma.uhc.scoreboard.PersonalScoreboard;

import be.alexandre01.inazuma_eleven.InazumaEleven;
import org.bukkit.Bukkit;
import org.bukkit.World;

public class WaitingScoreboard {
    PresetData inazuma;
    String scenario;
    public WaitingScoreboard(PresetData inazuma){
        this.inazuma = inazuma;
        setScoreboard();
        if(inazuma.hasScenario()){
            if(!inazuma.getScenarios().isEmpty()){

            if(inazuma.getScenarios().size()> 1){
                scenario = "§a/scenario";
            }else {
               scenario = "§a"+inazuma.getScenarios().get(0).getName();
            }
        }else {
            scenario = "§cAucun";
        }

        }else {
            scenario = "§cAucun";
        }
    }

    public void setScoreboard(){
        inazuma.i = player -> {
            PersonalScoreboard ps = new PersonalScoreboard(player);
            ps.setIScore(new IScoreBoard() {

                @Override
                public void lines(String ip, ObjectiveSign objectiveSign) {
                    objectiveSign.setDisplayName("§3§lInazuma §3§lEleven§8 §c§lV1");

                    objectiveSign.setLine(4, "§r§l§8»§8§m------------§l§8«");
                    objectiveSign.setLine(5, "§e§lHost§7 §l» §c§lNan0uche");
                    objectiveSign.setLine(6, "§7Joueurs §l» §e" + Bukkit.getOnlinePlayers().size() + "§7/§e"+inazuma.getPlayerSize());
                    objectiveSign.setLine(9, "§r§l§8»§8§m------------§l§8«§r");
                    World world = player.getWorld();
                    int borderSize = Preset.instance.p.getBorderSize(world.getEnvironment());
                    objectiveSign.setLine(10, "§7PvP §f§l» §3" + inazuma.pvpValue);
                    objectiveSign.setLine(12,"§7Meetup §f§l» §3" + inazuma.bordureValue);
                    objectiveSign.setLine(13,"§7Bordure §f§l» §e-"+ borderSize+"§7/§e"+borderSize);
                    objectiveSign.setLine(14, "§l§8»§8§m------------§l§8«");

                    objectiveSign.setLine(15, "§7Scénario(s) §l» "+scenario);


                    objectiveSign.setLine(16, "§l§8»§8§m------------§l§8« ");
                    objectiveSign.setLine(17, ip);

                    objectiveSign.updateLines();

                }
            });
            return ps;
        };

    }
}

