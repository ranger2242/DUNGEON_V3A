package com.quadx.dungeons.commands;

/**
 * Created by Chris Cavazos on 8/8/2016.
 */
public abstract class Command {
    protected String name;
    protected int keyboard;
    protected int controller;
    protected Class cls;
    public abstract void execute();
    public abstract void changeKey(int k);
    public abstract void changeCont(int c);
    public int getButtonK(){return keyboard;}
    public int getButtonC(){return controller;}


}
