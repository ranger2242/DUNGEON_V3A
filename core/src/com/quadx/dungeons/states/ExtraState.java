package com.quadx.dungeons.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Tom on 12/30/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class ExtraState extends State {
    public ExtraState(GameStateManager gsm) {
        super(gsm);

    }

    public void update(float dt) {
        handleInput();
    }
    public void render(SpriteBatch sb) {

    }
    public void dispose() {

    }
}
