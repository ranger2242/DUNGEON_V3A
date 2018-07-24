package com.quadx.dungeons.items.potions;

import com.quadx.dungeons.Game;
import com.quadx.dungeons.tools.stats.PlayerStat;
import com.quadx.dungeons.tools.timers.Delta;

import static com.quadx.dungeons.tools.timers.Time.ft;

/**
 * Created by Chris Cavazos on 6/5/2018.
 */
public class StatPotion extends Potion {
    public int[] getStats() {
        return stats;
    }

    public void setStats(int[] stats) {
        this.stats = stats;
        strmod=stats[0];
        defensemod=stats[1];
        intelmod=stats[2];
        speedmod=stats[3];
    }

    int[] stats= new int[4];

    public StatPotion(){
        dEffectTime=new Delta(2*ft);
        name="Stat Boost Potion";
        gINIT(1,"pMagenta");
    }



    @Override
    void onStart() {
        PlayerStat st = Game.player.st;
        st.addStrength(stats[0]);
        st.addDefense(stats[1]);
        st.addIntel(stats[2]);
        st.addSpeed(stats[3]);

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
