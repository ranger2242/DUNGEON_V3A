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
        type = CostType.Mana;
        powerA = new int[]{0, 0, 0, 0, 0};
        costA = new int[]{30, 50, 70, 90, 110};
        name = "Heal";
        power = 0;
        cost = 0;
        mod = 2;
        spread = 0;
        range = 0;
        description = "Drain ALL M and E for HP";
        hitBoxShape=HitBoxShape.None;
        loadArray();
        setIcon(ImageLoader.attacks.get(4));
    }
    public void runAttackMod() {
        int d=(cost*3)/4;
        player.st.addHp(d);
        new HoverText("+"+d,Color.GREEN ,player.fixed(),false);
    }
}