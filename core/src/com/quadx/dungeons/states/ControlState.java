package com.quadx.dungeons.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.quadx.dungeons.Game;

import java.util.ArrayList;

import static com.quadx.dungeons.Game.commandList;

/**
 * Created by Tom on 12/30/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class ControlState extends State {
    private final ArrayList<String> controlList = new ArrayList<>();

    public ControlState(GameStateManager gsm){
        super(gsm);
        //controlList.add("");
        controlList.add("");
        controlList.add("W A S D        : Move");
        controlList.add("I J K L        : Aim");
        controlList.add("Shift          : Dig");
        controlList.add("(1-8)          : Set Main Attack");
        controlList.add("- + (1-8)      : Set Alt Attack");
        controlList.add("Shift + (1-8)  : Display Attack Info");
        controlList.add("Space          : Main Attack");
        controlList.add("Alt            : Alt Attack");
        controlList.add("Z              : Use\\Equip Item");
        controlList.add("X + (1-8)      : Unequip Item");
        controlList.add("E              : Next Item");
        controlList.add("Q              : Prev Item");
        controlList.add("Tab            : Exit Menus");
        controlList.add("       *Debug*");
        controlList.add("F1             : Load New Map");
        controlList.add("ESC            : Load Shop ");
    }


    public static void exit(){
        gsm.pop();

    }
    public void update(float dt) {
        handleInput();
    }
    public void render(SpriteBatch sb) {
        sb.begin();
        Game.setFontSize(3);
        for(int i=0;i<commandList.size();i++) {
            commandList.get(i).init();
            Game.getFont().draw(sb, commandList.get(i).print(), 30, Game.HEIGHT - (30 * (i + 1)));
        }
        sb.end();
    }
    public void dispose() {

    }
}
