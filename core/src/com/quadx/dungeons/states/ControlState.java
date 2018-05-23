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
import com.quadx.dungeons.tools.ShapeRendererExt;
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
    private Title key=new Title("KEY",WIDTH*3/4-10,HEIGHT-10);
    private Title act=new Title("ACTION",30+10,HEIGHT-10);

    private final ArrayList<String> controlList = new ArrayList<>();
    public static Selector selector=null;
    public ControlState(GameStateManager gsm){
        super(gsm);
        selector = new Selector(0,commandList.size()-1,30,new Vector2(30, Game.HEIGHT - (50)-10),new Vector2(WIDTH-60,30), Color.WHITE);

        selector.disableSelection();
        Gdx.input.setInputProcessor(this);
        Command.disableControls=true;

    }

    private void defaultControls(){
        String s2 ="51 47 29 32 19 20 21 22 57 45 33 129 130 62 35 31 60 52 131 61 66 ";
        String[] sp = s2.split(" ");
        for (int i = 0; i < sp.length; i++) {
            commandList.get(i).changeKey(Integer.parseInt(sp[i]));
        }
    }
    private static void exit(){
        selector.disableSelection();
        try {
            PrintWriter pw = new PrintWriter("controls.txt");
            for(Command c: commandList){
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
        Game.setFontSize(2);
        Game.getFont().setColor(Color.WHITE);
        //Draw Titles
        Game.getFont().draw(sb,key.text,view.x+key.x,viewY+ key.y);
        Game.getFont().draw(sb,act.text,view.x+act.x,viewY+ act.y);

        Game.setFontSize(3);
        for(int i=0;i<commandList.size();i++) {
            commandList.get(i).init();
            Game.getFont().draw(sb, commandList.get(i).print(), 30, Game.HEIGHT - (30 * (i + 1))-10);
        }
        String msg="ESC:EXIT    HOME:DEFAULT";
        Game.getFont().draw(sb,msg,(WIDTH/2)- (Text.strWidth(msg)/2),20);
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
            commandList.get(selector.getPos()).changeKey(key);
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
