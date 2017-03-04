package com.quadx.dungeons.attacks;

import com.badlogic.gdx.math.Rectangle;
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
        type=1;
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
    public static void update(float dt){
        if(active){
            dtDash+=dt;
            //player.setAttackBox(calculateHitBox());
            if(dtDash>dashEnd){
                active =false;
                dtDash=0;
            }
        }
    }

    public Rectangle calculateHitBox() {
        active=true;
        Rectangle rect=player.getHitBox();
        int h=player.getIcon().getHeight()/2;
        int w=player.getIcon().getWidth()/2;
        switch (player.facing) {
            case North:
                rect.y+=h;
                break;
            case Northwest:
                rect.x-=w;
                rect.y+=h;
                break;
            case West:
                rect.x-=w;
                break;
            case Southwest:
                rect.x-=w;
                rect.y-=h;
                break;
            case South:
                rect.y-=h;
                break;
            case Southeast:
                rect.y-=h;
                rect.x+=w;
                break;
            case East:
                rect.x+=w;
                break;
            case Northeast:
                rect.x+=w;
                rect.y+=h;
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
