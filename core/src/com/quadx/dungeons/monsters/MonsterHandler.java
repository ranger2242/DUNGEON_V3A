package com.quadx.dungeons.monsters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.quadx.dungeons.GridManager;
import com.quadx.dungeons.shapes1_5.ShapeRendererExt;
import com.quadx.dungeons.states.mapstate.MapStateRender;
import com.quadx.dungeons.tools.Tests;

import java.util.ConcurrentModificationException;

/**
 * Created by Chris Cavazos on 7/24/2018.
 */
public class MonsterHandler {
    public static void render(SpriteBatch sb) {
        try {
            for (Monster m : Monster.mdrawList) { // TODO fix this loop to run only monsters on screen
                m.render(sb);
            }
        } catch (Exception ignored) {
        }
    }

    public static void renderSR(ShapeRendererExt sr){
        try {
            //monster list sr functions
            MapStateRender.monAgroBoxes.clear();
            for (Monster m : GridManager.monsterList) { // TODO fix this loop to run only monsters on screen
                if (m != null) {
                    if (Tests.showhitbox) {
                        // add agro box to transparent draw queue
                        MapStateRender.monAgroBoxes.add(m.getAgroBox());
                        //draw monster hitbox
                        sr.setColor(Color.RED);
                        sr.rect(m.body.getHitBox());
                    }
                    //draw healthbars
                    sr.setColor(Color.DARK_GRAY);
                    sr.rect(m.getHbar());
                    if (m.isLowHP())
                        sr.setColor(Color.GREEN);
                    else
                        sr.setColor(Color.RED);
                    sr.rect(m.getHbar2());
                }
            }
        } catch (ConcurrentModificationException ignored) {
        }
    }
}
