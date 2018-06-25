package com.quadx.dungeons.monsters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.quadx.dungeons.Cell;
import com.quadx.dungeons.Damage;
import com.quadx.dungeons.GridManager;
import com.quadx.dungeons.Inventory;
import com.quadx.dungeons.ai.AI;
import com.quadx.dungeons.attacks.Attack;
import com.quadx.dungeons.attacks.Blind;
import com.quadx.dungeons.attacks.Illusion;
import com.quadx.dungeons.items.resources.Gold;
import com.quadx.dungeons.physics.Body;
import com.quadx.dungeons.physics.Physics;
import com.quadx.dungeons.shapes1_5.EMath;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.tools.Direction;
import com.quadx.dungeons.tools.Elapsed;
import com.quadx.dungeons.tools.StatManager;
import com.quadx.dungeons.tools.Tests;
import com.quadx.dungeons.tools.gui.HoverText;
import com.quadx.dungeons.tools.gui.InfoOverlay;
import com.quadx.dungeons.tools.gui.Text;
import com.quadx.dungeons.tools.stats.MonsterStat;
import com.quadx.dungeons.tools.timers.Delta;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.stream.Collectors;

import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.GridManager.*;
import static com.quadx.dungeons.GridManager.rn;
import static com.quadx.dungeons.states.mapstate.MapState.*;
import static com.quadx.dungeons.tools.gui.HUD.out;
import static com.quadx.dungeons.tools.timers.Time.SECOND;
import static com.quadx.dungeons.tools.timers.Time.ft;
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
    Vector2 prevPos = new Vector2();
    Vector2 texturePos = new Vector2();
    Vector3 sights = new Vector3();
    InfoOverlay overlay = new InfoOverlay();
    AI.State state = AI.State.INACTIVE;

    Inventory inv = new Inventory();
    public Body body = new Body();
    public MonsterStat st = new MonsterStat();
    protected String status = "0";

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


    protected double percentHP = 1;

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
    static int count = 0;

    protected static Delta dRespawn = new Delta(7.5f * SECOND);
    protected Delta dChangeDirection = new Delta(.5f);
    protected Delta dInvincibility = new Delta(ft * 3);
    protected Delta dAgro = new Delta(rn.nextInt(200) + 15);

    protected Elapsed eAgroTime = new Elapsed();

    protected float dtAnim = 0;
    protected float moveSpeedMin = .12f;
    protected float moveSpeedMax = .09f;
    protected float moveSpeed = .15f;
    protected float dtAgro = 0;
    protected float callRadius = 0;
    protected float expFactor = 1;


    public Monster() {
    }

    public Monster(int i) {
        st.genStats();
        dAgro = new Delta(rn.nextInt(200) + 15);
    }

    //GETTERS--------------------------------------------------------------------------------
    public static Monster getNew(Vector2 absPos) {
        Monster m;
        int total = player.st.getStrength() + player.st.getIntel()
                + player.st.getSpeed() + player.st.getDefense();
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
        m.body.init();
        m.body.setMonster(m);
        m.body.setAbs(absPos);
        m.dChangeDirection.finish();
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
        float x = abs().x,
                y = abs().y,
                w = getIcon().getWidth(),
                h = getIcon().getHeight();

        return new Rectangle(x - (w / 2), GridManager.fixY(new Vector2(x, y)) - (h / 2), w, h);
    }

    public Rectangle getAgroBox() {
        Vector2 r = new Vector2(cell).scl(getSight());
        Vector2 p = new Vector2(abs()).add(r);
        r.scl(2);
        return new Rectangle(p.x, fixY(p), r.x, r.y);
    }

    public Vector2 pos() {
        return body.getPos();
    }

    public Vector2 abs() {
        return body.getAbs();
    }

    public Vector2 fixed() {
        return body.getFixedPos();
    }

    public Vector2 getTexturePos() {
        texturePos.set(abs().x - (getIcon().getWidth()) / 2, GridManager.fixY(abs()) - (getIcon().getHeight()) / 2);

        return texturePos;
    }

    public Vector3 getSights() {
        return sights;
    }

    public Texture getIcon() {
        return body.getIcons();
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
        if (body.getHitBox().overlaps(player.body.getHitBox()) && !player.wasHit && !player.jumping) {
            player.wasHit = true;
            int d =Damage.monsterMagicDamage(this);
            player.takeDamage(d,abs());
            if(player.isRoughSkin()){
                takeAttackDamage(st.getHpMax()/4);
            }
            hit = true;
            StatManager.killer = this;
        }
        return hit;
    }

    public boolean isDrawable() {
        return drawable;
    }

    public boolean checkIfDead() {
        if (st.isDead()) {
            out(DIVIDER);
            int lvl = st.getLevel();
            out(st.getName() + " Level " + lvl + " was killed.");
            player.addKills();
            player.setExp(lvl, st.getExpFactor());
            player.checkLvlUp();
            player.addGold(new Gold(lvl));
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

    public double getIntelDamage() {
        double damage = d.monsterMagicDamage(this);
        return damage;
    }

    public float getMoveSpeed() {
        return moveSpeed;
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

    public int getMonListIndex() {
        return monListIndex;
    }

    //SETTERS---------------------------------------------------------------------------------
    public void setMonListIndex(int i) {
        monListIndex = i;
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
      /*  if (oldFront != front) {
            loadIcon();
        }*/
    }

    public void setHit() {
        if (!blind) {
            eAgroTime = new Elapsed();
            hit = true;
        }
    }

    public void setMoved() {
        moved = true;
    }

    public void setDrawable(boolean s) {
        drawable = s;
    }

    public void setSights() {
        int side = sight * 2 * cellW;
        sights = new Vector3(px - (side / 2) + (cellW / 2), py - (side / 2) + (cellW / 2), side);
    }

    void setHUDOverlay() {//<<refactor this
        float width = 80;
        Texture icon = getIcon();
        int x = (int) (abs().x - (width / 2));
        int y = (int) (abs().y - (icon.getHeight() / 2));
        Vector2 hbarpos = new Vector2(x - 2, y - 31);
        Vector2 hbarpos2 = new Vector2(x, y - 30);
        hbar = new Rectangle(hbarpos.x, fixY(hbarpos), width + 4, 6);
        hbar2 = new Rectangle(hbarpos2.x, fixY(hbarpos2), (float) ((double) width * st.getPercentHP()), 4);
        texturePos.set(pos().x * cellW - icon.getWidth() / 4, pos().y * cellW - icon.getHeight() / 4);
        if (!st.isLowHP()) lowhp = true;
        else lowhp = false;
        overlay.texts.clear();
        Vector2 ne = new Vector2(abs());
        ne.add(0, -20);
        overlay.texts.add(new Text("LVL " + st.getLevel(), new Vector2(abs().x - 22, GridManager.fixY(ne)), Color.GRAY, 1));
        if (isHit())
            overlay.texts.add(new Text("!", new Vector2(abs().x - 22, GridManager.fixY(ne)), Color.GRAY, 1));
    }//<<refactor this

    //OTHER----------------------------------------------------------------------------------
    void dropItems() {
        int r = rn.nextInt(4);
            for (int i = 0; i < r; i++) {
                inv.discard(pos(), false, this);
            }
    }

    public void checkAgro() {
        if (dAgro.isDone())
            setHit();
    }

    void collisionWithDummies() {
        for (Illusion.Dummy d : Illusion.dummies) {
            if (body.getHitBox().overlaps(d.hitbox)) {
                int damage;
                damage = Damage.monsterMagicDamage(this);
                d.hp += -damage;
                if (d.hp <= 0) d.dead = true;
                Color c = new Color(1f, .2f, .2f, 1f);
                new HoverText("-" + damage, c, d.fixed(), true);
            }
        }
        for (int i = Illusion.dummies.size() - 1; i >= 0; i--) {
            Illusion.Dummy d = Illusion.dummies.get(i);
            if (d.dead)
                Illusion.dummies.remove(i);
        }

    }

    public void takeDamage() {
        player.getAttack().runAttackMod();
        hit = true;
        Attack attack = player.attackList.get(Attack.pos);
        takeAttackDamage(Damage.calcPlayerDamage(attack, this));
        if (attack.getClass().equals(Blind.class)) {
            hit = false;
        }
        if(player.hasFireShield()){
            MapState.particleHandler.addFireEffect(fixed());
        }
        checkIfDead();
    }

    public static boolean isListMaxed() {
        return monsterList.size() >= Tests.spawnLimit;
    }

    public static boolean canSpawn() {
        boolean b = Tests.spawn && !isListMaxed();
        out("Spawn:" + b + " " + monsterList.size());//ALWAYS FALSE UNLESS OBSERVED
        return b;
    }

    public static void spawn(float dt) {
        dRespawn.update(dt);
        if (dRespawn.isDone()) {
            if (canSpawn()) {
                Monster m = Monster.getNew();
                Cell c = gm.getAnyCell();
                m.body.setAbs(c.abs());
                if (!isNearPlayer(c.pos(), 10)) {
                    c.setMonster(m);
                    out("Monsters:" + monsterList.size());
                    new HoverText("!!!", .5f, Color.RED, player.fixed(), true);
                }
            }
            dRespawn.reset();
        }
    }

    public static Monster getNew() {
        return getNew(GridManager.boundI(gm.spreadv(res)));
    }

    public void stattest() {
        for (int i = 0; i < 100; i++) {
            st.getStatsList();
        }
    }

    public void takeAttackDamage(double i) {
        if (invincable && dInvincibility.isDone()) {
            invincable = false;
            dInvincibility.reset();
        } else {
            st.addHp((int) -i);
            setHit();
            Color c = new Color(1f, .2f, .2f, 1f);
            new HoverText("-" + (int) i, c, fixed(), true);
            out("Hit " + st.getName() + " for " + (int) i + " damage.");
            invincable = true;
            body.setKnockBackDest(player.abs());
        }
        checkIfDead();

    }

    public void collisionWithMonsters() {
        List<Monster> list = mdrawList.stream().filter(m -> standingOnMon(m)).collect(Collectors.toList());
        for (Monster m : list) {
            Vector2 v = Physics.getVector(2 * cellW, abs(), m.abs());
            move2(v);
            m.move2(v.scl(-1));
        }
    }

    private boolean standingOnMon(Monster m) {
        return !m.equals(this) && m.body.getHitBox().overlaps(body.getHitBox());
    }

    double calcMoveSpeed() {
        float spd = st.getSpeed();
        float max = 20f;
        double t = eAgroTime.getTime();
        double s = spd;
        if (state == AI.State.AGRO) {
            if (t < max)
                s = spd * (1 + t / max);
            else
                s = spd * 2;
        }
        s /= 5;
        return s;
    }

    boolean hasMoved() {
        return body.getPos().equals(prevPos);
    }

    public void setState(AI.State state) {
        this.state = state;
    }

    void load(String name, int[] stats) {
        st.setName(name);
        st.loadStats(stats);
        st.genLevel();
        st.genStats();
    }

    //AI ACTIONS-------------------------------------------------------------------------------------------
    void changeDirection() {

        float dst = player.pos().dst(pos());
        if (dChangeDirection.isDone()) {
            facing = Direction.Facing.getRandom();
            dChangeDirection.reset();
        }
        try {
            move(Direction.getVector(facing));
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }

    private void chasePlayer() {
        if (player.isInvisible()) {
            changeDirection();
        } else {
            float angle;
            boolean dir = player.pos().dst(pos()) > res / 2;
            if (!Illusion.dummies.isEmpty()) {
                angle = (float) Math.toRadians(EMath.angle(abs(), Illusion.dummies.get(rn.nextInt(Illusion.dummies.size())).absPos) + 180);
            } else
                angle = (float) Math.toRadians(EMath.angle(abs(), player.abs()) + 180);
            if (dir)
                angle += Math.PI;
            angle += Math.PI;
            facing = Direction.getDirection(angle);
            try {
                move(Direction.getVector(facing));
            } catch (ArrayIndexOutOfBoundsException e) {
            }
        }

    }

    private void move2(Vector2 v) {
        body.setAbs(abs().add(v));
    }

    public void move(Vector2 vel) {//actually moves the thing
        Vector2 end = new Vector2(abs()).add(vel);
        int gw = cellW * (res + 1);

        if (end.x < 0)
            end.x = getIcon().getWidth();
        else if (end.x + getIcon().getWidth() > gw)
            end.x = (gw) - getIcon().getWidth();

        if (end.y < 0)
            end.y = getIcon().getHeight();
        else if (end.y + getIcon().getHeight() > gw)
            end.y = (gw) - getIcon().getHeight();

        Vector2 comp = Physics.getVector(body.getVelMag(), abs(), end);
        comp.scl((float) calcMoveSpeed());

        //pos.set(EMath.round(pos));
        Cell c = dispArray(pos());
        if ((c.isWater() || c.isClear()) && (!c.hasMon() || (prevPos.x == pos().x && prevPos.y == pos().y))) {
            Vector2 v = new Vector2(abs()).add(comp);
            body.setAbs(v);
        }
        if (c.isWall() && rn.nextBoolean()) {
            gm.clearArea(pos().x, pos().y, false);
        }
    }

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
            dispArray[(int) pos().x][(int) pos().y].setMon(true);
            prevPos.set(pos());
        } catch (ArrayIndexOutOfBoundsException e) {

        }

    }

    //UPDATE------------------------------------------------------
    public static void update(float dt) {
        spawn(dt);
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

        body.update(dt);
        checkAgro();
        setSights();
        setHUDOverlay();
        collisionWithPlayer();
        collisionWithDummies();
        collisionWithMonsters();
        if (!Tests.allstop) {
            state = AI.State.determine(this);
            move();
        }
    }

    public Vector2 getIconDim() {
        return new Vector2(icon.getWidth(), icon.getHeight());
    }

}  //------------------------------------------------------------