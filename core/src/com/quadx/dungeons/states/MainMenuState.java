package com.quadx.dungeons.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.Xbox360Pad;
import com.quadx.dungeons.commands.Command;
import com.quadx.dungeons.commands.ConfirmComm;
import com.quadx.dungeons.commands.DownComm;
import com.quadx.dungeons.commands.UpComm;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.tools.Tests;

import java.util.ArrayList;

import static com.quadx.dungeons.Game.*;
import static com.quadx.dungeons.states.mapstate.MapState.viewX;
import static com.quadx.dungeons.states.mapstate.MapState.viewY;
import static com.quadx.dungeons.tools.Tests.centerString;

/**
 * Created by Tom on 12/22/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class MainMenuState extends State implements ControllerListener {
    public static final GlyphLayout gl=new GlyphLayout();
    public static Controller controller;
    private final ArrayList<String> options=new ArrayList<>();
    private static ParticleEffect effect;
    private static int selector=0;
    private int titlePosY=0;
    private int optionsPosY =0;
    private float dtCursor = 0;
    private Texture title = new Texture("images\\title.png");
    private ArrayList<Command> commandList=new ArrayList<>();

    public MainMenuState(GameStateManager gsm) {
        super(gsm);
        Gdx.gl.glClearColor(0,0,0,1);
        initController();
        loadParticles();
        addOptionsToList();
        addCommands();
        titlePosY=(int)(viewY+ (Game.HEIGHT/3)*2);
        optionsPosY =(int)(viewY+ (Game.HEIGHT/3));
    }
    void loadParticles(){
        effect = new ParticleEffect();
        ParticleEmitter emitter;
        String s = "StartScreen";
        effect.load(Gdx.files.internal("particles\\pt" + s), Gdx.files.internal("particles"));
        emitter = effect.findEmitter(s);
        emitter.setContinuous(true);
        effect.setPosition(viewX+ Game.WIDTH/2,viewY+ 0);
    }
    void initController(){
        if(Controllers.getControllers().size>0) {
            controller = Controllers.getControllers().first();
            controller.addListener(this);
            controllerMode =true;
        }
    }
    void addOptionsToList(){
        if(!MapState.inGame)
            options.add("Start");
        else
            options.add("Continue");
        options.add("Options");
        options.add("Controls");
        options.add("Extra");
        options.add("Exit");
    }
    void addCommands(){
        commandList.add(new ConfirmComm(MainMenuState.class));
        commandList.add(new UpComm(MainMenuState.class));
        commandList.add(new DownComm(MainMenuState.class));

    }
    @Override
    protected void handleInput() {
        for(Command c: commandList){
            c.execute();
        }
    }
    public static void incrementSelector(){
        selector--;
        if(selector<0)selector=4;
    }
    public static void decrementSelector() {
        selector++;
        if(selector>4)selector=0;
    }
    public static void selectOption(){
        switch (selector){
            case(0):{
                if (Tests.timeKill()) {
                    if (MapState.inGame)
                        gsm.pop();
                    else
                        gsm.push(new AbilitySelectState(gsm));
                }
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
        if(dtCursor>.15) {
            dtCursor = 0;
            handleInput();
        }
        effect.update(dt);
    }
    @Override
    public void dispose() {
        //  shapeR.dispose();
        effect.dispose();
    }
    /////////////////////////////////////////////////////////////////////////////////////////
    //DRAWING FUNCTIONS
    @Override
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(title,viewX+(WIDTH/2)-(title.getWidth()/2),viewY+HEIGHT- title.getHeight()-30);
        sb.end();
        drawTitle(sb);
        drawOptions(sb);
    }
    private void drawTitle(SpriteBatch sb){
        sb.begin();
        Game.setFontSize(5);
        Game.font.setColor(Color.WHITE);
        String title="-DEMO-";
        Game.getFont().draw(sb,title, centerString(title),titlePosY-100);
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

            Game.getFont().draw(sb,options.get(i),centerString(options.get(i)),optionsPosY-(i*20));
        }
        sb.end();
    }
    /////////////////////////////////////////////////////////////////////////////////////////
    //Controller FUNCTIONS
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
        Xbox360Pad.updateSticks(axisCode,value);
        return false;
    }
    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        Xbox360Pad.updatePOV(value);
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
