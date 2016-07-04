package com.quadx.dungeons.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.quadx.dungeons.attacks.Attack;
import com.quadx.dungeons.tools.ImageLoader;

public class Item
{
    protected String name;
    protected int cost;
    protected int hpmod;
    protected int manamod;
    protected int attackmod;
    protected int defensemod;
    protected int intelmod;
    protected int speedmod;
    protected int emod;
    public boolean isEquip=false;
    public boolean isSpell=false;
    Texture icon= ImageLoader.crate;
    int gold;
    Attack attack;
    public Item() {}
    public int getDefensemod()
    {
        return defensemod;
    }
    public int getSpeedmod()
    {
        return speedmod;
    }
    public int getAttackmod()
    {
        return attackmod;
    }
    public int getIntelmod()
    {
        return intelmod;
    }
    public int getHpmod()
    {
        return hpmod;
    }
    public int getEmod()
    {
        return emod;
    }

    public int getCost() {return cost;}
    public String getName(){return name;}
    public String getType(){
        return null;
    }
    public void loadIcon(String s){
        try {
            icon = new Texture(Gdx.files.internal("images\\icons\\items\\ic" + s + ".png"));
        }catch (GdxRuntimeException e){
            icon= ImageLoader.crate;
        }
    }

    public int getManamod() {
        return manamod;
    }

    public Texture getIcon() {
        return icon;
    }

    public void setIcon(Texture icon) {
        this.icon = icon;
    }
}
