package com.quadx.dungeons.ai;

import com.quadx.dungeons.monsters.Monster;

import static com.quadx.dungeons.Game.player;

/**
 * Created by Chris Cavazos on 4/28/2018.
 */
public class AI {
    public enum State {
        INACTIVE, AWARE, WANDER, AGRO, DEAD, INVALID;

        public static State determine(Monster m) {
            State s = INVALID;
            if (m.getAgroBox().overlaps(player.body.getHitBox()))
                s = AGRO;
            if (m.isHit())
                s = AGRO;
            if (s == INVALID)
                s = WANDER;
            return s;
        }
    }
}
