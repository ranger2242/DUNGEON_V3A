package com.quadx.dungeons.items.equipment;

/**
 * Created by Tom on 12/29/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Gloves extends Equipment {
    public Gloves(){
        type=Type.Gloves;
        craftCost=new int[]{10,10,1500};
        init("icGloves");

    }
}
