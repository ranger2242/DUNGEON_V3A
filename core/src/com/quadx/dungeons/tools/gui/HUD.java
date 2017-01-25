package com.quadx.dungeons.tools.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.quadx.dungeons.Game;
import com.quadx.dungeons.attacks.Attack;
import com.quadx.dungeons.items.Item;
import com.quadx.dungeons.states.HighScoreState;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.states.mapstate.MapStateRender;
import com.quadx.dungeons.tools.ImageLoader;

import java.util.ArrayList;

import static com.quadx.dungeons.Game.*;
import static com.quadx.dungeons.states.mapstate.Map2State.res;
import static com.quadx.dungeons.states.mapstate.MapState.viewX;
import static com.quadx.dungeons.states.mapstate.MapState.viewY;

/**
 * Created by Chris Cavazos on 1/25/2017.
 */
public class HUD {
    static InfoOverlay hud= new InfoOverlay();
    static InfoOverlay invOverlay=new InfoOverlay();
    static InfoOverlay equipOverlay=new InfoOverlay();
    static ArrayList<InfoOverlay> attackBarHud = new ArrayList<>();

    public static Vector2[] statsPos;
    public static Vector2 minimapPos= new Vector2();
    public static Vector2 fpsGridPos = new Vector2();
    static Vector2 equipPos=new Vector2();
    static Vector2 attackBarPos= new Vector2();
    static Vector2 inventoryPos= new Vector2();
    static Vector2 statListPos = new Vector2();
    static Vector2 playerStatBarPos = new Vector2();

    public static ArrayList<Rectangle> equipBoxes= new ArrayList<>();
    public static Rectangle[] playerStatBars=new Rectangle[3];

    public static void create(){
        //reset variables
        equipBoxes.clear();
        hud.texts.clear();
        hud.rects.clear();
        statsPos= new Vector2[player.getStatsList().size()];

        //hud rects
        if (MapState.showStats) {
            hud.rects.add(new Rectangle(viewX, viewY + HEIGHT - 300, 300, 300));
        }
        hud.rects.add(new Rectangle(viewX, viewY, WIDTH, 207));
        //player score
        int x= (int) ((viewX +20));
        String score="SCORE: " + player.getPoints() + "";
        hud.texts.add(new Text(score, new Vector2(x, (viewY + 200)), Color.GRAY, 1));
        String scoreH;
        try {
            scoreH="HIGH SCORE: " + HighScoreState.scores.get(0).getScore();
        }catch (IndexOutOfBoundsException | NullPointerException ex){
            scoreH="HIGH SCORE: 000000";
        }
        hud.texts.add(new Text(scoreH, new Vector2(x+(Game.WIDTH/4)-100, (viewY + 200)), Color.GRAY, 1));
        generateAttackBarUI(attackBarPos);
        generateInventoryUI(inventoryPos);
        generateEquipmentUI(equipPos);
        generateStatListPos(statListPos);
        generatePlayerStatBars(playerStatBarPos);
    }

