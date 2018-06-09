package com.pinnkeyy.gdx.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.pinnkeyy.gdx.GDXGame;
import com.pinnkeyy.gdx.scenes.HUD;
import com.pinnkeyy.gdx.sprites.enemies.Enemy;
import com.pinnkeyy.gdx.sprites.enemies.Goomba;
import com.pinnkeyy.gdx.sprites.Mario;
import com.pinnkeyy.gdx.sprites.items.Item;
import com.pinnkeyy.gdx.sprites.items.ItemDefinition;
import com.pinnkeyy.gdx.sprites.items.Mushroom;
import com.pinnkeyy.gdx.utility.B2WorldCreator;
import com.pinnkeyy.gdx.utility.WorldContactListener;

import java.util.concurrent.LinkedBlockingQueue;

public class PlayScreen implements Screen
{
    private GDXGame game;

    private TextureAtlas atlas;

    private OrthographicCamera camera;
    private Viewport viewPort;
    private static HUD hud;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private Array<Goomba> goombas;
    private World world;
    private Box2DDebugRenderer b2dr;

    private Mario player;

    private Music music;

    private Array<Item> items;
    private LinkedBlockingQueue<ItemDefinition> itemsToSpawn;

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

        goombas = new Array<Goomba>();
        world = new World(new Vector2(0.0f, -10.0f), true);
        b2dr = new Box2DDebugRenderer();

        B2WorldCreator.CreateWorld(this, goombas);

        player = new Mario(this);

        world.setContactListener(new WorldContactListener());

        music = GDXGame.manager.get("audio/music/mario_music.ogg", Music.class);
        music.setLooping(true);
        //music.play();

        items = new Array<Item>();
        itemsToSpawn = new LinkedBlockingQueue<ItemDefinition>();
    }

    public void spawnItem(ItemDefinition iDef)
    {
        itemsToSpawn.add(iDef);
    }

    public void handleSpawningItems()
    {
        if (!itemsToSpawn.isEmpty())
        {
            ItemDefinition iDef = itemsToSpawn.poll();

            if (iDef.type == Mushroom.class)
            {
                items.add(new Mushroom(this, iDef.position.x, iDef.position.y));
            }
        }
    }

    public TextureAtlas getAtlas()
    {
        return atlas;
    }

    @Override
    public void show()
    {

    }

    public static HUD getCurrentHUD()
    {
        return hud;
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
        handleSpawningItems();

        world.step(1.0f / 60.0f, 6, 2);

        player.update(dt);

        for (Enemy enemy : goombas)
        {
            enemy.update(dt);
            if (enemy.getX() < player.getX() + 224 / GDXGame.PPM)
                enemy.body.setActive(true);
        }

        for (Item item : items)
        {
            item.update(dt);
        }

        hud.update(dt);

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

        for (Enemy enemy : goombas)
        {
            enemy.draw(game.getSB());
        }

        for (Item item : items)
        {
            item.draw(game.getSB());
        }

        game.getSB().end();

        game.getSB().setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
    }

    @Override
    public void resize(int width, int height)
    {
        viewPort.update(width, height);
    }

    public TiledMap getMap()
    {
        return map;
    }

    public World getWorld()
    {
        return world;
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
