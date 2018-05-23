package com.quadx.dungeons.attacks;

import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.tools.ImageLoader;
import com.quadx.dungeons.tools.shapes.Line;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.GridManager.monsterList;

/**
 * Created by Chris Cavazos on 6/25/2016.
 */
public class Lightning extends Attack {
    public Lightning(){
        costGold=30000;
        type=2;
        hitBoxShape =HitBoxShape.Chain;
        int a=0;
        powerA = new int[]{50,75,100,130,150};
        costA =new int[]{40,65,75,100,120};
        //costA =new int[]{0,0,0,0,0};

        name="Lightning";
        power=50;
        cost=a;
        mod=10;
        description="Summons lightning.";
        spread=0;
        range=0;
        setIcon(ImageLoader.attacks.get(12));
    }
    ArrayList<Line> getHitChainList(){
        ArrayList<Line> edges=new ArrayList<>();
        ArrayList<Monster> hit=new ArrayList<>();
        try{
        for(Monster m:monsterList){
            if(player.pos().dst(m.getPos())<10){
                hit.add(m);
                edges.add(new Line(player.getFixPos(), m.getFixPos()));
                m.takeDamage();
            }
        }}catch (ConcurrentModificationException ignored){}
        try{
        for(Monster m:hit){
            for(Monster m1:monsterList) {
                if (!m.equals(m1) && m.getPos().dst(m1.getPos()) < 10) {
                    edges.add(new Line(m.getFixPos(),m1.getFixPos()));
                    m1.takeDamage();
                }
            }
        }}catch (ConcurrentModificationException ignored){}
        return edges;
    }
}
