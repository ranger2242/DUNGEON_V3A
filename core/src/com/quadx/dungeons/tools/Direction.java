package com.quadx.dungeons.tools;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Jonas on 1/12/2017.
 */
public class Direction {
    public enum Facing{
        North,Northwest,West,Southwest,South,Southeast,East,Northeast
    }
    public Vector2 getVector(Facing facing){
        switch (facing){

            case North: {
                return new Vector2(0,1);
            }
            case Northwest:{
                return new Vector2(-1,1);
            }
            case West:{
                return new Vector2(-1,0);
            }
            case Southwest:{
                return new Vector2(-1,-1);
            }
            case South:{
                return new Vector2(0,-1);
            }
            case Southeast:{
                return new Vector2(1,-1);
            }
            case East:{
                return new Vector2(1,0);
            }
            case Northeast:{
                return new Vector2(1,1);
            }
            default:return null;
        }
    }
}
