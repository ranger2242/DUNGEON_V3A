package com.quadx.dungeons.monsters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.quadx.dungeons.Damage;
import com.quadx.dungeons.attacks.Attack;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.states.mapstate.MapStateRender;
import com.quadx.dungeons.states.mapstate.MapStateUpdater;
import com.quadx.dungeons.tools.HealthBar;
import com.quadx.dungeons.tools.StatManager;
import com.quadx.dungeons.tools.gui.InfoOverlay;
import com.quadx.dungeons.tools.gui.Text;

import java.util.ArrayList;

import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.GridManager.*;
import static com.quadx.dungeons.states.mapstate.MapState.cellW;
import static com.quadx.dungeons.states.mapstate.MapState.out;
import static javax.swing.JSplitPane.DIVIDER;

/**
 * Created by Tom on 11/10/2015.
 */
@SuppressWarnings("ALL")
public class Monster {
    public static boolean reindexMons=false;

    public MonAIv1 ai= new MonAIv1();
    protected Texture[] icons= new Texture[4];
    protected Texture icon = null;
    protected Damage d = new Damage();
    protected String name = "monster";
    protected String status = "0";
    protected TextureAtlas textureAtlas=null;
    protected Animation anim = null;
    HealthBar hbar = new HealthBar();
    HealthBar hbar2 = new HealthBar();

    Vector2 pos = new Vector2();
    Vector2 absPos = new Vector2();
    InfoOverlay overlay= new InfoOverlay();

    protected boolean caller = true;
    protected boolean clockwise = rn.nextBoolean();
    protected boolean willCircle = rn.nextBoolean();
    protected boolean circling = false;
    protected boolean blind = false;
    protected boolean hit = false;
    protected boolean moved = false;
    protected boolean aa = false;
    protected boolean bb = false;
    boolean lowhp=false;
    protected int level = 1;
    protected double attack;
    protected double intel;
    protected double power = 20;
    protected double hpBase = 60;
    protected double attBase = 40;
    protected double defBase = 40;
    protected double intBase = 40;
    protected double spdBase = 40;
    protected double hp = 0;
    protected double hpMax = 0;
    protected double hpsoft = 0;
    protected double defense;
    protected double speed;
    protected double percentHP=1;
    protected int front = 0;
    protected int oldFront =0;
    protected int liveCellIndex = -1;
    protected int monListIndex = -1;
    protected int healCount = 0;
    protected int sight = 6;
    protected int x;
    protected int px;
    protected int y;
    protected int agroTime=rn.nextInt(200)+15;
    protected int py;
    protected int circleAgro=rn.nextInt(6);
    protected int circleCount=0;
    protected int iconSet=0;
    protected float dtAnim=0;
    protected float moveSpeedMin = .12f;
    protected float moveSpeedMax = .09f;
    protected float dtMove = 0;
    protected float moveSpeed = .15f;
    protected float dtAgro=0;
    protected float callRadius= 0;
    protected int circleAngle=0;
    protected int[] maxes= new int[4];
    protected float expFactor=1;
    Vector3 sights = new Vector3();

    public Monster() {
    }
//GETTERS---------------------------------------------------------------------------------
    public Texture getIcon() {
        return icon;
    }
    public static Monster getNew(){
        Monster m;
        int total=player.getAttack()+player.getIntel()+player.getSpeed()+player.getDefense();
        if(total>300 && rn.nextBoolean()){
            m=new Muk();
        }
        else if(total>230 && rn.nextBoolean()){
            m=new Dodrio();
        }
        else if(total>200 && rn.nextBoolean()){
            if(rn.nextBoolean())
                m=new Gengar();
            else m = new Dragonair();
        }
        else if(total>140 && rn.nextBoolean()){
            m=new Ponyta();
        }
        else if(total >80 && rn.nextBoolean()){
            if(rn.nextBoolean())
                m=new Porygon();
            else
                m=new Krabby();
        }
        else{
            if(rn.nextBoolean())
                m= new Anortih();

//                m = new Kabuto();
            else
                m= new Anortih();
        }
        return m;
    }
    public String getName() {
        return name;
    }
    public Vector2 getPos(){return pos;}
    public Vector2 getAbsPos(){return absPos;}
    Vector2 texturePos=new Vector2();

    public Vector3 getSights(){return sights;}
    public HealthBar getHbar(){return hbar;}
    public HealthBar getHbar2(){return hbar2;}

