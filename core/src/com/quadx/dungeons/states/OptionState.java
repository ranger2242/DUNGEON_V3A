package com.quadx.dungeons.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.states.mapstate.MapState;

import java.util.ArrayList;

import static com.quadx.dungeons.Game.HEIGHT;
import static com.quadx.dungeons.states.mapstate.MapState.viewX;
import static com.quadx.dungeons.states.mapstate.MapState.viewY;

/**
 * Created by Tom on 12/30/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class OptionState extends State{
    public static boolean lockAim=false;
    ShapeRenderer sr = new ShapeRenderer();
    ArrayList<String> options = new ArrayList<>();
    ArrayList<Vector2> resolutions= new ArrayList<>();
    int respos=0;

    public OptionState(GameStateManager gsm) {
        super(gsm);
        resolutions.add(new Vector2(800,600));
        resolutions.add(new Vector2(1366,760));
        cam.position.set(0,0,0);
        viewX=cam.position.x;
        MapState.viewY=cam.position.y;
        cam.setToOrtho(false);
        addOptions();
    }
    void addOptions(){
        options.add("RESOLUTION");
        options.add("Exit");
    }

    public void update(float dt) {
        handleInput();
        if(Gdx.input.isButtonPressed(Input.Keys.NUM_1)){
            if( respos<resolutions.size()-1){
                respos++;
            }else{
                respos=0;
            }
            Gdx.graphics.setWindowedMode((int)resolutions.get(respos).x,(int)resolutions.get(respos).y);

        }
    }
    public void render(SpriteBatch sb) {

        Gdx.gl.glClearColor(1,0,0,1);
        sr.setProjectionMatrix(cam.combined);
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        for(int i=0;i<options.size();i++){
            if(i!=options.size()-1) {
                Game.getFont().draw(sb, options.get(i) + " : " + lockAim, viewX + 30, viewY + HEIGHT - 30);
            }else{
                Game.getFont().draw(sb, options.get(i), viewX + 30, viewY + HEIGHT - 30);
            }

        }
        sb.end();
    }

    public void dispose() {

    }

    public static void exit() {
        gsm.pop();
    }
}
