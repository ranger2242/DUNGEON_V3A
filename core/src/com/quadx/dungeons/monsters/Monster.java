package com.quadx.dungeons.monsters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.quadx.dungeons.*;
import com.quadx.dungeons.ai.AI;
import com.quadx.dungeons.attacks.Attack;
import com.quadx.dungeons.attacks.Blind;
import com.quadx.dungeons.attacks.Illusion;
import com.quadx.dungeons.items.Gold;
import com.quadx.dungeons.physics.Body;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.tools.*;
import com.quadx.dungeons.tools.gui.HoverText;
import com.quadx.dungeons.tools.gui.InfoOverlay;
import com.quadx.dungeons.tools.gui.Text;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.stream.Collectors;

import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.GridManager.*;
import static com.quadx.dungeons.states.mapstate.MapState.cell;
import static com.quadx.dungeons.states.mapstate.MapState.cellW;
import static com.quadx.dungeons.tools.gui.HUD.out;
import static javax.swing.JSplitPane.DIVIDER;

/**
 * Created by Tom on 11/10/2015.
 */
@SuppressWarnings("ALL")
public class Monster {
    public static ArrayList<Monster> mdrawList = new ArrayList<>();
    protected Texture[] icons = new Texture[4];
    protected Texture icon = null;
    protected Damage d = new Damage();
    protected TextureAtlas textureAtlas = null;
    protected Animation anim = null;
    public Direction.Facing facing = Direction.Facing.North;
    Rectangle hbar = new Rectangle();
    Rectangle hbar2 = new Rectangle();
    Vector2 pos = new Vector2();
    Vector2 absPos = new Vector2();
    Vector2 prevPos = new Vector2();
    Vector2 texturePos = new Vector2();
    Vector3 sights = new Vector3();
    InfoOverlay overlay = new InfoOverlay();
    AI.State state = AI.State.INACTIVE;

    protected String name = "monster";
    protected String status = "0";

    public static boolean reindexMons = false;
    protected boolean caller = true;
    protected boolean clockwise = rn.nextBoolean();
    protected boolean willCircle = rn.nextBoolean();
    protected boolean circling = false;
    protected boolean blind = false;
    protected boolean hit = false;
    protected boolean moved = false;
    protected boolean drawable = false;
    protected boolean invincable = false;
    boolean lowhp = false;

    protected double str;
    protected double intel;
    protected double power = 20;
    protected double hpBase = 60;
    protected double strBase = 40;
    protected double defBase = 40;
    protected double intBase = 40;
    protected double spdBase = 40;
    protected double hp = 0;
    protected double hpMax = 0;
    protected double hpsoft = 0;
    protected double defense;
    protected double speed;
    protected double percentHP = 1;

    protected int level = 1;
    protected int front = 0;
    protected int oldFront = 0;
    protected int liveCellIndex = -1;
    protected int monListIndex = -1;
    protected int healCount = 0;
    protected int sight = 6;
    protected int x;
    protected int px;
    protected int y;
    protected int py;
    protected int circleAgro = rn.nextInt(6);
    protected int circleCount = 0;
    protected int iconSet = 0;
    protected int circleAngle = 0;
    protected int[] maxes = new int[4];

    protected static Delta dRespawn = new Delta(10);
    protected Delta dChangeDirection = new Delta(.5f);
    protected Delta dInvincibility = new Delta(Game.ft * 3);
    protected Delta dAgro = new Delta(rn.nextInt(200) + 15);

    protected Elapsed eAgroTime = new Elapsed();

    protected float dtAnim = 0;
    protected float moveSpeedMin = .12f;
    protected float moveSpeedMax = .09f;
    protected float moveSpeed = .15f;
    protected float dtAgro = 0;
    protected float callRadius = 0;
    protected float expFactor = 1;
    protected float velocity = 5;

    public Monster() {
    }

    public Monster(int i) {
        genStats(i);
        dAgro = new Delta(rn.nextInt(200) + 15);
    }

    //GETTERS--------------------------------------------------------------------------------
    public ArrayList<String> getStatsList() {
        ArrayList<String> stats = new ArrayList<>();
        stats.add(name);
        stats.add("HP: " + (int) hp);
        stats.add("Level: " + level);
        stats.add("Attack: " + (int) str);
        stats.add("Defense: " + (int) defense);
        stats.add("Intel: " + (int) intel);
        stats.add("Speed: " + (int) speed);
        return stats;
    }

