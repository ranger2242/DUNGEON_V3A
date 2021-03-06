package com.quadx.dungeons.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.tools.gui.Text;

import java.util.ArrayList;

import static com.quadx.dungeons.Game.scr;


/**
 * Created by Tom on 12/30/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class OptionState extends State{
    private static boolean lockAim=false;
    private ShapeRenderer sr = new ShapeRenderer();
    private ArrayList<String> options = new ArrayList<>();
    private ArrayList<Vector2> resolutions= new ArrayList<>();
    private int respos=0;

    public OptionState(GameStateManager gsm) {
        super(gsm);
        resolutions.add(new Vector2(800,600));
        resolutions.add(new Vector2(1366,760));
        cam.position.set(0,0,0);
 /*       viewX=cam.position.x;
        MapState.viewY=cam.position.y;*/
        State.setView(new Vector2(cam.position.x,cam.position.y));
        cam.setToOrtho(false);
        addOptions();
    }
    private void addOptions(){
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
                Text.getFont().draw(sb, options.get(i) + " : " + lockAim, view.x + 30, viewY + scr.y - 30);
            }else{
                Text.getFont().draw(sb, options.get(i), view.x + 30, viewY + scr.y - 30);
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
