package com.quadx.dungeons;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector3;
import com.quadx.dungeons.commands.*;
import com.quadx.dungeons.items.equipment.EquipSets;
import com.quadx.dungeons.states.GameStateManager;
import com.quadx.dungeons.states.HighScoreState;
import com.quadx.dungeons.states.MainMenuState;
import com.quadx.dungeons.tools.ImageLoader;
import com.quadx.dungeons.tools.Score;
import com.quadx.dungeons.tools.WallPattern;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



@SuppressWarnings("UnusedParameters")
public class Game extends ApplicationAdapter implements ControllerListener{
/*
screen shake
shaders
particle effect for dig
hidden enemie comes out of ground
particle effect on warp
release velocity falloff
enemy and player knock back

 */
    public static boolean disableGfx=true;
    public static BitmapFont font;
    public static Player player= new Player();
    public static EquipSets equipSets;
    public static ArrayList<Command> commandList=new ArrayList<>();
    public static boolean controllerMode =false;
    public static final int WIDTH = 1366;
    public static final int HEIGHT = 724;
    public static final float frame = .01666f;
    private SpriteBatch spriteBatch;
    private static GameStateManager gameStateManager;
    private static final BitmapFont[] fonts = new BitmapFont[6];
    public static boolean shakeCam=false;

    static void addCommand(){
        commandList.clear();
        commandList.add(new UpComm());
        commandList.add(new DownComm());
        commandList.add(new LeftComm());
        commandList.add(new RightComm());
        commandList.add(new AimUpComm());
        commandList.add(new AimDownComm());
        commandList.add(new AimLeftComm());
        commandList.add(new AimRightComm());
        commandList.add(new AltAttackComm());
        commandList.add(new ConfirmComm());
        commandList.add(new BackComm());
        commandList.add(new DigComm());
        commandList.add(new DropComm());
        commandList.add(new MainAttackComm());
        commandList.add(new PauseComm());
        commandList.add(new ScrollLeftComm());
        commandList.add(new ScrollRightComm());
        commandList.add(new UseComm());
    }
    @Override
    public void create () {
        ImageLoader il=new ImageLoader();
        equipSets= new EquipSets();
        WallPattern wp=new WallPattern();
        addCommand();
        Xbox360Pad.addNames();
        fonts[0]=createFont(8);
        fonts[1]=createFont(10);
        fonts[2]=createFont(12);
        fonts[3]=createFont(14);
        fonts[4]=createFont(16);
        fonts[5]=createFont(20);
        initFile();
        gameStateManager=new GameStateManager();
        Gdx.graphics.setWindowedMode(WIDTH,HEIGHT);
        setFontSize(5);
        spriteBatch = new SpriteBatch();
        gameStateManager.push(new MainMenuState(gameStateManager));
    }
    @Override
    public void render () {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gameStateManager.update(Gdx.graphics.getDeltaTime());
        gameStateManager.render(spriteBatch);
    }
    public static BitmapFont getFont(){

        return font;
    }
    public static void console(String s){
        System.out.println(s);
    }
    public static void printLOG(Exception e) {
		/*
		sw=new StringWriter();
		pw2=new PrintWriter(sw);
		e.printStackTrace(pw2);
		pw.append(sw.toString()+"\n");*/
    }
    public static void setFontSize(int x){
        font=fonts[x];
    }
    private void initFile(){

        try {
            FileReader file= new FileReader("scores.txt");
            BufferedReader bf = new BufferedReader(file);
            String s;
            while((s=bf.readLine()) != null){
                List<String> split = Arrays.asList(s.split(","));
                HighScoreState.addScore(new Score(split.get(0),split.get(1),split.get(2),split.get(3),split.get(4)));
            }
        } //penis
        catch (IOException e) {
            e.printStackTrace();
        }

    }
    private static BitmapFont createFont(int x){
        BitmapFont temp=new BitmapFont();

        try {
            FreeTypeFontGenerator generator= new FreeTypeFontGenerator(Gdx.files.internal("fonts\\prstart.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter= new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = x;
            temp = generator.generateFont(parameter);
            //console("Font Generated");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return temp;
    }
    @Override
    public void connected(Controller controller) {

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
        return false;
    }

    @Override
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
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
