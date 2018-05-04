package com.pinnkeyy.gdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pinnkeyy.gdx.GDXGame;
import com.pinnkeyy.gdx.scenes.HUD;

public class PlayScreen implements Screen
{
    private GDXGame game;
    OrthographicCamera camera;
    Viewport viewPort;
    HUD hud;

    public PlayScreen(GDXGame game)
    {
        this.game = game;
        camera = new OrthographicCamera();
        viewPort = new FitViewport(GDXGame.V_WIDTH, GDXGame.V_HEIGHT, camera);
        hud = new HUD(game.getSB());
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta)
    {
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.getSB().setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height)
    {
        viewPort.update(width, height);
    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {

    }

    @Override
    public void dispose()
    {

    }
}
