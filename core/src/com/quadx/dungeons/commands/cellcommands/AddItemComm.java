package com.quadx.dungeons.commands.cellcommands;

import com.quadx.dungeons.commands.Command;
import com.quadx.dungeons.items.Item;

import static com.quadx.dungeons.GridManager.liveCellList;

/**
 * Created by Chris Cavazos on 1/10/2017.
 */
public class AddItemComm extends Command {
    Item item=null;
    int index;
    public AddItemComm(Item i,int ind){
        item  =i;
        index= ind;
    }
    @Override
    public void execute() {
        if(item !=null){
            liveCellList.get(index).setItem(item);
            liveCellList.get(index).setCrate(true);
        }
    }
}
