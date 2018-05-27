package com.quadx.dungeons.tools.stats;

import java.util.ArrayList;

import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.GridManager.rn;

/**
 * Created by Chris Cavazos on 5/26/2018.
 */
public class MonsterStat extends Stats{
    protected int[] maxes = new int[4];
    protected double power = 20;
    protected double hpBase = 60;
    protected double strBase = 40;
    protected double defBase = 40;
    protected double intBase = 40;
    protected double spdBase = 40;

    //getters
    public double getPower() {
        return power;
    }
    public ArrayList<String> getStatsList() {
        ArrayList<String> stats = new ArrayList<>();
        stats.add(name);
        stats.add("HP: " + getHp());
        stats.add("Level: " +level);
        stats.add("Attack: " + getStrength());
        stats.add("Defense: " + getDefense());
        stats.add("Intel: " + getIntel());
        stats.add("Speed: " + getSpeed());
        return stats;
    }
    //gens
    public void genStats() {
        // System.out.println("---------------------------------------");
        genHp();
        genStrength();
        genDefense();
        genIntel();
        genSpeed();
    }
    private void genHp() {
        hp = (float) (15 + (hpBase) * Math.exp(Math.sqrt(Math.pow(level, 1.5)) / 2) / 2);
        hpMax = hp;
    }
    private void genStrength() {
        double a = strBase + rn.nextInt(31);
        double b = Math.sqrt(rn.nextInt(65535)) / 4;
        str = (int) ((((a * 2 + b) * level) / 100) + 5);
        maxes[0] = (int) (((((strBase + 31) * 2 + (Math.sqrt(65535) / 4)) * level) / 100) + 5);
    }
    private void genDefense() {
        double a = defBase + rn.nextInt(31);
        double b = Math.sqrt(rn.nextInt(65535)) / 4;
        def = (int) ((((a * 2 + b) * level) / 100) + 5);
        maxes[1] = (int) (((((defBase + 31) * 2 + (Math.sqrt(65535) / 4)) * level) / 100) + 5);
    }
    private void genSpeed() {
        double a = spdBase + rn.nextInt(31);
        double b = Math.sqrt(rn.nextInt(65535)) / 4;
        spd = (int) ((((a * 2 + b) * level) / 100) + 5);
        maxes[2] = (int) (((((spdBase + 31) * 2 + (Math.sqrt(65535) / 4)) * level) / 100) + 5);
    }
    private void genIntel() {
        double a = intBase + rn.nextInt(31);
        double b = Math.sqrt(rn.nextInt(65535)) / 4;
        intel = (int) ((((a * 2 + b) * level) / 100) + 5);
        maxes[3] = (int) (((((intBase + 31) * 2 + (Math.sqrt(65535) / 4)) * level) / 100) + 5);
    }
    public void genLevel() {
        level = player.st.getLevel() + rn.nextInt(player.floor);
        //out(level+"");
    }

    public float getExpFactor() {
        float a = 1;
        float m = maxes[0] + maxes[1] + maxes[2] + maxes[3];
        float tot = str+def+intel+spd;
        a = m / tot;
        return a;
    }

    public void loadStats(int[] s) {
        power=s[0];
        hpBase=s[1];
        strBase=s[2];
        defBase=s[3];
        intBase=s[4];
        spdBase=s[5];
    }
}
