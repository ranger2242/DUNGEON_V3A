package com.quadx.dungeons.states.mapstate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.quadx.dungeons.Anim;
import com.quadx.dungeons.GridManager;
import com.quadx.dungeons.abilities.Ability;
import com.quadx.dungeons.attacks.Attack;
import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.paricles.ParticleHandler;
import com.quadx.dungeons.shapes1_5.ShapeRendererExt;
import com.quadx.dungeons.states.*;
import com.quadx.dungeons.tools.StatManager;
import com.quadx.dungeons.tools.Tests;
import com.quadx.dungeons.tools.buttons.ButtonHandler;
import com.quadx.dungeons.tools.buttons.MapStateButtonHandler;
import com.quadx.dungeons.tools.controllers.Controllers;
import com.quadx.dungeons.tools.controllers.Xbox360Pad;
import com.quadx.dungeons.tools.gui.HUD;
import com.quadx.dungeons.tools.gui.HoverText;

import java.util.Random;

import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.Game.scr;
import static com.quadx.dungeons.GridManager.dispArray;
import static com.quadx.dungeons.states.mapstate.MapStateRender.renderLayers;


@SuppressWarnings("DefaultFileTemplate")
public class MapState extends State implements ControllerListener {

    public static final boolean debug = true;
    public static boolean pause = false;
    public static boolean showStats = true;
    public static boolean inGame = false;

    public static int cellW = 30;
    static int altNumPressed = 1;

    public static Vector2 cell = new Vector2(cellW, cellW * (2f / 3f));
    public static Vector2 warp = new Vector2();
    public static Vector2 shop = new Vector2();


    static ShapeRendererExt sr = new ShapeRendererExt();
    public static Texture statPopup;
    public static GridManager gm;
    public static final Random rn = new Random();
    static ButtonHandler buttons=new MapStateButtonHandler();
    public static ParticleHandler particleHandler = new ParticleHandler();

    public MapState(GameStateManager gsm) {
        super(gsm);
        new HUD();
        StatManager.reset();
        Controllers.initControllers(this);
        player.initPlayer();
        gm = new GridManager();
        cam.setToOrtho(false, scr.x, scr.y);
        debug();
    }

    public static void warpToNext(boolean abilityState) {
        ParticleHandler.removeParticles();
        if (player.hasAP() && abilityState) {
            gsm.push(new AbilitySelectState(gsm));
        }
        player.floor++;
        gm.initializeGrid();
    }

    public static void warpToShop() {
        dispArray[(int) shop.x][(int) shop.y].setShop(false);
        gsm.push(new ShopState(gsm));
    }

    public static void pause() {
        pause = true;
        cam.position.set(0, 0, 0);
        gsm.push(new ShopState(gsm));
    }

    public static void loadCraftState() {
        camController.setSnapCam(true);
        camController.update(Gdx.graphics.getDeltaTime(),cam);
        gsm.push(new CraftState(gsm));
    }

    private void debug() {
        if(Tests.allAttacks)
            player.addAllAttacks();
        //Tests.goldTest();
        //Tests.testEquipmentRates();
        //Tests.giveItems(100);
        //Tests.testsMonsterStats();
    }
    public void handleInput() {
    }
    public void update(float dt) {
        //nothing before Tests load
        Tests.update(dt);
        buttons.update(dt);
        Anim.update(dt);
        Attack.update(dt);
        Ability.update(dt);
        particleHandler.update(dt);
        Monster.update(dt);// <<------come back to this
        player.update(dt,getClass());// <<------
        HoverText.update(dt);
        camController.update(dt, cam);
        GridManager.update(dt);
        HUD.update();
        MapStateRender.updateVariables(dt);

    }
    public void render(SpriteBatch sb) {
        renderLayers(sb);
    }
    public void dispose() {
    }



/*    public static void scrollAttacks(boolean right){
        if (dtScrollAtt > .3) {
            if (right) {
                if (Inventory.pos < player.invList.size() - 1)
                    Inventory.pos++;
                else Inventory.pos = 0;
                Inventory.dtInvSwitch = 0;
            } else {
                if (Inventory.pos > 0)
                    Inventory.pos--;
                else Inventory.pos = player.invList.size() - 1;
                Inventory.dtInvSwitch = 0;
            }
        }
    }*/

    //-----------------------------------------------------------------------------------------
    //Controller Interface
    public void connected(Controller controller) {

    }
    public void disconnected(Controller controller) {

    }
    public boolean buttonDown(Controller controller, int buttonCode) {
        return false;
    }
    public boolean buttonUp(Controller controller, int buttonCode) {
        return false;
    }
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        Xbox360Pad.updateSticks(axisCode,value);
        return false;

   }
    public boolean povMoved(Controller controller, int povCode, PovDirection value) {
        Xbox360Pad.updatePOV(value);
        return false;
    }
    public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }
    public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
        return false;
    }
    public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
        return false;
    }
}
