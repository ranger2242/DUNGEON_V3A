package com.quadx.dungeons.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.Xbox360Pad;
import com.quadx.dungeons.tools.Score;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import static com.quadx.dungeons.states.mapstate.MapState.viewX;
import static com.quadx.dungeons.states.mapstate.MapState.viewY;

/**
 * Created by Chris Cavazos on 5/29/2016.
 */
public class HighScoreState extends State {
    public static  ArrayList<Score> scores= new ArrayList<>();
    Score[] highscores=new Score[10];

    public HighScoreState(GameStateManager gsm) {
        super(gsm);

        for(int j=0;j<10;j++) {
            int pos = 0;
            int high = 0;
            for (int i = 0; i < scores.size(); i++) {
                try {
                    int score = (int) Double.parseDouble(scores.get(i).getScore());
                    if (score > high) {
                        high = score;
                        pos = i;
                    }
                }catch (NullPointerException e){

                }
            }
            try {
                highscores[j] = scores.get(pos);
                scores.remove(pos);
            }catch (Exception e){}
        }
        //scores.clear();
        for(int i=0;i<10;i++){
            scores.add(highscores[i]);
        }
        try {
            PrintWriter pw = new PrintWriter("scores.txt");
            for(Score s: highscores){
            try {
                pw.println(s.getSaveFormat());
            }
            catch (NullPointerException e){

            }
            }
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void handleInput() {
        if(Game.controllerMode){
            if(MainMenuState.controller.getButton(Xbox360Pad.BUTTON_B)){
                gsm.push(new MainMenuState(gsm));
            }
        }
        else {
            if (Gdx.input.isKeyPressed(Input.Keys.TAB)) {
                gsm.push(new MainMenuState(gsm));
            }
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl.glClearColor(1,0,0,1);

        sb.begin();
        Game.setFontSize(3);
        sb.setColor(Color.WHITE);
        Game.getFont().draw(sb,"HIGHSCORES",viewX+ Game.WIDTH/2,viewY+ Game.HEIGHT-30);
        int count =10;
        for(int i=9;i>=0;i--){
            try {
                Game.getFont().draw(sb,(i+1)+": "+ highscores[i].toString(), viewX + 30, viewY + Game.HEIGHT - 60 - (i * 60));
                count++;
            }catch (NullPointerException e){}

        }
        sb.end();
    }
    public static void addScore(Score s){
        scores.add(s);
    }
    @Override
    public void dispose() {

    }
}
