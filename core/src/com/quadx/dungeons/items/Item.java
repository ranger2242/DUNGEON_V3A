package com.quadx.dungeons.items;

import com.quadx.dungeons.attacks.Attack;

public class Item
{
    public String name;
    public int cost;
    public int hpmod;
    public int manamod;
    public int attackmod;
    public int defensemod;
    public int intelmod;
    public int speedmod;
    public int levelmod;
    public int accmod;
    public boolean isEquip=false;
    public boolean isSpell=false;
    public Attack attack;

    public Item(int a, int b, int c, int d, int e, int f, int g,String h)
    {
        cost=a;
        hpmod=b;
        attackmod=c;
        defensemod=d;
        intelmod=e;
        speedmod=f;
        levelmod=g;
        name=h;
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

    public int getLevelmod()
    {
        return levelmod;
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
    public int getAccmod()
    {
        return accmod;
    }
    public int getCost() {return cost;}
    public String getName(){return name;}
    public String getType(){
        return null;
    }


    public int getManamod() {
        return manamod;
    }
}
