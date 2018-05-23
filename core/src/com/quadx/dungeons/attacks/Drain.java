package com.quadx.dungeons.attacks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.tools.ImageLoader;
import com.quadx.dungeons.tools.gui.HoverText;
import com.quadx.dungeons.tools.shapes.Triangle;

import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.GridManager.fixY;
import static com.quadx.dungeons.states.mapstate.MapState.cell;

/**
 * Created by Tom on 11/17/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Drain extends Attack {
    public Drain() {
        costGold = 6045;
        type = 2;
        powerA = new int[]{25, 35, 45, 50, 70};
        //costA =new int[]{15,25,35,50,60};
        costA = new int[]{0, 0, 0, 0, 0};
        hitBoxShape = HitBoxShape.Triangle;
        name = "Drain";
        power = powerA[level];
        cost = costA[level];
        mod = 1;
        spread = 1;
        range = 10;
        description = "Heals user the same amount as damage done to opponent.";
        setIcon(ImageLoader.attacks.get(1));

    }
    public void runAttackMod(){
        int d=power/4;
        player.addHp(d);
        new HoverText("+"+d,1, Color.GREEN,player.getAbsPos().x,player.getAbsPos().y+50,false);
    }

    public Triangle getHitTri() {
        float[] p = new float[6];
        float base = 4;
        float height = 7;
        float h = player.getIcon().getHeight() / 2;
        float w = player.getIcon().getWidth() / 2;

        float cx = player.getAbsPos().x,
                cy = player.getAbsPos().y;
        switch (player.facing) {
            case North:
            case Northwest:
            case Northeast:
                p[0] = cx + w;
                p[1] = cy + (h * 2);
                p[2] = cx + w - (base * cell.x) / 2;
                p[3] = cy + (h * 2) + (height * cell.y);
                p[4] = cx + w + (base * cell.x) / 2;
                p[5] = cy + (h * 2) + (height * cell.y);

                break;
            case Southwest:
            case South:
            case Southeast:
                p[0] = cx + w;
                p[1] = cy;
                p[2] = cx + w - (base * cell.x) / 2;
                p[3] = cy - (height * cell.y);
                p[4] = cx + w + (base * cell.x) / 2;
                p[5] = cy - (height * cell.y);
                break;
            case West:
                p[0] = cx;
                p[1] = cy + h;
                p[2] = cx - (height * cell.x);
                p[3] = cy + h - (base * cell.y) / 2;
                p[4] = cx - (height * cell.x);
                p[5] = cy + h + (base * cell.y) / 2;
                break;
            case East:
                p[0] = cx + (w * 2);
                p[1] = cy + h;
                p[2] = cx + (w * 2) + (height * cell.x);
                p[3] = cy + h - (base * cell.y) / 2;
                p[4] = cx + (w * 2) + (height * cell.x);
                p[5] = cy + h + (base * cell.y) / 2;

                break;
        }
        p[1] = fixY(new Vector2(p[0], p[1]));
        p[3] = fixY(new Vector2(p[2], p[3]));
        p[5] = fixY(new Vector2(p[4], p[5]));
        return new Triangle(p);
    }
}

