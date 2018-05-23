package com.quadx.dungeons.tools.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.Game;

import java.util.ArrayList;

/**
 * Created by range_000 on 1/5/2017.
 */
public class InfoOverlay {
    public ArrayList<Text> texts = new ArrayList<>();
    public ArrayList<Texture> textures = new ArrayList<>();
    public ArrayList<Rectangle> rects = new ArrayList<>();
    public ArrayList<Vector2> texturePos=new ArrayList<>();
    public void draw(SpriteBatch sb){
            for (int i = 0; i < textures.size(); i++) {
                Vector2 v=texturePos.get(i);
                sb.draw(textures.get(i), v.x, v.y);
            }

        for (Text t : texts) {
                Game.getFont().draw(sb, t.text, t.pos.x, t.pos.y);
            }
     }
    public void add(InfoOverlay io){
        this.texts.addAll(io.texts);
        this.textures.addAll(io.textures);
        this.texturePos.addAll(io.texturePos);
        this.rects.addAll(io.rects);

    }
}
