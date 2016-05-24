package com.quadx.dungeons.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Tom on 12/30/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class OptionState extends State{
    public OptionState(GameStateManager gsm) {
        super(gsm);
    }

    protected void handleInput() {
        if(Gdx.input.isKeyPressed(Input.Keys.TAB)){
            gsm.pop();
        }
    }
    public void update(float dt) {
        handleInput();
    }
    public void render(SpriteBatch sb) {

    }

    public void dispose() {

    }
}
