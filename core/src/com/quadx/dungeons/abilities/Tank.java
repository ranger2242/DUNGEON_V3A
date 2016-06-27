package com.quadx.dungeons.abilities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.states.mapstate.MapStateRender;

import java.util.ArrayList;

import static com.quadx.dungeons.Game.player;

/**
 * Created by range on 5/20/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Tank extends Ability {
    public Tank() {
        icon= loadIcon("images/icons/abilities/icTank.png");
        details();
    }
    @Override
    public Texture getIcon(){
        return icon;
    }
    @Override

    public void onActivate() {
        player.setAbility(1);

        Game.player.setDefense(Game.player.getDefense()*2);
        Game.player.setHp((int)(Game.player.getHp()*1.5));
        Game.player.setHpMax((int)(Game.player.getHpMax()*1.5));        Game.player.setHpRegen(Game.player.getHpRegen()*2);
        Game.player.setMoveSpeed((float) (Game.player.getMoveSpeed()*1.005));
        MapState.out("----------------------------------");
        MapState.out(Game.player.getName()+" activated the TANK ability!");
        MapState.out("HP Max x1.5!");
        MapState.out("HP Regen x2!");
        MapState.out("DEF x2!");
        MapState.out("Move Speed x.75!");
        MapStateRender.setHoverText("Tank!!",1.5f, Color.WHITE, Game.player.getPX(),Game.player.getPY(),false);

    }

    @Override
    public int getMod() {
        return 6;
    }

    public ArrayList<String> details() {
        output.clear();

        output.add("-TANK-");
        output.add("HP Max x1.5");
        output.add("HP Regen x2");
        output.add("DEF x2");
        output.add("Move Speed x0.75                                                                                                                                                         ");
        return output;
    }

    @Override
    public String getName() {
        return "TANK";
    }
}
