package com.quadx.dungeons.tools;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.items.Gold;
import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.items.equipment.Equipment;
import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.shapes1_5.ShapeRendererExt;
import com.quadx.dungeons.states.mapstate.Map2State;
import com.quadx.dungeons.tools.timers.Delta;
import com.quadx.dungeons.tools.timers.Time;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.badlogic.gdx.graphics.Color.BLACK;
import static com.quadx.dungeons.Game.*;
import static com.quadx.dungeons.GridManager.monsterList;
import static com.quadx.dungeons.states.mapstate.MapState.gm;
import static com.quadx.dungeons.tools.gui.HUD.out;

/**
 * Created by Chris Cavazos on 7/10/2016.
 */
public class Tests {
    static ArrayList<Integer> fpsList = new ArrayList<>();
    public static ArrayList<Double> mapLoadTimes = new ArrayList<>();
    public static ArrayList<Double> memUsageList = new ArrayList<>();
    public static long currentMemUsage = 0;
    public static int meterListMax = 50;
    public static int runs = 100;
    public static float dtReload = 0;
    public static boolean allAttacks=       true;
    public static boolean allstop=          false;
    public static boolean spawn =           true;
    public static boolean nodeath=          false;
    public static boolean fastreg=          false;
    public static boolean noLand=           false;
    public static boolean showhitbox=       false;
    public static boolean output=           true;
    public static boolean clearmap=         false;
    public static boolean infiniteRegen =   false;
    public static boolean displayFPS =      true;
    public static boolean mouseAim=         true;
    public static int spawnLimit = 60;

    static float fps = 0;
    static int testCount = 0;
    static Delta dFPS = new Delta( 3* Time.ft);

    public static void update(float dt){
        processMetrics();
        updateFPSCounter(dt);
    }
    static void updateFPSCounter(float dt){
        dFPS.update(dt);
        if (dFPS.isDone()) {
            fps = 1 / dt;
            Tests.updateFPSList(fps);
            dFPS.reset();
        }
    }
    public static void drawFPSModule(SpriteBatch sb, ShapeRendererExt sr, Vector2 pos){
        //fps meter module
        if(displayFPS) { //TODO optomize this to draw faster
            //DRAW FPS meter
            sr.begin(ShapeRenderer.ShapeType.Filled);
            sr.setColor(BLACK);
            sr.rect(pos.x, pos.y, 100, 100);
            sr.end();
            sr.begin(ShapeRenderer.ShapeType.Line);
            sr.setColor(Color.WHITE);
            sr.rect(pos.x, pos.y, 100, 100);
            sr.line(pos.x, pos.y, pos.x + 100, pos.y);

            sr.setColor(Color.GREEN);
            int prev = 0;
            for (int i = 0; i < fpsList.size(); i++) {
                sr.line(pos.x + (i * 2), pos.y + prev, pos.x + ((i + 1) * 2), pos.y + fpsList.get(i));
                prev = fpsList.get(i);
            }
            double prev1=0;
            for (int i = 0; i < Tests.memUsageList.size(); i++) {
                sr.setColor(Color.PURPLE);
                sr.line(pos.x + (i * 2), (float) (pos.y +100* prev1), pos.x + ((i + 1) * 2), (float) (pos.y + 100*Tests.memUsageList.get(i)));
                prev1 = Tests.memUsageList.get(i);
            }
            sr.end();
            //draw fps counter
            sb.begin();
            if(displayFPS){
                Game.setFontSize(1);
                Game.getFont().setColor(Color.WHITE);
                Game.getFont().draw(sb, (int) fps + " FPS", pos.x+2, pos.y + 80);
                double x=0;
                try {
                    x= Tests.memUsageList.get(Tests.memUsageList.size() - 1);
                }catch(Exception ignored){}
                Game.getFont().draw(sb, (int) Tests.currentMemUsage + "MB "+Math.floor(x*100)+"%" , pos.x+2, pos.y + 95);
            }
            sb.end();
        }
    }

    public static void updateFPSList(float fps){
        fpsList.add((int) fps);
        if (fpsList.size() > meterListMax) {
            fpsList.remove(0);
        }
    }

