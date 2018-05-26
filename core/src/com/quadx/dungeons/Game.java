package com.quadx.dungeons;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.quadx.dungeons.abilities.*;
import com.quadx.dungeons.commands.*;
import com.quadx.dungeons.items.equipment.EquipSets;
import com.quadx.dungeons.states.AbilitySelectState;
import com.quadx.dungeons.states.GameStateManager;
import com.quadx.dungeons.states.HighScoreState;
import com.quadx.dungeons.states.MainMenuState;
import com.quadx.dungeons.tools.FilePaths;
import com.quadx.dungeons.tools.ImageLoader;
import com.quadx.dungeons.tools.Score;
import com.quadx.dungeons.tools.WallPattern;
import com.quadx.dungeons.tools.controllers.Xbox360Pad;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


@SuppressWarnings("UnusedParameters")
public class Game extends ApplicationAdapter implements ControllerListener {

    public static ArrayList<Command> commandList = new ArrayList<>();

    private static final BitmapFont[] fonts = new BitmapFont[6];
    public static BitmapFont font;

    public static Player player = new Player();

    public static final int WIDTH = 1366;
    public static final int HEIGHT = 724;
    public static boolean controllerMode = false;
    public static float multiplier =1;

    public static Vector2 scr = new Vector2(WIDTH,HEIGHT);

    private static GameStateManager gameStateManager;
    private static BitmapFont[] fontsA = new BitmapFont[6];
    public static EquipSets equipSets;
    public static Random rn = new Random();
    private SpriteBatch spriteBatch;


    private static void addCommand() {
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
        commandList.add(new ScrollLeftComm());
        commandList.add(new ScrollRightComm());
        commandList.add(new RotLeftComm());
        commandList.add(new RotRightComm());
        commandList.add(new MainAttackComm());
        commandList.add(new ChangeAttackComm());
        commandList.add(new UseComm());
        commandList.add(new JumpComm());
        commandList.add(new DropComm());
        commandList.add(new PauseComm());
        commandList.add(new BackComm());
        commandList.add(new ConfirmComm());
    }

    @Override
    public void create() {
        FilePaths.checkOS();
        ImageLoader il = new ImageLoader();
        equipSets = new EquipSets();
        WallPattern wp = new WallPattern();
        Gdx.graphics.setVSync(true);
        addCommand();
        Xbox360Pad.addNames();
        fonts[0] = createFont(8);
        fonts[1] = createFont(10);
        fonts[2] = createFont(12);
        fonts[3] = createFont(14);
        fonts[4] = createFont(16);
        fonts[5] = createFont(20);
        fontsA = fonts;
        initFile();
        gameStateManager = new GameStateManager();
        Gdx.graphics.setWindowedMode(WIDTH, HEIGHT);
        setFontSize(5);
        spriteBatch = new SpriteBatch();
        gameStateManager.push(new MainMenuState(gameStateManager));
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gameStateManager.update(Gdx.graphics.getDeltaTime());
        gameStateManager.render(spriteBatch);
    }

    public static BitmapFont getFont() {

        return font;
    }



    public static void console(String s) {
        System.out.println(s);
    }

    public static void printLOG(Exception e) {
		/*
		sw=new StringWriter();
		pw2=new PrintWriter(sw);
		e.printStackTrace(pw2);
		pw.append(sw.toString()+"\n");*/
    }

    public static void setFontSize(int x) {
        font = fonts[x];

    }

    private void initFile() {

        try {
            FileReader file2 = new FileReader("controls.txt");
            BufferedReader bf2 = new BufferedReader(file2);
            String s2 = bf2.readLine();
            String[] sp = s2.split(" ");
            for (int i = 0; i < sp.length; i++) {
                commandList.get(i).changeKey(Integer.parseInt(sp[i]));
            }


            FileReader file = new FileReader("scores.txt");
            BufferedReader bf = new BufferedReader(file);
            String s;
            while ((s = bf.readLine()) != null) {
                if (!s.equals("")) {
                    List<String> split = Arrays.asList(s.split(","));
                    HighScoreState.addScore(new Score(split.get(0), split.get(1), split.get(2), split.get(3), split.get(4)));
                }
            }
            FileReader file3 = new FileReader("data\\abilityDetails.csv");
            BufferedReader bf3 = new BufferedReader(file3);
            String s3;

            //LOAD ABILITY DESCRIPTIONS
            //name, description
            //hpmax,hpreg,Mmax,Mreg,Emax,Ereg,att,def,int,spd
            ArrayList<String> det = new ArrayList<>();
            ArrayList<Ability> abs= new ArrayList<>();
            Tank tank = new Tank();
            Investor inv = new Investor();
            Mage mage = new Mage();
            Quick quick = new Quick();

            int cnt=0;
            for (int i=0;(s3 = bf3.readLine()) != null; i++) {

                List<String> split = Arrays.asList(s3.split(","));
                if(i %6==0){
                    det.addAll(split);
                }else{
                    det.add(s3);
                }

                abs.add(tank);
                abs.add(inv);
                abs.add(mage);
                abs.add(quick);
                if(i %6==5) {
                    abs.get(cnt).setDetails(det);
                    det.clear();
                    cnt++;
                }
            }

            AbilitySelectState.setAbilityList(abs);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static BitmapFont createFont(int x) {
        BitmapFont temp = new BitmapFont();

        try {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(FilePaths.getPath("fonts\\prstart.ttf")));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = x;
            temp = generator.generateFont(parameter);
            //console("Font Generated");
        } catch (Exception e) {
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

    public static void resetFont() {
        for(BitmapFont f: fonts){
            Color c= new Color(1,1,1,1);
            f.setColor(c);
        }
    }
}
