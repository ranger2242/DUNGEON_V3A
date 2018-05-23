package com.quadx.dungeons.tools.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.Game;

import java.util.ArrayList;

import static com.quadx.dungeons.GridManager.fixHeight;
import static com.quadx.dungeons.GridManager.setInBounds;
import static com.quadx.dungeons.states.MainMenuState.gl;

/**
 * Created by Chris Cavazos on 5/28/2016.
 */
public class HoverText {
    public static ArrayList<HoverText> texts = new ArrayList<>();
    private Color color;
    private final String text;
    private boolean active;
    private final int x;
    private final int y;
    private int px=0;
    private int py=0;
    private int ymod;
    private float dtHov;
    private float dtFlash;
    protected float alpha=1;
    private final boolean flash;
    private boolean cycle;
    private float time;
    BitmapFont font;

    public HoverText(String s, float t, Color c, float x1, float y1, boolean flash){
        Game.setFontSize(2);
        font= Game.getFont();
        active=true;
        time=t;
        text=s;
        color=c;
        x= Math.round(x1);
        y=Math.round(y1);
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
                i = setInBounds(i, index.length);
                if (index[i]) {
                    texts.remove(i);
                }
            }
            while (HoverText.texts.size() > 10) HoverText.texts.remove(0);
            Game.setFontSize(1);
            if (active) {
                float time = 1.2f;
                if (dtHov < time) {
                    Game.setFontSize(3);
                    CharSequence cs = text;
                    gl.setText(Game.getFont(), cs);
                    alpha= (float) -(Math.pow(Math.E,5*((dtHov/time)-1f)+1));
                    if (dtFlash > .1) {
                        if (cycle) {
                            font.setColor(Color.WHITE);
                        } else font.setColor(color);
                        cycle = !cycle;
                        dtFlash = 0;
                    }
                    font.setColor(color);
                    font.getColor().a=alpha;
                    px = (int) (x - (gl.width / 2));
                    py = y + ymod;

                    ymod++;
                } else {
                    dtHov = 0;
                    ymod = 0;
                    active = false;
                }

            }
        }
    }
    public void draw(SpriteBatch sb) {
        if (active) {
            if (dtHov < 1.2) {
                font.setColor(color);
                font.draw(sb, text, px,fixHeight(new Vector2(px,py)));
            }
        }
//        font.getColor().a=1;


    }
    public boolean isActive() {
        return active;
    }

    public float getTime() {
        return time;
    }
}
