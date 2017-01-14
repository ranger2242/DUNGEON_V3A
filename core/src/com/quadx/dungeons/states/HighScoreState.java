package com.quadx.dungeons.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.Player;
import com.quadx.dungeons.tools.Score;
import com.quadx.dungeons.tools.StatManager;
import com.quadx.dungeons.tools.gui.Title;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import static com.quadx.dungeons.Game.HEIGHT;
import static com.quadx.dungeons.Game.WIDTH;
import static com.quadx.dungeons.states.mapstate.MapState.viewX;
import static com.quadx.dungeons.states.mapstate.MapState.viewY;
import static com.quadx.dungeons.tools.StatManager.pScore;
import static com.quadx.dungeons.tools.StatManager.stats;
import static com.quadx.dungeons.tools.Tests.centerString;
import static com.quadx.dungeons.tools.Tests.strWidth;

/**
 * Created by Chris Cavazos on 5/29/2016.
 */
public class HighScoreState extends State {
    public static Player pfinal= new Player();
    public static Score fin=new Score();
    public static final ArrayList<Score> scores= new ArrayList<>();
    ShapeRenderer sr=new ShapeRenderer();
    private final Score[] highscores=new Score[10];
    ArrayList<Double> list;
    boolean blink=true;
    float dtBlink=0;
    static boolean drawOther=false;
    Title hscore;
    Title roundStat;
    Title killby;
    Title plStat;
    Title equip;

    public HighScoreState(GameStateManager gsm) {
        super(gsm);
        hscore=new Title("HIGHSCORES",30,HEIGHT-44);
        roundStat=new Title("ROUND STATS",30,HEIGHT/2);
        killby=new Title("KILLED BY",WIDTH/4,HEIGHT/2);
        plStat=new Title("PLAYER STATS",(WIDTH/2)-100,HEIGHT/2);
        equip=new Title("EQUIPMENT",(WIDTH/3)*2,HEIGHT/2);

        list= StatManager.getFinalStats();
        cam.setToOrtho(false);
        cam.position.set(0,0,0);
        viewX=cam.position.x;
        viewY=cam.position.y;
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
    public static void exit(){
        pfinal=null;
        drawOther=false;
        gsm.clear();
        gsm.push(new MainMenuState(gsm));
    }

    public void update(float dt) {
        handleInput();
        dtBlink+=dt;
        if(dtBlink>.5){
            blink=!blink;
            dtBlink=0;
        }
    }
    float[] fitLineToWord(String s){
        //x1,y1,x2,y2
        float[] arr=new float[8];
        arr[0]= -10;
        arr[1]=-15;
        arr[2]=strWidth(s)+20;
        arr[3]=-15;
        arr[4]= -10;
        arr[5]=-12;
        arr[6]=strWidth(s)+35;
        arr[7]=-12;
        return arr;
    }
    public static void addScore(Score s){
        scores.add(s);
    }
//---------------------------------------------------------
// Render
    void titleLine(Title t){
        float[] s = fitLineToWord(t.text);
        float x=t.x;
        float y=t.y;
        sr.line(viewX + x + s[0], viewY +y+ s[1] , viewX + x + s[2], viewY + y+ s[3]);
        sr.line(viewX + x + s[4], viewY +y+ s[5] , viewX + x + s[6], viewY + y+ s[7]);
    }
    void drawLines(){
        float w= WIDTH;
        float hw=w/2;
        float qw=w/4;
        float h=HEIGHT;
        float hh=h/2;
        float qh=h/4;
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.GRAY);
        sr.line(viewX + 40, viewY + hh + 20, viewX + w - 40, viewY + hh + 20);
        sr.line(viewX + qw - 10, viewY + hh - 30, viewX + qw - 10, viewY + 30);
        sr.end();
    }
    void drawTitles(SpriteBatch sb){
        sb.begin();
        Game.setFontSize(2);
        Game.getFont().setColor(Color.WHITE);
        //Draw Titles
        Game.getFont().draw(sb,hscore.text,viewX+hscore.x,viewY+ hscore.y);
        Game.getFont().draw(sb,roundStat.text,viewX+roundStat.x,viewY+ roundStat.y);
        if(drawOther){
            Game.getFont().draw(sb,killby.text,killby.x,killby.y);
            Game.getFont().draw(sb,plStat.text,viewX+plStat.x,viewY+plStat.y);
            Game.getFont().draw(sb,equip.text,viewX+equip.x,viewY+equip.y);
        }
        sb.end();
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.RED);

