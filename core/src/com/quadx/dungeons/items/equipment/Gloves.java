package com.quadx.dungeons.items.equipment;

/**
 * Created by Tom on 12/29/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
class Gloves extends Equipment {
    public Gloves(){
        type=Type.Gloves;

        setGrade();
        setBoost();
        setMods();
        name=grade.toString()+" "+boost.toString()+" Gloves";
    }
}
