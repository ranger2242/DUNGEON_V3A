package com.quadx.dungeons.physics;

import com.badlogic.gdx.math.Vector2;
import javafx.util.Pair;

import static com.quadx.dungeons.GridManager.res;
import static com.quadx.dungeons.states.mapstate.MapState.cellW;

/**
 * Created by Chris Cavazos on 4/28/2018.
 */
public class Body {
    public static Pair<Vector2,Vector2> wrapPos(Vector2 iconDim, Vector2 absPos){//outputs (absolutePos, gridPos)
        float x = absPos.x;
        float y = absPos.y;

        float gw = res * cellW;//grid width
        float c = cellW *.8f;//slight extension past the grid boundaries

        float x1 = absPos.x - iconDim.x,//corners of the player icon
                x2 = absPos.x + iconDim.x,
                y1 = absPos.y - iconDim.y,
                y2 = absPos.y + iconDim.y;


        if (x1 < -c) {
            x = gw - iconDim.x;
        } else if (x2 > gw) {
            x = iconDim.x;
        }
        if (y1 < -c) {
            y = gw - iconDim.y;
        } else if (y2 > gw) {
            y = iconDim.y;
        }

        int gx = Math.round(x / cellW);
        int gy = Math.round(y / cellW);

        return new Pair<>(new Vector2(x, y),new Vector2(gx, gy));
    }
}
