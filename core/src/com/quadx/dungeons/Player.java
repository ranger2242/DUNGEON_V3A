package com.quadx.dungeons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.abilities.Ability;
import com.quadx.dungeons.abilities.Investor;
import com.quadx.dungeons.abilities.WaterBreath;
import com.quadx.dungeons.attacks.*;
import com.quadx.dungeons.items.Gold;
import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.items.SpellBook;
import com.quadx.dungeons.items.equipment.Equipment;
import com.quadx.dungeons.items.modItems.ModItem;
import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.physics.Body;
import com.quadx.dungeons.shapes1_5.Circle;
import com.quadx.dungeons.shapes1_5.Line;
import com.quadx.dungeons.shapes1_5.ShapeRendererExt;
import com.quadx.dungeons.shapes1_5.Triangle;
import com.quadx.dungeons.states.AbilitySelectState;
import com.quadx.dungeons.states.GameStateManager;
import com.quadx.dungeons.states.HighScoreState;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.states.mapstate.ParticleHandler;
import com.quadx.dungeons.tools.StatManager;
import com.quadx.dungeons.tools.Tests;
import com.quadx.dungeons.tools.gui.HUD;
import com.quadx.dungeons.tools.gui.HoverText;
import com.quadx.dungeons.tools.stats.PlayerStat;
import com.quadx.dungeons.tools.timers.Delta;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.GridManager.*;
import static com.quadx.dungeons.states.mapstate.MapState.*;
import static com.quadx.dungeons.tools.ImageLoader.pl;
import static com.quadx.dungeons.tools.ImageLoader.statIcons;
import static com.quadx.dungeons.tools.Tests.infiniteRegen;
import static com.quadx.dungeons.tools.gui.HUD.out;
import static com.quadx.dungeons.tools.timers.Time.SECOND;
import static com.quadx.dungeons.tools.timers.Time.ft;

