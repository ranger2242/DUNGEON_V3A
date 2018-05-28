package com.quadx.dungeons.attacks;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.tools.ImageLoader;

import static com.quadx.dungeons.Game.player;

/**
 * Created by Tom on 11/21/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Dash extends Attack {
    public static boolean active                                         =false;
    public static float dtDash=0;
    public static float dashEnd=.5f;

    public Dash(){
        costGold=48035;
        type=CostType.Energy;
        powerA = new int[]{70,85,95,115,145};
        costA =new int[]{32,64,100,120,140};
        //costA =new int[]{0,0,0,0,0};
        name="Dash";
        power=powerA[0];
        cost=costA[0];
        mod=-1;
        hitBoxShape =HitBoxShape.Rect;
        description="Charges in the forward direction";
        setIcon(ImageLoader.attacks.get(13));
    }
    public void runAttackMod() {
        active=true;
    }
    public static void updateSelf(float dt){
        if(active){
            dtDash+=dt;
            player.setAttackBox(player.getAttack().getHitBox());
            if(dtDash>dashEnd){
                active =false;
                dtDash=0;
            }
        }
    }

    public Rectangle getHitBox() {
        active=true;
        Rectangle rect=player.body.getHitBox();
        Vector2 v=new Vector2(player.body.getIconsDim()).scl(.5f);

        switch (player.body.getFacing()) {
            case North:
                rect.y+=v.y;
                break;
            case Northwest:
                rect.x-=v.x;
                rect.y+=v.y;
                break;
            case West:
                rect.x-=v.x;
                break;
            case Southwest:
                rect.x-=v.x;
                rect.y-=v.y;
                break;
            case South:
                rect.y-=v.y;
                break;
            case Southeast:
                rect.y-=v.y;
                rect.x+=v.x;
                break;
            case East:
                rect.x+=v.x;
                break;
            case Northeast:
                rect.x+=v.x;
                rect.y+=v.y;
                break;
        }
/*
        if (player.facing.equals(Direction.Facing.North)) {
        } else if (player.facing.equals(Direction.Facing.South)) {
        } else if (player.facing.equals(Direction.Facing.East)) {
        }else if (player.facing.equals(Direction.Facing.West)) {
            }
        else if (player.facing.equals(Direction.Facing.Northeast)) {
        }else if (player.facing.equals(Direction.Facing.Northwest)) {
        }else if (player.facing.equals(Direction.Facing.Southeast)) {
        }else if (player.facing.equals(Direction.Facing.Southwest)) {
        }*/
        return rect;
    }
}
