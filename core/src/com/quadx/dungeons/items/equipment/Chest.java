package com.quadx.dungeons.items.equipment;

import static com.quadx.dungeons.tools.ImageLoader.equipBasic;

/**
 * Created by Tom on 12/29/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
class Chest extends Equipment {
    public Chest(){
        type=Type.Chest;
        setIcon(equipBasic[3]);
        setGrade();
        setBoost();
        setMods();
        name=grade.toString()+" "+boost.toString()+" Chest";
    }
}
