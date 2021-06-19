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
                    objectiveSign.setDisplayName("§3§lInazuma §3§lEleven§8 §c§lV1");

                    objectiveSign.setLine(4,    "§l§8»§f§lInfos§l§8«");
                    objectiveSign.setLine(5,"§7Episode §f§l» §e"+ Episode.getEpisode());
                    objectiveSign.setLine(6, "§7Joueurs §f§l» §e" + InazumaUHC.get.getRemainingPlayers().size());
                    objectiveSign.setLine(7,    "§l§8»§f§lTimer§l§8«");
                    objectiveSign.setLine(8, "§7Temps §f§l» §e" + inazuma.totalTimeValue);
                    objectiveSign.setLine(9, "§7PvP §f§l» §e" + inazuma.pvpValue);
                    if(inazuma.hasNether()){
                        objectiveSign.setLine(10,"§7Meetup §f§l» §e" + inazuma.netherValue);
                    }
                    objectiveSign.setLine(11,"§7Meetup §f§l» §e" + inazuma.bordureValue);
                    objectiveSign.setLine(12,    "§l§8»§f§lBordure§l§8«");
                    World world = player.getWorld();
                    int borderSize = (int) world.getWorldBorder().getSize() /2;
                    objectiveSign.setLine(18,"§7Bordure §l» §e-"+ borderSize+"§7/§e"+borderSize);
                    objectiveSign.setLine(19,"§7Centre §l» §e "+ inazuma.getArrows().get(player.getUniqueId()));
                    objectiveSign.setLine(20,    "§8§m------------");
                    objectiveSign.setLine(22, ip);

                    objectiveSign.updateLines();
                }
            });
            return ps;
        };

    }
}

