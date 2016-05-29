package com.quadx.dungeons.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.items.equipment.Equipment;

import static com.quadx.dungeons.states.MainMenuState.gl;

/**
 * Created by Chris Cavazos on 5/28/2016.
 */
public class HoverText {
    protected Color color;
    protected String text;
    protected boolean active=false;
    protected int x;
    protected int y;
    protected int ymod;
    protected float time=1.2f;
    protected float dtHov;
    protected float dtFlash;
    protected boolean flash;
    protected boolean cycle;
    public static final float bufferTime= .1f;
    public HoverText(String s, Color c, int x1, int y1, float t, boolean flash){
        active=true;
        text=s;
        color=c;
        x=x1;
        y=y1;
        this.flash=flash;
        if(flash){
            dtFlash=0;
            cycle=true;
        }
    }
    public void updateDT(){
        dtHov+= Gdx.graphics.getDeltaTime();
        if(flash)dtFlash+=Gdx.graphics.getDeltaTime();
    }
    public void draw(SpriteBatch sb){
        if(active) {
            if (dtHov < time) {
                Game.setFontSize(14);
                CharSequence cs = text;
                gl.setText(Game.getFont(), cs);
                Game.getFont().setColor(color);
                if(dtFlash>.1){
                    if(cycle){
                        Game.getFont().setColor(Color.WHITE);
                    }
                    else Game.getFont().setColor(color);
                    cycle=!cycle;
                    dtFlash=0;
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
}
