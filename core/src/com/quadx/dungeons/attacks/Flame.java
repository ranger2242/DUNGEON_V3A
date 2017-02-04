package com.quadx.dungeons.attacks;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.tools.Direction;
import com.quadx.dungeons.tools.ImageLoader;

import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.GridManager.fixHeight;
import static com.quadx.dungeons.states.mapstate.MapState.cell;

/**
 * Created by Tom on 11/17/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Flame extends Attack {
    public Flame()  {
        hbs=HitBoxShape.Rect;
        ptSpawnH=0;
        ptSpawnW=50;
        costGold=100;
        type=2;
        powerA = new int[]{40,70,85,100,120};
        costA =new int[]{10,20,30,50,70};
        name="Flame";
        power=40;
        cost=30;
        mod=0;
        description="Player creates a burst of fire.";
        spread=3;
        range=6;
        setIcon(ImageLoader.attacks.get(2));
    }

    @Override
    public Rectangle calculateHitBox() {
        float x, y, w, h;
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
