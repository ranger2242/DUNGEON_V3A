package com.quadx.dungeons.attacks;

import com.badlogic.gdx.graphics.Color;
import com.quadx.dungeons.tools.ImageLoader;
import com.quadx.dungeons.tools.gui.HoverText;

import static com.quadx.dungeons.Game.player;

/**
 * Created by Chris Cavazos on 6/21/2016.
 */
public class Focus extends Attack {
    public Focus()  {
        costGold=2510;
        type=CostType.Energy;
        powerA = new int[]{0,0,0,0,0};
        costA =new int[]{20,30,60,100,120};
        name="Focus";
        power=0;
        cost=0;
        mod=9;
        description="Player focuses E and restores M.";
        spread=0;
        range=0;
        hitBoxShape =HitBoxShape.None;
        loadArray();
        setIcon(ImageLoader.attacks.get(3));
    }
    public void runAttackMod(){
        int d=(cost*3)/4;
        player.st.addMana(d);
        Color c=new Color(.2f,.2f,1,1);
        new HoverText("+"+d,c ,player.fixed(),false);
    }
}
