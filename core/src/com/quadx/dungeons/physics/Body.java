package com.quadx.dungeons.physics;

import com.badlogic.gdx.math.Vector2;

import static com.quadx.dungeons.GridManager.bound;
import static com.quadx.dungeons.GridManager.res;
import static com.quadx.dungeons.states.mapstate.MapState.cell;

/**
 * Created by Chris Cavazos on 4/28/2018.
 */
public class Body {

    static float max = res * cell.x;//grid width
    static float min = cell.x * .8f;//slight extension past the grid boundaries

    static float wrap(float p, float r1, float r2, float w) {
       // return  boundW(p,-min,max);
        if (r1 < -min) {
            p = max - (2 * w);
        }
        if (r2 > max) {
            p = 2 * w;
        }
        return p;
    }

    public static Vector2 wrapPos(Vector2 absPos) {//outputs (absolutePos, gridPos)
        Vector2 p = new Vector2(absPos);
        p.x=bound(p.x,max);
        p.y=bound(p.y,max);
/*

        Vector2 bl = new Vector2(absPos).sub(iconDim);
        Vector2 tr = new Vector2(absPos).add(iconDim);

        p.x = wrap(p.x, bl.x, tr.x, iconDim.x);
        p.y = wrap(p.y, bl.y, tr.y, iconDim.y);
*/

        return p;
    }
}
