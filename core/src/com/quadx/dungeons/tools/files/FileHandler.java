package com.quadx.dungeons.tools.files;

import com.quadx.dungeons.abilities.*;
import com.quadx.dungeons.commands.Command;
import com.quadx.dungeons.states.AbilitySelectState;
import com.quadx.dungeons.states.HighScoreState;
import com.quadx.dungeons.tools.Score;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Chris Cavazos on 6/29/2018.
 */
public class FileHandler {

    public static void initFile() {

        try {
            FileReader file2 = new FileReader("controls.txt");
            BufferedReader bf2 = new BufferedReader(file2);
            String s2 = bf2.readLine();
            String[] sp = s2.split(" ");
            for (int i = 0; i < sp.length; i++) {
                Command.commands.get(i).changeKey(Integer.parseInt(sp[i]));
            }


            FileReader file = new FileReader("scores.txt");
            BufferedReader bf = new BufferedReader(file);
            String s;
            while ((s = bf.readLine()) != null) {
                if (!s.equals("")) {
                    List<String> split = Arrays.asList(s.split(","));
                    HighScoreState.addScore(new Score(split.get(0), split.get(1), split.get(2), split.get(3), split.get(4)));
                }
            }
            FileReader file3 = new FileReader("data\\abilityDetails.csv");
            BufferedReader bf3 = new BufferedReader(file3);
            String s3;

            //LOAD ABILITY DESCRIPTIONS
            //name, description
            //hpmax,hpreg,Mmax,Mreg,Emax,Ereg,att,def,int,spd
            ArrayList<String> det = new ArrayList<>();
            ArrayList<Ability> abs= new ArrayList<>();
            Tank tank = new Tank();
            Investor inv = new Investor();
            Mage mage = new Mage();
            Quick quick = new Quick();

            int cnt=0;
            for (int i=0;(s3 = bf3.readLine()) != null; i++) {

                List<String> split = Arrays.asList(s3.split(","));
                if(i %6==0){
                    det.addAll(split);
                }else{
                    det.add(s3);
                }

                abs.add(tank);
                abs.add(inv);
                abs.add(mage);
                abs.add(quick);
                if(i %6==5) {
                    abs.get(cnt).setDetails(det);
                    det.clear();
                    cnt++;
                }
            }

            AbilitySelectState.setAbilityList(abs);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
