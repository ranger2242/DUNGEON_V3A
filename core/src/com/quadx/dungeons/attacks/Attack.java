package com.quadx.dungeons.attacks;

import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;

import java.util.Arrays;

/**
 * Created by Tom on 11/14/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Attack {

    int[] powerA = new int[5];
    int[] costA = new int[5];
    String name = "";
    int type;// 1-physical 2-magic
    int power = 0;
    int cost =0;
    int mod =0;
    int level =0;
    private int uses=0;
    int range=0;
    int spread=0;
    int costGold=0;
   // int[] usesCheck={125,270,400,875,1200};
    int[] usesCheck={80,300,650,1000,1500};

    String description="s";
    public void checkLvlUp(){
        if(level<=4)
        if(uses>usesCheck[level]){level++; range++;uses=0;}

        power=powerA[level];
        cost=costA[level];

    }
    Attack(){}
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
        return Arrays.toString(powerA);
    }
    public String getCostArr() {
        return Arrays.toString(costA);
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
