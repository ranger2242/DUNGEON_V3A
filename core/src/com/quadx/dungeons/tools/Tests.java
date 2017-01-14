package com.quadx.dungeons.tools;

import com.quadx.dungeons.Game;
import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.items.equipment.Equipment;
import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.states.mapstate.Map2State;
import com.quadx.dungeons.states.mapstate.MapStateUpdater;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.quadx.dungeons.Game.*;
import static com.quadx.dungeons.GridManager.monsterList;
import static com.quadx.dungeons.states.MainMenuState.gl;
import static com.quadx.dungeons.states.mapstate.MapState.gm;
import static com.quadx.dungeons.states.mapstate.MapState.out;
import static com.quadx.dungeons.states.mapstate.MapState.viewX;

/**
 * Created by Chris Cavazos on 7/10/2016.
 */
public class Tests {
    public static ArrayList<Double> mapLoadTimes = new ArrayList<>();
    public static ArrayList<Double> memUsageList = new ArrayList<>();
    public static long currentMemUsage = 0;
    public static int meterListMax = 50;
    public static int runs = 100;
    public static float dtReload = 0;
    public static boolean allstop = false;
    public static boolean nospawn = false;
    public static boolean nodeath = false;


    static int testCount = 0;

    public static void giveItems(int x) {
        for (int i = 0; i < x; i++) {
            player.addItemToInventory(Item.generateNoGold());
        }
    }
    public static float strWidth(String s){
        CharSequence cs=s;
        gl.setText(Game.getFont(),cs);
        return gl.width;
    }
    public static float centerString(String s){
        return (viewX+WIDTH/2)- (strWidth(s)/2);
    }
    public static void loadEmptyMap() {
        Map2State.fillArray();
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date kill = null;
        try {
            kill = sdf.parse("2016-12-31");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        String d = sdf.format(cal.getTime());
        Date today = null;
        try {
            today = sdf.parse(d);
            if (kill.compareTo(today) > 0)
                return true;
            else
                return false;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

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
                player.maxStat();
                outText="Stats Maxed.";
            }
            if(comm.get(1).equals("item")){
                if(comm.get(2).equals("set")){
                    int a=Integer.parseInt(comm.get(3));
                    try {
                        for(Equipment e:equipSets.ref[a])
                        player.addItemToInventory(e);
                        outText="Added set "+a;
                    }catch (Exception e){
                    }
                }
            }

            if(comm.get(1).equals("ap")){
                int a=Integer.parseInt(comm.get(2));
                player.setAbilityPoints(a);
                outText="Added Ability Points.";
            }
            if (comm.get(1).equals("maxmove")) {
                player.setMoveSpeed(.000000001f);
                player.seteRegen(100000);
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
                player.setHpMax(a);
                player.setHp(a);
                outText="Hp set to "+a;
            }
            if(comm.get(1).equals("mana")){
                int a=Integer.parseInt(comm.get(2));
                player.setManaMax(a);
                player.setMana(a);
                outText="M set to "+a;
            }
            if(comm.get(1).equals("e")){
                int a=Integer.parseInt(comm.get(2));
                player.setHpMax(a);
                player.setHp(a);
                outText="E set to "+a;
            }
            if(comm.get(1).equals("att")){
                int a=Integer.parseInt(comm.get(2));
                player.setAttack(a);
                outText="ATT set to "+a;
            }
            if(comm.get(1).equals("int")){
                int a=Integer.parseInt(comm.get(2));
                player.setIntel(a);
                outText="INT set to "+a;
            }
            if(comm.get(1).equals("def")){
                int a=Integer.parseInt(comm.get(2));
                player.setDefense(a);
                outText="DEF set to "+a;
            }
            if(comm.get(1).equals("spd")){
                int a=Integer.parseInt(comm.get(2));
                player.setSpeed(a);
                outText="SPD set to "+a;
            }
        }
        if (comm.get(0).equals("disable")) {
            if (comm.get(1).equals("m")) {
                allstop = true;
                nospawn = true;
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
                nospawn = false;
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
            nospawn = true;
            outText = "Monster Spawn Disabled!";
        }
        if (comm.get(0).equals("spawn")) {
            nospawn = false;
            int a=Integer.parseInt(comm.get(1));
            MapStateUpdater.spawnMonsters(a);
            outText = "Monster Spawn Enabled!";
        }
        out(outText);
    }

}

