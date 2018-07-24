package com.quadx.dungeons.tools.gui;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.tools.Direction;
import com.quadx.dungeons.tools.ImageLoader;

import java.util.ArrayList;

import static com.quadx.dungeons.tools.ImageLoader.nullIc;

/**
 * Created by Chris Cavazos on 7/2/2018.
 */
public abstract class Drawable {
      ArrayList<TextureRegion> icons = new ArrayList<>();
      public TextureRegion icon = new TextureRegion();

    public void gINIT(int i, String n) {
        addIcon(i, n);
        defaultIcon();
    }

    public void addIcon(int i, String name) {
        TextureAtlas atlas;
        switch (i) {
            case 0: {
                atlas = ImageLoader.actorsAt;
                break;
            }
            case 1: {
                atlas = ImageLoader.itemsAt;
                break;
            }
            case 2: {
                atlas = ImageLoader.hudAt;
                break;
            }
            default:
                atlas = ImageLoader.itemsAt;
        }
        icons.add(atlas.findRegion(name));
    }

    public ArrayList<TextureRegion> getIcons() {
        return icons;
    }

    public TextureRegion getIcon() {
        if(icon != null)
        return icon;
        else
            return ImageLoader.nullIc;
    }

    public TextureRegion getIcon(Direction.Facing facing) {
        int u;
        switch (facing) {
            case North:
            case Northwest:
            case Northeast:
                u = 0;
                break;
            case Southwest:
            case South:
            case Southeast:
                u = 1;
                break;
            case East:
                u = 3;
                break;
            case West:
                u = 2;
                break;
            default:
                u = 0;
                break;
        }
        if (icons.isEmpty())
            return nullIc;
        return icons.get(u);

    }

    protected void defaultIcon() {
        if(icons.isEmpty())
            icon=nullIc;
        else
            icon=icons.get(0);
    }

    void setIcon(TextureRegion i) {
        icon.setRegion(i);
    }

    public void setIcon(int i) {
        // setIcon(icons.get(i));
    }

    public Vector2 getIconDim() {
        return new Vector2(icon.getRegionWidth(), icon.getRegionHeight());
    }
}
