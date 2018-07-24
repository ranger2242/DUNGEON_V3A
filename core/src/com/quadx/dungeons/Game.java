package com.quadx.dungeons;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.quadx.dungeons.commands.Command;
import com.quadx.dungeons.items.equipment.EquipSets;
import com.quadx.dungeons.states.GameStateManager;
import com.quadx.dungeons.tools.ImageLoader;
import com.quadx.dungeons.tools.Tests;
import com.quadx.dungeons.tools.controllers.Xbox360Pad;
import com.quadx.dungeons.tools.files.FileHandler;
import com.quadx.dungeons.tools.files.FilePaths;
import com.quadx.dungeons.tools.gui.HUD;
import com.quadx.dungeons.tools.gui.Text;

import java.util.Random;


@SuppressWarnings("UnusedParameters")
public class Game extends ApplicationAdapter implements ControllerListener {
    private static GameStateManager gameStateManager;
    private SpriteBatch spriteBatch;

    public static Random rn = new Random();
    public static Player player = new Player();
    public static Vector2 scr = new Vector2(1336,724);


    @Override
    public void create() {
        Tests.gameLoadTime.start();
        HUD.setRes(scr);
        FilePaths.checkOS();
        Command.initComms();
        Text.generateFonts();
        Xbox360Pad.init();
        ImageLoader.load();//<
        HUD.load();
        EquipSets.load();
        FileHandler.initFile();
        spriteBatch = new SpriteBatch();
        gameStateManager= GameStateManager.gsmInit();
        Tests.gameLoadTime.end();
        Tests.gameLoadTime.print();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        gameStateManager.update(Gdx.graphics.getDeltaTime());
        gameStateManager.render(spriteBatch);
    }

    public static void console(String s) {
        System.out.println(s);
    }

    @Override
    public void connected(Controller controller) { }

    @Override
    public void disconnected(Controller controller) { }

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
