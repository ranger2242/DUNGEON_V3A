package com.quadx.dungeons.abilities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.states.mapstate.MapStateRender;

import java.util.ArrayList;

import static com.quadx.dungeons.Game.player;

/**
 * Created by Chris Cavazos on 5/27/2016.
 */
public class Warp extends Ability {

    public Warp() {
        icon= loadIcon("images/icons/abilities/icWarp.png");
    }
    @Override
    public void onActivate() {
        cooldown = 2f;
        enabled = true;
        MapState.out("----------------------------------");
        MapState.out(player.getName() + " activated the WARP ability!");
        MapState.out("Warp 10 spaces forward on demand.");
        MapState.out("Cost 10E");
        MapState.out("Cooldown 4s");
        MapStateRender.setHoverText("WARP!", 1.5f, Color.WHITE, player.getPX(), player.getPY(),false);
    }

    @Override
    public int getMod() {
        return 7;
    }

    @Override
    public void l1() {

    }

    @Override
    public void l2() {

    }

    @Override
    public void l3() {

    }

    @Override
    public void l4() {

    }

    @Override
    public void l5() {

    }

    public static void warpPlayer(){
        if(timeCounter>cooldown){
            //char front=MapState.lastPressed;
                /*

            for(int i=0;i<10;i++) {
                if(front=='w')player.move(0, 1, 'w');
                if(front=='s')player.move(0, -1, 's');
                if(front=='a')player.move(-1, 0, 'a');
                if(front=='d')player.move(1, 0, 'd');
            }
*/

            timeCounter=0;
        }
        else{
            MapStateRender.setHoverText(""+(cooldown-timeCounter),.5f,Color.GRAY, player.getPX(), player.getPY(),false);
        }
    }
    @Override
    public Texture getIcon(){
        return icon;
    }

    public ArrayList<String> details() {
        //ArrayList<String> output=new ArrayList<>();
        output.clear();
        output.add("-WARP-");
        output.add("Warp 10 spaces forward on demand.");
        output.add("Cost 10E");
        output.add("Cooldown 4s");
        return output;
    }

    @Override
    public String getName() {
        return "WARP";
    }
}