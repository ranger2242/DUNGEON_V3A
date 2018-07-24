package com.quadx.dungeons.items.recipes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.Inventory;
import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.items.equipment.Equipment;
import com.quadx.dungeons.items.resources.Leather;
import com.quadx.dungeons.items.resources.Ore;
import com.quadx.dungeons.tools.gui.Text;
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
    protected int itemLimit = 5;

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

        gINIT(1,"icRecipe");
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

        Text.setFontSize(1);
        BitmapFont font = Text.getFont();
        font.setColor(Color.WHITE);
        font.draw(sb, name, pos.x, pos.y+36);
        sb.draw(output.getIcon(), pos.x - 34, pos.y);
        for (int i = 0; i < costs.length; i++) {
            if (costs[i] != null) {
                TextureRegion t = costs[i].getValue().getIcon();
                Vector2 v= new Vector2(pos.x + (48 * i), pos.y-24);
                if(t!=null)
                sb.draw(t,v.x,v.y);
                font.draw(sb,"x"+costs[i].getKey(),v.x,v.y);

            }
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

    public boolean meetsCost(Inventory slots, boolean exactly) {
        boolean[] met = new boolean[]{false, false, false, false, false};
        Pair<Integer, Item>[] costs = getCosts();
        int lm=0;
        for (int i = 0; i < 5; i++) {
            if (costs[i] == null) {
                met[i] = true;
            } else {
                int lim = costs[i].getKey();
                try {
                    int size = slots.getStackSize(costs[i].getValue().getClass());
                    if (size != 0)
                        met[i] = 0 == size % lim;
                } catch (ArithmeticException e) {
                    met[i] = false;
                }
            }
        }
        boolean craftable = true;
        for (int i = 0; i < 5; i++) {
            craftable = craftable && met[i];
        }
        if(craftable){
            for(int i=0;i<5;i++){
                if(costs[i]!=null) {
                    int size = slots.getStackSize(costs[i].getValue().getClass());
                }
                // costs[i].getKey()
            }
        }

        if (exactly) {
            return craftable && slots.getList().size() == getItemLimit();
        } else
            return craftable;
    }

    public Rectangle getRect(Vector2 p) {
       Vector2 v= new Vector2(p.x-34,p.y);
       Vector2 d= getIconDim();
       return new Rectangle(v.x,v.y,d.x,d.y);
    }
}
