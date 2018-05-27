package com.quadx.dungeons.tools.buttons;

import com.badlogic.gdx.Gdx;
import com.quadx.dungeons.states.ShopState;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.states.mapstate.MapStateRender;
import com.quadx.dungeons.tools.DebugTextInputListener;
import com.quadx.dungeons.tools.FilePaths;
import com.quadx.dungeons.tools.Tests;

import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.GridManager.monsterList;
import static com.quadx.dungeons.tools.Tests.displayFPS;
import static com.quadx.dungeons.tools.gui.HUD.out;

/**
 * Created by Chris Cavazos on 5/23/2018.
 */
public class DebugKeys {

    public void execute(int i){
        switch (i){
            case 0:
                reloadMap();
                break;
            case 1:
                toggleFPSModule();
                break;
            case 2:
                clearMap();
                break;
            case 3:
                loadShop();
                break;
            case 4:
                toggleSimpleStats();
                break;
            case 5:
                lvlupPlayer();
                break;
            case 6:
                clearMonsters();
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                break;
            case 10:
                toggleStats();
                break;
            case 11:
                loadInputDiag();
                break;
        }
    }

    private void loadInputDiag() {
        if(FilePaths.checkOS() == 0) {//open debug prompt
            DebugTextInputListener listener = new DebugTextInputListener();
            Gdx.input.getTextInput(listener, "Command", "", "");
        }
    }

    private void reloadMap(){
        MapState.warpToNext(false);
    }

    private void toggleFPSModule(){
        displayFPS = !displayFPS;
    }

    private void clearMap(){
        Tests.calcAvgMapLoadTime();
        Tests.loadEmptyMap();
    }

    private void loadShop(){
        MapState.gsm.push(new ShopState(MapState.gsm));
    }

    private void toggleSimpleStats(){
        player.st.toggleSimpleStats();
    }

    private void toggleStats(){
        MapStateRender.showStats = !MapStateRender.showStats;

    }

    private void lvlupPlayer(){
        player.forceLevelUp();
    }

    private void clearMonsters(){
        Tests.allstop = !Tests.allstop;
        Tests.spawn = !Tests.spawn;
        out(!Tests.spawn + "");
        monsterList.clear();
    }
}
