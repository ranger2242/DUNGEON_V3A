package com.quadx.dungeons.attacks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.shapes1_5.Triangle;
import com.quadx.dungeons.tools.ImageLoader;
import com.quadx.dungeons.tools.gui.HoverText;

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
        type = CostType.Mana;
        powerA = new int[]{25, 35, 45, 50, 70};
        //costA =new int[]{15,25,35,50,60};
        costA = new int[]{0, 0, 0, 0, 0};
        hitBoxShape = HitBoxShape.Triangle;
        name = "Drain";
        mod = 1;
        spread = 1;
        range = 10;
        description = "Heals user the same amount as damage done to opponent.";
        loadArray();
        setIcon(ImageLoader.attacks.get(1));

    }
    public void runAttackMod(){
        int d=power/4;
        player.st.addHp(d);
        new HoverText("+"+d,Color.GREEN ,player.fixed(),false);

    }

    public Triangle getHitTri() {
        float[] p = new float[6];
        float base = 4;
        float height = 7;
        Vector2 v= new Vector2(player.body.getIconsDim()).scl(.5f);
        float cx = player.abs().x,
                cy = player.abs().y;
        switch (player.body.getFacing()) {
            case North:
            case Northwest:
            case Northeast:
                p[0] = cx + v.x;
                p[1] = cy + (v.y * 2);
                p[2] = cx + v.x - (base * cell.x) / 2;
                p[3] = cy + (v.y * 2) + (height * cell.y);
                p[4] = cx + v.x + (base * cell.x) / 2;
                p[5] = cy + (v.y * 2) + (height * cell.y);

                break;
            case Southwest:
            case South:
            case Southeast:
                p[0] = cx + v.x;
                p[1] = cy;
                p[2] = cx + v.x - (base * cell.x) / 2;
                p[3] = cy - (height * cell.y);
                p[4] = cx + v.x+ (base * cell.x) / 2;
                p[5] = cy - (height * cell.y);
                break;
            case West:
                p[0] = cx;
                p[1] = cy + v.y;
                p[2] = cx - (height * cell.x);
                p[3] = cy + v.y - (base * cell.y) / 2;
                p[4] = cx - (height * cell.x);
                p[5] = cy + v.y + (base * cell.y) / 2;
                break;
            case East:
                p[0] = cx + (v.x * 2);
                p[1] = cy + v.y;
                p[2] = cx + (v.x * 2) + (height * cell.x);
                p[3] = cy + v.y - (base * cell.y) / 2;
                p[4] = cx + (v.x * 2) + (height * cell.x);
                p[5] = cy + v.y + (base * cell.y) / 2;

                break;
        }
        p[1] = fixY(new Vector2(p[0], p[1]));
        p[3] = fixY(new Vector2(p[2], p[3]));
        p[5] = fixY(new Vector2(p[4], p[5]));
        return new Triangle(p);
    }
}

