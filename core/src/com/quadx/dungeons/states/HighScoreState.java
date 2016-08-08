package com.quadx.dungeons.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.Player;
import com.quadx.dungeons.Xbox360Pad;
import com.quadx.dungeons.tools.Score;
import com.quadx.dungeons.tools.StatManager;
import com.quadx.dungeons.tools.Tests;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import static com.quadx.dungeons.Game.HEIGHT;
import static com.quadx.dungeons.Game.WIDTH;
import static com.quadx.dungeons.states.mapstate.MapState.viewX;
import static com.quadx.dungeons.states.mapstate.MapState.viewY;
import static com.quadx.dungeons.tools.StatManager.stats;

/**
 * Created by Chris Cavazos on 5/29/2016.
 */
public class HighScoreState extends State {
    public static Player pfinal= new Player();
    public static final ArrayList<Score> scores= new ArrayList<>();
    private final Score[] highscores=new Score[10];
    ArrayList<Double> list;
    boolean blink=true;
    float dtBlink=0;
    public HighScoreState(GameStateManager gsm) {
        super(gsm);
         list= StatManager.getFinalStats();

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
        dtBlink+=dt;
        if(dtBlink>.5){
            blink=!blink;
            dtBlink=0;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl.glClearColor(1,0,0,1);

        float killerx=viewX+(WIDTH/4);
        float killery=viewY+(HEIGHT/2);
        sb.begin();
        Game.setFontSize(2);
        Game.getFont().setColor(Color.WHITE);
        Game.getFont().draw(sb,"HIGHSCORES",viewX+ WIDTH/2,viewY+ HEIGHT-30);
        Game.getFont().draw(sb,"ROUND STATS",viewX+30,viewY+ (HEIGHT/2));

        for(int i=9;i>=0;i--) {
            if(i+1!= 10) {
                if (highscores[i] != null) {
                    Game.getFont().draw(sb, (i + 1) + ":  " + highscores[i].toString(), viewX + 30, viewY + HEIGHT - 80 - (i * 24));
                }
            }
            else
            if (highscores[i] != null) {
                Game.getFont().draw(sb, (i + 1) + ": " + highscores[i].toString(), viewX + 30, viewY + HEIGHT - 80 - (i *24));
            }
        }
        for(int i=0;i< stats.size();i++){
            Game.getFont().draw(sb,stats.get(i)+list.get(i).toString(),viewX+30,viewY+(HEIGHT/2)-30-((i+1)*24));
        }
        try {

            StatManager.killer.setFront(2);
            ArrayList<String> list = StatManager.killer.sayStats();
            Game.getFont().draw(sb,"KILLED BY",killerx,killery);


            sb.draw(StatManager.killer.getIcon(),killerx,viewY+100);
        for(int i=0;i<list.size();i++){
            Game.getFont().draw(sb,list.get(i),killerx,killery-30-((i+1)*20));
        }

            Game.getFont().draw(sb,"PLAYER STATS",viewX+(WIDTH/2)-100,viewY+(HEIGHT/2));

            for(int i=0;i<pfinal.getStatsList().size();i++){
                try{
                Game.getFont().draw(sb,pfinal.getStatsList().get(i),viewX+(WIDTH/2)-100,viewY+(HEIGHT/2)-20-((i+1)*20));
                }catch (Exception e){}

            }
            Game.getFont().draw(sb,"EQUIPMENT",viewX+((WIDTH/3)*2),viewY+(HEIGHT/2));

            for(int i=0;i<pfinal.equipedList.size();i++){
                try{
                    Game.getFont().draw(sb,pfinal.equipedList.get(i).getName(),viewX+((WIDTH/3)*2)+50,viewY+(HEIGHT/2)-35-((i+1)*30));
                    sb.draw(pfinal.equipedList.get(i).getIcon(),viewX+((WIDTH/3)*2),viewY+(HEIGHT/2)-55-((i+1)*30));
                }catch (Exception e){}

            }
        }catch (Exception e){}
        if(blink) {
            Game.getFont().setColor(Color.RED);
            Game.getFont().draw(sb, "TAB : EXIT", Tests.centerString("TAB : EXIT"), viewY + 30);

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
