package com.quadx.dungeons.tools.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.GridManager;
import com.quadx.dungeons.tools.timers.Delta;
import com.quadx.dungeons.tools.timers.Oscillator;

import java.util.ArrayList;

import static com.quadx.dungeons.tools.timers.Time.SECOND;
import static com.quadx.dungeons.tools.timers.Time.ft;

/**
 * Created by Chris Cavazos on 5/28/2016.
 */
public class HoverText {
    private static ArrayList<HoverText> textQueue = new ArrayList<>();
    private boolean active;
    private boolean flash;
    private float ymod;

    private String text;
    private Vector2 pos;
    private Color color;
    private Delta dHover;
    private static BitmapFont font;
    private Oscillator blink = new Oscillator(6 * ft);

    public HoverText(String s, float t, Color c, Vector2 fixed, boolean flash) {
        this.flash = flash;
        active = true;
        text = s;
        dHover = new Delta(t);
        color = new Color(c);
        this.pos = new Vector2(fixed);
        //out("Text:" + fixed.toString());
        textQueue.add(this);
        if(textQueue.size()>10)
            remove(0);
    }
    public HoverText(String s, Color c, Vector2 fixed, boolean b) {
        this(s,1.5f*SECOND,c,fixed,b);
    }


    private static void filterTexts(){
        int size = textQueue.size();
        for (int i = size - 1; i >= 0; i--) {//remove dead
            if (!textQueue.get(i).isActive())
                remove(i);
        }

        while (size > 25)// remove excess
            remove(0);

    }
    private static void remove(int i){
        if(i<textQueue.size())
        textQueue.remove(i);
    }
    public static void render(SpriteBatch sb) {
        Text.setFontSize(2);
        font = Text.getFont();
        try {
            for (HoverText h : textQueue)
                h.renderSelf(sb);
        } catch (Exception ignored) {
        }
    }
    public static void update(float dt) {
        textQueue.forEach(h -> h.updateSelf(dt));
        filterTexts();
    }

    private void updateSelf(float dt) {
        dHover.update(dt);
        pos.add(0, ymod += .08f);
        if (flash)
            blink.update(dt);
        active = !dHover.isDone();
    }
    private void renderSelf(SpriteBatch sb) {
        setColor();

        font.draw(sb, text, pos.x, pos.y);
    }
    private void setColor() {
        float x = dHover.percent();
        float a = (float) (-x * Math.pow(Math.E, (x - 1))) + 1;
        a = GridManager.bound(a, 1);
        Color c;
        c=blink.getVal()? new Color(Color.WHITE) : color;
        c.a = a;
        font.setColor(c);
    }
    private boolean isActive() {
        return active;
    }


}
