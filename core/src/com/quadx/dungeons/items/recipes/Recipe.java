package com.quadx.dungeons.items.recipes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.Inventory;
import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.items.resources.Leather;
import com.quadx.dungeons.items.resources.Ore;
import com.quadx.dungeons.items.equipment.Equipment;
import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by Chris Cavazos on 5/29/2018.
 */
public class Recipe extends Item {
    public Pair<Integer, Item>[] getCosts() {
        return costs;
    }

    Pair<Integer, Item>[] costs = new Pair[5];
    int goldCost = 0;
    Equipment equip = null;
    protected boolean isEquipRecipe = false;
    protected boolean isPotionRecipe = false;
    protected String craftFileName = "";
    protected Item output = null;
    protected int itemLimit=5;

    public void setCosts(Pair<Integer, Item>[] costs) {
        this.costs = costs;
    }

    public int getGoldCost() {
        return goldCost;
    }

    public void setGoldCost(int goldCost) {
        this.goldCost = goldCost;
    }

    public Recipe() {
        fileName = "icRecipe.png";
        name = "Recipe";
    }

    public void loadEquip(Equipment e) {
        output = e;
        int[] a = e.getCraftCost();
        costs[0] = new Pair<>(a[0], new Ore());
        costs[1] = new Pair<>(a[1], new Leather());
        goldCost = a[2];
    }

    public void render(SpriteBatch sb, Vector2 pos) {
        ArrayList<String> cost = new ArrayList<>();
        for (int i = 0; i < costs.length; i++) {
            Pair p = costs[i];
            if (p != null) {
                Item it = (Item) p.getValue();
                cost.add((p.getKey()) + "x " + it.getName());
            }
        }

        Game.setFontSize(1);
        BitmapFont font = Game.getFont();
        font.setColor(Color.WHITE);
        font.draw(sb, name, pos.x, pos.y);
        sb.draw(output.getIcon(), pos.x - 34, pos.y);
        for (int i = 0; i < cost.size(); i++) {
            font.draw(sb, cost.get(i), pos.x + 200, pos.y - (i * 14));

        }
    }

    public Item getOutput() {
        return output;
    }

    public Equipment getEquip() {
        return (Equipment) output;
    }

    public boolean isEquipRecipe() {
        return isEquipRecipe;
    }

    public boolean isPotionRecipe() {
        return isPotionRecipe;
    }

    public int getItemLimit() {
        return itemLimit;
    }

    public boolean meetsCost(Inventory slots, boolean exactly){
        boolean[] met = new boolean[]{false,false,false,false,false};
        Pair<Integer, Item>[] costs=getCosts();
        for(int i=0;i<5;i++){
            if(costs[i]==null){
                met[i]=true;
            }else{
                met[i]= costs[i].getKey()== slots.getStackSize(costs[i].getValue().getClass());
            }
        }
        boolean craftable=true;
        for(int i=0;i<5;i++) {
            craftable= craftable&&met[i];
        }
        if(exactly) {
            return craftable && slots.getList().size() == getItemLimit();
        }else
            return craftable;
    }
}
