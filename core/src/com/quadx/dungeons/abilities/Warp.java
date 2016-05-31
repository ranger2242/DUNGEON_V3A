package com.quadx.dungeons.abilities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.Map;
import com.quadx.dungeons.Cell;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.GridManager;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.states.mapstate.MapStateRender;
import com.quadx.dungeons.states.mapstate.MapStateUpdater;

import java.util.ArrayList;

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
        MapState.out(Game.player.getName() + " activated the WARP ability!");
        MapState.out("Warp 10 spaces forward on demand.");
        MapState.out("Cost 10E");
        MapState.out("Cooldown 4s");
        MapStateRender.setHoverText("WARP!", 1.5f, Color.WHITE, Game.player.getPX(),Game.player.getPY(),false);
    }

    @Override
    public int getMod() {
        return 7;
    }

    public static void warpPlayer(){
        if(timeCounter>cooldown){
            char front=MapState.lastPressed;
            for(int i=0;i<10;i++) {
                if(front=='w')MapStateUpdater.movementHandler(0, 1, 'w');
                if(front=='s')MapStateUpdater.movementHandler(0, -1, 's');
                if(front=='a')MapStateUpdater.movementHandler(-1, 0, 'a');
                if(front=='d')MapStateUpdater.movementHandler(1, 0, 'd');

            }
            timeCounter=0;
        }
        else{
            MapStateRender.setHoverText(""+(cooldown-timeCounter),.5f,Color.GRAY, Game.player.getPX(),Game.player.getPY(),false);
        }
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