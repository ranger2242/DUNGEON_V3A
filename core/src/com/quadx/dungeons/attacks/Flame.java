package com.quadx.dungeons.attacks;

import com.badlogic.gdx.math.Rectangle;
import com.quadx.dungeons.attacks.hitboxTypes.ForwardRectHitBox;
import com.quadx.dungeons.tools.ImageLoader;

/**
 * Created by Tom on 11/17/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Flame extends Attack {
    public Flame()  {
        hitBoxShape =HitBoxShape.Rect;
        ptSpawnH=0;
        ptSpawnW=50;
        costGold=100;
        type=CostType.Mana;
        powerA = new int[]{100,70,85,100,120};
        costA =new int[]{10,20,30,50,70};
        name="Flame";
        power=40;
        cost=30;
        mod=0;
        description="Player creates a burst of fire.";
        spread=3;
        range=6;
        loadArray();
        setIcon(ImageLoader.attacks.get(2));
    }


    @Override
    public void runAttackMod() {

    }

    public Rectangle getHitBox() {
        return new ForwardRectHitBox().getShape(this);
    }
}
