package be.alexandre01.inazuma_eleven.scoreboards;

import be.alexandre01.inazuma.uhc.InazumaUHC;

import be.alexandre01.inazuma.uhc.presets.IPreset;
import be.alexandre01.inazuma.uhc.presets.PresetData;
import be.alexandre01.inazuma.uhc.scoreboard.IScoreBoard;
import be.alexandre01.inazuma.uhc.scoreboard.ObjectiveSign;
import be.alexandre01.inazuma.uhc.scoreboard.PersonalScoreboard;
import be.alexandre01.inazuma.uhc.utils.Episode;
import be.alexandre01.inazuma_eleven.InazumaEleven;
import org.bukkit.World;

public class GameScoreboard {
    PresetData inazuma;
    String scenario;
    public GameScoreboard(PresetData inazuma){
        this.inazuma = inazuma;
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
                    objectiveSign.setDisplayName("§3§lInazuma §3§lEleven");

                    objectiveSign.setLine(1, "§8» §6§lInformations:");
                    objectiveSign.setLine(3, "  §8» §7Episode: §a"+Episode.getEpisode());
                    objectiveSign.setLine(4, "  §8» §7Joueurs: §c"+InazumaUHC.get.getRemainingPlayers().size());
                    objectiveSign.setLine(5, "§e");
                    objectiveSign.setLine(6, "§8» §6§lTimer:");
                    objectiveSign.setLine(7, "  §8» §7Temps: §a"+inazuma.totalTimeValue);
                    objectiveSign.setLine(8, "  §8» §7PvP: §a"+inazuma.pvpValue);
                    if(inazuma.hasNether()){
                        objectiveSign.setLine(10,"§7Meetup §f§l» §a" + inazuma.netherValue);
                    }
                    objectiveSign.setLine(9, "  §8» §7Meetup: §a"+inazuma.bordureValue);
                    objectiveSign.setLine(10, "§f");
                    objectiveSign.setLine(11, "§8» §6§lBordure:");
                    World world = player.getWorld();
                    int borderSize = (int) world.getWorldBorder().getSize() /2;
                    objectiveSign.setLine(12, "  §8» §7Bordure: §b± "+ borderSize);
                    objectiveSign.setLine(13, "  §8» §7Centre: §6"+inazuma.getArrows().get(player.getUniqueId()));
                    objectiveSign.setLine(14, "§9");
                    objectiveSign.setLine(15, ip);

                    objectiveSign.updateLines();
                }
            });
            return ps;
        };

    }
}

