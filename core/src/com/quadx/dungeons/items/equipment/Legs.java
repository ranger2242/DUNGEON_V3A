package com.quadx.dungeons.items.equipment;

/**
 * Created by Tom on 12/29/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
class Legs extends Equipment {
    public Legs(){
        type=Type.Legs;

        setGrade();
        setBoost();
        setMods();
        name=grade.toString()+" "+boost.toString()+" Legs";
    }
}
