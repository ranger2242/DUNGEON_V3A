package com.quadx.dungeons.monsters;

import com.quadx.dungeons.*;
import com.quadx.dungeons.states.mapstate.MapState;

import java.util.Random;

/**
 * Created by Tom on 11/10/2015.
 */
public class Monster {
    Damage d = new Damage();
    int x,px;
    int y,py;
    static Random rn;
    static double level=1;
    static int levelmin;
    static int levelmax;
    double power=40;
    double hp=0;
    double hpsoft=0;
    public double acc=1;
    public double attack;
    public double defense;
    public double intel;
    public double speed;
    double hpBase =30;
    double attBase =50;
    double defBase =50;
    double intBase =50;
    double spdBase =50;
    double hpMod=0;
    double attackMod=0;
    double defenseMod=1;
    double intelMod=1;
    double speedMod=0;
    String status="0";
    int sight=4;
    public double damage;
    static int playerlevel;
    boolean agro=false;
    public String name= "monster";

    public Monster(){
        System.out.println("=++");
        rn = new Random();
        genLevel(Game.player.level);
        System.out.println("@#@#@");
        genStats();
        //System.out.println("@#@#@");

    }
    public String getName(){return name;}
    public void stattest() {
        for(int i=0; i<100;i++)
        {
            sayStats();
        }
    }
    public void genLevel(int i) {
        playerlevel=i;
        levelmin=i;
        levelmax=i+3;
        level=rn.nextGaussian()*i;
        while(level<=0) {
            System.out.println("$$"+level);
            level = rn.nextGaussian() * i;
                    System.out.println(level);
        };
        System.out.println("###$");
        if(level<1)
        {level=1;}
        System.out.println("Level "+level);
    }
    public void genStats(){
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
    public boolean getAgro(){return agro;}
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
        damage=d.monsterPhysicalDamage(Game.player, this, (int)power);
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
    public void sayStats() {
        System.out.println("\nHP: "+(int)hp);
        System.out.println("Level: "+level);
        System.out.println("openAttackMenu: "+(int)attack);
        System.out.println("Defense: "+(int)defense);
        System.out.println("Intel: "+(int)intel);
        System.out.println("Speed: "+(int)speed);
    }
    public int monsterAttack() {
        int attackswitch =0;
        if (attackswitch==0)
        {
            getAttackDamage();
        }
        if (attackswitch==1)
        {
            getIntelDamage();
        }
        return (int)damage;
    }
    public void move() {
        int a=rn.nextInt(3)+1;
        int b=rn.nextInt(3)+1;
        int tx=x;
        int ty=y;
        //MapState.out(x+"  "+y);

        //check if player is around
        if(Game.player.getX()>this.getX()-this.getSight() && Game.player.getX()<this.getX()+this.getSight()
                &&Game.player.getY()>this.getY()-this.getSight() && Game.player.getY()<this.getY()+this.getSight()){
            //MapState.out(x+"  "+y);
            if(Game.player.getX()>this.getX()){

                tx=this.x+1;
            }
            else if(Game.player.getX()==this.getX()){

            }
            else{
                tx=this.x-1;
            }
            if(Game.player.getY()>this.getY()){
                ty=this.y+1;
            }
            else if(Game.player.getY()==this.getY()){

            }
            else{
                ty=this.y-1;
            }
            MapState.gm.clearArea(tx,ty,false);

        }
        else {
            switch (a) {
                case (1): {
                    tx += rn.nextInt(2);
                    break;
                }
                case (2): {
                    tx -= rn.nextInt(2);
                    break;
                }
                case (3): {
                    break;
                }
            }
            switch (b) {
                case (1): {
                    ty += rn.nextInt(2);
                    break;
                }
                case (2): {
                    ty -= rn.nextInt(2);
                    break;
                }
                case (3): {
                    break;
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
