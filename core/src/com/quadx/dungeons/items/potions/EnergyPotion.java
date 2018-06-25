package com.quadx.dungeons.items.potions;

import com.quadx.dungeons.Game;
import com.quadx.dungeons.tools.stats.PlayerStat;
import com.quadx.dungeons.tools.timers.Delta;

import static com.quadx.dungeons.tools.timers.Time.SECOND;

/**
 * Created by Chris Cavazos on 5/31/2018.
 */
public class EnergyPotion extends Potion {
    public EnergyPotion(){
        dEffectTime=new Delta(30*SECOND);
        name="Energy Potion";
        fileName="potions\\pYellow.png";
        loadIcon();
        effects.add("MAX ENERGY");
        effects.add("2x MANA REGEN");
    }
    @Override
    void onStart() {
            PlayerStat st = Game.player.st;
            st.setEnergy(st.getEnergyMax());
            st.scaleEnergyRegen(2f);
    }

    @Override
    void onEnd() {
            PlayerStat st = Game.player.st;
            st.scaleEnergyRegen(.5f);

    }

    @Override
    public void onUpdate(float dt) {

    }


}
