package com.quadx.dungeons.items.equipment;

/**
 * Created by Tom on 12/29/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
class Ring extends Equipment {
    public Ring(){
        type=Type.Ring;

        setGrade();
        setBoost();
        setMods();
        name=grade.toString()+" "+boost.toString()+" Ring";
    }
}
