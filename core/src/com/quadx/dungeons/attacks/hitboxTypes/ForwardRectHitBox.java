package com.quadx.dungeons.attacks.hitboxTypes;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.attacks.Attack;
import com.quadx.dungeons.tools.Direction;

import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.GridManager.fixY;
import static com.quadx.dungeons.states.mapstate.MapState.cell;

/**
 * Created by Chris Cavazos on 5/20/2018.
 */
public class ForwardRectHitBox {
    enum Orientation {
        VerticalU, HorizontalL,        VerticalD, HorizontalR

    }

    Orientation getOrientation() {
        Orientation o;
        Direction.Facing dir = player.body.getFacing();

        if (dir.equals(Direction.Facing.North) || dir.equals(Direction.Facing.Northeast)
                || dir.equals(Direction.Facing.Northwest)) {
            o = Orientation.VerticalU;

        } else if (dir.equals(Direction.Facing.South) || dir.equals(Direction.Facing.Southeast)
                || dir.equals(Direction.Facing.Southwest)) {
            o = Orientation.VerticalD;
        } else if (dir.equals(Direction.Facing.East)) {
            o = Orientation.HorizontalR;
        } else {
            o = Orientation.HorizontalL;
        }
        return o;
    }

    public Rectangle getShape(Attack a) {
        float x, y, w, h;
        int spread = a.getSpread();
        int range = a.getRange();
        Vector2 abs = player.abs();
        Vector2 ic = player.body.getIconDim();
        Orientation o=getOrientation();

        if (o == Orientation.VerticalU ||o == Orientation.VerticalD  ) {
            w = spread * cell.x;
            h = range * cell.y;
        }else {
            w = range * cell.x;
            h = spread * cell.y;
        }

        switch (o){

            case VerticalU:
                x = abs.x - (w / 2) + (ic.x / 2);
                y = abs.y + (ic.y);
                break;
            case HorizontalL:
                x = abs.x - w;
                y = abs.y - (h / 2) + (ic.y / 2);
                break;
            case VerticalD:
                x = abs.x - (w / 2) + (ic.x / 2);
                y = abs.y - h - (ic.y);
                break;
            default:
                x = abs.x + (ic.x);
                y = abs.y - (h / 2) + (ic.y / 2);
                break;
        }
        return new com.badlogic.gdx.math.Rectangle(x, fixY(new Vector2(x, y)), w, h);
    }
}
