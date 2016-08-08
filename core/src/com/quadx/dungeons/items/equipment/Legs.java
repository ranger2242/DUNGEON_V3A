package com.quadx.dungeons.items.equipment;

import static com.quadx.dungeons.tools.ImageLoader.equipBasic;

/**
 * Created by Tom on 12/29/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
class Legs extends Equipment {
    public Legs(){
        type=Type.Legs;
        setIcon(equipBasic[6]);

        setGrade();
        setBoost();
        setMods();
        name=grade.toString()+" "+boost.toString()+" Legs";
    }
}
