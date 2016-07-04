package com.quadx.dungeons.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.quadx.dungeons.Game;

import java.util.ArrayList;

import static com.quadx.dungeons.states.MainMenuState.gl;

/**
 * Created by Chris Cavazos on 5/28/2016.
 */
public class HoverText {
    public static ArrayList<HoverText> texts = new ArrayList<>();
    private final Color color;
    private final String text;
    private boolean active=false;
    private final int x;
    private final int y;
    private int ymod;
    private float dtHov;
    private float dtFlash;
    private final boolean flash;
    private boolean cycle;
    private float time;
    public HoverText(String s, Color c, int x1, int y1, float t, boolean flash){
        active=true;
        time=t;
        text=s;
        color=c;
        x=x1;
        y=y1;
        this.flash=flash;
        if(flash){
            dtFlash=0;
            cycle=true;
        }
        texts.add(this);
    }
    public void updateDT(){
        dtHov+= Gdx.graphics.getDeltaTime();
        if(flash)dtFlash+=Gdx.graphics.getDeltaTime();
    }
    public void draw(SpriteBatch sb) {
        if (active) {
            float time = 1.2f;
            if (dtHov < time) {
                Game.setFontSize(3);
                CharSequence cs = text;
                gl.setText(Game.getFont(), cs);
                Game.getFont().setColor(color);
                if (dtFlash > .1) {
                    if (cycle) {
                        Game.getFont().setColor(Color.WHITE);
                    } else Game.getFont().setColor(color);
                    cycle = !cycle;
                    dtFlash = 0;
                }
                int px = (int) (x - (gl.width / 2));
                int py = y + ymod;
                sb.begin();
                Game.getFont().draw(sb, text, px, py);
                sb.end();
                ymod++;
            } else {
                dtHov = 0;
                ymod = 0;
                active = false;
            }
        }
    }
    public boolean isActive() {
        return active;
    }

    public float getTime() {
        return time;
    }
}
