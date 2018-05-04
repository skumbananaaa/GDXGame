package com.pinnkeyy.gdx;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pinnkeyy.gdx.screens.PlayScreen;

public class GDXGame extends Game
{
	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 208;
	SpriteBatch sb;
	
	@Override
	public void create()
	{
		sb = new SpriteBatch();
		setScreen(new PlayScreen(this));
	}

	@Override
	public void render()
	{
		super.render();
	}
	
	@Override
	public void dispose()
	{
		sb.dispose();
	}

	public SpriteBatch getSB() { return sb; }
}