    public static void goldTest(){
        int[] avgarr=new int[100];
        for(int i=1;i<=100;i++) {
            double avg=0;
            for(int j=0;j<100;j++) {
                Gold g=new Gold(i);
                avg+=g.getValue();
                Game.console(i+": "+g.getValue());
            }
            Game.console("");
            avg/=100;
            avgarr[i-1]= (int) avg;
        }
        Game.console(Arrays.toString(avgarr));
    }
    public static void processMetrics(){
        Runtime runtime = Runtime.getRuntime();
        currentMemUsage = runtime.totalMemory()/(1024*1024);
        memUsageList.add((double) (currentMemUsage/runtime.maxMemory()/(1024*1024)));
        if(memUsageList.size()>meterListMax)
            memUsageList.remove(0);
    }
    public static void giveItems(int x) {
        for (int i = 0; i < x; i++) {
            player.pickupItem(Item.generateNoGold());
        }
    }

    public static void loadEmptyMap() {
        Map2State.fillArray();
    }

    public static void testsMonsterStats(){
       /* ArrayList<Monster> mons=new  ArrayList<>();
        mons.add(new Anortih());
        mons.add(new Dodrio());
        mons.add(new Dragonair());
        mons.add(new Gengar());
        mons.add(new Kabuto());
        mons.add(new Krabby());
        mons.add(new Muk());
        mons.add(new Ponyta());
        mons.add(new Porygon());

        for(Monster m:mons){
            String out;
            String name= m.getName();
            ArrayList<Integer> hp = new ArrayList<>();
            ArrayList<Integer> str = new ArrayList<>();
            ArrayList<Integer> def = new ArrayList<>();
            ArrayList<Integer> in = new ArrayList<>();
            ArrayList<Integer> spd = new ArrayList<>();
            console(name);
            for(int j=1;j<50;j++) {
                hp.clear();
                str.clear();
                def.clear();
                in.clear();
                spd.clear();
                int n=100;
                for (int i = 0; i < n; i++) {
                    m.genStats(j);
                    hp.add((int) m.getHpMax());
                    str.add((int) m.getStrength());
                    def.add((int) m.getDefense());
                    in.add((int) m.getIntel());
                    spd.add((int) m.getSpeed());
                }
                float hpav=0,strav=0,defav=0,inav=0,spdav=0;
                for (int i = 0; i < n; i++) {
                    hpav+=hp.get(i);
                    strav+=str.get(i);
                    defav+=def.get(i);
                    inav+=in.get(i);
                    spdav+=spd.get(i);
                }
                hpav/=n;
                strav/=n;
                defav/=n;
                inav/=n;
                spdav/=n;
                out=j+":\t"+hpav+"\t\t"+strav+"\t\t"+defav+"\t\t"+inav+"\t\t"+spdav;
                console(out);
            }
        }
*/
    }

    public static void testEquipmentRates() {
        int e = 0;
        int l = 0;
        int a = 0;
        int b = 0;
        console("----Equipment Drop Rate Test---------------------");
        for (int j = 0; j < 1000; j++) {
            for (int i = 0; i < 1000; i++) {
                Equipment item = Equipment.generateEquipment();
                if (item.getGrade().equals("Elite"))
                    e++;
                if (item.getGrade().equals("Legendary"))
                    l++;
            }
            console(Mathq.percent(e, 1000) + "% E");
            console(Mathq.percent(l, 1000) + "% L");
            a += e;
            b += l;
            e = 0;
            l = 0;
        }
        console("-------------------------------------------------");
        console(Mathq.percent(a, 1000 * 1000) + "% AVG E");
        console(Mathq.percent(b, 1000 * 1000) + "% AVG L");
        console("-------------------------------------------------");

    }

    public static void reloadMap(float dt) {
        Tests.dtReload += dt;

        if (Tests.dtReload > .2) {
            if (testCount < Tests.runs) {
                gm.initializeGrid();
                Tests.dtReload = 0;
            } else {
                Tests.calcAvgMapLoadTime();
                System.exit(0);
            }
        }
    }

    public static void calcAvgMapLoadTime() {

        double total = 0;
        for (Double i : mapLoadTimes) {
            total += i;
        }
        total /= mapLoadTimes.size();
        Game.console("Map Load Time Avg:" + total);
    }

