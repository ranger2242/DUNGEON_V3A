package com.quadx.dungeons.attacks;

import com.badlogic.gdx.math.Rectangle;
import com.quadx.dungeons.attacks.hitboxTypes.ForwardRectHitBox;
import com.quadx.dungeons.tools.ImageLoader;

/**
 * Created by Tom on 11/21/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Stab extends Attack {
    public Stab(){
        costGold=2000;
        type=CostType.Energy;
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
        loadArray();
        setIcon(ImageLoader.attacks.get(10));
    }

    @Override
    public void runAttackMod() {

    }

    public  Rectangle getHitBox() {
        return new ForwardRectHitBox().getShape(this);
    }
}
