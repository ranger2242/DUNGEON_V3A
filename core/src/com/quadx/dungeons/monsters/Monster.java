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
    protected boolean hit= false;
    protected boolean moved= false;
    private Damage d = new Damage();
    private int x;
    private int px;
    private int y;
    private int py;
    private static Random rn;
    private static double level=1;
    double power=20;
    private double hp=0;
    private double hpMax=0;
    private double hpsoft=0;
    public double acc=1;
    public double attack;
    private double defense;
    public double intel;
    private double speed;
    double hpBase =60;
    double attBase =40;
    double defBase =40;
    double intBase =40;
    double spdBase =40;
    double hpMod=0;
    double attackMod=0;
    double defenseMod=1;
    double intelMod=1;
    double speedMod=0;
    private float dtMove=0;
    private float moveSpeed=.12f;
    float moveSpeedMin=.12f;
    float moveSpeedMax=.06f;
    int liveCellIndex=-1;
    int monListIndex=-1;
    String status="0";
    int sight=6;
    private double damage;
    public String name= "monster";
    boolean aa=false;
    boolean bb=false;

    public Monster(){
        rn = new Random();
        genLevel();
        genStats();
    }
    public int getLiveCellIndex(){
        return  liveCellIndex;
    }
    public int getMonListIndex(){
        return monListIndex;
    }
    public void setLiveCellIndex(int i){
        liveCellIndex=i;
    }
    public void setMonListIndex(int i){
        monListIndex=i;
    }
    public void updateVariables(float dt){
        dtMove+=dt;
        aa=rn.nextBoolean();
        bb=rn.nextBoolean();
    }
    public float getdtMove(){
        return dtMove;
    }
    public float getMoveSpeed(){
        return moveSpeed;
    }
    public String getName(){return name;}
    public void stattest() {
        for(int i=0; i<100;i++)
        {
            sayStats();
        }
    }
    private void genLevel() {
        level=Game.player.level+rn.nextInt(Game.player.floor);
    }
    private void genStats(){
       // System.out.println("---------------------------------------");
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
        //System.out.println("Attack :"+attack);
    }
    public void genDefense() {
        double a= defBase +rn.nextInt(31);
        double b=Math.sqrt(rn.nextInt(65535))/4;
        defense=(((a*2+b)*level)/100)+5;
        //System.out.println("Defense :"+defense);
    }
    public void genSpeed() {
        double a= spdBase +rn.nextInt(31);
        double b=Math.sqrt(rn.nextInt(65535))/4;
        speed=(((a*2+b)*level)/100)+5;
        //System.out.println("Speed :"+speed);
    }
    public void genIntel() {
        double a= intBase +rn.nextInt(31);
        double b=Math.sqrt(rn.nextInt(65535))/4;
        intel=(((a*2+b)*level)/100)+5;
        //System.out.println("Intel :"+intel);
    }
    public void genHp(){
        double a= hpBase +rn.nextInt(31);
        double b=Math.sqrt(rn.nextInt(65535))/4;
        hp=(((a*2+b)*level)/100)+level+10;
        hpMax=hp;
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
    public void setHit(){
        hit=true;
    }
    public void move() {
        int a=rn.nextInt(3)+1;
        int b=rn.nextInt(3)+1;
        int tx=x;
        int ty=y;
        if(hit) {
            if ((x == Game.player.getX() && y == Game.player.getY()) ||
                    (x + 1 == Game.player.getX() && y == Game.player.getY()) ||
                    (x - 1 == Game.player.getX() && y == Game.player.getY()) ||
                    (x == Game.player.getX() && y + 1 == Game.player.getY()) ||
                    (x == Game.player.getX() && y - 1 == Game.player.getY())) {
                int d = Damage.monsterPhysicalDamage(Game.player, this, (int) power);
                Game.player.setHp(Game.player.getHp() - d);
                MapStateRender.setHoverText("-" + d, 1, Color.RED, Game.player.getPX(), Game.player.getPY(), true);
            } else {
                if (hp > hpMax / 4) {
                    if (Game.player.getX() > this.getX()) {
                        tx = this.x + 1;
                    } else if (Game.player.getX() == this.getX()) {
                        tx = Game.player.getX();
                    } else {
                        tx = this.x - 1;
                    }
                    if (Game.player.getY() > this.getY()) {
                        ty = this.y + 1;
                    } else if (Game.player.getY() == this.getY()) {
                        ty = Game.player.getY();
                    } else {
                        ty = this.y - 1;
                    }
                } else {
                    moveSpeed = moveSpeedMax;


                    if (Game.player.getX() > this.getX() &&aa) {
                        tx = this.x - 1;
                    } else {
                        tx = this.x + 1;
                    }
                    if (Game.player.getY() > this.getY() &&bb) {
                        ty = this.y - 1;
                    } else {
                        ty = this.y + 1;
                    }
                }
            }
        }
        else {
            if ((x == Game.player.getX() && y == Game.player.getY()) ||
                    (x + 1 == Game.player.getX() && y == Game.player.getY()) ||
                    (x - 1 == Game.player.getX() && y == Game.player.getY()) ||
                    (x == Game.player.getX() && y + 1 == Game.player.getY()) ||
                    (x == Game.player.getX() && y - 1 == Game.player.getY())) {
                int d = Damage.monsterPhysicalDamage(Game.player, this, (int) power);
                Game.player.setHp(Game.player.getHp() - d);
                MapStateRender.setHoverText("-" + d, 1, Color.RED, Game.player.getPX(), Game.player.getPY(), true);
            } else {
                if (Game.player.getX() > this.getX() - this.getSight() && Game.player.getX() < this.getX() + this.getSight()
                        && Game.player.getY() > this.getY() - this.getSight() && Game.player.getY() < this.getY() + this.getSight()) {

                    if (Game.player.getX() > this.getX()) {
                        tx = this.x + 1;
                    } else if (Game.player.getX() == this.getX()) {
                        tx = Game.player.getX();
                    } else {
                        tx = this.x - 1;
                    }
                    if (Game.player.getY() > this.getY()) {
                        ty = this.y + 1;
                    } else if (Game.player.getY() == this.getY()) {
                        ty = Game.player.getY();
                    } else {
                        ty = this.y - 1;
                    }
                    MapState.gm.clearArea(tx, ty, false);
                } else {
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

            }
        }
            boolean placed = false;
            boolean cont = false;
            for (Cell c : GridManager.liveCellList) {//checks if there is monster already on chosen cell
                if (c.getX() == tx && c.getY() == ty) {
                    if (c.hasMon())
                        cont = true;
                }
            }
            if (!cont) {    //if there is no monster
                for (Cell c1 : GridManager.liveCellList) { //search list for cell
                    //if (c1.getX() == this.x && c1.getY() == this.y) {//if cell position matches monster
                    if(c1.getMonsterIndex()==monListIndex){
                        c1.setMon(false);                           //remove
                    }
                    if (c1.getX() == tx && c1.getY() == ty) {
                        c1.setMonsterIndex(monListIndex);
                        c1.setMon(true);
                        setCords(tx, ty);
                        placed = true;
                        moved=true;

                    }
                }
                if (!placed) {//if it didnt move because wall use dig
                    setCords(x, y);
                    for (Cell c : GridManager.liveCellList) {
                        if (c.getX() == x && c.getY() == y) {
                            c.setMon(true);
                        }
                    }
                    if (rn.nextBoolean()) MapState.gm.clearArea(x, y, false);
                }
            }
        dtMove=0;
        }

    public boolean isHit(){
        return hit;
    }
    public void setMoved(){
        moved=true;
    }
    public boolean isMoved(){
        return moved;
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
