package com.quadx.dungeons;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.commands.Command;
import com.quadx.dungeons.commands.cellcommands.AddItemToCellComm;
import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.physics.Physics;
import com.quadx.dungeons.shapes1_5.EMath;
import com.quadx.dungeons.tools.timers.Delta;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import static com.quadx.dungeons.Game.player;

/**
 * Created by range_000 on 1/10/2017.
 */
public class Anim {
    public static ArrayList<Anim> anims = new ArrayList<>();

    private Cell cell;
    private TextureRegion texture;
    private Vector2 pos;
    private Vector2 dest;
    private Delta dEnd;
    private Type flag;
    private float vel;
    private boolean timerOn;
    private boolean end = false;

    public void setCell(Cell cell, Item item) {
        this.cell = cell;
        this.cell.addCommand(new AddItemToCellComm(item, cell));

    }

    public void render(SpriteBatch sb) {
        if(texture != null)
            sb.draw(texture,pos.x, pos.y);
    }

    public static void update(float dt) {
        try {
            for (Anim a : anims) {
                a.updateSelf(dt);
            }
        } catch (ConcurrentModificationException ignored) {
        }
    }

    public enum Type{
        Drop,MovePlayer
    }


    public Anim(TextureRegion t, Vector2 fixedStart, Vector2 fixedEnd, float v, Type f) {
        texture = t;
        pos = new Vector2(fixedStart);
        vel = v;
        dest =new Vector2(fixedEnd);
        flag = f;
        dEnd = new Delta(0);
        timerOn = false;
    }



    void updatePosition(){
        float dx = Math.abs(EMath.dx(pos, dest));
        float dy = Math.abs(EMath.dy(pos, dest));
        Vector2 velcomp = Physics.getVector(vel, pos, dest);

        pos.x = dx < vel ? dest.x : pos.x + velcomp.x;
        pos.y = dy < vel ? dest.y : pos.y + velcomp.y;
        if(!end && flag == Type.MovePlayer) {
            player.body.setAbs(pos);
        }
    }

    void checkEndConditions(float dt){
        if(timerOn) {
            dEnd.update(dt);
            if (dEnd.isDone()) {
                end = true;
            }
        }

        if ((int) dest.x == (int) pos.x && (int) dest.y == (int) pos.y)
            end = true;

    }

    public void updateSelf(float dt) {
        updatePosition();
        checkEndConditions(dt);
        if(end)
            handleEndAnim();
    }

    void handleEndAnim() {
        if (flag == Type.MovePlayer) {
            player.dig();
        }

        if (flag == Type.Drop) {
            Command c = cell.getNextComm();
            if (c.getClass().equals(AddItemToCellComm.class)) {
                c.execute();
            }
            anims.remove(this);
        }
        player.overrideControls = false;

    }
}

