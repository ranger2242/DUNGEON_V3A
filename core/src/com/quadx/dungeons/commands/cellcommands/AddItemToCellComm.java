package com.quadx.dungeons.commands.cellcommands;

import com.quadx.dungeons.Cell;
import com.quadx.dungeons.commands.Command;
import com.quadx.dungeons.items.Item;

/**
 * Created by Chris Cavazos on 1/10/2017.
 */
public class AddItemToCellComm extends Command {
    Item item;
    Cell cell;
    public AddItemToCellComm(Item i, Cell c){
        item  =i;
        cell = c;
    }
    @Override
    public void execute() {
        if(item !=null){
            cell.setItem(item);
        }
    }
}
