package com.quadx.dungeons.attacks;

import com.badlogic.gdx.graphics.Color;
import com.quadx.dungeons.tools.gui.HoverText;

import static com.quadx.dungeons.Game.player;

/**
 * Created by range on 5/17/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Rest extends Attack {
    public Rest(){
        costGold = 3000;
        type = CostType.Mana;
        powerA = new int[]{0, 0, 0, 0, 0};
        costA = new int[]{10, 20, 30, 40, 50};
        name = "Rest";
        power = 0;
        cost = 10;
        mod = 8;
        range = 0;
        spread = 0;
        description = "Converts M to Energy";
        hitBoxShape=HitBoxShape.None;
        gINIT(2,"icRest");

    }
    public void runAttackMod() {
        int d=(cost*3)/4;
       // player.addEnergy(d);
        new HoverText("+"+d,Color.YELLOW ,player.fixed(),false);
    }
}
