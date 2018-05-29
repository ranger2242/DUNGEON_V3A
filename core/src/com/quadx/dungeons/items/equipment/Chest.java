package com.quadx.dungeons.items.equipment;

/**
 * Created by Tom on 12/29/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Chest extends Equipment {
    public Chest(){
        type=Type.Chest;
        //setIcon(equipBasic[3]);
        setGrade();
        setBoost();
        setMods();
        fileName="equip\\icChest.png";

        name=grade.toString()+" "+boost.toString()+" Chest";
        craftCost=new int[]{30,20,5000};
        loadIcon();

    }
}
