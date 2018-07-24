package com.quadx.dungeons.attacks;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.states.State;

import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.Game.scr;
import static com.quadx.dungeons.states.mapstate.MapState.viewY;

/**
 * Created by Tom on 11/21/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Quake extends Attack {
    public Quake(){
        costGold=35000;
        type=CostType.Energy;
        int a= (int) (player.st.getEnergyMax()/2);
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
        gINIT(2,"icQuake");
    }

    @Override
    public void runAttackMod() {

    }

    public Rectangle getHitBox(){
        Vector2 view = State.getView();

        return new Rectangle(view.x,viewY,view.x+scr.x,viewY+scr.y);
    }
}
