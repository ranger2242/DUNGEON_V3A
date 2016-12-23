package com.quadx.dungeons.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.quadx.dungeons.commands.Command;

import static com.quadx.dungeons.Game.commandList;

/**
 * Created by Brent on 6/26/2015.
 */
@SuppressWarnings("ALL")
public abstract class State {
    protected static OrthographicCamera cam;
    private Vector3 mouse;
    protected static GameStateManager gsm;

    protected State(GameStateManager gsm){
        State.gsm = gsm;
        cam = new OrthographicCamera();
        cam.setToOrtho(true);
        mouse = new Vector3();
    }
    void handleInput(){
        for(Command c: commandList){
            c.execute();
        }
    }
    public abstract void update(float dt);
    public abstract void render(SpriteBatch sb);
    public abstract void dispose();
}
