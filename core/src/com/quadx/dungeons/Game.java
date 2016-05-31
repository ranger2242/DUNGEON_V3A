package com.quadx.dungeons;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.quadx.dungeons.states.GameStateManager;
import com.quadx.dungeons.states.HighScoreState;
import com.quadx.dungeons.states.MainMenuState;
import com.quadx.dungeons.states.mapstate.Map2State;
import com.quadx.dungeons.states.mapstate.MapState;
import com.quadx.dungeons.tools.Score;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class Game extends ApplicationAdapter {

	public static BitmapFont font;
	private SpriteBatch spriteBatch;
	public static final int WIDTH = 1366;
	public static final int HEIGHT = 724;
	public static float frame = .01666f;
	public static Player player= new Player();
	private static GameStateManager gameStateManager;

	@Override
	public void create () {
		initFile();

		gameStateManager=new GameStateManager();
		Gdx.graphics.setWindowedMode(WIDTH,HEIGHT);
		setFontSize(20);
		spriteBatch = new SpriteBatch();
		 gameStateManager.push(new MainMenuState(gameStateManager));
	}
	public static BitmapFont getFont(){

		return font;
	}
	private void initFile(){

		try {
			FileReader file= new FileReader("scores.txt");
			BufferedReader bf = new BufferedReader(file);
			String s;
			while((s=bf.readLine()) != null){
				List<String> split = Arrays.asList(s.split(","));
				HighScoreState.addScore(new Score(split.get(0),split.get(1),split.get(2),split.get(3)));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}//penis
		catch (IOException e) {
			e.printStackTrace();
		}

	}
	public static void console(String s){
		System.out.println(s);
	}
	public static void setFontSize(int x){

		try{
			font.dispose();
			//generator.dispose();
		}
		catch (NullPointerException e)
		{
			//console("Null pointer disposing generator or font");
		}
		try {
			FreeTypeFontGenerator generator= new FreeTypeFontGenerator(Gdx.files.internal("fonts\\prstart.ttf"));
			FreeTypeFontGenerator.FreeTypeFontParameter parameter= new FreeTypeFontGenerator.FreeTypeFontParameter();
			parameter.size = x;
			font = generator.generateFont(parameter);
			//console("Font Generated");
		}
			catch (Exception e){
			e.printStackTrace();
		}
	}
	public static void printLOG(Exception e) {
		/*
		sw=new StringWriter();
		pw2=new PrintWriter(sw);
		e.printStackTrace(pw2);
		pw.append(sw.toString()+"\n");*/
	}
	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gameStateManager.update(Gdx.graphics.getDeltaTime());
		gameStateManager.render(spriteBatch);
	}
}
