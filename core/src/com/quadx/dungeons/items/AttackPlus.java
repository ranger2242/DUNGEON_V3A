package com.quadx.dungeons.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Tom on 11/21/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class AttackPlus extends Item {
    public AttackPlus(){
        name="Attack+";
        attackmod=1;
        cost=1000;
        loadIcon(name);
    }
}
