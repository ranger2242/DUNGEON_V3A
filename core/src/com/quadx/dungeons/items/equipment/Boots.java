package com.quadx.dungeons.items.equipment;

/**
 * Created by Tom on 12/29/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Boots extends Equipment {
    public Boots(){
        type=Type.Boots;
        //setIcon(equipBasic[1]);

        setGrade();
        setBoost();
        setMods();
        fileName="equip\\icBoots.png";
        name=grade.toString()+" "+boost.toString()+" Boots";
        craftCost=new int[]{10,10,1000};
        loadIcon();

    }
}
