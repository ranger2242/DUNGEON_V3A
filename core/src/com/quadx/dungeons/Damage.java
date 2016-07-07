package com.quadx.dungeons;

import com.badlogic.gdx.graphics.Color;
import com.quadx.dungeons.attacks.Attack;
import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.states.mapstate.MapStateRender;

import java.util.Random;

import static com.quadx.dungeons.Game.player;

/**
 * Created by Tom on 11/24/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Damage {
    private static int crit;
    private static int damage;
    private static final int defaultDamage = 1;
    private static final Random rn =new Random();

    public static int calcPlayerDamage(Attack att, Monster m){
        double a=((2*(double)player.getLevel()+10)/250);
        double b;
        double c=att.getPower();
        if(att.getType()==1){
            b= ((double)(player.getAttack()+player.getAttackMod())/m.getDefense());
        }
        else if(att.getType()==2){
            b= ((double)(player.getIntel()+player.getIntelMod())/m.getDefense());
        }
        else b=0;
        damage =(int) (a *b * c );
        if (damage < 0) //checks for negative damage
            damage = defaultDamage;
        if(rn.nextFloat()<.1){
            damage*=1.15;
            MapStateRender.setHoverText("-CRITICAL-",.2f, Color.BLUE,m.getPX(), m.getPY(),true);
        }
        return damage;
    }
    public static int monsterPhysicalDamage(Player p, Monster m, int power){
        int baseDamage=(int)((m.getAttack()*3)+(power))-((p.getDefense()));
        crit=(baseDamage/100)*15;
        if(crit<1)crit=1;
        damage= baseDamage+rn.nextInt(crit);
        if (damage < 0) //checks for negative damage
            damage = defaultDamage;
        if(player.safe)
            damage=0;
        return damage;
    }
    public static int monsterMagicDamage(Player p, Monster m, int power){
        int baseDamage=(int)((m.getIntel()*3)+(power))-((p.getIntel()));
        crit=(baseDamage/100)*15;
        if(crit<1)crit=1;
        damage= baseDamage+rn.nextInt(crit);
        if (damage < 0) //checks for negative damage
            damage = defaultDamage;
        if(player.safe)
            damage=0;
        return damage;
    }
}