    public static Monster getNew() {
        Monster m;
        int total = player.getStrength() + player.getIntel() + player.getSpeed() + player.getDefense();
        if (total > 300 && rn.nextBoolean()) {
            m = new Muk();
        } else if (total > 500 && rn.nextBoolean()) {
            m = new Dodrio();
        } else if (total > 500 && rn.nextBoolean()) {
            if (rn.nextBoolean())
                m = new Gengar();
            else m = new Dragonair();
        } else if (total > 300 && rn.nextBoolean()) {
            m = new Ponyta();
        } else if (total > 150 && rn.nextBoolean()) {
            if (rn.nextBoolean())
                m = new Porygon();
            else
                m = new Krabby();
        } else {
            if (rn.nextBoolean())
                m = new Anortih();

//                m = new Kabuto();
            else
                m = new Anortih();
        }
        return m;
    }

    public InfoOverlay getInfoOverlay() {
        return overlay;
    }

    public Rectangle getHbar() {
        return hbar;
    }

    public Rectangle getHbar2() {
        return hbar2;
    }

    public Rectangle getHitBox() {
        float x = getAbsPos().x,
                y = getAbsPos().y,
                w = getIcon().getWidth(),
                h = getIcon().getHeight();

        return new Rectangle(x - (w / 2), GridManager.fixHeight(new Vector2(x, y)) - (h / 2), w, h);
    }

    public Rectangle getAgroBox() {
        float rx = (getSight() * cell.x);
        float ry = (getSight() * cell.y);
        return new Rectangle(absPos.x - rx, fixHeight(new Vector2(absPos.x - rx, absPos.y - ry)), rx * 2, ry * 2);

//        return new Rectangle(absPos.x-r,absPos.y-r,r*2,r*2);
    }

    public Vector2 getPos() {
        return pos;
    }

    public Vector2 getAbsPos() {
        return absPos;
    }

    public Vector2 getFixPos() {
        return new Vector2(absPos.x,fixHeight(absPos));
    }

    public Vector2 getTexturePos() {
        texturePos.set(getAbsPos().x - (getIcon().getWidth()) / 2, GridManager.fixHeight(getAbsPos()) - (getIcon().getHeight()) / 2);

        return texturePos;
    }

    public Vector3 getSights() {
        return sights;
    }

    public Texture getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public boolean isHit() {
        return hit;
    }

    public boolean isMoved() {
        return moved;
    }

    public boolean isLowHP() {
        return lowhp;
    }

    public boolean PlayerInSight() {
        return player.pos().x > this.getX() - this.getSight() && player.pos().x < this.getX() + this.getSight()
                && player.pos().y > this.getY() - this.getSight() && player.pos().y < this.getY() + this.getSight();
    }

    public boolean collisionWithPlayer() {
        boolean hit = false;
        if (getHitBox().overlaps(player.getHitBox()) && !player.wasHit && !player.jumping) {
            player.wasHit = true;
            int d = Damage.monsterMagicDamage(this);
            player.addHp(-d);
            player.setDest(absPos);
            MapState.shakeScreen = true;

            new HoverText("-" + d, 1, new Color(1f, .2f, .2f, 1f), player.getAbsPos().x, player.getAbsPos().y, true);

            hit = true;
            StatManager.killer = this;
        }
        return hit;
    }

    public boolean isDrawable() {
        return drawable;
    }

    public boolean checkIfDead() {
        if (hp < 1) {
            out(DIVIDER);
            out(name + " Level " + level + " was killed.");
            player.addKills();
            player.setExp(level, getExpFactor());
            player.checkLvlUp();
            player.addGold(new Gold(level));
            //MapState.makeGold(level);
            dropItems();
            try {
                monsterList.remove(this);
                dispArray[x][y].clearMonster();
            } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            }

            return true;
        } else
            return false;
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

    public double getStrength() {
        return str;
    }

    public double getPercentHP() {
        return (double) hp / (double) hpMax;
    }

    public double getIntelDamage() {
        double damage = d.monsterMagicDamage(this);
        return damage;
    }

    public float getExpFactor() {
        float a = 1;
        float m = maxes[0] + maxes[1] + maxes[2] + maxes[3];
        float tot = (float) (str + defense + intel + speed);
        a = m / tot;
        return a;
    }

    public float getMoveSpeed() {
        return moveSpeed;
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
        pos = new Vector2(a, b);
        absPos = new Vector2(a * cellW, b * cellW);
    }

    public void setCordsPX(int a, int b) {
        x = a / cellW;
        y = b / cellW;
        px = a;
        py = b;
    }

    public void setFront(int x) {
        oldFront = front;
        front = x;
        if (oldFront != front) {
            loadIcon();
        }
    }

