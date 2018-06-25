package com.quadx.dungeons.tools.stats;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.Player;
import com.quadx.dungeons.tools.Score;
import com.quadx.dungeons.tools.gui.HoverText;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.quadx.dungeons.Game.multiplier;
import static com.quadx.dungeons.Game.rn;
import static com.quadx.dungeons.GridManager.bound;
import static com.quadx.dungeons.tools.Tests.fastreg;
import static com.quadx.dungeons.tools.timers.Time.ft;

/**
 * Created by Chris Cavazos on 5/26/2018.
 */
public class PlayerStat extends Stats {

    DecimalFormat df = new DecimalFormat("0.00");
    private boolean simpleStats = true;

    private String[] statNames = new String[]{
            "HP", "M", "E", "STR", "DEF", "INT", "SPD"
    };

    private int hpMod = 0;
    private float hpMult = 1;
    private float hpRegenMod = 1;
    private float manaRegenMod = 1;
    private float energyRegenMod = 1;
    private float goldMult = 1;


    private float mana = barStatGrowthFunction(1);
    private float manaMax = barStatGrowthFunction(1);
    private int manaMod = 0;
    private float mMult = 1;

    private float energy = barStatGrowthFunction(1);
    private float energyMax = barStatGrowthFunction(1);
    private int eMod = 0;
    private float eMult = 1;

    private float strMult = 1;
    private int strMod = 0;

    private float defMult = 1;
    private int defMod = 0;

    private float intMult = 1;
    private int intMod = 0;

    private float spdMult = 1;
    private int spdMod = 0;


    //adders
    public void addMana(int m) {
        this.mana += m;
    }
    public void addEnergy(int e) {
        energy += e;
    }
    //multipliers
    public void scaleHpRegen(float sc){
        hpRegenMod*=sc;
    }
    public void scaleManaRegen(float sc){
        manaRegenMod*=sc;
    }
    public void scaleEnergyRegen(float sc){
        energyRegenMod*=sc;
    }
    public void setxHpRegen(float hpRegenMod) { this.hpRegenMod = hpRegenMod; }
    public void setxManaRegen(float manaRegenMod) {
        this.manaRegenMod = manaRegenMod;
    }
    public void setxEnergyRegen(float v) {
        energyRegenMod=v;
    }

    public void setxManaMax(double manaMax) {
        this.manaMax *= manaMax;
    }
    public void setxEnergyMax(double xEnergyMax) {
        this.energyMax *= xEnergyMax;
    }


    //setters
    public void setMana(float m) {
        this.mana = m;
    }
    public void setManaMax(int manaMax) {
        this.manaMax = manaMax;
    }
    public void setEnergy(float e) {
        energy = e;
    }
    public void setEnergyMax(int e) {
        energyMax = e;
    }

    //set mods
    public void setStrMod(int m){
        strMod=m;
    }
    public void setDefMod(int m) {
        defMod=m;

    }
    public void setIntMod(int m) {
        intMod=m;
    }
    public void setSpdMod(int m) {
        spdMod=m;
    }
    public void setHpMod(int m) {
        hpMod=m;
    }
    public void setManaMod(int m) {
        manaMod=m;
    }
    //getters
    private float getHpRegen() {
        return regenGrowthFunction(level, getDefComp() / 2,hpRegenMod);
    }
    private float getManaRegen() {
        return regenGrowthFunction(level, getIntComp(),manaRegenMod);
    }
    private float getEnergyRegen() {
        return regenGrowthFunction(level, getStrComp(),energyRegenMod);
    }
    public float getMana() {
        return mana;
    }
    public float getEnergy() {
        return energy;
    }
    public float getManaMax() {
        return manaMax;
    }
    public float getEnergyMax() {
        return energyMax;
    }
    public float getPercentEnergy(){
        return energy/energyMax;
    }
    public float getPercentMana(){
        return mana/manaMax;
    }
    public boolean isSimpleStatsEnabled() {
        return simpleStats;
    }
    //get comp
    private int getHpComp() {
        return (int) (hpMax * hpMult + hpMod);
    }
    private int getMComp() {
        return (int) (manaMax * mMult + manaMod);
    }
    private int getEComp() {
        return (int) (energyMax * eMult + eMod);
    }
    public int getStrComp() {
        return (int) (str * strMult + strMod);
    }
    public int getDefComp() {
        return (int) (def * defMult + defMod);
    }
    public int getIntComp() {
        return (int) (intel * intMult + intMod);
    }
    public int getSpdComp() {
        return (int) (spd * spdMult + spdMod);
    }




