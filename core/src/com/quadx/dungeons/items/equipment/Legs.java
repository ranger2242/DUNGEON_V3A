package com.quadx.dungeons.items.equipment;

/**
 * Created by Tom on 12/29/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Legs extends Equipment {
    public Legs(){
        type=Type.Legs;
        craftCost=new int[]{20,7,3500};
        init("icLegs");
    }
}
