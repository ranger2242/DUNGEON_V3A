package com.quadx.dungeons.attacks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.tools.gui.HoverText;
import com.quadx.dungeons.tools.shapes.Circle;
import com.quadx.dungeons.tools.shapes.Line;
import com.quadx.dungeons.tools.shapes.Triangle;

import java.util.ArrayList;
import java.util.Arrays;

import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.tools.gui.HUD.out;

/**
 * Created by Tom on 11/14/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Attack {
    public HitBoxShape getHitBoxShape() {
        return hitBoxShape;
    }

    public enum HitBoxShape{
        Circle,Rect,Chain,Triangle,None
    }
    HitBoxShape hitBoxShape =null;
    int[] powerA = new int[5];
    int[] costA = new int[5];
    String name = "";
    int type;// 1-physical 2-magic
    int power = 0;
    int cost = 0;
    int mod = 0;
    int level = 0;
    private int uses = 0;
    int range = 0;
    int spread = 0;
    int costGold = 0;
    float weight;
    int ptSpawnH = 0;
    int ptSpawnW = 0;
    private final int[] usesCheck = {80, 300, 550, 750, 1000};
    private Texture icon;
    public static float dtInfo = 0;
    public static final float attackMintime = Game.ft *3;
    public static int pos =0;
    String description = "s";
    public static float dtAttack = 0;

    private void checkLvlUp() {
        if (level <= 4)
            if (uses > usesCheck[level]) {
                level++;
                range++;
                uses = 0;
            }

        power = powerA[level];
        cost = costA[level];

    }

    Attack() {
    }

    public Rectangle calculateHitBox() {
        return new Rectangle();
    }
    Circle calculateHitCircle(){
        return new Circle();
    }
    Triangle calculateHitTri(){return new Triangle();}
    ArrayList<Line> calculateHitChain(){return new ArrayList<>();}

    public void runAttackMod(){}
    public Vector2 getSpawnBox() {
        return new Vector2(ptSpawnW, ptSpawnH);
    }
    public static void fixPos(){
        if(pos>=player.attackList.size())
            pos=0;
    }
    public String getName() {
        return name;
    }

    @SuppressWarnings("WeakerAccess")
    public String getDescription() {
        return description;
    }
    @SuppressWarnings("WeakerAccess")
    public String getPowerArr() {
        return Arrays.toString(powerA);
    }

    @SuppressWarnings("WeakerAccess")
    public String getCostArr() {
        return Arrays.toString(costA);
    }

    public int getPower() {
        return power;
    }

    public int getCost() {
        return cost;
    }

    public int getCostGold() {
        return costGold;
    }

    public int getMod() {
        return mod;
    }

    public int getLevel() {
        return level;
    }

    public int getType() {
        return type;
    }

    public int getSpread() {
        return spread;
    }

    public int getRange() {
        return range;
    }

    private void setUses() {
        uses++;
    }

    void setIcon(Texture t) {
        icon = t;
    }

    public Texture getIcon() {
        return icon;
    }

    public static void showDescription(int x) {
        if (dtInfo > .4 && x < player.attackList.size()) {
            Attack a = player.attackList.get(x);
            out(a.getName() + ":");
            out("P:" + a.getPowerArr());
            out("M:" + a.getCostArr());
            out(a.getDescription());
            dtInfo = 0;
        }
    }

    private boolean canUse() {
        switch (getType()) {
            case 1: {
                return player.getEnergy() >= getCost();
            }
            case 2:
            case 3:
            case 4: {
                return player.getMana() >= getCost();
            }
            default:
                return false;
        }
    }

    public void use() {
        if (dtAttack > attackMintime) {
            if (pos < player.attackList.size()) {
                Attack a;
                a = player.attackList.get(pos);
                if (a.canUse()) {
                    switch (type) {
                        case 1: {
                            player.addEnergy(-cost);
                            break;
                        }
                        case 2:
                        case 3:
                        case 4: {
                            player.addMana(-cost);
                            break;
                        }
                    }
                    switch (hitBoxShape) {
                        case Circle:
                            player.setAttackCircle(calculateHitCircle());
                            break;
                        case Rect:
                            player.setAttackBox(calculateHitBox());
                            break;
                        case Chain:
                            player.setAttackChain(calculateHitChain());
                            break;
                        case Triangle:
                            player.setAttackTriangle(calculateHitTri());
                            break;
                        case None:
                            runAttackMod();
                            break;
                    }
                    setUses();
                    checkLvlUp();
                }

            } else
                new HoverText("-!-", .5f, Color.MAGENTA, player.getAbsPos().x + (player.getIcon().getWidth() / 2), player.getAbsPos().y + player.getIcon().getHeight() + 10, true);
        }
        dtAttack = 0;
    }
}
