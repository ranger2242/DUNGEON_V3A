package com.quadx.dungeons.attacks;

import com.quadx.dungeons.tools.ImageLoader;

/**
 * Created by Tom on 11/17/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Heal extends Attack {
    public Heal() {
        costGold =20000;
        type = 3;
        powerA = new int[]{0, 0, 0, 0, 0};
        costA = new int[]{0, 0, 0, 0, 0};
        name = "Heal";
        power = 0;
        cost = 0;
        mod = 2;
        spread = 0;
        range = 0;
        description = "Drain ALL M and E for HP";
        setIcon(ImageLoader.attacks.get(4));

    }
}