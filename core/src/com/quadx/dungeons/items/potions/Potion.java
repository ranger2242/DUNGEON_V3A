package com.quadx.dungeons.items.potions;

import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.tools.timers.Delta;

import java.util.ArrayList;

/**
 * Created by Chris Cavazos on 5/31/2018.
 */
public abstract class Potion extends Item {
    public boolean enabled=false;
    public boolean runOnce=true;
    public boolean remove= false;

    protected ArrayList<String> effects = new ArrayList<>();
    Delta dEffectTime = new Delta(0);



    public ArrayList<String> getEffects() {
        return effects;
    }
    abstract void onStart();
    abstract void onEnd();
    public abstract void onUpdate(float dt);

    public void update(float dt) {
        if (runOnce) {
            onStart();
            runOnce = false;
        }
        if(enabled){
            dEffectTime.update(dt);
        }
        onUpdate(dt);
        checkEnd();
    }
    public void checkEnd(){
        if (dEffectTime.isDone()) {
            enabled=false;
            if(!remove) {

                onEnd();
            }
            remove = true;
        }
    }

}
