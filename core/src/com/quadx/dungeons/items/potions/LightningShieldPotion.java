package com.quadx.dungeons.items.potions;

import com.quadx.dungeons.attacks.Lightning;
import com.quadx.dungeons.tools.timers.Delta;

import static com.quadx.dungeons.tools.timers.Time.SECOND;

/**
 * Created by Chris Cavazos on 6/5/2018.
 */
public class LightningShieldPotion extends Potion {
    Delta dUse = new Delta(.15f * SECOND);

    public LightningShieldPotion() {
        dEffectTime = new Delta(30 * SECOND);
        name = "Lighning Shield Potion";
        effects.add("LIGHTNING SHIELD");
        gINIT(1,"pYellow");
    }

    @Override
    void onStart() {

    }

    @Override
    void onEnd() {

    }

    @Override
    public void onUpdate(float dt) {
        dUse.update(dt);
        if(dUse.isDone()) {
            Lightning l = new Lightning(true);
            l.useNoCost();
            dUse.reset();
        }
    }
}
