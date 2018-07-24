package com.quadx.dungeons.items.equipment;

/**
 * Created by Tom on 12/29/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Boots extends Equipment {
    public Boots(){
        type=Type.Boots;
        craftCost=new int[]{10,10,1000};
        init("icBoots");
    }
}
