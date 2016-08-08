package com.quadx.dungeons.items.equipment;

import static com.quadx.dungeons.tools.ImageLoader.equipBasic;

/**
 * Created by Tom on 12/29/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
class Ring extends Equipment {
    public Ring(){
        type=Type.Ring;
        setIcon(equipBasic[7]);

        setGrade();
        setBoost();
        setMods();
        name=grade.toString()+" "+boost.toString()+" Ring";
    }
}