/**
 * Created by Tom on 11/9/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Player {


    public final ArrayList<Attack> attackList = new ArrayList<>();
    public final ArrayList<Ability> secondaryAbilityList = new ArrayList<>();
    private ArrayList<Line> attackChain = new ArrayList<>();

    public Vector2 kba = new Vector2();
    private ParticleEffect lvlupEffect;
    private Rectangle attackBox = new Rectangle();
    private Circle attackCircle = new Circle();
    private Triangle attackTri = new Triangle();
    private Ability ability = null;
    public Item lastItem = null;
    public Inventory inv = new Inventory();
    public Body body = new Body();
    public PlayerStat st = new PlayerStat();

    private static Delta dWater = new Delta(10 * ft);
    private Delta dShopInvScroll = new Delta(10 * ft);
    private Delta dJump = new Delta(2 / 3 * SECOND);
    private Delta dClearHitBox = new Delta(6 * ft);
    private Delta dInvincibility = new Delta(.2f * SECOND);

    private float gold = 0;
    private int abilityMod = 0;
    private int expLimit = 0;
    private int abilityPoints = 0;
    private int killCount = 0;
    private int exp = 0;
    private int shopInvPos = 0;
    public int maxSec = 2;
    public int floor = 1;


    public boolean canMove = false;
    public boolean wasHit = false;
    public boolean protect = false;
    public boolean jumping = false;
    boolean overrideControls = false;
    private boolean renderEffect = false;



    private double moveSpeed = .1f;




    public Player() {
        st.setLevel(1);
        st.fullHeal();
    }

    //-----------------------------------------------SETTERS------------------------------------------------------------------
    public void setKnockBackDest(Vector2 initPos) {
        Vector2 vel = Physics.getVector(1, initPos, abs());
        float force = cellW * 5;

        kba = vel.scl(force);
        float x = abs().x + vel.x;
        float y = abs().y + vel.y;
        body.setAbs(new Vector2(x, y));
        gm.clearArea(pos(), true);
    }

    public void setAttackBox(Rectangle r) {
        attackBox = r;
    }

    public void setAttackCircle(Circle c) {
        attackCircle = c;
    }

    public void setAttackTriangle(Triangle t) {
        attackTri = t;
    }

    public void setAttackChain(ArrayList<Line> c) {
        attackChain = c;
    }



    public void setExp(int lvl, float factor) {
        double a = 50.9055;
        double b = 1.0015;
        int gain = (int) (a * Math.pow(b, lvl) * factor);
        new HoverText(gain + " EXP", Color.GREEN, fixed(), false);
        this.exp += gain;
        out(st.getName() + " gained " + gain + " EXP");
    }

    public void setGold(float g) {
        gold = g;
    }

    public void setxMoveSpeed(double moveSpeed) {
        this.moveSpeed *= moveSpeed;
    }

    public void setAbilityMod(int o) {
        abilityMod = o;
    }

    public void setAbility(Ability a) {
        ability = a;
    }

    public void setAbilityPoints(int abilityPoints) {
        this.abilityPoints += abilityPoints;
    }

    //ADDERS------------------------------------------------------------------
    public void addGold(int g) {
        gold += g;
    }

    public void addGold(Gold g) {
        addGold(g.getValue());
        out(g.getValue() + " added to stash");
        StatManager.totalGold += g.getValue();
        new HoverText(g.getValue() + "G", Color.GOLD, player.fixed(), false);
        lastItem = g;
        HUD.setLootPopup(g.getIcon());

    }

    public void addKills() {
        killCount++;
    }

    //GETTERS------------------------------------------------------------------
    //====BOOLS------------------------------------------------------------------
    public boolean haveAbility(Class cls) {
        return secondaryAbilityList.stream().anyMatch(x -> x.getClass().equals(cls));
    }

    @SuppressWarnings("WeakerAccess")
    public boolean notHaveAbility(Class cls) {
        return !haveAbility(cls);
    }

    public boolean isInvEmpty() {
        return inv.getList().isEmpty();
    }

    public boolean attackOverlaps(Rectangle hitBox) {
        Attack.HitBoxShape hbs = getAttack().getHitBoxShape();
        switch (hbs) {
            case Circle:
                if (getAttackCircle().overlaps(hitBox))
                    return true;
            case Rect:
                if (getAttackBox().overlaps(hitBox))
                    return true;
            case Chain:
                break;
            case Triangle:
                if (getAttackTriangle().overlaps(hitBox))
                    return true;
            case None:
                break;
        }

        return false;
    }

    public boolean isDead(GameStateManager gsm) {
        boolean dead = false;
        if (st.getHp() < 1 && !Tests.nodeath) {
            HighScoreState.pfinal = player;
            HighScoreState.addScore(st.getScore(this));
            StatManager.pScore = st.getScore(this);
            player = null;
            player = new Player();
            AbilitySelectState.pressed = false;
            inGame = false;
            AttackMod.resetAttacks();
            gsm.push(new HighScoreState(gsm));
            dead = true;
        }

        return dead;
    }

    public boolean hasAP() {
        return getAbilityPoints() != 0;
    }

    public boolean canUseAttack() {
        Attack a = getAttack();
        switch (a.getType()) {
            case Energy:
                return st.getEnergy() >= a.getCost();
            case Mana:
                return st.getMana() >= a.getCost();
        }
        return false;
    }

    public boolean canBuy(Item item) {
        return Game.player.getGold() >= item.getCost();
    }

    //====INTS------------------------------------------------------------------
    public int getGold() {
        return (int) gold;
    }

    public int getAbilityMod() {
        return abilityMod;
    }

    public int getKillCount() {
        return killCount;
    }

    public int getFloor() {
        return floor;
    }

    public int getExpLimit() {
        return expLimit;
    }

    public int getAbilityPoints() {
        return abilityPoints;
    }

    public int invSize() {
        return inv.getList().size();
    }

    //====FLOATS------------------------------------------------------------------
    public double getMoveSpeed() {
        return moveSpeed;
    }

    //====OTHER------------------------------------------------------------------

    private Rectangle getAttackBox() {
        //return new Rectangle(attackBox.x,,attackBox.width,attackBox.height);
        return attackBox;
    }

    private Triangle getAttackTriangle() {
        return attackTri;
    }

    private Circle getAttackCircle() {
        return attackCircle;
    }

    public ArrayList<Line> getAttackChain() {
        return attackChain;
    }

    public Attack getAttack() {
        return attackList.get(Attack.pos);
    }

    public Illusion.Dummy getDummy() {
        return new Illusion.Dummy((int) st.getHpMax() * 2, pos(), abs(), body.getHitBox());
    }

    public Vector2 abs() {
        return body.getAbs();
    }

    public Vector2 pos() {
        return body.getPos();
    }

    public Vector2 fixed() {
        return body.getFixedPos();
    }

    public ArrayList<Ability> getSecondaryAbilityList() {
        return secondaryAbilityList;
    }



    public Ability getAbility() {
        return ability;
    }

    private Cell getStandingTile() {
        if (GridManager.isInBounds(pos()))
            return dispArray[(int) pos().x][(int) pos().y];
        else return new Cell();
    }

    //-----------------------------------------------MISC------------------------------------------------------------------
    public void discard() {
        inv.discard(pos(), true, null);
    }

    public void useAttack(Attack a) {
        int c = a.getCost();
        switch (a.getType()) {
            case Energy:
                st.addEnergy(-c);
                break;
            case Mana:
                st.addMana(-c);
                break;
        }
        setHitBoxShape(a);
    }

    private void setHitBoxShape(Attack a) {
        Attack.HitBoxShape hbs = a.getHitBoxShape();
        switch (hbs) {
            case Rect:
                setAttackBox(a.getHitBox());
                break;
            case Chain:
                setAttackChain(a.getHitChainList());
                break;
            case Triangle:
                setAttackTriangle(a.getHitTri());
                break;
            case None:
                a.runAttackMod();
                break;
        }

    }

    public void unequip(int x) {
        if (inv.ready())
            pickupItem(inv.unequipSlot(x));
    }

    public void scrollItems(boolean b) {
        inv.scroll(b);
    }

    private void regenPlayer() {
        if (infiniteRegen) {
            st.fullHeal();
        } else {
            st.regenModifiers();
        }
    }

    private void calculateArmorBuff() {
        int sum1 = 0, sum2 = 0, sum3 = 0, sum4 = 0, sum5 = 0, sum6 = 0;
        for (Equipment eq : inv.getEquiped()) {
            sum1 += eq.getStrmod();
            sum2 += eq.getDefensemod();
            sum3 += eq.getIntelmod();
            sum4 += eq.getSpeedmod();
            sum5 += eq.getHpmod();
            sum6 += eq.getManamod();
        }
        st.setStrMod(sum1);
        st.setDefMod(sum2);
        st.setIntMod(sum3);
        st.setSpdMod(sum4);
        st.setHpMod(sum5);
        st.setManaMod(sum6);
    }

    public void equipSpell(Item item) {
        player.attackList.add(((SpellBook) item).getAttack());
    }

    //#####MOVE TO INVENTORY
    public void useItem() {
        if (inv.ready()) {
            inv.useItem(this);
        }
    }

    public void useItem(Item item) {
        int[] mods = ((ModItem) item).runMod();
        st.addItemMods(mods,fixed());
        if (item.getClass().equals(Gold.class)) {
            player.setGold(player.getGold() + item.getValue());
            StatManager.totalGold += item.getValue();
            out(st.getName() + " recieved " + item.getValue() + "G");
            new HoverText(item.getValue() + "G", Color.GOLD, fixed(), false);
        }
    }

    public void pickupItem(Item item) {
        item.loadIcon();
        if (isInvEmpty()) {
            Inventory.pos = 0;
        }
        if (item.isGold()) {
            addGold((Gold) item);
        } else {
            lastItem = item;
            boolean added = false;
            for (ArrayList<Item> al : inv.getList()) {
                if (!al.isEmpty()) {
                    try {
                        if (al.get(0).getName().equals(item.getName())) {
                            al.add(item);
                            added = true;
                        }
                    } catch (NullPointerException ignored) {
                    }
                }
            }

            if (!added) {
                inv.addItem(item);
            }

            out(item.getName() + " added to inventory");
            new HoverText(item.getName(), Color.WHITE, fixed(), false);
            StatManager.totalItems++;
            HUD.setLootPopup(item.getIcon());
        }

    }

    //#######
    public void initPlayer() {
        //load icons
        body.init();
        body.setPlayer(this);
        body.setIcons(new Texture[]{pl[0], pl[1], pl[2], pl[3]});
        //load attacks
        attackList.clear();
        out(st.getDefComp()+"");
        ability.onActivate();
        attackList.add(new Dash());

        st.fullHeal();
    }

    public void checkLvlUp() {
        if (exp >= expLimit) {
            Vector2 p = new Vector2(fixed());
            p.add(0, -40);
            new HoverText("--LVL UP--", Color.GREEN, p, true);
            exp = 0;
            st.addLevel();
            st.generateLevelBonus();
            expLimit = (int) ((((Math.pow(1.2, st.getLevel())) * 1000) / 2) - 300);

            abilityPoints++;
            p.add(0, -40);
            new HoverText("+1 Ability Point", Color.GREEN, p, true);
            lvlupEffect = ParticleHandler.loadParticles("ptFlame", abs());
            renderEffect = true;
            lvlupEffect.start();
            st.fullHeal();
        }
    }

    public void addAllAttacks() {
        attackList.clear();
        attackList.add(new Flame());
        attackList.add(new Dash());
        attackList.add(new Illusion());
        attackList.add(new Lightning());
        attackList.add(new Quake());
        attackList.add(new Protect());
        attackList.add(new Focus());
        attackList.add(new Stab());
    }


    public void forceLevelUp() {
        exp += expLimit;
        checkLvlUp();
    }

    public void addShopInvOffset(int i) {
        if (dShopInvScroll.isDone()) {
            shopInvPos += i;
            dShopInvScroll.reset();
        }
    }

    private void clearHitBox() {
        if (dClearHitBox.isDone()) {
            attackBox = new Rectangle(0, 0, 0, 0);
            attackCircle = new Circle();
            attackChain = new ArrayList<>();
            attackTri = new Triangle();
            dClearHitBox.reset();
        }
    }

    private void attackCollision() {
        try {
            for (Monster m : monsterList) {
                if (attackOverlaps(m.body.getHitBox())) {
                    m.takeDamage();
                }
            }
        } catch (ConcurrentModificationException ignored) {
        }
    }

    private void invincibilityFrames(float dt) {
        if (wasHit) {
            if (dInvincibility.isDone()) {
                wasHit = false;
                dInvincibility.reset();
            } else
                dInvincibility.update(dt);
        }
    }

    //UPDATE METHODS------------------------------------------------
    public void update(float dt, Class cls) {
        if (cls.equals(MapState.class)) {
            updateMapState(dt);
        }
    }

    public void updateMapState(float dt) {
        inv.update(dt);
        dClearHitBox.update(dt);
        if (renderEffect)
            lvlupEffect.update(dt);

        swim(dt);
        dig();
        jump(dt);
        clearHitBox();
        invincibilityFrames(dt);
        calculateArmorBuff();
        body.update(dt);
        attackCollision();
        worldCollision();
        regenPlayer();
        st.update();
        Investor.generatePlayerGold();
        isDead(MapState.gsm);
    }

    public void updateShopState(float dt) {
        dShopInvScroll.update(dt);

    }

    private void worldCollision() {
        ArrayList<Cell> list = GridManager.getSurroundingCells(pos(), 1);
        list.removeIf(x -> !(x.hasItem() || x.isWarp() || x.isShop()));
        for (Cell c : list) {
            if (c.hasItem()) {
                out("ITEM");
                Item item = c.getItem();
                if (item != null)
                    item.colliion(c.pos());
            }
            if (c.isWarp()) {
                MapState.warpToNext(true);
            }
            if (c.isShop()) {
                MapState.warpToShop();
                c.setCleared();
            }
        }
    }

    //RENDER METHODS------------------------------------------------
    public void render(SpriteBatch sb) {
        renderEffect(sb);
        sb.draw(body.getIcon(), fixed().x, fixed().y);
    }

    private void renderEffect(SpriteBatch sb) {
        if (renderEffect) {
            lvlupEffect.draw(sb);
            if (lvlupEffect.isComplete())
                lvlupEffect.dispose();
        }
    }

    public void renderStatList(SpriteBatch sb, Vector2 pos) {
        Game.setFontSize(1);
        Game.getFont().setColor(Color.WHITE);
        Vector2[] v = HUD.generateStatListPos(pos);
        ArrayList<String> stats = st.getStatsList(this);
        for (int i = 0; i < stats.size(); i++) {
            //draw all stat icons here
            float x = v[i].x;
            float y = v[i].y;
            if (i > 1 && i < statIcons.size())
                sb.draw(statIcons.get(i), x - 16, y - 14);
            Game.font.draw(sb, stats.get(i), x, y);
        }

        ArrayList<String> a = st.getStatsList(this);
        if (HUD.statsPos != null) {

            for (int i = 0; i < a.size(); i++) {
                if (Inventory.statCompare != null && i - 2 < Inventory.statCompare.length && i - 2 >= 0) {
                    switch (Inventory.statCompare[i - 2]) {
                        case 1: {
                            Game.font.setColor(Color.BLUE);
                            break;
                        }
                        case 2: {
                            Game.font.setColor(Color.RED);
                            break;
                        }
                        default: {
                            Game.font.setColor(Color.WHITE);
                            break;
                        }
                    }
                } else {
                    Game.font.setColor(Color.WHITE);
                }
                Game.getFont().draw(sb, a.get(i), v[i].x, v[i].y);
            }

        }

    }

    public void renderShopInventory(SpriteBatch sb) {
        BitmapFont font = Game.getFont();
        float size = invSize();
        float end = size >= 8 ? 8 : size;
        for (int i = 0; i < end; i++) {
            float x = scrx(2f / 3f);
            float y = scry(.85f - (i * .1f));


            float ind = (i + shopInvPos);
            if (ind < 0) {
                ind = size - 1;
                shopInvPos = (int) (size - 1);
            } else {
                ind %= size;
                shopInvPos %= size;
            }
            ArrayList<Item> itemStack = inv.getStack((int) ind);
            Item item = itemStack.get(0);
            String qty = "x" + itemStack.size();
            String name = (i + 1) + ". " + item.getName();
            String debug = (1 + (int) ind) + "/" + (int) size + " SELL:" + item.getSellPrice();


            font.draw(sb, name, x, y);
            font.draw(sb, qty, scrx(9f / 10f), y);
            font.draw(sb, debug, x, y - 20);
            //font.draw(sb, "_________________________________", x, y - 22);


            sb.draw(item.getIcon(), x, y + 10);
        }
        float per = shopInvPos / size;
        font.draw(sb, "_", scrx(.95f), scry(.86f));

        font.draw(sb, "|", scrx(.95f), scry(.85f - (.79f * per)));
        font.draw(sb, "_", scrx(.95f), scry(.85f - (.79f * ((size - 1) / size))));

    }

    public void renderAttackbox(ShapeRendererExt sr) {
        Attack.HitBoxShape hbs = getAttack().getHitBoxShape();
        if (hbs != null)
            switch (hbs) {

                case Circle:
                    Circle c = getAttackCircle();
                    sr.circle(c.center.x, c.center.y, c.radius);
                    break;
                case Rect:
                    sr.rect(getAttackBox());
                    break;
                case Triangle:
                    sr.triangle(getAttackTriangle());
                    break;
            }
    }

    //MOVEMENT------------------------------------------------------
    private float jump() {
        return 10; //(float) ((625 * dtJump) - (927.5 * Math.pow(dtJump, 2)));
    }

    private void jump(float dt) {
        //jumping=dJump.triggerUpdate(dt,jumping);
    }

    public void move(Vector2 vel) {
        if (!overrideControls) {
            try {
                float dt = Gdx.graphics.getDeltaTime();
                Vector2 abs = abs();
                Vector2 end = new Vector2(abs.x + (vel.x * dt), abs.y + (vel.y * dt));//endpoint of direction vector
                int gridW = cellW * (res + 1);
                Vector2 ic = body.getIconDim();
                float c1 = end.x,
                        c2 = end.x + ic.x,
                        c3 = end.y,
                        c4 = end.y + ic.y;


                if (c1 < 0) {
                    end.x = gridW;
                } else if (c2 > gridW) {
                    end.x = ic.x;
                }


                if (c3 < 0) {
                    end.y = gridW;
                } else if (c4 > gridW) {
                    end.y = ic.y;
                }

                Vector2 comp = Physics.getVector(body.getVelMag(), abs, end);//get movement vector

                if (canMove) {
                    Vector2 start = new Vector2(abs());
                    start.add(comp);
                    body.setAbs(start);

                }
            } catch (ArrayIndexOutOfBoundsException ignored) {

            }
        }
    }

    public void dig() {
        if (getStandingTile().isWall()) {
            int digCost = 20;
            if (st.getEnergy() > digCost) {
                gm.clearArea(pos(), true);
                st.addEnergy(-digCost);
            } else {
                canMove = false;
                new HoverText("-!-", SECOND, Color.YELLOW, player.fixed(), true);
            }
        } else
            canMove = true;

    }

    private void swim(float dt) {
        dWater.update(dt);
        if (getStandingTile().isWater() &&
                notHaveAbility(WaterBreath.class) && dWater.isDone()) {
            st.addHp(-40);
            dWater.reset();
        }
    }

    public int getExp() {
        return exp;
    }

    public Color getDeathShade() {
        return new Color(1,0,0,(1-(st.getHp() / st.getHpMax()))/2.8f);
    }

    public void renderStatBars(ShapeRendererExt sr) {
        sr.setColor(0f, 1f, 0f, 1);
        try {
            Rectangle[] bars = HUD.playerStatBars;
            if (st.getHp() < st.getHpMax() / 2) {
                sr.setColor(1f, 0f, 0f, 1);
            }
            sr.rect(bars[0]);
            sr.setColor(0f, 0f, 1f, 1);
            sr.rect(bars[1]);
            sr.setColor(1f, 1f, 0, 1);
            sr.rect(bars[2]);
        }catch (NullPointerException ignored){}
        sr.end();
    }
}


