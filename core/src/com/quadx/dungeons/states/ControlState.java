package com.quadx.dungeons.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.commands.Command;
import com.quadx.dungeons.shapes1_5.ShapeRendererExt;
import com.quadx.dungeons.tools.gui.Selector;
import com.quadx.dungeons.tools.gui.Text;
import com.quadx.dungeons.tools.gui.Title;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import static com.quadx.dungeons.Game.*;
import static com.quadx.dungeons.tools.gui.HUD.titleLine;

/**
 * Created by Tom on 12/30/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class ControlState extends State implements InputProcessor {
    private float dtInputBuffer=0;
    private ShapeRendererExt sr= new ShapeRendererExt();
    private Title key=new Title("KEY",scr.x*3/4-10,scr.y-10);
    private Title act=new Title("ACTION",30+10,scr.y-10);

    private final ArrayList<String> controlList = new ArrayList<>();
    public static Selector selector=null;
    public ControlState(GameStateManager gsm){
        super(gsm);
        selector = new Selector(0, Command.commands.size()-1,30,new Vector2(30, scr.y - (50)-10),new Vector2(scr.x-60,30), Color.WHITE);

        selector.disableSelection();
        Gdx.input.setInputProcessor(this);
        Command.disableControls=true;

    }

    private void defaultControls(){
        String s2 ="51 47 29 32 19 20 21 22 57 45 33 129 130 62 35 31 60 52 131 61 66 ";
        String[] sp = s2.split(" ");
        for (int i = 0; i < sp.length; i++) {
            Command.commands.get(i).changeKey(Integer.parseInt(sp[i]));
        }
    }
    private static void exit(){
        selector.disableSelection();
        try {
            PrintWriter pw = new PrintWriter("controls.txt");
            for(Command c: Command.commands){
                try {
                    pw.print(c.getButtonK()+" ");
                }
                catch (NullPointerException e){}
            }
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        gsm.pop();
    }

    public void update(float dt) {
        dtInputBuffer+=dt;
        if(dtInputBuffer>=.2f) {
            Command.disableControls=false;
            handleInput();
            if(Gdx.input.isKeyPressed(Input.Keys.HOME)){
                defaultControls();
            }
            if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)){
                exit();
            }
            dtInputBuffer=.2f;
        }
        selector.update(dt);
    }
    public void render(SpriteBatch sb) {
        sb.begin();
        Text.setFontSize(2);
        Text.getFont().setColor(Color.WHITE);
        //Draw Titles
        Text.getFont().draw(sb,key.text,view.x+key.x,viewY+ key.y);
        Text.getFont().draw(sb,act.text,view.x+act.x,viewY+ act.y);

        Text.setFontSize(3);
        for(int i = 0; i< Command.commands.size(); i++) {
            Command.commands.get(i).init();
            Text.getFont().draw(sb, Command.commands.get(i).print(), 30, scr.y- (30 * (i + 1))-10);
        }
        String msg="ESC:EXIT    HOME:DEFAULT";
        Text.getFont().draw(sb,msg,(scr.x/2)- (Text.strWidth(msg)/2),20);
        sb.end();
        selector.render(sr);
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.RED);

        titleLine(sr, key);
        titleLine(sr, act);

        sr.end();
    }

    //---------------------------------------------------------
// Render


    public void dispose() {
    }
    private void changeKey(int key){
        if(selector.isActive()){
            Command.commands.get(selector.getPos()).changeKey(key);
            Game.console(key+"");
            selector.disableSelection();
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        Game.console(keycode+"");
        if(keycode== Input.Keys.ESCAPE){
            ControlState.selector.disableSelection();
        }else {
            changeKey(keycode);
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        Game.console(character+"");

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
