package com.quadx.dungeons;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.quadx.dungeons.monsters.Monster;
import com.quadx.dungeons.states.GameStateManager;
import com.quadx.dungeons.states.MainMenuState;
import com.quadx.dungeons.tools.ColorConverter;


public class Game extends ApplicationAdapter {

	public static BitmapFont font;
	private SpriteBatch spriteBatch;
	public static final int WIDTH = 1366;
	public static final int HEIGHT = 724;
	public static Player player= new Player();
	private GameStateManager gameStateManager;

	@Override
	public void create () {
		gameStateManager=new GameStateManager();
		player.addSpell();
		Gdx.graphics.setWindowedMode(WIDTH,HEIGHT);
		setFontSize(20);
		spriteBatch = new SpriteBatch();
		//gameStateManager.push(new Map2State(gameStateManager));
		gameStateManager.push(new MainMenuState(gameStateManager));

		//gameStateManager.push(new AbilitySelectState(gameStateManager));
	}
	public static BitmapFont getFont(){

		return font;
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

	@Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gameStateManager.update(Gdx.graphics.getDeltaTime());
		gameStateManager.render(spriteBatch);
	}
}
