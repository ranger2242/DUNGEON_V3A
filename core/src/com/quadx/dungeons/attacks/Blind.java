package com.quadx.dungeons.attacks;


import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.GridManager;
import com.quadx.dungeons.tools.ImageLoader;
import com.quadx.dungeons.tools.shapes.Circle;

import static com.quadx.dungeons.Game.player;

/**
 * Created by Tom on 11/18/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Blind extends Attack {
    public Blind()  {
        hitBoxShape =HitBoxShape.Circle;
        costGold=15309;
        type=4;
        powerA = new int[]{30,50,70,90,110};
        costA =new int[]{10,20,30,40,50};
        name="Blind";
        power=10;
        cost=25;
        mod=3;
        range=10;
        spread=10;
        description="Disables enemy vision.";
        setIcon( ImageLoader.attacks.get(0));

    }
    public Circle getHitCircle(){
        return new Circle(new Vector2(player.getAbsPos().x+(player.getIcon().getWidth()/2), GridManager.fixY(player.getAbsPos())) ,200);
    }
}
