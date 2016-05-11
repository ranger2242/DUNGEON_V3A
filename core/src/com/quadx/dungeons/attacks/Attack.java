package com.quadx.dungeons.attacks;

import java.util.Arrays;
import java.util.StringJoiner;

/**
 * Created by Tom on 11/14/2015.
 */
public class Attack {

    int[] powerA = new int[5];
    int[] costA = new int[5];
    String name = "";
    int type;// 1-physical 2-magic
    int power = 0;
    int cost =0;
    int mod =0;
    int level =0;
    int uses=0;
    int range=0;
    int spread=0;
    int costGold=0;
    String shape = "";
    String description="s";
    public void checkLvlUp(){
        if(uses>20){level=1;}
        if(uses>60){level=2;}
        if(uses>150){level=3;}
        if(uses>220){level=4;}
        power=powerA[level];
        cost=costA[level];
    }
    public Attack(){}
    public Attack(String na, int lvl, int pow, int co, int mo){
        name=na;
        power=pow;
        cost=co;
        mod=mo;
        level=lvl;
    }
    public String getName(){return name;}
    public String getDescription(){return description;}
    public String getPowerArr(){
        String s =Arrays.toString(powerA);
        return s;
    }
    public String getCostArr() {
        String s = Arrays.toString(costA);
        return s;
    }
    public int getPower(){return power;}
    public int getCost(){return cost;}
    public int getCostGold(){return costGold;}
    public int getMod(){
    return mod;
    }
    public int getLevel(){
        return level;
    }
    public int getType(){return type;}
    public int getSpread(){return spread;}
    public int getRange(){return range;}
    public void setUses(){
        uses++;
    }

}
