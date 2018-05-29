package com.quadx.dungeons.items.recipes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.items.Leather;
import com.quadx.dungeons.items.Ore;
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

    public void setCosts(Pair<Integer, Item>[] costs) {
        this.costs = costs;
    }

    public int getGoldCost() {
        return goldCost;
    }

    public void setGoldCost(int goldCost) {
        this.goldCost = goldCost;
    }

    Pair<Integer, Item>[] costs = new Pair[5];
    int goldCost = 0;
    Equipment equip=null;

    public Recipe() {
        fileName = "icRecipe.png";
        name = "Recipe";
    }

    void loadEquip(Equipment e) {
        equip=e;
        int[] a = e.getCraftCost();
        costs[0] = new Pair<>(a[0], new Ore());
        costs[1] = new Pair<>(a[1], new Leather());
        goldCost=a[2];
    }

    public void render(SpriteBatch sb, Vector2 pos){
        ArrayList<String> cost= new ArrayList<>();
        for(int i=0;i<costs.length;i++){
            Pair p=costs[i];
            if(p !=null){
                Item it= (Item) p.getValue();
                cost.add((p.getKey())+"x "+it.getName());
            }
        }

        Game.setFontSize(1);
        BitmapFont font= Game.getFont();
        font.setColor(Color.WHITE);
        font.draw(sb,name,pos.x,pos.y);
        if(equip!=null)
        sb.draw(equip.getIcon(),pos.x-34,pos.y);
        for(int i = 0;i< cost.size();i++){
            font.draw(sb,cost.get(i),pos.x+200,pos.y-(i*14));

        }
    }

    public Equipment getEquip() {
        return equip;
    }
}
