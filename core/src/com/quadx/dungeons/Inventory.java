package com.quadx.dungeons;

import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.items.equipment.Equipment;
import com.quadx.dungeons.items.resources.*;
import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.tools.timers.Delta;

import java.util.ArrayList;

import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.GridManager.*;
import static com.quadx.dungeons.tools.gui.HUD.out;
import static com.quadx.dungeons.tools.timers.Time.ft;

/**
 * Created by Chris Cavazos on 2/2/2017.
 */
public class Inventory {
    public static Delta dUseTime = new Delta(10 * ft);
    private static Delta dInvSwitch = new Delta(20 * ft);
    public static int pos = 0;
    public static int[] statCompare = null;
    private final ArrayList<ArrayList<Item>> invList = new ArrayList<>();
    private final ArrayList<Equipment> equipedList = new ArrayList<>();

    public void discard(Vector2 p, boolean isPlayer, Monster m) {
        Item item = isPlayer ? getSelectedItem() : Item.generateMonsterDrop();

        if (dUseTime.isDone() && item != null) {
            Cell c = chooseDiscardCell(p);
            Vector2 start = new Vector2(isPlayer ? player.fixed() : m.fixed());
            Anim a = new Anim(item.getIcon(), start, c.fixed(), 10, Anim.Type.Drop);
            a.setCell(c, item);
            Anim.anims.add(a);
            if (isPlayer)
                remove(pos);

            dUseTime.reset();
        }
    }

    public void update(float dt) {
        dUseTime.update(dt);
        dInvSwitch.update(dt);
        fix();
        compareItemToEquipment();
    }

    public void scroll(boolean right) {
        if (dInvSwitch.isDone()) {
            pos += right ? 1 : -1;
            dInvSwitch.reset();
            fix();
        }
    }

    public void useItem(Player p) {
        if (pos >= 0 && pos < invList.size()) {
            Item item = getSelectedItem();
            if (item.isEquip) {
                Equipment e = equipItem(item);
                if (e != null)
                    p.pickupItem(e);
                remove(pos);
            } else if (item.isSpell) {
                p.equipSpell(item);
                remove(pos);
            } else if (item.isUsable) {
                p.useItem(item);
                remove(pos);
            }
        }
    }

    public void clear(){
        invList.clear();
        equipedList.clear();
    }

    public void newStack(Item i) {
        ArrayList<Item> al = new ArrayList<>();
        al.add(i);
        invList.add(al);
    }

    public void addToInv(Item item) {
        boolean added = false;
        for (ArrayList<Item> stack : invList) {
            if (!stack.isEmpty()) {
                try {
                    if (stack.get(0).getName().equals(item.getName())) {
                        stack.add(item);
                        out("ADDED in stack");
                        added = true;
                    }
                } catch (NullPointerException ignored) {
                }
            }
        }
        if (!added) {
            newStack(item);
            out("ADDED new stack");
        }
    }

    public Equipment equipItem(Item item) {
        Equipment temp = getEquipedByType(item.getType());
        if (temp != null) {
            equipedList.remove(temp);
            return temp;
        }
        equipedList.add((Equipment) item);
        return null;
    }

    public void remove(int ind) {
        if(ind<invList.size()) {
            invList.get(ind).remove(0);
            if (invList.get(ind).isEmpty()) {
                removeItemStack(ind);
                pos = GridManager.bound(ind, invList.size());
            }
        }
    }

    public boolean ready() {
        if (dUseTime.isDone()) {
            dUseTime.reset();
            fix();
            return true;
        }
        return false;
    }

    public Item getSelectedItem() {
        if (invList.size() > 0)
            return invList.get(pos).get(0);
        else
            return null;
    }

    public Equipment unequipSlot(int x) {
        x = bound(x, equipedList.size());
        if (equipedList.isEmpty()) {
            Equipment e = equipedList.get(x);
            equipedList.remove(x);
            return e;
        }
        return null;
    }

    public ArrayList<ArrayList<Item>> getList() {
        return invList;
    }

    public ArrayList<Equipment> getEquiped() {
        return equipedList;
    }

    public ArrayList<Item> getStack(int i) {
        if (!invList.isEmpty()) {
            i = bound(i, invList.size());
            return invList.get(i);
        } else
            return new ArrayList<>();
    }

    public ArrayList<Item> getSelectedStack() {
        return getStack(pos);
    }

    private void removeItemStack(int i) {
        invList.remove(i);
    }

    private void compareItemToEquipment() {
        Item item = getSelectedItem();
        if (item != null && item.isEquip) {
            Equipment it = (Equipment) item;
            Equipment eq = getEquipedByType(item.getType());
            statCompare = eq == null ? it.compare() : it.compare(eq);
        } else {
            statCompare = null;
        }
    }

    private void fix() {
        pos = boundW(pos, player.invSize());

    }

    private static int f(float spread) {
        return rn.nextInt((int) spread) * (rn.nextBoolean() ? -1 : 1);
    }

    public static Cell chooseDiscardCell(Vector2 p) {
        Cell cell;
        boolean samePos;
        do {
            Vector2 n = new Vector2(cluster(p, 6));
            cell = dispArray(n);
            samePos = isNearPlayer(n, 2);
        } while (!cell.canPlaceItem() || samePos);
        return cell;
    }

    private static Vector2 cluster(Vector2 ref, float spread) {
        ref.add(f(spread), f(spread));
        return bound(ref);
    }

    private Equipment getEquipedByType(String type) {
        for (Equipment e : equipedList) {
            if (e.getType().equals(type)) {
                return e;
            }
        }
        return null;
    }

    private int stackCountByType(Class c) {
        for (ArrayList list : invList) {
            if (list.get(0).getClass().equals(c))
                return list.size();
        }
        return 0;
    }
    private int stackCountByType(String name) {
        for (ArrayList<Item> list : invList) {
            if (list.get(0).getName().equals(name))
                return list.size();
        }
        return 0;
    }

    public int[] getCraftStacks() {
        int[] arr = new int[]{
                player.getOreCnt(),
                player.getLeatherCnt(),
                stackCountByType(StrengthPlus.class),
                stackCountByType(DefPlus.class),
                stackCountByType(IntPlus.class),
                stackCountByType(SpeedPlus.class),
                stackCountByType(Hypergem.class),
                stackCountByType(Heart.class),
                stackCountByType("SMALL CRYSTAL" ),
                stackCountByType("MEDIUM CRYSTAL"),
                stackCountByType("LARGE CRYSTAL" ),
                stackCountByType("SMALL MEAT" ),
                stackCountByType("MEDIUM MEAT"),
                stackCountByType("LARGE MEAT" ),
                stackCountByType(Blood.class),


        };
        return arr;
    }

    public void removeStack(int i) {
        invList.remove(i);
    }

    public boolean hasStack(Item resource) {
        for(ArrayList list: invList){
            if(list.get(0).getClass().equals(resource.getClass()))
                return true;
        }
        return false;
    }

    public int getStackSize(Class c){
        for(ArrayList list: invList){
            if(list.get(0).getClass().equals(c))
                return list.size();
        }
        return 0;
    }

    public ArrayList<Item> getStackByType(Class c) {
        for(ArrayList list: invList){
            if(list.get(0).getClass().equals(c))
                return list;
        }
        return null;
    }
}
