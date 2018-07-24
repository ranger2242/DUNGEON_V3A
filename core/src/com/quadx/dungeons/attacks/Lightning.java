package com.quadx.dungeons.attacks;

import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.shapes1_5.Line;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.GridManager.monsterList;

/**
 * Created by Chris Cavazos on 6/25/2016.
 */
public class Lightning extends Attack {
    boolean small=false;
    public Lightning(boolean small){
        costGold=30000;
        type=CostType.Mana;
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
        loadArray();
        this.small=small;
        gINIT(2,"icLightning");

    }

    @Override
    public void runAttackMod() {

    }

    public ArrayList<Line> getHitChainList(){
        ArrayList<Line> edges=new ArrayList<>();
        ArrayList<Monster> hit=new ArrayList<>();
        int range= small? 8: 10;
        try{
        for(Monster m:monsterList){
            if(player.pos().dst(m.pos())<range){
                hit.add(m);
                edges.add(new Line(player.fixed(), m.fixed()));
                m.takeDamage();
            }
        }}catch (ConcurrentModificationException ignored){}
        try{
        for(Monster m:hit){
            for(Monster m1:monsterList) {
                if (!m.equals(m1) && m.pos().dst(m1.pos()) < range) {
                    edges.add(new Line(m.fixed(),m1.fixed()));
                    m1.takeDamage();
                }
            }
        }}catch (ConcurrentModificationException ignored){}
        return edges;
    }
}
