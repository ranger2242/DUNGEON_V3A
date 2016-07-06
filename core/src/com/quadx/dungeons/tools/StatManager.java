package com.quadx.dungeons.tools;

import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.states.mapstate.MapState;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import static com.quadx.dungeons.Game.player;

/**
 * Created by Chris Cavazos on 7/4/2016.
 */
public class StatManager {
         /*
         stats to check

         ----overall
         total gold
         total kills
         total items picked up
         total shots fired
         total misses
         overall accuracy
         longest play time
         shortest play time
         highest floor
         highest player level
         most used class
         total games played
         total play time

         most used attack

         ----per round
         most used attack
         */
    //----PER ROUND VARS------------------
    public static ArrayList<String>stats = new ArrayList<>();
    public static int shotsFired=0;
    public static int shotsMissed=0;
    public static double accurcy=0;
    public static int totalItems=0;
    public static int totalGold=0;
    public static Monster killer =null;

    public StatManager(){
    }
    public static ArrayList<Double> getFinalStats(){
        NumberFormat formatter = new DecimalFormat("#0.00");
        stats.clear();
        long millis = System.nanoTime();
        double gameTime=(double) ( millis-MapState.millis)/ 1000000000.0;
        int x= (int) gameTime;
        double gameTimeMin=(gameTime/60);
        stats.add("Game Time (s): ");
        stats.add("Shots Fired: ");
        stats.add("Shots Missed: ");
        stats.add("Accuracy %: ");
        stats.add("Total Items: ");
        stats.add("Total Gold: ");
        stats.add("Kills/Min: ");
        stats.add("Gold/Min: ");

        ArrayList<Double> list=new ArrayList<>();
        list.add(Double.valueOf(formatter.format( gameTime)));
        list.add((double) shotsFired);
        list.add((double) shotsMissed);
        accurcy=((float)shotsFired-(float)shotsMissed)/(float)shotsFired;
        list.add(Math.floor(accurcy*100));
        list.add((double) totalItems);
        list.add((double) totalGold);
        list.add(Double.valueOf(formatter.format(player.getKillCount()/gameTimeMin)));
        list.add(Double.valueOf(formatter.format(totalGold/gameTimeMin)));


        return list;
    }
    public static void reset() {
        shotsFired=0;
        shotsMissed=0;
        accurcy=0;
        totalItems=0;
        totalGold=0;
        killer=null;
    }
}
