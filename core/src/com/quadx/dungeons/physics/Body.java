package com.quadx.dungeons.physics;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.GridManager;
import com.quadx.dungeons.Player;
import com.quadx.dungeons.attacks.Dash;
import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.shapes1_5.EMath;
import com.quadx.dungeons.tools.Direction;
import com.quadx.dungeons.tools.timers.Delta;

import static com.quadx.dungeons.GridManager.*;
import static com.quadx.dungeons.GridManager.rn;
import static com.quadx.dungeons.states.mapstate.MapState.*;
import static com.quadx.dungeons.tools.timers.Time.ft;

/**
 * Created by Chris Cavazos on 4/28/2018.
 */
public class Body {
    private final Vector2 pos = new Vector2(0, 0);
    private final Vector2 absPos = new Vector2(0, 0);
    public Direction.Facing facing = Direction.Facing.North;
    private float velocity = 10;
    private boolean isPlayer = false;
    private Player player;
    private Monster monster;
    private TextureRegion icon;

    boolean bumped = false;
    Vector2 knockBackVel = new Vector2();
    Delta dKb = new Delta(5 * ft);

    public void setKnockBackDest(Vector2 initPos) {
        Vector2 vel = Physics.getVector(15 * cellW, initPos, getAbs());
        knockBackVel = vel;
        bumped = true;
    }

    public void update(float dt) {
        calcVeloctiy();
        if (bumped) {
            dKb.update(dt);
            Vector2 v = new Vector2(knockBackVel).scl(2 * dt);
            setAbs(getAbs().add(v));
            if (dKb.isDone()) {
                dKb.reset();
                gm.clearArea(getPos(), true);
                bumped = false;
            }
        }

        setAbs(wrapPos(absPos));
    }

    private void calcVeloctiy() {
        float v;
        if (isPlayer) {
            float s = player.st.getSpdComp();
            v = (float) (6 + .0136 * s + .000005 * Math.pow(s, 2));
            if (Dash.active)
                v *= 6;
            v = bound((int) v, 5, 18);
        } else {
            v =
                    (float) (6 + .0136 * monster.st.getSpeed()
                            + .000005 * Math.pow(monster.st.getSpeed(), 2)) * (3f / 4f);
        }
        velocity = v;
    }

    public void init() {
        facing = Direction.Facing.values()[rn.nextInt(Direction.Facing.values().length)];
    }

    private void setPos(Vector2 v) {
        pos.set(v);
    }


    public void setIcon(TextureRegion a) {
        icon = a;
    }

    public void setFacing(Direction.Facing f) {
        facing = f;
    }

    public void setPlayer(Player p) {
        player = p;
        isPlayer = true;
    }

    public void setMonster(Monster m) {
        this.monster = m;
    }

    public void setAbs(Vector2 a) {
        Vector2 v = getIconDim();
        float max = res * cellW;
        a.x = boundW(a.x, max, v.x);
        a.y = boundW(a.y, max, v.y);
        absPos.set(a);
        float y = (float) EMath.round(absPos.y / cellW);
        float x = (float) EMath.round(absPos.x / cellW);
        setPos(new Vector2(x, y));
    }


    public Vector2 wrapPos(Vector2 absPos) {//outputs (absolutePos, gridPos)
        float max = res * cell.x;//grid width

        Vector2 p = new Vector2(absPos);
        p.x = bound(p.x, max);
        p.y = bound(p.y, max);
        return p;
    }

    public Vector2 getPos() {
        return new Vector2(pos);
    }

    public Vector2 getAbs() {
        return new Vector2(absPos);
    }

    public Vector2 getFixedPos() {
        return GridManager.fixYv(absPos);
    }

    public Direction.Facing getFacing() {
        return facing;
    }

    public Vector2 getIconDim(){
        Vector2 v=new Vector2();
        if(player!=null)
            v = player.getIconDim();
        else if(monster !=null)
            v = monster.getIconDim();
        return v;
    }
    public Rectangle getHitBox() {
        Vector2 v= getIconDim();
        Vector2 p = getFixedPos();
        return new Rectangle(p.x, p.y, v.x, v.y);
    }

    public double getVelMag() {
        return velocity;
    }

    public void setVelMag(float i) {
        velocity = i;
    }
}
