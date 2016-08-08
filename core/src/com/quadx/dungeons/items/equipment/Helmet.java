package com.quadx.dungeons.items.equipment;

import static com.quadx.dungeons.tools.ImageLoader.equipBasic;

/**
 * Created by Tom on 12/29/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
class Helmet extends Equipment {
    public Helmet(){
        type=Type.Helmet;
        setIcon(equipBasic[5]);

        setGrade();
        setBoost();
        setMods();
        name=grade.toString()+" "+boost.toString()+" Helmet";
    }
}
