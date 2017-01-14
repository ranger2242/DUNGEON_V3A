package com.quadx.dungeons.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Tom on 11/21/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class AttackPlus extends Item {
    public AttackPlus(){
        ptColor= Color.RED;
        name="Attack+";
        attackmod=1;
        cost=1000;
        setIcon(new Texture("images\\icons\\items\\icAtt+.png"));

    }
}
