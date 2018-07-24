package com.quadx.dungeons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.abilities.Ability;
import com.quadx.dungeons.abilities.Investor;
import com.quadx.dungeons.abilities.WaterBreath;
import com.quadx.dungeons.attacks.*;
import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.items.Mine;
import com.quadx.dungeons.items.SpellBook;
import com.quadx.dungeons.items.equipment.Equipment;
import com.quadx.dungeons.items.potions.*;
import com.quadx.dungeons.items.recipes.Recipe;
import com.quadx.dungeons.items.recipes.equipRecipes.*;
import com.quadx.dungeons.items.resources.*;
import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.physics.Body;
import com.quadx.dungeons.physics.Physics;
import com.quadx.dungeons.shapes1_5.Circle;
import com.quadx.dungeons.shapes1_5.Line;
import com.quadx.dungeons.shapes1_5.ShapeRendererExt;
import com.quadx.dungeons.shapes1_5.Triangle;
import com.quadx.dungeons.states.AbilitySelectState;
import com.quadx.dungeons.states.GameStateManager;
import com.quadx.dungeons.states.HighScoreState;
import com.quadx.dungeons.states.State;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.states.mapstate.MapStateRender;
import com.quadx.dungeons.tools.Direction;
import com.quadx.dungeons.tools.StatManager;
import com.quadx.dungeons.tools.Tests;
import com.quadx.dungeons.tools.gui.Drawable;
import com.quadx.dungeons.tools.gui.HUD;
import com.quadx.dungeons.tools.gui.HoverText;
import com.quadx.dungeons.tools.gui.Text;
import com.quadx.dungeons.tools.stats.PlayerStat;
import com.quadx.dungeons.tools.timers.Delta;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

import static com.quadx.dungeons.Game.console;
import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.Game.scr;
import static com.quadx.dungeons.GridManager.*;
import static com.quadx.dungeons.states.mapstate.MapState.*;
import static com.quadx.dungeons.tools.Tests.infiniteRegen;
import static com.quadx.dungeons.tools.gui.HUD.out;
import static com.quadx.dungeons.tools.gui.HUD.statIcons;
import static com.quadx.dungeons.tools.timers.Time.SECOND;
import static com.quadx.dungeons.tools.timers.Time.ft;

/**
 * Created by Tom on 11/9/2015.
 */
@SuppressWarnings("DefaultFileTemplate")
public class Player extends Drawable {


    public final ArrayList<Attack> attackList = new ArrayList<>();
    public final ArrayList<Ability> secondaryAbilityList = new ArrayList<>();
    private ArrayList<Line> attackChain = new ArrayList<>();
    public ArrayList<Potion> activePotions = new ArrayList<>();

    public ArrayList<Recipe> getCraftable() {
        return craftable;
    }

    public void setCraftable(ArrayList<Recipe> craftable) {
        this.craftable = craftable;
    }

    private ArrayList<Recipe> craftable = new ArrayList<>();

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

    public static float multiplier =1;
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
    private boolean fireShield=false;
    public boolean wasHit = false;
    public boolean protect = false;
    public boolean jumping = false;
    boolean overrideControls = false;
    private boolean renderEffect = false;
    private boolean invisible = false;
    private boolean invincible = false;
    private boolean roughSkin = false;


    private double moveSpeed = .1f;
    private int oreCnt = 0;
    private int leatherCnt = 0;


    public Player() {
        st.setLevel(1);
        st.fullHeal();


    }
    public void initPlayer() {
        //load icons
        for(int i=0;i<4;i++)
            gINIT(0,"0"+i);

        body.init();
        body.setPlayer(this);
        // body.setIcons(new Texture[]{pl[0], pl[1], pl[2], pl[3]});

        //load attacks
        //craftable.add(new FireShieldPotionRe());

 /*       craftable.add(new StatPotionRe());

        craftable.add(new InvisibilityPotionRe());
        craftable.add(new LightningShieldPotionRe());

        craftable.add(new HealthPotionRe());
        craftable.add(new ManaPotionRe());
        craftable.add(new EnergyPotionRe());
        craftable.add(new GoldBoostPotionRe());*/
        craftable.add(new ArmRe());
        craftable.add(new BootsRe());
        craftable.add(new CapeRe());
        craftable.add(new ChestRe());
        craftable.add(new GlovesRe());
        craftable.add(new HelmetRe());
        craftable.add(new LegsRe());
        craftable.add(new RingRe());

        attackList.clear();
        out(st.getDefComp() + "");
        ability.onActivate();
        attackList.add(new Dash());

        pickupItem(new RoughSkinPotion());
        pickupItem(new InvincibilityPotion());
        pickupItem(new FireShieldPotion());
        pickupItem(new LightningShieldPotion());
        pickupItem(new InvisibilityPotion());

        st.fullHeal();
    }

