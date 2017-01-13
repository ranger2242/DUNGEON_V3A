package com.quadx.dungeons.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.quadx.dungeons.attacks.Attack;
import com.quadx.dungeons.items.equipment.Equipment;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.tools.ImageLoader;

import static com.quadx.dungeons.Game.equipSets;
import static com.quadx.dungeons.GridManager.rn;

public class Item
{
    protected String name="ITEM";
    protected int value=0;

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
    Texture icon=null;
    int gold;
    Attack attack;
    public Item() {
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
            if(this.isEquip){
                MapState.out(this.getClass().getName());

            }
            icon= ImageLoader.crate;
        }
    }

    public int getManamod() {
        return manamod;
    }

    public Texture getIcon() {
        return icon;
    }

    public void setIcon(Texture icon)  {
        this.icon = icon;
    }

    public int getValue() {
        return value;
    }

    public static Item generateNoGold() {
        Item a;
        int q = rn.nextInt(10) + 1;
        a = new Item();
        if (q == 1 || q == 2) a = new AttackPlus();
        else if (q == 3 || q == 4) a = new DefPlus();
        else if (q == 5 || q == 6) a = new IntPlus();
        else if (q == 7 || q == 8) a = new SpeedPlus();
        else if (q == 9 && rn.nextFloat()<.7) {
            if (rn.nextFloat() < .1) {
                a = new SpellBook();
            }
        } else if (q == 10 && rn.nextFloat()<.7) {
            if (rn.nextFloat() < .9)
                a = Equipment.generateEquipment();
            else {
                a = equipSets.ref[rn.nextInt(5)].get(rn.nextInt(8));
            }
        }
        if(a.getClass().equals(Item.class)){
            a=new Gold();
        }
        return a;
    }
    public static Item generate() {
        Item a;
        int q = rn.nextInt(14) + 1;
        if (q >= 11) {
            a = new Gold();
        } else {
            a =Item.generateNoGold();
        }
        return a;
    }
}
