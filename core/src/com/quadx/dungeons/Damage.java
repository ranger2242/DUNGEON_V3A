package com.quadx.dungeons;

import com.quadx.dungeons.monsters.Monster;

import java.util.Random;

/**
 * Created by Tom on 11/24/2015.
 */
public class Damage {
    double damagemax;
    double crit;
    static int damage;
    static Random rn =new Random();

    //damage = ( ((2*p.level+10)/250) * (p.attack/m.defense) * (power+2) ) * Mod
    //Mod = stab * type bonus* crit* other *(random[.85,1]

    public int playerPhysicalDamage(Player p, Monster m, int power){
        damagemax=((p.attack+p.level)*3);
        int baseDamage=((p.attack*3)+(power))-(int)((m.getDefense()));
        crit=(baseDamage/100)*15;
        if(crit<1)crit=1;
        damage= baseDamage+rn.nextInt((int)crit);
        return damage;
    }
    public int monsterPhysicalDamage(Player p, Monster m, int power){
        damagemax=((m.getAttack()+m.getLevel())*3);
        int baseDamage=(int)((m.getAttack()*3)+(power))-(int)((p.getDefense()));
        crit=(baseDamage/100)*15;
        if(crit<1)crit=1;
        damage= baseDamage+rn.nextInt((int)crit);
        return damage;
    }
    public static int playerMagicDamage(Player p, Monster m, int power){
        double a=((2*(double)p.level+10)/250);
        double b= ((double)(p.intel+p.intelMod)/m.intel);
        double c=(power+2);
        damage =(int) (a *b * c );
        return damage;
    }
    public int monsterMagicDamage(Player p, Monster m, int power){
        damagemax=((m.getIntel()+m.getLevel())*3);
        int baseDamage=(int)((m.getIntel()*3)+(power))-(int)((p.getIntel()));
        crit=(baseDamage/100)*15;
        if(crit<1)crit=1;
        damage= baseDamage+rn.nextInt((int)crit);
        return damage;
    }
}
