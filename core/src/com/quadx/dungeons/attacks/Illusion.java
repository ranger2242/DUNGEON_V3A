package com.quadx.dungeons.attacks;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.GridManager.fixY;

/**
 * Created by Tom on 11/18/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Illusion extends Attack {

    public static class Dummy {
        public int hp;
        public Vector2 pos;
        public Vector2 absPos;
        public Rectangle hitbox;
        public TextureRegion icon;
        public boolean dead=false;

        public Dummy(int  v, Vector2 pos, Vector2 absPos, Rectangle h) {
        hp=v;
        this.pos=pos;
        this.absPos=absPos;
        hitbox=h;
        icon=player.getIcon();
        }
        public Vector2 fixed(){
            return new Vector2(absPos.x,fixY(absPos));
        }
    }

    public static ArrayList<Dummy> dummies = new ArrayList<>();

    public Illusion() {
        costGold = 5000;
        type = CostType.Mana;
        powerA = new int[]{0, 0, 0, 0, 0};
        costA = new int[]{45, 50, 60, 65, 70};
        //costA = new int[]{0, 0, 0, 0, 0};

        name = "Illusion";
        power = 0;
        cost = 45;
        mod = 5;
        spread = 1;
        range = 3;
        spread = 3;
        range = 4;
        description = "Lowers INT by increasing amounts.";
        hitBoxShape =HitBoxShape.None;
        loadArray();
        gINIT(2,"icIllusion");
    }

    @Override
    public void runAttackMod() {
        dummies.add(player.getDummy());
    }


}
