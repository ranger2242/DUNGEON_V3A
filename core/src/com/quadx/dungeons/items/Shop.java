package com.quadx.dungeons.items;

import com.quadx.dungeons.Game;
import com.quadx.dungeons.Player;
import com.quadx.dungeons.items.equipment.Equipment;
import com.quadx.dungeons.items.resources.*;

import java.util.ArrayList;


/**
 * Created by Chris Cavazos on 5/25/2018.
 */
public class Shop {
    Player player;
    private static final ArrayList<Item> inv = new ArrayList<>();

    public Shop(Player p){
        player=p;
        genInventory();
    }
    public int sellItem(int i){
        int s=0;
        if (i < player.invSize()) {
            Item item = player.inv.getSelectedItem();
            item.loadIcon();
            s = item.getSellPrice();
            Game.player.addGold( s);
            player.inv.remove(i);
        }
        return s;
    }

   public int invSize(){
        return inv.size();
   }
   public Item getItem(int i){
       return inv.get(i);
   }
    private void genInventory(){
        inv.clear();
        //inv.add(new Potion());
        inv.add(new ManaPlus());
        inv.add(new StrengthPlus());
        inv.add(new DefPlus());
        inv.add(new IntPlus());
        inv.add(new SpeedPlus());
        inv.add(Equipment.generateEquipment());
        inv.add(Equipment.generateEquipment());
        inv.add(new SpellBook());

    }
    public void buyItem(int i) {
        if (i < inv.size()) {
            Item item = inv.get(i);
            if (player.canBuy(item)) {
                player.addGold(-item.getCost());
                player.pickupItem(item);
                if (i >= 6)
                    inv.remove(i);
            }
        }
    }
}
