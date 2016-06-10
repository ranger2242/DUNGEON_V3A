package com.quadx.dungeons.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.quadx.dungeons.Game;

import java.util.ArrayList;

/**
 * Created by Tom on 12/30/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class ControlState extends State {
    private ArrayList<String> controlList = new ArrayList<>();

    public ControlState(GameStateManager gsm){
        super(gsm);
        controlList.add("W A S D - Move");
        controlList.add("I J K L - Aim");
        controlList.add("Shift - Dig");
        controlList.add("Ctrl - Sprint/Fast Dig");
        controlList.add("1-8 - Select Attack");
        controlList.add("Space - Attack");
        controlList.add("F1-F3 - Use Item");
        controlList.add("Q E - Scroll Inventory Left/Right");
        controlList.add("Tab- Exit Menus");
        controlList.add("");
        controlList.add("*Debug*");
        controlList.add("M - Load New Map");
        controlList.add("Comma - Load Shop ");
        //controlList.add("");

    }

    protected void handleInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.TAB)){
            gsm.pop();
        }
    }
    public void update(float dt) {
        handleInput();
    }
    public void render(SpriteBatch sb) {
        sb.begin();
        Game.setFontSize(3);
        for(int i=0;i<controlList.size();i++)
        Game.getFont().draw(sb,controlList.get(i),30,Game.HEIGHT-(30*(i+1)));
        sb.end();
    }
    public void dispose() {

    }
}
