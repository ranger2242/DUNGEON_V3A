package com.quadx.dungeons.monsters;

import com.badlogic.gdx.graphics.Color;
import com.quadx.dungeons.*;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.states.mapstate.MapStateRender;

import java.util.Random;

/**
 * Created by Tom on 11/10/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Monster {
    private Damage d = new Damage();
    private int x;
    private int px;
    private int y;
    private int py;
    private static Random rn;
    private static double level=1;
    double power=20;
    private double hp=0;
    private double hpsoft=0;
    public double acc=1;
    public double attack;
    private double defense;
    public double intel;
    private double speed;
    double hpBase =30;
    double attBase =30;
    double defBase =30;
    double intBase =30;
    double spdBase =30;
    double hpMod=0;
    double attackMod=0;
    double defenseMod=1;
    double intelMod=1;
    double speedMod=0;
    String status="0";
    int sight=6;
    private double damage;
    public String name= "monster";

    public Monster(){
        rn = new Random();
        genLevel(Game.player.level+Game.player.floor);
        genStats();
    }
    public String getName(){return name;}
    public void stattest() {
        for(int i=0; i<100;i++)
        {
            sayStats();
        }
    }
    private void genLevel(int i) {
        level=rn.nextInt(i)+2;
        while(level<=0) {
            System.out.println("$$"+level);
            level = rn.nextGaussian() * i;
                    System.out.println(level);
        }
        System.out.println("###$");
        if(level<1)
        {level=1;}
        System.out.println("Level "+level);
    }
    private void genStats(){
        System.out.println("---------------------------------------");
        genHp();
        genAttack();
        genDefense();
        genIntel();
        genSpeed();
    }
    public void genAttack() {
        double a= attBase +rn.nextInt(31);
        double b=Math.sqrt(rn.nextInt(65535))/4;
        attack=(((a*2+b)*level)/100)+5;
        System.out.println("Attack :"+attack);
    }
    public void genDefense() {
        double a= defBase +rn.nextInt(31);
        double b=Math.sqrt(rn.nextInt(65535))/4;
        defense=(((a*2+b)*level)/100)+5;
        System.out.println("Defense :"+defense);
    }
    public void genSpeed() {
        double a= spdBase +rn.nextInt(31);
        double b=Math.sqrt(rn.nextInt(65535))/4;
        speed=(((a*2+b)*level)/100)+5;
        System.out.println("Speed :"+speed);
    }
    public void genIntel() {
        double a= intBase +rn.nextInt(31);
        double b=Math.sqrt(rn.nextInt(65535))/4;
        intel=(((a*2+b)*level)/100)+5;
        System.out.println("Intel :"+intel);
    }
    public void genHp(){
        double a= hpBase +rn.nextInt(31);
        double b=Math.sqrt(rn.nextInt(65535))/4;
        hp=(((a*2+b)*level)/100)+level+10;
        System.out.println("HP :"+hp);
        hpsoft=hp;
    }
    public double getHp()
    {
        return hp;
    }
    public double getHpMax()
    {
        return hpsoft;
    }
    public double getDefense() {return defense;}
    public double getIntel()
    {
        return intel;
    }
    public int    getLevel() {return (int)level;}
    public int    getX()
    {
        return x;
    }
    public int    getY()
    {
        return y;
    }
    public int    getPX()
    {
        return px;
    }
    public int    getPY()
    {
        return py;
    }
    public void  setCords(int a, int b) {
        x=a;
        y=b;
        px=a*MapState.cellW;
        py=b*MapState.cellW;
    }
    public void setCordsPX(int a,int b){
        x=a/MapState.cellW;
        y=b/MapState.cellW;
        px=a;py=b;
    }
    public void setLevel(int l){level=l;}
    public double getAttackDamage() {
        damage= Damage.monsterPhysicalDamage(Game.player, this, (int)power);
        return damage;
    }
    public double getIntelDamage(){
        damage=d.monsterMagicDamage(Game.player,this,(int)power);
        return damage;
    }
    public void takeAttackDamage(double i) {
        hp=hp-(int)i;
        if(hp<0)
        {
            hp=0;
        }
    }
    private void sayStats() {
        System.out.println("\nHP: "+(int)hp);
        System.out.println("Level: "+level);
        System.out.println("openAttackMenu: "+(int)attack);
        System.out.println("Defense: "+(int)defense);
        System.out.println("Intel: "+(int)intel);
        System.out.println("Speed: "+(int)speed);
    }

    public void move() {
        int a=rn.nextInt(3)+1;
        int b=rn.nextInt(3)+1;
        int tx=x;
        int ty=y;
        if((x==Game.player.getX() && y==Game.player.getY() )||
            (x+1==Game.player.getX() && y==Game.player.getY()) ||
            (x-1==Game.player.getX() && y==Game.player.getY()) ||
            (x==Game.player.getX() && y+1==Game.player.getY()) ||
            (x==Game.player.getX() && y-1==Game.player.getY()) ) {
            int d= Damage.monsterPhysicalDamage(Game.player,this,(int)power);
            Game.player.setHp(Game.player.getHp()- d);
            MapStateRender.setHoverText("-"+d,1, Color.RED, Game.player.getPX(),Game.player.getPY(),true);
        }
        else{
            if (Game.player.getX() > this.getX() - this.getSight() && Game.player.getX() < this.getX() + this.getSight()
                    && Game.player.getY() > this.getY() - this.getSight() && Game.player.getY() < this.getY() + this.getSight()) {

                if (Game.player.getX() > this.getX()) {tx = this.x + 1;}
                else if (Game.player.getX() == this.getX()) {tx=Game.player.getX();}
                else {tx = this.x - 1;}
                if (Game.player.getY() > this.getY()) {ty = this.y + 1;}
                else if (Game.player.getY() == this.getY()) {ty=Game.player.getY();}
                else {ty = this.y - 1;}
                MapState.gm.clearArea(tx, ty, false);
            }
            else {
                switch (a) {
                    case (1): {tx += rn.nextInt(2);break;}
                    case (2): {tx -= rn.nextInt(2);break;}
                    case (3): {break;}
                }
                switch (b) {
                    case (1): {ty += rn.nextInt(2);break;}
                    case (2): {ty -= rn.nextInt(2);break;}
                    case (3): {break;}
                }
            }
        }
        boolean placed = false;
        boolean cont=false;
        for(Cell c :GridManager.liveCellList){//checks if there is monster already on chosen cell
            if(c.getX()==tx && c.getY() ==ty){
                if(c.hasMon())
                    cont=true;
            }
        }
        if(!cont) {
            for (Cell c1 : GridManager.liveCellList) {
                if (c1.getX() == x && c1.getY() == y) {
                    c1.setMon(false);
                }
                if (c1.getX() == tx && c1.getY() == ty) {
                    c1.setMon(true);
                    setCords(tx, ty);
                    placed = true;
                }
            }
            if (!placed) {
                setCords(x, y);
                for (Cell c : GridManager.liveCellList) {
                    if (c.getX() == x && c.getY() == y) {
                        c.setMon(true);
                    }
                }
                if(rn.nextBoolean()) MapState.gm.clearArea(x,y,false);
            }
        }
    }

    public double getAttack() {
        return attack;
    }

    public double getSpeed() {
        return speed;
    }

    public int getSight() {
        return sight;
    }
}