        titleLine(hscore);
        titleLine(roundStat);
        if(drawOther){
            titleLine(killby);
            titleLine(plStat);
            titleLine(equip);
        }
        sr.end();
    }
    public void render(SpriteBatch sb) {
        Gdx.gl.glClearColor(1,0,0,1);
        sr.setProjectionMatrix(cam.combined);
        sb.setProjectionMatrix(cam.combined);
        drawLines();
        float killerx=viewX+(WIDTH/4);
        float killery = viewY + (HEIGHT / 2);
        drawTitles(sb);
        sb.begin();
        boolean in = false;
        for (int i = 9; i >= 0; i--) {
            if (highscores[i] != null) {
                try {
                    if (pScore.toString().equals(highscores[i].toString())) {
                        in = true;
                        if (blink) {

                            Game.getFont().setColor(Color.BLUE);
                        } else Game.getFont().setColor(Color.WHITE);
                    } else {
                        Game.getFont().setColor(Color.WHITE);
                    }
                } catch (NullPointerException e) {
                    Game.getFont().setColor(Color.WHITE);
                }
                String s="";
                if(i==9){
                    s = (i + 1) + ": " + highscores[i].toString();
                }
                else{
                    s = (i + 1) + ":  " + highscores[i].toString();

                }
                Game.getFont().draw(sb, s, viewX + 60, viewY + HEIGHT - 80 - (i * 24));
            }
        }

        try {
            if (!in) {
                Game.getFont().setColor(Color.RED);
                Game.getFont().draw(sb, "XX: " + pScore.toString(), viewX + 60, viewY + HEIGHT - 80 - (10 * 24));
                Game.getFont().setColor(Color.WHITE);
            }
        } catch (NullPointerException e) {
            Game.getFont().setColor(Color.WHITE);
        }

        for(int i=0;i< stats.size();i++){
            Game.getFont().draw(sb,stats.get(i)+list.get(i).toString(),viewX+30,viewY+(HEIGHT/2)-30-((i+1)*24));
        }
        try {

            StatManager.killer.setFront(2);
            ArrayList<String> list = StatManager.killer.getStatsList();
            drawOther=true;

            sb.draw(StatManager.killer.getIcon(),killerx,viewY+100);
            for(int i=0;i<list.size();i++){
                Game.getFont().draw(sb,list.get(i),killerx,killery-30-((i+1)*20));
            }
            for(int i=0;i<pfinal.getStatsList().size();i++){
                try{
                    Game.getFont().draw(sb,pfinal.getStatsList().get(i),viewX+(WIDTH/2)-100,viewY+(HEIGHT/2)-20-((i+1)*20));
                }catch (Exception e){}

            }
            for(int i=0;i<pfinal.equipedList.size();i++){
                try{
                    Game.getFont().draw(sb,pfinal.equipedList.get(i).getName(),viewX+((WIDTH/3)*2)+50,viewY+(HEIGHT/2)-35-((i+1)*30));
                    sb.draw(pfinal.equipedList.get(i).getIcon(),viewX+((WIDTH/3)*2),viewY+(HEIGHT/2)-55-((i+1)*30));
                }catch (Exception e){}

            }
        }catch (Exception e){}
        if(blink) {
            Game.getFont().setColor(Color.RED);
            Game.getFont().draw(sb, "TAB : EXIT", centerString("TAB : EXIT"), viewY + 30);

        }
        sb.end();
    }
//----------------------------------------------------
    public void dispose() {

    }
}
