package com.quadx.dungeons.tools.controllers;

import com.quadx.dungeons.states.MainMenuState;
import com.quadx.dungeons.states.mapstate.MapState;

import static com.quadx.dungeons.Game.controllerMode;

/**
 * Created by Chris Cavazos on 1/31/2017.
 */
public class Controllers {
    public static void initControllers(MapState state){
        if(controllerMode)
            MainMenuState.controller.addListener(state);
    }
}