    //-----------------------------------------------SETTERS------------------------------------------------------------------
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
        int gain = (int) (a * Math.pow(b, lvl) * factor) / 2;
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
    public void setAimVector(Vector2 end, boolean overrideMouse) {
        Vector2 start = new Vector2();
        if (!overrideMouse) {
            start = new Vector2(scr).scl(.5f);
        }
        double aim = Physics.getAngleRad(start, end);
        body.setFacing(Direction.getDirection(aim));

    }
    public TextureRegion getIcon(){
        String s;
        return getIcon(body.facing);
    }
    //ADDERS------------------------------------------------------------------
    public void addGold(int g) {
        gold += (g * st.getGoldMult());
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
        if (!attackList.isEmpty())
            return attackList.get(Attack.pos);
        else
            return new Flame(false);
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
    public void useAttack(Attack a, boolean free) {
        int c = a.getCost();
        if (!free) {
            switch (a.getType()) {
                case Energy:
                    st.addEnergy(-c);
                    break;
                case Mana:
                    st.addMana(-c);
                    break;
            }
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
        if (item instanceof Resource) {
            int[] mods = ((Resource) item).runMod();
            st.addItemMods(mods, fixed());
            if (item.getClass().equals(Gold.class)) {
                player.setGold(player.getGold() + item.getValue());
                StatManager.totalGold += item.getValue();
                out(st.getName() + " recieved " + item.getValue() + "G");
                new HoverText(item.getValue() + "G", Color.GOLD, fixed(), false);
            }
        }
    }
    public void pickupItem(Item item) {
        if (isInvEmpty()) {
            Inventory.pos = 0;
        }
        if (item.isGold()) {
            addGold((Gold) item);
        } else if (item instanceof Ore) {
            oreCnt++;
        } else if (item instanceof Leather) {
            leatherCnt++;
        } else if (!(item instanceof Mine)) {
            lastItem = item;
            inv.addToInv(item);

            out(item.getName() + " added to inventory");
            new HoverText(item.getName(), Color.WHITE, fixed(), false);
            StatManager.totalItems++;
            HUD.setLootPopup(item.getIcon());
        }

    }
    //#######
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
            //lvlupEffect = ParticleHandler.loadParticles("ptFlame", abs());
            //renderEffect = true;
//            lvlupEffect.start();
            st.fullHeal();
        }
    }
    public void addAllAttacks() {
        attackList.clear();
        attackList.add(new Flame(false));
        attackList.add(new Dash());
        attackList.add(new Illusion());
        attackList.add(new Lightning(false));
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

        activePotions.forEach(x -> x.update(dt));
        for (int i = activePotions.size() - 1; i >= 0; i--) {
            Potion p = activePotions.get(i);
            if (p.remove) {
                activePotions.remove(i);
            }
        }
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
                if (c.getItem() instanceof Mine) {
                    move(Physics.getVector(3, c.abs(), abs()));
                } else if (c.getItem() instanceof Grass) {

                } else
                    c.removeItem();
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
        Color c = new Color(sb.getColor());
        Color start = new Color(c);
        if(invincible){
            c=Color.BLUE;
        }
        if(roughSkin){
            c=Color.RED;
        }

        if (invisible) {
            c.a = .3f;
        } else
            c.a = 1;

        sb.setColor(c);
        TextureRegion t= getIcon();
        try {
            sb.draw(t, fixed().x, fixed().y);
        }catch (NullPointerException e){
            console(sb.toString()+"\n"+t.toString()+"\n"+t.toString());
        }
        c.a = 1;
        sb.setColor(start);

        for (Illusion.Dummy d : Illusion.dummies) {
            sb.draw(d.icon, d.absPos.x, fixY(d.absPos));
        }
    }

