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
import java.util.Arrays;

import static com.quadx.dungeons.states.mapstate.MapState.viewX;
import static com.quadx.dungeons.states.mapstate.MapState.viewY;

/**
 * Created by Chris Cavazos on 5/29/2016.
 */
public class HighScoreState extends State {
    public static final ArrayList<Score> scores= new ArrayList<>();
    private final Score[] highscores=new Score[10];

    public HighScoreState(GameStateManager gsm) {
        super(gsm);
        /*
         stats to check

         ----overall
         total gold
         total kills
         total items picked up
         total shots fired
         total misses
         overall accuracy
         longest play time
         shortest play time
         highest floor
         highest player level
         most used class
         total games played
         total play time

         most used attack


         ----per round
         shots fired
         shots missed
         accuracy
         items used/ picked up
         avg kills per min
         avg gold per min
         killed by --
         most used attack


         */

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
                }catch(NullPointerException e){}
            }

            try {
                highscores[j] = scores.get(pos);
                scores.remove(pos);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        scores.addAll(Arrays.asList(highscores).subList(0, 10));
        try {
            PrintWriter pw = new PrintWriter("scores.txt");
            for(Score s: highscores){
                try {pw.println(s.getSaveFormat());}
                catch (NullPointerException e){}
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
        Game.setFontSize(1);
        Game.getFont().setColor(Color.WHITE);
        Game.getFont().draw(sb,"HIGHSCORES",viewX+ Game.WIDTH/2,viewY+ Game.HEIGHT-30);
        for(int i=9;i>=0;i--) {
            if(i+1!= 10) {
                if (highscores[i] != null) {
                    Game.getFont().draw(sb, (i + 1) + ":  " + highscores[i].toString(), viewX + 30, viewY + Game.HEIGHT - 60 - (i * 14));
                }
            }
            else
            if (highscores[i] != null) {
                Game.getFont().draw(sb, (i + 1) + ": " + highscores[i].toString(), viewX + 30, viewY + Game.HEIGHT - 60 - (i * 14));
            }
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
