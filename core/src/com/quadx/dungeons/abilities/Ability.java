package com.quadx.dungeons.abilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

/**
 * Created by Chris Cavazos on 5/20/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public abstract class Ability {
    static Texture icon;
    final ArrayList<String> output=new ArrayList<>();
    static boolean enabled=false;
    static float cooldown=0;
    static float timeCounter=0;

    Ability(){
    }
    Texture loadIcon(String s){icon= new Texture(Gdx.files.internal(s));return icon;}
    public  Texture getIcon(){return icon;}

    public abstract void onActivate();
    public abstract int getMod();
    public ArrayList<String> details(){return  output;}
    public static void updateTimeCounter(){timeCounter+= Gdx.graphics.getDeltaTime();}

    public static boolean isEnabled(){return enabled;}
    public abstract String getName();

}