    public void setHit() {
        if (!blind) {
            eAgroTime = new Elapsed();
            hit = true;
        }
    }

    public void setIntel(double intel) {
        this.intel = intel;
    }

    public void setStr(double str) {
        this.str = str;
    }

    public void setMoved() {
        moved = true;
    }

    public void setPos(Vector2 pos) {
        this.pos = pos;
    }

    public void setAbsPos(Vector2 a) {
        this.absPos.set(a);
        px = (int) EMath.round(a.x);
        py = (int) EMath.round(a.y);
        setPos(new Vector2((int) (EMath.round(absPos.x / cellW)), (int) (EMath.round(absPos.y / cellW))));
    }

    public void setDrawable(boolean s) {
        drawable = s;
    }

    public void setSights() {
        int side = sight * 2 * cellW;
        sights = new Vector3(px - (side / 2) + (cellW / 2), py - (side / 2) + (cellW / 2), side);
    }

    void setOverlay() {
        float width = 80;
        int x = (int) (absPos.x - (width / 2));
        int y = (int) (absPos.y - (getIcon().getHeight() / 2));
        Vector2 hbarpos = new Vector2(x - 2, y - 31);
        Vector2 hbarpos2 = new Vector2(x, y - 30);
        hbar = new Rectangle(hbarpos.x, fixHeight(hbarpos), width + 4, 6);
        hbar2 = new Rectangle(hbarpos2.x, fixHeight(hbarpos2), (float) ((double) width * getPercentHP()), 4);
        texturePos.set(pos.x * cellW - icon.getWidth() / 4, pos.y * cellW - icon.getHeight() / 4);
        if (!(hp <= hpMax / 3)) lowhp = true;
        else lowhp = false;
        overlay.texts.clear();
        Vector2 ne = new Vector2(absPos.x, absPos.y);
        ne.add(0, -20);
        overlay.texts.add(new Text("LVL " + level, new Vector2(absPos.x - 22, GridManager.fixHeight(ne)), Color.GRAY, 1));
        if (isHit())
            overlay.texts.add(new Text("!", new Vector2(absPos.x - 22, GridManager.fixHeight(ne)), Color.GRAY, 1));
    }

    //OTHER----------------------------------------------------------------------------------
    void dropItems() {
        int r = rn.nextInt(4);
        if (rn.nextBoolean())
            for (int i = 0; i < r; i++) {
                Inventory.discard(getPos(), false, this);
            }
    }

    protected void loadIcon() {
        int i = 0;
        switch (facing) {
            case North:
                ;
            case Northwest:
            case Northeast:
                i = 0;
                break;
            case West:
                i = 2;
                break;
            case Southwest:
            case South:
            case Southeast:
                i = 1;
                break;
            case East:
                i = 3;
                break;
        }
        icon = icons[i];
    }

    protected void genLevel() {
        level = player.level + rn.nextInt(player.floor);
        //out(level+"");
    }

    protected void genStats() {
        // System.out.println("---------------------------------------");
        genHp();
        genStrength();
        genDefense();
        genIntel();
        genSpeed();
    }

    private void genStrength() {
        double a = strBase + rn.nextInt(31);
        double b = Math.sqrt(rn.nextInt(65535)) / 4;
        str = (((a * 2 + b) * level) / 100) + 5;
        maxes[0] = (int) (((((strBase + 31) * 2 + (Math.sqrt(65535) / 4)) * level) / 100) + 5);
        //System.out.println("Attack :"+str);
    }

    private void genDefense() {
        double a = defBase + rn.nextInt(31);
        double b = Math.sqrt(rn.nextInt(65535)) / 4;
        defense = (((a * 2 + b) * level) / 100) + 5;
        maxes[1] = (int) (((((defBase + 31) * 2 + (Math.sqrt(65535) / 4)) * level) / 100) + 5);

        //System.out.println("Defense :"+defense);
    }

    private void genSpeed() {
        double a = spdBase + rn.nextInt(31);
        double b = Math.sqrt(rn.nextInt(65535)) / 4;
        speed = (((a * 2 + b) * level) / 100) + 5;
        maxes[0] = (int) (((((spdBase + 31) * 2 + (Math.sqrt(65535) / 4)) * level) / 100) + 5);
    }

    private void genIntel() {
        double a = intBase + rn.nextInt(31);
        double b = Math.sqrt(rn.nextInt(65535)) / 4;
        intel = (((a * 2 + b) * level) / 100) + 5;
        maxes[0] = (int) (((((intBase + 31) * 2 + (Math.sqrt(65535) / 4)) * level) / 100) + 5);
    }

