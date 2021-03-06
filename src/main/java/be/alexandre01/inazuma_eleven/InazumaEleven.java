package be.alexandre01.inazuma_eleven;

import be.alexandre01.inazuma.uhc.InazumaUHC;
import be.alexandre01.inazuma.uhc.generations.Plateform;
import be.alexandre01.inazuma.uhc.presets.IPreset;
import be.alexandre01.inazuma.uhc.presets.PresetData;
import be.alexandre01.inazuma.uhc.roles.Role;
import be.alexandre01.inazuma.uhc.roles.RoleManager;
import be.alexandre01.inazuma.uhc.scenarios.betazombie.BetaZombie;
import be.alexandre01.inazuma.uhc.scenarios.cancelenchant.CancelEnchant;
import be.alexandre01.inazuma.uhc.scenarios.cateyes.CatEyes;
import be.alexandre01.inazuma.uhc.scenarios.cutclean.Cutclean;
import be.alexandre01.inazuma.uhc.scenarios.diamondlimit.DiamondLimit;
import be.alexandre01.inazuma.uhc.scenarios.hasteyboys.HasteyBoys;
import be.alexandre01.inazuma.uhc.scenarios.rodless.RodLess;
import be.alexandre01.inazuma.uhc.scenarios.timber.Timber;
import be.alexandre01.inazuma.uhc.scenarios.trashpotion.TrashPotion;
import be.alexandre01.inazuma.uhc.scoreboard.IPersonalScoreBoard;
import be.alexandre01.inazuma.uhc.scoreboard.PersonalScoreboard;
import be.alexandre01.inazuma.uhc.timers.Timer;
import be.alexandre01.inazuma_eleven.categories.Alius;
import be.alexandre01.inazuma_eleven.categories.Raimon;
import be.alexandre01.inazuma_eleven.categories.Solo;
import be.alexandre01.inazuma_eleven.commands.Particles;
import be.alexandre01.inazuma_eleven.commands.Structure;
import be.alexandre01.inazuma_eleven.listeners.*;
import be.alexandre01.inazuma_eleven.objects.BallonInv;
import be.alexandre01.inazuma_eleven.objects.Capitaine;
import be.alexandre01.inazuma_eleven.roles.alius.*;
import be.alexandre01.inazuma_eleven.roles.raimon.*;
import be.alexandre01.inazuma_eleven.roles.solo.Byron;
import be.alexandre01.inazuma_eleven.timer.DelayedTimeChangeTimer;
import com.boydti.fawe.object.schematic.Schematic;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import lombok.Getter;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class InazumaEleven extends PresetData implements IPreset{
    @Getter
    private BallonInv ballonInv;
    public InazumaEleven(){
        //DefaultSettings Value
        generatorSettings = new String[]{"", ""};
        hasNether = false;
        minPlayerToStart = 2;
        playerSize = 20;
        totalTime = 60*60;
        teamSize = 1;
        pvpTime = 20*60;
        netherTime = 55*60;
        bordureTime = 100*60;
        borderSize = 1000;
        borderSizeNether = 150;
        endBordureTime = 60*15;
        endBordureSize = 250*2;
        scenarios = null;

        getScenarios();

        //INITIALZE OBJECTS
        ballonInv = new BallonInv();
        //INITIALIZE ROLESCATEGORIES
        new Raimon("??quipe Raimon","??6", "??lVictoire de ??6??lRaimon");
        new Alius("Acad??mie-Alius","??5","??lVictoire de ??5??ll'Acad??mie ??lAlius");
        new Solo("Solo","??c","cc");

        Capitaine.init();
        Capitaine.addCapitaine(Dvalin.class, Torch.class, Gazelle.class, Janus.class, Xavier.class);


        //INITIALIZE ROLES

        Role.addRoles(Axel.class,
                Nathan.class,
                Darren.class,
                Hurley.class,
                Jack.class,
                Jude.class,
                Kevin.class,
                Mark.class,
                Scotty.class,
                Shawn.class,
                William.class,
                Aiden.class,
                Bobby.class,
                Erik.class,

                Byron.class,

                Caleb.class,
                Xavier.class,
                Torch.class,
                Joseph.class,
                Janus.class,
                Gazelle.class,
                Dvalin.class,
                David.class,
                Nero.class,
                Kim.class,
                Bellatrix.class

        );

        Role.initializeRoles();


        Capitaine.getInstance().giveMdCommand();
      //  new Bellatrix(this);
        ClipboardFormat clipboardFormat = ClipboardFormat.findByFile(new File(InazumaUHC.get.getDataFolder().getAbsolutePath()+"/schematics/fawetest.fawe"));

        try {
            Schematic schematic = clipboardFormat.load(new File(InazumaUHC.get.getDataFolder().getAbsolutePath()+"/schematics/fawetest.fawe"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        InazumaUHC.get.registerCommand("structure",new Structure("structure"));
        InazumaUHC.get.registerCommand("particles",new Particles("particles"));
        playerSize = Role.getRoles().size();

        CustomGlasses items = new CustomGlasses();
        items.craftCustom();
    }

    @Override
    public boolean autoJoinWorld() {
        return false;
    }

    @Override
    public String getName() {
        return "InazumaEleven";
    }

    @Override
    public String prefixName() {
        return "??3Inazuma??8-??3Eleven??8????7 ";
    }

    @Override
    public boolean hasRoles() {
        return true;
    }

    @Override
    public String getPackageName() {
        return "be/alexandre01/inazuma_eleven";
    }

    @Override
    public ArrayList<Listener> getListeners() {
        if(listeners.isEmpty()){
            listeners.add(new PlayerEvent());
            listeners.add(new StateEvent(this));
            listeners.add(new ChunkEvent());
            listeners.add(new TimerEvent());
            listeners.add(new TeamsEvent());
            listeners.add(new EpisodeEvent());
            listeners.add(new WeatherEvent());
            listeners.add(new MysteryEvent());
            listeners.add(new ChunkSaver());
            listeners.add(new Capitaine());
            listeners.add(ballonInv);
        }
        return listeners;
    }

    @Override
    public ArrayList<Timer> getTimers() {
        if(timers.isEmpty()){
            timers.add(new DelayedTimeChangeTimer(DelayedTimeChangeTimer.State.DAY,1,20*2));
        }
        return timers;
    }

    @Override
    public ArrayList<Class<?>> getScenarios() {
        if(scenarios == null){
            scenarios = new ArrayList<>();
            System.out.println("SCENARIO CREATE!");
            scenarios.add(Cutclean.class);
            scenarios.add(CatEyes.class);
            scenarios.add(HasteyBoys.class);
            scenarios.add(Timber.class);
            scenarios.add(RodLess.class);
            scenarios.add(BetaZombie.class);
            scenarios.add(TrashPotion.class);
            scenarios.add(DiamondLimit.class);
            scenarios.add(CancelEnchant.class);
        }
        System.out.println("SCENARIO RETURN!");
        return scenarios;
    }

    @Override
    public ArrayList<IPersonalScoreBoard> getScoreboards() {
        return null;
    }

    @Override
    public boolean getNether() {
        return hasNether;
    }

    @Override
    public boolean canRespawnOnRejoin() {
        return true;
    }

    @Override
    public boolean isInvicible() {
        return isInvisible;
    }

    @Override
    public int getWaitingTime() {
        return waitingTime;
    }

    @Override
    public int getInvisibleTime() {
        return invisibilityTime*60;
    }

    @Override
    public int getMinPlayerToStart() {
        return minPlayerToStart;
    }

    @Override
    public int getTotalTime() {
        return totalTime;
    }

    @Override
    public int getPVPTime() {
        return pvpTime;//60*2;
    }

    @Override
    public int getNetherTime() {
        return netherTime;
    }

    @Override
    public int getBordureTime() {
        return 60*60;
    }

    @Override
    public int getEndBordure() {
        return 250*2;
    }
    @Override
    public int getEndBordureTime() {
        return 60*15;
    }
    @Override
    public Plateform getPlatform() {
        return null;
    }

    @Override
    public int getChunksArea() {
        return 300;
    }

    @Override
    public int nerfPotForce() {
        return 0;
    }

    @Override
    public int nerfPotResistance() {
        return 0;
    }

    @Override
    public int getPlayerSize() {
        return playerSize;
    }

    @Override
    public int getTeamSize() {
        return teamSize;
    }


    @Override
    public PersonalScoreboard getScoreboard(Player player) {
        return i.actual(player);
    }

    @Override
    public int getBorderSize(World.Environment environment) {
        int i = 0;
        switch (environment){
            case NORMAL:
                i = borderSize;
                break;
            case NETHER:
                i = borderSizeNether;
                break;
        }
        return i;
    }



    @Override
    public String getGeneratorSettings(World.Environment environment) {
        String s = "";
        switch (environment){
            case NORMAL:
                s = generatorSettings[0];
                break;
            case NETHER:
                s = generatorSettings[1];
                break;
        }
        return s;
    }

    @Override
    public String getRandomTickSpeed(World.Environment environment) {
        String s = "";
        switch (environment){
            case NORMAL:
                s = "15";
                break;
            case NETHER:
                s = "2";
                break;
        }
        return s;
    }

    @Override
    public String getNaturalRegeneration(World.Environment environment) {
        String s = "";
        switch (environment){
            case NORMAL:
                s = "false";
                break;
            case NETHER:
                s = "false";
                break;
        }
        return s;
    }

    @Override
    public boolean isArrowCalculated() {
        return true;
    }

    @Override
    public HashMap<UUID, String> getArrows() {
        return arrows;
    }


}