    public void renderSR(ShapeRendererExt sr) {
        if ((MapStateRender.oBlink2.getVal() || !wasHit) && GridManager.isInBounds(pos())) {
            //draw player attack box
            sr.setColor(Color.RED);
            renderAttackbox(sr);
            //draw player hitbox
            if (Tests.showhitbox) {
                sr.setColor(Color.BLUE);
                sr.rect(body.getHitBox());
            }
        }
    }
    private void renderEffect(SpriteBatch sb) {
        if (renderEffect) {
            lvlupEffect.draw(sb);
            if (lvlupEffect.isComplete())
                lvlupEffect.dispose();
        }
    }
    public void renderStatList(SpriteBatch sb, Vector2 pos) {
        Text.setFontSize(1);
        Text.getFont().setColor(Color.WHITE);
        Vector2[] v = HUD.generateStatListPos(pos);
        ArrayList<String> stats = st.getStatsList(this);
        int c=0;
        for (int i = 0; i < stats.size(); i++) {
            //draw all stat icons here
            float x = v[i].x;
            float y = v[i].y;
            if (i>1 &&  c < statIcons.size())
                sb.draw(statIcons.get(c++), x - 16, y - 14);
            Text.font.draw(sb, stats.get(i), x, y);
        }

        ArrayList<String> a = st.getStatsList(this);
        if (HUD.statsPos != null) {

            for (int i = 0; i < a.size(); i++) {
                if (Inventory.statCompare != null && i - 2 < Inventory.statCompare.length && i - 2 >= 0) {
                    switch (Inventory.statCompare[i - 2]) {
                        case 1: {
                            Text.font.setColor(Color.BLUE);
                            break;
                        }
                        case 2: {
                            Text.font.setColor(Color.RED);
                            break;
                        }
                        default: {
                            Text.font.setColor(Color.WHITE);
                            break;
                        }
                    }
                } else {
                    Text.font.setColor(Color.WHITE);
                }
                Text.getFont().draw(sb, a.get(i), v[i].x, v[i].y);
            }

        }
        for (int i = 0; i < activePotions.size(); i++) {
            Potion p = activePotions.get(i);
            TextureRegion t = p.getIcon();
            sb.draw(p.getIcon(), v[0].x + (i * (t.getRegionWidth() + 4)), viewY + (scr.y / 4) + t.getRegionHeight() + 5);
        }

    }
    public void renderShopInventory(SpriteBatch sb) {
        BitmapFont font = Text.getFont();
        float size = invSize();
        float end = size >= 8 ? 8 : size;
        for (int i = 0; i < end; i++) {
            float x = scrVx(2f / 3f);
            float y = scrVy(.85f - (i * .1f));


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
            font.draw(sb, qty, scrVx(9f / 10f), y);
            font.draw(sb, debug, x, y - 20);
            //font.draw(sb, "_________________________________", x, y - 22);


            sb.draw(item.getIcon(), x, y + 10);
        }
        float per = shopInvPos / size;
        font.draw(sb, "_", scrVx(.95f), scrVy(.86f));

        font.draw(sb, "|", scrVx(.95f), scrVy(.85f - (.79f * per)));
        font.draw(sb, "_", scrVx(.95f), scrVy(.85f - (.79f * ((size - 1) / size))));

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
            int digCost = 10;
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
        return new Color(1, 0, 0, (1 - (st.getHp() / st.getHpMax())) / 2.8f);
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
        } catch (NullPointerException ignored) {
        }
        sr.end();
    }
    public int getOreCnt() {
        return oreCnt;
    }
    public int getLeatherCnt() {
        return leatherCnt;
    }
    public void addOre() {
        oreCnt++;
    }
    public void addLeather() {
        leatherCnt++;
    }
    public void activatePotion(Potion potion) {
        potion.enabled = true;
        activePotions.add(potion);

    }
    public void setInvisible(boolean b) {
        invisible = b;
    }
    public boolean isInvisible() {
        return invisible;
    }
    public boolean hasFireShield() {
        return fireShield;
    }
    public void setFireShield(boolean b) {
        fireShield=b;
    }
    public void setInvincible(boolean b) {
        invincible=b;
    }
    public void takeDamage(int d, Vector2 abs) {
        d = invincible ? 0 : d;
        st.addHp(-d);
        if (!(invincible || roughSkin))
            body.setKnockBackDest(abs);
        State.shake();
        Color c = new Color(1f, .2f, .2f, 1f);
        new HoverText("-" + d, 1, c, fixed(), true);

    }
    public void setRoughSkin(boolean b) {
        roughSkin=b;
    }
    public boolean isRoughSkin() {
        return roughSkin;
    }
}


