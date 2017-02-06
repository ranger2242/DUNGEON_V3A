package com.quadx.dungeons.attacks;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.tools.Direction;
import com.quadx.dungeons.tools.ImageLoader;

import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.GridManager.fixHeight;
import static com.quadx.dungeons.states.mapstate.MapState.cell;

/**
 * Created by Tom on 11/21/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Stab extends Attack {
    public Stab(){
        costGold=2000;
        type=1;
        powerA = new int[]{70,85,95,115,145};
        costA =new int[]{8,16,32,64,100};
        //costA =new int[]{0,0,0,0,0};

        name="Stab";
        power=50;
        cost=20;
        mod=-1;
        spread=2;
        range=7;
        hitBoxShape =HitBoxShape.Rect;
        description="Stabs the opponent.";
        setIcon(ImageLoader.attacks.get(10));
    }
    public  Rectangle calculateHitBox() {
        float x, y, w, h;
        int spread=2;
        int range=7;
        if (player.facing.equals(Direction.Facing.North) || player.facing.equals(Direction.Facing.Northeast) || player.facing.equals(Direction.Facing.Northwest)) {
            w=spread*cell.x;
            h=range*cell.y;
            x = player.getAbsPos().x-(w/2)+(player.getIcon().getWidth()/2);
            y = player.getAbsPos().y+(player.getIcon().getHeight());
        } else if (player.facing.equals(Direction.Facing.South) || player.facing.equals(Direction.Facing.Southeast) || player.facing.equals(Direction.Facing.Southwest)) {
            w=spread*cell.x;
            h=range*cell.y;
            x = player.getAbsPos().x-(w/2)+(player.getIcon().getWidth()/2);
            y = player.getAbsPos().y-h-(player.getIcon().getHeight());
        } else if (player.facing.equals(Direction.Facing.East)) {
            w=range*cell.x;
            h=spread*cell.y;
            x = player.getAbsPos().x+(player.getIcon().getWidth());
            y = player.getAbsPos().y-(h/2)+(player.getIcon().getHeight()/2);
        } else{
            w=range*cell.x;
            h=spread*cell.y;
            x = player.getAbsPos().x-w;
            y = player.getAbsPos().y-(h/2)+(player.getIcon().getHeight()/2);
        }
        // return new Rectangle(x,y,w,h);

        return new Rectangle(x,fixHeight(new Vector2(x,y)),w,h);
    }
}
