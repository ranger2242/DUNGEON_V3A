package com.quadx.dungeons.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.states.mapstate.MapState;

import java.util.ArrayList;

import static com.quadx.dungeons.states.mapstate.MapState.viewX;
import static com.quadx.dungeons.states.mapstate.MapState.viewY;

/**
 * Created by Tom on 12/22/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class MainMenuState extends State {
    public static GlyphLayout gl=new GlyphLayout();
    private static ShapeRenderer shapeR=new ShapeRenderer();
    private static ParticleEffect effect;
    private static int selector=0;
    private int titlePosX =0;
    private int titlePosY=0;
    private int selectorPosX=0;
    private int optionsPosX =0;
    private int optionsPosY =0;
    private float dtCursor = 0;
    public MainMenuState(GameStateManager gsm)
    {
        super(gsm);

        Gdx.gl.glClearColor(0,0,0,1);

        effect = new ParticleEffect();
        ParticleEmitter emitter = new ParticleEmitter();
        String s = "StartScreen";
        effect.load(Gdx.files.internal("particles\\pt" + s), Gdx.files.internal("particles"));
        emitter = effect.findEmitter("StartScreen");
        emitter.setContinuous(true);
        font.setColor(1,1,1,1);
        //Game.setFontSize(40);
        CharSequence cs="DUNGEON";
        gl.setText(Game.getFont(),cs);
        cam.position.x=0;
        cam.position.y=0;

    }
    @Override
    protected void handleInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.ENTER)){
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
                    gsm.push(new ExtraState(gsm));
                    break;
                }case(4):{
                    System.exit(0);
                    break;
                }
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            selector--;
            if(selector<0)selector=4;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            selector++;
            if(selector>4)selector=0;
            // was selector>options.size()-1
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
        selectorPosX=(int)(viewX+        (Game.WIDTH/2)-100);
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
        drawSelector();
    }
/////////////////////////////////////////////////////////////////////////////////////////
//DRAWING FUNCTIONS
private void drawTitle(SpriteBatch sb){
        sb.begin();
        Game.setFontSize(40);
        Game.font.setColor(Color.WHITE);
        Game.getFont().draw(sb,"DUNGEON", titlePosX,titlePosY);
        effect.draw(sb);
        sb.end();
    }
    private void drawOptions(SpriteBatch sb){
        sb.begin();
        Game.setFontSize(20);
        if(MapState.inGame)
            Game.getFont().draw(sb,"CONTINUE", optionsPosX, optionsPosY);
        else
            Game.getFont().draw(sb,"START", optionsPosX, optionsPosY);
        Game.getFont().draw(sb,"OPTIONS", optionsPosX, optionsPosY -(20));
        Game.getFont().draw(sb,"CONTROLS", optionsPosX, optionsPosY -(2*20));
        Game.getFont().draw(sb,"EXTRA", optionsPosX, optionsPosY -(3*20));
        Game.getFont().draw(sb,"EXIT", optionsPosX, optionsPosY -(4*20));
        sb.end();
    }
    private void drawSelector(){
        shapeR.begin(ShapeRenderer.ShapeType.Filled);
        shapeR.setColor(1,1,1,1);
        shapeR.rect(selectorPosX,optionsPosY-((selector+1)*20),20,20);
        shapeR.end();
    }
/////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void dispose() {
      //  shapeR.dispose();
       effect.dispose();
    }
}
