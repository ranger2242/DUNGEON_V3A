package com.quadx.dungeons.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Brent on 6/26/2015.
 */
@SuppressWarnings("ALL")
public abstract class State {
    protected static OrthographicCamera cam;
    private Vector3 mouse;
    protected static GameStateManager gsm;
   // public static BitmapFont font = Game.font;
    static BitmapFont font = new BitmapFont();

    protected State(GameStateManager gsm){
        State.gsm = gsm;
        cam = new OrthographicCamera();
        cam.setToOrtho(true);
        mouse = new Vector3();
    }

    protected abstract void handleInput();
    public abstract void update(float dt);
    public abstract void render(SpriteBatch sb);
    public abstract void dispose();
}
