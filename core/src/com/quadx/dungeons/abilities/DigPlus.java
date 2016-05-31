package com.quadx.dungeons.abilities;

import com.badlogic.gdx.graphics.Color;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.states.mapstate.MapStateRender;

import java.util.ArrayList;

/**
 * Created by Chris Cavazos on 5/27/2016.
 */
public class DigPlus extends Ability {
    //protected static ArrayList<String> output=new ArrayList<>();
    public DigPlus(){
        details();
    }

    @Override
    public void onActivate() {
        enabled=true;
        MapState.out("----------------------------------");
        MapState.out(Game.player.getName()+" activated the Dig+ ability!");
        MapState.out("DIG!");
        MapStateRender.setHoverText("DIG+!",1.5f, Color.WHITE, Game.player.getPX(),Game.player.getPY(),false);

        //super.onActivate();
    }

    @Override
    public int getMod() {
        return 1;
    }

    public ArrayList<String> details() {
        output.clear();
        output.add("- DIG+ -");
        output.add("Extended dig in forward direction");

        return output;
    }

    @Override
    public String getName() {
        return "DIG+";
    }
}
