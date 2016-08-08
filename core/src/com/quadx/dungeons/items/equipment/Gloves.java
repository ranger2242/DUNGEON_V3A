package com.quadx.dungeons.items.equipment;

import static com.quadx.dungeons.tools.ImageLoader.equipBasic;

/**
 * Created by Tom on 12/29/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
class Gloves extends Equipment {
    public Gloves(){
        type=Type.Gloves;
        setIcon(equipBasic[4]);

        setGrade();
        setBoost();
        setMods();
        name=grade.toString()+" "+boost.toString()+" Gloves";
    }
}
