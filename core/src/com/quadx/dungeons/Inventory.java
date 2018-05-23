package com.quadx.dungeons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.commands.cellcommands.AddItemToCellComm;
import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.items.equipment.Equipment;
import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.tools.timers.Delta;

import java.util.ArrayList;

import static com.quadx.dungeons.Game.ft;
import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.GridManager.*;

/**
 * Created by Chris Cavazos on 2/2/2017.
 */
public class Inventory {
    public static float dtItem = 0;
    static Delta dUseTime= new Delta(10*ft);
    static Delta dInvSwitch = new Delta(20*ft);
    public static int pos =0;
    public static int[] statCompare=null;

    public static void update(float dt){
        dUseTime.update(dt);
        dInvSwitch.update(dt);

        compareItemToEquipment();
    }

    public static void selectItemFromInventory() {
        if (dUseTime.isDone() && player.invList.size() > 0) {            //check if cooldown is over
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
            dUseTime.reset();
        }
    }

    public static void scrollItems(boolean right) {
        if (dInvSwitch.isDone()) {
            if (right) {
                pos++;
            } else {
                pos--;
            }
            dInvSwitch.reset();
            pos=setInBoundsW(pos,player.invList.size());
        }
    }


    static int discardPos(float ref){
        return (int) (ref + (rn.nextGaussian() * 6));
    }


    public static void discard(Vector2 pos, boolean isPlayer, Monster m) {
        Item item = isPlayer ?
                player.getSelectedItem() : Item.generate();

        if (!isPlayer || dUseTime.isDone()) {
            try {
                Cell cell;
                boolean samePos;
                do {
                    int x = setInBounds(discardPos(pos.x));
                    int y = setInBounds(discardPos(pos.y));
                    cell = dispArray[x][y];
                    samePos=false;
                    ArrayList<Cell> cells= getSurroundingCells(x,y,1);
                    for(Cell c : cells){
                        if(c.getPos().equals(pos))
                            samePos=true;
                    }
                } while (!cell.canPlaceItem() || samePos);

                Vector2 start = new Vector2(isPlayer ? player.getAbsPos() : m.getAbsPos());
                if(item!=null) {
                    Anim a=new Anim(item.getIcon(), start, cell.getAbsPos(),10, Anim.Type.Drop);
                    a.setCell(cell);
                    Anim.anims.add(a);
                    cell.addCommand(new AddItemToCellComm(item, cell));
                }
                if (isPlayer) {
                    player.removeFromInv(Inventory.pos);
                }
                dUseTime.reset();
            } catch (IndexOutOfBoundsException ignored) { }
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
            player.pickupItem(e);
            dtItem =0;
        }
    }
    public static void fixPos(){
        if(pos <0 || pos >player.invList.size()-1){
            pos =0;
        }
    }
}