    //other
    private float regenGrowthFunction(int level, int stat , float mod) {
        float rate = (level * (level / 192f) + (stat / 3650f) + .25f)*mod;
        float g = (60 * ft) * rate;
        return g;
    }
    public void toggleSimpleStats() {
        simpleStats = !simpleStats;
    }
    public void addItemMods(int[] arr, Vector2 pos) {
        hp += arr[0];
        mana += arr[1];
        energy += arr[2];
        str += arr[3];
        def += arr[4];
        intel += arr[5];
        spd += arr[6];
        for (int i = 0; i < 7; i++) {
            if (arr[i] != 0) {
                String s = "+" + arr[i] + " " + statNames[i];
                new HoverText(s, Color.GREEN, pos, false);

            }
        }
    }
    public void generateLevelBonus() {
        hpMax = barStatGrowthFunction(level) * 2;
        manaMax = barStatGrowthFunction(level);
        energyMax = barStatGrowthFunction(level);
        str += rn.nextInt(15);
        def += rn.nextInt(15);
        intel += rn.nextInt(15);
        spd += rn.nextInt(15);
    }
    public int getPoints(Player p) {
        return (int) ((int) (
                (p.getGold() * 10)
                        + (getStrComp() * 100)
                        + (getDefComp() * 100)
                        + (getSpdComp() * 100)
                        + (getIntComp() * 100)
                        + (level * 2000)
                        + (hpMax * 10)
                        + (manaMax * 10)
                        + (energyMax * 10)
                        + (p.getFloor() * 1000)
                        + (p.getKillCount() * 200)
        ) * multiplier);
    }
    public ArrayList<String> getSimpleStats() {
        ArrayList<String> statsList = new ArrayList<>();
        statsList.add(": " + (int) hp + "/" + getHpComp() + " :" + df.format(getHpRegen()));
        statsList.add(": " + (int) mana + "/" + getMComp() + " :" + df.format(getManaRegen()));
        statsList.add(": " + (int) energy + "/" + getEComp() + " :" + df.format(getEnergyRegen()));
        statsList.add(": " + getStrComp());
        statsList.add(": " + getDefComp());
        statsList.add(": " + getIntComp());
        statsList.add(": " + getSpdComp());
        return statsList;
    }
    public ArrayList<String> getExpandedStats() {
        ArrayList<String> statsList = new ArrayList<>();
        statsList.add("HP   " + (int) (hp * hpMult) + "/" + (int) hpMax + " +" + hpMod + ": " + getHpComp());
        statsList.add("M    " + (int) (mana * mMult) + "/" + (int) manaMax + " +" + manaMod + ": " + getMComp());
        statsList.add("E    " + (int) (energy * eMult) + "/" + (int) energyMax + " +" + eMod + ": " + getEComp());
        statsList.add("STR: " + (int) (str * strMult) + " +" + strMod + ": " + getStrComp());
        statsList.add("DEF: " + (int) (def * defMult) + " +" + defMod + ": " + getDefComp());
        statsList.add("INT: " + (int) (intel * intMult) + " +" + intMod + ": " + getIntComp());
        statsList.add("SPD: " + (int) (spd * spdMult) + " +" + spdMod + ": " + getSpdComp());
        return statsList;
    }
    public ArrayList<String> getStatsList(Player p) {
        ArrayList<String> statsList = new ArrayList<>();
        statsList.clear();

        statsList.add(name);
        statsList.add("Level " + level);
        statsList.addAll(simpleStats? getSimpleStats(): getExpandedStats());
        statsList.add("AP:     " + p.getAbilityPoints());
        statsList.add("FLOOR:  " + p.getFloor());
        statsList.add("KILLS:  " + p.getKillCount());
        statsList.add("EXP:    " + p.getExp() + "/" + p.getExpLimit());
        statsList.add("ORE:    "+ p.getOreCnt());
        statsList.add("LEATHER:"+p.getLeatherCnt());
        statsList.add("GOLD:   " + p.getGold());
        return statsList;
    }
    public Score getScore(Player p) {
        return new Score("" +name, "" + getPoints(p), "" + p.getGold(), p.getAbility().getName() + " " + p.getAbility().getLevel() + " Lvl " + level, "" + p.getKillCount());
    }
    public void regenModifiers() {
        if (!fastreg) {
            hp += getHpRegen();
            if (hp > hpMax) hp = hpMax;
            mana += getManaRegen();
            if (mana > manaMax) mana = manaMax;
            energy += getEnergyRegen();
            if (energy > energyMax) energy = energyMax;
        } else {
            energy = energyMax;
            mana = manaMax;
            hp = hpMax;
        }
    }
    public void boundStatBars() {
        hp = bound(hp, hpMax);
        mana = bound(mana, manaMax);
        energy = bound(energy, energyMax);
    }
    public void fullHeal() {
        setHp(getHpMax());
        setMana(getManaMax());
        setEnergy(getEnergyMax());
    }
    public void update(){
        boundStatBars();

    }

    public void maxStat() {
        int a=10000;
        setHp(a);
        setHpMax(a);
        setMana(a);
        setManaMax(a);
        setEnergy(a);
        setEnergyMax(a);
        setStrength(a);
        setDefense(a);
        setIntel(a);
        setSpeed(a);
    }

    public float getGoldMult() {
        return goldMult;
    }

    public void scaleGoldMult(float sc) {
        goldMult*=sc;
    }
}
