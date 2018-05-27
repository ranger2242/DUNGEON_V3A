package com.quadx.dungeons.attacks;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.shapes1_5.Circle;
import com.quadx.dungeons.shapes1_5.Line;
import com.quadx.dungeons.shapes1_5.Triangle;
import com.quadx.dungeons.tools.gui.HoverText;
import com.quadx.dungeons.tools.timers.Delta;

import java.util.ArrayList;
import java.util.Arrays;

import static com.quadx.dungeons.GridManager.bound;
import static com.quadx.dungeons.tools.timers.Time.SECOND;
import static com.quadx.dungeons.tools.timers.Time.ft;
import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.tools.gui.HUD.out;

/**
 * Created by Tom on 11/14/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public abstract class Attack {

    public static int pos = 0;

    private static Delta dShowInfo = new Delta(10 * ft);
    private static Delta dUse = new Delta(10 * ft);
    protected HitBoxShape hitBoxShape = null;
    protected CostType type;
    private Texture icon;

    private final int[] usesCheck = {80, 300, 550, 750, 1000};
    protected int[] powerA = new int[5];
    protected int[] costA = new int[5];
    protected int power = 0;
    protected int cost = 0;
    protected int mod = 0;
    private int level = 0;
    private int uses = 0;
    protected int range = 0;
    protected int spread = 0;
    protected int costGold = 0;
    protected int ptSpawnH = 0;
    protected int ptSpawnW = 0;

    String description = "s";
    String name = "";



    public enum HitBoxShape {
        Circle, Rect, Chain, Triangle, None
    }

    public enum CostType{
        Energy, Mana
    }

    public static void update(float dt) {
        dShowInfo.update(dt);
        dUse.update(dt);
        //AttackMod.updaterVariables(dt);
        Protect.updateSelf(dt);
        Dash.updateSelf(dt);
    }

    public static void changePos(int i) {
        pos = i;
        pos = bound(pos,player.attackList.size());
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
        uses++;
        if (level <= 4 && uses > usesCheck[level]) {
            level++;
            range++;
            uses = 0;
            loadArray();
        }
    }

    public void loadArray(){
        power = powerA[level];
        cost = costA[level];
    }

    public abstract void runAttackMod();

    public void use() {
        if (dUse.isDone()) {
            if (player.canUseAttack()) {
                player.useAttack(this);
                uses++;
                checkLvlUp();
                dUse.reset();
            } else
                new HoverText("-!-", SECOND, Color.MAGENTA, player.fixed(), true);
        }
    }


    //SETTERS------------------------------------------------------------

    void setIcon(Texture t) {
        icon = t;
    }
    //GETTERS------------------------------------------------------------
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

    public CostType getType() {
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

    public ArrayList<Line> getHitChainList() {
        return new ArrayList<>();
    }

    Circle getHitCircle() {
        return new Circle();
    }

    public Triangle getHitTri() {
        return new Triangle();
    }
}
