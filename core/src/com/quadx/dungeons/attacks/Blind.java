package com.quadx.dungeons.attacks;


import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.GridManager;
import com.quadx.dungeons.shapes1_5.Circle;

/**
 * Created by Tom on 11/18/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Blind extends Attack {
    public Blind()  {
        hitBoxShape =HitBoxShape.Circle;
        costGold=15309;
        type=CostType.Mana;
        powerA = new int[]{30,50,70,90,110};
        costA =new int[]{10,20,30,40,50};
        name="Blind";
        power=10;
        cost=25;
        mod=3;
        range=10;
        spread=10;
        description="Disables enemy vision.";
        loadArray();
        gINIT(2,"icBlind");

        //    addIcon( ImageLoader.attacks.get(0));

    }

    @Override
    public void runAttackMod() {

    }

    public Circle getHitCircle(){
        return new Circle(GridManager.fixYv(new Vector2()) ,200);
    }
}
