package com.quadx.dungeons.attacks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.tools.timers.Delta;
import com.quadx.dungeons.tools.gui.HoverText;
import com.quadx.dungeons.tools.shapes.Circle;
import com.quadx.dungeons.tools.shapes.Line;
import com.quadx.dungeons.tools.shapes.Triangle;

import java.util.ArrayList;
import java.util.Arrays;

import static com.quadx.dungeons.Game.ft;
import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.tools.gui.HUD.out;

/**
 * Created by Tom on 11/14/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Attack {

    private static Delta dShowInfo = new Delta(10 * ft);
    private static Delta dUseAttack = new Delta(20 * ft);
    HitBoxShape hitBoxShape = null;
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
    int ptSpawnH = 0;
    int ptSpawnW = 0;
    private final int[] usesCheck = {80, 300, 550, 750, 1000};
    private Texture icon;
    public static int pos = 0;
    String description = "s";

    public enum HitBoxShape {
        Circle, Rect, Chain, Triangle, None;

        public boolean overlaps(Rectangle hitBox) {
            switch (this) {
                case Circle:
                    if (player.getAttackCircle().overlaps(hitBox)) {
                        return true;
                    }
                    break;
                case Rect:
                    if (player.getAttackBox().overlaps(hitBox)) {
                        return true;
                    }
                    break;
                case Triangle: {
                    if (player.getAttackTriangle().overlaps(hitBox))
                        return true;
                    break;
                }
            }
            return false;
        }
    }


    Attack() {
    }


    public static void update(float dt) {
        dShowInfo.update(dt);
        dUseAttack.update(dt);
        AttackMod.updaterVariables(dt);
        Protect.updateSelf(dt);
        Dash.updateSelf(dt);
    }

    public static void incPos() {
        changePos(pos + 1);
    }

    public static void changePos(int i) {
        pos = i;
        pos = pos % player.attackList.size();
 /*       Attack a=player.attackList.get(i);
        dUseAttack=new Delta(3*ft);*/
        //-------------------------------
    }

    public static void fixSelectorPos() {
        if (pos >= player.attackList.size())
            changePos(0);
    }

    public static void showDescription(int x) {
        if (dShowInfo.isDone() && x < player.attackList.size()) {
            Attack a = player.attackList.get(x);
            out(a.getName() + ":");
            out("P:" + a.getPowerArr());
            out("M:" + a.getCostArr());
            out(a.getDescription());
            dShowInfo.reset();
        }
    }

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

    public void runAttackMod() {
    }

    public void use() {
        if (dUseAttack.isDone()) {
            Attack a = player.getAttack();
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
                        player.setAttackCircle(a.getHitCircle());
                        break;
                    case Rect:
                        player.setAttackBox(a.getHitBox());
                        break;
                    case Chain:
                        player.setAttackChain(a.getHitChainList());
                        break;
                    case Triangle:
                        player.setAttackTriangle(a.getHitTri());
                        break;
                    case None:
                        runAttackMod();
                        break;
                }
                setUses();
                checkLvlUp();

                dUseAttack.reset();
            } else
                new HoverText("-!-", .5f, Color.MAGENTA, player.getAbsPos().x + (player.getIcon().getWidth() / 2), player.getAbsPos().y + player.getIcon().getHeight() + 10, true);
        }
    }

    private void setUses() {
        uses++;
    }

    void setIcon(Texture t) {
        icon = t;
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

    public String getName() {
        return name;
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

    public HitBoxShape getHitBoxShape() {
        return hitBoxShape;
    }

    public Rectangle getHitBox() {
        return new Rectangle();
    }

    public Texture getIcon() {
        return icon;
    }

    public Vector2 getSpawnBox() {
        return new Vector2(ptSpawnW, ptSpawnH);
    }

    ArrayList<Line> getHitChainList() {
        return new ArrayList<>();
    }

    Circle getHitCircle() {
        return new Circle();
    }

    Triangle getHitTri() {
        return new Triangle();
    }
}
