package com.quadx.dungeons.abilities;

import java.util.ArrayList;

/**
 * Created by range_000 on 8/6/2016.
 */
public class WaterBreath extends Ability {

    public WaterBreath(){
        name="Water Breath";
    }
    @Override
    public void onActivate() {
        l1();
    }

    @Override
    public int getMod() {
        return 7;
    }

    @Override
    public void l1() {
        level++;

    }

    @Override
    public void l2() {

    }

    @Override
    public void l3() {

    }

    @Override
    public void l4() {

    }

    @Override
    public void l5() {

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ArrayList<String> details() {
        return new ArrayList<>();
        /*details.clear();
        details.add("-"+name+" "+(level+1) +"-");
        switch (this.level+1){
            case 1:{
                details.add("Breathe under water.");
                details.add("Limited breath");
                break;
            }case 2:{
                break;
            }case 3:{

                break;
            }case 4:{
                break;
            }case 5:{

                break;
            }
        }
        if(level<2)
            details.add("Upgrade cost: "+upCost[level+1]+" AP");
        return details;   */ }
}
