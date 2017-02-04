package com.quadx.dungeons.attacks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.tools.gui.HoverText;
import com.quadx.dungeons.tools.shapes.Circle;
import com.quadx.dungeons.tools.shapes.Line;

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
        return hbs;
    }

    public enum HitBoxShape{
        Circle,Rect,Chain
    }
    HitBoxShape hbs=null;
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
    private final int[] usesCheck = {80, 300, 650, 1000, 1500};
    private Texture icon;
    public static float dtInfo = 0;
    public static final float attackMintime = Game.frame*3;
    public static int pos =0;

    String description = "s";
    public static float dtAttack = 0;

    public void checkLvlUp() {
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
    public Circle calculateHitCircle(){
        return new Circle();
    }
    ArrayList<Line> chain(){return new ArrayList<>();}
    public Vector2 getSpawnBox() {
        return new Vector2(ptSpawnW, ptSpawnH);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPowerArr() {
        return Arrays.toString(powerA);
    }

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

    public void setUses() {
        uses++;
        checkLvlUp();
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

    public boolean canUse() {
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
                    switch (hbs){
                        case Circle:
                            player.setAttackCircle(calculateHitCircle());
                            break;
                        case Rect:
                            player.setAttackBox(calculateHitBox());
                            break;
                        case Chain:
                            player.setAttackChain(chain());
                            break;
                    }
                    setUses();
                }

            } else
                new HoverText("-!-", .5f, Color.MAGENTA, player.getAbsPos().x + (player.getIcon().getWidth() / 2), player.getAbsPos().y + player.getIcon().getHeight() + 10, true);
        }
        dtAttack = 0;
    }
}