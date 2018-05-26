package com.quadx.dungeons.shapes1_5;

import com.badlogic.gdx.math.Vector2;

import static com.quadx.dungeons.Game.HEIGHT;
import static com.quadx.dungeons.Game.WIDTH;

/**
 * Created by Chris Cavazos on 2/1/2018.
 */
public class Tools {
    public static Vector2 wrap(Vector2 pos) {
        float x=pos.x %= WIDTH + 1;
        float y=pos.y %= HEIGHT+1;
        return new Vector2((x < 0) ? x + WIDTH : x,(y < 0) ? y + HEIGHT : y);
    }
}
