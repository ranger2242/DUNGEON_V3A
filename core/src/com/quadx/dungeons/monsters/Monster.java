package com.quadx.dungeons.monsters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.quadx.dungeons.Cell;
import com.quadx.dungeons.Damage;
import com.quadx.dungeons.GridManager;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.states.mapstate.MapStateRender;

import java.util.Random;

import static com.quadx.dungeons.Game.player;

/**
 * Created by Tom on 11/10/2015.
 */
@SuppressWarnings("ALL")
public class Monster {
    public static Texture[] icons={  new Texture(Gdx.files.internal("images/icons/monsters/test/testBack.png")),
                                     new Texture(Gdx.files.internal("images/icons/monsters/test/testRight.png")),
                                     new Texture(Gdx.files.internal("images/icons/monsters/test/testFront.png")),
                                     new Texture(Gdx.files.internal("images/icons/monsters/test/testLeft.png"))};
    private static Random rn;
    protected double level = 1;
    public double attack;
    public double intel;
    public String name = "monster";
    private boolean hit = false;
    private boolean moved = false;
    private Texture icon = null;
    private int front = 0;
    int oldFront =0;
    double power = 20;
    double hpBase = 60;
    double attBase = 40;
    double defBase = 40;
    double intBase = 40;
    double spdBase = 40;
    float moveSpeedMin = .12f;
    private float moveSpeedMax = .09f;
    private int liveCellIndex = -1;
    private int monListIndex = -1;
    private int healCount = 0;
    String status = "0";
    int sight = 6;
    private boolean aa = false;
    private boolean bb = false;
    private Damage d = new Damage();
    private int x;
    private int px;
    private int y;
    private int py;
    private double hp = 0;
    private double hpMax = 0;
    private double hpsoft = 0;
    private double defense;
    private double speed;
    private float dtMove = 0;
    private float moveSpeed = .15f;

    public Monster() {
        rn = new Random();
        genLevel();
        genStats();
        loadIcon();
    }

    private void loadIcon() {
        icon=icons[front];
    }

    public int getLiveCellIndex() {
        return liveCellIndex;
    }

    public void setLiveCellIndex(int i) {
        liveCellIndex = i;
    }

    public int getMonListIndex() {
        return monListIndex;
    }

    public void setMonListIndex(int i) {
        monListIndex = i;
    }

    public void updateVariables(float dt) {
        dtMove += dt;
        aa = rn.nextBoolean();
        bb = rn.nextBoolean();
    }

    public float getdtMove() {
        return dtMove;
    }

    public float getMoveSpeed() {
        return moveSpeed;
    }

    public String getName() {
        return name;
    }

    public void stattest() {
        for (int i = 0; i < 100; i++) {
            sayStats();
        }
    }

    private void genLevel() {
        level = player.level + rn.nextInt(player.floor);
        //out(level+"");
    }

    private void genStats() {
        // System.out.println("---------------------------------------");
        genHp();
        genAttack();
        genDefense();
        genIntel();
        genSpeed();
    }

    private void genAttack() {
        double a = attBase + rn.nextInt(31);
        double b = Math.sqrt(rn.nextInt(65535)) / 4;
        attack = (((a * 2 + b) * level) / 100) + 5;
        //System.out.println("Attack :"+attack);
    }

    private void genDefense() {
        double a = defBase + rn.nextInt(31);
        double b = Math.sqrt(rn.nextInt(65535)) / 4;
        defense = (((a * 2 + b) * level) / 100) + 5;
        //System.out.println("Defense :"+defense);
    }

    private void genSpeed() {
        double a = spdBase + rn.nextInt(31);
        double b = Math.sqrt(rn.nextInt(65535)) / 4;
        speed = (((a * 2 + b) * level) / 100) + 5;
        //System.out.println("Speed :"+speed);
    }

    private void genIntel() {
        double a = intBase + rn.nextInt(31);
        double b = Math.sqrt(rn.nextInt(65535)) / 4;
        intel = (((a * 2 + b) * level) / 100) + 5;
        //System.out.println("Intel :"+intel);
    }

    private void genHp() {
        double a = hpBase + rn.nextInt(31);
        double b = Math.sqrt(rn.nextInt(65535)) / 4;
        hp = (((a * 2 + b) * level) / 100) + level + 10;
        hpMax = hp;
        hpsoft = hp;
    }

    public double getHp() {
        return hp;
    }

    public double getHpMax() {
        return hpsoft;
    }

    public double getDefense() {
        return defense;
    }

    public double getIntel() {
        return intel;
    }

    public int getLevel() {
        return (int) level;
    }

