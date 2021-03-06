package com.quadx.dungeons.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.quadx.dungeons.commands.Command;
import com.quadx.dungeons.tools.gui.CamController;

import static com.quadx.dungeons.commands.Command.commands;
import static com.quadx.dungeons.Game.scr;

/**
 * Created by Brent on 6/26/2015.
 */
@SuppressWarnings("ALL")
public abstract class State {
    public static OrthographicCamera cam;
    private Vector3 mouse;
    public static GameStateManager gsm;
    public static float viewX;
    public static float viewY;
    protected static Vector2 view = new Vector2();
    public static CamController camController = new CamController();


    protected State(GameStateManager gsm){
        State.gsm = gsm;
        cam = new OrthographicCamera();
        cam.setToOrtho(true);
        mouse = new Vector3();
    }

    public static Vector2 getView() {
        return view;
    }

    void handleInput(){
        for(Command c: commands){
            c.execute();
        }
    }
    public static void setView(Vector2 v){
        view.set(v);
        viewX=v.x;
        viewY=v.y;
    }
    public abstract void update(float dt);
    public abstract void render(SpriteBatch sb);
    public abstract void dispose();
    public static float scrVx(float percent){
        return view.x+(scr.x*percent);
    }
    public static float scrVy(float percent){
        return view.y+(scr.y*percent);
    }
    public static Vector2 scrV(float px, float py){
        return new Vector2(scrVx(px),scrVy(py));
    }

    public static void shake() {
        camController.shakeScreen(20f, 5);
    }
}
