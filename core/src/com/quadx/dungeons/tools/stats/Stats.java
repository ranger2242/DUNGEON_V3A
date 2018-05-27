package com.quadx.dungeons.tools.stats;

/**
 * Created by Chris Cavazos on 5/26/2018.
 */
public class Stats {
    protected String name = "DEMO";

    protected float hp = barStatGrowthFunction(1);
    protected float hpMax = barStatGrowthFunction(1);
    protected int str = 15;
    protected int def = 15;
    protected int intel = 15;
    protected int spd = 15;

    protected int level=1;


    protected int barStatGrowthFunction(int level) {
        return (int) (45 * Math.pow(Math.E, .25 * (level - 1) / 2) + 100);
    }

    //adders
    public void addHp(int hp) {
        this.hp += hp;
    }
    public void addLevel(){
        level++;
    }

    //multipliers
    public void setxHpMax(double hpMax) {
        this.hpMax *= hpMax;
    }
    public void setxStrength(double attack) {
        this.str *= attack;
    }
    public void setxDefense(double defense) {
        this.def *= defense;
    }
    public void setxIntel(double intel) {
        this.intel *= intel;
    }
    public void setxSpeed(double speed) {
        this.spd *= speed;
    }

    //setters
    public void setHp(float hp) {
        this.hp = hp;
    }
    public void setHpMax(int hpMax) {
        this.hpMax = hpMax;
    }
    public void setStrength(int attack) {
        this.str = attack;
    }
    public void setDefense(int defense) {
        this.def = defense;
    }
    public void setIntel(int intel) {
        this.intel = intel;
    }
    public void setSpeed(int speed) {
        this.spd = speed;
    }
    public void setLevel(int l){level=l;}
    public void setName(String n) {
        name = n;
        if (name.length() > 19) {
            name = name.substring(0, 19);
        }
    }
    //getters
    public float getHp() {
        return hp;
    }
    public float getHpMax() {
        return hpMax;
    }
    public int getStrength() {
        return str;
    }
    public int getDefense() {
        return def;
    }
    public int getIntel() {
        return intel;
    }
    public int getSpeed() {
        return spd;
    }
    public boolean isDead(){
        boolean b= hp<1;
        if(b) hp=0;
        return b;
    }
    public boolean isLowHP(){return (hp <= hpMax / 3);}
    public float getPercentHP() {
        return  hp /  hpMax;
    }
    public int getLevel() {
        return level;
    }
    public String getName() {
        return name;
    }

}
