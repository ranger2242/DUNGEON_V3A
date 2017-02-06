package com.quadx.dungeons.attacks;

import com.badlogic.gdx.graphics.Color;
import com.quadx.dungeons.tools.ImageLoader;
import com.quadx.dungeons.tools.gui.HoverText;

import static com.quadx.dungeons.Game.player;

/**
 * Created by Tom on 11/17/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Heal extends Attack {
    public Heal() {
        costGold =20000;
        type = 3;
        powerA = new int[]{0, 0, 0, 0, 0};
        costA = new int[]{30, 50, 70, 90, 110};
        name = "Heal";
        power = 0;
        cost = 0;
        mod = 2;
        spread = 0;
        range = 0;
        description = "Drain ALL M and E for HP";
        setIcon(ImageLoader.attacks.get(4));
        hitBoxShape=HitBoxShape.None;
    }
    public void runAttackMod() {
        int d=(cost*3)/4;
        player.addHp(d);
        new HoverText("+"+d,1, Color.GREEN,player.getAbsPos().x,player.getAbsPos().y+50,false);
    }
}