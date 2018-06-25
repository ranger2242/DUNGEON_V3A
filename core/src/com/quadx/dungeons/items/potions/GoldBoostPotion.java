package com.quadx.dungeons.items.potions;

import com.quadx.dungeons.Game;
import com.quadx.dungeons.tools.stats.PlayerStat;
import com.quadx.dungeons.tools.timers.Delta;

import static com.quadx.dungeons.tools.timers.Time.SECOND;

/**
 * Created by Chris Cavazos on 5/31/2018.
 */
public class GoldBoostPotion extends Potion {
    public GoldBoostPotion() {
        dEffectTime = new Delta(30 * SECOND);
        name = "Gold Boost Potion";
        fileName = "potions\\pGold.png";
        loadIcon();
        effects.add("DOUBLES ALL GOLD");
        effects.add("FOR 30 SECONDS");
    }

    @Override
    void onStart() {
        PlayerStat st = Game.player.st;
        st.scaleGoldMult(2f);
    }

    @Override
    void onEnd() {
        PlayerStat st = Game.player.st;
        st.scaleGoldMult(.5f);
    }

    @Override
    public void onUpdate(float dt) {
    }
}
