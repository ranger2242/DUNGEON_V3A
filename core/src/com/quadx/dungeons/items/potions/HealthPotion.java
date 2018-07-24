package com.quadx.dungeons.items.potions;

import com.quadx.dungeons.Game;
import com.quadx.dungeons.tools.stats.PlayerStat;
import com.quadx.dungeons.tools.timers.Delta;

import static com.quadx.dungeons.tools.timers.Time.SECOND;

/**
 * Created by Chris Cavazos on 5/31/2018.
 */
public class HealthPotion extends Potion {
    public HealthPotion() {
        dEffectTime = new Delta(10 * SECOND);

        name = "Health Potion";
        effects.add("MAX HP");
        effects.add("2x HP REGEN");
        gINIT(1,"pRed");
    }

    @Override
    void onStart() {
        PlayerStat st = Game.player.st;
        st.setHp(st.getHpMax());
        st.scaleHpRegen(2f);
    }

    @Override
    void onEnd() {
        PlayerStat st = Game.player.st;
        st.scaleHpRegen(.5f);
    }

    @Override
    public void onUpdate(float dt) {
    }
}
