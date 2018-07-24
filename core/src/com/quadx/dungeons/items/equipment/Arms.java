package com.quadx.dungeons.items.equipment;

/**
 * Created by Tom on 12/29/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Arms extends Equipment {
    public Arms(){
        type=Type.Arms;
        craftCost=new int[]{15,5,1250};
        init("icArms");
    }
}
