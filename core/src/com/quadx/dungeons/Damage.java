package com.quadx.dungeons;

import com.badlogic.gdx.graphics.Color;
import com.quadx.dungeons.attacks.Attack;
import com.quadx.dungeons.attacks.Dash;
import com.quadx.dungeons.attacks.Protect;
import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.tools.EMath;
import com.quadx.dungeons.tools.gui.HoverText;

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
        float a, b, c, d, e;
        a = 0.06753f;
        b = player.getIntComp();
        c = (float) m.getIntel();
        d = att.getPower();
        e = player.getLevel();

        damage = Math.round(a*(b/c)*d*e);
        if (damage < 0) //checks for negative damage
            damage = defaultDamage;
        if (rn.nextFloat() < .1) {
            damage *= 1.15;
            new HoverText("-CRITICAL-", .2f, Color.BLUE, m.getPX(), m.getPY(), true);
        }
        return damage;
    }
    static float rate=10;
    public static int monsterMagicDamage(Monster m){
        float a, b, c, d;
        a = (float) EMath.randomGaussianAverage(player.getIntComp(),player.getDefComp());
        b = (float) EMath.randomGaussianAverage(m.getIntel(),m.getStrength());
        c = (float) m.getPower();
        d = (float) m.getLevel();

        damage = (int) (Math.round(((c*d)+(b*8))/(a)));
        if (damage < 0) //checks for negative damage
            damage = defaultDamage;
        if (rn.nextFloat() < .1) {
            damage *= 1.15;
            new HoverText("-CRITICAL-", .2f, Color.BLUE, player.getAbsPos().x,player.getAbsPos().y, true);
        }
        if(Protect.active || Dash.active)
            return 0;
        else
        return (int) (damage *rate);
    }
}
