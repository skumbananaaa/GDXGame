package com.pinnkeyy.gdx.utility;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.pinnkeyy.gdx.GDXGame;
import com.pinnkeyy.gdx.sprites.Brick;
import com.pinnkeyy.gdx.sprites.Coin;

public class B2WorldCreator
{
    public static void CreateWorld(World world, TiledMap map)
    {
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;


        //Ground
        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2.0f) / GDXGame.PPM, (rect.getY() + rect.getHeight() / 2.0f) / GDXGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox((rect.getWidth() / 2.0f) / GDXGame.PPM, (rect.getHeight() / 2.0f) / GDXGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        //Pipes
        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2.0f) / GDXGame.PPM, (rect.getY() + rect.getHeight() / 2.0f) / GDXGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox((rect.getWidth() / 2.0f) / GDXGame.PPM, (rect.getHeight() / 2.0f) / GDXGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        //Coins
        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Coin(world, map, rect);
        }

        //Bricks
        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Brick(world, map, rect);
        }

        //Goombas
        /*for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2.0f) / GDXGame.PPM, (rect.getY() + rect.getHeight() / 2.0f) / GDXGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox((rect.getWidth() / 2.0f) / GDXGame.PPM, (rect.getHeight() / 2.0f) / GDXGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }*/

        //Turtles
        /*for (MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class))
        {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2.0f) / GDXGame.PPM, (rect.getY() + rect.getHeight() / 2.0f) / GDXGame.PPM);

            body = world.createBody(bdef);

            shape.setAsBox((rect.getWidth() / 2.0f) / GDXGame.PPM, (rect.getHeight() / 2.0f) / GDXGame.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }*/
    }
}
