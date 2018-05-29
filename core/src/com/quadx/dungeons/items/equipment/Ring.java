package com.quadx.dungeons.items.equipment;

/**
 * Created by Tom on 12/29/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Ring extends Equipment {
    public Ring(){
        type=Type.Ring;
        //setIcon(equipBasic[7]);

        setGrade();
        setBoost();
        setMods();
        fileName="equip\\icRing.png";
        name=grade.toString()+" "+boost.toString()+" Ring";
        craftCost=new int[]{5,0,6000};
        loadIcon();

    }
}
