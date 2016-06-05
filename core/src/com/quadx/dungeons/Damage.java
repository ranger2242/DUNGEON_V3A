package com.quadx.dungeons;

import com.quadx.dungeons.monsters.Monster;

import java.util.Random;

/**
 * Created by Tom on 11/24/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Damage {
    private static int crit;
    private static int damage;
    private static int defaultDamage = 1;
    private static Random rn =new Random();


    //damage = ( ((2*p.level+10)/250) * (p.attack/m.defense) * (power+2) ) * Mod
    //Mod = stab * type bonus* crit* other *(random[.85,1]

    public int playerPhysicalDamage(Player p, Monster m, int power){
        int baseDamage=((p.attack*3)+(power))-(int)((m.getDefense()));
        crit=(baseDamage/100)*15;
        if(crit<1)crit=1;
        damage= baseDamage+rn.nextInt(crit);
        if (damage < 0) //checks for negative damage
            damage = defaultDamage;
        Game.player.pDamage=damage;
        return damage;
    }
    public static int monsterPhysicalDamage(Player p, Monster m, int power){
        int baseDamage=(int)((m.getAttack()*3)+(power))-((p.getDefense()));
        crit=(baseDamage/100)*15;
        if(crit<1)crit=1;
        damage= baseDamage+rn.nextInt(crit);
        if (damage < 0) //checks for negative damage
            damage = defaultDamage;
        if(Game.player.safe)
            damage=0;
        return damage;
    }
    public static int playerMagicDamage(Player p, Monster m, int power){
        double a=((2*(double)p.level+10)/250);
        double b= ((double)(p.intel+p.intelMod)/m.intel);
        double c=(power+2);
        damage =(int) (a *b * c );
        if (damage < 0) //checks for negative damage
            damage = defaultDamage;
        Game.player.mDamage=damage;
        return damage;
    }
    public int monsterMagicDamage(Player p, Monster m, int power){
        int baseDamage=(int)((m.getIntel()*3)+(power))-((p.getIntel()));
        crit=(baseDamage/100)*15;
        if(crit<1)crit=1;
        damage= baseDamage+rn.nextInt(crit);
        if (damage < 0) //checks for negative damage
            damage = defaultDamage;
        if(Game.player.safe)
            damage=0;
        return damage;
    }
}
