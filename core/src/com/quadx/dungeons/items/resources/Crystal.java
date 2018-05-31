package com.quadx.dungeons.items.resources;

import static com.quadx.dungeons.Game.rn;

/**
 * Created by Chris Cavazos on 5/31/2018.
 */
public class Crystal extends Resource {
    public Crystal(){
        int size=1;
        float r=rn.nextFloat();
        if(r<.1){
            size=3;
        }else if(r< .4){
            size=2;
        }
        init(size);
    }

    public Crystal(int s){
        init(s);
    }
    public void init(int s){
        hasEffect=true;
        fileName="mods\\icCrystal"+s+".png";
        String m="";
        switch (s){
            case 1:{
                m="SMALL";
                break;
            }
            case 2:{
                m="MEDIUM";
                break;
            }
            case 3:{
                m="LARGE";
                break;
            }
        }
        name=m+" CRYSTAL";
        loadIcon();
    }
    @Override
    public int[] runMod() {
        return new int[0];
    }
}