    private void genHp() {
        double a = hpBase + rn.nextInt(31);
        double b = Math.sqrt(rn.nextInt(65535)) / 4;
        //hp = ((((a * 2 + b) * level) / 100) + level + 10)*1.5;
        hp = 15 + (hpBase) * Math.exp(Math.sqrt(Math.pow(level, 1.5)) / 2) / 2;
        hpMax = hp;
        hpsoft = hp;
    }

    public void genStats(int i) {
        // System.out.println("---------------------------------------");
        level = i;
        genHp();
        genStrength();
        genDefense();
        genIntel();
        genSpeed();
    }


    public void checkAgro() {
        if (dAgro.isDone())
            setHit();
    }

    static int count = 0;

    void checkForDamageToDummies() {
        for (Illusion.Dummy d : Illusion.dummies) {
            if (getHitBox().overlaps(d.hitbox)) {
                int damage;
                damage = Damage.monsterMagicDamage(this);
                d.hp += -damage;
                if (d.hp <= 0) d.dead = true;
                new HoverText("-" + damage, 1, new Color(1f, .2f, .2f, 1f), d.absPos.x, d.absPos.y, true);
            }
        }
        for (int i = Illusion.dummies.size() - 1; i >= 0; i--) {
            Illusion.Dummy d = Illusion.dummies.get(i);
            if (d.dead)
                Illusion.dummies.remove(i);
        }

    }

    public void hitByAttack() {
        player.getAttack().runAttackMod();
        hit = true;
        Attack attack = player.attackList.get(Attack.pos);
        takeAttackDamage(Damage.calcPlayerDamage(attack, this));
        if (attack.getClass().equals(Blind.class)) {
            hit = false;
        }
        checkIfDead();
    }

    public static boolean isNearPlayer(Vector2 pos) {
        return EMath.pathag(player.pos(), pos) < 10;
    }

    public static void spawn() {
        if (Tests.spawn && dRespawn.isDone() && monsterList.size() < 120) {
            Monster m = Monster.getNew();
            int index = rn.nextInt(liveCellList.size());
            if (!liveCellList.get(index).isWater() && liveCellList.get(index).isClear()) {
                Cell c = liveCellList.get(index);
                m.setCords(c.getX(), c.getY());

                if (!isNearPlayer(c.getPos())) {
                    monsterList.add(m);
                    c.setMon(true);
                    c.setMonsterIndex(monsterList.indexOf(m));
                    liveCellList.set(index, c);
                    //Game.console();
                    out("MList:" + monsterList.indexOf(m));
                    new HoverText("!", .5f, Color.RED, player.getAbsPos().x, player.getAbsPos().y, true);
                    reindexMons = true;
                    GridManager.loadLiveCells();
                    dRespawn.reset();
                }
            }
        }
    }

    public void fixPosition() {
/*        int x= (int) absPos.x;
        int y= (int) absPos.y;
        if(absPos.x<2){
            x=2;
        }
        else if(absPos.x+getIcon().getWidth()>(res*cellW)-2){
            x=(res*cellW)-(getIcon().getWidth()-2);
        }
        if(absPos.y<2){
            y=2;
        }
        else if(absPos.y+getIcon().getHeight()>(res*cellW)-2){
            y=(res*cellW)-(getIcon().getHeight()-2);
        }
        setAbsPos(new Vector2(x,y));//terminal test*/

        Vector2 iconDim = new Vector2(getIcon().getWidth() / 2, getIcon().getHeight() / 2);
        Pair<Vector2, Vector2> pos = Body.wrapPos(iconDim, absPos);
        setAbsPos(pos.getKey());
        setPos(pos.getValue());
    }

    public void stattest() {
        for (int i = 0; i < 100; i++) {
            getStatsList();
        }
    }

    public void takeAttackDamage(double i) {
        if (!invincable) {
            hp = hp - (int) i;
            if (hp < 0) {
                hp = 0;
            }
            setHit();
            new HoverText("-" + (int) i, .8f, new Color(1f, .2f, .2f, 1f), absPos.x, absPos.y + 20, true);
            out("Hit " + name + " for " + (int) i + " damage.");
            invincable = true;
        } else {
            if (dInvincibility.isDone()) {
                invincable = false;
                dInvincibility.reset();
            }
        }

    }

