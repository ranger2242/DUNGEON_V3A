package com.quadx.dungeons.attacks;

import com.badlogic.gdx.math.Rectangle;
import com.quadx.dungeons.tools.ImageLoader;

import static com.quadx.dungeons.Game.HEIGHT;
import static com.quadx.dungeons.Game.WIDTH;
import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.states.mapstate.MapState.viewX;
import static com.quadx.dungeons.states.mapstate.MapState.viewY;

/**
 * Created by Tom on 11/21/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Quake extends Attack {
    public Quake(){
        costGold=35000;
        type=1;
        int a= (int) (player.getEnergyMax()/2);
        powerA = new int[]{80,90,110,130,150};
        costA =new int[]{50,90,110,150,190};
        name="Quake";
        power=80;
        cost=a;
        mod=10;
        spread=0;
        range=0;
        description="EARTHQUAKE.";
        hitBoxShape=HitBoxShape.Rect;
        setIcon(ImageLoader.attacks.get(9));
    }
    public Rectangle calculateHitBox(){
        return new Rectangle(viewX,viewY,viewX+WIDTH,viewY+HEIGHT);
    }
}