    public static boolean timeKill() {
        //returns true to allow play
        //ONLY ENABLE WHEN GIVNING OUT TEST COPIES
        /*
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date kill = null;
        try {
            kill = sdf.parse("2017-10-31");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        String d = sdf.format(cal.getTime());
        Date today;
        try {
            today = sdf.parse(d);
            if (kill.compareTo(today) > 0)
                return true;
            else
                return true;
        } catch (ParseException e) {
            e.printStackTrace();
            return true;
        }*/
        return true;
    }

    public static void parseCommand(String text) {
        String s = text.toLowerCase();
        List<String> comm = new ArrayList<>(Arrays.asList(s.split(" ")));
        String outText = "-Invalid Command-";
        if(comm.get(0).equals("allhit")){
            for(Monster m:monsterList){
                m.setHit();
            }
        }

        if(comm.get(0).equals("p")){
            if (comm.get(1).equals("maxstat")) {
                player.st.maxStat();

                outText="Stats Maxed.";
            }
            if(comm.get(1).equals("item")){
                if(comm.get(2).equals("set")){
                    int a=Integer.parseInt(comm.get(3));
                    try {
                        for(Equipment e:equipSets.ref[a])
                        player.pickupItem(e);
                        outText="Added set "+a;
                    }catch (Exception ignored){
                    }
                }
            }

            if(comm.get(1).equals("ap")){
                int a=Integer.parseInt(comm.get(2));
                player.setAbilityPoints(a);
                outText="Added Ability Points.";
            }
            if (comm.get(1).equals("maxmove")) {
                //player.setMoveSpeed(.000000001f);
                //player.seteRegen(100000);
                outText="Move speed Maxed.";
            }
            if(comm.get(1).equals("lvlup")){
                player.setExp(player.getExpLimit(),1);
                player.checkLvlUp();
               // player.setExp(0,1);
                outText="Level up";
            }
            if(comm.get(1).equals("hp")){
                int a=Integer.parseInt(comm.get(2));
                player.st.setHpMax(a);
                player.st.setHp(a);
                outText="Hp set to "+a;
            }
            if(comm.get(1).equals("mana")){
                int a=Integer.parseInt(comm.get(2));
                player.st.setManaMax(a);
                player.st.setMana(a);
                outText="M set to "+a;
            }
            if(comm.get(1).equals("e")){
                int a=Integer.parseInt(comm.get(2));
                player.st.setHpMax(a);
                player.st.setHp(a);
                outText="E set to "+a;
            }
            if(comm.get(1).equals("att")){
                int a=Integer.parseInt(comm.get(2));
                player.st.setStrength(a);
                outText="ATT set to "+a;
            }
            if(comm.get(1).equals("int")){
                int a=Integer.parseInt(comm.get(2));
                player.st.setIntel(a);
                outText="INT set to "+a;
            }
            if(comm.get(1).equals("def")){
                int a=Integer.parseInt(comm.get(2));
                player.st.setDefense(a);
                outText="DEF set to "+a;
            }
            if(comm.get(1).equals("spd")){
                int a=Integer.parseInt(comm.get(2));
                player.st.setSpeed(a);
                outText="SPD set to "+a;
            }
        }
        if (comm.get(0).equals("disable")) {
            if (comm.get(1).equals("m")) {
                allstop = true;
                spawn = false;
                monsterList.clear();
                outText = "Monsters disabled!";
            }
            if(comm.get(1).equals("death")){
                nodeath=true;
            }
        }
        if (comm.get(0).equals("enable")) {
            if (comm.get(1).equals("m")) {
                allstop = false;
                spawn = true;
                outText = "Monsters enabled!";
            }
            if(comm.get(1).equals("death")){
                nodeath=false;
            }
        }
        if (comm.get(0).equals("mstop")) {
            if (comm.get(1).equals("all")) {
                allstop = true;
                outText = "All monsters stopped!";
            }
        }
        if (comm.get(0).equals("mstart")) {
            if (comm.get(1).equals("all")) {
                allstop = false;
                outText = "All monsters started!";
            }
        }
        if (comm.get(0).equals("mclear")) {
            if (comm.get(1).equals("all")) {
                monsterList.clear();
                outText = "Cleared monster list!";
            }
        }
        if (comm.get(0).equals("nospawn")) {
            spawn = false;
            outText = "Monster Spawn Disabled!";
        }

        out(outText);
    }

}

