package com.quadx.dungeons.items;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.Anim;
import com.quadx.dungeons.Cell;
import com.quadx.dungeons.Inventory;
import com.quadx.dungeons.items.resources.*;
import com.quadx.dungeons.tools.timers.Delta;

import java.util.ArrayList;
import java.util.Collections;

import static com.quadx.dungeons.Game.player;
import static com.quadx.dungeons.Game.rn;
import static com.quadx.dungeons.tools.timers.Time.ft;

/**
 * Created by Chris Cavazos on 5/28/2018.
 */
public class Mine extends Item {

    enum Type{
        STR, DEF, INT, SPD, ORE
    }

    ArrayList<Item> items=  new ArrayList<>();
    Type type=null;
    Delta dBreak = new Delta(7*ft);
    public boolean kill=false;

    public Mine(Vector2 abs) {
        name="Mine";
        body.setAbs(abs);
        gINIT(1,"icMine");
    }

    public void genItems(){
        type= Type.values()[rn.nextInt(Type.values().length)];
        int n1=rn.nextInt(2)+2;
        int n2=rn.nextInt(4);
        int n3=rn.nextInt(3)+1;

        if(rn.nextFloat()<1f/3f){
            items.add(new Crystal());
        }
        if(rn.nextFloat()<.025f){
            items.add(new Hypergem());
        }

        for(int i=0;i<n1;i++){
            Item item=null;
            switch (type){
                case STR:
                    item=new StrengthPlus();
                    break;
                case DEF:
                    item=new DefPlus();
                    break;
                case INT:
                    item=new IntPlus();
                    break;
                case SPD:
                    item=new SpeedPlus();
                    break;
                case ORE:
                    item=new Ore();
                    break;
            }
            items.add(item);
        }
  /*      for(int i=0;i<n2;i++){
            itemsAt.add(new Gold());
        }*/
        for(int i=0;i<n3;i++){
            items.add(new Ore());
        }
        Collections.shuffle(items);

    }

    Item dropItem() {
        if (!items.isEmpty()) {
            Item item = items.get(0);
            items.remove(0);
            Cell c = Inventory.chooseDiscardCell(body.getPos());
            Anim a = new Anim(item.getIcon(), body.getFixedPos(), c.fixed(), 10, Anim.Type.Drop);
            a.setCell(c, item);
            Anim.anims.add(a);

            return item;
        }else
            return null;
    }

    void onBreak(){

    }
    public void update(float dt){
        dBreak.update(dt);
        if(!kill&& player.attackOverlaps(body.getHitBox()) && dBreak.isDone()){
            dropItem();
            dBreak.reset();
        }
        if(items.size()==0){
            body=null;
            kill=true;
        }
    }

    public void render(SpriteBatch sb){
        if(!kill) {
            Vector2 p = body.getFixedPos();
            TextureRegion t=getIcon();
            if(t!=null)
                sb.draw(t, p.x, p.y);
        }
    }
}
