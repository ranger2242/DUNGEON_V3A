package com.quadx.dungeons.items.equipment;

/**
 * Created by Tom on 12/29/2015.
 */
public class Cape extends Equipment {
    public Cape(){
        type=Type.Cape;

        setGrade();
        setBoost();
        setMods();
        name=grade.toString()+" "+boost.toString()+" Cape";
    }
}
