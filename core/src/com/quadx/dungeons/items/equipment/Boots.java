package com.quadx.dungeons.items.equipment;

import static com.quadx.dungeons.tools.ImageLoader.equipBasic;

/**
 * Created by Tom on 12/29/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
class Boots extends Equipment {
    public Boots(){
        type=Type.Boots;
        setIcon(equipBasic[1]);

        setGrade();
        setBoost();
        setMods();
        name=grade.toString()+" "+boost.toString()+" Boots";
    }
}
