package com.quadx.dungeons.abilities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.quadx.dungeons.tools.gui.HoverText;

import java.util.ArrayList;

import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.tools.gui.HUD.out;

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
        out("----------------------------------");
        out(player.getName() + " activated the WARP ability!");
        out("Warp 10 spaces forward on demand.");
        out("Cost 10E");
        out("Cooldown 4s");
        hoverName();
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
            new HoverText(""+(cooldown-timeCounter),Color.GRAY, player.fixed(),false);
        }
    }
    @Override
    public Texture getIcon(){
        return icon;
    }

    public ArrayList<String> details() {
        //ArrayList<String> details=new ArrayList<>();
        details.clear();
        details.add("-WARP-");
        details.add("Warp 10 spaces forward on demand.");
        details.add("Cost 10E");
        details.add("Cooldown 4s");
        return details;
    }

    @Override
    public String getName() {
        return "WARP";
    }
}