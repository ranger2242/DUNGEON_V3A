package com.quadx.dungeons.items.equipment;

/**
 * Created by Tom on 12/29/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
class Helmet extends Equipment {
    public Helmet(){
        type=Type.Helmet;

        setGrade();
        setBoost();
        setMods();
        name=grade.toString()+" "+boost.toString()+" Helmet";
    }
}
