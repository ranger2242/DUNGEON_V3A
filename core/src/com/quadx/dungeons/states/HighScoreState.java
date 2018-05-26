package com.quadx.dungeons.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.Player;
import com.quadx.dungeons.items.equipment.Equipment;
import com.quadx.dungeons.shapes1_5.ShapeRendererExt;
import com.quadx.dungeons.tools.Score;
import com.quadx.dungeons.tools.StatManager;
import com.quadx.dungeons.tools.gui.Title;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

import static com.quadx.dungeons.Game.HEIGHT;
import static com.quadx.dungeons.Game.WIDTH;
import static com.quadx.dungeons.tools.StatManager.pScore;
import static com.quadx.dungeons.tools.StatManager.stats;
import static com.quadx.dungeons.tools.gui.HUD.titleLine;
import static com.quadx.dungeons.tools.gui.Text.centerString;

/**
 * Created by Chris Cavazos on 5/29/2016.
 */
public class HighScoreState extends State {
    public static Player pfinal= new Player();
    public static Score fin=new Score();
    public static final ArrayList<Score> scores= new ArrayList<>();
    private ShapeRendererExt sr=new ShapeRendererExt();
    private final Score[] highscores=new Score[10];
    private ArrayList<Double> finalStatList;
    private boolean blink=true;
    private float dtBlink=0;
    private static boolean drawPlayerStats =false;
    private Title hscore;
    private Title roundStat;
    private Title killby;
    private Title plStat;
    private Title equip;

    public HighScoreState(GameStateManager gsm) {
        super(gsm);
        hscore=new Title("HIGHSCORES",30,HEIGHT-44);
        roundStat=new Title("ROUND STATS",30,HEIGHT/2);
        killby=new Title("KILLED BY",WIDTH/4,HEIGHT/2);
        plStat=new Title("PLAYER STATS",(WIDTH/2)-100,HEIGHT/2);
        equip=new Title("EQUIPMENT",(WIDTH/3)*2,HEIGHT/2);

        finalStatList = StatManager.getFinalStats();
        cam.setToOrtho(false);
        cam.position.set(0,0,0);
    /*    viewX=cam.position.x;
        viewY=cam.position.y;*/
        State.setView(new Vector2(cam.position.x,cam.position.y));
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
                }catch(NullPointerException ignored){}
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
                catch (NullPointerException ignored){}
            }
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void exit(){
        pfinal=null;
        drawPlayerStats =false;
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
    public static void addScore(Score s){
        scores.add(s);
    }

//---------------------------------------------------------
// Render

    private void drawLines(){
        float w= WIDTH;
        float hw=w/2;
        float qw=w/4;
        float h=HEIGHT;
        float hh=h/2;
        float qh=h/4;
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.GRAY);
        sr.line(view.x + 40, viewY + hh + 20, view.x + w - 40, viewY + hh + 20);
        sr.line(view.x + qw - 10, viewY + hh - 30, view.x + qw - 10, viewY + 30);
        sr.end();
    }
    private void drawTitles(SpriteBatch sb){
        sb.begin();
        Game.setFontSize(2);
        Game.getFont().setColor(Color.WHITE);
        //Draw Titles
        Game.getFont().draw(sb,hscore.text,view.x+hscore.x,viewY+ hscore.y);
        Game.getFont().draw(sb,roundStat.text,view.x+roundStat.x,viewY+ roundStat.y);
        if(drawPlayerStats){
            Game.getFont().draw(sb,killby.text,killby.x,killby.y);
            Game.getFont().draw(sb,plStat.text,view.x+plStat.x,viewY+plStat.y);
            Game.getFont().draw(sb,equip.text,view.x+equip.x,viewY+equip.y);
        }
        sb.end();
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.RED);

        titleLine(sr, hscore);
        titleLine(sr, roundStat);
        if(drawPlayerStats){
            titleLine(sr, killby);
            titleLine(sr, plStat);
            titleLine(sr, equip);
        }
        sr.end();
    }

    public void render(SpriteBatch sb) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        sr.setProjectionMatrix(cam.combined);
        sb.setProjectionMatrix(cam.combined);
        drawLines();
        float killerx = view.x + (WIDTH / 4);
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
                String s;
                if (i == 9) {
                    s = (i + 1) + ": " + highscores[i].toString();
                } else {
                    s = (i + 1) + ":  " + highscores[i].toString();

                }
                Game.getFont().draw(sb, s, view.x + 60, viewY + HEIGHT - 80 - (i * 24));
            }
        }

        try {
            if (!in) {
                Game.getFont().setColor(Color.RED);
                Game.getFont().draw(sb, "XX: " + pScore.toString(), view.x + 60, viewY + HEIGHT - 80 - (10 * 24));
                Game.getFont().setColor(Color.WHITE);
            }
        } catch (NullPointerException e) {
            Game.getFont().setColor(Color.WHITE);
        }

        for (int i = 0; i < stats.size(); i++) {
            Game.getFont().draw(sb, stats.get(i) + finalStatList.get(i).toString(), view.x + 30, viewY + (HEIGHT / 2) - 30 - ((i + 1) * 24));
        }
        try {

            StatManager.killer.setFront(2);
            ArrayList<String> list = StatManager.killer.getStatsList();
            drawPlayerStats = true;

            sb.draw(StatManager.killer.getIcon(), killerx, viewY + 100);
            for (int i = 0; i < list.size(); i++) {
                Game.getFont().draw(sb, list.get(i), killerx, killery - 30 - ((i + 1) * 20));
            }
            pfinal.renderStatList(sb, new Vector2(view.x + (WIDTH / 2) - 100, viewY + (HEIGHT / 2) - 20));
           /* for(int i=0;i<pfinal.getStatsList().size();i++){
                try{

                    Game.getFont().draw(sb,pfinal.getStatsList().get(i),
                            viewX+(WIDTH/2)-100,viewY+(HEIGHT/2)-20-((i+1)*20));
                }catch (Exception e){}

            }*/
            int end = pfinal.inv.getEquiped().size();
            for (int i = 0; i < end; i++) {
                try {
                    Equipment e = pfinal.inv.getEquiped().get(i);
                    float x = view.x + ((WIDTH / 3) * 2);
                    float y = viewY + (HEIGHT / 2) - 35 - ((i + 1) * 30);
                    Game.getFont().draw(sb, e.getName(), x + 50, y);
                    sb.draw(e.getIcon(), x, y);
                } catch (Exception ignored) {
                }

            }
        } catch (Exception ignored) {
        }
        if (blink) {
            Game.getFont().setColor(Color.RED);
            Game.getFont().draw(sb, "TAB : EXIT", centerString("TAB : EXIT"), viewY + 30);

        }
        sb.end();
    }



    //----------------------------------------------------
    public void dispose() {

    }
}