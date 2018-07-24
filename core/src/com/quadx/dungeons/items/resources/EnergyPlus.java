package com.quadx.dungeons.items.resources;

import static com.quadx.dungeons.Game.player;

/**
 * Created by Chris Cavazos on 7/2/2016.
 */
public class EnergyPlus extends Resource {
    public EnergyPlus(){
        name="Energy+";
        isUsable=true;

        int size= (int) (3*((double)player.st.getEnergyMax()/(double)500));
        if(size==0){
            emod=60;
            cost=2250;
        }
        else if(size==1){
            emod=200;
            cost=2250;
        }
        else if(size>1){
            emod=1000;
            cost=2250;
        }
        gINIT(1,"icMeat"+(size+1));
    }

    @Override
    public int[] runMod() {

        return new int[]{0,0,emod,0,0,0,0};
    }
}