    public void fixMonsterCollisions() {
        List<Monster> list = mdrawList.stream().filter(m -> !m.equals(this) && m.getHitBox().overlaps(getHitBox())).collect(Collectors.toList());
        for (Monster m : list) {
            float ang = (float) Math.toRadians(EMath.angle(getAbsPos(), m.getAbsPos())),
                    x = 10 * (float) Math.cos(ang),
                    y = 10 * (float) Math.sin(ang);
            move2(x, y);
            m.move2(-x, -y);
        }
    }


    public void move2(float x, float y) {
        setAbsPos(getAbsPos().add(x, y));
    }

    double calcMoveSpeed(){
        float max=20f;
        double t=eAgroTime.getTime();
        double s=speed;
        if(state== AI.State.AGRO) {
            if (t < max)
                s= speed * (1 + t / max);
            else
                s= speed * 2;
        }
        s/=5;
        return s;
    }

    public void move(Vector2 vel) {//actually moves the thing
        Vector2 end = new Vector2(absPos.x + vel.x, absPos.y + vel.y);
        int gw = cellW * (res + 1);

        if (end.x < 0)
            end.x = getIcon().getWidth();
        else if (end.x + getIcon().getWidth() > gw)
            end.x = (gw) - getIcon().getWidth();

        if (end.y < 0)
            end.y = getIcon().getHeight();
        else if (end.y + getIcon().getHeight() > gw)
            end.y = (gw) - getIcon().getHeight();

        Vector2 comp = Physics.getVxyComp(velocity, absPos, end);
        comp.scl((float) calcMoveSpeed());

        int x = (int) (EMath.round(absPos.x / cellW));
        int y = (int) (EMath.round(absPos.y / cellW));

        Cell c = dispArray[x][y];
        if (c.isWater()||  c.isClear() && (!c.hasMon() || (prevPos.x == x && prevPos.y == y))) {
            setAbsPos(new Vector2(absPos.x + comp.x, absPos.y + comp.y));
        } else {
            if (rn.nextFloat() < .05)
                MapState.gm.clearArea(x, y, false);
        }

    }

    //AI ACTIONS-------------------------------------------------------------------------------------------
    void changeDirection() {

        float dst = player.pos().dst(getPos());
        if (dChangeDirection.isDone()) {
            facing = Direction.Facing.getRandom();
            dChangeDirection.reset();
        }
        try {
            move(Direction.getVector(facing));
        } catch (ArrayIndexOutOfBoundsException e) { }
    }

    private void chasePlayer() {
        float angle;
        boolean dir = player.pos().dst(getPos()) > res / 2;
        if (!Illusion.dummies.isEmpty()) {
            angle = (float) Math.toRadians(EMath.angle(absPos, Illusion.dummies.get(rn.nextInt(Illusion.dummies.size())).absPos) + 180);
        } else
            angle = (float) Math.toRadians(EMath.angle(absPos, player.getAbsPos()) + 180);
        if (dir)
            angle += Math.PI;
        facing = Direction.getDirection(angle);
        try {
            move(Direction.getVector(facing));
        } catch (ArrayIndexOutOfBoundsException e) { }
    }

    //-----------------------------------------------------------------------------------------------------
    public void move() {//calculates move direction
        switch (state) {
            case INACTIVE:
                break;
            case AWARE:
                break;
            case WANDER:
                changeDirection();
                break;
            case AGRO:
                chasePlayer();
                break;
            case DEAD:
                break;
        }

        try {
            dispArray[(int) prevPos.x][(int) prevPos.y].setMon(false);
            dispArray[(int) pos.x][(int) pos.y].setMon(true);
            prevPos.set(pos);
        } catch (ArrayIndexOutOfBoundsException e) {

        }

    }

    boolean hasMoved() {
        return prevPos.x != pos.x || prevPos.y != pos.y;
    }


    public double getPower() {
        return power;
    }

    public static void update(float dt) {
        dRespawn.update(dt);
        spawn();
        try {
            for (Monster m : monsterList) {
                m.updateVariables(dt);
            }
        } catch (ConcurrentModificationException e) {
            out("CME Monsters");
        }
    }

    public void updateVariables(float dt) {
        dChangeDirection.update(dt);
        dAgro.update(dt);
        eAgroTime.update(dt);
        if (invincable)
            dInvincibility.update(dt);

        velocity = (float) (6 + .0136 * speed + .000005 * Math.pow(speed, 2)) * (3f / 4f);
        checkAgro();
        setSights();
        setOverlay();
        collisionWithPlayer();
        checkForDamageToDummies();
        loadIcon();
        fixMonsterCollisions();
        if (!Tests.allstop) {
            state = AI.State.determine(this);
            move();
            fixPosition();
        }
    }

    public void setState(AI.State state) {
        this.state = state;
    }
}