    public float getdtMove() {
        return dtMove;
    }
    public float getExpFactor(){
        float a=1;
        float m=maxes[0]+maxes[1]+maxes[2]+maxes[3];
        float tot= (float) (attack+defense+intel+speed);
        a=m/tot;
        return a;
    }
    public float getMoveSpeed() {
        return moveSpeed;
    }
    public double getHp() {
        return hp;
    }
    public double getHpMax() {
        return hpsoft;
    }
    public double getSpeed() {
        return speed;
    }
    public double getDefense() {
        return defense;
    }
    public double getIntel() {
        return intel;
    }
    public double getAttack() {
        return attack;
    }
    public double getPercentHP(){return hp/hpMax;}
    public double getIntelDamage() {
        double damage = d.monsterMagicDamage(player, this, (int) power);
        return damage;
    }
    public int getLevel() {
        return (int) level;
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
    public int getSight() {
        return sight;
    }
    public int getLiveCellIndex() {
        return liveCellIndex;
    }
    public int getMonListIndex() {
        return monListIndex;
    }
//SETTERS---------------------------------------------------------------------------------
    public void setLiveCellIndex(int i) {
        liveCellIndex = i;
    }
    public void setMonListIndex(int i) {
        monListIndex = i;
    }
    public void setLevel(int l) {
        level = l;
    }
    public void setCords(int a, int b) {
        x = a;
        y = b;
        px = a * cellW;
        py = b * cellW;
        pos=new Vector2(a,b);
        absPos=new Vector2(a*cellW,b*cellW);
    }
    public void setCordsPX(int a, int b) {
        x = a / cellW;
        y = b / cellW;
        px = a;
        py = b;
    }
    public void setFront(int x){
        oldFront=front;
        front=x;
        if(oldFront !=front){
                loadIcon();
        }
    }
    public void setHit() {
        if(!blind) {
            hit = true;
        }
    }
    public void setIntel(double intel) {
        this.intel = intel;
    }
    public void setAttack(double attack) {
        this.attack = attack;
    }
    public void setMoved() {
        moved = true;
    }
//OTHER----------------------------------------------------------------------------------
    protected void loadIcon() {
        icon=icons[front];
    }
    public void updateVariables(float dt) {
        dtMove += dt;
        aa = rn.nextBoolean();
        bb = rn.nextBoolean();
        if(agroTime<dtAgro){
            setHit();
        }else{
            dtAgro+=dt;

        }
        //calculate sights
        int side=sight*2*cellW;
        sights=new Vector3(px-(side/2)+(cellW/2),py-(side/2)+(cellW/2),side);
        //update healthbar pos
        hbar.update(absPos.x-22,absPos.y-31,84,6);
        hbar2.update(absPos.x-20,absPos.y-30, (float) (80*getPercentHP()),4);
        texturePos.set(pos.x* cellW -icon.getWidth()/4,pos.y * cellW -icon.getHeight()/4);
        if(!(hp<=hpMax/3)) lowhp=true;
        else lowhp=false;
        overlay.texts.clear();
        overlay.texts.add(new Text("LVL "+level,new Vector2(absPos.x-22,absPos.y-10),Color.GRAY,1));
        if(isHit())
        overlay.texts.add(new Text("!",new Vector2(absPos.x-22, (float) ((pos.y+1.5)*cellW-10)),Color.GRAY,1));

    }
    public void stattest() {
        for (int i = 0; i < 100; i++) {
            sayStats();
        }
    }
    protected void genLevel() {
        level = player.level + rn.nextInt(player.floor);
        //out(level+"");
    }
    protected void genStats() {
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
        maxes[0]= (int) (((((attBase+31) * 2 + (Math.sqrt(65535)/4)) * level) / 100) + 5);
        //System.out.println("Attack :"+attack);
    }
    private void genDefense() {
        double a = defBase + rn.nextInt(31);
        double b = Math.sqrt(rn.nextInt(65535)) / 4;
        defense = (((a * 2 + b) * level) / 100) + 5;
        maxes[1]= (int) (((((defBase+31) * 2 + (Math.sqrt(65535)/4)) * level) / 100) + 5);

        //System.out.println("Defense :"+defense);
    }
    private void genSpeed() {
        double a = spdBase + rn.nextInt(31);
        double b = Math.sqrt(rn.nextInt(65535)) / 4;
        speed = (((a * 2 + b) * level) / 100) + 5;
        maxes[0]= (int) (((((spdBase+31) * 2 + (Math.sqrt(65535)/4)) * level) / 100) + 5);
    }
    private void genIntel() {
        double a = intBase + rn.nextInt(31);
        double b = Math.sqrt(rn.nextInt(65535)) / 4;
        intel = (((a * 2 + b) * level) / 100) + 5;
        maxes[0]= (int) (((((intBase+31) * 2 + (Math.sqrt(65535)/4)) * level) / 100) + 5);
    }
    private void genHp() {
        double a = hpBase + rn.nextInt(31);
        double b = Math.sqrt(rn.nextInt(65535)) / 4;
        //hp = ((((a * 2 + b) * level) / 100) + level + 10)*1.5;
        hp=15+Math.exp(Math.sqrt(Math.pow(level,1.5))/2)*1.5;
        hpMax = hp;
        hpsoft = hp;
    }
    public void takeAttackDamage(double i) {
        hp = hp - (int) i;
        if (hp < 0) {
            hp = 0;
        }
        setHit();
        MapStateRender.setHoverText("-" + (int)i, .8f, Color.RED,px,py, true);
        out("Hit "+name+" for " + (int)i + " damage.");
    }
    public ArrayList<String> sayStats() {
        ArrayList<String> stats=new ArrayList<>();
        stats.add(name);
        stats.add("HP: " + (int) hp);
        stats.add("Level: " + level);
        stats.add("Attack: " + (int) attack);
        stats.add("Defense: " + (int) defense);
        stats.add("Intel: " + (int) intel);
        stats.add("Speed: " + (int) speed);
        return stats;
    }
    public void move() {
        int a = rn.nextInt(3) + 1;
        int b = rn.nextInt(3) + 1;
        int[] pos={x,y};

        if (hit) {
            pos=ai.agro(this,pos[0],pos[1]);
        } else {                                                                          //if monster has not been hit
            if (!checkForDamageToPlayer(player.getX(), player.getY())) {                  //if not hitting player
                if (PlayerInSight()) {
                    pos = ai.moveToPoint(this, player.getX(), player.getY());
                   // MapState.gm.clearArea(pos[0], pos[1], false);
                } else {
                    switch (a) {
                        case (1): {
                            pos[0] += rn.nextInt(2);
                            setFront(3);
                            break;
                        }
                        case (2): {
                            pos[0] -= rn.nextInt(2);
                            setFront(2);
                            break;
                        }
                        case (3): {
                            setFront(front);
                            break;
                        }
                    }
                    switch (b) {
                        case (1): {
                            pos[1]+= rn.nextInt(2);
                            setFront(0);
                            break;
                        }
                        case (2): {
                            pos[1] -= rn.nextInt(2);
                            setFront(1);
                            break;
                        }
                        case (3): {
                            setFront(front);
                            break;
                        }
                    }
                }

            }
        }
        boolean placed = false;
        boolean cont = false;
        //checks if there is monster already on chosen cell
        try {
            Monster m = dispArray[pos[0]][pos[1]].getMonster();
            if (m != null) {
                if (m.getX() == pos[0] && m.getY() == pos[1]) {
                    cont = true;
                }
            }
        }catch (ArrayIndexOutOfBoundsException r){}
        if (!cont) {    //if there is no monster
            dispArray[x][y].clearMonster();
            try {
                if (dispArray[pos[0]][pos[1]].getState()) {
                    dispArray[pos[0]][pos[1]].setMonster(this);
                    placed = true;
                    moved = true;
                }
            }catch (ArrayIndexOutOfBoundsException e){}
            if (!placed) {//if it didnt move because wall use di32g
                setCords(x, y);
               /* for (Cell c : GridManager.liveCellList) {
                    if (c.getX() == x && c.getY() == y) {
                        c.setMon(true);
                    }
                }*/
                if (rn.nextBoolean() || rn.nextBoolean()) MapState.gm.clearArea(x, y, false);
            }
        }
        dtMove = 0;
    }
    private boolean PlayerInSight() {
        return player.getX() > this.getX() - this.getSight() && player.getX() < this.getX() + this.getSight()
                && player.getY() > this.getY() - this.getSight() && player.getY() < this.getY() + this.getSight();
    }
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean checkForDamageToPlayer(int px, int py) {
        boolean hit = false;
        if ((x == px && y == py) || (x + 1 == px && y == py) || (x - 1 == px && y == py)    //check surrounding tiles for player
                || (x == px && y + 1 == py) || (x == px && y - 1 == py)) {                  //hurt if found
            int d ;
            if(intel>attack)
                d=Damage.monsterMagicDamage(player, this, (int) power);
            else if(attack>intel)
                d= Damage.monsterPhysicalDamage(player, this, (int) power);
            else{
                if(rn.nextBoolean())
                    d=Damage.monsterMagicDamage(player, this, (int) power);
                else
                    d= Damage.monsterPhysicalDamage(player, this, (int) power);
            }
            player.setHp(player.getHp() - d);//apply damage
            MapStateUpdater.shakeScreen(.5f);
            player.setDest(absPos);
            MapStateRender.setHoverText("-" + d, 1, Color.RED, player.getPX(), player.getPY(), true);
            hit = true;
            StatManager.killer=this;
        }
        return hit;
    }
    public boolean isHit() {
        return hit;
    }
    public boolean isMoved() {
    return moved;
}
    public boolean isLowHP(){return  lowhp;}
    public boolean checkIfDead(){
        if (hp < 1) {
            out(DIVIDER);
            out(name + " Level " + level + " was killed.");
            player.addKills();
            player.setExp(level,getExpFactor());
            player.checkLvlUp();
            MapState.makeGold(level);
            try {
                monsterList.remove(this);
                dispArray[x][y].clearMonster();
            } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {}
            return true;
        }
        else
            return false;
    }
    public static void reindexMonsterList(){
        if(reindexMons) {
            for (Monster m : monsterList) {
                m.setMonListIndex(monsterList.indexOf(m));
            }
            reindexMons=false;
        }
    }

    public void takeEffect(Attack attack) {
        if(attack.getName().equals("Blind")) {
            blind=true;
            hit = false;
            sight=0;
        }
    }

    public Vector2 getTexturePos() {
        return texturePos;
    }

    public InfoOverlay getInfoOverlay() {
        return overlay;
    }
}
