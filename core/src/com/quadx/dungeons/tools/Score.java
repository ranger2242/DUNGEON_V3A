package com.quadx.dungeons.tools;


/**
 * Created by Chris Cavazos on 5/29/2016.
 */
public class Score {
    private String score = null;
    private String name= null;
    private String ability= null;
    private String gold= null;
    private String kills= null;
    public Score(String n, String s, String g, String a,String as){
        score=""+s;
        name=n;
        gold=""+g;
        ability=a;
        kills=as;
    }

    public Score() {

    }

    public String toString(){
        StringBuilder s= new StringBuilder(name);
        while (s.length()<20){
            s.append(" ");
        }
        s.append(score);
        while (s.length()<35){
            s.append(" ");
        }
        s.append(gold).append("G");
        while (s.length()<55){
            s.append(" ");
        }
        s.append(ability);
        while (s.length()<80){
            s.append(" ");
        }
        s.append(kills).append(" KILLS");
        return s.toString();
    }
    public String getSaveFormat(){
        return name+","+score+","+gold+","+ability+","+kills;
    }
    public String getScore(){
        return score;
    }
}
