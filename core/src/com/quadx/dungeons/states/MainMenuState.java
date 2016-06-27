package com.quadx.dungeons.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.Xbox360Pad;
import com.quadx.dungeons.states.mapstate.MapState;

import java.util.ArrayList;

import static com.quadx.dungeons.Game.controllerMode;
import static com.quadx.dungeons.states.mapstate.MapState.viewX;
import static com.quadx.dungeons.states.mapstate.MapState.viewY;

/**
 * Created by Tom on 12/22/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class MainMenuState extends State implements ControllerListener {
    ArrayList<String> options=new ArrayList<>();
    public static GlyphLayout gl=new GlyphLayout();
    private static ParticleEffect effect;
    private static int selector=0;

    private int titlePosX =0;
    private int titlePosY=0;
    int selectorPosX;
    private int optionsPosX =0;
    private int optionsPosY =0;
    private float dtCursor = 0;
    public static Controller controller;
    public static String s="";

    public MainMenuState(GameStateManager gsm)
    {
        super(gsm);
        if(Controllers.getControllers().size>0) {
            controller = Controllers.getControllers().first();
            controller.addListener(this);
            controllerMode =true;
        }
        Gdx.gl.glClearColor(0,0,0,1);

        effect = new ParticleEffect();
        ParticleEmitter emitter = new ParticleEmitter();
        String s = "StartScreen";
        effect.load(Gdx.files.internal("particles\\pt" + s), Gdx.files.internal("particles"));
        emitter = effect.findEmitter("StartScreen");
        emitter.setContinuous(true);
        CharSequence cs="DUNGEON";
        gl.setText(Game.getFont(),cs);
        selector=0;
        if(!MapState.inGame)
            options.add("Start");
        else
            options.add("Continue");
        options.add("Options");
        options.add("Controls");
        options.add("Extra");
        options.add("Exit");

    }
    @Override
    protected void handleInput() {
        //controller functions
        if(controllerMode){
            if(controller.getButton(Xbox360Pad.BUTTON_A))
                selectOption();
        }
        //keyboard functions
        if(Gdx.input.isKeyPressed(Input.Keys.ENTER)){
            selectOption();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            selector--;
            if(selector<0)selector=4;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            selector++;
            if(selector>4)selector=0;
        }
    }
    void selectOption(){
        switch (selector){
            case(0):{
                if(MapState.inGame)
                    gsm.pop();
                else
                    gsm.push(new AbilitySelectState(gsm));
                dispose();
                break;
            }
            case(1):{
                gsm.push(new OptionState(gsm));
                break;
            }case(2):{
                gsm.push(new ControlState(gsm));
                break;
            }case(3):{
                gsm.push(new HighScoreState(gsm));
                break;
            }case(4):{
                System.exit(0);
                break;
            }
        }
    }

    @Override
    public void update(float dt) {
        dtCursor+=dt;
        if(dtCursor>.10) {
            dtCursor = 0;
            handleInput();
        }
        effect.update(dt);
        //System.out.println(selector);
        titlePosX = (int)(viewX+(Game.WIDTH/2)-(gl.width/2));
        titlePosY=(int)(viewY+ (Game.HEIGHT/3)*2);
        effect.setPosition(viewX+ Game.WIDTH/2,viewY+ 0);
        selectorPosX = (int) (viewX + (Game.WIDTH / 2) - 100);
        optionsPosX =(int)(viewX+(Game.WIDTH/2));
        optionsPosY =(int)(viewY+ (Game.HEIGHT/3));
    }

    @Override
    public void render(SpriteBatch sb) {
        /*if(MapState.inGame) {
            shapeR.setProjectionMatrix(cam.combined);
            sb.setProjectionMatrix(cam.combined);
        }*/
        drawTitle(sb);
        drawOptions(sb);
        sb.begin();
        Game.getFont().draw(sb,Controllers.getControllers().size+"",viewX+30,viewY+140);

        Game.getFont().draw(sb,s,viewX+30,viewY+100);
        sb.end();
    }
/////////////////////////////////////////////////////////////////////////////////////////
//DRAWING FUNCTIONS
private void drawTitle(SpriteBatch sb){
        sb.begin();
        Game.setFontSize(5);
        Game.font.setColor(Color.WHITE);
        Game.getFont().draw(sb,"DUNGEON", titlePosX,titlePosY);
        effect.draw(sb);
        sb.end();
    }
    private void drawOptions(SpriteBatch sb){

        sb.begin();
        Game.setFontSize(5);
        for(int i=0;i<options.size();i++){
            if(selector==i)
                Game.getFont().setColor(Color.BLUE);
            else
                Game.getFont().setColor(Color.WHITE);
            Game.getFont().draw(sb,options.get(i),optionsPosX,optionsPosY-(i*20));
        }
        sb.end();
    }
/////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void dispose() {
      //  shapeR.dispose();
       effect.dispose();
    }

    @Override
    public void connected(Controller controller) {
       // controllerMode=true;
    }

    @Override
    public void disconnected(Controller controller) {

    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        if(axisCode==Xbox360Pad.AXIS_LEFT_TRIGGER){
            s=value+"";
        }
        return false;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {

        if (value == Xbox360Pad.BUTTON_DPAD_UP) {
            selector--;
            if(selector<0)selector=4;
        }
        if (value == Xbox360Pad.BUTTON_DPAD_DOWN) {
            selector++;
            if(selector>4)selector=0;
        }
        return false;
    }

    @Override
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }

    @Override
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
        return false;
    }
}
