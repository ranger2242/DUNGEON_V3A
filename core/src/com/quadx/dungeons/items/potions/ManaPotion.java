package com.quadx.dungeons.items.potions;

import com.quadx.dungeons.Game;
import com.quadx.dungeons.tools.stats.PlayerStat;
import com.quadx.dungeons.tools.timers.Delta;

import static com.quadx.dungeons.tools.timers.Time.SECOND;

/**
 * Created by Chris Cavazos on 5/31/2018.
 */
public class ManaPotion extends Potion {
    public ManaPotion(){
        super();
        dEffectTime=new Delta(30*SECOND);
        name="Mana Potion";
        effects.add("MAX MANA");
        effects.add("2x MANA REGEN");
        gINIT(1,"pLightBlue");
    }
    @Override
    void onStart() {
            PlayerStat st = Game.player.st;
            st.setMana(st.getManaMax());
            st.scaleManaRegen(2f);
    }

    @Override
    void onEnd() {
            PlayerStat st = Game.player.st;
            st.scaleManaRegen(.5f);

    }

    @Override
    public void onUpdate(float dt) {
    }
}