    public static void update() {
        int x = (int) viewX;
        int y = (int) viewY;
        int w = WIDTH;
        int h = HEIGHT;
        equipPos = new Vector2(new Vector2((x + (w / 3) + 35), y + 130));
        minimapPos = new Vector2(x + w - ((res * 2) + 20), y + 20);
        attackBarPos = new Vector2(x + 20, y + 30);
        inventoryPos = new Vector2((viewX + (WIDTH / 2)), viewY + 130);
        fpsGridPos.set((int) (viewX + (Game.WIDTH * 2 / 3)), viewY + 20);
        statListPos = new Vector2(viewX + 30, viewY + HEIGHT - 30);
        playerStatBarPos= new Vector2(viewX+20,viewY+145);
    }
    static void generateAttackBarUI(Vector2 pos){
        attackBarHud.clear();
        for (int i = 0; i < player.attackList.size(); i++) {
            Attack a = player.attackList.get(i);
            InfoOverlay io=new InfoOverlay();
            int type = a.getType();
            int xoff = (i * 52);
            int x= (int) (pos.x+xoff);
            try {
                if (type == 3 || type == 2 || type==4) {
                    if (player.getMana() >= a.getCost()) {
                        io.textures.add(a.getIcon());
                        io.texturePos.add(new Vector2( x, pos.y+18));
                        if (i <= 7)
                            io.texts.add(new Text( (i + 1) + "", new Vector2( x, pos.y+28),Color.WHITE,1));
                    } else {
                        int rem = a.getCost() - player.getMana();
                        io.texts.add(new Text( rem + "", new Vector2(x + 52 / 2, pos.y + 40),Color.WHITE,1));
                    }
                    io.texts.add(new Text("M" + a.getCost(), new Vector2(x, pos.y),Color.WHITE,1));
                } else if (type == 1) {
                    if (player.getEnergy() >= a.getCost()) {
                        io.textures.add(a.getIcon());
                        io.texturePos.add(new Vector2( x, pos.y+18));
                        if (i <= 7)
                            io.texts.add(new Text( (i + 1) + "", new Vector2( x, pos.y + 28),Color.WHITE,1));
                    } else {
                        int rem = a.getCost() - player.getEnergy();
                        io.texts.add(new Text( rem + "", new Vector2(x + 52 / 2, pos.y + 40),Color.WHITE,1));
                    }
                    io.texts.add(new Text("E" + a.getCost(), new Vector2(x, pos.y),Color.WHITE,1));
                }
                io.texts.add(new Text("Lv." + (a.getLevel() + 1), new Vector2(x, pos.y+18),Color.WHITE,1));
            } catch (NullPointerException ignored) {
            }
            attackBarHud.add(io);
        }
    }
    static void generateInventoryUI(Vector2 pos) {
        //add selected item
        if (!player.invList.isEmpty() && MapStateRender.inventoryPos > -1) {
            try {
                Item item = player.invList.get(MapStateRender.inventoryPos).get(0);
                invOverlay = new InfoOverlay();
                //if (prevItem != item) {
                String name = (MapStateRender.inventoryPos) + ":" + item.getName();
                ArrayList<String> outList = new ArrayList<>();
                if (item.getHpmod() != 0) {
                    outList.add("HP " + item.getHpmod());
                }
                if (item.getManamod() != 0) {
                    outList.add("M :" + item.getManamod());
                }//Mana
                if (item.getEmod() != 0) {
                    outList.add("E :" + item.getEmod());
                }//Mana
                if (item.getAttackmod() != 0) {
                    outList.add("ATT :" + item.getAttackmod());
                }  //attack
                if (item.getDefensemod() != 0) {
                    outList.add("DEF :" + item.getDefensemod());
                } //defense
                if (item.getIntelmod() != 0) {
                    outList.add("INT :" + item.getIntelmod());
                }//intel
                if (item.getSpeedmod() != 0) {
                    outList.add("SPD :" + item.getSpeedmod());
                }//speed
                invOverlay.texts.add(new Text(name, new Vector2(pos.x, pos.y+50), Color.WHITE, 1));
                for (int i = 0; i < outList.size(); i++) {
                    invOverlay.texts.add(new Text(outList.get(i), new Vector2(pos.x, pos.y-((i + 1) * 20)), Color.WHITE, 1));
                }
                invOverlay.texts.add(new Text("x" + player.invList.get(MapStateRender.inventoryPos).size(),pos, Color.WHITE, 1));
                try {
                    invOverlay.textures.add(player.invList.get(MapStateRender.inventoryPos).get(0).getIcon());
                    invOverlay.texturePos.add(pos);

                } catch (Exception e) {
                    invOverlay.textures.add(ImageLoader.crate);
                    invOverlay.texturePos.add(pos);
                }
                //}
            } catch (IndexOutOfBoundsException ignored) {
            }
        }
        if(player.invList.isEmpty()){
            invOverlay= new InfoOverlay();
        }

    }
    static void generateEquipmentUI(Vector2 pos){
        //update equipboxes positions
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                equipBoxes.add(new Rectangle(pos.x + (j * 36), pos.y + (i * 36) - 20, 32, 32));
            }
        }
        //add equipment
        equipOverlay= new InfoOverlay();
        int count = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                if(count<player.equipedList.size()) {
                    int nx= (int) (pos.x + (j * 36));
                    int ny= (int) (pos.y + (i * 36) - 20);
                    try {
                        equipOverlay.textures.add(player.equipedList.get(count).getIcon());
                    }catch (Exception e){
                        equipOverlay.textures.add(ImageLoader.crate);
                    }
                    equipOverlay.texturePos.add(new Vector2(nx , ny));

                    count++;
                }
            }
        }
        invOverlay.add(equipOverlay);
    }
    static void generatePlayerStatBars(Vector2 pos){
            float h=10;
            float barMax = (WIDTH/3)-10;
            float pHPBar = ((float) player.getHp() / (float) player.getHpMax()) * barMax;
            float pManaBar = ((float)player.getMana() / (float)player.getManaMax()) * barMax;
            float pEnergyBar=((float)player.getEnergy()/(float)player.getEnergyMax())*barMax;
            playerStatBars[0]=new Rectangle(pos.x,pos.y+30 , pHPBar, h);
            playerStatBars[1]=new Rectangle(pos.x, pos.y+15, pManaBar, h);
            playerStatBars[2]=new Rectangle(pos.x, pos.y, pEnergyBar, h);
    }
    static void generateStatListPos(Vector2 pos){
        //update player stat list pos
        for(int i=0;i<player.getStatsList().size();i++){
            statsPos[i]=new Vector2(pos.x,pos.y - (i * 20));
        }
    }
    public static Vector2[] getStatPos(){
        return statsPos;
    }
    public static InfoOverlay getInfoOverlay(){
        return hud;
    }
    public static InfoOverlay getInvOverlay(){return invOverlay;}
    public static ArrayList<InfoOverlay> getAttackBarOverlay(){
        return attackBarHud;
    }

}
