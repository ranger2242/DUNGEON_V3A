package com.quadx.dungeons.abilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

/**
 * Created by Chris Cavazos on 5/20/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public abstract class Ability {
    protected static Texture icon;
    ArrayList<String> output=new ArrayList<>();
    protected static boolean enabled=false;
    protected static float cooldown=0;
    protected static float timeCounter=0;
    protected static float mod=0;
    Ability(){
    }
    public Texture loadIcon(String s){icon= new Texture(Gdx.files.internal(s));return icon;}
    public  Texture getIcon(){return icon;}

    public abstract void onActivate();
    public abstract int getMod();
    public ArrayList<String> details(){return  output;}
    public static void updateTimeCounter(){timeCounter+= Gdx.graphics.getDeltaTime();}
    public static float getCoolDown(){return cooldown;}
    public static boolean isEnabled(){return enabled;}
    public abstract String getName();

}
