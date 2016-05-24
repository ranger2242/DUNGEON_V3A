package com.quadx.dungeons.items.equipment;

/**
 * Created by Tom on 12/29/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Chest extends Equipment {
    public Chest(){
        type=Type.Chest;

        setGrade();
        setBoost();
        setMods();
        name=grade.toString()+" "+boost.toString()+" Chest";
    }
}
