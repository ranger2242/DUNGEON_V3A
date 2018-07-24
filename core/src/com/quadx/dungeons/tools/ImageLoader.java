package com.quadx.dungeons.tools;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.quadx.dungeons.tools.files.FilePaths;


/**
 * Created by Chris Cavazos on 6/14/2016.
 */
public class ImageLoader {

    public static TextureAtlas actorsAt;
    public static TextureAtlas itemsAt;
    public static TextureAtlas hudAt;
    public static TextureRegion nullIc;
    public static final Texture[] equipBasic = new Texture[8];
    public static final Texture warp = new Texture(FilePaths.getPath("images\\tiles\\icWarp.png"));

    public static void load() {
        hudAt = new TextureAtlas("C:\\Users\\Chris\\Google Drive\\DUNGEON_V3A 2018\\core\\assets\\images\\packs\\hud\\hudAt.atlas");
        actorsAt = new TextureAtlas("images\\packs\\actors\\actors.atlas");
        itemsAt = new TextureAtlas("images\\packs\\items\\items.atlas");
        nullIc= itemsAt.findRegion("icCrate");
    }
}
