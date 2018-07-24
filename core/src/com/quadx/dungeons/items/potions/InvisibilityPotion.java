package com.quadx.dungeons.items.potions;

import com.quadx.dungeons.Game;
import com.quadx.dungeons.tools.timers.Delta;

import static com.quadx.dungeons.tools.timers.Time.SECOND;

/**
 * Created by Chris Cavazos on 6/5/2018.
 */
public class InvisibilityPotion extends Potion {
    public InvisibilityPotion() {
        dEffectTime = new Delta(10 * SECOND);

        name = "Invisibility Potion";
        effects.add("BECOME INVISIBLE");
        gINIT(1,"pGrey");
    }

    @Override
    void onStart() {
        Game.player.setInvisible(true);
    }

    @Override
    void onEnd() {
        Game.player.setInvisible(false);
    }

    @Override
    public void onUpdate(float dt) {
    }

}
