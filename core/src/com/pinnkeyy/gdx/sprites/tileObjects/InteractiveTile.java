package com.pinnkeyy.gdx.sprites.tileObjects;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.pinnkeyy.gdx.GDXGame;
import com.pinnkeyy.gdx.screens.PlayScreen;

public abstract class InteractiveTile
{
    protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;
    protected Fixture fixture;
    protected PlayScreen screen;

    public InteractiveTile(PlayScreen screen, Rectangle bounds)
    {
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.bounds = bounds;
        this.screen = screen;

        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2.0f) / GDXGame.PPM, (bounds.getY() + bounds.getHeight() / 2.0f) / GDXGame.PPM);

        body = world.createBody(bdef);

        shape.setAsBox((bounds.getWidth() / 2.0f) / GDXGame.PPM, (bounds.getHeight() / 2.0f) / GDXGame.PPM);
        fdef.shape = shape;
        fixture = body.createFixture(fdef);
    }

    public void setCatFilter(short filterBit)
    {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    public TiledMapTileLayer.Cell getCell()
    {
        TiledMapTileLayer layer = (TiledMapTileLayer)map.getLayers().get(1);
        return layer.getCell((int)(body.getPosition().x * GDXGame.PPM / GDXGame.TILE_SIZE), (int)(body.getPosition().y * GDXGame.PPM/ GDXGame.TILE_SIZE));
    }

    public abstract void onHeadHit();
}