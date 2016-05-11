package com.quadx.dungeons.tools;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * Created by range on 5/10/2016.
 */
public class FontHandler {
    public static FreeTypeFontGenerator generator;
    public static FreeTypeFontGenerator.FreeTypeFontParameter parameter;

     static BitmapFont font;
    public FontHandler(){
        font=new BitmapFont();

    }
    public static void setFontSize(int x){

		try{
			font.dispose();
			generator.dispose();
		}
		catch (NullPointerException e)
		{

		}
		try {

//			generator= new FreeTypeFontGenerator(Gdx.files.internal("fonts/prstart.ttf"));
			parameter= new FreeTypeFontGenerator.FreeTypeFontParameter();
			parameter.size=x;

			font = generator.generateFont(parameter);
		}
		catch (NullPointerException e)
		{

		}

    }
    public void setColor(ColorConverter c){
        font.setColor(c.getLIBGDXColor());
    }
    public BitmapFont getFont(){
        return font;
    }
}
