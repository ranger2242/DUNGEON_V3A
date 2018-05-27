package com.quadx.dungeons.physics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.Player;
import com.quadx.dungeons.attacks.Dash;
import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.shapes1_5.EMath;
import com.quadx.dungeons.tools.Direction;

import static com.quadx.dungeons.GridManager.*;
import static com.quadx.dungeons.states.mapstate.MapState.cell;
import static com.quadx.dungeons.states.mapstate.MapState.cellW;

/**
 * Created by Chris Cavazos on 4/28/2018.
 */
public class Body {
    private final Vector2 pos = new Vector2(0,0);
    private final Vector2 absPos = new Vector2(0, 0);
    public Direction.Facing facing = Direction.Facing.North;
    private float velocity = 10;
    private boolean isPlayer=false;
    private Player player;
    private Texture[] icons = new Texture[4];
    private float max;//grid width
    private Monster monster;

    private void calcVeloctiy() {
        float v;
        if(isPlayer) {
            float s = player.st.getSpdComp();
            v = (float) (6 + .0136 * s + .000005 * Math.pow(s, 2));
            if (Dash.active)
                v *= 6;
            v = bound((int) v, 5, 18);
        }else{
            v=
                    (float) (6 + .0136 * monster.st.getSpeed()
                            + .000005 * Math.pow(monster.st.getSpeed(), 2)) * (3f / 4f);
        }
        velocity = v;
    }
    public void init(){
        max = res * cell.x;//grid width
    }
    public void setPos(Vector2 v) {
        pos.set(v);
    }
    public void setIcons(Texture[] a){
        icons=a;
    }
    public void setFacing(Direction.Facing f){
        facing = f;
    }
    public void setPlayer(Player p ){
        player=p;
        isPlayer=true;
    }
    public void setMonster(Monster m){
        this.monster=m;
    }
    public void setAbs(Vector2 a) {
        float max = res * cellW;
        a.x = boundW(a.x, max, getIconDim().x);
        a.y = boundW(a.y, max, getIconDim().y);
        absPos.set(a);
        float x = boundW((float) EMath.round(absPos.x / cellW));
        float y = boundW((float) EMath.round(absPos.y / cellW));
        setPos(new Vector2(x, y));
    }
    public void update(float dt) {
        calcVeloctiy();
        setAbs(wrapPos(absPos));
    }

    public Vector2 wrapPos(Vector2 absPos) {//outputs (absolutePos, gridPos)
        Vector2 p = new Vector2(absPos);
        p.x=bound(p.x,max);
        p.y=bound(p.y,max);
        return p;
    }
    public Vector2 getPos() {
        return new Vector2(pos);
    }
    public Vector2 getAbs() {
        return new Vector2(absPos);
    }
    public Vector2 getFixedPos(){
        return new Vector2(absPos.x, fixY(absPos));
    }
    public Vector2 getIconDim() {
        Texture ic = getIcon();
        return new Vector2(ic.getWidth(), ic.getHeight());
    }
    public Texture getIcon() {
        int u;

        switch (facing) {
            case North:
            case Northwest:
            case Northeast:
                u = 0;
                break;
            case Southwest:
            case South:
            case Southeast:
                u = 2;
                break;
            case East:
                u = 1;
                break;
            case West:
                u = 3;
                break;
            default:
                u = 0;
                break;
        }

        return icons[u];
    }
    public Direction.Facing getFacing() {
        return facing;
    }
    public Rectangle getHitBox() {
        Vector2 v = getIconDim();
        Vector2 p = getFixedPos();
        return new Rectangle(p.x, p.y, v.x, v.y);
    }

    public double getVelMag() {
        return velocity;
    }
}
