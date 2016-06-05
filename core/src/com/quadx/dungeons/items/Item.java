package com.quadx.dungeons.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.quadx.dungeons.attacks.Attack;
import com.quadx.dungeons.items.equipment.Equipment;

public class Item
{
    protected String name;
    int cost;
    protected int hpmod;
    protected int manamod;
    protected int attackmod;
    protected int defensemod;
    protected int intelmod;
    protected int speedmod;
    private int levelmod;
    public boolean isEquip=false;
    public boolean isSpell=false;
    protected Texture icon;
    Attack attack;

    public Item(Texture t,int a, int b, int c, int d, int e, int f, int g,String h)
    {
        cost=a;
        hpmod=b;
        attackmod=c;
        defensemod=d;
        intelmod=e;
        speedmod=f;
        levelmod=g;
        name=h;
        icon=t;
    }

    public Item() {
    }

    public void itemDetails()
    {
        System.out.println(name);
        System.out.println("HP mod "+hpmod);
        System.out.println("Attack mod "+attackmod);
        System.out.println("Defense mod "+defensemod);
        System.out.println("Intel mod "+intelmod);
        System.out.println("Speed mod "+speedmod);
        System.out.println("Level mod "+levelmod);
    }

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
    public int getCost() {return cost;}
    public String getName(){return name;}
    public String getType(){
        return null;
    }
    protected void loadIcon(String s){
        if(icon ==null)
        {
            icon= new Texture(Gdx.files.internal("images/icons/items/ic" + s + ".png"));
        }

    }

    public int getManamod() {
        return manamod;
    }

    public Texture getIcon() {
        return icon;
    }
}
