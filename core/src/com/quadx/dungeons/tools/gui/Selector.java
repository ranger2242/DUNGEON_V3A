package com.quadx.dungeons.tools.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.states.ControlState;
import com.quadx.dungeons.tools.ShapeRendererExt;

import static com.quadx.dungeons.Game.frame;

/**
 * Created by Chris Cavazos on 7/10/2017.
 */
public class Selector {
    float dtChange=0;
    boolean active= false;
    int pos=0;
    int max=0;
    int inc =0;
    Vector2 origin = new Vector2(0,0);
    Vector2 dimm= new Vector2(0,0);
    Rectangle shape=new Rectangle();
    Color color = Color.WHITE;

    public boolean isActive() {
        return active;
    }

    public void flipState(Class c){
        if(dtChange>=frame*10) {
            if (c.equals(ControlState.class)) {

                setActive(!isActive());
                if (isActive()) {
                    ControlState.selector.setColor(Color.RED);
                } else {
                    ControlState.selector.setColor(Color.WHITE);
                }
            }
            dtChange=0;
        }
    }

    public void disableSelection(){
        setActive(false);
        setColor(Color.WHITE);
    }

    public void upSelection() {
        if (!isActive())
            setPos(getPos() - 1);
    }

    public void downSelection() {
        if (!isActive())
            setPos(getPos() + 1);
    }
    public void setActive(boolean active) {
        this.active = active;
    }

    public void update(float dt){
        dtChange+=dt;
    }

    void setPosInBounds(){
        if(pos>max){
            pos=0;
        }else if(pos<0){
            pos=max;
        }
    }
    void calculateBox(){
        shape=new Rectangle(origin.x,origin.y-(pos*inc),dimm.x,dimm.y);
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        if(dtChange>frame*6) {
            this.pos = pos;
            setPosInBounds();
            calculateBox();
            dtChange=0;
        }
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public Rectangle getShape() {
        return shape;
    }

    public void setShape(Rectangle shape) {
        this.shape = shape;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void render(ShapeRendererExt sr){
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(color);
        sr.rect(shape);
        sr.end();
    }
    public Selector(int pos, int max,int inc,Vector2 orig,Vector2 dimm, Color color) {

        this.origin=orig;
        this.dimm=dimm;
        this.inc=inc;
        this.pos = pos;
        this.max = max;
        this.color = color;
        this.shape=new Rectangle(orig.x,orig.y,dimm.x,dimm.y);
    }
}
