package com.quadx.dungeons.tools.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.commands.Command;

import static com.quadx.dungeons.Game.commandList;

/**
 * Created by Chris Cavazos on 5/23/2018.
 */
public abstract class ButtonHandler {
    static DebugKeys db=new DebugKeys();

    Vector2 mpos=new Vector2();

    boolean prsd(int i){
        return Gdx.input.isKeyPressed(i);
    }

  public abstract void update(float dt);

    void runCommands(){
        for (Command c : commandList) {
            c.execute();
        }
    }

}
