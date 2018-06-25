package com.quadx.dungeons.attacks;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.attacks.hitboxTypes.ForwardRectHitBox;
import com.quadx.dungeons.tools.ImageLoader;

/**
 * Created by Tom on 11/17/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Flame extends Attack {
    boolean shield=false;

    public Flame(boolean shield)  {
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
        this.shield=shield;
    }


    @Override
    public void runAttackMod() {

    }

    public Rectangle getHitBox() {
        if(shield) {
            int r=100;
            Vector2 v=new Vector2(Game.player.fixed());
            v.add(-r,-r);
            return new Rectangle(v.x,v.y,r*2,r*2);
        }else
        return new ForwardRectHitBox().getShape(this);
    }
}
