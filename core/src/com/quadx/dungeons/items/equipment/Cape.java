package com.quadx.dungeons.items.equipment;

import static com.quadx.dungeons.tools.ImageLoader.equipBasic;

/**
 * Created by Tom on 12/29/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
class Cape extends Equipment {
    public Cape(){
        type=Type.Cape;
        setIcon(equipBasic[2]);
        setGrade();
        setBoost();
        setMods();
        name=grade.toString()+" "+boost.toString()+" Cape";
    }
}
