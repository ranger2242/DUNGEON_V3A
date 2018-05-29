package com.quadx.dungeons.items.equipment;

/**
 * Created by Tom on 12/29/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Cape extends Equipment {
    public Cape(){
        type=Type.Cape;
        //setIcon(equipBasic[2]);
        setGrade();
        setBoost();
        setMods();
        fileName="equip\\icCape.png";

        name=grade.toString()+" "+boost.toString()+" Cape";
        craftCost=new int[]{0,30,1500};
        loadIcon();

    }
}