    public void setLevel(int l) {
        level = l;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getPX() {
        return px;
    }

    public int getPY() {
        return py;
    }

    public Texture getIcon() {
        return icon;
    }

    public void setCords(int a, int b) {
        x = a;
        y = b;
        px = a * MapState.cellW;
        py = b * MapState.cellW;
    }

    public void setCordsPX(int a, int b) {
        x = a / MapState.cellW;
        y = b / MapState.cellW;
        px = a;
        py = b;
    }

    public double getIntelDamage() {
        double damage = d.monsterMagicDamage(player, this, (int) power);
        return damage;
    }

    public void takeAttackDamage(double i) {
        hp = hp - (int) i;
        if (hp < 0) {
            hp = 0;
        }
    }

    private void sayStats() {
        System.out.println("\nHP: " + (int) hp);
        System.out.println("Level: " + level);
        System.out.println("openAttackMenu: " + (int) attack);
        System.out.println("Defense: " + (int) defense);
        System.out.println("Intel: " + (int) intel);
        System.out.println("Speed: " + (int) speed);
    }

    public void setHit() {
        hit = true;
    }

    public void move() {
        int a = rn.nextInt(3) + 1;
        int b = rn.nextInt(3) + 1;
        int tx = x;
        int ty = y;
        if (hit) {                                                                       //If the monster was hit
            if (!checkForDamageToPlayer(player.getX(), player.getY())) {                 //if player was not found
                if (hp > hpMax / 4) {                                                   //if not fleeing from damage
                    int[] arr = chasePlayer();
                    tx = arr[0];
                    ty = arr[1];
                } else {                                                                //if monster is fleeing
                    int[] arr = flee(tx, ty);
                    tx = arr[0];
                    ty = arr[1];
                    if (healCount > 15) {                                                  //begin hp regen
                        hp++;
                        healCount = 0;
                    }
                    healCount++;
                }
            }
        } else {                                                                          //if monster has not been hit
            if (!checkForDamageToPlayer(player.getX(), player.getY())) {                  //if not hitting player
                if (PlayerInSight()) {
                    int[] arr = chasePlayer();
                    tx = arr[0];
                    ty = arr[1];
                    MapState.gm.clearArea(tx, ty, false);
                } else {
                    switch (a) {
                        case (1): {
                            tx += rn.nextInt(2);
                            setFront(1);
                            break;
                        }
                        case (2): {
                            tx -= rn.nextInt(2);
                            setFront(3);
                            break;
                        }
                        case (3): {
                            break;
                        }
                    }
                    switch (b) {
                        case (1): {
                            ty += rn.nextInt(2);
                            setFront(0);
                            break;
                        }
                        case (2): {
                            ty -= rn.nextInt(2);
                            setFront(2);
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
        for (Monster c : GridManager.monsterList) {//checks if there is monster already on chosen cell
            if (c.getX() == tx && c.getY() == ty) {
                    cont = true;
            }
        }
        if (!cont) {    //if there is no monster
            for (Cell c1 : GridManager.liveCellList) { //search list for cell
                //if (c1.getX() == this.x && c1.getY() == this.y) {//if cell position matches monster
                if (c1.getMonsterIndex() == monListIndex) {
                    c1.setMon(false);                           //remove
                }
                if (c1.getX() == tx && c1.getY() == ty && c1.getState()) {
                    c1.setMonsterIndex(monListIndex);
                    c1.setMon(true);
                    setCords(tx, ty);
                    placed = true;
                    moved = true;

                }
            }
            if (!placed) {//if it didnt move because wall use dig
                setCords(x, y);
                for (Cell c : GridManager.liveCellList) {
                    if (c.getX() == x && c.getY() == y) {
                        c.setMon(true);
                    }
                }
                if (rn.nextBoolean() || rn.nextBoolean()) MapState.gm.clearArea(x, y, false);
            }
        }
        dtMove = 0;
    }
    void setFront(int x){
        oldFront=front;
        front=x;
        if(oldFront !=front){
            loadIcon();
        }
    }

    private int[] chasePlayer() {
        int tx;
        if (player.getX() > this.getX()) {
            tx = this.x + 1;
            setFront(1);
        }       //set new coordinates based on
        else if (player.getX() == this.getX()) {
            tx = player.getX();
        }        //players relative position
        else {
            tx = this.x - 1;
            setFront(3);
        }
        int ty;
        if (player.getY() > this.getY()) {
            ty = this.y + 1;
            setFront(0);
        } else if (player.getY() == this.getY()) {
            ty = player.getY();
        } else {
            ty = this.y - 1;
            setFront(2);
        }
        return new int[]{tx, ty};
    }

    private int[] flee(int tx, int ty) {
        moveSpeed = moveSpeedMax;                                           //set new coordinates based on
        if (player.getX() > this.getX() && aa) {
            tx = this.x - 1;
            setFront(3);
        }   //players relative position
        else if (player.getX() < this.getX()) {
            tx = this.x + 1;
            setFront(1);
        }
        if (player.getY() > this.getY() && bb) {
            ty = this.y - 1;
            setFront(2);
        } else if (player.getY() < this.getY()) {
            ty = this.y + 1;
            setFront(0);
        }
        return new int[]{tx, ty};
    }

    private boolean PlayerInSight() {
        return player.getX() > this.getX() - this.getSight() && player.getX() < this.getX() + this.getSight()
                && player.getY() > this.getY() - this.getSight() && player.getY() < this.getY() + this.getSight();
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean checkForDamageToPlayer(int px, int py) {
        boolean hit = false;
        if ((x == px && y == py) || (x + 1 == px && y == py) || (x - 1 == px && y == py)    //check surrounding tiles for player
                || (x == px && y + 1 == py) || (x == px && y - 1 == py)) {                  //hurt if found
            int d = Damage.monsterPhysicalDamage(player, this, (int) power);
            player.setHp(player.getHp() - d);                                     //apply damage
            MapStateRender.setHoverText("-" + d, 1, Color.RED, player.getPX(), player.getPY(), true);
            hit = true;
        }
        return hit;
    }

    public boolean isHit() {
        return hit;
    }

    public void setMoved() {
        moved = true;
    }

    public boolean isMoved() {
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
