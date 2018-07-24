package com.quadx.dungeons.items.potions;

import com.quadx.dungeons.Game;
import com.quadx.dungeons.tools.timers.Delta;

import static com.quadx.dungeons.tools.timers.Time.SECOND;

/**
 * Created by Chris Cavazos on 6/6/2018.
 */
public class InvincibilityPotion extends Potion {
    public InvincibilityPotion() {
        dEffectTime = new Delta(10 * SECOND);
        name = "Invincibility Potion";
        effects.add("NO DAMAGE");
        gINIT(1,"pGrey");
    }

    @Override
    void onStart() {
        Game.player.setInvincible(true);
    }

    @Override
    void onEnd() {
        Game.player.setInvincible(false);
    }

    @Override
    public void onUpdate(float dt) {
    }

}