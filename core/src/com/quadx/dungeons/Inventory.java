package com.quadx.dungeons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.commands.cellcommands.AddItemComm;
import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.items.equipment.Equipment;
import com.quadx.dungeons.monsters.Monster;

import java.util.ArrayList;

import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.GridManager.*;

/**
 * Created by Chris Cavazos on 2/2/2017.
 */
public class Inventory {
    public static float dtItem = 0;
    public static float itemMinTime=.15f;
    public static float dtInvSwitch = 0;
    public static int pos =0;
    public static int[] statCompare=null;


    public static void selectItemFromInventory() {
        if (dtItem > itemMinTime && player.invList.size() > 0) {            //check if cooldown is over
            player.useItem(pos);                    //actually use the item
            dtItem = 0;                                                     //reset cooldown

            int remove=-1;
            for(ArrayList<Item> arr : player.invList){                      //search for empty list just created
                if(arr.isEmpty()){
                    remove=player.invList.indexOf(arr);                     //get index if any
                }
            }
            if(remove>-1) {                                                 //if empty list is found
                player.invList.remove(remove);                              //remove at index
                if(pos >=player.invList.size()-1){   //reset inventory postion if out of bounds
                    pos =player.invList.size()-1;
                }
            }
        }
    }
    public static void scrollItems(boolean right){
        if((dtInvSwitch > .3)) {
            if (right) {
                if (pos < player.invList.size() - 1)
                    pos++;
                else {
                    pos = 0;
                }
                dtInvSwitch = 0;
            } else {
                if (pos > 0)
                    pos--;
                else {
                }
                pos = player.invList.size() - 1;
                dtInvSwitch = 0;
            }
        }
    }
    public static void discard(Vector2 pos, boolean isPlayer, Monster m) {
        Item item;
        if (isPlayer) {
            if(player.invList.size()>0)
                item = player.invList.get(Inventory.pos).get(0);
            else
                return;
        } else {
            item = Item.generate();
        }
        if (!isPlayer || Inventory.dtItem > Inventory.itemMinTime) {
            try {
                int nx = (int) (pos.x + (rn.nextGaussian() * 2));
                int ny = (int) (pos.y + (rn.nextGaussian() * 2));
                Cell test = dispArray[nx][ny];
                while (test.hasCrate() || !test.isClear() || test.hasLoot() || test.isWarp() || test.isWater()
                        || (nx == pos.x && ny == pos.y)) {
                    nx = (int) (pos.x + (rn.nextGaussian() * 2));
                    ny = (int) (pos.y + (rn.nextGaussian() * 2));
                    test = dispArray[nx][ny];
                }
                Vector2 v = new Vector2(nx, ny);
                Cell c = dispArray[(int) v.x][(int) v.y];
                int index = liveCellList.indexOf(c);
                c.addCommand(new AddItemComm(item, index));
                Anim.pending.add(c);
                int x, y;
                if (isPlayer) {
                    x = (int) player.getAbsPos().x;
                    y = (int) player.getAbsPos().y;
                } else {
                    x = (int) m.getAbsPos().x;
                    y = (int) m.getAbsPos().y;
                }
                Vector2 v2 = new Vector2(x, y);
                Anim.anims.add(new Anim(item.getIcon(), v2, 10, liveCellList.get(index).getAbsPos(), 0));
                if (isPlayer) {
                    player.invList.get(Inventory.pos).remove(0);
                    if (player.invList.get(Inventory.pos).isEmpty()) {
                        player.invList.remove(Inventory.pos);
                        if (Inventory.pos >= player.invList.size())
                            Inventory.pos = player.invList.size();
                    }
                    Inventory.dtItem = 0;
                }
            } catch (IndexOutOfBoundsException e) {
            }
        }

    }
    public static void compareItemToEquipment(){
        try {
            if (!player.invList.isEmpty()) {
                if (player.invList.get(Inventory.pos).get(0).isEquip) {
                    boolean found = false;
                    Equipment eq = (Equipment) player.invList.get(Inventory.pos).get(0);

                    for (Equipment e : player.equipedList) {
                        if (e.getType().equals(player.invList.get(Inventory.pos).get(0).getType())) {
                            statCompare = eq.compare(e);
                            found = true;
                        }
                    }
                    if (!found) statCompare = eq.compare();
                } else {
                    statCompare=null;
                }
            }
            else{
                statCompare=null;
            }
        }catch (IndexOutOfBoundsException e){
            Game.getFont().setColor(Color.WHITE);
        }
    }
    public static void unequip(int x){
        if(x<player.equipedList.size()) {
            Equipment e = player.equipedList.get(x);
            player.equipedList.remove(x);
            player.addItemToInventory(e);
            dtItem =0;
        }
    }
    public static void fixPos(){
        if(pos <0 || pos >player.invList.size()-1){
            pos =0;
        }
    }
}
