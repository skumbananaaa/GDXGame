package com.pinnkeyy.gdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pinnkeyy.gdx.GDXGame;
import com.pinnkeyy.gdx.scenes.HUD;
import com.pinnkeyy.gdx.sprites.Mario;
import com.pinnkeyy.gdx.utility.B2WorldCreator;

public class PlayScreen implements Screen
{
    private GDXGame game;

    private TextureAtlas atlas;

    private OrthographicCamera camera;
    private Viewport viewPort;
    private HUD hud;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private World world;
    private Box2DDebugRenderer b2dr;

    private Mario player;

    public PlayScreen(GDXGame game)
    {
        this.game = game;

        atlas = new TextureAtlas("Mario_and_Enemies.pack");

        camera = new OrthographicCamera();
        viewPort = new FitViewport(GDXGame.V_WIDTH / GDXGame.PPM, GDXGame.V_HEIGHT / GDXGame.PPM, camera);
        hud = new HUD(game.getSB());

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1.0f / GDXGame.PPM);

        camera.position.set(viewPort.getWorldWidth() / 2, viewPort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0.0f, -10.0f), true);
        b2dr = new Box2DDebugRenderer();

        B2WorldCreator.CreateWorld(world, map);

        player = new Mario(world, this);
    }

    public TextureAtlas getAtlas()
    {
        return atlas;
    }

    @Override
    public void show()
    {

    }

    private void handleInput(float dt)
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
            player.body.applyLinearImpulse(new Vector2(0.0f, 4.0f), player.body.getWorldCenter(), true);

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.body.getLinearVelocity().x <= 2.0f)
            player.body.applyLinearImpulse(new Vector2(0.1f, 0.0f), player.body.getWorldCenter(), true);

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.body.getLinearVelocity().x >= -2.0f)
            player.body.applyLinearImpulse(new Vector2(-0.1f, 0.0f), player.body.getWorldCenter(), true);
    }

    public void update(float dt)
    {
        handleInput(dt);

        world.step(1.0f / 60.0f, 6, 2);

        player.update(dt);

        camera.position.x = player.body.getPosition().x;

        camera.update();

        renderer.setView(camera);
    }

    @Override
    public void render(float delta)
    {
        update(delta);

        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

        b2dr.render(world, camera.combined);

        game.getSB().setProjectionMatrix(camera.combined);
        game.getSB().begin();
        player.draw(game.getSB());
        game.getSB().end();

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
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
}
