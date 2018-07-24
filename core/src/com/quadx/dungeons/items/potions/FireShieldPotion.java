package com.quadx.dungeons.items.potions;

import com.quadx.dungeons.Game;
import com.quadx.dungeons.attacks.Flame;
import com.quadx.dungeons.tools.timers.Delta;

import static com.quadx.dungeons.tools.timers.Time.SECOND;

/**
 * Created by Chris Cavazos on 6/5/2018.
 */
public class FireShieldPotion extends Potion {
    Delta dUse = new Delta(.15f * SECOND);

    public FireShieldPotion() {
        dEffectTime = new Delta(30 * SECOND);
        name = "Fire Shield Potion";
        effects.add("FIRE SHIELD");
        gINIT(1,"pOrange");
    }

    @Override
    void onStart() {
        Game.player.setFireShield(true);
    }

    @Override
    void onEnd() {
        Game.player.setFireShield(false);
    }

    @Override
    public void onUpdate(float dt) {
        dUse.update(dt);
        if(dUse.isDone()) {
            Flame l = new Flame(true);
            l.useNoCost();
            dUse.reset();
        }
    }
}