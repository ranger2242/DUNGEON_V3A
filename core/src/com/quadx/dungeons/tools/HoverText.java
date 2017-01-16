package com.quadx.dungeons.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.quadx.dungeons.Game;

import java.util.ArrayList;

import static com.quadx.dungeons.states.MainMenuState.gl;

/**
 * Created by Chris Cavazos on 5/28/2016.
 */
public class HoverText {
    public static ArrayList<HoverText> texts = new ArrayList<>();
    private Color color;
    private final String text;
    private boolean active=false;
    private final int x;
    private final int y;
    private int ymod;
    private float dtHov;
    private float dtFlash;
    protected float alpha=1;
    private final boolean flash;
    private boolean cycle;
    private float time;
    BitmapFont font;

    public HoverText(String s, Color c, int x1, int y1, float t, boolean flash){
        Game.setFontSize(2);
        font= Game.getFont();
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
    public void updateDT() {
        dtHov += Gdx.graphics.getDeltaTime();
        if (flash) dtFlash += Gdx.graphics.getDeltaTime();
        //delete inactive hoverText
        boolean[] index;
        if (!texts.isEmpty()) {
            index = new boolean[texts.size()];
            texts.stream().filter(h -> !h.isActive()).forEach(h -> index[texts.indexOf(h)] = true);
            for (int i = texts.size() - 1; i >= 0; i--) {
                try {
                    if (index[i]) {
                        texts.remove(i);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
            while (HoverText.texts.size() > 10) HoverText.texts.remove(0);
        }
    }
    public void draw(SpriteBatch sb) {
        Game.setFontSize(1);
        if (active) {
           float time = 1.2f;
            if (dtHov < time) {
                Game.setFontSize(3);
                CharSequence cs = text;
                gl.setText(Game.getFont(), cs);
                alpha= (float) -(Math.pow(Math.E,5*((dtHov/time)-1f)+1));

                // color.a=alpha;
                if (dtFlash > .1) {
                    if (cycle) {
                        font.setColor(Color.WHITE);
                    } else font.setColor(color);
                    cycle = !cycle;
                    dtFlash = 0;
                }
                //if(alpha<.3)alpha=.3f;
                //color.a=alpha;
                font.setColor(color);
                font.getColor().a=alpha;
                int px = (int) (x - (gl.width / 2));
                int py = y + ymod;
                sb.begin();
                font.draw(sb, text, px, py);
                font.getColor().a=1;

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
