package com.quadx.dungeons.abilities;

import java.util.ArrayList;

import static com.quadx.dungeons.tools.gui.HUD.out;

/**
 * Created by Chris Cavazos on 5/27/2016.
 */
public class DigPlus extends Ability {
    //protected static ArrayList<String> details=new ArrayList<>();
    public DigPlus() {
        details();
    }
      @Override
    public void onActivate() {
        enabled=true;
        out("----------------------------------");
        //out(Game.player.getName()+" activated the Dig+ ability!");
        out("DIG!");
        hoverName();
        //super.onActivate();
    }

    @Override
    public int getMod() {
        return 1;
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

    public ArrayList<String> details() {
        details.clear();
        details.add("- DIG+ -");
        details.add("Extended dig in forward direction");

        return details;
    }

    @Override
    public String getName() {
        return "DIG+";
    }
}
