package com.quadx.dungeons.tools;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;


/**
 * Created by Chris Cavazos on 1/21/2017.
 */
public class ShapeRendererExt extends ShapeRenderer {

    public void rect(Rectangle r) {
        rect(r.x,r.y,r.width,r.height);

    }
}
