package com.quadx.dungeons;

import com.quadx.dungeons.attacks.Attack;
import com.quadx.dungeons.items.Item;

/**
 * Created by Tom on 1/4/2016.
 */
public class QButton {
    public static int px,py,width,height;
    Item item;
    Attack attack;
    String name="";
    public QButton(){}
    public QButton(int x, int y, int w, int h){
        px=x;
        py=y;
        width=w;
        height=h;
    }
    public void setItem(Item it){
        item=it;
    }
    public void setAttack(Attack at){
        attack=at;
    }
    public void setCordsPx(int a,int b){
        px=a;
        py=b;
    }
    public int getPx(){
        return px;
    }
    public int getPy(){
        return py;
    }
    public int getHeight(){
        return height;
    }
    public int getWidth(){
        return width;
    }
    public Item getItem(){return item;}
    public Attack getAttack(){return attack;}
    public String getName(){
        if(item!=null){
            name=item.getName();
        }
        if(attack!=null){
            name=attack.getName();
        }
        return name;
    }
}
