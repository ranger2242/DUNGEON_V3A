package com.quadx.dungeons.items;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.attacks.Attack;
import com.quadx.dungeons.items.equipment.Equipment;
import com.quadx.dungeons.items.modItems.*;
import com.quadx.dungeons.items.recipes.Recipe;
import com.quadx.dungeons.physics.Body;
import com.quadx.dungeons.tools.gui.HUD;

import static com.quadx.dungeons.Game.equipSets;
import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.GridManager.rn;

public class Item
{
    public boolean isUsable=false;
    protected String name="ITEM";
    protected int value=0;
    protected String fileName="";
    protected int cost;
    protected int hpmod;
    protected int manamod;
    protected int strmod;
    protected int defensemod;
    protected int intelmod;
    protected int speedmod;
    protected int emod;
    protected Color ptColor=Color.WHITE;
    public boolean isEquip=false;
    public boolean isSpell=false;
    Rectangle hitbox=new Rectangle();
    protected Vector2 texturePos=new Vector2();
    protected Texture icon=null;
    int gold;
    Attack attack;
    int effect= -1;
    public Body body = new Body();

    public Item() {
    }
    public static Item generateNoGold() {
        Item a;
        int q = rn.nextInt(11) + 1;
        a = new Item();
        if (q == 1 || q == 2) a = new StrengthPlus();
        else if (q == 3 || q == 4) a = new DefPlus();
        else if (q == 5 || q == 6) a = new IntPlus();
        else if (q == 7 || q == 8) a = new SpeedPlus();
        else if (q == 9 && rn.nextFloat() < .7) {
            if (rn.nextFloat() < .1) {
                a = new SpellBook();
            }
        } else if (q == 10 && rn.nextFloat() < .7) {
            if (rn.nextFloat() < .9)
                a = Equipment.generateEquipment();
            else {
                a = equipSets.ref[rn.nextInt(5)].get(rn.nextInt(8));
            }
        } else if (q == 11 && rn.nextFloat() < .7) {
            a=new Recipe();
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
    public static Item generateMonsterDrop() {
        Item a;
        int q = rn.nextInt(10) + 1;
        if (q <= 2) {
            a = new Leather();
        } else {
            a = Item.generateNoGold();
        }
        return a;
    }
    public Vector2 getTexturePos() {
        return texturePos;
    }
    public Rectangle getHitbox(){return hitbox;}
    public Color getPtColor(){return ptColor;}
    public String getName(){return name;}
    public String getType(){
        return null;
    }
    public Texture getIcon() {
        if(icon==null)
            loadIcon();
        return icon;
    }
    public int getDefensemod()
    {
        return defensemod;
    }
    public int getSpeedmod()
    {
        return speedmod;
    }
    public int getStrmod()
    {
        return strmod;
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
    public int getManamod() {
        return manamod;
    }
    public int getValue() {
        return value;
    }
    public void setIcon(Texture icon)  {
        this.icon = icon;
    }
    public void setHitBox(Rectangle r){
        hitbox=r;
    }
    public void setTexturePos(Vector2 v) {
        texturePos.set(v);
    }
    public void loadIcon(String s){
        if(!s.isEmpty())
            icon = new Texture(Gdx.files.internal("images\\icons\\items\\"+s));
    }

    public void colliion(Vector2 v) {
        int x = (int) v.x,
                y = (int) v.y;

        Class c = this.getClass();
        boolean a= c.equals(EnergyPlus.class) ||
                c.equals(Potion.class) ||
                c.equals(ManaPlus.class);
        if(a){
            HUD.out("USE ITEM");
            player.useItem(this);
        }
        else
            player.pickupItem(this);




    }
    public boolean isGold(){
        return getClass().equals(Gold.class);
    }
    public boolean hasEffect() {
        if(!this.getClass().equals(Gold.class) &&!this.getClass().equals(EnergyPlus.class)&&
                !this.getClass().equals(Potion.class))
        return true;
        else return false;
    }
    public int getSellPrice(){
        return (int) (cost*.75f);
    }

    public void loadIcon() {
        loadIcon(fileName);
    }


    public static Item generateSpecial() {
        Item item=null;
        if(rn.nextInt(8)<7){
            int i = rn.nextInt(3);
            if (i == 1) {
                item = new Potion();
            }
            if (i == 2) {
                item = new ManaPlus();
            }
            if (i == 0) {
                item = new EnergyPlus();
            }
        }else{
            item = Equipment.generate();
        }
        return item;
    }

    public Vector2 getIconDim() {
        if(icon !=null){
            return new Vector2(icon.getWidth(),icon.getHeight());
        }else
            return new Vector2();
    }


    public void setParticle(int effect) {
        this.effect=effect;
    }
}
