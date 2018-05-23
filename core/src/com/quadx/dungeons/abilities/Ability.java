package com.quadx.dungeons.abilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.quadx.dungeons.Game.equipSets;
import static com.quadx.dungeons.Game.player;

/**
 * Created by Chris Cavazos on 5/20/2016.
 */
@SuppressWarnings("DefaultFileTemplate")
public abstract class Ability {
    protected Texture icon;
    protected String name = "default";
    protected final ArrayList<String> details = new ArrayList<>();

    int[] upCost = {0, 2, 4, 4, 4};
    static boolean enabled = false;
    static float cooldown = 0;
    static float timeCounter = 0;
    private boolean maxed= false;
    int level = 0;

    Ability() {
    }

    public void setDetails(ArrayList<String> d) {
        details.clear();
        details.addAll(d);
    }

    Texture loadIcon(String s) {
        icon = new Texture(Gdx.files.internal(s));
        return icon;
    }

    public Texture getIcon() {
        return icon;
    }

    public abstract void onActivate();

    public abstract int getMod();

    public abstract void l1();

    public abstract void l2();

    public abstract void l3();

    public abstract void l4();

    public abstract void l5();

    public static void updateTimeCounter() {
        timeCounter += Gdx.graphics.getDeltaTime();
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public abstract String getName();

    void levelUp() {
        if(!maxed) {
            level++;
            int len = upCost.length;
            if(level >= len)
                maxed=true;
        }
    }

    public void upgrade() {
        if (!maxed) {
            int cost = upCost[level];

            if (player.getAbilityPoints() >= cost) {
                player.setAbilityPoints(-cost);
                levelUp();
                switch (level) {
                    case 2: {
                        l2();
                        break;
                    }
                    case 3: {
                        l3();
                        break;
                    }
                    case 4: {
                        l4();
                        break;
                    }
                    case 5: {
                        l5();
                        break;
                    }
                }
            }
        }

    }

    public int getLevel() {
        return level;
    }

    DecimalFormat df = new DecimalFormat("0.0");
    private String f(String s){
        return "x" +df.format(Float.parseFloat(s));
    }


    public ArrayList<String> details() {
        ArrayList<String> output = new ArrayList<>();
        int ind = getMod();
        ArrayList<String> list = details;
        if(list == null)
            list = new ArrayList<>();
            String[] s = list.get((level + 2)).split(",");
        if(s.length>=10) {

            output.add(list.get(0) + " " + (level + 1));
            output.add(list.get(1));
            output.add(f(s[0]) + " | " + f(s[1]));
            output.add(f(s[2]) + " | " + f(s[3]));
            output.add(f(s[4]) + " | " + f(s[5]));
            output.add(f(s[6]));
            output.add(f(s[7]));
            output.add(f(s[8]));
            output.add(f(s[9]));
            output.add("+" + equipSets.ref[ind].get(level).getName());
            output.add("Upgrade cost: " + upCost[level] + " AP");
        }
        return output;
    }
}
