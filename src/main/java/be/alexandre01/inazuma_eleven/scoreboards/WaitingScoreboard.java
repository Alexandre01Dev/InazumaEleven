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

                    objectiveSign.setLine(2, "§e ");
                    objectiveSign.setLine(3, "§e§lHost§7 §f§l» §c§lNan0uche");
                    objectiveSign.setLine(4, "§7Joueurs §f§l» §e" + Bukkit.getOnlinePlayers().size() + "§7/§e"+inazuma.getPlayerSize());
                    objectiveSign.setLine(5, "§e ");
                    objectiveSign.setLine(6, "§7Mode de jeu §f§l» §3§lInazuma §lEleven");
                    objectiveSign.setLine(7, "§7Scénario(s) §f§l» "+scenario);
                    objectiveSign.setLine(8, "§e ");
                    objectiveSign.setLine(9, ip);

                    objectiveSign.updateLines();

                }
            });
            return ps;
        };

    }
}

