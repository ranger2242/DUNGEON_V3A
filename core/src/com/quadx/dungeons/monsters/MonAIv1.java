package com.quadx.dungeons.monsters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.quadx.dungeons.states.mapstate.MapStateRender;

import java.util.ArrayList;

import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.GridManager.monsterList;

/**
 * Created by Chris Cavazos on 6/28/2016.
 */
public class MonAIv1 {
    class Points{
        int x;
        int y;
        public Points(double a,double b){
            x= (int) a;
            y= (int) b;
        }
        public int[] getPos(){
            return new int[]{x,y};
        }
    }
    public int[] agro(Monster m,int x,int y){
        //If the monster was hit
        /*
        int[] pos={x,y};
        if (!m.checkForDamageToPlayer(player.getX(), player.getY())) {//if player was not found
            if (m.willCircle) {                                                         //If mon will circle player
                m.moveSpeed = m.moveSpeedMax;
                pos =circle(m);
            } else {
                if (m.hp > m.hpMax / 4) {                                                   //if not fleeing from damage
                    pos= moveToPoint(m, player.getX(), player.getY());;
                } else {//if monster is fleeing
                    pos = flee(m, pos[0], pos[1]);
                    if (m.healCount > 15) {                                                  //begin hp regen
                        m.hp++;
                        m.healCount = 0;
                    }
                    m.healCount++;
                }
            }
        }*/
        return  new int[2];
    }
    public int[] flee(Monster m, int tx, int ty) {
        m.moveSpeed = m.moveSpeedMax;                                           //set new coordinates based on
        if (player.getX() > m.getX() && m.aa) {
            tx = m.x - 1;
            m.setFront(2);
        }   //players relative position
        else if (player.getX() < m.getX()) {
            tx = m.x + 1;
            m.setFront(3);
        }
        if (player.getY() > m.getY() && m.bb) {
            ty = m.y - 1;
            m.setFront(1);
        } else if (player.getY() < m.getY()) {
            ty = m.y + 1;
            m.setFront(0);
        }
        return new int[]{tx, ty};
    }
    public int[] circle(Monster m){

        float px=player.getX();
        float py=player.getY();
        float mx=m.getX();
        float my=m.getY();
        ArrayList<Points> circleList= new ArrayList<>();
        //calculate circle around player of available spots
        float range= m.getSight();

        for(int i=0;i<360;i++){
            float fx= (float) (range *Math.cos(Math.toRadians(i))+px);
            float fy= (float) (range *Math.sin(Math.toRadians(i))+py);
            circleList.add(new Points(fx,fy));
        }
        if(!m.circling) {
            double dx=(px- mx);
            double dy=(py - my);
            int angle;
            if(dx>0){
                if(dy>0){
                    angle=45;
                }else if(dy==0){
                    angle=0;
                }
                else{
                    angle=315;
                }
            }
            else if(dx==0){
                if(dy>0){
                    angle=90;
                }
                else{
                    angle=270;
                }
            }else{
                if(dy>0){
                    angle=135;
                }
                else if(dy==0){
                    angle=180;
                }else{
                    angle=225;
                }
            }
            try {
                monsterList.get(m.getMonListIndex()).circleAngle = angle;
                monsterList.get(m.getMonListIndex()).circling = true;
            }catch (IndexOutOfBoundsException e){}
        }
        try {

        //move monster incrementally towards nearest circle spot
        int[] f;
        if(m.clockwise) {
            monsterList.get(m.getMonListIndex()).circleAngle += 15;
        }else{
            monsterList.get(m.getMonListIndex()).circleAngle -= 15;
        }
        if(monsterList.get(m.getMonListIndex()).circleAngle<0){
            monsterList.get(m.getMonListIndex()).circleAngle=360-monsterList.get(m.getMonListIndex()).circleAngle;
            monsterList.get(m.getMonListIndex()).circleCount++;
        }
        if(monsterList.get(m.getMonListIndex()).circleAngle>360){
            monsterList.get(m.getMonListIndex()).circleAngle=monsterList.get(m.getMonListIndex()).circleAngle-360;
            monsterList.get(m.getMonListIndex()).circleCount++;
        }
        if(monsterList.get(m.getMonListIndex()).circleAngle==360){
            monsterList.get(m.getMonListIndex()).circleAngle=0;
            monsterList.get(m.getMonListIndex()).circleCount++;
        }
        f = circleList.get(m.circleAngle).getPos();
        if(monsterList.get(m.getMonListIndex()).circleCount<m.circleAgro)
        return moveToPoint(m,f[0],f[1]);
        else
            return  moveToPoint(m,player.getX(),player.getY());
        }catch (IndexOutOfBoundsException e){
            return  moveToPoint(m,player.getX(),player.getY());
        }

    }
    public int[] moveToPoint(Monster m, int x, int y) {
        int tx;
        if (x > m.getX()) {
            tx =m.x + 1;
            m.setFront(3);
        }       //set new coordinates based on
        else if (x== m.getX()) {
            tx = x;
        }        //players relative position
        else {
            tx = m.x - 1;
            m.setFront(2);
        }
        int ty;
        if (y > m.getY()) {
            ty = m.y + 1;
            m.setFront(0);
        } else if (y== m.getY()) {
            ty = y;
        } else {
            ty = m.y - 1;
            m.setFront(1);
        }
        return new int[]{tx, ty};
    }
    public static void callForHelp(Monster m){
        if(m.hp<(m.hpMax/4) && m.caller && m.callRadius<6){
            MapStateRender.drawCircle(m.getPX(),m.getPY(),m.callRadius, Color.WHITE);
            monsterList.get(m.getMonListIndex()).callRadius+= Gdx.graphics.getDeltaTime();
        }
       // else monsterList.get(m.getMonListIndex()).callRadius=0;
    }
}
